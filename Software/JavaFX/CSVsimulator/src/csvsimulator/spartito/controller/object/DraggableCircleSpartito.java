/*
 * The MIT License
 *
 * Copyright 2014 lion.
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
package csvsimulator.spartito.controller.object;

import csvsimulator.model.ModelBattuta;
import csvsimulator.spartito.controller.SpartitoBaseController;
import csvsimulator.spartito.controller.SpartitoPentagrammaController;
import java.util.Map;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author lion
 */
public class DraggableCircleSpartito extends Circle {
    // X e Y del mouse
    private double mouseX = 0;
    private double mouseY = 0;
    
    private double startDragX = 0;
    
    private Integer numero_battuta;

    private ModelBattuta mb;
    private final Integer numero_campana;
    private final SpartitoBaseController spartitoBC;
    private final SpartitoPentagrammaController spartitoPC;
    

    public DraggableCircleSpartito(ModelBattuta _mb, Integer _numero_campana, SpartitoBaseController _spartitoBaseController, SpartitoPentagrammaController _spartitoPentagrammaController) {
        super(7.5, Color.ROYALBLUE);

        this.mb = _mb;
        this.numero_campana = _numero_campana;
        this.spartitoBC = _spartitoBaseController;
        this.spartitoPC = _spartitoPentagrammaController;
        
        
        setCursor(Cursor.HAND);

        onMouseClickedProperty().set(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                //Trovo l'indice della battuta con hasCode
                int numero_battuta = spartitoPC.getModelSuonata().getNumberBattutaFromModelBattuta(mb);
                spartitoBC.getOptionBar().setUpOptionBattuta(numero_battuta, numero_campana);
            }
        });

        onMousePressedProperty().set(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                mouseX = t.getSceneX();
                mouseY = t.getSceneY();
                
                numero_battuta = spartitoPC.getModelSuonata().getNumberBattutaFromModelBattuta(mb);
                startDragX = getCenterX();
            }
        });

        onMouseDraggedProperty().set(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                Double posNote = t.getX();

                Double contrattempo = mb.getTimeContrattempo(numero_campana, spartitoPC.getModelSuonata().getTempoSuonata());
                Double tempoAttuale = spartitoPC.getTimeByPosition(getCenterX());
                Double tempoCorretto = tempoAttuale - contrattempo;

                Double tempoFinale = spartitoPC.getTimeByPosition(t.getX());
                Double contrattempoAttuale = (tempoFinale - tempoCorretto) / 1000;
                
                if (contrattempoAttuale > spartitoPC.getModelSuonata().getMaxContrattempoSec(numero_battuta, numero_campana)) {
                    Map<String, Object> pos = spartitoPC.getPositionCampana(tempoCorretto + spartitoPC.getModelSuonata().getMaxContrattempoSec(numero_battuta, numero_campana) * 1000, numero_campana);
                    posNote = (Double) pos.get("x");
                }
                if (contrattempoAttuale < -spartitoPC.getModelSuonata().getMinContrattempoSec(numero_battuta, numero_campana)) {
                    Map<String, Object> pos = spartitoPC.getPositionCampana(tempoCorretto - spartitoPC.getModelSuonata().getMinContrattempoSec(numero_battuta, numero_campana) * 1000, numero_campana);
                    posNote = (Double) pos.get("x");
                }

                setCenterX(posNote);
                spartitoPC.getContrattempoFromTime(numero_battuta, numero_campana, startDragX, posNote);
                startDragX = posNote;

                int numero_battuta = spartitoPC.getModelSuonata().getNumberBattutaFromModelBattuta(mb);
                spartitoBC.getOptionBar().setUpOptionBattuta(numero_battuta, numero_campana);

            }
        });
    }

}
