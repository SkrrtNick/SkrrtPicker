package scripts;

import org.tribot.api.General;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Arguments;
import org.tribot.script.interfaces.Ending;
import org.tribot.script.interfaces.Painting;
import org.tribot.script.interfaces.Starting;
import org.tribot.util.Util;
import scripts.data.Profile;
import scripts.gui.GUI;
import scripts.api.data_tracker.DataTracker;
import scripts.api.Core;
import scripts.api.task.Task;
import scripts.api.task.TaskSet;
import scripts.api.util.antiban.Antiban;
import scripts.api.util.functions.Logging;
import scripts.api.util.functions.Traversing;
import scripts.api.items.ItemID;
import scripts.api.util.numbers.Hourly;
import scripts.api.util.numbers.Prices;
import scripts.api.util.numbers.Randomisation;
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

public class SkrrtPicker extends Script implements Starting, PaintInfo, Painting, Arguments, Ending {

    @ScriptManifest(name = "SkrrtPicker", authors = {"SkrrtNick"}, category = "Tools")
    private URL fxml, darkModeURL;
    private GUI gui;
    private boolean launchGUI = true;
    private final FluffeesPaint SkrrtPaint = new FluffeesPaint(this, FluffeesPaint.PaintLocations.BOTTOM_LEFT_PLAY_SCREEN, new Color[]{new Color(255, 251, 255)}, "Trebuchet MS", new Color[]{new Color(0, 0, 0, 124)},
            new Color[]{new Color(179, 0, 0)}, 1, false, 5, 3, 0);
    DataTracker tracker = new DataTracker("https://api.skrrtscripts.com", "secret", "picker");

    @Override
    public void run() {
        Core.setRunning(true);
        Core.setStartTime(System.currentTimeMillis());
        Antiban.setPrintDebug(true);
        Randomisation.setMouseSpeed();

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
        tracker.start();
        trackStats();
        while (Core.isRunning()) {
            Task task = tasks.getValidTask();
            if (task != null) {
                trackStats();
                Core.setStatus(task.toString());
                task.execute();
            }
            General.sleep(20, 40);
        }

    }

    @Override
    public void onStart() {
        Core.setProfileDirectory("/Skrrt/Picker/Profiles");
        FileUtilities.createProfileDirectory(Core.getProfileDirectory());
        Traversing.setDaxKey(false);
    }

    @Override
    public String[] getPaintInfo() {
        if (pickupItemID != 0) {
            return new String[]{"SkrrtPicker V0.04", "Time ran: " + SkrrtPaint.getRuntimeString(), "Status: " + Core.getStatus(), "Items Picked: " + (pickedCount) + Hourly.getHourly(pickedCount), "Profit: " + getProfit() + Hourly.getHourly(getProfit())};
        }
        return new String[]{"SkrrtPicker V0.04", "Time ran: " + SkrrtPaint.getRuntimeString(), "Status: " + Core.getStatus(), "Items Picked: " + pickedCount};

    }


    @Override
    public void onPaint(Graphics graphics) {
        SkrrtPaint.paint(graphics);
    }

    @Override
    public void passArguments(HashMap<String, String> hashMap) {
        Core.setProfileDirectory("/Skrrt/Picker/Profiles");
        FileUtilities.createProfileDirectory(Core.getProfileDirectory());
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
        tracker.stop();
    }

    void trackStats() {
        tracker.trackNumber("runtime", getRunningTime());
        tracker.trackNumber("pickedItems", pickedCount);
        tracker.trackNumber("profit", getProfit());
    }

    int getProfit() {
        if(pickupItemID == 0){
            return 0;
        }
        if (Prices.getScaledPrice(pickupItemID, pickedCount).isPresent() && Prices.getScaledPrice(ItemID.STAMINA_POTION1, sipsTaken).isPresent()) {
            return Prices.getScaledPrice(pickupItemID, pickedCount).get() - Prices.getScaledPrice(ItemID.STAMINA_POTION1, sipsTaken).get();
        } return 0;
    }

}