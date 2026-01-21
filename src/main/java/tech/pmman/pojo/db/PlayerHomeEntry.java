package tech.pmman.pojo.db;

import lombok.Data;
import tech.pmman.pojo.Location;

@Data
public class PlayerHomeEntry {
    private String uuid;
    private String homeName;
    private String worldUUID;
    private Location location;

    public PlayerHomeEntry(String uuid, String homeName, String worldUUID, Location location) {
        this.uuid = uuid;
        this.homeName = homeName;
        this.worldUUID = worldUUID;
        this.location = location;
    }

    public PlayerHomeEntry() {
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
