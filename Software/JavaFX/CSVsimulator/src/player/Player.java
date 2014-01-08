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

import csvsimulator.model.ModelConcerto;
import java.util.ArrayList;
import java.util.Iterator;
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

    private PlayerUtils.runThreadCampane playSuonata;

    public Player() {
        coda = new ArrayList<>();
        isPlaying = false;
        isPause = false;
        posizioneAttuale = 0;
        playSuonata = null;
    }

    public Player(ArrayList<PlayerCodaCampana> _coda) {
        coda = _coda;
        isPlaying = false;
        isPause = false;
        posizioneAttuale = 0;
        playSuonata = null;
    }

    public void createCodaCamapana(Map<String, Double> listaCampane, ModelConcerto concerto) {
        coda.clear();
        for (Map.Entry<String, Double> entry : listaCampane.entrySet()) {
            String string = entry.getKey();
            Double double1 = entry.getValue();

            String[] battuta = string.split("\\|\\|");

            coda.add(new PlayerCodaCampana(Integer.parseInt(battuta[0]), concerto.getCampanaByNumero(Integer.parseInt(battuta[1])), double1));

        }
    }

    public void play() {
        isPlaying = true;
        playSuonata = new PlayerUtils.runThreadCampane("CodaCampane", coda, this);
        playSuonata.start();
    }

    public void pause() {
        isPause = !isPause;
        playSuonata.pause();
    }

    public void stop() {
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
}
