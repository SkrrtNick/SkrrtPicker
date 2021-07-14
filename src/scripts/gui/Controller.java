package scripts.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import org.tribot.api.General;
import org.tribot.util.Util;
import scripts.data.Profile;
import scripts.skrrt_api.events.Core;
import scripts.skrrt_api.util.functions.Logging;
import scripts.utilities.FileUtilities;

import java.io.File;
import java.io.IOException;

import static scripts.data.Vars.runningPrep;
import static scripts.data.Vars.runtimeSettings;

public class Controller extends AbstractGUIController {
    private String profile = "";
    @FXML
    private Pane ownerWindow;

    @FXML
    private TextField zCoordinate;

    @FXML
    private TextField yCoordinate;

    @FXML
    private TextField xCoordinate;

    @FXML
    private TextField itemName;

    @FXML
    private MenuItem save;

    @FXML
    private MenuItem saveAs;

    @FXML
    private MenuItem load;

    @FXML
    private MenuItem exit;
    @FXML
    private CheckMenuItem darkMode;
    @FXML
    private CheckBox useWorldhopping;

    @FXML
    private CheckBox useStaminas;

    @FXML
    private Button startBtn;

    @FXML
    void loadClicked(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Profile");
        fileChooser.setInitialDirectory(new File(Util.getWorkingDirectory() + Core.getProfileDirectory()));
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"));
        File file = fileChooser.showOpenDialog(ownerWindow.getScene().getWindow());
        if (file != null) {
            String fileName = file.getName();
            save.setDisable(false);
            profile = fileName;
            try {
                Profile settings = FileUtilities.gson.fromJson(new String(FileUtilities.loadFile(new File(Util.getWorkingDirectory().getAbsolutePath() + Core.getProfileDirectory() + "/" + fileName))), Profile.class);
                load(settings);
            } catch (IOException e) {
                General.println(e.getMessage());
            }
        } else {
            General.println(file);
        }
    }

    @FXML
    public void saveAsClicked(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        Profile settings = new Profile();
        save(settings);
        fileChooser.setTitle("Save Profile");
        fileChooser.setInitialDirectory(new File(Util.getWorkingDirectory() + Core.getProfileDirectory()));
        File file = fileChooser.showSaveDialog(ownerWindow.getScene().getWindow());
        if (file != null) {
            String fileName = file.getName();
            save.setDisable(false);
            if (!fileName.endsWith(".json")) {
                fileName += ".json";
            }
            profile = fileName;
            FileUtilities.createFile(FileUtilities.gson.toJson(settings), Core.getProfileDirectory() + "/" + fileName);
        }
    }


    @FXML
    void checkDarkMode(ActionEvent event) {
        if(darkMode.isSelected()){
            getGUI().getScene().getStylesheets().add("https://raw.githubusercontent.com/SkrrtNick/SkrrtPicker/master/src/scripts/gui/dark-mode.css");
        } else {
            getGUI().getScene().getStylesheets().remove("https://raw.githubusercontent.com/SkrrtNick/SkrrtPicker/master/src/scripts/gui/dark-mode.css");
        }
    }

    @FXML
    void exitClicked(ActionEvent event) {
        this.getGUI().close();
        runningPrep = false;
    }

    @FXML
    public void saveClicked(ActionEvent event) {
        Profile settings = new Profile();
        save(settings);
        FileUtilities.createFile(FileUtilities.gson.toJson(settings), Core.getProfileDirectory() + "/" + profile);
    }

    @FXML
    public void startScriptPressed() {
        Profile settings = new Profile();
        save(settings);
        FileUtilities.createFile(FileUtilities.gson.toJson(settings), Core.getProfileDirectory() + "/" + "last.json");
        runningPrep = true;
        runtimeSettings = settings;
        this.getGUI().close();
    }

    @Override
    public void initialize() {
        try {
            Profile settings = FileUtilities.gson.fromJson(new String(FileUtilities.loadFile(new File(Util.getWorkingDirectory().getAbsolutePath() + Core.getProfileDirectory() + "/last.json"))), Profile.class);
            load(settings);
            if(this.getGUI() != null && settings.darkModeEnabled){
                darkMode.fire();
            }
        } catch (IOException e) {
            Logging.debug("Wasn't able to load last.json");
        }
    }

    public void load(Profile settings) {
        xCoordinate.setText(String.valueOf(settings.getX()));
        yCoordinate.setText(String.valueOf(settings.getY()));
        zCoordinate.setText(String.valueOf(settings.getZ()));
        itemName.setText(settings.getPickupItemName());
        useWorldhopping.setSelected(settings.isShouldWorldHop());
        darkMode.setSelected(settings.isDarkModeEnabled());
        useStaminas.setSelected(settings.isShouldUseStaminas());
    }

    public void save(Profile settings) {
        settings.setX(Integer.parseInt(xCoordinate.getText()));
        settings.setY(Integer.parseInt(yCoordinate.getText()));
        settings.setZ(Integer.parseInt(zCoordinate.getText()));
        settings.setPickupItemName(String.valueOf(itemName.getText()));
        settings.setShouldUseStaminas(useStaminas.isSelected());
        settings.setDarkModeEnabled(darkMode.isSelected());
        settings.setShouldWorldHop(useWorldhopping.isSelected());
    }

}
