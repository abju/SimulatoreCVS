/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csvsimulator.spartito.controller;

import csvsimulator.model.ModelBattuta;
import csvsimulator.model.ModelSuonata;
import csvsimulator.spartito.controller.object.DraggableCircleSpartito;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 *
 * @author lion
 */
public class SpartitoPentagrammaController extends AnchorPane {

    private int ncampane = 6;
    private int battutePerRiga = 0;
    private ModelSuonata modelSuonata;

    private List<Group> battute;

    private Group rigaPentagramma;
    private Group rigaPentagrammaLinee;

    private SpartitoBaseController spartitoBaseController;
    /*
     COSTANTI PENTAGRAMMA
     */
    private static double dimensioneNote = 15;
    private static double distanzaTraRighe = 40;

    private double tempoSuonataUnit;

    public SpartitoPentagrammaController() {
        modelSuonata = new ModelSuonata();
        battute = new ArrayList<>();
        tempoSuonataUnit = 100.0;
        rigaPentagramma = new Group();
        rigaPentagrammaLinee = new Group();

        initRigaPentagramma();
    }

    private void initRigaPentagramma() {
        int nlinee = ncampane / 2;
        for (int i = 0; i < nlinee; i++) {
            Line line = new Line(0, dimensioneNote * i + dimensioneNote, 0, dimensioneNote * i + dimensioneNote);
            line.setStrokeWidth(2);
            line.setStroke(Color.BLACK);
            rigaPentagrammaLinee.getChildren().add(line);
        }
        rigaPentagramma.getChildren().add(rigaPentagrammaLinee);
        getChildren().add(rigaPentagramma);
        setTopAnchor(rigaPentagramma, distanzaTraRighe);
    }

    public Double getTimeByPosition(Double position) {
        Double time = position - tempoSuonataUnit / 2;
        time = time * getModelSuonata().getTempoSuonata() / tempoSuonataUnit;
        return time;
    }

    public void getContrattempoFromTime(Integer nBattuta, Integer nCampana, Double oldPos, Double newPos) {
        ModelBattuta mb = getModelSuonata().getListaBattute().get(nBattuta);
        Double contrattempo = mb.getTimeContrattempo(nCampana, getModelSuonata().getTempoSuonata());
        Double tempoAttuale = getTimeByPosition(oldPos);
        Double tempoCorretto = tempoAttuale - contrattempo;

        Double tempoFinale = getTimeByPosition(newPos);
        Double contrattempoAttuale = tempoFinale - tempoCorretto;

        //      Double contrattempo = newValue.doubleValue()*1000;
//                contrattempo = contrattempo / spartitoPentagrammaController.getModelSuonata().getTempoSuonata();
        mb.getListaCampane().put(nCampana, contrattempoAttuale / getModelSuonata().getTempoSuonata());
        //System.out.println(tempoAttuale + " -- " + tempoCorretto + " -- " + tempoFinale + " -- " + oldPos + " -- " + newPos + "--" + (contrattempoAttuale / modelSuonata.getTempoSuonata()));
    }

    public Map<String, Object> getPositionCampana(Double timeCampana, Integer numero_campana) {
        Map<String, Object> r = new HashMap<>();
        double x = tempoSuonataUnit * timeCampana / getModelSuonata().getTempoSuonata() + tempoSuonataUnit / 2;
        double y = dimensioneNote + (dimensioneNote * (ncampane / 2 - 1)) + dimensioneNote / 2 - numero_campana * dimensioneNote / 2;

        r.put("y", y);
        r.put("x", x);

        return r;
    }

    /*
     FUNZIONI PUBBLICHE
     */
    public void pushBattuta(final ModelBattuta mb, Label label) {
        final Integer numero_battuta = getModelSuonata().pushBattuta(mb);

        Group campaneBattuta = new Group();
        for (final Integer numeroCampana : mb.getListaCampane().keySet()) {

            Double timeCampana = getModelSuonata().getTimeCampana(numero_battuta, numeroCampana);
            Map<String, Object> position = getPositionCampana(timeCampana, numeroCampana);

            DraggableCircleSpartito c = new DraggableCircleSpartito(mb, numeroCampana, spartitoBaseController, this);
            c.setCenterX((Double) position.get("x"));
            c.setCenterY((Double) position.get("y"));
            campaneBattuta.getChildren().add(c);

        }

        rigaPentagramma.getChildren().add(campaneBattuta);
        battute.add(campaneBattuta);

        refreshPosizioneCampane();
    }

    public void popBattuta() {
        Integer index = modelSuonata.getListaBattute().size() - 1;
        if (index >= 0) {
            rigaPentagramma.getChildren().remove(battute.get(index));
            battute.remove(battute.get(index));
            modelSuonata.getListaBattute().remove(modelSuonata.getListaBattute().size() - 1);
            refreshPosizioneCampane();
        }
    }

    private void refreshLunghezzaLinee() {
        Map<String, Double> timeBattute = getModelSuonata().getTimeBattute();
        if (timeBattute.keySet().size() - 1 >= 0) {
            String lastKey = (String) timeBattute.keySet().toArray()[timeBattute.keySet().size() - 1];

            Double lunghezza = timeBattute.get(lastKey) / getModelSuonata().getTempoSuonata() * tempoSuonataUnit + tempoSuonataUnit * 2;
            for (Iterator<Node> it = rigaPentagrammaLinee.getChildren().iterator(); it.hasNext();) {
                Line node = (Line) it.next();
                node.setEndX(lunghezza);
            }
        }
    }

    public void refreshPosizioneCampane() {
        Map<String, Double> timeBattute = getModelSuonata().getTimeBattute();

        for (ListIterator<ModelBattuta> it = getModelSuonata().getListaBattute().listIterator(); it.hasNext();) {
            Integer numero_battuta = it.nextIndex();
            ModelBattuta modelBattuta = it.next();
            for (ListIterator<Integer> it1 = new ArrayList<>(modelBattuta.getListaCampane().keySet()).listIterator(); it1.hasNext();) {
                Integer numero_campana = it1.nextIndex();
                Integer id_campana = it1.next();

                Double timeCampana = timeBattute.get(numero_battuta + "||" + id_campana);

                Circle c = (Circle) battute.get(numero_battuta).getChildren().get(numero_campana);
                Map<String, Object> position = getPositionCampana(timeCampana, id_campana);
                c.setCenterX((Double) position.get("x"));
            }
        }

        refreshLunghezzaLinee();
    }

    public void setContrattempoCampana(Integer nBattuta, Integer nCampana, Double newValue) {
        ModelBattuta mb = getModelSuonata().getListaBattute().get(nBattuta);
        Integer campanaCode = (Integer) mb.getListaCampane().keySet().toArray()[nCampana];
        mb.getListaCampane().put(campanaCode, newValue);
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

    /**
     * @return the battutePerRiga
     */
    public int getBattutePerRiga() {
        return battutePerRiga;
    }

    /**
     * @param battutePerRiga the battutePerRiga to set
     */
    public void setBattutePerRiga(int battutePerRiga) {
        this.battutePerRiga = battutePerRiga;
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
}
