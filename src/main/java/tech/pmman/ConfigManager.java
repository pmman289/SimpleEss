package tech.pmman;

import com.hypixel.hytale.server.core.util.Config;
import tech.pmman.config.PluginConfig;
import tech.pmman.config.data.PlayerHomeConfig;
import tech.pmman.config.data.PlayerLastTeleportConfig;
import tech.pmman.config.data.PlayerTpaSettingsConfig;

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
                PLUGIN_CONFIG,
                PLAYER_HOME_DATA,
                PLAYER_LAST_TELEPORT_DATA,
                PLAYER_TPA_SETTINGS_DATA
        };
    }

    public static void load() {
        for (Config<?> config : ACTIVE_CONFIG) {
            config.load();
        }
    }

    public static void save(){
        for (Config<?> config : ACTIVE_CONFIG) {
            config.save();
        }
    }
}
