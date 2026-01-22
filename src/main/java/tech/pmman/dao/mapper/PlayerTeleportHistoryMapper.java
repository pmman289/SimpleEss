package tech.pmman.dao.mapper;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import tech.pmman.core.db.TableMapper;
import tech.pmman.pojo.db.PlayerTeleportHistory;

import java.util.List;

@RegisterBeanMapper(PlayerTeleportHistory.class)
public interface PlayerTeleportHistoryMapper extends TableMapper {
    @Override
    default String getCreateTableSql() {
        return """
                CREATE TABLE IF NOT EXISTS "player_teleport_history" (
                  "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                  "uuid" text NOT NULL,
                  "world_uuid" text NOT NULL,
                  "location" text NOT NULL
                );
                """;
    }

    @SqlUpdate("""
            INSERT INTO player_teleport_history (uuid, world_uuid, location)
            VALUES (:uuid, :worldUUID, :location)
            """)
    int insertTeleportHistory(@BindBean PlayerTeleportHistory playerTeleportHistory);

    @SqlBatch("""
            INSERT INTO player_teleport_history (uuid, world_uuid, location)
            VALUES (:uuid, :worldUUID, :location)
            """)
    void insertBatch(@BindBean List<PlayerTeleportHistory> playerTeleportHistoryList);

    @SqlQuery("""
            SELECT uuid, world_uuid, location FROM player_teleport_history
            WHERE uuid = :uuid
            ORDER BY id DESC
            LIMIT 1
            """)
    PlayerTeleportHistory queryLastTeleportHistory(@Bind("uuid") String uuid);
}
