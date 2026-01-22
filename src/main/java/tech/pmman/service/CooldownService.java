package tech.pmman.service;

import org.jdbi.v3.core.Jdbi;
import tech.pmman.DbManager;
import tech.pmman.dao.mapper.PersistenceRecordMapper;
import tech.pmman.pojo.db.PersistenceRecord;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CooldownService {
    /**
     * 当玩家尝试使用有cd的服务时调用
     *
     * @param uuid        uuid
     * @param serviceName 服务唯一名称
     * @param cooldown    服务的冷却时间
     * @return 剩余的冷却时间
     */
    public static int tryUseCooldownService(String uuid, String serviceName, int cooldown) {
        final long currentTimeMillis = System.currentTimeMillis();
        Jdbi db = DbManager.getInstance()
                           .get();
        return db.withExtension(PersistenceRecordMapper.class, mapper -> {
            Long lastTimestamp = mapper.queryLastTimestamp(uuid, serviceName);
            if (lastTimestamp == null) {
                // 插入记录
                mapper.insert(uuid, serviceName, currentTimeMillis);
                return 0;
            }
            int gapSecs = Math.toIntExact((currentTimeMillis - lastTimestamp) / 1000);
            if (gapSecs > cooldown) {
                mapper.update(uuid, serviceName, currentTimeMillis);
                return 0;
            }
            return cooldown - gapSecs;
        });
    }

    /**
     * 获取指定用户的所有kit冷却情况
     *
     * @param uuid   用户uuid
     * @param kitIds kit id列表
     * @return 冷却情况map
     */
    public static Map<String, Integer> getUserKitCooldownRecord(String uuid, List<String> kitIds) {
        // 统一处理前面加"kit.use."
        List<String> kitKeys = kitIds.stream()
                                     .map(i -> "kit.use." + i)
                                     .toList();
        List<PersistenceRecord> recordList = DbManager.getInstance()
                                                      .get()
                                                      .withExtension(
                                                              PersistenceRecordMapper.class,
                                                              dao -> dao.queryUserRecords(uuid, kitKeys));
        return recordList.stream()
                         .collect(
                                 Collectors.toMap(o -> o.getKey()
                                                        .split("\\.")[2],
                                         PersistenceRecord::getElapsedSeconds));
    }

    /**
     * 尝试使用kit
     *
     * @param uuid     uuid
     * @param kitId    kit id
     * @param cooldown kit冷却时间
     * @return 剩余时间，为-1时表示永远不可用
     */
    public static int tryUseKit(String uuid, String kitId, int cooldown) {
        String kitKey = "kit.use." + kitId;
        long currentTimeMillis = System.currentTimeMillis();
        return DbManager
                .getInstance()
                .get()
                .withExtension(
                        PersistenceRecordMapper.class,
                        dao -> {
                            Long lastTimestamp = dao.queryLastTimestamp(uuid, kitKey);
                            if (lastTimestamp == null) {
                                dao.insert(uuid, kitKey, currentTimeMillis);
                                return 0;
                            }
                            int gapSecs = Math.toIntExact((currentTimeMillis - lastTimestamp) / 1000);
                            if (gapSecs > cooldown) {
                                dao.update(uuid, kitKey, currentTimeMillis);
                                return 0;
                            }
                            return gapSecs - cooldown;
                        });
    }
}
