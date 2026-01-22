package tech.pmman.deprecated;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import lombok.Data;

@Data
public class PlayerLocationEntry {
    private String worldUUID;
    private Vector3d position;
    private Vector3f rotation;

    public PlayerLocationEntry(String worldUUID, Vector3d position, Vector3f rotation) {
        this.worldUUID = worldUUID;
        this.position = position;
        this.rotation = rotation;
    }

    public PlayerLocationEntry() {
    }

    public static final BuilderCodec<PlayerLocationEntry> CODEC =
            BuilderCodec.builder(PlayerLocationEntry.class, PlayerLocationEntry::new)
                        .append(
                                new KeyedCodec<>("WorldUUID", BuilderCodec.STRING),
                                (o, d) -> o.worldUUID = d,
                                o -> o.worldUUID
                        )
                        .add()
                        .append(
                                new KeyedCodec<>("Position", Vector3d.CODEC),
                                (o, d) -> o.position = d,
                                o -> o.position
                        )
                        .add()
                        .append(
                                new KeyedCodec<>("Rotation", Vector3f.CODEC),
                                (o, d) -> o.rotation = d,
                                o -> o.rotation
                        )
                        .add()
                        .build();
}
