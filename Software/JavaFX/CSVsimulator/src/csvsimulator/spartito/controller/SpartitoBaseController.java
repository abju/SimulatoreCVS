/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csvsimulator.spartito.controller;

import csvsimulator.model.ModelBattuta;
import csvsimulator.model.ModelConcerto;
import csvsimulator.navbar.controller.NavbarBaseController;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.media.AudioClip;

/**
 *
 * @author lion
 */
public class SpartitoBaseController extends BorderPane implements Initializable {

    @FXML
    private TextField nuovaBattuta;

    @FXML
    private SplitPane divisore;

    @FXML
    private TilePane elenco_battute; //Traformare in TilePane

    @FXML
    private SpartitoPentagrammaController spartitoPentagramma;

    private ModelConcerto modelConcerto;
    private NavbarBaseController navbar;
    private BorderPane leftBar;

    private SpartitoOptionBarController optionBar;

    public SpartitoBaseController() {
        init();
    }

    public SpartitoBaseController(ModelConcerto modelConcerto, NavbarBaseController navbar, BorderPane leftBar) {
        init();
        this.modelConcerto = modelConcerto;
        this.navbar = navbar;
        this.leftBar = leftBar;

        this.navbar.setUpNavSpartito(this);
        this.spartitoPentagramma.getModelSuonata().setConcerto(modelConcerto);
        this.spartitoPentagramma.setSpartitoBaseController(this);

        this.optionBar = new SpartitoOptionBarController(this, this.spartitoPentagramma);
        this.leftBar.setCenter(this.optionBar);
        this.optionBar.getPaneBattutaNonSelezionata().setVisible(true);
        this.optionBar.getPaneBattutaSelezionata().setVisible(false);
    }

    private void init() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/csvsimulator/spartito/view/spartitoBase2.fxml"));
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
        nuovaBattuta.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                //TODO
                if (t1.matches("^.*[^a-zA-Z0-9/].*$")) {
                    nuovaBattuta.setText(t);
                }

                if (t1.length() > 1) {
                    if (t1.charAt(t1.length() - 2) == '/' && t1.charAt(t1.length() - 1) == '/') {
                        nuovaBattuta.setText(t);
                    }
                }
            }
        });
    }

    @FXML
    private void nuovaBattutaKeyPress(KeyEvent t) {
        if (t.getCode() == KeyCode.BACK_SPACE) {
            if (nuovaBattuta.getCharacters().length() <= 0) {
                popBattuta();
            }
        }
    }

    @FXML
    private void nuovaBattutaKeyReleased(KeyEvent t) {
        ModelBattuta md = new ModelBattuta();
        if (t.getCode() == KeyCode.SPACE || t.getCode() == KeyCode.ENTER) {
            String text = nuovaBattuta.getText().trim();
            boolean battutaValida = true;
            if (!text.equals("")) {
                String[] battute = text.split("/");
                for (int i = 0; i < battute.length; i++) {
                    if (md.pushCampanaByNome(battute[i], modelConcerto) != null) {
                        battutaValida = false;
                    }
                }

                if (battutaValida) {
                    this.addBattuta(md);
                }

            }
            nuovaBattuta.setText("");
        }
    }

    /*METODI PRIVATI*/
    private void addBattuta(final ModelBattuta mb) {
              
        
        //mb.play(modelConcerto);
        Label label = new Label(mb.getNomeBattuta(modelConcerto));
        label.setMaxWidth(USE_PREF_SIZE);
        label.setPrefWidth(100);
        label.setMaxWidth(USE_PREF_SIZE);
        label.getStyleClass().add("battuta");
        label.setAlignment(Pos.CENTER);

        label.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                //Trovo l'indice della battuta con hasCode
                int numero_battuta = spartitoPentagramma.getModelSuonata().getNumberBattutaFromModelBattuta(mb);
                getOptionBar().setUpOptionBattuta(numero_battuta);
            }
        });

        elenco_battute.getChildren().add(elenco_battute.getChildren().size() - 1, label);

        //Ridisegna cosi posso avere le coordinate deve essere fatto proprio alla fine
        elenco_battute.layout();
        getSpartitoPentagramma().setBattutePerRiga(getColumns());
        getSpartitoPentagramma().pushBattuta(mb, label);

    }

    public void popBattuta() {
        Integer index = spartitoPentagramma.getModelSuonata().getListaBattute().size() - 1;
        if (index >= 0) {
            elenco_battute.getChildren().remove(elenco_battute.getChildren().get(index));
            spartitoPentagramma.popBattuta();
        }
    }

    public Integer getColumns() {
        double cols;
        cols = (elenco_battute.getWidth() - elenco_battute.getPadding().getLeft() - elenco_battute.getPadding().getRight()) / elenco_battute.getTileWidth();
        return (int) cols;
    }

    /**
     * @return the modelConcerto
     */
    public ModelConcerto getModelConcerto() {
        return modelConcerto;
    }

    /**
     * @param modelConcerto the modelConcerto to set
     */
    public void setModelConcerto(ModelConcerto modelConcerto) {
        this.modelConcerto = modelConcerto;
    }

    /**
     * @return the navbar
     */
    public NavbarBaseController getNavbar() {
        return navbar;
    }

    /**
     * @param navbar the navbar to set
     */
    public void setNavbar(NavbarBaseController navbar) {
        this.navbar = navbar;
    }

    /**
     * @return the spartitoPentagramma
     */
    public SpartitoPentagrammaController getSpartitoPentagramma() {
        return spartitoPentagramma;
    }

    /**
     * @param spartitoPentagramma the spartitoPentagramma to set
     */
    public void setSpartitoPentagramma(SpartitoPentagrammaController spartitoPentagramma) {
        this.spartitoPentagramma = spartitoPentagramma;
    }

    /**
     * @return the leftBar
     */
    public BorderPane getLeftBar() {
        return leftBar;
    }

    /**
     * @param leftBar the leftBar to set
     */
    public void setLeftBar(BorderPane leftBar) {
        this.leftBar = leftBar;
    }

    /**
     * @return the optionBar
     */
    public SpartitoOptionBarController getOptionBar() {
        return optionBar;
    }

    /**
     * @param optionBar the optionBar to set
     */
    public void setOptionBar(SpartitoOptionBarController optionBar) {
        this.optionBar = optionBar;
    }

}
