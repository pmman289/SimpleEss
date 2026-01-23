package tech.pmman.gui.kit;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import lombok.Data;
import tech.pmman.ConfigManager;
import tech.pmman.pojo.KitConfigEntry;
import tech.pmman.pojo.KitItemEntry;
import tech.pmman.service.CooldownService;
import tech.pmman.util.MessageTool;
import tech.pmman.util.PlayerTool;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class KitSelectorGui extends InteractiveCustomUIPage<KitSelectorGui.KitSelectorGuiData> {
    public KitSelectorGui(@Nonnull PlayerRef playerRef) {
        super(playerRef, CustomPageLifetime.CanDismiss, KitSelectorGuiData.CODEC);
    }

    @Override
    public void build(@Nonnull Ref<EntityStore> ref, @Nonnull UICommandBuilder uiCommandBuilder, @Nonnull UIEventBuilder uiEventBuilder, @Nonnull Store<EntityStore> store) {
        UUID uuid = playerRef.getUuid();
        uiCommandBuilder.append("Pages/Kit/Kit_Selector.ui");
        uiEventBuilder.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#CancelButton",
                EventData.of("Action", "CANCEL"),
                false
        );
        Map<String, KitConfigEntry> kitData = new HashMap<>(ConfigManager.KIT_CONFIG.get()
                                                                                    .getKits());
        // 对数据进行筛选只能在这里做
        // 查询冷却时间
        Map<String, Integer> kitElapsedSeconds = CooldownService.getUserKitCooldownRecord(
                uuid.toString(), kitData.keySet()
                                        .stream()
                                        .toList());
        // 构造冷却时间查找表
        Iterator<Map.Entry<String, KitConfigEntry>> kitDataIter = kitData.entrySet()
                                                                         .iterator();
        while (kitDataIter.hasNext()) {
            Map.Entry<String, KitConfigEntry> kitDataEntry = kitDataIter.next();
            String key = kitDataEntry.getKey();
            KitConfigEntry value = kitDataEntry.getValue();
            int kitCooldown = value
                    .getCooldown();
            if (kitCooldown == -1) {
                // 从data和查找表移除该项，因为已领取过
                kitDataIter.remove();
                continue;
            } else if (!kitElapsedSeconds.containsKey(key) || kitElapsedSeconds.get(key) > kitCooldown) {
                // 如果经过的时间大于冷却时间
                kitElapsedSeconds.put(key, 0);
            } else {
                kitElapsedSeconds.put(key, kitCooldown - kitElapsedSeconds.get(key));
            }
            if (!value.hasPermission(uuid, key)) {
                kitDataIter.remove();
            }
        }
        Iterator<Map.Entry<String, KitConfigEntry>> kitDataIterator = kitData.entrySet()
                                                                             .iterator();
        for (int i = 0; i < kitData.size(); i++) {
            Map.Entry<String, KitConfigEntry> data = kitDataIterator.next();
            String kitId = data.getKey();
            String kitName = data.getValue()
                                 .getName();
            String kitDesc = data.getValue()
                                 .getDesc();
            int kitCooldown = data.getValue()
                                  .getCooldown();
            uiCommandBuilder.append("#KitList", "Pages/Kit/Kit_Entry.ui");
            String entryPath = "#KitList[" + i + "]";
            uiCommandBuilder.set(entryPath + " #KitName.Text", kitName);
            uiCommandBuilder.set(entryPath + " #KitDesc.Text", kitDesc);
            // 设置cd领取提示
            if (kitCooldown == -1) {
                uiCommandBuilder.set(entryPath + " #Status.Text",
                        Message.translation("kitSelector.onlyOnce"));
            } else {
                uiCommandBuilder.set(entryPath + " #Status.Text",
                        Message.translation("kitSelector.cooldownNote")
                               .param("time", kitCooldown));
            }
            // 判断是否可用
            Integer cooldown = kitElapsedSeconds.get(kitId);
            if (cooldown != null) {
                if (cooldown != 0) {
                    uiCommandBuilder.set(entryPath + ".Disabled", true);
                    uiCommandBuilder.set(entryPath + " #Status.Text",
                            Message.translation("kitSelector.cooldownText")
                                   .param("time", cooldown));
                }
            }
            uiEventBuilder.addEventBinding(
                    CustomUIEventBindingType.Activating,
                    entryPath,
                    EventData.of("KitId", kitId),
                    false
            );
        }
    }

    @Override
    public void handleDataEvent(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store, @Nonnull KitSelectorGuiData data) {
        if ("CANCEL".equals(data.getAction())) {
            close();
        }
        if (data.kitId != null) {
            Map<String, KitConfigEntry> kitDataMap = ConfigManager.KIT_CONFIG.get()
                                                                             .getKits();
            KitConfigEntry kitData = kitDataMap.get(data.kitId);
            if (kitData == null) {
                close();
                return;
            }
            // 检查冷却
            int cooldown = CooldownService.tryUseKit(playerRef.getUuid()
                                                              .toString(), data.kitId, kitData.getCooldown());
            if (kitData.getCooldown() != 0 && cooldown != 0) {
                close();
                return;
            }
            // 检查权限
            if (!kitData.hasPermission(playerRef.getUuid(), data.getKitId())) {
                close();
                return;
            }
            Player player = PlayerTool.getPlayerFromRef(ref);
            for (KitItemEntry item : kitData.getItems()) {
                String itemId = item.getItemId();
                ItemStack itemStack = new ItemStack(itemId, item.getAmount());
                player.getInventory()
                      .getCombinedStorageFirst()
                      .addItemStack(itemStack);
            }
            MessageTool.sendPluginMessage(playerRef, Message.translation("simpleEssCommand.kit.use")
                                                            .param("name", kitData.getName()));
            close();
        }
    }

    @Data
    public static class KitSelectorGuiData {
        private String action;
        private String kitId;

        public static final BuilderCodec<KitSelectorGuiData> CODEC =
                BuilderCodec.builder(KitSelectorGuiData.class, KitSelectorGuiData::new)
                            .append(new KeyedCodec<>("Action", BuilderCodec.STRING), (o, d) -> o.action = d, o -> o.action)
                            .add()
                            .append(new KeyedCodec<>("KitId", BuilderCodec.STRING), (o, d) -> o.kitId = d, o -> o.kitId)
                            .add()
                            .build();
    }
}
