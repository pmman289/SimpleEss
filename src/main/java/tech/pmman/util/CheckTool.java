package tech.pmman.util;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.exceptions.GeneralCommandException;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class CheckTool {
    public static void checkCommandFromPlayer(CommandContext context) {
        if (!context.isPlayer()) {
            throw new GeneralCommandException(Message.raw("Only players can use this command"));
        }
    }

    public static Ref<EntityStore> getPlayerRefByCommandContext(CommandContext context) {
        checkCommandFromPlayer(context);
        Ref<EntityStore> playerRef = context.senderAsPlayerRef();
        if (playerRef == null || !playerRef.isValid()) {
            throw new GeneralCommandException(Message.raw("PlayerRef not invalid now"));
        }
        return playerRef;
    }

    public static void checkPlayerRef(PlayerRef playerRef) {
        if (playerRef != null && playerRef.isValid()) {
            return;
        }
        throw new GeneralCommandException(Message.raw("PlayerRef not invalid now"));
    }
}
