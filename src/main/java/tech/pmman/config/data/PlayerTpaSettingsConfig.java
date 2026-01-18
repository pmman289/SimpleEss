package tech.pmman.config.data;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.map.MapCodec;
import lombok.Data;
import tech.pmman.pojo.PlayerTpaSettingsConfigEntry;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
public class PlayerTpaSettingsConfig {
    private Map<String, PlayerTpaSettingsConfigEntry> playerSettings = new HashMap<>();

    public boolean getDisableTpa(UUID uuid) {
        PlayerTpaSettingsConfigEntry settings = playerSettings.get(uuid.toString());
        if (settings == null) {
            playerSettings.put(uuid.toString(), new PlayerTpaSettingsConfigEntry());
            return false;
        }
        return settings.isDisableTpa();
    }

    public boolean getAutoAccept(UUID uuid) {
        PlayerTpaSettingsConfigEntry settings = playerSettings.get(uuid.toString());
        if (settings == null) {
            playerSettings.put(uuid.toString(), new PlayerTpaSettingsConfigEntry());
            return false;
        }
        return settings.isEnableAutoAccept();
    }

    public boolean getAutoDeny(UUID uuid) {
        PlayerTpaSettingsConfigEntry settings = playerSettings.get(uuid.toString());
        if (settings == null) {
            playerSettings.put(uuid.toString(), new PlayerTpaSettingsConfigEntry());
            return false;
        }
        return settings.isEnableAutoDeny();
    }

    public static final BuilderCodec<PlayerTpaSettingsConfig> CODEC =
            BuilderCodec.builder(PlayerTpaSettingsConfig.class, PlayerTpaSettingsConfig::new)
                        .append(
                                new KeyedCodec<>(
                                        "PlayerSettings",
                                        new MapCodec<>(PlayerTpaSettingsConfigEntry.CODEC, HashMap::new)
                                ),
                                (o, m) -> o.playerSettings.putAll(m),
                                o -> o.playerSettings
                        )
                        .add()
                        .build();
}
