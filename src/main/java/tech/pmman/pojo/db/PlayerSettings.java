package tech.pmman.pojo.db;

import lombok.Data;
import tech.pmman.util.JsonUtil;

@Data
public class PlayerSettings {
    private String uuid;
    private String name;
    private String settings;

    public <T> T getSettings(Class<T> type) {
        return JsonUtil.fromJson(type, settings);
    }
}
