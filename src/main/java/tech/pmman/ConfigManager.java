package tech.pmman;

import com.hypixel.hytale.server.core.util.Config;
import tech.pmman.config.PluginConfig;
import tech.pmman.config.data.PlayerHomeConfig;
import tech.pmman.config.data.PlayerLastTeleportConfig;
import tech.pmman.config.data.PlayerTpaSettingsConfig;

import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {
    public static Config<PluginConfig> PLUGIN_CONFIG;

    public static Config<PlayerHomeConfig> PLAYER_HOME_DATA;
    public static Config<PlayerLastTeleportConfig> PLAYER_LAST_TELEPORT_DATA;
    public static Config<PlayerTpaSettingsConfig> PLAYER_TPA_SETTINGS_DATA;

    public static Config<?>[] ACTIVE_CONFIG;

    public static void initConfig(SimpleEssPlugin plugin) {
        PLUGIN_CONFIG = plugin.registerConfig("pluginConfig", PluginConfig.CODEC);
        PLAYER_HOME_DATA = plugin.registerConfig("data/homeData", PlayerHomeConfig.CODEC);
        PLAYER_LAST_TELEPORT_DATA = plugin.registerConfig("data/playTeleportHistory", PlayerLastTeleportConfig.CODEC);
        PLAYER_TPA_SETTINGS_DATA = plugin.registerConfig("data/playerTpaSettingsConfig", PlayerTpaSettingsConfig.CODEC);

        ACTIVE_CONFIG = new Config[]{
                PLUGIN_CONFIG
        };
    }

    public static void setup(){
        loadAll();
        saveAll();
        migrationOldConfigData();
    }

    /**
     * 从配置文件存储迁移到sqlite
     */
    public static void migrationOldConfigData(){
        Path oldDataPath = SimpleEssPlugin.getInstance()
                                   .getDataDirectory()
                                   .resolve("data");
        // 如果data文件夹存在再判断
        if (Files.exists(oldDataPath)){
            if (Files.exists(oldDataPath.resolve("homeData.json"))){
                // 读取配置文件
                PLAYER_HOME_DATA.load();
            }
            if (Files.exists(oldDataPath.resolve("playTeleportHistory.json"))){
                PLAYER_LAST_TELEPORT_DATA.load();
            }
            if (Files.exists(oldDataPath.resolve("playerTpaSettingsConfig.json"))){
                PLAYER_TPA_SETTINGS_DATA.load();
            }
        }
    }

    public static void loadAll() {
        for (Config<?> config : ACTIVE_CONFIG) {
            config.load();
        }
    }

    public static void saveAll(){
        for (Config<?> config : ACTIVE_CONFIG) {
            config.save();
        }
    }
}
