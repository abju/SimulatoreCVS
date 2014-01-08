/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package csvsimulator.model;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import player.PlayerThreadSound;
import player.PlayerUtils;

/**
 *
 * @author lion
 */
public class ModelCampana implements Serializable{
    private static final long serialVersionUID = 1;
    
    private String nome;
    private int numero;
    private InputStream streamAudio;
    private String pathFileAudio;

    //mp3
    public ModelCampana() {
        pathFileAudio = null;
    }

    public ModelCampana(String nome, int numero) {
        this.nome = nome;
        this.numero = numero;
    }
    
    public void loadFromPath(String path){
        pathFileAudio = path;
        try {
            FileInputStream fis3 = new FileInputStream(pathFileAudio);
            BufferedInputStream bis = new BufferedInputStream(fis3);
            streamAudio = bis;
        } catch (FileNotFoundException e) {
            System.err.println(e.toString());
        }
    }
    
    public void play() {
        ArrayList<InputStream> stream = PlayerUtils.cloneInputStream(streamAudio);
        if (stream != null) {
            streamAudio = stream.get(0);
            PlayerThreadSound sound = new PlayerThreadSound("Sestina", stream.get(1));
            sound.start();
        }
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

    /**
     * @return the streamAudio
     */
    public InputStream getStreamAudio() {
        return streamAudio;
    }

    /**
     * @param streamAudio the streamAudio to set
     */
    public void setStreamAudio(InputStream streamAudio) {
        this.streamAudio = streamAudio;
    }
    
    
}
