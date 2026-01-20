package tech.pmman.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.DefaultArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import lombok.Getter;
import tech.pmman.SimpleEssPlugin;
import tech.pmman.pojo.PlayerLocationEntry;
import tech.pmman.util.MessageTool;

import javax.annotation.Nonnull;
import java.util.Map;

public class SetHomeCommand extends AbstractPlayerCommand implements PermissionGroupSettable {
    @Getter
    private final String permissionStr = "simpleess.command.sethome";

    private final DefaultArg<String> name;

    public SetHomeCommand() {
        super("sethome", "simpleEssCommand.sethome.desc");
        name = withDefaultArg("name", "simpleEssCommand.sethome.desc.argName", ArgTypes.STRING, "default", "simpleEssCommand.sethome.desc.argNameDefault");
        requirePermission(permissionStr);
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        String uuid = playerRef.getUuid()
                               .toString();
        Map<String, PlayerLocationEntry> homeMap = SimpleEssPlugin.playerHomeConfig.get()
                                                                                   .getHomeMap(uuid);
        // 限制不能超过配置中的最大home数量
        if (homeMap.size() >= SimpleEssPlugin.pluginConfig.get()
                                                          .getMaxHome()) {
            MessageTool.sendPluginMessage(commandContext, Message.translation("simpleEssCommand.sethome.outOfMaxHome")
                                                                 .param("maxHome", SimpleEssPlugin.pluginConfig.get()
                                                                                                               .getMaxHome()));
            return;
        }
        Transform transform = playerRef.getTransform()
                                       .clone();
        homeMap.put(name.get(commandContext), new PlayerLocationEntry(world.getWorldConfig()
                                                                           .getUuid()
                                                                           .toString(), transform.getPosition(), transform.getRotation()));
        MessageTool.sendPluginMessage(commandContext, Message.translation("simpleEssCommand.sethome.execute"));
    }

    @Override
    public void resetPermissionGroups(String... groups) {
        setPermissionGroups(groups);
    }
}
