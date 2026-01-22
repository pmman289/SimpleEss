package tech.pmman.pojo.db;

import lombok.Data;
import tech.pmman.util.JsonUtil;

@Data
public class PlayerTpaSettings {
    public static final String NAME = "playerTpaSettings";

    private boolean enableAutoAccept;
    private boolean enableAutoDeny;
    private boolean disableTpa;

    public PlayerTpaSettings(boolean enableAutoAccept, boolean enableAutoDeny, boolean disableTpa) {
        this.enableAutoAccept = enableAutoAccept;
        this.enableAutoDeny = enableAutoDeny;
        this.disableTpa = disableTpa;
    }

    public PlayerTpaSettings() {
    }

    public String toJson() {
        return JsonUtil.toJson(this);
    }

    public static PlayerTpaSettings fromJson(String json) {
        return JsonUtil.fromJson(PlayerTpaSettings.class, json);
    }
}
