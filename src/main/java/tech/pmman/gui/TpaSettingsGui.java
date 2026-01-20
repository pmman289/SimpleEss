package tech.pmman.gui;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import lombok.Data;
import tech.pmman.ConfigManager;
import tech.pmman.pojo.PlayerTpaSettingsConfigEntry;
import tech.pmman.util.PlayerTool;

import javax.annotation.Nonnull;
import java.util.Map;

public class TpaSettingsGui extends InteractiveCustomUIPage<TpaSettingsGui.TpaSettingsGuiData> {
    private final PlayerTpaSettingsConfigEntry settings;

    public TpaSettingsGui(@Nonnull PlayerRef playerRef) {
        super(playerRef, CustomPageLifetime.CanDismiss, TpaSettingsGuiData.CODEC);
        // 为玩家创建配置文件
        Map<String, PlayerTpaSettingsConfigEntry> playerSettings = ConfigManager.PLAYER_TPA_SETTINGS_DATA.get()
                                                                                                         .getPlayerSettings();
        if (playerSettings.get(playerRef.getUuid()
                                        .toString()) == null) {
            playerSettings.put(playerRef.getUuid()
                                        .toString(), new PlayerTpaSettingsConfigEntry());
        }
        settings = playerSettings.get(playerRef.getUuid()
                                               .toString());
    }

    @Override
    public void build(@Nonnull Ref<EntityStore> ref, @Nonnull UICommandBuilder uiCommandBuilder, @Nonnull UIEventBuilder uiEventBuilder, @Nonnull Store<EntityStore> store) {
        uiCommandBuilder.append("Pages/Tpa/Tpa_Settings.ui");
        uiCommandBuilder.set("#EnableAutoDeny #CheckBox.Value", settings.isEnableAutoDeny());
        uiCommandBuilder.set("#EnableAutoAccept #CheckBox.Value", settings.isEnableAutoAccept());
        uiCommandBuilder.set("#EnableDisableTpa #CheckBox.Value", settings.isDisableTpa());
        uiEventBuilder.addEventBinding(
                CustomUIEventBindingType.ValueChanged,
                "#EnableAutoDeny #CheckBox",
                EventData.of("@EnableAutoDeny", "#EnableAutoDeny #CheckBox.Value"),
                false
        );
        uiEventBuilder.addEventBinding(
                CustomUIEventBindingType.ValueChanged,
                "#EnableAutoAccept #CheckBox",
                EventData.of("@EnableAutoAccept", "#EnableAutoAccept #CheckBox.Value"),
                false
        );
        uiEventBuilder.addEventBinding(
                CustomUIEventBindingType.ValueChanged,
                "#EnableDisableTpa #CheckBox",
                EventData.of("@DisableTpa", "#EnableDisableTpa #CheckBox.Value"),
                false
        );
        uiEventBuilder.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#BackButton",
                EventData.of("Action", "BACK")
        );
    }

    @Override
    public void handleDataEvent(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store, @Nonnull TpaSettingsGuiData data) {
        if ("BACK".equals(data.getAction())) {
            PlayerTool.getPlayerFromRef(ref)
                      .getPageManager()
                      .openCustomPage(ref, store, new TpaRequestManagerGui(playerRef));
        }
        // 解决冲突选项
        if (data.getEnableAutoAccept() != null) {
            if (data.getEnableAutoAccept()) {
                data.setEnableAutoDeny(false);
                settings.setEnableAutoDeny(data.getEnableAutoDeny());
            }
            settings.setEnableAutoAccept(data.getEnableAutoAccept());
        }
        if (data.getEnableAutoDeny() != null) {
            if (data.getEnableAutoDeny()) {
                data.setEnableAutoAccept(false);
                settings.setEnableAutoAccept(data.getEnableAutoAccept());
            }
            settings.setEnableAutoDeny(data.getEnableAutoDeny());
        }
        if (data.getDisableTpa() != null) {
            settings.setDisableTpa(data.getDisableTpa());
        }
    }

    @Data
    public static class TpaSettingsGuiData {
        private String action;
        private Boolean enableAutoAccept;
        private Boolean enableAutoDeny;
        private Boolean disableTpa;

        public static final BuilderCodec<TpaSettingsGuiData> CODEC =
                BuilderCodec.builder(TpaSettingsGuiData.class, TpaSettingsGuiData::new)
                            .append(new KeyedCodec<>("@EnableAutoDeny", Codec.BOOLEAN),
                                    (d, b) -> d.enableAutoDeny = b,
                                    d -> d.enableAutoDeny)
                            .add()
                            .append(new KeyedCodec<>("@EnableAutoAccept", Codec.BOOLEAN),
                                    (d, b) -> d.enableAutoAccept = b,
                                    d -> d.enableAutoAccept)
                            .add()
                            .append(new KeyedCodec<>("@DisableTpa", Codec.BOOLEAN),
                                    (d, b) -> d.disableTpa = b,
                                    d -> d.disableTpa)
                            .add()
                            .append(
                                    new KeyedCodec<>("Action", Codec.STRING),
                                    (d, s) -> d.action = s,
                                    d -> d.action
                            )
                            .add()
                            .build();
    }
}
