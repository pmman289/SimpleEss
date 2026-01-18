package tech.pmman;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;
import tech.pmman.config.PluginConfig;
import tech.pmman.config.data.PlayerHomeConfig;
import tech.pmman.config.data.PlayerLastTeleportConfig;
import tech.pmman.config.data.PlayerTpaSettingsConfig;
import tech.pmman.events.EventListener;
import tech.pmman.service.TpaManager;
import tech.pmman.util.MessageTool;

import javax.annotation.Nonnull;

public class SimpleEssPlugin extends JavaPlugin {
    public static Config<PluginConfig> pluginConfig;

    public static Config<PlayerHomeConfig> playerHomeConfig;
    public static Config<PlayerLastTeleportConfig> playerLastTeleportConfig;
    public static Config<PlayerTpaSettingsConfig> playerTpaSettingsConfig;

    public SimpleEssPlugin(@Nonnull JavaPluginInit init) {
        super(init);
        initConfig();
    }

    private void initConfig() {
        pluginConfig = withConfig("pluginConfig", PluginConfig.CODEC);
        playerHomeConfig = withConfig("data/homeData", PlayerHomeConfig.CODEC);
        playerLastTeleportConfig = withConfig("data/playTeleportHistory", PlayerLastTeleportConfig.CODEC);
        playerTpaSettingsConfig = withConfig("data/playerTpaSettingsConfig", PlayerTpaSettingsConfig.CODEC);
    }

    private void loadConfig() {
        pluginConfig.load();
        pluginConfig.save();
        playerHomeConfig.load();
        playerHomeConfig.save();
        playerLastTeleportConfig.load();
        playerLastTeleportConfig.save();
        playerTpaSettingsConfig.load();
        playerTpaSettingsConfig.save();
    }

    @Override
    protected void setup() {
        loadConfig();
        TpaManager.loadConfig();
        MessageTool.loadConfig();
        // 注册命令
        CommandRegister.register(getCommandRegistry());
        // 注册事件
        EventListener.register(getEventRegistry());
    }

    @Override
    protected void shutdown() {
        pluginConfig.save();
        playerHomeConfig.save();
        playerLastTeleportConfig.save();
        playerTpaSettingsConfig.save();
    }
}