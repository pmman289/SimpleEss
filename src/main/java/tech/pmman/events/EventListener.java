package tech.pmman.events;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.AddPlayerToWorldEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.permissions.PermissionsModule;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import tech.pmman.SimpleEssPlugin;
import tech.pmman.util.MessageTool;
import tech.pmman.util.PlayerTool;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class EventListener {
    public static void register(EventRegistry registry) {
        String welcomeText = SimpleEssPlugin.pluginConfig.get()
                                                         .getWelcomeText();
        String joinBroadcast = SimpleEssPlugin.pluginConfig.get()
                                                           .getJoinBroadcast();
        boolean autoAddToDefault = SimpleEssPlugin.pluginConfig.get()
                                                               .isAutoAddNewPlayerToDefault();
        if (!welcomeText.isEmpty()) {
            registry.registerGlobal(PlayerReadyEvent.class, EventListener::sendWelcomeText);
        }
        if (!joinBroadcast.isEmpty()) {
            registry.registerGlobal(AddPlayerToWorldEvent.class, EventListener::onPlayerJoin);
            registry.registerGlobal(PlayerReadyEvent.class, EventListener::broadcastJoinMessage);
        }
        if (autoAddToDefault) {
            registry.registerGlobal(PlayerReadyEvent.class, EventListener::autoAddPlayerToDefault);
        }
    }

    public static void autoAddPlayerToDefault(PlayerReadyEvent event) {
        Ref<EntityStore> playerRef = event.getPlayerRef();
        UUID uuid = PlayerTool.getUUID(playerRef.getStore(), playerRef);
        PermissionsModule permissionsModule = PermissionsModule.get();
        Set<String> groups = permissionsModule.getGroupsForUser(uuid);
        if (groups.size() < 2 && !groups.contains("Default")) {
            permissionsModule.addUserToGroup(uuid, "Default");
        }
    }

    public static void sendWelcomeText(PlayerReadyEvent event) {
        String welcomeText = SimpleEssPlugin.pluginConfig.get()
                                                         .getWelcomeText();

        Player player = event.getPlayer();
        MessageTool.sendPluginMessage(player, Message.raw(welcomeText.replace("{player}", player.getDisplayName())));
    }

    public static void broadcastJoinMessage(PlayerReadyEvent event) {
        String joinBroadcast = SimpleEssPlugin.pluginConfig.get()
                                                           .getJoinBroadcast();
        List<PlayerRef> players = Universe.get()
                                          .getPlayers();
        for (PlayerRef player : players) {
            MessageTool.sendPluginMessage(player, Message.raw(joinBroadcast.replace("{player}", event.getPlayer()
                                                                                                     .getDisplayName())));
        }
    }

    public static void onPlayerJoin(AddPlayerToWorldEvent event) {
        event.setBroadcastJoinMessage(false);
    }
}