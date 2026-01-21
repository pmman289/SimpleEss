package tech.pmman.pojo.db;

import lombok.Data;
import tech.pmman.pojo.Location;

@Data
public class PlayerTeleportHistory {
    private String uuid;
    private String worldUUID;
    private Location location;

    public PlayerTeleportHistory(String uuid, String worldUUID, Location location) {
        this.uuid = uuid;
        this.worldUUID = worldUUID;
        this.location = location;
    }

    public PlayerTeleportHistory() {
    }

    public void setLocation(String locationStr) {
        location = Location.fromString(locationStr);
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getLocation() {
        return location.toJson();
    }

    public Location getLocationObj() {
        return location;
    }
}
