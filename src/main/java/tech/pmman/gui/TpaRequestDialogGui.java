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
import lombok.Data;
import tech.pmman.util.PlayerTool;

import javax.annotation.Nonnull;

public class TpaRequestDialogGui extends InteractiveCustomUIPage<TpaRequestDialogGui.TpaRequestDialogGuiData> {

    public TpaRequestDialogGui(@Nonnull PlayerRef playerRef) {
        super(playerRef, CustomPageLifetime.CanDismiss, TpaRequestDialogGuiData.CODEC);
    }

    @Override
    public void build(@Nonnull Ref<EntityStore> ref, @Nonnull UICommandBuilder uiCommandBuilder, @Nonnull UIEventBuilder uiEventBuilder, @Nonnull Store<EntityStore> store) {
        uiCommandBuilder.append("Pages/Tpa/Tpa_Request_Dialog.ui");
        uiCommandBuilder.set("#RequestMessage.Text", Message.translation("tpaDialog.content")
                                                     .param("player", PlayerTool.getPlayerDisplayName(ref)));
        uiEventBuilder.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#AcceptButton",
                EventData.of("Action", "CONFIRM"),
                false
        );
        uiEventBuilder.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#DenyButton",
                EventData.of("Action", "CANCEL"),
                false
        );
    }

    @Override
    public void handleDataEvent(@Nonnull Ref<EntityStore> playerRef, @Nonnull Store<EntityStore> store, @Nonnull TpaRequestDialogGuiData data) {
        Player player = PlayerTool.getPlayerFromRef(playerRef);
        if ("CONFIRM".equals(data.getAction())) {
            player.sendMessage(Message.raw("CONFIRM"));
        } else if ("CANCEL".equals(data.getAction())) {
            player.sendMessage(Message.raw("CANCEL"));
            close();
        }
    }

    @Data
    public static class TpaRequestDialogGuiData {
        private String action;

        public static final BuilderCodec<TpaRequestDialogGuiData> CODEC =
                BuilderCodec.builder(TpaRequestDialogGuiData.class, TpaRequestDialogGuiData::new)
                            .append(new KeyedCodec<>(
                                            "Action", Codec.STRING),
                                    (o, d) -> o.action = d,
                                    o -> o.action)
                            .add()
                            .build();
    }
}
