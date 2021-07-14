package scripts.data;

import lombok.Getter;
import lombok.Setter;

public class Profile {
    @Getter
    @Setter
    public boolean
    shouldUseStaminas,
    shouldWorldHop,
    darkModeEnabled;
    @Getter @Setter
    public int x,y,z;
    @Getter @Setter
    public String pickupItemName;
}
