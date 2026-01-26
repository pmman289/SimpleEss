package tech.pmman;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import tech.pmman.core.db.DatabaseProvider;
import tech.pmman.core.db.TableMapper;
import tech.pmman.dao.mapper.PersistenceRecordMapper;
import tech.pmman.dao.mapper.PlayerHomeMapper;
import tech.pmman.dao.mapper.PlayerSettingsMapper;
import tech.pmman.dao.mapper.PlayerTeleportHistoryMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class DbManager implements DatabaseProvider {
    private static DbManager INSTANCE;

    private static final List<Class<? extends TableMapper>> ACTIVE_TABLE = List.of(
            PersistenceRecordMapper.class,
            PlayerHomeMapper.class,
            PlayerTeleportHistoryMapper.class,
            PlayerSettingsMapper.class
    );

    private Jdbi JDBI;

    private DbManager() {
    }

    public static DatabaseProvider getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DbManager();
        }
        return INSTANCE;
    }

    public void init() {
        if (JDBI != null) {
            throw new IllegalStateException("DbManager already initialized");
        }
        try {
            Class.forName("org.sqlite.JDBC");
            Files.createDirectories(SimpleEssPlugin.getInstance()
                                                   .getDataDirectory());
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
        Path dbPath = SimpleEssPlugin.getInstance()
                                     .getDataDirectory()
                                     .resolve("data.db");
        String url = "jdbc:sqlite:" + dbPath;
        JDBI = Jdbi.create(url);
        JDBI.installPlugin(new SqlObjectPlugin());
        JDBI.useHandle(handle -> handle.execute("PRAGMA foreign_keys = ON"));
        // 开始初始化建表
        JDBI.useHandle(handle -> {
            for (Class<? extends TableMapper> aClass : ACTIVE_TABLE) {
                TableMapper mapper = handle.attach(aClass);
                handle.execute(mapper.getCreateTableSql());
            }
        });
    }

    public Jdbi get() {
        if (JDBI == null) throw new IllegalStateException("Plugin not init");
        return JDBI;
    }
}
