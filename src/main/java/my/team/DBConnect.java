/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.team;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author jura
 */
public class DBConnect {

    private static TextField result = new TextField();
    private static String myLink = "";
    private static String myUser = "";
    private static String myPwd = "";
    private static Properties properties = new Properties(); 
    private  static final ClassLoader CLASS_LOADER = Thread.currentThread().getContextClassLoader();
    
    static{
        readProp();
    }

    public static void display(String message) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("DB connection ");
        window.setMinWidth(250);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        Label linkLabel = new Label("DB link: ");
        GridPane.setConstraints(linkLabel, 0, 0);
        TextField linkField = new TextField();
        linkField.setPromptText(properties.getProperty("DB_URL"));
        GridPane.setConstraints(linkField, 1, 0);
        //Second Label
        Label userLabel = new Label("User name:");
        GridPane.setConstraints(userLabel, 0, 1);
        TextField userInput = new TextField();
        userInput.setPromptText(properties.getProperty("user"));
        GridPane.setConstraints(userInput, 1, 1);
        //Third Label
        Label pwdLabel = new Label("Password:");
        GridPane.setConstraints(pwdLabel, 0, 2);
        PasswordField pwdInput = new PasswordField();
        pwdInput.setPromptText("********");
        GridPane.setConstraints(pwdInput, 1, 2);

        //Four Label
        result.setPromptText("Result");
        GridPane.setConstraints(result, 0, 3);
        result.setMaxWidth(80);
        Button connDB = new Button("Check connection");
        GridPane.setHalignment(connDB, HPos.RIGHT);
        GridPane.setConstraints(connDB, 1, 3);
        connDB.setOnAction(e -> {
            myLink = linkField.getText();
            myUser = userInput.getText();
            myPwd = pwdInput.getText();
            checkDB();
        });

        Button okButton = new Button("Ok");
        GridPane.setConstraints(okButton, 1, 5);
        GridPane.setHalignment(okButton, HPos.CENTER);
        okButton.setOnAction(e -> {
            myLink = linkField.getText();
            myUser = userInput.getText();
            myPwd = pwdInput.getText();
            if (checkDB()) {
                CarsData.firstLanch();
                window.close();
            } else {
                checkDB();
            }
        });
        Button cancelButton = new Button("Cancel");
        GridPane.setHalignment(cancelButton, HPos.RIGHT);
        GridPane.setConstraints(cancelButton, 1, 5);
        cancelButton.setOnAction(e -> window.close());

        grid.getChildren().addAll(linkField, linkLabel, userInput, userLabel,
                pwdLabel, pwdInput, result, connDB, okButton, cancelButton);

        //border.setBottom(grid);
        Scene scene = new Scene(grid, 300, 200);
        window.setScene(scene);
        window.showAndWait();
    }

    public static boolean checkDB() {
        if (readProp()!=null) {
            if (myLink.isEmpty()) 
                myLink = properties.getProperty("DB_URL");
            else
                updatePropertis("DB_URL", myLink);
            if (myUser.isEmpty()) 
                myUser = properties.getProperty("user");
            else
                updatePropertis("user", myUser);
            if (myPwd.isEmpty()) 
                myPwd = properties.getProperty("password");
            else
                updatePropertis("password", myPwd);
        }
        if (CarsData.createConnection(myLink, myUser, myPwd)) {
            result.setText("Succes");
            result.setStyle("-fx-control-inner-background: green;");
            return true;
        } else {
            result.setText("Error");
            result.setStyle("-fx-control-inner-background: red;");
            return false;
        }

    }

    public static Properties readProp() {
        try (InputStream is = new FileInputStream(new File("./programm.properties"))) {
            //System.out.println("CLASS_LOADER = "+CLASS_LOADER.getResource("/"));
            
            properties.load(is);
            return properties;

        } catch (Exception ex) {
            ex.printStackTrace();
            ErrorWindow.display("No \'properties\' file found!\nUse manual input");
            return null;
        }

    }
    
    public static void updatePropertis(String key, String value){
        if(!properties.getProperty(key).equals(value))
            properties.setProperty(key, value);
        
    }

}
