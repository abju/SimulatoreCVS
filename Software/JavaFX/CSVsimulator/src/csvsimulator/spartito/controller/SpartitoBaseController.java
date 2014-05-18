/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csvsimulator.spartito.controller;

import csvsimulator.model.ModelBattuta;
import csvsimulator.model.ModelConcerto;
import csvsimulator.model.ModelSuonata;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
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
    private SpartitoPentagrammaMascheraController spartitoPentagrammaMaschera;
    private SpartitoChartController spartitoChar;

    private ModelConcerto modelConcerto;
    private ModelSuonata modelSuonata;
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
        this.spartitoPentagrammaMaschera = new SpartitoPentagrammaMascheraController();
        this.spartitoPentagrammaMaschera.setSpartitoBaseController(this);
        
        
        bpPentagramma.setCenter(this.spartitoPentagrammaMaschera);
        
        setModelSuonata(new ModelSuonata());
        
        NumberAxis xAxis = new NumberAxis("X-Axis", 0, 1, 1);
        NumberAxis yAxis = new NumberAxis("Y-Axis", 0.0, 0.0, 0.0);
        xAxis.setForceZeroInRange(false);

        
        ObservableList<XYChart.Series> data = FXCollections.observableArrayList(
            new ScatterChart.Series("Series 1", FXCollections.<ScatterChart.Data>observableArrayList(
                new XYChart.Data(0.2, 3.5),
                new XYChart.Data(0.7, 4.6),
                new XYChart.Data(1.8, 1.7),
                new XYChart.Data(2.1, 2.8),
                new XYChart.Data(4.0, 2.2),
                new XYChart.Data(4.1, 2.6),
                new XYChart.Data(4.5, 2.0),
                new XYChart.Data(6.0, 3.0),
                new XYChart.Data(7.0, 2.0),
                new XYChart.Data(7.8, 4.0),
                new XYChart.Data(9, 4.0)
            ))
        );
        spartitoChar = new SpartitoChartController(new NumberAxis("Tempo", 0, 0, 1000), new CategoryAxis());
        spartitoChar.setLegendVisible(false);
        spartitoChar.setAnimated(false);
        spartitoChar.setSpartitoBC(this);
        
        xAxis.setLowerBound(xAxis.getLowerBound()+1);
        xAxis.setUpperBound(xAxis.getUpperBound()+1);
        
        
        bpPentagramma.setCenter(spartitoChar);
        bpPentagramma.setBottom(spartitoChar.getScrollBarX());
        
        this.navbar = new NavbarBaseController();
        setTop(this.navbar);
        this.navbar.setUpNavSpartito(this);

        this.optionBar = new SpartitoOptionBarController(this, this.spartitoPentagramma, this.spartitoChar);
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
        ModelBattuta mb = new ModelBattuta();
        if (t.getCode() == KeyCode.SPACE || t.getCode() == KeyCode.ENTER) {
            String text = nuovaBattuta.getText().trim();
            boolean battutaValida = true;
            if (!text.equals("")) {
                String[] battute = text.split("/");
                for (int i = 0; i < battute.length; i++) {
                    if (mb.pushCampanaByNome(battute[i], modelConcerto) != null) {
                        battutaValida = false;
                    }
                }

                if (battutaValida) {
                    this.addBattuta(mb);
                    
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

        
        final Integer numero_battuta = this.modelSuonata.pushBattuta(mb);
        this.spartitoChar.pushBattuta(numero_battuta);
        
    }

    public void popBattuta() {
        Integer index = spartitoPentagramma.getModelSuonata().getListaBattute().size() - 1;
        if (index >= 0) {
            elenco_battute.getChildren().remove(elenco_battute.getChildren().get(index));
            spartitoPentagramma.popBattuta();
            spartitoChar.popBattuta();
            
            modelSuonata.getListaBattute().remove(modelSuonata.getListaBattute().size() - 1);
        }
    }
    
    public void setActiveBattuta(Integer i){
      if(i > 0){
        elenco_battute.getChildren().get(i-1).getStyleClass().remove("battuta-active");
      }
      if(!elenco_battute.getChildren().get(i).getStyleClass().toString().contains("battuta-active")){
        elenco_battute.getChildren().get(i).getStyleClass().add("battuta-active");
      }
      //System.err.println(elenco_battute.getChildren().get(i).getStyleClass().toString());
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
    
    public void setTimePlayer(Integer i){
      this.spartitoPentagrammaMaschera.setArrowPosition(i);
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
        this.spartitoPentagrammaMaschera.setSpartitoPentagramma(this.spartitoPentagramma);
        this.spartitoChar.setModelConcerto(modelConcerto);
        this.spartitoChar.setModelSuonata(this.getModelSuonata());
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

  /**
   * @return the modelSuonata
   */
  public ModelSuonata getModelSuonata() {
    return modelSuonata;
  }

  /**
   * @param modelSuonata the modelSuonata to set
   */
  public void setModelSuonata(ModelSuonata modelSuonata) {
    this.modelSuonata = modelSuonata;
  }

}
