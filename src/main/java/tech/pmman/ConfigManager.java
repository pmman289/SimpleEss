package tech.pmman;

import com.hypixel.hytale.server.core.util.Config;
import tech.pmman.config.KitConfig;
import tech.pmman.config.PluginConfig;
import tech.pmman.deprecated.data.PlayerHomeConfig;
import tech.pmman.deprecated.data.PlayerLastTeleportConfig;
import tech.pmman.deprecated.data.PlayerTpaSettingsConfig;
import tech.pmman.dao.mapper.PlayerHomeMapper;
import tech.pmman.dao.mapper.PlayerSettingsMapper;
import tech.pmman.dao.mapper.PlayerTeleportHistoryMapper;
import tech.pmman.pojo.Location;
import tech.pmman.deprecated.PlayerLocationEntry;
import tech.pmman.deprecated.PlayerTpaSettingsConfigEntry;
import tech.pmman.pojo.db.PlayerHome;
import tech.pmman.pojo.db.PlayerSettings;
import tech.pmman.pojo.db.PlayerTeleportHistory;
import tech.pmman.pojo.db.PlayerTpaSettings;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class ConfigManager {
    public static Config<PluginConfig> PLUGIN_CONFIG;
    public static Config<KitConfig> KIT_CONFIG;

    private static Config<PlayerHomeConfig> PLAYER_HOME_DATA;
    private static Config<PlayerLastTeleportConfig> PLAYER_LAST_TELEPORT_DATA;
    private static Config<PlayerTpaSettingsConfig> PLAYER_TPA_SETTINGS_DATA;

    public static Config<?>[] ACTIVE_CONFIG;

    public static void initConfig(SimpleEssPlugin plugin) {
        PLUGIN_CONFIG = plugin.registerConfig("pluginConfig", PluginConfig.CODEC);
        KIT_CONFIG = plugin.registerConfig("kitConfig", KitConfig.CODEC);

        ACTIVE_CONFIG = new Config[]{
                PLUGIN_CONFIG,
                KIT_CONFIG
        };
        migrateOldConfigData(plugin);
    }

    public static void setup() {
        loadAll();
        saveAll();
    }

    public static void reload(){
        loadAll();
    }

    /**
     * 从配置文件存储迁移到sqlite
     */
    public static void migrateOldConfigData(SimpleEssPlugin plugin) {
        Path oldDataPath = plugin.getDataDirectory()
                                 .resolve("data");
        // 如果data文件夹存在且没有成功迁移过再判断
        if (Files.exists(oldDataPath) && !Files.exists(oldDataPath.resolve("migration.lock"))) {
            plugin.getLogger()
                  .at(Level.WARNING)
                  .log("Legacy configuration detected. Starting migration. Please DO NOT shut down the server. Old data files will be moved once complete");
            PLAYER_HOME_DATA = plugin.registerConfig("data/homeData", PlayerHomeConfig.CODEC);
            PLAYER_LAST_TELEPORT_DATA = plugin.registerConfig("data/playTeleportHistory", PlayerLastTeleportConfig.CODEC);
            PLAYER_TPA_SETTINGS_DATA = plugin.registerConfig("data/playerTpaSettingsConfig", PlayerTpaSettingsConfig.CODEC);
            // 开启事务
            try {
                DbManager.getInstance()
                         .get()
                         .useTransaction(handle -> {
                             if (Files.exists(oldDataPath.resolve("homeData.json"))) {
                                 // 读取配置文件
                                 PLAYER_HOME_DATA.load();
                                 migratePlayerHomeData(handle.attach(PlayerHomeMapper.class));
                             }
                             if (Files.exists(oldDataPath.resolve("playTeleportHistory.json"))) {
                                 PLAYER_LAST_TELEPORT_DATA.load();
                                 migratePlayTeleportHistory(handle.attach(PlayerTeleportHistoryMapper.class));
                             }
                             if (Files.exists(oldDataPath.resolve("playerTpaSettingsConfig.json"))) {
                                 PLAYER_TPA_SETTINGS_DATA.load();
                                 migratePlayerTeleportSettings(handle.attach(PlayerSettingsMapper.class));
                             }
                             // 创建lock文件标记迁移完成
                             Files.createFile(oldDataPath.resolve("migration.lock"));
                         });
            } catch (Exception e) {
                plugin.getLogger()
                      .at(Level.WARNING)
                      .log("An error occurred during migration. Changes have been rolled back: " + e);
                return;
            }
            plugin.getLogger()
                  .at(Level.WARNING)
                  .log("Data migration completed successfully");
        }
    }

    private static void migratePlayerHomeData(PlayerHomeMapper mapper) {
        Map<String, Map<String, PlayerLocationEntry>> oldData = PLAYER_HOME_DATA.get()
                                                                                .getHomeData();
        List<PlayerHome> insertList = new ArrayList<>();
        for (Map.Entry<String, Map<String, PlayerLocationEntry>> userData : oldData.entrySet()) {
            for (Map.Entry<String, PlayerLocationEntry> homeData : userData.getValue()
                                                                           .entrySet()) {
                PlayerHome newData = new PlayerHome(userData.getKey(), homeData.getKey(), homeData.getValue()
                                                                                                  .getWorldUUID(), new Location(homeData.getValue()
                                                                                                                                        .getPosition(), homeData.getValue()
                                                                                                                                                                .getRotation()));
                insertList.add(newData);
            }
        }
        mapper.insertBatch(insertList);
    }

    private static void migratePlayTeleportHistory(PlayerTeleportHistoryMapper mapper) {
        Map<String, PlayerLocationEntry> oldData = PLAYER_LAST_TELEPORT_DATA.get()
                                                                            .getLastTeleportData();
        List<PlayerTeleportHistory> insertList = new ArrayList<>();
        for (Map.Entry<String, PlayerLocationEntry> tpData : oldData.entrySet()) {
            PlayerTeleportHistory insertDo = new PlayerTeleportHistory(tpData.getKey(), tpData.getValue()
                                                                                              .getWorldUUID(), new Location(tpData.getValue()
                                                                                                                                  .getPosition(), tpData.getValue()
                                                                                                                                                        .getRotation()));
            insertList.add(insertDo);
        }
        mapper.insertBatch(insertList);
    }

    private static void migratePlayerTeleportSettings(PlayerSettingsMapper mapper) {
        Map<String, PlayerTpaSettingsConfigEntry> oldData = PLAYER_TPA_SETTINGS_DATA.get()
                                                                                    .getPlayerSettings();
        List<PlayerSettings> insertList = new ArrayList<>();
        for (Map.Entry<String, PlayerTpaSettingsConfigEntry> settingsData : oldData.entrySet()) {
            PlayerSettings insertDo = new PlayerSettings();
            insertDo.setUuid(settingsData.getKey());
            insertDo.setName(PlayerTpaSettings.NAME);
            insertDo.setSettings(new PlayerTpaSettings(settingsData.getValue()
                                                                   .isEnableAutoAccept(), settingsData.getValue()
                                                                                                      .isEnableAutoDeny(), settingsData.getValue()
                                                                                                                                       .isDisableTpa()).toJson());
            insertList.add(insertDo);
        }
        mapper.insertBatch(insertList);
    }

    public static void loadAll() {
        for (Config<?> config : ACTIVE_CONFIG) {
            config.load();
        }
    }

    public static void saveAll() {
        for (Config<?> config : ACTIVE_CONFIG) {
            config.save();
        }
    }
}
