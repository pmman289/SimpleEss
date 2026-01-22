package tech.pmman.pojo.db;

import lombok.Data;

@Data
public class PersistenceRecord {
    private String uuid;
    private String key;
    private Long lastTimestamp;

    public int getElapsedSeconds() {
        if (lastTimestamp == null) return -1;
        return Math.toIntExact((System.currentTimeMillis() - lastTimestamp) / 1000);
    }
}
