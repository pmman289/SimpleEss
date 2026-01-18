package tech.pmman.config.data;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.map.MapCodec;
import com.hypixel.hytale.math.vector.Transform;
import lombok.Data;

import java.util.*;

@Data
public class PlayerHomeConfig {
    public static final BuilderCodec<PlayerHomeConfig> CODEC;
    private Map<String, Transform> homeData = new HashMap<>();

    static {
        BuilderCodec.Builder<PlayerHomeConfig> builder =
                BuilderCodec.builder(PlayerHomeConfig.class, PlayerHomeConfig::new);

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
                            obj.homeData.putAll(map);
                        },
                        obj -> obj.homeData
                )
                .documentation("Player UUID -> Home Transform map")
                .add()
                .build();
    }
}
