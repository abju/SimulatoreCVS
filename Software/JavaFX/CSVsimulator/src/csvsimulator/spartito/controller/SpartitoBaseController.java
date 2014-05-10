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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;

/**
 *
 * @author lion
 */
public class SpartitoBaseController extends BorderPane implements Initializable {

    private BorderPane mainBorderPain;
    @FXML
    private TextField nuovaBattuta;
    @FXML
    private SplitPane divisore; 
    @FXML
    private TilePane elenco_battute;
    @FXML
    private ScrollPane scrollSpartito;
    @FXML
    private ScrollPane scrollPentagramma;
    @FXML
    private BorderPane bpPentagramma;
    
    private SpartitoPentagrammaController spartitoPentagramma;

    private ModelConcerto modelConcerto;
    private NavbarBaseController navbar;
    private BorderPane leftBar;

    private SpartitoOptionBarController optionBar;

    public SpartitoBaseController() {
        init();
        //this.modelConcerto = loadModelConcerto();
    }

    public SpartitoBaseController(ModelConcerto modelConcerto) {
        init();
        this.modelConcerto = modelConcerto;
        this.spartitoPentagramma.setSpartitoBaseController(this);
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

        this.spartitoPentagramma = new SpartitoPentagrammaController();
        bpPentagramma.setCenter(this.spartitoPentagramma);
        this.navbar = new NavbarBaseController();
        setTop(this.navbar);
        this.navbar.setUpNavSpartito(this);

        this.optionBar = new SpartitoOptionBarController(this, this.spartitoPentagramma);
        setLeft(this.optionBar);

        this.optionBar.getPaneBattutaNonSelezionata().setVisible(true);
        this.optionBar.getPaneBattutaSelezionata().setVisible(false);
    }

    public ModelConcerto loadModelConcerto() {
        ModelConcerto concerto = null;

        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Concerto campane sistema veronese (*.csvc)", "*.csvc");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showOpenDialog(((Node)this).getScene().getWindow());
        if (file == null) {
            //stage.close();
            System.exit(0);
        }
        String path = file.getAbsolutePath();

        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(path));
            concerto = (ModelConcerto) ois.readObject();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            
        } catch (ClassNotFoundException e) {
        }

        

        return concerto;
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
        //label.getStyleClass().add("battuta-active");
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
        scrollSpartito.layout();        
        scrollSpartito.setVvalue(nuovaBattuta.getLayoutY());
        
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
    
    public void setActiveBattuta(Integer i){
      if(i > 0){
        elenco_battute.getChildren().get(i-1).getStyleClass().remove("battuta-active");
      }
      elenco_battute.getChildren().get(i).getStyleClass().add("battuta-active");
    }
    
    public void removeAllActiveBattuta(){
      for (Object b : elenco_battute.getChildren()) {
        if(elenco_battute.getChildren().indexOf(b) != elenco_battute.getChildren().size()-1){
          ((Label) b).getStyleClass().remove("battuta-active");
        }
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
        this.spartitoPentagramma.setSpartitoBaseController(this);
        this.spartitoPentagramma.getModelSuonata().setConcerto(this.modelConcerto);
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

    /**
     * @return the scrollPentagramma
     */
    public ScrollPane getScrollPentagramma() {
        return scrollPentagramma;
    }

    /**
     * @param scrollPentagramma the scrollPentagramma to set
     */
    public void setScrollPentagramma(ScrollPane scrollPentagramma) {
        this.scrollPentagramma = scrollPentagramma;
    }

}
