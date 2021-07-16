package scripts;

import org.tribot.api.General;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Game;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Arguments;
import org.tribot.script.interfaces.Ending;
import org.tribot.script.interfaces.Painting;
import org.tribot.script.interfaces.Starting;
import org.tribot.util.Util;
import scripts.data.Profile;
import scripts.gui.GUI;
import scripts.skrrt_api.events.Core;
import scripts.skrrt_api.listeners.inventory.InventoryListener;
import scripts.skrrt_api.listeners.inventory.InventoryObserver;
import scripts.skrrt_api.task.Task;
import scripts.skrrt_api.task.TaskSet;
import scripts.skrrt_api.util.antiban.Antiban;
import scripts.skrrt_api.util.functions.Banking07;
import scripts.skrrt_api.util.functions.Logging;
import scripts.skrrt_api.util.functions.Traversing;
import scripts.skrrt_api.util.items.ItemID;
import scripts.skrrt_api.util.numbers.Hourly;
import scripts.skrrt_api.util.numbers.Prices;
import scripts.skrrt_api.util.numbers.SeedGenerator;
import scripts.tasks.Banking;
import scripts.tasks.IntitialCheck;
import scripts.tasks.Picker;
import scripts.utilities.FileUtilities;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import static scripts.data.Vars.*;

public class SkrrtPicker extends Script implements Starting, PaintInfo, Painting, Arguments, Ending, InventoryListener {

    @ScriptManifest(name = "SkrrtPicker", authors = {"SkrrtNick"}, category = "Tools")
    private URL fxml, darkModeURL;
    private GUI gui;
    private boolean launchGUI = true;
    private int i = 0;
    private final FluffeesPaint SkrrtPaint = new FluffeesPaint(this, FluffeesPaint.PaintLocations.BOTTOM_LEFT_PLAY_SCREEN, new Color[]{new Color(255, 251, 255)}, "Trebuchet MS", new Color[]{new Color(0, 0, 0, 124)},
            new Color[]{new Color(179, 0, 0)}, 1, false, 5, 3, 0);

    @Override
    public void run() {
        Core.setRunning(true);
        Core.setStartTime(System.currentTimeMillis());
        SeedGenerator seed = new SeedGenerator();
        Antiban.setPrintDebug(true);
        while (seed.getPlayerSeed() == 0) {
            seed.generateRandom();
            Core.setPlayerSeed(seed.getPlayerSeed());
            if ((int) seed.getPlayerSeed() * 100 > 200) {
                Mouse.setSpeed(General.random(130, 200));
            } else {
                Mouse.setSpeed((int) (seed.getPlayerSeed() * 100));
            }
        }
        if (launchGUI) {
            try {
                fxml = new URL("https://raw.githubusercontent.com/SkrrtNick/SkrrtPicker/master/src/scripts/gui/gui.fxml");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            gui = new GUI(fxml);
            gui.show();
            while (gui.isOpen()) {
                sleep(500);
            }
        }

        TaskSet tasks = new TaskSet(new IntitialCheck(), new Banking(), new Picker());

        while (Core.isRunning) {
            Task task = tasks.getValidTask();
            if (task != null) {
                Core.setStatus(task.toString());
                task.execute();
            }
            General.sleep(20,40);
        }

    }

    @Override
    public void onStart() {
        Core.setProfileDirectory("/Skrrt/Picker/Profiles");
        FileUtilities.createProfileDirectory(Core.profileDirectory);
        Traversing.setDaxKey(false);
    }

    @Override
    public String[] getPaintInfo() {
        if(pickupItemID!=0){
            return new String[]{"SkrrtPicker V0.03 alpha", "Time ran: " + SkrrtPaint.getRuntimeString(), "Status: " + Core.getStatus(), "Items Picked: " + (pickedCount) + Hourly.getHourly(pickedCount), "Profit: " + ((Prices.getPrices(pickupItemID).get() * pickedCount) - (Prices.getPrices(ItemID.STAMINA_POTION1).get()) * sipsTaken) + Hourly.getHourly(Prices.getPrices(pickupItemID).get() * pickedCount)};
        }
        return new String[]{"SkrrtPicker V0.03 alpha", "Time ran: " + SkrrtPaint.getRuntimeString(), "Status: " + Core.getStatus(), "Items Picked: " + pickedCount};

    }


    @Override
    public void onPaint(Graphics graphics) {
        SkrrtPaint.paint(graphics);
    }

    @Override
    public void passArguments(HashMap<String, String> hashMap) {
        Core.setProfileDirectory("/Skrrt/Picker/Profiles");
        FileUtilities.createProfileDirectory(Core.profileDirectory);
        String scriptSelect = hashMap.get("custom_input");
        String clientStarter = hashMap.get("autostart");
        String input = clientStarter != null ? clientStarter : scriptSelect;
        String[] settings = input.split(",");
        if (settings.length > 0) {
            for (String s : settings) {
                if (s.contains("settings:")) {
                    profileName = s.split(":")[1] != null ? s.split(":")[1] : null;
                }
            }
        }
        if (profileName != null) {
            if (!profileName.endsWith(".json")) {
                profileName += ".json";
            }
            try {
                runtimeSettings = FileUtilities.gson.fromJson(new String(FileUtilities.loadFile(new File(Util.getWorkingDirectory().getAbsolutePath() + Core.getProfileDirectory() + "/" + profileName))), Profile.class);
                launchGUI = false;
                runningPrep = true;
            } catch (IOException e) {
                Logging.debug(e.getMessage());
                Logging.debug("Unable to locate profile");
            }
        }
    }


    @Override
    public void onEnd() {
        Antiban.destroy();
    }

    @Override
    public void inventoryItemGained(int id, int count) {
        initItemCount = initItemCount++;
    }

    @Override
    public void inventoryItemLost(int id, int count) {

    }
}