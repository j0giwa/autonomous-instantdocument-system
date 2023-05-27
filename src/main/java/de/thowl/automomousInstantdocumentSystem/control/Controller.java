package de.thowl.automomousInstantdocumentSystem.control;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import de.thowl.automomousInstantdocumentSystem.Main;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * This Class is the controller of the class <emph>Gui</emph>
 * 
 * @author Jonas Schwind
 * @version 0.1.2
 * @see de.thowl.automomousInstantdocumentSystem.view.Gui
 */
public class Controller implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;
    // Sidebar
    @FXML
    private Button btnMainScene;
    @FXML
    private Button btnLatexScene;
    @FXML
    private Button btnDatabaseScene;

    // Button area
    @FXML
    private ComboBox<String> cmbType;
    @FXML
    private TextField txtAmount;
    @FXML
    private TextField txtChapters;
    @FXML
    private TextField txtChatGptPrompt;
    @FXML
    private CheckBox chkShuffle;
    @FXML
    private Button btnGenerateDocument;
    @FXML
    private Button btnChatGptGo;

    // Multipurpose TextArea
    @FXML
    private TextArea txtMultipurposeTextArea;

    /**
     * This method chages the scene to the specifiedd one
     * 
     * @param event     Actionevent from the current scene
     * @param sceneName scene to switch to
     */
    private void switchToScene(ActionEvent event, String sceneName) throws IOException {
        root = FXMLLoader.load(getClass().getClassLoader().getResource(sceneName + ".fxml"));
        root.getStylesheets().add(getClass().getClassLoader().getResource("styles.css").toExternalForm());
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This Methodd populates the Dropdownmenu <emph>cmbType</emph>
     */
    private void initialiseTypeDropdown() {
        String OS = Main.getOS();
        String snippetsDir = null;
        // determine OS specific file path
        if (OS.equals("Windows")) {
            snippetsDir = System.getenv("appdata") + "/aids/latex/";
        } else if (OS.equals("UNIX")) {
            snippetsDir = System.getenv("XDG_CONFIG_HOME") + "/aids/latex/";
        }
        File directory = new File(snippetsDir);
        String[] files = directory.list();
        ArrayList<String> dropdownItems = new ArrayList<String>();
        for (String file : files) {
            if (new File(snippetsDir + "/" + file).isDirectory()) {
                dropdownItems.add(file);
            }
        }
        cmbType.getItems().setAll(dropdownItems);
    }

    private void appendToMultiPurposeTextArea(String newString) {
        StringBuilder areaContent = new StringBuilder();
        areaContent.append(txtMultipurposeTextArea.getText());
        areaContent.append(newString);
        txtMultipurposeTextArea.setText(areaContent.toString());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Populate Dropdown menu
        try {
            initialiseTypeDropdown();
        } catch (NullPointerException e) {
        }
    }

    // Events
    @FXML
    private void btnMainSceneClick(ActionEvent event) {
        try {
            switchToScene(event, "MainScene");
        } catch (IOException e) {
            Alert errorAlert = new Alert(AlertType.ERROR);
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            errorAlert.setHeaderText("NumberFormatException");
            errorAlert.setContentText(sw.toString());
            errorAlert.showAndWait();
            return;
        }
    }

    @FXML
    private void btnLatexSceneClick(ActionEvent event) {
        try {
            switchToScene(event, "LatexScene");
        } catch (IOException e) {
            Alert errorAlert = new Alert(AlertType.ERROR);
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            e.printStackTrace();
            errorAlert.setHeaderText("NumberFormatException");
            errorAlert.setContentText(sw.toString());
            errorAlert.showAndWait();
            return;
        }
    }

    @FXML
    private void btnDatabaseSceneClick(ActionEvent event) {
        try {
            switchToScene(event, "DatabaseScene");
        } catch (IOException e) {
            Alert errorAlert = new Alert(AlertType.ERROR);
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            errorAlert.setHeaderText("NumberFormatException");
            errorAlert.setContentText(sw.toString());
            errorAlert.showAndWait();
            return;
        }
    }

    /**
     * This method contains the logic behind the "Generate" Button on the GUI
     * Exected when button is pressed
     */
    @FXML
    private void btnGenerateDocumentClick(ActionEvent event) {
        Alert errorAlert = new Alert(AlertType.ERROR);
        // Defining Document related Variables
        final String documentType = cmbType.getSelectionModel().getSelectedItem();
        final String documentDestination = "/home/jogiwa/Downloads"; // TODO: add location in settings
        int documentAmountInput = 0;
        int documentChaptersInput = 0;
        try {
            documentAmountInput = Integer.parseInt(txtAmount.getText());
            documentChaptersInput = Integer.parseInt(txtChapters.getText());
        } catch (NumberFormatException e) {
            // Display
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            errorAlert.setHeaderText("NumberFormatException");
            errorAlert.setContentText(sw.toString());
            errorAlert.showAndWait();
            return;
        }
        final int documentAmount = documentAmountInput;
        final int documentChapters = documentChaptersInput;
        final boolean shuffle = chkShuffle.isArmed();
        // Check Vars, Abort on invalid inputs
        if (documentType == null) {
            errorAlert.setHeaderText("Invalid Inputs");
            errorAlert.setContentText("Please specify a document type");
            errorAlert.showAndWait();
            return;
        }
        if (documentAmount <= 0 || documentChapters <= 0) {
            errorAlert.setHeaderText("Invalid Inputs");
            errorAlert.setContentText("Please specify a value higher than 0");
            errorAlert.showAndWait();
            return;
        }
        // Generate Latex Documents
        new Thread(() -> {
            Latex latex = new Latex();
            latex.generate(documentType, documentDestination, documentAmount, documentChapters, shuffle);
            Platform.runLater(() -> {
                appendToMultiPurposeTextArea("[ INFO ] Starting new LaTeX instance\n");
                appendToMultiPurposeTextArea("[ INFO ]  Generating " + documentAmount +
                        " Documents with " + documentChapters + " Chapters...\n");
                appendToMultiPurposeTextArea("[ INFO ]  done!\n");
            });
        }).start();
    }

    @FXML
    private void btnChatGptGoClick() {
        // Todo: add logic
    }
}
