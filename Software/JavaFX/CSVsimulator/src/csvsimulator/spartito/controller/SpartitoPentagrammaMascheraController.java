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

import csvsimulator.model.ModelCampana;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

/**
 * FXML Controller class
 *
 * @author Marco Dalla Riva <marco.dallariva@outlook.com>
 */
public class SpartitoPentagrammaMascheraController extends StackPane implements Initializable {
  //@FXML
  //private BorderPane mainSpartitoPentagrammaMaschera;
  @FXML
  private ScrollPane scollSpartito;
  @FXML
  private AnchorPane legendBar;
  @FXML
  private AnchorPane timeBar;
  @FXML
  private BorderPane bpPentagramma;
  @FXML
  private Group groupArrow;
  
  
  
  private SpartitoBaseController spartitoBaseController;
  private SpartitoPentagrammaController spartitoPentagramma;

  
  public SpartitoPentagrammaMascheraController(){
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/csvsimulator/spartito/view/spartitoPentagrammaMaschera.fxml"));
    fxmlLoader.setRoot(this);
    fxmlLoader.setController(this);

    try {
        fxmlLoader.load();
    } catch (IOException exception) {
        throw new RuntimeException(exception);
    }
    
  }
  
  private void init(){
    //spartitoBaseController.getModelConcerto().getListaCampane().size();
    int ncampane = spartitoBaseController.getModelConcerto().getListaCampane().size();
    for (int i = 0; i < ncampane; i++) {
      String t = ((ModelCampana)spartitoBaseController.getModelConcerto().getListaCampane().get(i)).getNome();
      Label l = new Label(t);
      l.setFont(new Font("Arial", 11));
      double y = spartitoPentagramma.getDimensioneNote() + (spartitoPentagramma.getDimensioneNote() * (ncampane / 2 - 1)) + spartitoPentagramma.getDimensioneNote() - i * spartitoPentagramma.getDimensioneNote() / 2;
      if (ncampane % 2 == 0) {
        y -= spartitoPentagramma.getDimensioneNote() / 2;
      }
      AnchorPane.setTopAnchor(l, (y + spartitoPentagramma.getDistanzaTraRighe()) - 10/2);
      AnchorPane.setRightAnchor(l, ((i % 2) == 0) ? 24.0 : 2.0);
      legendBar.getChildren().add(l);
      
      Map<String, Object> position = spartitoPentagramma.getPositionCampana(0.0, 0);
      groupArrow.setLayoutX((Double) position.get("x"));
    }
    
    
  }
  
  public void setArrowPosition(Integer i){
    Map<String, Object> position = spartitoPentagramma.getPositionCampana((double)i, 0);
    groupArrow.setLayoutX((Double) position.get("x"));
  }
  
  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // TODO
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
    this.bpPentagramma.setCenter(this.spartitoPentagramma);
    this.init();
  }
  
}
