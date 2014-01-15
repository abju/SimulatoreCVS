package csvsimulator.campanile.controller;

import csvsimulator.model.ModelCampana;
import csvsimulator.model.ModelConcerto;
import global.AlertDialog;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Marco Dalla Riva <marco.dallariva@outlook.com>
 */
public class CampanileNuovoController extends BorderPane implements Initializable {

    @FXML
    private Button btnAggiungiCampana;
    @FXML
    private Button btnSalvaConcerto;
    @FXML
    private VBox listCampane;
    @FXML
    private TextField txtNomeConcerto;

    private List<ModelCampana> listModelCampana;

    public CampanileNuovoController() {
        init();
        listCampane.getChildren().add(new CampanileNuovoFieldsetController(this, 1));
        //listCampane.getChildren().add(fieldsetNuovaCampana);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    private void init() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/csvsimulator/campanile/view/campanileNuovo.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        listModelCampana = new ArrayList<>();
    }

    @FXML
    private void aggiungiCampanaAction(ActionEvent event) {
        listCampane.getChildren().add(new CampanileNuovoFieldsetController(this, listCampane.getChildren().size() + 1));
    }

    @FXML
    private void salvaConcertoAction(ActionEvent event) {
        boolean valid = true;
        ModelConcerto mc = new ModelConcerto();
        
        if (!txtNomeConcerto.getText().trim().equals("")) {
            mc.setNomeConcerto(txtNomeConcerto.getText().trim());
            for (ListIterator<Node> it = listCampane.getChildren().listIterator(); it.hasNext();) {
                CampanileNuovoFieldsetController cnf = (CampanileNuovoFieldsetController) it.next();
                ModelCampana campana = cnf.getMb();
                if (campana != null) {
                    mc.pushCampana(cnf.getMb());
                } else {
                    valid = false;
                    break;
                }
            }
        } else {
            new AlertDialog((Stage) this.getScene().getWindow(), "Il nome del concerto non può essere vuoto", AlertDialog.ICON_ERROR).showAndWait();
            valid = false;
        }
        
        if (valid) {
            mc.saveFileConcerto(((Node) this).getScene().getWindow());
        }
    }

    public void rimuoviCampanaByObject(Object node) {
        listCampane.getChildren().remove(node);

        //Risistemo i numeri 
        for (ListIterator<Node> it = listCampane.getChildren().listIterator(); it.hasNext();) {
            CampanileNuovoFieldsetController cnf = (CampanileNuovoFieldsetController) it.next();
            cnf.setNumero(it.nextIndex() - 1);
        }
    }

}
