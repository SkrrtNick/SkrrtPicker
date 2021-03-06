package scripts.tasks;

import dax.walker_engine.interaction_handling.NPCInteraction;
import org.tribot.api.General;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.WorldHopper;
import org.tribot.api2007.types.*;
import scripts.api.task.Priority;
import scripts.api.task.Task;
import scripts.api.util.antiban.Antiban;
import scripts.api.util.functions.*;
import scripts.api.util.numbers.Reactions;
import scripts.data.Vars;

import static scripts.data.Vars.*;

public class Picker implements Task {
    int attempt = 0;

    @Override
    public String toString() {
        return "Picking " + Vars.runtimeSettings.getPickupItemName();
    }

    @Override
    public Priority priority() {
        return Priority.MEDIUM;
    }

    @Override
    public boolean validate() {
        return Vars.initialCheck && !shouldBank;
    }

    @Override
    public void execute() {
        Antiban.activateRun();
        if (initItemCount == 0 && pickupItemID == 0) {
            if (Inventory07.getAll().length > 0) {
                pickupItemID = Inventory07.getAll()[0].getID();
            }
        }
        if (Inventory07.isFull()) {
            shouldBank = true;
        }
        if (Player07.distanceTo(Vars.customTile) > 30) {
            Traversing.walkTo(Vars.customArea);
        }
        Vars.customArea = new RSArea(Vars.customTile.translate(General.random(-3, -9), General.random(-3, -9)), Vars.customTile.translate(General.random(3, 9), General.random(3, 9)));
        if (!Player07.isMoving() && !Player07.isAnimating()) {
            if (Vars.groundItemObject) {
                RSGroundItem[] items = Interaction.getGroundItems(Vars.runtimeSettings.getPickupItemName());
                if (items.length > 0) {
                    RSItemDefinition itemDefinition = items[0].getDefinition();
                    if (itemDefinition != null) {
                        String[] actions = itemDefinition.getGroundActions();
                        if (!items[0].isClickable()) {
                            items[0].adjustCameraTo();
                        }
                        if (items[0].click(actions[0])) {
                            General.sleep(Reactions.getNormal());
                            Sleep.until(() -> inventCount < Inventory07.getAll().length);
                            if (Interfaces.isInterfaceSubstantiated(553)) {
                                Interfaces.closeAll();
                            }
                            if (NPCInteraction.isConversationWindowUp()) {
                                Interaction.handleContinue();
                            }
                            if (pickupItemID == 0 && Inventory07.getAll().length == 1) {
                                pickupItemID = Inventory07.getAll()[0].getID();
                            }
                        }
                    }

                } else {
                    attempt++;
                    General.sleep(Reactions.getNormal());
                    if (Vars.runtimeSettings.isShouldWorldHop() && attempt > 5) {
                        if (Vars.worldHopToP2P) {
                            Logging.message("WorldHopper Event", "Hopping to Random P2P world");
                            WorldHopper.changeWorld(WorldHopper.getRandomWorld(true, false));
                        } else {
                            Logging.message("WorldHopper Event", "Hopping to Random F2P world");
                            WorldHopper.changeWorld(WorldHopper.getRandomWorld(false, false));
                        }
                    }
                    if (!Vars.customArea.contains(Player07.getPosition())) {
                        Traversing.walkTo(Vars.customArea);
                    }
                }
            }
            if (Vars.interactableObject) {
                //TODO interactable object handling
                RSObject[] items = Interaction.getObjects(Vars.runtimeSettings.getPickupItemName());
                if (items.length > 0) {
                    RSObjectDefinition objectDefinition = items[0].getDefinition();
                    if (objectDefinition != null) {
                        String[] actions = objectDefinition.getActions();
                        if (!items[0].isClickable()) {
                            items[0].adjustCameraTo();
                        }
                        if (items[0].click(actions[0])) {
                            General.sleep(Reactions.getNormal());
                            Sleep.until(() -> inventCount < Inventory07.getAll().length);
                            if (Interfaces.isInterfaceSubstantiated(553)) {
                                Interfaces.closeAll();
                            }
                            if (NPCInteraction.isConversationWindowUp()) {
                                Interaction.handleContinue();
                            }
                            if (pickupItemID == 0 && Inventory07.getAll().length == 1) {
                                pickupItemID = Inventory07.getAll()[0].getID();
                            }
                        } else {
                            attempt++;
                            General.sleep(Reactions.getNormal());
                            if (Vars.runtimeSettings.isShouldWorldHop() && attempt > 5) {
                                if (Vars.worldHopToP2P) {
                                    Logging.message("WorldHopper Event", "Hopping to Random P2P world");
                                    WorldHopper.changeWorld(WorldHopper.getRandomWorld(true, false));
                                } else {
                                    Logging.message("WorldHopper Event", "Hopping to Random F2P world");
                                    WorldHopper.changeWorld(WorldHopper.getRandomWorld(false, false));
                                }
                            }
                            if (!Vars.customArea.contains(Player07.getPosition())) {
                                Traversing.walkTo(Vars.customArea);
                            }
                        }
                    }

                }
            }
        }
    }
}