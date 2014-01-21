/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csvsimulator.model;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URI;
import javafx.scene.media.AudioClip;

/**
 *
 * @author lion
 */
public class ModelCampana implements Serializable {

    private static final long serialVersionUID = 1;

    private String nome;
    private int numero;
    transient private AudioClip audioClip;
    transient private String path;

    public ModelCampana() {
    }

    public ModelCampana(String nome, int numero) {
        this.nome = nome;
        this.numero = numero;
    }

    public ModelCampana(String nome, int numero, String path) {
        this.nome = nome;
        this.numero = numero;
        this.path = path;

        File f = new File(path);
        URI u = f.toURI();
        this.audioClip = new AudioClip(u.toString());
    }

    public void loadFromPath(String path) {
        File f = new File(path);
        URI u = f.toURI();
        audioClip = new AudioClip(u.toString());

        this.path = path;
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.writeLong(serialVersionUID);
        oos.writeObject(nome);
        oos.writeInt(numero);

        File file = new File(path);

        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];

        for (int readNum; (readNum = fis.read(buf)) != -1;) {
            bos.write(buf, 0, readNum); //no doubt here is 0
            //Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
            //System.out.println("read " + readNum + " bytes,");
        }

        buf = bos.toByteArray();

        oos.write(buf);

        System.out.println("Lunghezza salvataggio: " + buf.length);
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        long versione = (Long) ois.readLong();
        nome = (String) ois.readObject();
        numero = ois.readInt();
        byte[] buf = new byte[1024];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        for (int readNum; (readNum = ois.read(buf)) != -1;) {
            bos.write(buf, 0, readNum); //no doubt here is 0
        }

        buf = bos.toByteArray();
        
        System.err.println("Lunghezza lettura: " + buf.length);

        File temp = File.createTempFile("tempfile", ".wav");
        FileOutputStream fos = new FileOutputStream(temp.getAbsolutePath());
        fos.write(buf);
        fos.close();
        
        URI u = temp.toURI();
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
