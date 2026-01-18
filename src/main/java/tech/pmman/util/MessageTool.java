package tech.pmman.util;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import tech.pmman.SimpleEssPlugin;

import java.awt.*;

public class MessageTool {
    private static String PLUGIN_PREFIX = "";

    public static void loadConfig() {
        PLUGIN_PREFIX = SimpleEssPlugin.pluginConfig.get()
                                                    .getPluginPrefix();
    }

    public static void sendPluginMessage(PlayerRef playerRef, Message message) {
        Message resultMessage = Message.join(Message.raw(PLUGIN_PREFIX), message)
                                       .color(Color.YELLOW);
        playerRef.sendMessage(resultMessage);
    }

    public static void sendPluginMessage(Player player, Message message) {
        Message resultMessage = Message.join(Message.raw(PLUGIN_PREFIX), message)
                                       .color(Color.YELLOW);
        player.sendMessage(resultMessage);
    }

    public static void sendPluginMessage(Ref<EntityStore> ref, Message message) {
        Message resultMessage = Message.join(Message.raw(PLUGIN_PREFIX), message)
                                       .color(Color.YELLOW);
        PlayerTool.getPlayerFromRef(ref)
                  .sendMessage(resultMessage);
    }

    public static void sendPluginMessage(CommandContext context, Message message) {
        Message resultMessage = Message.join(Message.raw(PLUGIN_PREFIX), message)
                                       .color(Color.YELLOW);
        context.sendMessage(resultMessage);
    }

    public static void sendPluginMessageNoChangeColor(PlayerRef playerRef, Message message) {
        Message resultMessage = Message.join(Message.raw(PLUGIN_PREFIX).color(Color.YELLOW), message);
        playerRef.sendMessage(resultMessage);
    }
}
