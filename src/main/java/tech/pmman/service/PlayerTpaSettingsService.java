package tech.pmman.service;

import tech.pmman.DbManager;
import tech.pmman.dao.mapper.PlayerSettingsMapper;
import tech.pmman.pojo.db.PlayerSettings;
import tech.pmman.pojo.db.PlayerTpaSettings;

public class PlayerTpaSettingsService {
    public static void setSettings(String uuid, PlayerTpaSettings tpaSettings) {
        PlayerSettings insertDo = new PlayerSettings();
        insertDo.setUuid(uuid);
        insertDo.setName(PlayerTpaSettings.NAME);
        insertDo.setSettings(tpaSettings.toJson());
        DbManager.getInstance()
                 .get()
                 .useExtension(PlayerSettingsMapper.class, dao -> {
                     // 先查询，如果存在就更新，否则插入
                     if (dao.querySettings(uuid, PlayerTpaSettings.NAME) != null) {
                         dao.updateSettings(insertDo);
                     } else {
                         dao.insert(insertDo);
                     }
                 });
    }

    public static PlayerTpaSettings getSettings(String uuid) {
        String playerSettings = DbManager.getInstance()
                                         .get()
                                         .withExtension(PlayerSettingsMapper.class,
                                                 dao -> dao.querySettings(uuid, PlayerTpaSettings.NAME));
        return PlayerTpaSettings.fromJson(playerSettings);
    }
}
