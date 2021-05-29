package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.File;



public class HomeSceneController {
    //Buttons
    @FXML private Button btnNew;
    @FXML private Button btnLoad;

    @FXML
    private void NewTree(ActionEvent event) throws Exception
    {
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        showTreeScene(window);
        TreeSceneController.start(true, null);
    }

    @FXML
    private void LoadTree(ActionEvent event) throws Exception
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add( new FileChooser.ExtensionFilter("CSV", "*.csv"));
        File file = fileChooser.showOpenDialog(btnNew.getScene().getWindow());
        if(file != null) {
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            showTreeScene(window);
            TreeSceneController.start(false, file);
        }
    }


    private void showTreeScene(Stage window) throws Exception
    {
        Parent root =  FXMLLoader.load(getClass().getResource("TreeScene.fxml"));
        Scene treeScene = new Scene(root);

        window.setScene(treeScene);
        window.show();
    }
}
