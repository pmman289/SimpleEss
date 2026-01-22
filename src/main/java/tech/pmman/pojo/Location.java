package tech.pmman.pojo;

import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import lombok.Data;
import tech.pmman.util.JsonUtil;

@Data
public class Location {
    private Vector3d position;
    private Vector3f rotation;

    public Location(Vector3d position, Vector3f rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public Location() {
    }

    /**
     * 将json字符串转换为Location
     *
     * @param json json
     * @return 对象
     */
    public static Location fromString(String json) {
        return JsonUtil.fromJson(Location.class, json);
    }

    public String toJson() {
        return JsonUtil.toJson(this);
    }
}
