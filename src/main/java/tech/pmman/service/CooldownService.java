package tech.pmman.service;

import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CooldownService {
    private static final Map<String, Map<String, Long>> cooldownData = new ConcurrentHashMap<>();

    public static void clearUserData(PlayerDisconnectEvent event) {
        String verifyId = event.getPlayerRef()
                               .getUuid()
                               .toString();
        cooldownData.remove(verifyId);
    }

    public static int tryUseCommand(String verifyId, String commandName, int cooldown) {
        Map<String, Long> userCooldownData = cooldownData.computeIfAbsent(verifyId, _ -> new ConcurrentHashMap<>());
        long currentTimeMillis = System.currentTimeMillis();
        long lastUsedTime = userCooldownData.getOrDefault(commandName, 0L);
        long diff = (currentTimeMillis - lastUsedTime) / 1000;
        if (diff >= cooldown) {
            userCooldownData.put(commandName, currentTimeMillis);
            return 0;
        }
        return Math.toIntExact(cooldown - diff);
    }
}
