package tech.pmman;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import tech.pmman.core.db.DatabaseProvider;

import java.nio.file.Path;

public class DbManager implements DatabaseProvider {
    private static DbManager INSTANCE;

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
        Path dbPath = SimpleEssPlugin.getInstance()
                                     .getDataDirectory()
                                     .resolve("data.db");
        String url = "jdbc:sqlite:" + dbPath;
        JDBI = Jdbi.create(url);
        JDBI.installPlugin(new SqlObjectPlugin());
        JDBI.useHandle(handle -> handle.execute("PRAGMA foreign_keys = ON"));
    }

    public Jdbi get() {
        if (JDBI == null) throw new IllegalStateException("Plugin not init");
        return JDBI;
    }
}
