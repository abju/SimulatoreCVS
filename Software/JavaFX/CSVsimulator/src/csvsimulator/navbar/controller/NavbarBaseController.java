/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package csvsimulator.navbar.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author lion
 */
public class NavbarBaseController extends HBox implements Initializable {

    public NavbarBaseController(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/csvsimulator/navbar/view/navbarBase.fxml"));
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
        /*ButtonsGroupController btng1 = new ButtonsGroupController();
        this.getChildren().add(btng1);
        btng1 = new ButtonsGroupController();
        this.getChildren().add(btng1);
        btng1 = new ButtonsGroupController();
        this.getChildren().add(btng1);*/
    }    
    
    public void setUpNavSpartito(Object obj){
        ButtonsGroupController btng1 = new ButtonsGroupController("player", obj);
        this.getChildren().add(btng1);
    }
    
}
