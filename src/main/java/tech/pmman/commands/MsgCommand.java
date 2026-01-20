package tech.pmman.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import lombok.Getter;
import tech.pmman.util.CheckTool;
import tech.pmman.util.MessageTool;

import javax.annotation.Nonnull;
import java.awt.*;

public class MsgCommand extends AbstractPlayerCommand implements PermissionGroupSettable {
    @Getter
    private final String permissionStr = "simpleess.command.msg";

    private final RequiredArg<PlayerRef> targetPlayerRefArg;
    private final RequiredArg<String> messageArg;

    public MsgCommand() {
        super("msg", "simpleEssCommand.msg.desc");
        addAliases("m", "talk", "wisper");
        targetPlayerRefArg = withRequiredArg("targetPlayer", "simpleEssCommand.msg.argTargetPlayer", ArgTypes.PLAYER_REF);
        messageArg = withRequiredArg("message", "simpleEssCommand.msg.argMessage", ArgTypes.STRING);
        requirePermission(permissionStr);
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        CheckTool.checkCommandFromPlayer(commandContext);
        PlayerRef targetPlayerRef = this.targetPlayerRefArg.get(commandContext);
        if (targetPlayerRef.getUuid()
                           .equals(playerRef.getUuid())) {
            MessageTool.sendPluginMessage(playerRef, Message.translation("simpleEssCommand.msg.cannotSendSelf"));
            return;
        }
        String message = messageArg.get(commandContext);
        Message fromPlayer = Message.raw(targetPlayerRef.getUsername())
                                    .color(Color.CYAN);
        Message msg = Message.raw(message)
                             .color(Color.WHITE);
        Message resultMessage = Message.join(fromPlayer,
                Message.translation("simpleEssCommand.msg.execute")
                       .color(Color.YELLOW),
                msg);
        MessageTool.sendPluginMessageNoChangeColor(targetPlayerRef, resultMessage);
        Message sendToSender = Message.join(Message.translation("simpleEssCommand.msg.executeMe")
                                                   .color(Color.YELLOW), msg);
        MessageTool.sendPluginMessageNoChangeColor(playerRef, sendToSender);
    }

    @Override
    public void resetPermissionGroups(String... groups) {
        setPermissionGroups(groups);
    }
}
