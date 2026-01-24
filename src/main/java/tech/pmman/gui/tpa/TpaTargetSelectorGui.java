package tech.pmman.gui.tpa;

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
    private final PermissionsModule permissionsModule;

    public TpaTargetSelectorGui(@Nonnull PlayerRef playerRef) {
        super(playerRef, CustomPageLifetime.CanDismiss, TpaTargetSelectorGuiData.CODEC);
        permissionsModule = PermissionsModule.get();
    }

    @Override
    public void build(@Nonnull Ref<EntityStore> ref, @Nonnull UICommandBuilder uiCommandBuilder, @Nonnull UIEventBuilder uiEventBuilder, @Nonnull Store<EntityStore> store) {
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
        // 预处理数据
        preDataFilter(players);
        for (int i = 0; i < players.size(); i++) {
            PlayerRef iRef = players.get(i);
            assert iRef.getWorldUuid() != null;
            UUID targetUUID = iRef.getUuid();
            String path = "#PlayerCards[" + i + "]";
            // 插入entry
            insertEntryUi(uiCommandBuilder, iRef, path);
            // 设置entry具体状态
            setEntryState(uiCommandBuilder, store, ref, targetUUID, path);
            uiEventBuilder.addEventBinding(
                    CustomUIEventBindingType.Activating,
                    path + " #TpaButton",
                    new EventData()
                            .append("Action", "TPA")
                            .append("TargetUUID", targetUUID.toString()),
                    false
            );
            uiEventBuilder.addEventBinding(
                    CustomUIEventBindingType.Activating,
                    path + " #TpahereButton",
                    new EventData()
                            .append("Action", "TPAHERE")
                            .append("TargetUUID", targetUUID.toString()),
                    false
            );
        }
    }

    private void preDataFilter(List<PlayerRef> players) {
//        players.removeIf(e -> e.getUuid()
//                               .equals(playerRef.getUuid()));
    }

    private void insertEntryUi(UICommandBuilder uiCommandBuilder, PlayerRef iRef, String path) {
        assert iRef.getWorldUuid() != null;
        String worldName = Objects.requireNonNull(Universe.get()
                                                          .getWorld(iRef.getWorldUuid()))
                                  .getName();
        uiCommandBuilder.append("#PlayerCards", "Pages/Tpa/Tpa_Target_Player_Entry.ui");
        uiCommandBuilder.set(path + " #Name.Text", iRef.getUsername());
        uiCommandBuilder.set(path + " #WorldName.Text", Message.translation("tpaSelector.worldName")
                                                               .param("worldName", worldName));
    }

    private void setEntryState(UICommandBuilder uiCommandBuilder, Store<EntityStore> store, Ref<EntityStore> ref,
                               UUID targetUUID, String path) {
        // 如果不能请求，则将按钮置灰()
        int requestCd = TpaService.getRequestCdWithRemoveExpiredRequest(PlayerTool.getUUID(store, ref), targetUUID);
        // 如果有bypass权限，则跳过
        if (requestCd > 0 &&
                !permissionsModule.hasPermission(playerRef.getUuid(), "simpleess.command.tpa.cooldown.bypass")) {
            uiCommandBuilder.set(path + " #TpaButton.Disabled", true);
            uiCommandBuilder.set(path + " #TpaButton.Text", Message.raw("tpaSelector.requestCd")
                                                                   .param("requestCd", requestCd));
        }
        // 获取目标玩家tpa设置
        PlayerTpaSettings targetSettings = PlayerTpaSettingsService.getSettings(targetUUID
                .toString());
        // 玩家关闭了tpa功能也要置灰
        if (targetSettings.isDisableTpa()) {
            uiCommandBuilder.set(path + " #TpaButton.Disabled", true);
        }
    }

    @Override
    public void handleDataEvent(@Nonnull Ref<EntityStore> playerRef, @Nonnull Store<EntityStore> store, @Nonnull TpaTargetSelectorGuiData data) {
        if ("CANCEL".equals(data.getAction())) {
            close();
        }
        if (data.getTargetUUID() != null) {
            if ("TPA".equals(data.getAction())) {
                // 发送传送请求
                TpaService.sendTpaRequestWithClear(PlayerTool.getUUID(store, playerRef), data.getTargetUUID());
                close();
            }
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
