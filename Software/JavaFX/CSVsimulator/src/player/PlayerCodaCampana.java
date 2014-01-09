package player;

import csvsimulator.model.ModelCampana;

/**
 * 
 * @author Marco Dalla Riva <marco.dallariva@outlook.com>
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
