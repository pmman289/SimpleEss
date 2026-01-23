package tech.pmman.config;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.map.MapCodec;
import lombok.Data;
import tech.pmman.pojo.KitConfigEntry;
import tech.pmman.pojo.KitItemEntry;

import java.util.HashMap;
import java.util.Map;

@Data
public class KitConfig {
    private Map<String, KitConfigEntry> kits = new HashMap<>();

    public KitConfig() {
        // 组装默认礼包
        KitItemEntry kitItem = new KitItemEntry();
        kitItem.setItemId("Tool_Pickaxe_Crude");
        kitItem.setAmount(1);
        KitItemEntry kitItem2 = new KitItemEntry();
        kitItem2.setItemId("Tool_Hatchet_Crude");
        kitItem2.setAmount(1);
        KitConfigEntry kitConfigEntry = new KitConfigEntry();
        kitConfigEntry.setName("Starter Pack");
        kitConfigEntry.setDesc("A gift for you");
        kitConfigEntry.setCooldown(300);
        kitConfigEntry.setItems(new KitItemEntry[]{kitItem, kitItem2});
        kits.put("starter", kitConfigEntry);
    }

    public static final BuilderCodec<KitConfig> CODEC =
            BuilderCodec.builder(KitConfig.class, KitConfig::new)
                        .append(
                                new KeyedCodec<>("Kits", new MapCodec<>(KitConfigEntry.CODEC, HashMap::new)),
                                (o, d) -> o.kits = d,
                                o -> o.kits
                        )
                        .add()
                        .build();
}
