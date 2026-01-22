package tech.pmman.dao.mapper;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import tech.pmman.core.db.TableMapper;
import tech.pmman.pojo.db.PlayerSettings;

import java.util.List;

@RegisterBeanMapper(PlayerSettings.class)
public interface PlayerSettingsMapper extends TableMapper {
    @Override
    default String getCreateTableSql() {
        return """
                CREATE TABLE IF NOT EXISTS "player_settings" (
                  "id" INTEGER NOT NULL,
                  "uuid" text NOT NULL,
                  "name" TEXT NOT NULL,
                  "json" TEXT NOT NULL,
                  PRIMARY KEY ("id"),
                  UNIQUE ("uuid", "name")
                );
                """;
    }

    @SqlBatch("""
            INSERT INTO player_settings (uuid, name, json)
            VALUES (:uuid, :name, :settings)
            """)
    void insertBatch(@BindBean List<PlayerSettings> playerSettingsList);

    @SqlUpdate("""
            INSERT INTO player_settings (uuid, name, json)
            VALUES (:uuid, :name, :settings)
            """)
    int insert(@BindBean PlayerSettings playerSettings);

    @SqlQuery("""
            SELECT json FROM player_settings
            WHERE uuid = :uuid AND name = :name
            """)
    String querySettings(@Bind("uuid") String uuid, @Bind("name") String name);

    @SqlUpdate("""
            UPDATE player_settings
            SET json = :settings
            WHERE uuid = :uuid AND name = :name
            """)
    int updateSettings(@BindBean PlayerSettings playerSettings);
}
