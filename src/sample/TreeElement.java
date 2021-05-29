package sample;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;

import java.time.LocalDate;


interface TreeNode{
   void drawTreeNode(Pane pane, double x, double y);
   void deleteTreeNode(Pane pane);
}


public class TreeElement extends Person implements TreeNode{

   Pane body;
   Label nameLb;
   Label surnameLb;
   Label birthdayLb;
   Ellipse ellipse;
   MenuButton treeElementOptions;

   MenuItem edit;
   MenuItem addParent;
   MenuItem addChild;
   MenuItem addSpouse;
   MenuItem delete;

   double x;
   double y;
   int level;
   boolean isSpouse = false;

   TreeElement(String name, String surname, LocalDate birthday)
   {
      super(name, surname, birthday);

      body = new Pane();
      body.setPrefSize(140, 140);

      ellipse = new Ellipse(74, 74);
      ellipse.setFill(Paint.valueOf("White"));
      ellipse.setLayoutX(70);
      ellipse.setCenterY(70);

      nameLb = new Label(name);
      nameLb.setLayoutX(0.0);
      nameLb.setLayoutY(30.0);
      nameLb.setAlignment(Pos.CENTER);
      nameLb.setPrefWidth(140.0);

      surnameLb = new Label(surname);
      surnameLb.setLayoutX(0.0);
      surnameLb.setLayoutY(60.0);
      surnameLb.setAlignment(Pos.CENTER);
      surnameLb.setPrefWidth(140.0);

      birthdayLb = new Label(birthday.toString());
      birthdayLb.setLayoutX(0.0);
      birthdayLb.setLayoutY(100.0);
      birthdayLb.setAlignment(Pos.CENTER);
      birthdayLb.setPrefWidth(140.0);

      treeElementOptions = new MenuButton("");
      treeElementOptions.setLayoutX(50);
      treeElementOptions.setLayoutY(114);
      edit = new MenuItem("Edit");
      addParent = new MenuItem("Add parent");
      addChild = new MenuItem("Add child");
      addSpouse = new MenuItem("Add spouse");
      delete = new MenuItem("Delete");
      treeElementOptions.getItems().addAll(edit, addParent, addChild, addSpouse, delete);


      treeElementOptions.setStyle("-fx-background-color: TRANSPARENT;");

      body.getChildren().addAll(ellipse, nameLb, surnameLb, birthdayLb, treeElementOptions);
   }

   @Override
   public void drawTreeNode(Pane pane, double x, double y) {
      this.body.setLayoutX(x);
      this.x = x;
      this.body.setLayoutY(y);
      this.y =y;
      if(!pane.getChildren().contains(this.body)) {
         pane.getChildren().add(this.body);
      }
   }

   @Override
   public void deleteTreeNode(Pane pane) {
      boolean remove = pane.getChildren().remove(this);
   }

   @Override
   public void  update()
   {
      nameLb.setText(this.name);
      surnameLb.setText(this.surname);
      birthdayLb.setText(this.birthday.toString());
      x = 0;
      y = 0;
      level = 0;
      isSpouse = false;
   }

}
