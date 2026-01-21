package tech.pmman.service;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import tech.pmman.DbManager;
import tech.pmman.dao.mapper.PlayerHomeMapper;
import tech.pmman.pojo.PlayerLocationEntry;
import tech.pmman.pojo.db.PlayerHome;
import tech.pmman.util.MessageTool;
import tech.pmman.util.PlayerTool;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

public class HomeService {
    public static void goHome(String homeName, @Nonnull CommandContext commandContext,
                              @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef) {
        String uuid = playerRef.getUuid()
                               .toString();
        DbManager.getInstance()
                 .get()
                 .useExtension(PlayerHomeMapper.class, dao -> {
                     List<PlayerHome> homes = dao.queryPlayerHomes(uuid);
                     PlayerHome targetHome = homes.stream()
                                                  .filter(h -> h.getHomeName()
                                                                .equals(homeName))
                                                  .findFirst()
                                                  .orElse(null);
                     if (targetHome == null) {
                         MessageTool.sendPluginMessage(commandContext, Message.translation("simpleEssCommand.home.nohome")
                                                                              .param("homeName", homeName));
                         return;
                     }
                     // 判断世界是否在线
                     World targetWorld = Universe.get()
                                                 .getWorld(UUID.fromString(targetHome.getWorldUUID()));
                     if (targetWorld == null || !targetWorld.isAlive()) {
                         MessageTool.sendPluginMessage(playerRef, Message.translation("simpleEssCore.teleport.targetWorldNotOnline"));
                         return;
                     }
                     PlayerTool.teleportPlayer(ref, targetWorld, targetHome.getLocationObj()
                                                                           .getPosition(), targetHome.getLocationObj()
                                                                                                     .getRotation());
                     PlayerTool.recordPlayerTransformHistory(playerRef.getUuid()
                                                                      .toString(), targetHome.getWorldUUID(),
                             targetHome.getLocationObj()
                                       .getPosition(), targetHome.getLocationObj()
                                                                 .getRotation());
                     MessageTool.sendPluginMessage(commandContext, Message.translation("simpleEssCommand.home.execute"));
                 });
    }
}
