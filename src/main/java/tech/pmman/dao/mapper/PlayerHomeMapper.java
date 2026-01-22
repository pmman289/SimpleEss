package tech.pmman.dao.mapper;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import tech.pmman.core.db.TableMapper;
import tech.pmman.pojo.db.PlayerHome;

import java.util.List;

@RegisterBeanMapper(PlayerHome.class)
public interface PlayerHomeMapper extends TableMapper {
    @Override
    default String getCreateTableSql() {
        return """
                CREATE TABLE IF NOT EXISTS "player_home" (
                  "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                  "uuid" text NOT NULL,
                  "home_name" TEXT NOT NULL,
                  "world_uuid" TEXT NOT NULL,
                  "location" TEXT NOT NULL,
                  UNIQUE ("uuid" ASC, "home_name" ASC)
                );
                """;
    }

    @SqlUpdate("""
            INSERT INTO player_home (uuid, home_name, world_uuid, location)
            VALUES (:uuid, :homeName, :worldUUID, :location)
            """)
    int insert(@BindBean PlayerHome playerHome);

    @SqlBatch("""
            INSERT INTO player_home (uuid, home_name, world_uuid, location)
            VALUES (:uuid, :homeName, :worldUUID, :location)
            """)
    void insertBatch(@BindBean List<PlayerHome> playerHomeList);

    @SqlUpdate("""
            UPDATE player_home
            SET location = :location, world_uuid = :worldUUID
            WHERE uuid = :uuid AND home_name = :homeName
            """)
    int updateLocationAndWorld(@BindBean PlayerHome playerHome);

    @SqlQuery("""
            SELECT uuid, home_name, world_uuid, location from player_home
            WHERE uuid = :uuid
            """)
    List<PlayerHome> queryPlayerHomes(@Bind("uuid") String uuid);
}
