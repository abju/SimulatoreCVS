/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package csvsimulator;

import csvsimulator.navbar.controller.NavbarBaseController;
import csvsimulator.spartito.controller.SpartitoBaseController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author lion
 */
public class SampleController extends AnchorPane implements Initializable {

    @FXML 
    private NavbarBaseController navbar;
    
    @FXML
    private SpartitoBaseController spartito;
    
    @FXML
    private BorderPane barraSinistra;
    

    public SampleController(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/csvsimulator/Sample.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    } 
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*
        AnchorPane.setTopAnchor(baseNavBar, Double.valueOf(0));
        AnchorPane.setLeftAnchor(baseNavBar, Double.valueOf(0));
                */
    }

    /**
     * @return the spartito
     */
    public SpartitoBaseController getSpartito() {
        return spartito;
    }

    /**
     * @param spartito the spartito to set
     */
    public void setSpartito(SpartitoBaseController spartito) {
        this.spartito = spartito;
    }

    /**
     * @return the navbar
     */
    public NavbarBaseController getNavbar() {
        return navbar;
    }

    /**
     * @return the spartitoBarraSinistra
     */
    public BorderPane getBarraSinistra() {
        return barraSinistra;
    }

    /**
     * @param spartitoBarraSinistra the spartitoBarraSinistra to set
     */
    public void setBarraSinistra(BorderPane barraSinistra) {
        this.barraSinistra = barraSinistra;
    }

}
