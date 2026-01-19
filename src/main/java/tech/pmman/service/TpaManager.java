package tech.pmman.service;

import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import tech.pmman.SimpleEssPlugin;
import tech.pmman.pojo.PlayerLocationEntry;
import tech.pmman.pojo.TpaRequestData;
import tech.pmman.util.CheckTool;
import tech.pmman.util.MessageTool;
import tech.pmman.util.PlayerTool;

import java.util.*;

public class TpaManager {
    private static int TELEPORT_COOLDOWN = 120;

    public static void loadConfig() {
        // 从配置读取
        TELEPORT_COOLDOWN = SimpleEssPlugin.pluginConfig.get()
                                                        .getTeleportCooldown();
    }

    // key为玩家uuid，value为要处理的请求
    private static final Map<UUID, List<TpaRequestData>> tpaDataMap = new HashMap<>();

    private static int getDurationSeconds(long start, long end) {
        return Math.toIntExact(Math.abs(end - start) / 1000);
    }

    private static void clearExpiredRequest(List<TpaRequestData> userRequestData) {
        // 扫描并清理过期的请求
        userRequestData.removeIf(data -> getDurationSeconds(data.getTimestamp(), System.currentTimeMillis()) > TELEPORT_COOLDOWN);
    }

    public static void sendTpaRequestWithClear(UUID from, UUID target) {
        // 如果玩家关闭了传送功能，则不发送请求
        if (SimpleEssPlugin.playerTpaSettingsConfig.get()
                                                   .getDisableTpa(target)) return;
        PlayerRef fromPlayerRef = Universe.get()
                                          .getPlayer(from);
        PlayerRef targetPlayerRef = Universe.get()
                                            .getPlayer(target);
        CheckTool.checkPlayerRef(fromPlayerRef);
        CheckTool.checkPlayerRef(targetPlayerRef);
        List<TpaRequestData> userRequestList = tpaDataMap.computeIfAbsent(target, k -> new ArrayList<>());
        // 清理过期数据
        clearExpiredRequest(userRequestList);
        // 如果用户已在TELEPORT_COOLDOWN秒内发起过申请，则告知发起者
        TpaRequestData tpaRequestData = userRequestList.stream()
                                                       .filter(o -> from.equals(o.getRequestPlayer()))
                                                       .findFirst()
                                                       .orElse(null);
        if (tpaRequestData != null && getDurationSeconds(tpaRequestData.getTimestamp(), System.currentTimeMillis()) < TELEPORT_COOLDOWN) {
            MessageTool.sendPluginMessage(fromPlayerRef, Message.translation("tpaRequestManager.requestCd"));
            return;
        }
        // 否则发起申请
        TpaRequestData req = new TpaRequestData(from);
        userRequestList.add(req);
        MessageTool.sendPluginMessage(targetPlayerRef, Message.translation("tpaRequestManager.requestReceived"));
        // 如果玩家开启自动接受申请，则自动调用接受
        if (SimpleEssPlugin.playerTpaSettingsConfig.get()
                                                   .getAutoAccept(target)) {
            acceptTpaRequest(from, target);
        } else if (SimpleEssPlugin.playerTpaSettingsConfig.get()
                                                          .getAutoDeny(target)) {
            // 如果开启了自动拒绝，则自动调用拒绝
            denyTpaRequest(from, target);
        }
    }

    public static void acceptTpaRequest(UUID from, UUID target) {
        PlayerRef fromPlayerRef = Universe.get()
                                          .getPlayer(from);
        PlayerRef targetPlayerRef = Universe.get()
                                            .getPlayer(target);
        CheckTool.checkPlayerRef(fromPlayerRef);
        CheckTool.checkPlayerRef(targetPlayerRef);
        // 移除数据
        List<TpaRequestData> userRequestList = tpaDataMap.computeIfAbsent(target, k -> new ArrayList<>());
        userRequestList.removeIf(o -> from.equals(o.getRequestPlayer()));
        // 传送玩家
        assert fromPlayerRef.getWorldUuid() != null;
        Transform targetTransform = targetPlayerRef.getTransform()
                                                   .clone();
        Transform fromTransform = fromPlayerRef.getTransform()
                                               .clone();
        PlayerTool.teleportPlayer(Objects.requireNonNull(fromPlayerRef.getReference()),
                Universe.get()
                        .getWorld(fromPlayerRef.getWorldUuid()), targetTransform
                        .getPosition(),
                targetTransform
                        .getRotation());
        PlayerTool.recordPlayerTransformHistory(from.toString(),
                new PlayerLocationEntry(fromPlayerRef.getWorldUuid()
                                                     .toString(), fromTransform
                        .getPosition(),
                        fromTransform
                                .getRotation()));
        // 发送通知
        MessageTool.sendPluginMessage(fromPlayerRef, Message.translation("tpaRequestManager.requestAcceptedToFrom"));
        MessageTool.sendPluginMessage(targetPlayerRef, Message.translation("tpaRequestManager.requestAcceptedToTarget"));
    }

    public static void denyTpaRequest(UUID from, UUID target) {
        PlayerRef fromPlayerRef = Universe.get()
                                          .getPlayer(from);
        PlayerRef targetPlayerRef = Universe.get()
                                            .getPlayer(target);
        CheckTool.checkPlayerRef(fromPlayerRef);
        CheckTool.checkPlayerRef(targetPlayerRef);
        // 移除数据
        List<TpaRequestData> userRequestList = tpaDataMap.computeIfAbsent(target, k -> new ArrayList<>());
        userRequestList.removeIf(o -> from.equals(o.getRequestPlayer()));
        // 发送通知
        MessageTool.sendPluginMessage(fromPlayerRef, Message.translation("tpaRequestManager.requestDeniedToFrom"));
    }

    public static List<TpaRequestData> getTargetAllRequestList(UUID target) {
        return tpaDataMap.get(target);
    }

    public static int getRequestCdWithRemoveExpiredRequest(UUID from, UUID target) {
        PlayerRef fromPlayerRef = Universe.get()
                                          .getPlayer(from);
        PlayerRef targetPlayerRef = Universe.get()
                                            .getPlayer(target);
        CheckTool.checkPlayerRef(fromPlayerRef);
        CheckTool.checkPlayerRef(targetPlayerRef);
        List<TpaRequestData> tpaRequestList = tpaDataMap.get(target);
        if (tpaRequestList != null) {
            TpaRequestData tpaRequestData = tpaRequestList.stream()
                                                          .filter(e -> from.equals(e.getRequestPlayer()))
                                                          .findFirst()
                                                          .orElse(null);
            if (tpaRequestData != null) {
                int lastCd = getDurationSeconds(System.currentTimeMillis(), tpaRequestData.getTimestamp());
                if (lastCd < TELEPORT_COOLDOWN) {
                    return TELEPORT_COOLDOWN - lastCd;
                } else {
                    tpaRequestList.remove(tpaRequestData);
                    return 0;
                }
            } else {
                return 0;
            }
        }
        return 0;
    }
}
