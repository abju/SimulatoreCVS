/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csvsimulator.navbar.controller;

import csvsimulator.spartito.controller.SpartitoBaseController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
        System.out.println(caso);
        switch (caso) {
            case "player":
                playerGroup((SpartitoBaseController) obj);
                break;
        }
    }

    public void playerGroup(final SpartitoBaseController obj) {
        nome_gruppo.setText("Player");
        final Player player = new Player();
        player.setSpartito(obj);
        BottoneGenericoController btng = new BottoneGenericoController("Play", getClass().getResourceAsStream("/csvsimulator/navbar/images/player_play.png"));
        btng.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                if (!player.getIsPlaying()) {
                    player.createCodaCamapana(obj.getSpartitoPentagramma().getModelSuonata().getSuonata(), obj.getSpartitoPentagramma().getModelSuonata().getConcerto());
                    player.play();
                } else {
                    if (player.getIsPause()) {
                        player.pause();
                    }
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
