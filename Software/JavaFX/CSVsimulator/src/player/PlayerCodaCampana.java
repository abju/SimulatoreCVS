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

import csvsimulator.model.ModelCampana;

/**
 *
 * @author lion
 */
public class PlayerCodaCampana {
    
    private int numeroBattuta;
    private ModelCampana campana;
    private double pausa;

    public PlayerCodaCampana() {
    }

    public PlayerCodaCampana(int numeroBattuta, ModelCampana campana, double pausa) {
        this.numeroBattuta = numeroBattuta;
        this.campana = campana;
        this.pausa = pausa;
    }

    /**
     * @return the numeroBattuta
     */
    public int getNumeroBattuta() {
        return numeroBattuta;
    }

    /**
     * @param numeroBattuta the numeroBattuta to set
     */
    public void setNumeroBattuta(int numeroBattuta) {
        this.numeroBattuta = numeroBattuta;
    }

    /**
     * @return the campana
     */
    public ModelCampana getCampana() {
        return campana;
    }

    /**
     * @param campana the campana to set
     */
    public void setCampana(ModelCampana campana) {
        this.campana = campana;
    }

    /**
     * @return the pausa
     */
    public double getPausa() {
        return pausa;
    }

    /**
     * @param pausa the pausa to set
     */
    public void setPausa(double pausa) {
        this.pausa = pausa;
    }
    
    
}
