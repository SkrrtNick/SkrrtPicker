package scripts.api.util.functions;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.input.Keyboard;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.NPCChat;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.types.RSGroundItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import scripts.api.util.numbers.Reactions;
import scripts.entityselector.Entities;
import scripts.entityselector.finders.prefabs.GroundItemEntity;
import scripts.entityselector.finders.prefabs.NpcEntity;
import scripts.entityselector.finders.prefabs.ObjectEntity;

public class Interaction {

    public static RSGroundItem[] getGroundItems(String name) {
        RSGroundItem[] obj = Entities.find(GroundItemEntity::new)
                .nameEquals(name)
                .sortByDistance()
                .getResults();
        if (obj.length > 0) {
            return obj;
        }
        return obj;
    }

    public static RSObject[] getObjects(String name) {
        RSObject[] obj = Entities.find(ObjectEntity::new)
                .nameEquals(name)
                .sortByDistance()
                .getResults();
        if (obj.length > 0) {
            return obj;
        }
        return obj;
    }

    public static RSObject getObject(String name) {
        RSObject obj = Entities.find(ObjectEntity::new)
                .nameEquals(name)
                .sortByDistance()
                .getFirstResult();
        if (obj != null) {
            return obj;
        }
        return null;
    }

    public static RSGroundItem getGroundItem(String name) {
        RSGroundItem obj = Entities.find(GroundItemEntity::new)
                .nameEquals(name)
                .sortByDistance()
                .getFirstResult();
        if (obj != null) {
            return obj;
        }
        return null;
    }

    public static boolean clickNPC(String name, String action) {
        RSNPC[] npc = NPCs.findNearest(name);
        if (npc.length == 0) {
            return false;
        } else if (!npc[0].isClickable()) {
            npc[0].adjustCameraTo();
        } else if (!npc[0].isOnScreen()) {
            return false;
        }
        return DynamicClicking.clickRSNPC(npc[0], action);
    }

    public static boolean npcExists(String name) {
        RSNPC npc = Entities.find(NpcEntity::new)
                .nameEquals(name)
                .getFirstResult();
        return npc != null;
    }

    public static RSNPC getNPC(String name) {
        RSNPC npc = Entities.find(NpcEntity::new)
                .nameEquals(name)
                .getFirstResult();
        return npc;
    }

    public static void handleContinue() {
        if (Interfaces.isInterfaceSubstantiated(NPCChat.getClickContinueInterface()) || Interfaces.isInterfaceSubstantiated(11) || Interfaces.isInterfaceSubstantiated(229)) {
            int decision = Reactions.getDecision(3);
            General.sleep(Reactions.getNormal());
            if (decision <= 100) {
                //does nothing intentionally
            }
            if (decision > 100 && decision <= 200) {
                Keyboard.typeString(" ");
                Logging.message("Pressing space to clear Click to Continue interface");
            }

            if (decision > 200 && decision <= 300) {
                if (Interfaces.isInterfaceSubstantiated(11)) {
                    Interfaces.get(11).getChild(4).click();
                } else if (Interfaces.isInterfaceSubstantiated(229)) {
                    Interfaces.get(229).getChild(2).click();
                } else {
                    NPCChat.clickContinue(true);
                }
                Logging.message("Clicking to clear Click to Continue interface");
            }
        }
    }

}

