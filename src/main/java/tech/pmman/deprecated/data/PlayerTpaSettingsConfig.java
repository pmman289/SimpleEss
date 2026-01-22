package tech.pmman.deprecated.data;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.map.MapCodec;
import lombok.Data;
import tech.pmman.deprecated.PlayerTpaSettingsConfigEntry;

import java.util.HashMap;
import java.util.Map;

@Data
public class PlayerTpaSettingsConfig {
    private Map<String, PlayerTpaSettingsConfigEntry> playerSettings = new HashMap<>();

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
