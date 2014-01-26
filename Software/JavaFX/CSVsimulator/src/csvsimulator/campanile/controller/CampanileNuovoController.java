package csvsimulator.campanile.controller;

import csvsimulator.model.ModelCampana;
import csvsimulator.model.ModelConcerto;
import csvsimulator.spartito.controller.SpartitoBaseController;
import global.AlertDialog;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
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
import javafx.stage.FileChooser;
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
    private Button btnProvaSenzaSalvare;
    @FXML
    private Button btnProvaSalva;
    @FXML
    private VBox listCampane;
    @FXML
    private TextField txtNomeConcerto;
    
    private File lastChooserFolder; 

    public CampanileNuovoController() {
        init();
        //listCampane.getChildren().add(new CampanileNuovoFieldsetController(this, 1));
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

        lastChooserFolder = null;
    }

    @FXML
    private void aggiungiCampanaAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Seleziona il file audio della campana");

        if (lastChooserFolder != null) {
            File existDirectory = lastChooserFolder.getParentFile();
            fileChooser.setInitialDirectory(existDirectory);
        } else {
            fileChooser.setInitialDirectory(
                    new File(System.getProperty("user.home"))
            );
        }
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("File Audio", "*.wav", "*.mp3"),
                new FileChooser.ExtensionFilter("WAV", "*.wav"),
                new FileChooser.ExtensionFilter("MP3", "*.mp3")
        );

        //Show save file dialog
        List<File> files = fileChooser.showOpenMultipleDialog(((Node) this).getScene().getWindow());
        if (files != null) {
            List<File> clone_files = new ArrayList<>(files);
            Collections.sort(clone_files, Collections.reverseOrder());
            for (Iterator<File> it = clone_files.iterator(); it.hasNext();) {
                File file1 = it.next();
                listCampane.getChildren().add(new CampanileNuovoFieldsetController(this, file1, listCampane.getChildren().size()));
            }
        } 
        //listCampane.getChildren().add(new CampanileNuovoFieldsetController(this, listCampane.getChildren().size() + 1));
    }

    @FXML
    private void salvaConcertoAction(ActionEvent event) {
        ModelConcerto mc = getConcertoCreato();
        
        if (mc != null) {
            salvaConcerto(mc);
        }
    }
    
    @FXML
    private void provaSalvaAction(ActionEvent event) {
        ModelConcerto mc = getConcertoCreato();
        
        if (mc != null) {
            salvaConcerto(mc);
            apriCreaSuonata(mc);
        }
    }
    
    @FXML
    private void provaSenzaSalvareAction(ActionEvent event) {
        ModelConcerto mc = getConcertoCreato();
        
        if (mc != null) {
            apriCreaSuonata(mc);
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

    /**
     * @return the lastChooserFolder
     */
    public File getLastChooserFolder() {
        return lastChooserFolder;
    }

    /**
     * @param lastChooserFolder the lastChooserFolder to set
     */
    public void setLastChooserFolder(File lastChooserFolder) {
        this.lastChooserFolder = lastChooserFolder;
    }

    
    private ModelConcerto getConcertoCreato(){
        ModelConcerto mc = new ModelConcerto();
        
        if (!txtNomeConcerto.getText().trim().equals("")) {
            mc.setNomeConcerto(txtNomeConcerto.getText().trim());
            for (ListIterator<Node> it = listCampane.getChildren().listIterator(); it.hasNext();) {
                CampanileNuovoFieldsetController cnf = (CampanileNuovoFieldsetController) it.next();
                ModelCampana campana = cnf.getMb();
                if (campana != null) {
                    mc.pushCampana(cnf.getMb());
                } else {
                    return null;
                }
            }
        } else {
            new AlertDialog((Stage) this.getScene().getWindow(), "Il nome del concerto non può essere vuoto", AlertDialog.ICON_ERROR).showAndWait();
            return null;
        }
        
        return mc;
    }
    
    private void salvaConcerto(ModelConcerto mc) {
        mc.saveFileConcerto(((Node) this).getScene().getWindow(), lastChooserFolder);
    }
    
    private void apriCreaSuonata(ModelConcerto mc){
        SpartitoBaseController sbc = new SpartitoBaseController();
        ((BorderPane)this.getParent()).setCenter(sbc);
        sbc.setModelConcerto(mc);
    }
}
