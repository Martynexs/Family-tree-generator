package sample;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.text.pdf.BaseFont;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;

import com.itextpdf.layout.Document;
import  com.itextpdf.layout.*;

public class TreeSceneController {

    static Hashtable<Integer, TreeElement> People = new Hashtable<>(1);
    static TreeElement rootPerson;

    static boolean pressedNew;
    boolean isSet = false;
    static File loaded;

    int newOptionType;
    int selectedID;

    @FXML MenuBar menuBar;
    @FXML TreeView<String> treeView;
    @FXML ScrollPane treeViewScrollPane;

    @FXML AnchorPane mainPane;
    @FXML AnchorPane filterPane;

    @FXML AnchorPane treePane;
    @FXML AnchorPane editPane;

    @FXML Pane infoPane;
    @FXML TextField textFieldName;
    @FXML TextField textFieldSurname;
    @FXML DatePicker datePickerBirthday;

    @FXML Pane optionPane;
    @FXML RadioButton rbNew;
    @FXML RadioButton rbExisting;
    @FXML MenuButton peopleMenuButton;

    @FXML Button buttonSave;
    @FXML Button buttonCancel;


    @FXML private void close() throws Exception
    {
        showHomeScene((Stage) menuBar.getScene().getWindow());
        People.clear();
        Person.setAmount(0);
        Person.setTotal(0);
        rootPerson = null;
    }

    void showHomeScene(Stage window) throws Exception
    {
        Parent root =  FXMLLoader.load(getClass().getResource("HomeScene.fxml"));
        Scene treeScene = new Scene(root, 613, 474);

        window.setScene(treeScene);
        window.show();
    }


    //Main tree view methods
    @FXML void setTreeView()
    {
        if(!pressedNew && !isSet)
        {
            loadTree(loaded);
            isSet = true;
        }

        reloadTreeElements();
        drawTreeElements();

        TreeItem<String> root, parents, children, grandparents, greatgrandparents, spouse, person, grandchildren, greatgrandchildren;
        root = new TreeItem<>();
        root.setExpanded(true);

        Enumeration<TreeElement> keys = People.elements();

        while(keys.hasMoreElements())
        {
            int key = keys.nextElement().ID;
            person = makeBranch(People.get(key).name + " " + People.get(key).surname + " " + People.get(key).ID, root);


            if(People.get(key).Spouse != 0) {
                spouse = makeBranch("Spouse", person);
                makeBranch(People.get(People.get(key).Spouse).name + " " + People.get(People.get(key).Spouse).surname + " " + People.get(People.get(key).Spouse).ID, spouse);
            }

            if(People.get(key).Parents.size() != 0) {
                parents = makeBranch("Parents", person);
                makeRelationshipBranches(parents, People.get(key).Parents);
            }

            if(People.get(key).Children.size() != 0) {
                children = makeBranch("Children", person);
                makeRelationshipBranches(children, People.get(key).Children);
            }

            if(People.get(key).Grandparents.size() != 0) {
                grandparents = makeBranch("Grandparents", person);
                makeRelationshipBranches(grandparents, People.get(key).Grandparents);
            }

            if(People.get(key).GreatGrandparents.size() != 0) {
                greatgrandparents = makeBranch("Great grandparents", person);
                makeRelationshipBranches(greatgrandparents, People.get(key).GreatGrandparents);
            }

            if(People.get(key).GrandChildren.size() != 0) {
                grandchildren = makeBranch("Grandchildren", person);
                makeRelationshipBranches(grandchildren, People.get(key).GrandChildren);
            }

            if(People.get(key).GreatGrandChildren.size() != 0) {
                greatgrandchildren = makeBranch("Great grandchildren", person);
                makeRelationshipBranches(greatgrandchildren, People.get(key).GreatGrandChildren);
            }
        }

        treeView.setRoot(root);
        treeView.setShowRoot(false);
    }

    void drawTreeElements()
    {
        int startX;
        int startY = 20;
        ArrayList<Integer> toDraw = new ArrayList<>();
        treePane.getChildren().clear();
        treePane.getChildren().add(editPane);

        if (rootPerson != null) {
            toDraw.add(rootPerson.ID);
            int level = 0;
            while (!toDraw.isEmpty()) {
                level++;
                startX = 1000;
                int inArray = toDraw.size(); //How many people to draw are in array
                startX = startX - (inArray * 420 / 2) + 420;

                for (int i = 0; i < inArray; i++) {
                    Line spouseLine = new Line();
                    spouseLine.strokeProperty().setValue(Paint.valueOf("RED"));
                    People.get(toDraw.get(0)).level = level;
                    toDraw.addAll(People.get(toDraw.get(0)).Children);
                    toDraw = removeDuplicates(toDraw);
                    People.get(toDraw.get(0)).drawTreeNode(treePane, startX, startY);

                    if (People.get(toDraw.get(0)).Spouse != 0) {
                        People.get(People.get(toDraw.get(0)).Spouse).isSpouse = true;
                        People.get(People.get(toDraw.get(0)).Spouse).level = level;
                        People.get(People.get(toDraw.get(0)).Spouse).drawTreeNode(treePane, startX + 210, startY);
                        spouseLine.setStartX(startX + 140);
                        spouseLine.setStartY(startY + 70);
                        spouseLine.setEndX(startX + 210);
                        spouseLine.setEndY(startY + 70);
                        treePane.getChildren().add(spouseLine);
                    }

                    if (People.get(toDraw.get(0)).Parents.size() != 0) {
                        for (int j = 0; j < People.get(toDraw.get(0)).Parents.size(); j++) {
                            Line parentLine = new Line();
                            parentLine.strokeProperty().setValue(Paint.valueOf("GREEN"));
                            if (People.get(People.get(toDraw.get(0)).Parents.get(j)).x != 0) {
                                parentLine.setStartX(startX + 70);
                                parentLine.setStartY(startY);
                                parentLine.setEndX(People.get(People.get(toDraw.get(0)).Parents.get(j)).x + 70);
                                parentLine.setEndY(People.get(People.get(toDraw.get(0)).Parents.get(j)).y + 140);
                                treePane.getChildren().add(parentLine);
                            }
                        }
                    }
                    startX += 420;
                    toDraw.remove(0);
                }
                startY += 140 + 70;
            }
        }
    }

    @FXML void makeRoot()
    {
        MultipleSelectionModel<TreeItem<String>> msm = treeView.getSelectionModel();
        if(msm.getSelectedItem() != null) {
            String text = msm.getSelectedItem().getValue();
            int ID = stringtoID(text);
            if (ID != 0) {
                rootPerson = People.get(ID);
            }
            setTreeView();
        }
        treeViewScrollPane.setHvalue(0.4);
        treeViewScrollPane.setVvalue(0);
    }

    void reloadTreeElements()
    {
        Enumeration<TreeElement> treeElementEnumeration = People.elements();

        while(treeElementEnumeration.hasMoreElements())
        {
            int ID = treeElementEnumeration.nextElement().ID;
            People.get(ID).update();
        }

    }


    //Edit options methods
    @FXML void optionChanged()
    {
        if(rbNew.isSelected())
        {
            optionNew();
        }
        else optionExisting();
    }

    void optionNew()
    {
        infoPane.setDisable(false);
        peopleMenuButton.setDisable(true);

        if(newOptionType == 2) buttonSave.setOnAction(event -> addTreeElement(textFieldName.getText(), textFieldSurname.getText(), datePickerBirthday.getValue(), People.get(selectedID), -1));
        else if(newOptionType == 3) buttonSave.setOnAction(event -> addTreeElement(textFieldName.getText(), textFieldSurname.getText(), datePickerBirthday.getValue(), People.get(selectedID), 1));
    }

    void optionExisting()
    {
        infoPane.setDisable(true);
        peopleMenuButton.setDisable(false);
        peopleMenuButton.getItems().clear();

        if(newOptionType == 2) //Parent
        {
            People.forEach((k, v) -> {
                MenuItem item;
                if(People.get(k).level == People.get(selectedID).level-1 /*&& !People.get(k).isSpouse*/)
                {
                    item = new MenuItem(People.get(k).name + " " + People.get(k).surname+ " " + People.get(k).ID);
                    item.setOnAction(event -> peopleMenuButton.setText(item.getText()));
                    peopleMenuButton.getItems().add(item);
                }
            });
        }
        else if(newOptionType == 3) //Child
        {
            People.forEach((k, v) -> {
                MenuItem item;
                if(People.get(k).level == People.get(selectedID).level+1 && !People.get(k).isSpouse)
                {
                    item = new MenuItem(People.get(k).name + " " + People.get(k).surname+ " " + People.get(k).ID);
                    item.setOnAction(event -> peopleMenuButton.setText(item.getText()));
                    peopleMenuButton.getItems().add(item);
                }
            });
        }
        buttonSave.setOnAction(event -> existingRelation(newOptionType));
    }


    //Person editor methods
    void openPersonEditor(int type, int ID)
    {
        editPane.setLayoutX(People.get(ID).x);
        editPane.setLayoutY(People.get(ID).y);
        editPane.toFront();
        peopleMenuButton.getItems().clear();
        rbNew.selectedProperty().set(true);
        optionNew();
        peopleMenuButton.setText("Choose");
        datePickerBirthday.setValue(LocalDate.now());


        if(type == 1) //Edit
        {
            textFieldName.setText(People.get(ID).name);
            textFieldSurname.setText(People.get(ID).surname);
            datePickerBirthday.setValue(People.get(ID).birthday);

            buttonSave.setOnAction(event -> updateTreeElement(textFieldName.getText(), textFieldSurname.getText(), datePickerBirthday.getValue(), ID));
            optionPane.setDisable(true);
        }
        else if(type == 2) //Parent
        {
            buttonSave.setOnAction(event -> addTreeElement(textFieldName.getText(), textFieldSurname.getText(), datePickerBirthday.getValue(), People.get(ID), -1));
            newOptionType = type;
            selectedID = ID;
            optionPane.setDisable(false);
            if(People.get(selectedID).isSpouse) optionPane.setDisable(true);
        }
        else if (type == 3) //Child
        {
            buttonSave.setOnAction(event -> addTreeElement(textFieldName.getText(), textFieldSurname.getText(), datePickerBirthday.getValue(), People.get(ID), 1));
            newOptionType = type;
            selectedID = ID;
            optionPane.setDisable(false);
        }
        else  if(type == 4) //Spouse
        {
            buttonSave.setOnAction(event -> addTreeElement(textFieldName.getText(), textFieldSurname.getText(), datePickerBirthday.getValue(), People.get(ID), 0));
            optionPane.setDisable(true);
        }

        editPane.setVisible(true);
    }

    @FXML void closePersonEditor()
    {
        textFieldName.setText("");
        textFieldSurname.setText("");
        datePickerBirthday.setValue(LocalDate.now());
        editPane.setVisible(false);
    }


    //Tree element methods
    void updateTreeElement(String name, String surname, LocalDate birthday, int ID)
    {
        People.get(ID).name = name;
        People.get(ID).surname = surname;
        People.get(ID).birthday = birthday;
        People.get(ID).update();
        setTreeView();
        closePersonEditor();
    }

    void deleteTreeElement(int ID)
    {
        People.forEach((k, v) -> {
            if(!v.Parents.isEmpty() && v.Parents.contains(ID)) v.Parents.remove((Object)ID);
            if(!v.Children.isEmpty() && v.Children.contains(ID)) v.Children.remove((Object)ID);
            if(v.Spouse == ID) v.Spouse = 0;
            if(!v.Grandparents.isEmpty() && v.Grandparents.contains(ID)) v.Grandparents.remove((Object)ID);
            if(!v.GreatGrandparents.isEmpty() && v.GreatGrandparents.contains(ID)) v.GreatGrandparents.remove((Object)ID);
            if(!v.GrandChildren.isEmpty() && v.GrandChildren.contains(ID)) v.GrandChildren.remove((Object)ID);
            if(!v.GreatGrandChildren.isEmpty() && v.GreatGrandChildren.contains(ID)) v.GreatGrandChildren.remove((Object)ID);
        });
        if(rootPerson == People.get(ID))
        {
            if(!rootPerson.Children.isEmpty())
            {
                rootPerson = People.get(rootPerson.Children.get(0));
            }
            else if(rootPerson.Spouse != 0) rootPerson = People.get(rootPerson.Spouse);
            else rootPerson = null;
        }
        People.remove(ID);
        setTreeView();
    }

    void addTreeElement(String name, String surname, LocalDate birthday, TreeElement selected, int relation)
    {
        TreeElement element = new TreeElement(name, surname, birthday);
        setElementButtons(element);

        People.put(element.ID, element);

        //Add parent
        if(relation == -1)
        {
            selected.addRelation(selected.Parents, element.ID);
            element.addRelation(element.Children, selected.ID);
            for(int i=0; i<selected.Children.size(); i++)
            {
                People.get(selected.Children.get(i)).Grandparents.add(element.ID);
                element.addRelation(element.GrandChildren, selected.Children.get(i));
                for(int j=0; j<People.get(selected.Children.get(i)).Children.size(); j++)
                {
                    People.get(People.get(selected.Children.get(i)).Children.get(j)).GreatGrandparents.add(element.ID);
                    element.addRelation(element.GreatGrandChildren, People.get(selected.Children.get(i)).Children.get(j));
                }
            }
            rootPerson = element;
        }
        //Add spouse
        else if(relation == 0)
        {
            if(selected.Spouse != 0) {
                People.get(selected.Spouse).Spouse=0;
            }
            selected.Spouse = element.ID;
            element.Spouse = selected.ID;
        }
        //Add child
        else
        {
            selected.addRelation(selected.Children, element.ID);
            element.addRelation(element.Parents, selected.ID);
            for(int i=0; i<selected.Parents.size(); i++)
            {
                People.get(selected.Parents.get(i)).GrandChildren.add(element.ID);
                element.addRelation(element.Grandparents, selected.Parents.get(i));
            }
            for(int i=0; i<selected.Grandparents.size(); i++)
            {
                People.get(selected.Grandparents.get(i)).GreatGrandChildren.add(element.ID);
                element.addRelation(element.GreatGrandparents, selected.Grandparents.get(i));
            }
        }

        setTreeView();
        closePersonEditor();
    }


    //Starting methods
    static void start(boolean isPressedNew, File file)
    {
        pressedNew = isPressedNew;
        loaded = file;
    }

    @FXML void newRoot()
    {
        TreeElement start = new TreeElement("Name", "Surname", LocalDate.now());
        People.put(start.ID, start);
        setElementButtons(start);
        rootPerson = start;
        setTreeView();
    }


    //File methods
    void loadTree(File file)
    {
        try
        {
            int lastID=0;
            BufferedReader br = new BufferedReader(new FileReader(file));
            String row;
            row = br.readLine();

            if(row == null || !row.equals("ID;Name;Surname;Birthday;Parents;Children;Grandparents;Great grandparents;Grandchildren;Great grandchildren;Spouse"))
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Tree not found");
                alert.setHeaderText(null);
                alert.setContentText("No tree in file " + file.getPath() + " could be found.");
                alert.showAndWait();
            }
            else {
                while ((row = br.readLine()) != null) {
                    String[] data = row.split(";");
                    TreeElement element = new TreeElement(data[1], data[2], LocalDate.parse(data[3]));
                    element.ID = Integer.parseInt(data[0]);
                    if (element.ID > lastID) lastID = element.ID;

                    String[] ids;
                    //Parents
                    if (!data[4].equals("")) {
                        ids = data[4].split(" ");
                        for (String id : ids) {
                            element.Parents.add(Integer.parseInt(id));
                        }
                    }

                    //Children
                    if (!data[5].equals("")) {
                        ids = data[5].split(" ");
                        for (String id : ids) element.Children.add(Integer.parseInt(id));
                    }

                    //Grandparents
                    if (!data[6].equals("")) {
                        ids = data[6].split(" ");
                        for (String id : ids) element.Grandparents.add(Integer.parseInt(id));
                    }

                    //Great grandparents
                    if (!data[7].equals("")) {
                        ids = data[7].split(" ");
                        for (String id : ids) element.GreatGrandparents.add(Integer.parseInt(id));
                    }

                    //Grandchildren
                    if (!data[8].equals("")) {
                        ids = data[8].split(" ");
                        for (String id : ids) element.GrandChildren.add(Integer.parseInt(id));
                    }

                    //Great grandchildren
                    if (!data[9].equals("")) {
                        ids = data[9].split(" ");
                        for (String id : ids) element.GreatGrandChildren.add(Integer.parseInt(id));
                    }

                    element.Spouse = Integer.parseInt(data[10]);

                    setElementButtons(element);
                    People.put(element.ID, element);
                }
            }
            Person.setTotal(lastID);
        }
        catch (Exception e)
        {
           // e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Could not open file");
            alert.setHeaderText(null);
            alert.setContentText("File " + file.getPath() + " could not be opened.");
            alert.showAndWait();
        }

    }

    @FXML void saveTree()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        File file = fileChooser.showSaveDialog(menuBar.getScene().getWindow());
        if(file != null)
        {
            try {
                PrintWriter pw = new PrintWriter(file);
                StringBuilder sb = new StringBuilder();

                sb.append("ID;Name;Surname;Birthday;Parents;Children;Grandparents;Great grandparents;Grandchildren;Great grandchildren;Spouse\n");

                People.forEach((k, v)->{
                    sb.append(v.ID).append(";").append(v.name).append(";").append(v.surname).append(";");

                    if(v.birthday!=null) sb.append(v.birthday.toString()).append(";");
                    else sb.append(";;");

                    v.Parents.forEach((i) -> sb.append(i).append(" "));
                    sb.append(";");

                    v.Children.forEach((i) -> sb.append(i).append(" "));
                    sb.append(";");

                    v.Grandparents.forEach((i) -> sb.append(i).append(" "));
                    sb.append(";");

                    v.GreatGrandparents.forEach((i) -> sb.append(i).append(" "));
                    sb.append(";");

                    v.GrandChildren.forEach((i) -> sb.append(i).append(" "));
                    sb.append(";");

                    v.GreatGrandChildren.forEach((i) -> sb.append(i).append(" "));
                    sb.append(";");

                    sb.append(v.Spouse);

                    sb.append("\n");
                });
                pw.write(sb.toString());
                pw.close();

                pw = new PrintWriter("StartSettings.txt");
                pw.write(file.getAbsolutePath());
                pw.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @FXML void exportToPdf() throws IOException {
        //Select file
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add( new FileChooser.ExtensionFilter("PDF", "*.pdf"));
        File file = fileChooser.showSaveDialog(menuBar.getScene().getWindow());

        //Create tree image
        WritableImage image =  treePane.snapshot( new SnapshotParameters(), null);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", byteArrayOutputStream);

        Image treeShot = new Image(ImageDataFactory.create(byteArrayOutputStream.toByteArray()));

        //Create pdf document
        PdfWriter pdfWriter = new PdfWriter(file);
        PdfDocument pdfDoc = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDoc, PageSize.A1);

        //Add image to pdf
        document.add(treeShot);


        float[] pointColumnWidths = {150F, 150F, 150F, 150F, 150F, 150F, 150F, 150F, 150F, 150F, 150};
        Table table = new Table(pointColumnWidths);

        //Column names
        table.addCell(new Cell().add(new Paragraph("ID")));
        table.addCell(new Cell().add(new Paragraph("Name")));
        table.addCell(new Cell().add(new Paragraph("Surname")));
        table.addCell(new Cell().add(new Paragraph("Birthday")));
        table.addCell(new Cell().add(new Paragraph("Spouse")));
        table.addCell(new Cell().add(new Paragraph("Children")));
        table.addCell(new Cell().add(new Paragraph("Parents")));
        table.addCell(new Cell().add(new Paragraph("Grandparents")));
        table.addCell(new Cell().add(new Paragraph("Grandchildren")));
        table.addCell(new Cell().add(new Paragraph("Great grandparents")));
        table.addCell(new Cell().add(new Paragraph("Great grandchildren")));

        //Rows
        ArrayList<Integer> toAdd = new ArrayList<>();
        toAdd.add(rootPerson.ID);
        while (!toAdd.isEmpty())
        {
            TreeElement person = People.get(toAdd.get(0));
            toAdd.addAll(person.Children);
            toAdd.remove(0);

            addPersonInfoCells(table, person);
            if(person.Spouse != 0) addPersonInfoCells(table, People.get(person.Spouse));
        }

        document.add(table);
        document.close();
    }

    void addPersonInfoCells(Table table,TreeElement person) throws IOException {
        PdfFont font = PdfFontFactory.createFont("arialuni.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

        table.addCell(new Cell().add(new Paragraph(String.valueOf(person.ID))));
        table.addCell(new Cell().add(new Paragraph(person.name).setFont(font)));

        table.addCell(new Cell().add(new Paragraph(person.surname).setFont(font)));
        table.addCell(new Cell().add(new Paragraph(person.birthday.toString())));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(person.Spouse))));

        String ids =  Arrays.toString(person.Children.toArray());
        table.addCell(new Cell().add(new Paragraph(ids)));

        ids =  Arrays.toString(person.Parents.toArray());
        table.addCell(new Cell().add(new Paragraph(ids)));

        ids =  Arrays.toString(person.Grandparents.toArray());
        table.addCell(new Cell().add(new Paragraph(ids)));

        ids =  Arrays.toString(person.GrandChildren.toArray());
        table.addCell(new Cell().add(new Paragraph(ids)));

        ids =  Arrays.toString(person.GreatGrandparents.toArray());
        table.addCell(new Cell().add(new Paragraph(ids)));

        ids =  Arrays.toString(person.GreatGrandChildren.toArray());
        table.addCell(new Cell().add(new Paragraph(ids)));
    }


    //Extra methods
    private TreeItem<String> makeBranch(String title, TreeItem<String> parent)
    {
        TreeItem<String> item = new TreeItem<>(title);
        parent.getChildren().add(item);
        return item;
    }

    private void makeRelationshipBranches(TreeItem<String> root, ArrayList<Integer> arrayList)
    {
        for (Integer integer : arrayList) {
            makeBranch(People.get(integer).name + " " + People.get(integer).surname + " " + People.get(integer).ID, root);
        }
    }

    void setElementButtons(TreeElement element)
    {
        element.edit.setOnAction(event -> openPersonEditor(1, element.ID));
        element.addParent.setOnAction(event -> openPersonEditor(2, element.ID));
        element.addChild.setOnAction(event -> openPersonEditor(3, element.ID));
        element.addSpouse.setOnAction(event -> openPersonEditor(4, element.ID));
        element.delete.setOnAction(event -> deleteTreeElement(element.ID));
    }

    void existingRelation(int type)
    {
        TreeElement selected = People.get(selectedID);
        int ID = stringtoID(peopleMenuButton.getText());
        if(ID != 0)
        {
            if(type == 2 && !People.get(selectedID).Parents.contains(ID)){ //Parent
                People.get(selectedID).addRelation(People.get(selectedID).Parents, ID);
                People.get(ID).Children.add(selectedID);

                for(int i=0; i<selected.Children.size(); i++)
                {
                    People.get(selected.Children.get(i)).Grandparents.add(ID);
                    People.get(ID).addRelation(People.get(ID).GrandChildren, selected.Children.get(i));
                    for(int j=0; j<People.get(selected.Children.get(i)).Children.size(); j++)
                    {
                        People.get(People.get(selected.Children.get(i)).Children.get(j)).GreatGrandparents.add(ID);
                        People.get(ID).addRelation(People.get(ID).GreatGrandChildren, People.get(selected.Children.get(i)).Children.get(j));
                    }
                }
            }
            else if(type == 3 && !People.get(selectedID).Children.contains(ID)) { //Child
                People.get(selectedID).addRelation(People.get(selectedID).Children, ID);
                People.get(ID).Parents.add(selectedID);
                for(int i=0; i<selected.Parents.size(); i++)
                {
                    People.get(selected.Parents.get(i)).GrandChildren.add(ID);
                    People.get(ID).addRelation(People.get(ID).Grandparents, selected.Parents.get(i));
                }
                for(int i=0; i<selected.Grandparents.size(); i++)
                {
                    People.get(selected.Grandparents.get(i)).GreatGrandChildren.add(ID);
                    People.get(ID).addRelation(People.get(ID).GreatGrandparents, selected.Grandparents.get(i));
                }
            }
            setTreeView();
        }
        closePersonEditor();
    }

    int stringtoID(String text)
    {
        Enumeration<TreeElement> keys = People.elements();

        while(keys.hasMoreElements())
        {
            int ID = keys.nextElement().ID;
            if(text.equals(People.get(ID).name + " " + People.get(ID).surname + " " + People.get(ID).ID)) return ID;
        }
        return 0;
    }

    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list)
    {
        ArrayList<T> newList = new ArrayList<T>();
        for (T element : list) {
            if (!newList.contains(element)) {

                newList.add(element);
            }
        }
        return newList;
    }
}