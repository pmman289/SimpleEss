package tech.pmman.events;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.AddPlayerToWorldEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import tech.pmman.SimpleEssPlugin;
import tech.pmman.util.MessageTool;

import java.util.List;

public class EventListener {
    public static void register(EventRegistry registry) {
        String welcomeText = SimpleEssPlugin.pluginConfig.get()
                                                         .getWelcomeText();
        String joinBroadcast = SimpleEssPlugin.pluginConfig.get()
                                                           .getJoinBroadcast();
        if (!welcomeText.isEmpty()) {
            registry.registerGlobal(PlayerReadyEvent.class, EventListener::sendWelcomeText);
        }
        if (!joinBroadcast.isEmpty()) {
            registry.registerGlobal(AddPlayerToWorldEvent.class, EventListener::onPlayerJoin);
            registry.registerGlobal(PlayerReadyEvent.class, EventListener::broadcastJoinMessage);
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