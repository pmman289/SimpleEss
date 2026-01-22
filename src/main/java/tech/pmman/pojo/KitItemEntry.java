package tech.pmman.pojo;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import lombok.Data;

@Data
public class KitItemEntry {
    private String itemId;
    private int amount;

    public static final BuilderCodec<KitItemEntry> CODEC =
            BuilderCodec.builder(KitItemEntry.class, KitItemEntry::new)
                        .append(
                                new KeyedCodec<>("ItemId", BuilderCodec.STRING),
                                (o, d) -> o.itemId = d,
                                o -> o.itemId
                        )
                        .add()
                        .append(
                                new KeyedCodec<>("Amount", BuilderCodec.INTEGER),
                                (o, d) -> o.amount = d,
                                o -> o.amount
                        )
                        .add()
                        .build();
}
