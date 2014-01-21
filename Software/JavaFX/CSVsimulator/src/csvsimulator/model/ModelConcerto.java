/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csvsimulator.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 *
 * @author lion
 */
public class ModelConcerto implements Serializable {

    private static final long serialVersionUID = 1;

    private String nomeConcerto;
    private List<ModelCampana> listaCampane;
    //se si vuole evitare di serializzare un attributo basta mettere transient
    //con readObject si può decidere come impostare le variaibili ingorate in scrittura
    //transient private List<ModelCampana> listaCampane;

    public ModelConcerto() {
        listaCampane = new ArrayList<>();
    }

    public ModelConcerto(String nomeConcerto, List<ModelCampana> listaCampane) {
        this.nomeConcerto = nomeConcerto;
        this.listaCampane = listaCampane;
    }

    public void pushCampana(ModelCampana campana) {
        listaCampane.add(campana);
        //TODO lanciare un'eccezione se esiste già una campana con lo stesso numero o con lo stesso nome
    }

    public ModelCampana getCampanaByNumero(int numeroCampana) {
        for (Iterator<ModelCampana> it = listaCampane.iterator(); it.hasNext();) {
            ModelCampana modelCampana = it.next();
            if (modelCampana.getNumero() == numeroCampana) {
                return modelCampana;
            }
        }
        return null;
    }

    public ModelCampana getCampanaByNome(String nome) {
        for (Iterator<ModelCampana> it = listaCampane.iterator(); it.hasNext();) {
            ModelCampana modelCampana = it.next();
            if (modelCampana.getNome().equals(nome)) {
                return modelCampana;
            }
        }
        return null;
    }

    public boolean isNomeValido(String nome) {
        for (Iterator<ModelCampana> it = listaCampane.iterator(); it.hasNext();) {
            ModelCampana modelCampana = it.next();
            if (modelCampana.getNome().equals(nome)) {
                return true;
            }
        }
        return false;
    }

    public void saveFileConcerto(final Window w, File dir) {
        FileChooser fileChooser = new FileChooser();
        if (dir != null) {
            fileChooser.setInitialDirectory(dir);
        }
        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Concerto campane sistema veronese (*.csvc)", "*.csvc");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(w);

        if (file != null) {
            SaveFile(file);
        }
    }

    private void SaveFile(File file) {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(this);
            oos.close();
        } catch (IOException e) {
            System.err.println(e.toString());
        }
        System.out.println("Salvataggio completato");
    }

    /**
     * @return the nomeConcerto
     */
    public String getNomeConcerto() {
        return nomeConcerto;
    }

    /**
     * @param nomeConcerto the nomeConcerto to set
     */
    public void setNomeConcerto(String nomeConcerto) {
        this.nomeConcerto = nomeConcerto;
    }

    /**
     * @return the listaCampane
     */
    public List<ModelCampana> getListaCampane() {
        return listaCampane;
    }

    /**
     * @param listaCampane the listaCampane to set
     */
    public void setListaCampane(List<ModelCampana> listaCampane) {
        this.listaCampane = listaCampane;
    }

}
