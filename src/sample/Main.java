/**
 * @author Martynas Valatka PS 1 kursas 4 gr. 2 pogrupis
 * Family tree generator
 */

package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.*;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        File startSetting = new File("StartSettings.txt");
        BufferedReader reader = new BufferedReader(new FileReader(startSetting));
        String filePath = reader.readLine();

        if(filePath != null && new File(filePath).exists())
        {
            TreeSceneController.start(false, new File(filePath));
            Parent root =  FXMLLoader.load(getClass().getResource("TreeScene.fxml"));
            Scene treeScene = new Scene(root);
            primaryStage.setScene(treeScene);
        }
        else
        {
            Writer writer = new FileWriter("StartSettings.txt");
            writer.write("");
            writer.close();

            Parent root = FXMLLoader.load(getClass().getResource("HomeScene.fxml"));
            primaryStage.setScene(new Scene(root, 613, 474));
        }

        primaryStage.setTitle("Family tree generator");
        primaryStage.getIcons().add(new Image("Tree.png"));
        primaryStage.show();
        reader.close();
    }
}
