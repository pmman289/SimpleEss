package tech.pmman.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import tech.pmman.gui.TpaRequestManagerGui;
import tech.pmman.util.CheckTool;

import javax.annotation.Nonnull;

public class TpaTabCommand extends AbstractPlayerCommand implements PermissionGroupSettable {
    public TpaTabCommand() {
        super("tpatab", "simpleEssCommand.tpatab.desc");
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store,
                           @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        // 打开gui
        CheckTool.checkCommandFromPlayer(commandContext);
        Player player = commandContext.senderAs(Player.class);
        player.getPageManager()
              .openCustomPage(ref, store, new TpaRequestManagerGui(playerRef));
//        player.getPageManager()
//              .openCustomPage(ref, store, new TpaSettingsGui(playerRef));
    }

    @Override
    public void resetPermissionGroups(String... groups) {
        setPermissionGroups(groups);
    }
}
