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

package csvsimulator;

import csvsimulator.campanile.controller.CampanileNuovoController;
import csvsimulator.navbar.controller.NavbarBaseController;
import csvsimulator.spartito.controller.SpartitoBaseController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author Marco Dalla Riva <marco.dallariva@outlook.com>
 */
public class MainController extends BorderPane implements Initializable {
    @FXML
    private BorderPane main;
    @FXML
    private MenuItem file_nuovo_concerto;
    @FXML
    private MenuItem file_nuova_suonata;
    @FXML
    private MenuItem file_salva;
    @FXML
    private MenuItem file_salva_con_nome;
    @FXML
    private MenuItem help_about;

    
    public MainController(){
        init();
        //CampanileNuovoController cnc = new CampanileNuovoController();
        //main.setCenter(cnc);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    private void init(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/csvsimulator/main.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    /* FXML ACTION */
    @FXML
    private void nuovoConcertoAction(ActionEvent event) {
        CampanileNuovoController cnc = new CampanileNuovoController();
        main.setCenter(cnc);
    }

    @FXML
    private void nuovaSuonataAction(ActionEvent event) {
        SpartitoBaseController sbc = new SpartitoBaseController();
        main.setCenter(sbc);
        sbc.setModelConcerto(sbc.loadModelConcerto());
    }
    
    
}
