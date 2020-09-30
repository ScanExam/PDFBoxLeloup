package org.openjfx;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.filesreaders.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

import javafx.stage.FileChooser;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;


    //Objets graphiques :

    private ComboBox chooseSourceFile;
    private ComboBox choosePage;
    private MenuItem addFile;
    private ImageView imagePDF;
    private Pane paneimg;
    private PDFfile pdfinterface;
    private PDFFactory pdfFactory;


    private FileChooser fileChooser = new FileChooser();


    @Override
    public void start(Stage stage) throws IOException {

        pdfFactory = new PDFFactory();

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("primary.fxml"));
        Parent fxmlparent = fxmlLoader.load();
        scene = new Scene(fxmlparent);

        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMaximized(true);
        stage.getIcons().add(new Image("Appicon.png"));
        stage.setTitle("Lexique");




        //initialisation des objets graphiques :
        addFile = (MenuItem)fxmlLoader.getNamespace().get("addFile");
        imagePDF = (ImageView)fxmlLoader.getNamespace().get("img");
        paneimg = (Pane)fxmlLoader.getNamespace().get("paneimg");
        chooseSourceFile = (ComboBox) fxmlLoader.getNamespace().get("chooseSourceFile");
        choosePage = (ComboBox) fxmlLoader.getNamespace().get("choosePage");






        //pdfinterface.documentClose();

        //Event ajouter source
        EventHandler<ActionEvent> addFileEvent =
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e) {

                        System.out.println("Open file");
                        File file = fileChooser.showOpenDialog(stage);

                        if ((file != null) && !pdfFactory.getPdfFileMap().containsKey(file.getName())) {
                            pdfFactory.setNewPdfFile(file);
                            System.out.println("Fichier chargé");
                            clearWindow();
                            chooseSourceFile.setItems(FXCollections.observableArrayList(pdfFactory.getPdfFileList()));
                        }
                        else{
                            System.out.println("Aucun fichier selectionné ou déjà chargé");
                        }
                    }
                };

        //Event choisir fichier source
        EventHandler<ActionEvent> chooseSrcEvent =
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e) {
                        System.out.println("Action sur menu Source");
                        if(chooseSourceFile.getValue()==null){
                            clearWindow();
                            choosePage.setValue(null);
                        }else{
                            try {
                                choosePage.setItems(FXCollections.observableArrayList(pdfFactory.getPagesOfDocument(chooseSourceFile.getValue().toString())));
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                            imagePDF.setImage(null);
                        }
                    }
                };

        //Event choisir fichier source
        EventHandler<ActionEvent> showPageEvent =
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e) {
                        System.out.println("Action sur menu Page");
                        if(choosePage.getValue()==null){
                            imagePDF.setImage(null);
                        }else{
                            imagePDF.setImage(null);
                            try {
                                System.out.println("Actualisation affichage page "+choosePage.getValue());

                                imagePDF.setImage(pdfFactory.getPdfFileFromKey(chooseSourceFile.getValue().toString()).getImageFromPDF((int) choosePage.getValue()));
                                imagePDF.setFitHeight(1000);
                                imagePDF.setFitWidth(1000);
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }

                        }
                    }
                };






        //Lien entre controles et actions
        addFile.setOnAction(addFileEvent);
        chooseSourceFile.setOnAction(chooseSrcEvent);
        choosePage.setOnAction(showPageEvent);


        stage.show();


    }

    //Reinitialise tout les champs graphiques
    private void clearWindow(){
        System.out.println("clear interface");
        chooseSourceFile.setValue(null);
        choosePage.setValue(null);
        imagePDF.setImage(null);


    }


    public static void main(String[] args) {
        launch();
    }

}