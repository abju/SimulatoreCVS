/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csvsimulator.spartito.controller;

import csvsimulator.model.ModelBattuta;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import jfxtras.labs.scene.control.BigDecimalField;

/**
 *
 * @author lion
 */
public class SpartitoOptionBarController extends BorderPane implements Initializable {

    @FXML
    private BigDecimalField spinnerContrattempo;

    @FXML
    private AnchorPane paneBattutaNonSelezionata;

    @FXML
    private GridPane paneBattutaSelezionata;

    @FXML
    private Label nomeBattuta;

    @FXML
    private Label numeroBattuta;

    @FXML
    private ChoiceBox optBattutaSelectCampana;

    private SpartitoBaseController spartitoBaseController;
    private SpartitoPentagrammaController spartitoPentagrammaController;
    private int nBattutaSelezionata;

    public SpartitoOptionBarController() {
        init();
    }

    public SpartitoOptionBarController(SpartitoBaseController spartitoBaseController, SpartitoPentagrammaController spartitoPentagrammaController) {
        init();
        this.spartitoBaseController = spartitoBaseController;
        this.spartitoPentagrammaController = spartitoPentagrammaController;
    }

    private void init() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/csvsimulator/spartito/view/spartitoOptionBar.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        spinnerContrattempo.numberProperty().addListener(new ChangeListener<BigDecimal>() {
            @Override
            public void changed(ObservableValue<? extends BigDecimal> ov, BigDecimal oldValue, BigDecimal newValue) {
                Double contrattempo = newValue.doubleValue()*1000;
                contrattempo = contrattempo / spartitoPentagrammaController.getModelSuonata().getTempoSuonata();
                spartitoPentagrammaController.setContrattempoCampana(nBattutaSelezionata, optBattutaSelectCampana.getSelectionModel().getSelectedIndex(), contrattempo);
                spartitoPentagrammaController.refreshPosizioneCampane();
            }
        });

        optBattutaSelectCampana.valueProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue ov, Object oldValue, Object newValue) {
                if (optBattutaSelectCampana.getSelectionModel().getSelectedIndex() >= 0) {
                    ModelBattuta mb = spartitoPentagrammaController.getModelSuonata().getListaBattute().get(nBattutaSelezionata);
                    Integer campanaCode = (Integer) mb.getListaCampane().keySet().toArray()[optBattutaSelectCampana.getSelectionModel().getSelectedIndex()];
                    
                    spinnerContrattempo.setMaxValue(new BigDecimal(spartitoPentagrammaController.getModelSuonata().getMaxContrattempoSec(nBattutaSelezionata, campanaCode)));
                    spinnerContrattempo.setMinValue(new BigDecimal(- spartitoPentagrammaController.getModelSuonata().getMinContrattempoSec(nBattutaSelezionata, campanaCode)));
                    spinnerContrattempo.setNumber(new BigDecimal(mb.getListaCampane().get(campanaCode) * spartitoPentagrammaController.getModelSuonata().getTempoSuonata() / 1000));
                }
            }
        });

    }

    public void setUpOptionBattuta(Integer numero_battuta) {
        getPaneBattutaNonSelezionata().setVisible(false);
        getPaneBattutaSelezionata().setVisible(true);

        nBattutaSelezionata = numero_battuta;
        ModelBattuta mb = spartitoPentagrammaController.getModelSuonata().getListaBattute().get(nBattutaSelezionata);

        //setto il nome della battuta e il numero della battuta
        nomeBattuta.setText(mb.getNomeBattuta(spartitoBaseController.getModelConcerto()));
        numeroBattuta.setText((nBattutaSelezionata + 1) + "");

        //dalla battuta estraggo le singole campane per la select box e lo spinner
        ObservableList names = FXCollections.observableArrayList();
        ObservableList<Integer> namesKey = FXCollections.observableArrayList();
        for (Map.Entry<Integer, Double> en : mb.getListaCampane().entrySet()) {
            Integer keyCampana = en.getKey();
            names.add(spartitoBaseController.getModelConcerto().getCampanaByNumero(keyCampana).getNome());
            namesKey.add(keyCampana);
        }
        optBattutaSelectCampana.setItems(names);
        optBattutaSelectCampana.setValue(names.get(0));
        
        spinnerContrattempo.setMaxValue(new BigDecimal(spartitoPentagrammaController.getModelSuonata().getMaxContrattempoSec(numero_battuta, namesKey.get(0))));
        spinnerContrattempo.setMinValue(new BigDecimal(- spartitoPentagrammaController.getModelSuonata().getMinContrattempoSec(numero_battuta, namesKey.get(0))));
        spinnerContrattempo.setNumber(new BigDecimal(mb.getListaCampane().get(namesKey.get(0)) * spartitoPentagrammaController.getModelSuonata().getTempoSuonata() / 1000));
    }
    
    public void setUpOptionBattuta(Integer numero_battuta, Integer numero_campana) {
        this.setUpOptionBattuta(numero_battuta);
        optBattutaSelectCampana.setValue(spartitoBaseController.getModelConcerto().getCampanaByNumero(numero_campana).getNome());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //spinnerContrattempo.setFormat(new DecimalFormat("#,##0.00 sec"));
    }

    /**
     * @return the spartitoBaseController
     */
    public SpartitoBaseController getSpartitoBaseController() {
        return spartitoBaseController;
    }

    /**
     * @param spartitoBaseController the spartitoBaseController to set
     */
    public void setSpartitoBaseController(SpartitoBaseController spartitoBaseController) {
        this.spartitoBaseController = spartitoBaseController;
    }

    /**
     * @return the spinnerContrattempo
     */
    public BigDecimalField getSpinnerContrattempo() {
        return spinnerContrattempo;
    }

    /**
     * @param spinnerContrattempo the spinnerContrattempo to set
     */
    public void setSpinnerContrattempo(BigDecimalField spinnerContrattempo) {
        this.spinnerContrattempo = spinnerContrattempo;
    }

    /**
     * @return the paneBattutaNonSelezionata
     */
    public AnchorPane getPaneBattutaNonSelezionata() {
        return paneBattutaNonSelezionata;
    }

    /**
     * @param paneBattutaNonSelezionata the paneBattutaNonSelezionata to set
     */
    public void setPaneBattutaNonSelezionata(AnchorPane paneBattutaNonSelezionata) {
        this.paneBattutaNonSelezionata = paneBattutaNonSelezionata;
    }

    /**
     * @return the paneBattutaSelezionata
     */
    public GridPane getPaneBattutaSelezionata() {
        return paneBattutaSelezionata;
    }

    /**
     * @param paneBattutaSelezionata the paneBattutaSelezionata to set
     */
    public void setPaneBattutaSelezionata(GridPane paneBattutaSelezionata) {
        this.paneBattutaSelezionata = paneBattutaSelezionata;
    }

    /**
     * @return the nomeBattuta
     */
    public Label getNomeBattuta() {
        return nomeBattuta;
    }

    /**
     * @param nomeBattuta the nomeBattuta to set
     */
    public void setNomeBattuta(Label nomeBattuta) {
        this.nomeBattuta = nomeBattuta;
    }

    /**
     * @return the numeroBattuta
     */
    public Label getNumeroBattuta() {
        return numeroBattuta;
    }

    /**
     * @param numeroBattuta the numeroBattuta to set
     */
    public void setNumeroBattuta(Label numeroBattuta) {
        this.numeroBattuta = numeroBattuta;
    }

    /**
     * @return the optBattutaSelectCampana
     */
    public ChoiceBox getOptBattutaSelectCampana() {
        return optBattutaSelectCampana;
    }

    /**
     * @param optBattutaSelectCampana the optBattutaSelectCampana to set
     */
    public void setOptBattutaSelectCampana(ChoiceBox optBattutaSelectCampana) {
        this.optBattutaSelectCampana = optBattutaSelectCampana;
    }

    /**
     * @return the nBattuta
     */
    public int getnBattuta() {
        return nBattutaSelezionata;
    }

    /**
     * @param nBattuta the nBattuta to set
     */
    public void setnBattuta(int nBattuta) {
        this.nBattutaSelezionata = nBattuta;
    }

    /**
     * @return the spartitoPentagrammaController
     */
    public SpartitoPentagrammaController getSpartitoPentagrammaController() {
        return spartitoPentagrammaController;
    }

    /**
     * @param spartitoPentagrammaController the spartitoPentagrammaController to
     * set
     */
    public void setSpartitoPentagrammaController(SpartitoPentagrammaController spartitoPentagrammaController) {
        this.spartitoPentagrammaController = spartitoPentagrammaController;
    }

}
