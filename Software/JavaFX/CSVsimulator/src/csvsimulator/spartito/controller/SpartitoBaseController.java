/* 
 * Copyright (C) 2014 Marco Dalla Riva <marco.dallariva@outlook.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package csvsimulator.spartito.controller;

import csvsimulator.model.ModelBattuta;
import csvsimulator.model.ModelCampana;
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
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;
import org.controlsfx.control.PopOver;

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

  private SpartitoChartController spartitoChar;

  private ModelConcerto modelConcerto;
  private ModelSuonata modelSuonata;
  private NavbarBaseController navbar;
  private BorderPane leftBar;

  private SpartitoOptionBarController optionBar;
  private PopOver popOver = new PopOver();

  public SpartitoBaseController() {
    init();
    //this.modelConcerto = loadModelConcerto();
  }

  public SpartitoBaseController(ModelConcerto modelConcerto) {
    init();
    this.modelConcerto = modelConcerto;

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

    setModelSuonata(new ModelSuonata());

    spartitoChar = new SpartitoChartController(new NumberAxis("Tempo", 0, 0, 1000), new CategoryAxis());
    spartitoChar.setLegendVisible(false);
    spartitoChar.setAnimated(false);
    spartitoChar.setSpartitoBC(this);

    bpPentagramma.setCenter(spartitoChar);
    bpPentagramma.setBottom(spartitoChar.getScrollBarX());

    this.navbar = new NavbarBaseController();
    setTop(this.navbar);
    this.navbar.setUpNavSpartito(this);

    this.optionBar = new SpartitoOptionBarController(this, this.spartitoChar);
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
    File file = fileChooser.showOpenDialog(((Node) this).getScene().getWindow());
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
    } catch (IOException | ClassNotFoundException e) {

    }

    return concerto;
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    nuovaBattuta.textProperty().addListener((ObservableValue<? extends String> ov, String t, String t1) -> {
      if (t1.matches("^.*[^a-zA-Z0-9/].*$")) {
        nuovaBattuta.setText(t);
      }

      if (t1.length() > 1) {
        if (t1.charAt(t1.length() - 2) == '/' && t1.charAt(t1.length() - 1) == '/') {
          nuovaBattuta.setText(t);
        }
      }
    });
    nuovaBattuta.setId("inputTextNuovaBattuta");

  }

  /**
   * 
   * @param t 
   */
  @FXML
  private void nuovaBattutaKeyPress(KeyEvent t) {
    String text = nuovaBattuta.getText().trim();
    if (t.isMetaDown()) {
      t.consume();
    }

    if (t.getCode() == KeyCode.BACK_SPACE) {
      if (nuovaBattuta.getCharacters().length() <= 0) {
        popBattuta();
      }
    } else if (t.isMetaDown() && t.getText().length() > 0) {

      ModelCampana mc = modelConcerto.getCampanaByNome(t.getText());

      String text2 = t.getText() + "/";
      if (mc.getNumero() < modelConcerto.getListaCampane().size() - 2) {
        text2 += modelConcerto.getCampanaByNumero(mc.getNumero() + 2).getNome();
        checkBattuta(text2);
      }
      nuovaBattuta.setText("");
    }
  }

  /**
   * 
   * @param t 
   */
  @FXML
  private void nuovaBattutaKeyReleased(KeyEvent t) {

    String text = nuovaBattuta.getText().trim();
    if (t.getCode() == KeyCode.SPACE || t.getCode() == KeyCode.ENTER) {
      checkBattuta(text);
      nuovaBattuta.setText("");
    } else if (t.isControlDown() && t.getText().length() > 0) {
      checkBattuta(t.getText());
      nuovaBattuta.setText("");
    }
  }

  /**
   * 
   * @param index 
   */
  public void moveInputTextNuovaBattuta(Integer index) {
    elenco_battute.getChildren().remove(nuovaBattuta);
    elenco_battute.getChildren().add(index, nuovaBattuta);
  }

  /**
   * 
   * @param text 
   */
  private void checkBattuta(String text) {
    ModelBattuta mb = new ModelBattuta();
    boolean battutaValida = true;
    if (text.toUpperCase().equals("T")) {
      modelConcerto.getListaCampane().stream().forEach((modelCampana) -> {
        mb.pushCampanaByNome(modelCampana.getNome(), modelConcerto);
      });
      this.addBattuta(mb);
    } else if (text.toUpperCase().equals("P")) {
      mb.pushCampanaByNome("P", modelConcerto);
      this.addBattuta(mb);
    } else if (!text.equals("")) {
      String[] battute = text.split("/");
      for (String battute1 : battute) {
        if (mb.pushCampanaByNome(battute1, modelConcerto) != null) {
          battutaValida = false;
        }
      }

      if (battutaValida) {
        this.addBattuta(mb);

      }

    }
  }

  /**
   * 
   * @param mb 
   */
  private void addBattuta(final ModelBattuta mb) {

    //Ridisegna cosi posso avere le coordinate deve essere fatto proprio alla fine
    scrollSpartito.layout();
    scrollSpartito.setVvalue(nuovaBattuta.getLayoutY());

    final Integer numero_battuta = elenco_battute.getChildren().indexOf(nuovaBattuta);
    this.modelSuonata.addBattuta(mb, numero_battuta);
    //final Integer numero_battuta = this.modelSuonata.pushBattuta(mb);

    this.spartitoChar.addBattuta(numero_battuta);

    SpartitoLabelBattutaController slb = new SpartitoLabelBattutaController(this);
    slb.getLabel().setText(mb.getNomeBattuta(modelConcerto));
    slb.setContexMenu(mb);

    elenco_battute.getChildren().add(numero_battuta, slb);
  }

  /**
   *
   * @param index
   */
  public void eliminaBattua(int index) {
    Integer indexText = elenco_battute.getChildren().indexOf(nuovaBattuta);
    modelSuonata.removeBattuta(index);

    //Aggiorno lo spartito
    elenco_battute.getChildren().remove(nuovaBattuta);
    elenco_battute.getChildren().remove(index);

    if (indexText <= index) {
      elenco_battute.getChildren().add(indexText, nuovaBattuta);
    } else {
      elenco_battute.getChildren().add(indexText - 1, nuovaBattuta);
    }

  }

  /**
   *
   */
  public void popBattuta() {
    Integer index = this.modelSuonata.getListaBattute().size() - 1;
    if (index >= 0) {
      elenco_battute.getChildren().remove(elenco_battute.getChildren().get(index));
      spartitoChar.popBattuta();
      modelSuonata.getListaBattute().remove(modelSuonata.getListaBattute().size() - 1);

    }
  }

  /**
   * 
   * @param i 
   */
  public void setActiveBattuta(Integer i) {
    if (i > 0) {
      elenco_battute.getChildren().get(i - 1).getStyleClass().remove("battuta-active");
    }
    if (!elenco_battute.getChildren().get(i).getStyleClass().toString().contains("battuta-active")) {
      elenco_battute.getChildren().get(i).getStyleClass().add("battuta-active");
    }
    //System.err.println(elenco_battute.getChildren().get(i).getStyleClass().toString());
  }

  /**
   * 
   */
  public void removeAllActiveBattuta() {
    elenco_battute.getChildren().stream().filter((b) -> (elenco_battute.getChildren().indexOf(b) != elenco_battute.getChildren().size() - 1)).forEach((Node b) -> {
      b.getStyleClass().remove("battuta-active");
    });
  }

  /**
   * 
   * @param i 
   */
  public void setTimePlayer(Integer i) {
    //this.spartitoPentagrammaMaschera.setArrowPosition(i);
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
    this.modelSuonata.setConcerto(this.modelConcerto);
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
