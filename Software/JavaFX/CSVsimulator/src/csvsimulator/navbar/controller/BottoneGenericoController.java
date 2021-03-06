/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package csvsimulator.navbar.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author lion
 */
public class BottoneGenericoController extends BorderPane implements Initializable {
    
    @FXML
    private ImageView button_icon;
    
    public BottoneGenericoController(String label, InputStream path_icon){
        init();
        button_icon.setImage(new Image(path_icon, button_icon.getFitWidth(), button_icon.getFitHeight(), true, true));        
    }
    
    public void init(){
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
    
}
