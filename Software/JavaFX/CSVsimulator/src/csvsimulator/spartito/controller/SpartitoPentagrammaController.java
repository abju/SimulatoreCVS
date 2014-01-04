/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csvsimulator.spartito.controller;

import csvsimulator.model.ModelBattuta;
import csvsimulator.model.ModelSuonata;
import java.math.BigDecimal;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
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

    private Circle createCircleCampana(double x, double y) {
        Circle c = new Circle(dimensioneNote / 2, Color.RED);
        c.setCenterX(x);
        c.setCenterY(y);
        c.setCursor(Cursor.HAND);
        return c;
    }

    private Map<String, Object> getPositionCampana(Double timeCampana, Integer numero_campana) {
        Map<String, Object> r = new HashMap<>();
        double x = tempoSuonataUnit * timeCampana / modelSuonata.getTempoSuonata() + tempoSuonataUnit / 2;
        double y = dimensioneNote + (dimensioneNote * (ncampane / 2 - 1)) + dimensioneNote / 2 - numero_campana * dimensioneNote / 2;

        r.put("y", y);
        r.put("x", x);

        return r;
    }

    /*
     FUNZIONI PUBBLICHE
     */
    public void pushBattuta(final ModelBattuta mb, Label label) {
        Integer numero_battuta = modelSuonata.pushBattuta(mb);

        Group campaneBattuta = new Group();
        for (final Integer numeroCampana : mb.getListaCampane().keySet()) {

            Double timeCampana = modelSuonata.getTimeCampana(numero_battuta, numeroCampana);
            Map<String, Object> position = getPositionCampana(timeCampana, numeroCampana);

            Circle c = createCircleCampana((Double) position.get("x"), (Double) position.get("y"));
            c.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    //Trovo l'indice della battuta con hasCode
                    int numero_battuta = SpartitoBaseController.getNumberBattutaFromModelBattuta(mb, modelSuonata.getListaBattute());
                    spartitoBaseController.getOptionBar().setUpOptionBattuta(numero_battuta, numeroCampana);
                }
            });

            campaneBattuta.getChildren().add(c);

        }

        rigaPentagramma.getChildren().add(campaneBattuta);
        battute.add(campaneBattuta);

        refreshPosizioneCampane();
    }

    private void refreshLunghezzaLinee() {
        Map<String, Double> timeBattute = modelSuonata.getTimeBattute();
        String lastKey = (String) timeBattute.keySet().toArray()[timeBattute.keySet().size() - 1];

        System.out.println("LastKey: " + lastKey);

        Double lunghezza = timeBattute.get(lastKey) / modelSuonata.getTempoSuonata() * tempoSuonataUnit + tempoSuonataUnit * 2;
        for (Iterator<Node> it = rigaPentagrammaLinee.getChildren().iterator(); it.hasNext();) {
            Line node = (Line) it.next();
            node.setEndX(lunghezza);
        }
    }

    public void refreshPosizioneCampane() {
        Map<String, Double> timeBattute = modelSuonata.getTimeBattute();

        for (ListIterator<ModelBattuta> it = modelSuonata.getListaBattute().listIterator(); it.hasNext();) {
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
        ModelBattuta mb = modelSuonata.getListaBattute().get(nBattuta);
        Integer campanaCode = (Integer) mb.getListaCampane().keySet().toArray()[nCampana];
        mb.getListaCampane().put(campanaCode, newValue);

        //System.out.println(nBattuta + " --- " + nCampana);
        Circle c = (Circle) battute.get(nBattuta).getChildren().get(nCampana);
    }

    public void popBattuta() {

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
