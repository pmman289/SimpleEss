package tech.pmman.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import lombok.Getter;
import tech.pmman.service.HomeService;

import javax.annotation.Nonnull;

public class HomeCommand extends AbstractPlayerCommand implements PermissionGroupSettable {
    @Getter
    private final String permissionStr = "simpleess.command.home";

    public HomeCommand() {
        super("home", "simpleEssCommand.home.desc");
        addUsageVariant(new HomeWithNameCommand());
        requirePermission(permissionStr);
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        String homeName = "default";
        HomeService.goHome(homeName, commandContext, ref, playerRef);
    }

    @Override
    public void resetPermissionGroups(String... groups) {
        setPermissionGroups(groups);
    }

    public static class HomeWithNameCommand extends AbstractPlayerCommand {
        private final RequiredArg<String> name;

        public HomeWithNameCommand() {
            super("simpleEssCommand.home.desc");
            name = withRequiredArg("name", "simpleEssCommand.home.desc.argName", ArgTypes.STRING);
        }

        @Override
        protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
            String homeName = name.get(commandContext);
            HomeService.goHome(homeName, commandContext, ref, playerRef);
        }
    }
}
