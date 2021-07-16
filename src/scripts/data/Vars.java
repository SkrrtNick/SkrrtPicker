package scripts.data;


import org.tribot.api.General;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSTile;
import scripts.skrrt_api.util.functions.Inventory07;

public class Vars {

    public static double playerSeed = 0;
    public static boolean runningPrep = false;
    public static boolean initialCheck = false;
    public static boolean groundItemObject = false;
    public static boolean interactableObject = false;
    public static String currentTask;
    public static String profileName;
    public static int x,y,z;
    public static Profile runtimeSettings = null;
    public static RSTile customTile = null;
    public static RSArea customArea = null;
    public static int initItemCount = 0;
    public static int inventCount = 0;
    public static int pickedCount = 0;
    public static int sipsTaken = 0;
    public static int pickupItemID = 0;
    public static RSItem[] initInventory = null;
    public static boolean worldHopToP2P = false;
    public static boolean shouldBank = false;
    //TASK RELATED

}




