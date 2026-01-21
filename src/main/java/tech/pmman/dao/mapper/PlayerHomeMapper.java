package tech.pmman.dao.mapper;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import tech.pmman.core.db.TableMapper;
import tech.pmman.pojo.db.PlayerHomeEntry;

import java.util.List;

@RegisterBeanMapper(PlayerHomeEntry.class)
public interface PlayerHomeMapper extends TableMapper {
    @Override
    default String getCreateTableSql() {
        return """
                CREATE TABLE IF NOT EXISTS "player_home" (
                  "id" INTEGER NOT NULL,
                  "uuid" text NOT NULL,
                  "home_name" TEXT NOT NULL,
                  "world_uuid" TEXT NOT NULL,
                  "location" TEXT NOT NULL,
                  PRIMARY KEY ("id"),
                  UNIQUE ("uuid" ASC, "home_name" ASC)
                );
                """;
    }

    @SqlUpdate("""
            INSERT INTO player_home (uuid, home_name, world_uuid, location)
            VALUES (:uuid, :homeName, :worldUUID, :location)
            """)
    int insert(@BindBean PlayerHomeEntry playerHomeEntry);

    @SqlUpdate("""
            UPDATE player_home
            SET location = :location, world_uuid = :worldUUID
            WHERE uuid = :uuid AND home_name = :homeName
            """)
    int updateLocationAndWorld(@BindBean PlayerHomeEntry playerHomeEntry);

    @SqlQuery("""
            SELECT uuid, home_name, world_uuid, location from player_home
            WHERE uuid = :uuid
            """)
    List<PlayerHomeEntry> queryPlayerHomes(@Bind("uuid") String uuid);
}
