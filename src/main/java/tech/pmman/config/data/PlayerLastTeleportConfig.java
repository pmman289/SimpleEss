package tech.pmman.config.data;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.map.MapCodec;
import lombok.Data;
import tech.pmman.deprecated.PlayerLocationEntry;

import java.util.HashMap;
import java.util.Map;

@Data
public class PlayerLastTeleportConfig {
    public static final BuilderCodec<PlayerLastTeleportConfig> CODEC;
    private Map<String, PlayerLocationEntry> lastTeleportData = new HashMap<>();

    static {
        BuilderCodec.Builder<PlayerLastTeleportConfig> builder =
                BuilderCodec.builder(PlayerLastTeleportConfig.class, PlayerLastTeleportConfig::new);

        CODEC = builder
                .append(
                        new KeyedCodec<>(
                                "Homes",
                                new MapCodec<>(
                                        PlayerLocationEntry.CODEC,
                                        HashMap::new
                                )
                        ),
                        (obj, map) -> obj.lastTeleportData.putAll(map),
                        obj -> obj.lastTeleportData
                )
                .add()
                .build();
    }
}
