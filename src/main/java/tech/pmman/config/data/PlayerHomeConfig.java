package tech.pmman.config.data;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.map.MapCodec;
import lombok.Getter;
import tech.pmman.deprecated.PlayerLocationEntry;

import java.util.*;

public class PlayerHomeConfig {
    public static final BuilderCodec<PlayerHomeConfig> CODEC;
    @Getter
    private final Map<String, Map<String, PlayerLocationEntry>> homeData = new HashMap<>();

    static {
        BuilderCodec.Builder<PlayerHomeConfig> builder =
                BuilderCodec.builder(PlayerHomeConfig.class, PlayerHomeConfig::new);

        CODEC = builder
                .append(
                        new KeyedCodec<>(
                                "Homes",
                                new MapCodec<>(
                                        new MapCodec<>(
                                                PlayerLocationEntry.CODEC,
                                                HashMap::new
                                        ),
                                        HashMap::new
                                )
                        ),
                        (obj, map) -> {
                            obj.homeData.clear();
                            map.forEach((k, v) -> obj.homeData.put(k, new HashMap<>(v)));
                        },
                        obj -> obj.homeData
                )
                .add()
                .build();
    }
}
