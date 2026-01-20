package tech.pmman.events;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import tech.pmman.service.CommandCooldownService;

public class MemClearListener {
    public static void register(EventRegistry registry) {
        // 清理冷却计时数据
        registry.registerGlobal(PlayerDisconnectEvent.class, CommandCooldownService::clearUserData);
    }
}
