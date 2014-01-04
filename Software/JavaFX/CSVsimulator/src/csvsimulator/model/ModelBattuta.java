/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csvsimulator.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author lion
 */
public final class ModelBattuta implements Serializable {

    private static final long serialVersionUID = 1;

    private Map<Integer, Double> listaCampane;

    public ModelBattuta() {
        listaCampane = new TreeMap(Collections.reverseOrder());
    }

    public Double pushCampanaByNome(String nome, ModelConcerto concerto) {
        ModelCampana campana = concerto.getCampanaByNome(nome);
        if (campana != null) {
            return listaCampane.putIfAbsent(campana.getNumero(), 0.0);
        }
        return 1.0;

    }

    public String getNomeBattuta(ModelConcerto concerto) {
        String nomeBattuta = "";

        for (Integer numeroCampana : listaCampane.keySet()) {
            if (nomeBattuta.length() > 0) {
                nomeBattuta += "/";
            }
            nomeBattuta += concerto.getCampanaByNumero(numeroCampana).getNome();
        }
        return nomeBattuta;
    }
    
    public void play(ModelConcerto concerto){
        for (Integer numeroCampana : listaCampane.keySet()) {
            concerto.getCampanaByNumero(numeroCampana).play();
        }

    }

    /**
     * @return the listaCampane
     */
    public Map<Integer, Double> getListaCampane() {
        return listaCampane;
    }

    /**
     * @param listaCampane the listaCampane to set
     */
    public void setListaCampane(Map<Integer, Double> listaCampane) {
        this.listaCampane = listaCampane;
    }
}
