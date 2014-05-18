package player;

import csvsimulator.model.ModelCampana;
import java.util.Map;
import java.util.Optional;

/**
 * 
 * @author Marco Dalla Riva <marco.dallariva@outlook.com>
 */
public class PlayerCodaCampana {
    
    private int numeroBattuta;
    private ModelCampana campana;
    private double pausa;
    private String reboto = "";
    private Boolean omessa = false;

    public PlayerCodaCampana() {
    }

    public PlayerCodaCampana(int numeroBattuta, ModelCampana campana, double pausa, Map<String, Object> parameters) {
        this.numeroBattuta = numeroBattuta;
        this.campana = campana;
        this.pausa = pausa;
        
        
        
        if(parameters.containsKey("reboto")){
          this.reboto = (String)parameters.get("reboto");
        }
        
        if(parameters.containsKey("omessa")){
          this.omessa = (Boolean)parameters.get("omessa");
        }
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

  /**
   * @return the reboto
   */
  public String getReboto() {
    return reboto;
  }

  /**
   * @param reboto the reboto to set
   */
  public void setReboto(String reboto) {
    this.reboto = reboto;
  }

  /**
   * @return the omessa
   */
  public Boolean getOmessa() {
    return omessa;
  }

  /**
   * @param omessa the omessa to set
   */
  public void setOmessa(Boolean omessa) {
    this.omessa = omessa;
  }
    
    
}
