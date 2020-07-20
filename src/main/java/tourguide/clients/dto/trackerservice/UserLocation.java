package tourguide.clients.dto.trackerservice;

import java.util.UUID;

public class UserLocation {
    private final UUID userId;
    private Location latLongUser;

    public UserLocation(UUID userId, Location latLongUser) {
        this.userId = userId;
        this.latLongUser = latLongUser;
    }
}
