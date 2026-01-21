package tech.pmman.core.db;

import org.jdbi.v3.core.Jdbi;

public interface DatabaseProvider {
    void init();

    Jdbi get();
}
