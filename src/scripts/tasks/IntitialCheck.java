package scripts.tasks;

import org.tribot.api.General;
import org.tribot.api2007.WorldHopper;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSTile;
import scripts.data.Vars;
import scripts.skrrt_api.task.Priority;
import scripts.skrrt_api.task.Task;
import scripts.skrrt_api.util.functions.*;

import static scripts.data.Vars.*;

public class IntitialCheck implements Task {

    @Override
    public String toString(){
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
        if(WorldHopper.isMembers(WorldHopper.getWorld())){
            worldHopToP2P = true;
        }
        customTile = new RSTile(runtimeSettings.getX(), runtimeSettings.getY(), runtimeSettings.getZ());
        Vars.customArea = new RSArea(Vars.customTile.translate(General.random(-3,-9),General.random(-3,-9)),Vars.customTile.translate(General.random(3,9),General.random(3,9)));
        if(!Vars.customArea.contains(Player07.getPosition())){
            Traversing.walkTo(Vars.customArea);
        } else {
            if(Interaction.getObject(runtimeSettings.getPickupItemName()) != null){
                Logging.message("Pickup Item is a RSObject");
                interactableObject = true;
            }
            if(Interaction.getGroundItem(runtimeSettings.getPickupItemName()) != null){
                Logging.message("Pickup Item is a RSGroundItem");
                groundItemObject = true;
            }
            initialCheck = true;
        }

    }
}
