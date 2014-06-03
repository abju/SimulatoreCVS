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
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.layout.BorderPane;

import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.MouseEvent;

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

  private SpartitoBaseController spc;

  public SpartitoLabelBattutaController(SpartitoBaseController spc) {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/csvsimulator/spartito/view/spartitoLabelBattuta.fxml"));
    fxmlLoader.setRoot(this);
    fxmlLoader.setController(this);

    this.spc = spc;

    try {
      fxmlLoader.load();
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }

  }

  public void setContexMenu(ModelBattuta mb) {
    final ContextMenu contextMenu = new ContextMenu();
    final Map<Integer, Double> listaCampane = mb.getListaCampane();

    Menu menuReboti = new Menu("Ribattute");
    MenuItem allReboti = new MenuItem("Inverti Selezione");
    allReboti.setOnAction((ActionEvent t) -> {
      listaCampane.entrySet().stream().map((entry) -> entry.getKey()).forEach((integer) -> {
        mb.setReboto(integer, !mb.getReboto(integer));
      });
    });

    Menu menuOmesse = new Menu("Omesse");
    MenuItem allOmesse = new MenuItem("Inverti Selezione");
    allOmesse.setOnAction((ActionEvent t) -> {
      listaCampane.entrySet().stream().map((entry) -> entry.getKey()).forEach((integer) -> {
        mb.setOmessa(integer, !mb.getOmessa(integer));
      });
    });

    menuReboti.getItems().addAll(allReboti, new SeparatorMenuItem());
    menuOmesse.getItems().addAll(allOmesse, new SeparatorMenuItem());

    for (Map.Entry<Integer, Double> entry : listaCampane.entrySet()) {
      Integer integer = entry.getKey();

      CheckMenuItem cmp = new CheckMenuItem(spc.getModelConcerto().getCampanaByNumero(integer).getNome());
      mb.getRebotoProperty(integer).bindBidirectional(cmp.selectedProperty());
      //Aggiungo o meno il bordo in caso di Reboto
      mb.getRebotoProperty(integer).addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
        if (t1) {
          if (!topBorderPain.getStyleClass().toString().contains("haveReboto")) {
            topBorderPain.getStyleClass().add("haveReboto");
          }
        } else {
          if (mb.countReboti() == 0) {
            topBorderPain.getStyleClass().remove("haveReboto");
          }
        }
      });
      menuReboti.getItems().addAll(cmp);

      cmp = new CheckMenuItem(spc.getModelConcerto().getCampanaByNumero(integer).getNome());
      mb.getOmessaProperty(integer).bindBidirectional(cmp.selectedProperty());
      mb.getOmessaProperty(integer).addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
        if (t1) {
          if (!bottomBorderPain.getStyleClass().toString().contains("haveOmessa")) {
            bottomBorderPain.getStyleClass().add("haveOmessa");
          }
        } else {
          if (mb.countOmesse() == 0) {
            bottomBorderPain.getStyleClass().remove("haveOmessa");
          }
        }
      });
      menuOmesse.getItems().addAll(cmp);
    }

    
    MenuItem aggiungi_prima = new MenuItem("Aggiungi Battute Prima");
    aggiungi_prima.setOnAction((ActionEvent t) -> {
      Integer numero_battuta = spc.getModelSuonata().getNumberBattutaFromModelBattuta(mb);
      spc.moveInputTextNuovaBattuta(numero_battuta);
    });
    
    MenuItem aggiungi_dopo = new MenuItem("Aggiungi Battute Dopo");
    aggiungi_dopo.setOnAction((ActionEvent t) -> {
      Integer numero_battuta = spc.getModelSuonata().getNumberBattutaFromModelBattuta(mb);
      spc.moveInputTextNuovaBattuta(numero_battuta+1);
    });
    
    MenuItem eliminia = new MenuItem("Elimina Battuta");
    eliminia.setOnAction((ActionEvent t) -> {
      Integer numero_battuta = spc.getModelSuonata().getNumberBattutaFromModelBattuta(mb);
      spc.eliminaBattua(numero_battuta);
    });
    
    
    contextMenu.getItems()
            .addAll(menuReboti,menuOmesse, new SeparatorMenuItem(), aggiungi_prima,aggiungi_dopo,eliminia);

    this.label.setContextMenu(contextMenu);

    this.setOnMousePressed((MouseEvent t) -> {
      Integer numero_battuta = spc.getModelSuonata().getNumberBattutaFromModelBattuta(mb);
      spc.getOptionBar().setUpOptionBattuta(numero_battuta);
    });
  }

  @Override
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
