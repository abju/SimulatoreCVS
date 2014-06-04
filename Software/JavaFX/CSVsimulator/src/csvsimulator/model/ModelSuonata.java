/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csvsimulator.model;

import global.GlobalUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author lion
 */
public class ModelSuonata implements Serializable {

  private static final long serialVersionUID = 1;

  private ModelConcerto concerto;
  private List<ModelBattuta> listaBattute;
  private double tempoSuonata;
  private double tempoRitorno;

  public ModelSuonata() {
    listaBattute = new ArrayList<>();
    tempoSuonata = 1200;
    tempoRitorno = tempoSuonata;
  }

  public void addBattuta(ModelBattuta b, Integer index) {
    listaBattute.add(index, b);
  }

  public void removeBattuta(int index) {
    listaBattute.remove(index);
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
      String chiave = entry.getKey();
      Double double1 = entry.getValue();

      offsetBattute.put(chiave, (double1 - lastValue));
      lastValue = double1;
    }

    return offsetBattute;

  }

  public double getTotalTime() {
    Map<String, Double> suonata = this.getSuonata();
    double total = 0.0;
    for (Double val : suonata.values()) {
      total += val;
    }
    return total;
  }

  public Map<String, Double> getTimeBattute() {
    return getTimeBattute(true);
  }

  public Map<String, Double> getTimeBattute(Boolean contrattempi) {
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
      Integer mainCampana = -1000;
      boolean isRitorno = (prevBattuta.size() > 0);

      prevBattuta = new ArrayList<>();
      for (Integer numeroCampana : modelBattuta.getListaCampane().keySet()) {

        //val è il tempo dell'dell'ultima campana più la giusta pausa
        //la moltiplicazione aggiunge il contrattempo
        double intval = val;

        if (contrattempi) {
          intval += modelBattuta.getTimeContrattempo(numeroCampana, tempoSuonata);
        }

        if (isRitorno) {
          intval += getTempoRitorno();
        }

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
   *
   * @param nBattuta
   * @param nCampana
   * @return
   */
  public Double getTimeCampana(Integer numero_battuta, Integer numero_campana) {
    Map<String, Double> battute = getTimeBattute();
    return battute.get(numero_battuta + "||" + numero_campana);
  }

  /**
   *
   * @param numero_battuta
   * @param numero_campana
   * @return
   */
  public Double getMinContrattempoSec(Integer numero_battuta, Integer numero_campana) {
    //Se è la prima battuta non può anticipare al messimo si fa ritardare la seconda battuta per far sembrare un anticipo
    if (numero_battuta == 0) {
      return 0.0;
    }

    //poi se deve fare un ritorno non può anticipare troppo senza fare ribattute o cali, ma può strozzare quindi gli si può annullare parte del tempo
    //per ora tolgo al massimo il tempo suonata
    Set<Integer> campanePrecendi = new LinkedHashSet<>(listaBattute.get(numero_battuta - 1).getListaCampane().keySet());
    boolean mioRitorno = campanePrecendi.contains(numero_campana);
    if (mioRitorno) {
      return tempoSuonata / 1000;
    }

    //Nel caso di un doppio con ritorno e la campana selezionata non è quella implicata nel ritorno può anticipare di più
    Set<Integer> campaneBattua = new LinkedHashSet<>(listaBattute.get(numero_battuta).getListaCampane().keySet());
    campaneBattua.retainAll(campanePrecendi);
    boolean isRitorno = (campaneBattua.size() > 0);
    if (isRitorno && !mioRitorno) {
      return (tempoSuonata + getTempoRitorno()) / 1000;
    }

    return tempoSuonata / 1000;
  }

  //restituire il minimo dei massimi
  public Double getMaxContrattempoSec(Integer numero_battuta, Integer numero_campana) {
    return tempoSuonata / 1000;
  }

  public Integer getNumberBattutaFromModelBattuta(ModelBattuta mb) {
    int numero_battuta;
    for (ListIterator it = listaBattute.listIterator(); it.hasNext();) {
      numero_battuta = it.nextIndex();
      ModelBattuta o = (ModelBattuta) it.next();
      if (o.hashCode() == mb.hashCode()) {
        return numero_battuta;
      }
    }
    return null;
  }

  public void saveFileSuonata(final Window w, File dir) {
    FileChooser fileChooser = new FileChooser();
    if (dir != null) {
      fileChooser.setInitialDirectory(dir);
    }

    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Suonata campane sistema veronese (*.csvs)", "*.csvs");
    fileChooser.getExtensionFilters().add(extFilter);

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
    System.out.println("Salvataggio suonata completato");
  }

  public void saveOnExcel(int numero_colonne, final Window w) {
    FileChooser fileChooser = new FileChooser();
    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel (*.xlsx)", "*.xlsx");
    fileChooser.getExtensionFilters().add(extFilter);

    File file = fileChooser.showSaveDialog(w);

    if (file != null) {

      Workbook wb = new XSSFWorkbook();
      Sheet sheet = wb.createSheet("Spartito_Suonata");
      int rowIndex = 0;
      int columnIndex = 0;
      Cell cell;
      Row row = sheet.createRow(rowIndex);
      for (ModelBattuta modelBattuta : this.getListaBattute()) {
        cell = row.createCell(columnIndex);
        cell.setCellValue(modelBattuta.getNomeBattuta(this.concerto));

        columnIndex++;
        if (columnIndex >= numero_colonne) {
          rowIndex++;
          columnIndex = 0;
          row = sheet.createRow(rowIndex);
        }
      }

      // Write the output to a file
      try {
        FileOutputStream out = new FileOutputStream(file);
        wb.write(out);
        out.close();
      } catch (Exception e) {
      }

    }
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
