package tech.pmman.core.db;

public interface TableMapper {
    /**
     * 返回建表语句，用于在每次启动时建表
     *
     * @return 建表语句
     */
    String getCreateTableSql();
}
