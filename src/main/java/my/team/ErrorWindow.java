/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.team;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author jura
 */
public class ErrorWindow {
    
       
    public static void display(String message) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Input Error");
        window.setMinWidth(250);
        Label label = new Label();
        label.setText(message);

        Button yesButton = new Button("Ok");
        yesButton.setOnAction(e -> {
            window.close();
        });
      
        
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, yesButton);
        layout.setAlignment(Pos.CENTER);
        
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
        

    
}