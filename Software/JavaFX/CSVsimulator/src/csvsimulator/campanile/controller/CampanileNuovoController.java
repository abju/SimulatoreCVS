
package csvsimulator.campanile.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author Marco Dalla Riva <marco.dallariva@outlook.com>
 */
public class CampanileNuovoController extends BorderPane implements Initializable {
    @FXML
    private HBox fieldsetNuovaCampana;
    @FXML
    private TextField tfNomeCampana;
    @FXML
    private Button btnBrowse;
    @FXML
    private Button btnPreview;
    @FXML
    private Button btnElimina;

    public CampanileNuovoController() {
        init();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    private void init(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/csvsimulator/campanile/view/campanileNuovo.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
