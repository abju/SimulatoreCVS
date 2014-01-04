/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csvsimulator.model;

import global.GlobalUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author lion
 */
public class ModelSuonata implements Serializable {

    private static final long serialVersionUID = 1;

    private ModelConcerto concerto;
    private int numeroCampane;
    private List<ModelBattuta> listaBattute;
    private double tempoSuonata;
    private double tempoRitorno;

    public ModelSuonata() {
        listaBattute = new ArrayList<>();
        tempoSuonata = 1200;
        tempoRitorno = tempoSuonata / 2;
    }

    public Integer pushBattuta(ModelBattuta b) {
        listaBattute.add(b);
        return listaBattute.indexOf(b);
    }

    public Map<String, Double> getSuonata() {
        Map<String, Double> battute = getTimeBattute();
        Map<String, Double> offsetBattute = new LinkedHashMap<>();
       
        
        double lastValue = 0;
        for (Map.Entry<String, Double> entry : battute.entrySet()) {
            String string = entry.getKey();
            Double double1 = entry.getValue();
            System.out.println(string + " -- " + (double1 - lastValue));

            offsetBattute.put(string, (double1 - lastValue));
            lastValue = double1;
        }

        return offsetBattute;

    }
    
    public Map<String, Double> getTimeBattute(){
        return getTimeBattute(true);
    }
    
    public Map<String, Double> getTimeBattute(Boolean contrattempi){
        Map<String, Double> battute = new TreeMap<>();
        double lastValue = 0;
        ArrayList<Integer> prevBattuta = new ArrayList<>();
        
        for (ListIterator<ModelBattuta> it = listaBattute.listIterator(); it.hasNext();) {
            int index = it.nextIndex();
            ModelBattuta modelBattuta = it.next();

            //Aumentare lastValue se è un ritorno di campana
            double val = lastValue;
            prevBattuta.retainAll(modelBattuta.getListaCampane().keySet());

            double mainVal = val;
            Integer mainCampana = -1;
            boolean isRitorno = (prevBattuta.size() > 0);
            
            prevBattuta = new ArrayList<>();
            for (Integer numeroCampana : modelBattuta.getListaCampane().keySet()) {
                
                //val è il tempo dell'dell'ultima campana più la giusta pausa
                //la moltiplicazione aggiunge il contrattempo
                //TODO deve essere in base al tempo suonata o al tempo suonata + tempo ritorno?
                double intval = val;
                        
                if(contrattempi)
                    intval += tempoSuonata * modelBattuta.getListaCampane().get(numeroCampana);
                
                if(isRitorno)
                    intval += tempoRitorno;
                
                //System.out.println(modelBattuta.getListaCampane().get(numeroCampana) + " --- " + intval + " --- " + val);
                battute.put(index + "||" + numeroCampana, intval);
                
                if (numeroCampana > mainCampana) {
                    mainCampana = numeroCampana;
                    mainVal = intval;
                }
                
                //Aggiorno la lista per controllare se è un ritorno
                prevBattuta.add(numeroCampana);
                
            }

            lastValue = mainVal + tempoSuonata;
        }
        
        battute = GlobalUtils.sortByValue(new TreeMap<>(battute));
        
        return battute;
    }
    
    /**
     * Restituisce quando una certa campana di una certa battuta viene eseguita
     * @param nBattuta
     * @param nCampana
     * @return 
     */
    public Double getTimeCampana(Integer numero_battuta, Integer numero_campana){
        Map<String, Double> battute = getTimeBattute();
        return battute.get(numero_battuta + "||" + numero_campana);
    }
    
    /**
     * 
     * @param numero_battuta
     * @param numero_campana
     * @return 
     */
    public Double getMinContrattempoSec(Integer numero_battuta, Integer numero_campana){
        //Se è la prima battuta non può anticipare al messimo si fa ritardare la seconda battuta per far sembrare un anticipo
        if(numero_battuta == 0){
            return 0.0;
        }
        
        //poi se deve fare un ritorno non può anticipare troppo senza fare ribattute o cali, ma può strozzare quindi gli si può annullare parte del tempo
        //per ora tolgo al massimo il tempo suonata
        Set<Integer> campanePrecendi = new LinkedHashSet<>(listaBattute.get(numero_battuta-1).getListaCampane().keySet());
        boolean mioRitorno = campanePrecendi.contains(numero_campana);
        if(mioRitorno){
            return tempoSuonata/1000;
        }
        
        //Nel caso di un doppio con ritorno e la campana selezionata non è quella implicata nel ritorno può anticipare di più
        Set<Integer> campaneBattua = new LinkedHashSet<>(listaBattute.get(numero_battuta).getListaCampane().keySet());
        campaneBattua.retainAll(campanePrecendi);
        boolean isRitorno = (campaneBattua.size() > 0);
        if(isRitorno && !mioRitorno){
            return (tempoSuonata+tempoRitorno)/1000;
        }        
        
        return tempoSuonata/1000;
    }
    
    //restituire il minimo dei massimi
    public Double getMaxContrattempoSec(Integer numero_battuta, Integer numero_campana){
        return tempoSuonata/1000;
    }

    /**
     * @return the concerto
     */
    public ModelConcerto getConcerto() {
        return concerto;
    }

    /**
     * @param concerto the concerto to set
     */
    public void setConcerto(ModelConcerto concerto) {
        this.concerto = concerto;
    }

    /**
     * @return the numeroCampane
     */
    public int getNumeroCampane() {
        return numeroCampane;
    }

    /**
     * @param numeroCampane the numeroCampane to set
     */
    public void setNumeroCampane(int numeroCampane) {
        this.numeroCampane = numeroCampane;
    }

    /**
     * @return the listaBattute
     */
    public List<ModelBattuta> getListaBattute() {
        return listaBattute;
    }

    /**
     * @param listaBattute the listaBattute to set
     */
    public void setListaBattute(List<ModelBattuta> listaBattute) {
        this.listaBattute = listaBattute;
    }

    /**
     * @return the tempoRitorno
     */
    public double getTempoRitorno() {
        return tempoRitorno;
    }

    /**
     * @param tempoRitorno the tempoRitorno to set
     */
    public void setTempoRitorno(double tempoRitorno) {
        this.tempoRitorno = tempoRitorno;
    }

    /**
     * @return the tempoSuonata
     */
    public double getTempoSuonata() {
        return tempoSuonata;
    }

    /**
     * @param tempoSuonata the tempoSuonata to set
     */
    public void setTempoSuonata(double tempoSuonata) {
        this.tempoSuonata = tempoSuonata;
    }

}
