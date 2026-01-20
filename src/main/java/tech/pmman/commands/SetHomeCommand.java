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
import tech.pmman.service.SetHomeService;

import javax.annotation.Nonnull;

public class SetHomeCommand extends AbstractPlayerCommand implements PermissionGroupSettable {
    @Getter
    private final String permissionStr = "simpleess.command.sethome";

    public SetHomeCommand() {
        super("sethome", "simpleEssCommand.sethome.desc");
        addUsageVariant(new SetHomeWithNameCommand());
        requirePermission(permissionStr);
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        SetHomeService.setHome("default", commandContext, store, ref, playerRef, world);
    }

    @Override
    public void resetPermissionGroups(String... groups) {
        setPermissionGroups(groups);
    }

    public static class SetHomeWithNameCommand extends AbstractPlayerCommand {
        private final RequiredArg<String> name;

        public SetHomeWithNameCommand() {
            super("simpleEssCommand.sethome.desc");
            name = withRequiredArg("name", "simpleEssCommand.sethome.desc.argName", ArgTypes.STRING);
        }

        @Override
        protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
            SetHomeService.setHome(name.get(commandContext), commandContext, store, ref, playerRef, world);
        }
    }
}
