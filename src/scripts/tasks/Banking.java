package scripts.tasks;

import org.tribot.api.General;
import org.tribot.api2007.Game;
import org.tribot.api2007.Options;
import org.tribot.api2007.types.RSItem;
import scripts.data.Vars;
import scripts.skrrt_api.task.Priority;
import scripts.skrrt_api.task.Task;
import scripts.skrrt_api.util.functions.*;
import scripts.skrrt_api.util.items.ItemCollections;

import static scripts.data.Vars.*;

public class Banking implements Task {

    @Override
    public String toString() {
        return "Banking items";
    }

    @Override
    public Priority priority() {
        return Priority.HIGH;
    }

    @Override
    public boolean validate() {
        return Vars.initialCheck && shouldBank;
    }

    @Override
    public void execute() {

        if (Inventory07.isFull() && !Banking07.isInBank()) {
            Logging.message("Walker", "Traversing to Closest Bank");
            Traversing.walkToBank();
        }
        if (!Inventory07.isEmpty() && Banking07.isInBank()) {
            if (Banking07.openBank()) {
                Sleep.until(Banking07::isBankLoaded);
                pickedCount = pickedCount + (Inventory07.getAll().length - initItemCount);
                if (Banking07.depositAll() > 0) {
                    initItemCount = 0;
                    Sleep.until(Inventory07::isEmpty);
                }
            }
        }

        if (runtimeSettings.isShouldUseStaminas() && Game.getRunEnergy() < General.random(15, 25)) {
            RSItem[] staminaPotion = Banking07.find(ItemCollections.getStaminaPotions());
            if (staminaPotion.length > 0) {
                Logging.message("StaminaEvent", "We have stamina potions and we should drink 1");
                if (Banking07.withdraw(1, staminaPotion[0].getID())) {
                    Sleep.until(() -> !Inventory07.isEmpty());
                }
            }
            staminaPotion = Inventory07.find(ItemCollections.getStaminaPotions());
            if (staminaPotion.length > 0) {
                if (staminaPotion[0].click("Drink")) {
                    Logging.message("StaminaEvent", "Sipped a Stam!");
                    sipsTaken++;
                    Sleep.until(()->!Inventory07.isEmpty());
                    if (!Game.isRunOn()) {
                        if (Options.setRunEnabled(true)) {
                            Sleep.until(Game::isRunOn);
                        }
                    }
                    if (Banking07.depositAll() > 0) {
                        Sleep.until(Inventory07::isEmpty);
                    }
                }
            }
        }

        if (Inventory07.isEmpty()) {
            Banking07.close();
            shouldBank = false;
        }

    }
}
