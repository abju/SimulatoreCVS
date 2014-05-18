/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csvsimulator.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author lion
 */
public final class ModelBattuta implements Serializable {

    private static final long serialVersionUID = 1;

    private Map<Integer, Double> listaCampane;
    private Map<Integer, Boolean> listaReboti;
    private Map<Integer, Boolean> listaOmesse;

    private Boolean defaultReboto = true;
    private Boolean defaultOmessa = false;
    
    public ModelBattuta() {
        listaCampane = new TreeMap(Collections.reverseOrder());
        listaReboti  = new TreeMap(Collections.reverseOrder());
        listaOmesse  = new TreeMap(Collections.reverseOrder());
    }

    public Double pushCampanaByNome(String nome, ModelConcerto concerto) {
        ModelCampana campana = concerto.getCampanaByNome(nome);
        if (campana != null) {
            listaReboti.put(campana.getNumero(), defaultReboto);
            return listaCampane.putIfAbsent(campana.getNumero(), 0.0);
        }
        return 1.0;

    }

    public String getNomeBattuta(ModelConcerto concerto) {
        String nomeBattuta = "";
        List<Integer> list = new ArrayList<Integer>(listaCampane.keySet());
        Collections.reverse(list);

        for (Integer numeroCampana : list) {
            if (nomeBattuta.length() > 0) {
                nomeBattuta += "/";
            }
            nomeBattuta += concerto.getCampanaByNumero(numeroCampana).getNome();
        }
        return nomeBattuta;
    }
    
    public Double getTimeContrattempo(Integer numero_campana, Double tempoSuonata){
        return tempoSuonata * listaCampane.get(numero_campana);
    }
    
    public Boolean haveReboto (Integer numeroCampana){
      return this.listaReboti.get(numeroCampana);
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
