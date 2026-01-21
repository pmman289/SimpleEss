package tech.pmman.dao.mapper;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import tech.pmman.core.db.TableMapper;

public interface PersistenceRecordMapper extends TableMapper {
    @Override
    default String getCreateTableSql() {
        return """
                CREATE TABLE IF NOT EXISTS "persistence_record" (
                  "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                  "uuid" text NOT NULL,
                  "key" TEXT NOT NULL,
                  "last_timestamp" integer NOT NULL
                );
                """;
    }

    @SqlUpdate("""
            INSERT INTO persistence_record (uuid, key, last_timestamp)
            VALUES (:uuid, :key, :lastTimestamp)
            """)
    int insert(@Bind("uuid") String uuid, @Bind("key") String key, @Bind("lastTimestamp") long lastTimestamp);

    @SqlUpdate("""
            UPDATE persistence_record
            SET last_timestamp = :lastTimestamp
            WHERE uuid = :uuid AND key = :key
            """)
    int update(@Bind("uuid") String uuid, @Bind("key") String key, @Bind("lastTimestamp") long lastTimestamp);

    @SqlUpdate("""
            DELETE persistence_record
            WHERE uuid = :uuid
            """)
    int deleteUserData(@Bind("uuid") String uuid);

    @SqlUpdate("""
            DELETE persistence_record
            WHERE uuid = :uuid AND key = :key
            """)
    int deleteUserKeyData(@Bind("uuid") String uuid, @Bind("key") String key);

    @SqlQuery("""
            SELECT last_timestamp from persistence_record
            WHERE uuid = :uuid AND key = :key
            """)
    Long queryLastTimestamp(@Bind("uuid") String uuid, @Bind("key") String key);
}
