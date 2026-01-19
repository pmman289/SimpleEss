package tech.pmman.gui;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import tech.pmman.pojo.TpaRequestData;
import tech.pmman.service.TpaManager;
import tech.pmman.util.PlayerTool;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class TpaRequestManagerGui extends InteractiveCustomUIPage<TpaRequestManagerGui.TpaRequestManagerGuiData> {

    public TpaRequestManagerGui(@Nonnull PlayerRef playerRef) {
        super(playerRef, CustomPageLifetime.CanDismiss, TpaRequestManagerGuiData.CODEC);
    }

    @Override
    public void build(@Nonnull Ref<EntityStore> ref, @Nonnull UICommandBuilder uiCommandBuilder, @Nonnull UIEventBuilder uiEventBuilder, @Nonnull Store<EntityStore> store) {
        uiCommandBuilder.append("Pages/Tpa/Tpa_Request_Manager.ui");

        // 1. 获取当前玩家收到的所有请求
        UUID myUuid = playerRef.getUuid();
        List<TpaRequestData> requests = TpaManager.getTargetAllRequestList(myUuid);
        if (requests == null) {
            requests = Collections.emptyList();
        }

        for (int i = 0; i < requests.size(); i++) {
            TpaRequestData req = requests.get(i);
            String path = "#RequestCards[" + i + "]";

            uiCommandBuilder.append("#RequestCards", "Pages/Tpa/Tpa_Request_Manager_Entry.ui");
            String requesterName = PlayerTool.getNameByUUID(req.getRequestPlayer());
            uiCommandBuilder.set(path + " #RequesterName.Text", requesterName);
            uiCommandBuilder.set(path + " #RequestTime.Text", Message.translation("tpaRequestManager.requestTime")
                                                                     .param("requestTime", TpaManager.getRequestCdWithRemoveExpiredRequest(req.getRequestPlayer(), myUuid)));
            // 2. 绑定批准事件
            uiEventBuilder.addEventBinding(
                    CustomUIEventBindingType.Activating,
                    path + " #AcceptButton",
                    EventData.of("AcceptRequesterUUID", req.getRequestPlayer()
                                                           .toString()),
                    false
            );

            // 3. 绑定拒绝事件
            uiEventBuilder.addEventBinding(
                    CustomUIEventBindingType.Activating,
                    path + " #DenyButton",
                    EventData.of("DenyRequesterUUID", req.getRequestPlayer()
                                                         .toString()),
                    false
            );
        }

        // 4. 绑定关闭按钮
        uiEventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#CloseButton", EventData.of("Action", "CLOSE"), false);

        // 绑定设置按钮
        uiEventBuilder.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#SettingsButton",
                EventData.of("Action", "SETTINGS"),
                false
        );
    }

    @Override
    public void handleDataEvent(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store, @Nonnull TpaRequestManagerGuiData data) {
        if ("CLOSE".equals(data.getAction())) {
            close();
        }
        if ("SETTINGS".equals(data.getAction())) {
            Player player = PlayerTool.getPlayerFromRef(ref);
            player.getPageManager()
                  .openCustomPage(ref, store, new TpaSettingsGui(playerRef));
        }
        if (data.getAcceptRequesterUuid() != null) {
            TpaManager.acceptTpaRequest(data.getAcceptRequesterUuid(), playerRef.getUuid());
            close();
        } else if (data.getDenyRequesterUuid() != null) {
            TpaManager.denyTpaRequest(data.getDenyRequesterUuid(), playerRef.getUuid());
            close();
        }
    }

    @lombok.Data
    public static class TpaRequestManagerGuiData {
        public static final BuilderCodec<TpaRequestManagerGuiData> CODEC =
                BuilderCodec.builder(TpaRequestManagerGuiData.class, TpaRequestManagerGuiData::new)
                            .append(new KeyedCodec<>(
                                    "Action", Codec.STRING), (d, s) -> d.action = s, d -> d.action
                            )
                            .add()
                            .append(new KeyedCodec<>(
                                    "AcceptRequesterUUID", Codec.UUID_STRING), (d, s) -> d.acceptRequesterUuid = s, d -> d.acceptRequesterUuid
                            )
                            .add()
                            .append(new KeyedCodec<>(
                                    "DenyRequesterUUID", Codec.UUID_STRING), (d, s) -> d.denyRequesterUuid = s, d -> d.denyRequesterUuid
                            )
                            .add()
                            .build();
        private String action;
        private UUID acceptRequesterUuid;
        private UUID denyRequesterUuid;
    }
}