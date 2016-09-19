/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.team;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 *
 * @author jura
 */
public class Main extends Application {

    Stage window;
    private static ScheduledExecutorService service;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Ria scanner");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);
        //First labell with site link input
        Label nameLabel = new Label("Link: ");
        GridPane.setConstraints(nameLabel, 0, 0);
        TextField linkField = new TextField();
        linkField.setPromptText("https://auto.ria.ua/...");
        GridPane.setConstraints(linkField, 1, 0);
        //Second Label with email input
        Label passLabel = new Label("e-mail: ");
        GridPane.setConstraints(passLabel, 0, 1);
        TextField mailInput = new TextField();
        mailInput.setPromptText("my@mail.com");
        GridPane.setConstraints(mailInput, 1, 1);
        //Third label with DB connection
        Button connDB = new Button("Database connection");
        GridPane.setConstraints(connDB, 1, 2);
        connDB.setOnAction(e -> DBConnect.display("name"));
        //Four label with start and cancel button
        Button startButton = new Button("Scanning");
        GridPane.setConstraints(startButton, 1, 5);
        startButton.setOnAction(e -> {
            if(service!=null)
                service.shutdownNow();
            runScanner(linkField.getText(), mailInput.getText());});
        Button cancelButton = new Button("Exit");
        GridPane.setHalignment(cancelButton, HPos.RIGHT);
        GridPane.setConstraints(cancelButton, 1, 5);
        cancelButton.setOnAction(e -> {
            window.close();
            if(service!=null)
                service.shutdownNow();
                });
        
        grid.getChildren().addAll(linkField, nameLabel, mailInput, passLabel, startButton, 
                cancelButton,connDB);

        Scene scene = new Scene(grid, 275, 160);
        window.setScene(scene);
        window.show();
    }

    public static void runScanner(String myLink, String myMail) {

        if (!CheckInput.validateLink(myLink)) {
            ErrorWindow.display("Please cheack link input");

        } else if (!CheckInput.validateMail(myMail)) {
            ErrorWindow.display("Please cheack mail input");
            //System.out.println("loks like mail");
        } else if(!DBConnect.checkDB())
            ErrorWindow.display("Please cheack Database connection");
        else {
            RunCarScanner rcs = new RunCarScanner(myLink, myMail);
            service = Executors.newSingleThreadScheduledExecutor();
            service.scheduleWithFixedDelay(rcs, 0, 20, TimeUnit.MINUTES);
            
        }

    }
}
