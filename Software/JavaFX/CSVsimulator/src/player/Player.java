/*
 * The MIT License
 *
 * Copyright 2013 lion.
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
package player;

import csvsimulator.model.ModelBattuta;
import csvsimulator.model.ModelConcerto;
import csvsimulator.spartito.controller.SpartitoBaseController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author lion
 */
public class Player {

    private List<PlayerCodaCampana> coda;
    private Boolean isPlaying;
    private Boolean isPause;
    private Integer posizioneAttuale;
    private SpartitoBaseController spartito;

    private PlayerUtils.runThreadCampane playSuonata;

    public Player() {
        coda = new ArrayList<>();
        isPlaying = false;
        isPause = false;
        posizioneAttuale = 0;
        playSuonata = null;
        this.spartito = null;
    }

    public Player(ArrayList<PlayerCodaCampana> _coda) {
        coda = _coda;
        isPlaying = false;
        isPause = false;
        posizioneAttuale = 0;
        playSuonata = null;
        this.spartito = null;
    }

    public void createCodaCamapana(Map<String, Double> listaCampane, ModelConcerto concerto) {
        coda.clear();
        
        Map<Integer, Boolean> reboti = new HashMap<>();
        
        for (Map.Entry<String, Double> entry : listaCampane.entrySet()) {
            String string = entry.getKey();
            Double double1 = entry.getValue();

            String[] battuta = string.split("\\|\\|");
            Integer numeroBattuta = Integer.parseInt(battuta[0]);
            Integer numeroCampana = Integer.parseInt(battuta[1]);
            
            reboti.putIfAbsent(numeroCampana, false);
            
            Map<String, Object> parameters = new HashMap<>();
            ModelBattuta mb = spartito.getModelSuonata().getListaBattute().get(numeroBattuta);

            if(mb.haveReboto(numeroCampana) && !reboti.get(numeroCampana)){
              parameters.put("reboto", "REB1");
              reboti.put(numeroCampana, true);
            } else if(mb.haveReboto(numeroCampana) && reboti.get(numeroCampana)) {
              parameters.put("reboto", "REB3");
              reboti.put(numeroCampana, true);
            } else {
              parameters.put("reboto", "REB2");
              reboti.put(numeroCampana, false);
            }
            
            coda.add(new PlayerCodaCampana(numeroBattuta, concerto.getCampanaByNumero(numeroCampana), double1, parameters));            
            
        }
    }
    
    public void nowPlaying(Integer i){
      //System.out.println("Ora sta suonando la battuta: " + i);
      this.spartito.setActiveBattuta(i);
    }
    
    public void timePlayer(Integer i){
      this.spartito.setTimePlayer(i);
    }

    public void play() {
        isPlaying = true;
        playSuonata = new PlayerUtils.runThreadCampane("CodaCampane", coda, this);
        this.spartito.removeAllActiveBattuta();
        playSuonata.start();
    }

    public void pause() {
        isPause = !isPause;
        playSuonata.pause();
    }

    public void stop() {
        this.spartito.removeAllActiveBattuta();
        playSuonata.stop();
    }

    public void stopListner() {
        isPlaying = false;
        playSuonata = null;
    }

    /**
     * @return the coda
     */
    public List<PlayerCodaCampana> getCoda() {
        return coda;
    }

    /**
     * @param coda the coda to set
     */
    public void setCoda(ArrayList<PlayerCodaCampana> coda) {
        this.coda = coda;
    }

    /**
     * @return the isPlaying
     */
    public Boolean getIsPlaying() {
        return isPlaying;
    }

    /**
     * @param isPlaying the isPlaying to set
     */
    public void setIsPlaying(Boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    /**
     * @return the isPause
     */
    public Boolean getIsPause() {
        return isPause;
    }

    /**
     * @param isPause the isPause to set
     */
    public void setIsPause(Boolean isPause) {
        this.isPause = isPause;
    }

    /**
     * @return the posizioneAttuale
     */
    public Integer getPosizioneAttuale() {
        return posizioneAttuale;
    }

    /**
     * @param posizioneAttuale the posizioneAttuale to set
     */
    public void setPosizioneAttuale(Integer posizioneAttuale) {
        this.posizioneAttuale = posizioneAttuale;
    }

  /**
   * @return the spartito
   */
  public SpartitoBaseController getSpartito() {
    return spartito;
  }

  /**
   * @param spartito the spartito to set
   */
  public void setSpartito(SpartitoBaseController spartito) {
    this.spartito = spartito;
  }
}
