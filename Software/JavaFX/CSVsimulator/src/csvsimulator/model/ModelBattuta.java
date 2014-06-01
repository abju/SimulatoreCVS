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
import java.util.Objects;
import java.util.TreeMap;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 *
 * @author lion
 */
public final class ModelBattuta implements Serializable {

  private static final long serialVersionUID = 1;

  private Map<Integer, DoubleProperty> listaCampane;
  private final Map<Integer, BooleanProperty> listaReboti;
  private final Map<Integer, BooleanProperty> listaOmesse;


  public final static Integer KEY_PAUSA = -1;

  public ModelBattuta() {
    listaCampane = new TreeMap(Collections.reverseOrder());
    listaReboti = new TreeMap(Collections.reverseOrder());
    listaOmesse = new TreeMap(Collections.reverseOrder());
  }

  public Double pushCampanaByNome(String nome, ModelConcerto concerto) {
    if (nome.equals("P")) {
      listaCampane.put(KEY_PAUSA, new SimpleDoubleProperty(0.0));
      return null;
    }

    ModelCampana campana = concerto.getCampanaByNome(nome);
    if (campana != null) {
      listaReboti.put(campana.getNumero(), new SimpleBooleanProperty(false));
      listaOmesse.put(campana.getNumero(), new SimpleBooleanProperty(false));
      DoubleProperty dp = listaCampane.putIfAbsent(campana.getNumero(), new SimpleDoubleProperty(0.0));
      return (dp == null) ? null : dp.getValue();
    }
    return 1.0;

  }

  public String getNomeBattuta(ModelConcerto concerto) {

    if (concerto.getListaCampane().size() == listaCampane.size()) {
      return "T";
    }

    String nomeBattuta = "";
    List<Integer> list = new ArrayList<>(listaCampane.keySet());
    Collections.reverse(list);

    for (Integer numeroCampana : list) {
      if (nomeBattuta.length() > 0) {
        nomeBattuta += "/";
      }

      nomeBattuta += concerto.getCampanaByNumero(numeroCampana).getNome();
    }
    return nomeBattuta;
  }

  public Double getTimeContrattempo(Integer numero_campana, Double tempoSuonata) {
    return tempoSuonata * listaCampane.get(numero_campana).getValue();
  }

  public BooleanProperty haveReboto(Integer numeroCampana) {
    if (Objects.equals(numeroCampana, KEY_PAUSA)) {
      return new SimpleBooleanProperty(false);
    }
    return this.listaReboti.get(numeroCampana);
  }

  public void play(ModelConcerto concerto) {
    listaCampane.keySet().stream().forEach((numeroCampana) -> {
      concerto.getCampanaByNumero(numeroCampana).play();
    });
  }

  public void setReboto(Integer numeroCampana, Boolean value) {
    this.listaReboti.get(numeroCampana).set(value);
    //this.listaReboti.put(numeroCampana, value);
  }

  public void setOmessa(Integer numeroCampana, Boolean value) {
    this.listaOmesse.get(numeroCampana).set(value);
  }

  public BooleanProperty getRebotoProperty(Integer numeroCampana) {
    if (Objects.equals(numeroCampana, KEY_PAUSA)) {
      return new SimpleBooleanProperty(false);
    }
    return this.listaReboti.get(numeroCampana);
  }

  public Integer countReboti(){
    Integer reboti = 0;
    for (Map.Entry<Integer, BooleanProperty> entry : listaReboti.entrySet()) {
      BooleanProperty booleanProperty = entry.getValue();
      if(booleanProperty.getValue()){
        reboti += 1;
      }
    }
    return reboti;
  }
  
  public BooleanProperty getOmessaProperty(Integer numeroCampana) {
    if (Objects.equals(numeroCampana, KEY_PAUSA)) {
      return new SimpleBooleanProperty(false);
    }
    return this.listaOmesse.get(numeroCampana);
  }
  
  

  public Boolean getReboto(Integer numeroCampana) {
    if (Objects.equals(numeroCampana, KEY_PAUSA)) {
      return false;
    }
    return this.listaReboti.get(numeroCampana).getValue();
  }

  public Boolean getOmessa(Integer numeroCampana) {
    if (Objects.equals(numeroCampana, KEY_PAUSA)) {
      return false;
    }
    return this.listaOmesse.get(numeroCampana).getValue();
  }
  
  public Integer countOmesse(){
    Integer reboti = 0;
    reboti = listaOmesse.entrySet().stream()
            .map((entry) -> entry.getValue())
            .filter((booleanProperty) -> (booleanProperty.getValue()))
            .map((_item) -> 1).reduce(reboti, Integer::sum);
    return reboti;
  }

  /**
   * @return the listaCampane
   */
  public Map<Integer, Double> getListaCampane() {
    final Map<Integer, Double> lista = new TreeMap(Collections.reverseOrder());
    listaCampane.forEach((a, b) -> lista.put(a, b.getValue()));
    return lista;
  }

  public void setContrattempo(Integer numeroCampana, Double value) {
    this.listaCampane.get(numeroCampana).set(value);
  }

  public Double getContrattempo(Integer numeroCampana) {
    return this.listaCampane.get(numeroCampana).getValue();
  }

}
