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
import tech.pmman.service.FlyService;

import javax.annotation.Nonnull;
import java.util.Objects;

public class FlyCommand extends AbstractPlayerCommand implements PermissionGroupSettable {
    @Getter
    private final String permissionStr = "simpleess.command.fly";

    public FlyCommand() {
        super("fly", "simpleEssCommand.fly.desc");
        addUsageVariant(new FlyCommandWithOthers());
        addUsageVariant(new FlyCommandWithOtherAndMode());
        requirePermission(permissionStr);
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        FlyService.fly(commandContext, store, ref, playerRef, null);
    }

    @Override
    public void resetPermissionGroups(String... groups) {
        setPermissionGroups(groups);
    }

    public static class FlyCommandWithOthers extends AbstractPlayerCommand {
        private final RequiredArg<PlayerRef> targetPlayerArg;

        public FlyCommandWithOthers() {
            super("simpleEssCommand.fly.others.desc");
            targetPlayerArg = withRequiredArg("targetPlayer", "simpleEssCommand.fly.others.arg", ArgTypes.PLAYER_REF);
            requirePermission("simpleess.command.fly.others");
        }

        @Override
        protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
            FlyService.fly(commandContext, store, Objects.requireNonNull(targetPlayerArg.get(commandContext)
                                                                                        .getReference()),
                    targetPlayerArg.get(commandContext), null);
        }
    }

    public static class FlyCommandWithOtherAndMode extends AbstractPlayerCommand {
        private final RequiredArg<PlayerRef> targetPlayerArg;
        private final RequiredArg<Boolean> modeArg;

        public FlyCommandWithOtherAndMode() {
            super("simpleEssCommand.fly.others.desc");
            targetPlayerArg = withRequiredArg("targetPlayer", "simpleEssCommand.fly.others.arg", ArgTypes.PLAYER_REF);
            modeArg = withRequiredArg("mode", "simpleEssCommand.fly.others.argMode", ArgTypes.BOOLEAN);
            requirePermission("simpleess.command.fly.others");
        }

        @Override
        protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
            FlyService.fly(commandContext, store, Objects.requireNonNull(targetPlayerArg.get(commandContext)
                                                                                        .getReference()),
                    targetPlayerArg.get(commandContext), modeArg.get(commandContext));
        }
    }
}
