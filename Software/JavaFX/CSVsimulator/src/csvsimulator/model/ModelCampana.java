/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package csvsimulator.model;

import java.io.File;
import java.io.Serializable;
import java.net.URI;
import javafx.scene.media.AudioClip;

/**
 *
 * @author lion
 */
public class ModelCampana implements Serializable{
    private static final long serialVersionUID = 1;
    
    private String nome;
    private int numero;
    private AudioClip audioClip;

    
    public ModelCampana() {
    }

    public ModelCampana(String nome, int numero) {
        this.nome = nome;
        this.numero = numero;
    }
    
    public ModelCampana(String nome, int numero, String path) {
        this.nome = nome;
        this.numero = numero;
        
        File f = new File(path);
        URI u = f.toURI();
        this.audioClip = new AudioClip(u.toString());
    }
    
    public void loadFromPath(String path){
        File f = new File(path);
        URI u = f.toURI();
        audioClip = new AudioClip(u.toString());
    }
    
    public void play() {
        audioClip.play();
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the numero
     */
    public int getNumero() {
        return numero;
    }

    /**
     * @param numero the numero to set
     */
    public void setNumero(int numero) {
        this.numero = numero;
    }   
    
}
