package tech.pmman.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.modules.collision.WorldUtil;
import com.hypixel.hytale.server.core.permissions.PermissionsModule;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import lombok.Getter;
import tech.pmman.ConfigManager;
import tech.pmman.pojo.WorldPlaneData;
import tech.pmman.service.CommandCooldownService;
import tech.pmman.util.MessageTool;
import tech.pmman.util.PlayerTool;

import javax.annotation.Nonnull;

public class RtpCommand extends AbstractPlayerCommand implements PermissionGroupSettable {
    @Getter
    private final String permissionStr = "simpleess.command.rtp";

    private static final WorldPlaneData MAX_RTP_LIMIT = new WorldPlaneData(
            new WorldPlaneData.WorldPlanePoint(10000, 10000),
            new WorldPlaneData.WorldPlanePoint(-10000, -10000)
    );

    public RtpCommand() {
        super("rtp", "simpleEssCommand.rtp.desc");
        requirePermission(permissionStr);
    }

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        PermissionsModule permissionsModule = PermissionsModule.get();
        // 获取剩余冷却时间
        int remainCooldown = CommandCooldownService.tryUseCommand(playerRef.getUuid()
                                                                           .toString(), getName(),
                ConfigManager.PLUGIN_CONFIG.get()
                                           .getRtpCooldown());
        // 如果具有权限则跳过cd检查
        if (remainCooldown != 0 &&
                !permissionsModule.hasPermission(playerRef.getUuid(), "simpleess.command.rtp.cooldown.bypass")) {
            MessageTool.sendPluginMessage(playerRef, Message.translation("simpleEssCommand.rtp.cooldown")
                                                            .param("cooldown", remainCooldown));
            return;
        }
        // 尝试查找可用的位置
        WorldPlaneData limitSpace = ConfigManager.PLUGIN_CONFIG.get()
                                                               .isEnableRtpLimitSpace() ?
                ConfigManager.PLUGIN_CONFIG.get()
                                           .getRtpLimitSpace() : MAX_RTP_LIMIT;
        for (int i = 0; i < 10; i++) {
            WorldPlaneData.WorldPlanePoint randomPoint = limitSpace.getRandomPointInSpace();
            WorldChunk chunk = world.getNonTickingChunk(ChunkUtil.indexChunkFromBlock(randomPoint.getX(), randomPoint.getZ()));
            if (chunk == null) {
                continue;
            }
            Vector3i targetPos = new Vector3d(
                    randomPoint.getX(), chunk.getHeight(randomPoint.getX(), randomPoint.getZ()), randomPoint.getZ())
                    .toVector3i();
            BlockType blockType = chunk.getBlockType(targetPos);

            assert blockType != null;
            boolean isSolid = WorldUtil.isSolidOnlyBlock(blockType, chunk.getFluidId(targetPos.x, targetPos.y, targetPos.z));
            isSolid = isSolid && WorldUtil.isSolidOnlyBlock(blockType, chunk.getFluidId(targetPos.x, targetPos.y + 1, targetPos.z));
            if (isSolid) {
                targetPos.setY(targetPos.y + 1);
                PlayerTool.teleportPlayer(ref, world, targetPos.toVector3d(), playerRef.getTransform()
                                                                                       .getRotation());
                MessageTool.sendPluginMessage(commandContext, Message.translation("simpleEssCommand.rtp.execute")
                                                                     .param("x", targetPos.x)
                                                                     .param("y", targetPos.y)
                                                                     .param("z", targetPos.z));
                return;
            }
        }
        MessageTool.sendPluginMessage(commandContext, Message.translation("simpleEssCommand.rtp.failed"));
    }

    @Override
    public void resetPermissionGroups(String... groups) {
        setPermissionGroups(groups);
    }
}
