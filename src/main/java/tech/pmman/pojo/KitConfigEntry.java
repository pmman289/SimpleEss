package tech.pmman.pojo;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import com.hypixel.hytale.server.core.permissions.PermissionsModule;
import lombok.Data;

import java.util.UUID;

@Data
public class KitConfigEntry {
    private String name;
    private String desc;
    private int cooldown = -1;
    private boolean checkNodePermission = false;
    private String checkGroup = "";
    private KitItemEntry[] items;

    public boolean hasPermission(UUID uuid, String kitKey) {
        PermissionsModule permissionsModule = PermissionsModule.get();
        // 检查是否具有权限
        if (isCheckNodePermission()) {
            if (!permissionsModule.hasPermission(uuid, "simpleess.command.kit.use." + kitKey)) {
                return false;
            }
        }
        if (!getCheckGroup()
                .isEmpty()) {
            return permissionsModule.getGroupsForUser(uuid)
                                    .contains(getCheckGroup());
        }
        return true;
    }

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
                                new KeyedCodec<>("CheckNodePermission", BuilderCodec.BOOLEAN),
                                (o, d) -> o.checkNodePermission = d,
                                o -> o.checkNodePermission
                        )
                        .add()
                        .append(
                                new KeyedCodec<>("CheckGroup", BuilderCodec.STRING),
                                (o, d) -> o.checkGroup = d,
                                o -> o.checkGroup
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
