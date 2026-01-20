package tech.pmman.service;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.permissions.PermissionsModule;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import tech.pmman.ConfigManager;
import tech.pmman.pojo.PlayerLocationEntry;
import tech.pmman.util.MessageTool;

import javax.annotation.Nonnull;
import java.util.Map;

public class SetHomeService {
    public static void setHome(String homeName, @Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store,
                               @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        PermissionsModule permissionsModule = PermissionsModule.get();
        String uuid = playerRef.getUuid()
                               .toString();
        Map<String, PlayerLocationEntry> homeMap = ConfigManager.PLAYER_HOME_DATA.get()
                                                                                 .getHomeMap(uuid);
        // 限制不能超过配置中的最大home数量，如果有bypass权限就不限制
        if (homeMap.size() >= ConfigManager.PLUGIN_CONFIG.get()
                                                         .getMaxHome() &&
                !permissionsModule.hasPermission(playerRef.getUuid(), "simpleess.command.sethome.limit.bypass")) {
            MessageTool.sendPluginMessage(commandContext, Message.translation("simpleEssCommand.sethome.outOfMaxHome")
                                                                 .param("maxHome", ConfigManager.PLUGIN_CONFIG.get()
                                                                                                              .getMaxHome()));
            return;
        }
        Transform transform = playerRef.getTransform()
                                       .clone();
        homeMap.put(homeName, new PlayerLocationEntry(world.getWorldConfig()
                                                           .getUuid()
                                                           .toString(), transform.getPosition(), transform.getRotation()))
        ;
        MessageTool.sendPluginMessage(commandContext, Message.translation("simpleEssCommand.sethome.execute"));
    }
}
