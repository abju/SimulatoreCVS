/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package csvsimulator.navbar.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author lion
 */
public class BottoneGenericoController extends BorderPane implements Initializable {

    @FXML
    private Label button_label;
    
    public BottoneGenericoController(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/csvsimulator/navbar/view/bottoneGenerico.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    /**
     * @return the button_label
     */
    public Label getButton_label() {
        return button_label;
    }

    /**
     * @param button_label the button_label to set
     */
    public void setButton_label(Label button_label) {
        this.button_label = button_label;
    }
    
}
