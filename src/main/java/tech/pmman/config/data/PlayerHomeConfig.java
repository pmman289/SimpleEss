package tech.pmman.config.data;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.map.MapCodec;
import lombok.Data;
import tech.pmman.pojo.PlayerLocationEntry;

import java.util.*;

@Data
public class PlayerHomeConfig {
    public static final BuilderCodec<PlayerHomeConfig> CODEC;
    private Map<String, PlayerLocationEntry> homeData = new HashMap<>();

    static {
        BuilderCodec.Builder<PlayerHomeConfig> builder =
                BuilderCodec.builder(PlayerHomeConfig.class, PlayerHomeConfig::new);

        CODEC = builder
                .append(
                        new KeyedCodec<>(
                                "Homes",
                                new MapCodec<>(
                                        PlayerLocationEntry.CODEC,
                                        HashMap::new
                                )
                        ),
                        (obj, map) -> obj.homeData.putAll(map),
                        obj -> obj.homeData
                )
                .add()
                .build();
    }
}
