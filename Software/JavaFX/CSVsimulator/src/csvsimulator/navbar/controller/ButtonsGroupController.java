/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csvsimulator.navbar.controller;

import csvsimulator.spartito.controller.SpartitoBaseController;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import jfxtras.labs.scene.control.BigDecimalField;
import org.controlsfx.control.ButtonBar;
import org.controlsfx.control.action.AbstractAction;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;
import player.Player;

/**
 * FXML Controller class
 *
 * @author lion
 */
public class ButtonsGroupController extends BorderPane implements Initializable {

  @FXML
  private Label nome_gruppo;
  @FXML
  private BorderPane mainButtonGroup;
  @FXML
  private HBox buttons_space;

  public ButtonsGroupController(String caso, Object obj) {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/csvsimulator/navbar/view/buttonsGroup.fxml"));
    fxmlLoader.setRoot(this);
    fxmlLoader.setController(this);

    try {
      fxmlLoader.load();
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }

    switch (caso) {
      case "player":
        playerGroup((SpartitoBaseController) obj);
        break;
      case "save":
        saveGroup((SpartitoBaseController) obj);
        break;
    }
  }

  public final void saveGroup(final SpartitoBaseController obj) {
    nome_gruppo.setText("Salva");

    BottoneGenericoController btng = new BottoneGenericoController("Esporta su Excel", getClass().getResourceAsStream("/csvsimulator/navbar/images/save_on_excel.png"));
    btng.setOnMouseClicked((MouseEvent t) -> {

      BigDecimalField spinnerNumeroColonne = new BigDecimalField(BigDecimal.TEN);
      spinnerNumeroColonne.setMinValue(BigDecimal.ONE);
      spinnerNumeroColonne.setMaxValue(new BigDecimal(50));

      final Action actionLogin = new AbstractAction("Salva") {
        {
          ButtonBar.setType(this, ButtonBar.ButtonType.OK_DONE);
        }
        @Override
        public void execute(ActionEvent ae) {
          Dialog dlg = (Dialog) ae.getSource();
          ((Node) obj).getScene().getWindow();
          obj.getModelSuonata().saveOnExcel(Integer.parseInt(spinnerNumeroColonne.getText()), ((Node) obj).getScene().getWindow());
          dlg.hide();
        }
      };

      Dialog dlg = new Dialog(null, "Salva su file Excel", true);
      final GridPane content = new GridPane();
      content.setHgap(10);
      content.setVgap(10);

      content.add(new Label("Seleziona il numero di colonne"), 0, 0);
      content.add(spinnerNumeroColonne, 0, 1);
      GridPane.setHgrow(spinnerNumeroColonne, Priority.ALWAYS);

      dlg.setResizable(false);
      dlg.setContent(content);
      dlg.getActions().addAll(actionLogin, Dialog.Actions.CANCEL);
      dlg.show();

      //System.out.println("response: " + response);
    });
    buttons_space.getChildren().add(btng);
  }

  public final void playerGroup(final SpartitoBaseController obj) {
    nome_gruppo.setText("Player");
    final Player player = new Player();
    player.setSpartito(obj);
    BottoneGenericoController btng = new BottoneGenericoController("Play", getClass().getResourceAsStream("/csvsimulator/navbar/images/player_play.png"));
    btng.setOnMouseClicked((MouseEvent t) -> {
      obj.moveInputTextNuovaBattuta(obj.getModelSuonata().getListaBattute().size());
      if (!player.getIsPlaying()) {
        obj.getModelSuonata().getTotalTime();
        player.createCodaCamapana(obj.getModelSuonata().getSuonata(), obj.getModelSuonata().getConcerto());
        player.play();
      } else {
        if (player.getIsPause()) {
          player.pause();
        }
      }
    });
    buttons_space.getChildren().add(btng);

    btng = new BottoneGenericoController("Pausa", getClass().getResourceAsStream("/csvsimulator/navbar/images/player_pause.png"));
    btng.setOnMouseClicked(new EventHandler<MouseEvent>() {

      @Override
      public void handle(MouseEvent t) {
        if (player.getIsPlaying()) {
          player.pause();
        }
      }
    });
    buttons_space.getChildren().add(btng);

    btng = new BottoneGenericoController("Stop", getClass().getResourceAsStream("/csvsimulator/navbar/images/player_stop.png"));
    btng.setOnMouseClicked(new EventHandler<MouseEvent>() {

      @Override
      public void handle(MouseEvent t) {
        if (player.getIsPlaying()) {
          player.stop();
        }
      }
    });
    buttons_space.getChildren().add(btng);
  }

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    /*BottoneGenericoController btng1 = new BottoneGenericoController();
     this.getChildren().add(btng1);
     btng1 = new BottoneGenericoController();
     this.getChildren().add(btng1);
     btng1 = new BottoneGenericoController();
     this.getChildren().add(btng1);*/
  }

  /**
   * @return the nome_gruppo
   */
  public Label getNome_gruppo() {
    return nome_gruppo;
  }

  /**
   * @param nome_gruppo the nome_gruppo to set
   */
  public void setNome_gruppo(Label nome_gruppo) {
    this.nome_gruppo = nome_gruppo;
  }

  /**
   * @return the mainButtonGroup
   */
  public BorderPane getMainButtonGroup() {
    return mainButtonGroup;
  }

  /**
   * @param mainButtonGroup the mainButtonGroup to set
   */
  public void setMainButtonGroup(BorderPane mainButtonGroup) {
    this.mainButtonGroup = mainButtonGroup;
  }

}
