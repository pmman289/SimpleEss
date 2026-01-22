package tech.pmman.pojo;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import lombok.Data;

@Data
public class KitConfigEntry {
    private String name;
    private String desc;
    private int cooldown = -1;
    private boolean checkPermission = false;
    private KitItemEntry[] items;

    public static final BuilderCodec<KitConfigEntry> CODEC =
            BuilderCodec.builder(KitConfigEntry.class, KitConfigEntry::new)
                        .append(
                                new KeyedCodec<>("Name", BuilderCodec.STRING),
                                (o, d) -> o.name = d,
                                o -> o.name
                        )
                        .add()
                        .append(
                                new KeyedCodec<>("Desc", BuilderCodec.STRING),
                                (o, d) -> o.desc = d,
                                o -> o.desc
                        )
                        .add()
                        .append(
                                new KeyedCodec<>("Cooldown", BuilderCodec.INTEGER),
                                (o, d) -> o.cooldown = d,
                                o -> o.cooldown
                        )
                        .add()
                        .append(
                                new KeyedCodec<>("Items", new ArrayCodec<>(KitItemEntry.CODEC, KitItemEntry[]::new)),
                                (o, d) -> o.items = d,
                                o -> o.items
                        )
                        .add()
                        .build();
}
