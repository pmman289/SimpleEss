package tech.pmman.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import lombok.Getter;
import tech.pmman.gui.kit.KitSelectorGui;

import javax.annotation.Nonnull;

public class KitCommand extends AbstractPlayerCommand implements PermissionGroupSettable {
    @Getter
    private final String permissionStr = "simpleess.command.kit";

    public KitCommand() {
        super("kit", "simpleEssCommand.kit.desc");
        addAliases("kits");
        requirePermission(permissionStr);
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        Player player = commandContext.senderAs(Player.class);
        player.getPageManager().openCustomPage(ref, store, new KitSelectorGui(playerRef));
    }

    @Override
    public void resetPermissionGroups(String... groups) {
        setPermissionGroups(groups);
    }
}
