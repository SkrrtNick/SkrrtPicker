package scripts.tasks;

import dax.teleports.Teleport;
import org.tribot.api.General;
import org.tribot.api2007.WorldHopper;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;
import scripts.data.Vars;
import scripts.api.task.Priority;
import scripts.api.task.Task;
import scripts.api.util.functions.*;

import static scripts.data.Vars.*;

public class IntitialCheck implements Task {

    @Override
    public String toString() {
        return "Initialising";
    }

    @Override
    public Priority priority() {
        return Priority.HIGH;
    }

    @Override
    public boolean validate() {
        return !initialCheck;
    }

    @Override
    public void execute() {
        initInventory = Inventory07.getAll();
        initItemCount = initInventory.length;
        if (WorldHopper.isMembers(WorldHopper.getWorld())) {
            worldHopToP2P = true;
        }
        customTile = new RSTile(runtimeSettings.getX(), runtimeSettings.getY(), runtimeSettings.getZ());
        Vars.customArea = new RSArea(Vars.customTile.translate(General.random(-3, -9), General.random(-3, -9)), Vars.customTile.translate(General.random(3, 9), General.random(3, 9)));
        if (!Inventory07.isEmpty()) {
            if (!Banking07.isInBank()) {
                Teleport.blacklistTeleports(Teleport.values());
                Traversing.walkToBank();
                Sleep.until(Banking07::isInBank);
            }
            if (Banking07.isInBank()) {
                Teleport.clearTeleportBlacklist();
                if (Banking07.openBank()) {
                    Sleep.until(Banking07::isBankLoaded);
                    if (Banking07.depositAll() > 0) {
                        Sleep.until(Inventory07::isEmpty);
                        Banking07.close();
                    }

                }
            }
        }

        if (!Vars.customArea.contains(Player07.getPosition())) {
            Traversing.walkTo(Vars.customArea);
        } else {
            if (Interaction.getObject(runtimeSettings.getPickupItemName()) != null) {
                Logging.message("Pickup Item is a RSObject");
                interactableObject = true;
            }
            if (Interaction.getGroundItem(runtimeSettings.getPickupItemName()) != null) {
                Logging.message("Pickup Item is a RSGroundItem");
                groundItemObject = true;
            }
            initialCheck = true;
        }

    }
}
