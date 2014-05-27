/*
 * The MIT License
 *
 * Copyright 2014 Marco Dalla Riva <marco.dallariva@outlook.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package csvsimulator.spartito.controller;

import csvsimulator.model.ModelBattuta;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.layout.BorderPane;

import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author Marco Dalla Riva <marco.dallariva@outlook.com>
 */
public class SpartitoLabelBattutaController extends BorderPane implements Initializable {

  @FXML
  private BorderPane node;

  @FXML
  private BorderPane bottomBorderPain;

  @FXML
  private Label label;

  @FXML
  private BorderPane topBorderPain;

  public SpartitoLabelBattutaController() {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/csvsimulator/spartito/view/spartitoLabelBattuta.fxml"));
    fxmlLoader.setRoot(this);
    fxmlLoader.setController(this);

    try {
      fxmlLoader.load();
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }

    
  }
  
  public void setContexMenu(ModelBattuta mb){
    final ContextMenu contextMenu = new ContextMenu();
    Menu menuReboti = new Menu("Ribattute");
    MenuItem allReboti = new MenuItem("Tutte le campane");
    
    menuReboti.getItems().addAll(allReboti);
    Map<Integer, Double> listaCampane = mb.getListaCampane();
    for (Map.Entry<Integer, Double> entry : listaCampane.entrySet()) {
      Integer integer = entry.getKey();
      Double double1 = entry.getValue();
      
      CheckMenuItem cmp = new CheckMenuItem(integer.toString());
      mb.getRebotoProperty(integer).bindBidirectional(cmp.selectedProperty());
      
      menuReboti.getItems().addAll(cmp);
    }
    
    
    Menu menuOmesse = new Menu("Omesse");
    MenuItem allOmesse = new MenuItem("Tutte le campane");
    
    menuOmesse.getItems().addAll(allOmesse);
    for (Map.Entry<Integer, Double> entry : listaCampane.entrySet()) {
      Integer integer = entry.getKey();
      Double double1 = entry.getValue();
      
      CheckMenuItem cmp = new CheckMenuItem(integer.toString());
      mb.getOmessaProperty(integer).bindBidirectional(cmp.selectedProperty());
      
      menuOmesse.getItems().addAll(cmp);
    }
    
    
    
    
    
    
    contextMenu.getItems().addAll(menuReboti);
    contextMenu.getItems().addAll(menuOmesse);
    this.label.setContextMenu(contextMenu);
  }
  

  public void initialize(URL url, ResourceBundle rb) {

  }

  /**
   * @return the label
   */
  public Label getLabel() {
    return label;
  }

  /**
   * @param label the label to set
   */
  public void setLabel(Label label) {
    this.label = label;
  }

  /**
   * @return the node
   */
  public BorderPane getNode() {
    return node;
  }

  /**
   * @param node the node to set
   */
  public void setNode(BorderPane node) {
    this.node = node;
  }

  /**
   * @return the bottomBorderPain
   */
  public BorderPane getBottomBorderPain() {
    return bottomBorderPain;
  }

  /**
   * @param bottomBorderPain the bottomBorderPain to set
   */
  public void setBottomBorderPain(BorderPane bottomBorderPain) {
    this.bottomBorderPain = bottomBorderPain;
  }

  /**
   * @return the topBorderPain
   */
  public BorderPane getTopBorderPain() {
    return topBorderPain;
  }

  /**
   * @param topBorderPain the topBorderPain to set
   */
  public void setTopBorderPain(BorderPane topBorderPain) {
    this.topBorderPain = topBorderPain;
  }

}
