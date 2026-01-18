package tech.pmman.config.data;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.map.MapCodec;
import com.hypixel.hytale.math.vector.Transform;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class PlayerLastTeleportConfig {
    public static final BuilderCodec<PlayerLastTeleportConfig> CODEC;
    private Map<String, Transform> lastTeleportData = new HashMap<>();

    static {
        BuilderCodec.Builder<PlayerLastTeleportConfig> builder =
                BuilderCodec.builder(PlayerLastTeleportConfig.class, PlayerLastTeleportConfig::new);

        CODEC = builder
                .documentation("A config file to save players' home transforms")
                .append(
                        new KeyedCodec<>(
                                "Homes",
                                new MapCodec<>(
                                        Transform.CODEC,
                                        HashMap::new
                                )
                        ),
                        (obj, map) -> {
                            obj.lastTeleportData.putAll(map);
                        },
                        obj -> obj.lastTeleportData
                )
                .documentation("Player UUID -> Home Transform map")
                .add()
                .build();
    }
}
