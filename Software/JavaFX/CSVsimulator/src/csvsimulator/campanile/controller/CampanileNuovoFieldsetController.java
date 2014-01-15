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
package csvsimulator.campanile.controller;

import csvsimulator.model.ModelCampana;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author Marco Dalla Riva <marco.dallariva@outlook.com>
 */
public class CampanileNuovoFieldsetController extends HBox implements Initializable {

    @FXML
    private HBox main;
    @FXML
    private TextField tfNomeCampana;
    @FXML
    private Button btnBrowse;
    @FXML
    private Button btnPreview;
    @FXML
    private Button btnElimina;
    @FXML
    private Label lblNumeroCampana;
    @FXML
    private Label lblPathFile;

    private ModelCampana mb;
    private Integer numero;
    private File file;
    private CampanileNuovoController parentController;

    public CampanileNuovoFieldsetController() {
        init();
    }

    public CampanileNuovoFieldsetController(CampanileNuovoController parentController, Integer numero) {
        init();
        lblNumeroCampana.setText(numero.toString());
        this.numero = numero;
        this.parentController = parentController;
        this.file = null;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    private void init() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/csvsimulator/campanile/view/campanileNuovoFieldset.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        mb = new ModelCampana();
    }

    @FXML
    private void browseAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Seleziona il file audio della campana");

        if (file != null) {
            File existDirectory = file.getParentFile();
            fileChooser.setInitialDirectory(existDirectory);
        } else {
            fileChooser.setInitialDirectory(
                    new File(System.getProperty("user.home"))
            );
        }
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("File Audio", "*.wav", "*.mp3"),
                new FileChooser.ExtensionFilter("WAV", "*.wav"),
                new FileChooser.ExtensionFilter("MP3", "*.mp3")
        );

        //Show save file dialog
        File file = fileChooser.showOpenDialog(((Node) this).getScene().getWindow());
        if (file != null) {
            lblPathFile.setText(file.getAbsolutePath());
            this.file = file;
        } else {
            lblPathFile.setText("");
            this.file = null;
        }
    }

    @FXML
    private void previewAction(ActionEvent event) {
        if (file != null) {
            getMb().play();
        }
    }

    @FXML
    private void eliminaAction(ActionEvent event) {
        parentController.rimuoviCampanaByObject(this);
    }

    /**
     * @return the mb
     */
    public ModelCampana getMb() {
        if (tfNomeCampana.getText() != "") {
            mb.setNome(tfNomeCampana.getText());
        } else {
            mb.setNome("");
        }

        mb.setNumero(numero);
        mb.loadFromPath(file.getAbsolutePath());
        return mb;
    }

    private boolean validateNomeCampana() {
        return true;
    }
}
