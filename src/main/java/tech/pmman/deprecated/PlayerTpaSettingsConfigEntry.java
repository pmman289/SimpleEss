package tech.pmman.deprecated;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import lombok.Data;

@Data
public class PlayerTpaSettingsConfigEntry {
    private boolean enableAutoAccept;
    private boolean enableAutoDeny;
    private boolean disableTpa;

    public static final BuilderCodec<PlayerTpaSettingsConfigEntry> CODEC =
            BuilderCodec.builder(PlayerTpaSettingsConfigEntry.class, PlayerTpaSettingsConfigEntry::new)
                        .append(
                                new KeyedCodec<>("EnableAutoAccept", BuilderCodec.BOOLEAN),
                                (o, d) -> o.enableAutoAccept = d,
                                o -> o.enableAutoAccept
                        )
                        .add()
                        .append(
                                new KeyedCodec<>("EnableAutoDeny", BuilderCodec.BOOLEAN),
                                (o, d) -> o.enableAutoDeny = d,
                                o -> o.enableAutoDeny
                        )
                        .add()
                        .append(
                                new KeyedCodec<>("DisableTpa", BuilderCodec.BOOLEAN),
                                (o, d) -> o.disableTpa = d,
                                o -> o.disableTpa
                        )
                        .add()
                        .build();
}
