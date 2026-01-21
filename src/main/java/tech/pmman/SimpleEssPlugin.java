package tech.pmman;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;
import tech.pmman.events.EventListener;
import tech.pmman.events.MemClearListener;
import tech.pmman.service.TpaService;
import tech.pmman.util.MessageTool;

import javax.annotation.Nonnull;

public class SimpleEssPlugin extends JavaPlugin {
    public static final String VERSION = "v0.0.4-beta";

    private static SimpleEssPlugin INSTANCE;

    public SimpleEssPlugin(@Nonnull JavaPluginInit init) {
        super(init);
        ConfigManager.initConfig(this);
        INSTANCE = this;
    }

    public static SimpleEssPlugin getInstance() {
        return INSTANCE;
    }

    public <T> Config<T> registerConfig(String name, BuilderCodec<T> configCodec) {
        return withConfig(name, configCodec);
    }

    @Override
    protected void setup() {
        // 加载数据库
        DbManager.getInstance().init();
        ConfigManager.setup();
        TpaService.loadConfig();
        MessageTool.loadConfig();
        // 注册命令
        CommandRegister.register(getCommandRegistry());
        // 注册事件
        EventListener.register(getEventRegistry());
        MemClearListener.register(getEventRegistry());
    }

    @Override
    protected void shutdown() {
        ConfigManager.saveAll();
    }
}