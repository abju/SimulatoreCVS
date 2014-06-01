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
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
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

  @FXML
  private BigDecimalField tempoSuonata;

  @FXML
  private CheckBox cbReboto;

  @FXML
  private CheckBox cbOmessa;

  private SpartitoBaseController spartitoBaseController;
  private SpartitoChartController spartitoChartController;
  private int nBattutaSelezionata = -1;

  public SpartitoOptionBarController() {
    init();
  }

  public SpartitoOptionBarController(SpartitoBaseController spartitoBaseController, SpartitoChartController spartitoChartController) {
    init();
    this.spartitoBaseController = spartitoBaseController;
    this.spartitoChartController = spartitoChartController;

    tempoSuonata.setMinValue(new BigDecimal(0.5));
    tempoSuonata.setNumber(new BigDecimal(this.spartitoBaseController.getModelSuonata().getTempoSuonata() / 1000));
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

    //SUONATA
    tempoSuonata.numberProperty().addListener(new ChangeListener<BigDecimal>() {

      @Override
      public void changed(ObservableValue<? extends BigDecimal> ov, BigDecimal t, BigDecimal t1) {
        spartitoBaseController.getModelSuonata().setTempoSuonata(t1.doubleValue() * 1000);
        spartitoBaseController.getModelSuonata().setTempoRitorno(t1.doubleValue() * 1000);
      }
    });

    //BATTUTA
    spinnerContrattempo.numberProperty().addListener(new ChangeListener<BigDecimal>() {
      @Override
      public void changed(ObservableValue<? extends BigDecimal> ov, BigDecimal oldValue, BigDecimal newValue) {
        Double contrattempo = newValue.doubleValue() * 1000;
        contrattempo = contrattempo / spartitoBaseController.getModelSuonata().getTempoSuonata();
        ModelBattuta mb = spartitoBaseController.getModelSuonata().getListaBattute().get(nBattutaSelezionata);
        mb.setContrattempo(getNumeroCampana(), contrattempo);

        spartitoChartController.refreshPosizioneCampane();
      }
    });

    optBattutaSelectCampana.valueProperty().addListener(new ChangeListener() {

      @Override
      public void changed(ObservableValue ov, Object oldValue, Object newValue) {
        if (optBattutaSelectCampana.getSelectionModel().getSelectedIndex() >= 0) {
          ModelBattuta mb = spartitoBaseController.getModelSuonata().getListaBattute().get(nBattutaSelezionata);
          Integer campanaCode = getNumeroCampana();          
          
          if (oldValue != null) {
            Integer oldCampanaCode = spartitoBaseController.getModelConcerto().getCampanaByNome(oldValue.toString()).getNumero();
            mb.getRebotoProperty(oldCampanaCode).unbindBidirectional(cbReboto.selectedProperty());
            mb.getOmessaProperty(oldCampanaCode).unbindBidirectional(cbOmessa.selectedProperty());
          }
          
          

          spinnerContrattempo.setMaxValue(new BigDecimal(spartitoBaseController.getModelSuonata().getMaxContrattempoSec(nBattutaSelezionata, campanaCode)));
          spinnerContrattempo.setMinValue(new BigDecimal(-spartitoBaseController.getModelSuonata().getMinContrattempoSec(nBattutaSelezionata, campanaCode)));
          try {
            spinnerContrattempo.setNumber(new BigDecimal(mb.getListaCampane().get(campanaCode) * spartitoBaseController.getModelSuonata().getTempoSuonata() / 1000));
          } catch (IllegalArgumentException ex) {

          }

          cbReboto.setSelected(mb.getReboto(campanaCode));
          cbOmessa.setSelected(mb.getOmessa(campanaCode));
          

          mb.getRebotoProperty(getNumeroCampana()).bindBidirectional(cbReboto.selectedProperty());
          mb.getOmessaProperty(getNumeroCampana()).bindBidirectional(cbOmessa.selectedProperty());
        }
      }
    });

    cbOmessa.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
      spartitoBaseController.getModelSuonata().getListaBattute().get(nBattutaSelezionata).setOmessa(getNumeroCampana(), t1);
    });

  }

  public void setUpOptionBattuta(Integer numero_battuta) {
    getPaneBattutaNonSelezionata().setVisible(false);

    //Tolgo i bind vecchi se ci sono
    ModelBattuta mb;
    if (nBattutaSelezionata != -1 && spartitoBaseController.getModelSuonata().getListaBattute().size() > nBattutaSelezionata ) {
      mb = spartitoBaseController.getModelSuonata().getListaBattute().get(nBattutaSelezionata);
      mb.getRebotoProperty(getNumeroCampana()).unbindBidirectional(cbReboto.selectedProperty());
      mb.getOmessaProperty(getNumeroCampana()).unbindBidirectional(cbOmessa.selectedProperty());
    }

    nBattutaSelezionata = numero_battuta;
    mb = spartitoBaseController.getModelSuonata().getListaBattute().get(nBattutaSelezionata);

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

    cbReboto.setSelected(mb.getReboto(getNumeroCampana()));
    cbOmessa.setSelected(mb.getOmessa(getNumeroCampana()));

    try {
      spinnerContrattempo.setMaxValue(new BigDecimal(spartitoBaseController.getModelSuonata().getMaxContrattempoSec(numero_battuta, namesKey.get(0))));
      spinnerContrattempo.setMinValue(new BigDecimal(-spartitoBaseController.getModelSuonata().getMinContrattempoSec(numero_battuta, namesKey.get(0))));
      spinnerContrattempo.setNumber(new BigDecimal(mb.getContrattempo(namesKey.get(0)) * spartitoBaseController.getModelSuonata().getTempoSuonata() / 1000));
    } catch (IllegalArgumentException ex) {

    }

    getPaneBattutaSelezionata().setVisible(true);
  }

  private Integer getNumeroCampana() {
    ModelBattuta mb = spartitoBaseController.getModelSuonata().getListaBattute().get(nBattutaSelezionata);
    return (Integer) mb.getListaCampane().keySet().toArray()[optBattutaSelectCampana.getSelectionModel().getSelectedIndex()];
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

}
