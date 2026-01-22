package tech.pmman.gui;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.permissions.PermissionsModule;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import lombok.Data;
import tech.pmman.pojo.db.PlayerTpaSettings;
import tech.pmman.service.PlayerTpaSettingsService;
import tech.pmman.service.TpaService;
import tech.pmman.util.PlayerTool;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class TpaTargetSelectorGui extends InteractiveCustomUIPage<TpaTargetSelectorGui.TpaTargetSelectorGuiData> {

    public TpaTargetSelectorGui(@Nonnull PlayerRef playerRef) {
        super(playerRef, CustomPageLifetime.CanDismiss, TpaTargetSelectorGuiData.CODEC);
    }

    @Override
    public void build(@Nonnull Ref<EntityStore> ref, @Nonnull UICommandBuilder uiCommandBuilder, @Nonnull UIEventBuilder uiEventBuilder, @Nonnull Store<EntityStore> store) {
        PermissionsModule permissionsModule = PermissionsModule.get();
        uiCommandBuilder.append("Pages/Tpa/Tpa_Target_Player_Selector.ui");
        uiEventBuilder.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#CancelButton",
                EventData.of("Action", "CANCEL"),
                false
        );
        // 填充玩家列表
        List<PlayerRef> players = Universe.get()
                                          .getPlayers();
        players.removeIf(e -> e.getUuid()
                               .equals(playerRef.getUuid()));
        for (int i = 0; i < players.size(); i++) {
            PlayerRef iRef = players.get(i);
            UUID targetUUID = iRef.getUuid();
            assert iRef.getWorldUuid() != null;
            String worldName = Objects.requireNonNull(Universe.get()
                                                              .getWorld(iRef.getWorldUuid()))
                                      .getName();
            uiCommandBuilder.append("#PlayerCards", "Pages/Tpa/Tpa_Target_Player_Entry.ui");
            String path = "#PlayerCards[" + i + "]";
            uiCommandBuilder.set(path + " #Name.Text", iRef.getUsername());
            uiCommandBuilder.set(path + " #WorldName.Text", Message.translation("tpaSelector.worldName")
                                                                   .param("worldName", worldName));
            // 如果不能请求，则将按钮置灰()
            int requestCd = TpaService.getRequestCdWithRemoveExpiredRequest(PlayerTool.getUUID(store, ref), targetUUID);
            // 如果有bypass权限，则跳过
            if (requestCd > 0 &&
                    !permissionsModule.hasPermission(playerRef.getUuid(), "simpleess.command.tpa.cooldown.bypass")) {
                uiCommandBuilder.set(path + ".Disabled", true);
                uiCommandBuilder.set(path + " #RequestCd.Text", Message.translation("tpaSelector.requestCd")
                                                                       .param("requestCd", requestCd));
            }
            // 获取目标玩家tpa设置
            PlayerTpaSettings targetSettings = PlayerTpaSettingsService.getSettings(targetUUID
                                                                                       .toString());
            // 玩家关闭了tpa功能也要置灰
            if (targetSettings.isDisableTpa()) {
                uiCommandBuilder.set(path + ".Disabled", true);
            }
            uiEventBuilder.addEventBinding(
                    CustomUIEventBindingType.Activating,
                    path,
                    EventData.of("TargetUUID", targetUUID
                                                   .toString()),
                    false
            );
        }
    }

    @Override
    public void handleDataEvent(@Nonnull Ref<EntityStore> playerRef, @Nonnull Store<EntityStore> store, @Nonnull TpaTargetSelectorGuiData data) {
        if ("CANCEL".equals(data.getAction())) {
            close();
        }
        if (data.getTargetUUID() != null) {
            // 发送传送请求
            TpaService.sendTpaRequestWithClear(PlayerTool.getUUID(store, playerRef), data.getTargetUUID());
            close();
        }
    }

    @Data
    public static class TpaTargetSelectorGuiData {
        private String action;
        private UUID targetUUID;

        public static final BuilderCodec<TpaTargetSelectorGuiData> CODEC =
                BuilderCodec.builder(TpaTargetSelectorGuiData.class, TpaTargetSelectorGuiData::new)
                            .append(new KeyedCodec<>(
                                            "TargetUUID", Codec.UUID_STRING),
                                    (o, d) -> o.targetUUID = d,
                                    o -> o.targetUUID)
                            .add()
                            .append(new KeyedCodec<>(
                                            "Action", Codec.STRING),
                                    (o, d) -> o.action = d,
                                    o -> o.action
                            )
                            .add()
                            .build();
    }
}
