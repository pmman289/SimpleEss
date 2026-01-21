package tech.pmman.service;

import org.jdbi.v3.core.Jdbi;
import tech.pmman.DbManager;
import tech.pmman.dao.mapper.PersistenceRecordMapper;

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
}
