/* 
 * Copyright (C) 2014 Marco Dalla Riva <marco.dallariva@outlook.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package csvsimulator.spartito.controller;

import csvsimulator.model.ModelBattuta;
import csvsimulator.model.ModelCampana;
import csvsimulator.model.ModelConcerto;
import csvsimulator.model.ModelSuonata;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Marco Dalla Riva <marco.dallariva@outlook.com>
 */
public class SpartitoChartController extends ScatterChart {

  private ModelConcerto modelConcerto;
  private ModelSuonata modelSuonata;
  private ScatterChart.Series<Long, String> serie;

  private SpartitoBaseController spartitoBC;

  //Impostazioni nodo
  private Double nodeTempoCorretto;
  private Double nodeTempoMax;
  private Double nodeTempoMin;
  private int radiusCircle = 8;

  //Impostazione asssi
  private long maxAxisX = 20000;
  private ScrollBar scrollBarX;

  public SpartitoChartController(NumberAxis _xAxis, CategoryAxis _yAxis) {
    super(_xAxis, _yAxis);
    serie = new ScatterChart.Series<Long, String>();
    serie.setName("Spartito");

    this.getData().add(serie);
    ((NumberAxis) this.getXAxis()).setUpperBound(maxAxisX);
    ((NumberAxis) this.getXAxis()).setLowerBound(-450);

    ((NumberAxis) this.getXAxis()).setTickLabelFormatter(new NumberAxis.DefaultFormatter((NumberAxis) this.getXAxis()) {

      @Override
      public String toString(Number number) {
        String s = String.format("%02d:%02d:%02d",
                (long) ((number.longValue() / (1000 * 60 * 60)) % 24),
                (long) ((number.longValue() / (1000 * 60)) % 60),
                (long) (number.longValue() / 1000) % 60
        );
        return s;
      }

    });

    this.scrollBarX = new ScrollBar();
    this.scrollBarX.setMax(maxAxisX);
    this.scrollBarX.setVisibleAmount(maxAxisX);
    this.scrollBarX.setUnitIncrement(500);
    this.scrollBarX.valueProperty().addListener(new ChangeListener<Number>() {

      @Override
      public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
        onScrollBarxX(scrollBarX.getValue());
      }
    });

    this.getStylesheets().add("/csvsimulator/spartito/css/spartitoChart.css");
  }

  public void updateYAxis() {
    ObservableList<String> ol = FXCollections.<String>observableArrayList();
    for (ModelCampana modelCampana : modelConcerto.getListaCampane()) {
      ol.add(modelCampana.getNome());
    }
    ((CategoryAxis) this.getYAxis()).setCategories(ol);
  }

  public void updateScroll() {
    Double totTime = this.modelSuonata.getTotalTime();

    if (totTime > maxAxisX) {
      this.scrollBarX.setMax(totTime);
    } else {
      this.scrollBarX.setMax(maxAxisX);
    }
  }

  public void addBattuta(Integer numero_battuta) {

    ModelBattuta mb = this.modelSuonata.getListaBattute().get(numero_battuta);
    final NumberAxis xAxis = (NumberAxis) this.getXAxis();

    for (final Integer numeroCampana : mb.getListaCampane().keySet()) {

      Double timeCampana = getModelSuonata().getTimeCampana(numero_battuta, numeroCampana);

      ScatterChart.Data<Long, String> point;
      point = new ScatterChart.Data<>(timeCampana.longValue(), this.modelConcerto.getCampanaByNumero(numeroCampana).getNome(), mb);

      this.serie.getData().add(point);

      final Node node = point.getNode();
      node.setId(numero_battuta + "||" + numeroCampana);

      if (Objects.equals(numeroCampana, ModelBattuta.KEY_PAUSA)) {
        node.getStyleClass().add("chart-symbol-pausa");
      }
      this.updateScroll();

      node.setOnMousePressed((MouseEvent t) -> {
        String[] nodeId = node.getId().split("\\|\\|");
        Integer nb = Integer.valueOf(nodeId[0]);
        Integer nc = Integer.valueOf(nodeId[1]);
        spartitoBC.getOptionBar().setUpOptionBattuta(nb, nc);
        double contrattempoAttuale = mb.getTimeContrattempo(nc, modelSuonata.getTempoSuonata());
        nodeTempoCorretto = (double) point.getXValue() - contrattempoAttuale;
        nodeTempoMin = nodeTempoCorretto - modelSuonata.getMinContrattempoSec(nb, nc) * 1000;
        nodeTempoMax = nodeTempoCorretto + modelSuonata.getMaxContrattempoSec(nb, nc) * 1000;
      });

      node.setOnMouseDragged((MouseEvent t) -> {
        String[] nodeId = node.getId().split("\\|\\|");
        Integer nb = Integer.valueOf(nodeId[0]);
        Integer nc = Integer.valueOf(nodeId[1]);
        Number posX = xAxis.getValueForDisplay(t.getX());
        long newPos = point.getXValue() + posX.longValue() - xAxis.getValueForDisplay(radiusCircle).longValue();

        if (newPos < nodeTempoMin) {
          newPos = nodeTempoMin.longValue();
        } else if (newPos > nodeTempoMax) {
          newPos = nodeTempoMax.longValue();
        }

        point.setXValue(newPos);

        mb.setContrattempo(nc, (newPos - nodeTempoCorretto) / getModelSuonata().getTempoSuonata());

        spartitoBC.getOptionBar().setUpOptionBattuta(nb, nc);
      });
    }
    refreshPosizioneCampane();
  }

  public void removeBattuta(ModelBattuta mb, Integer numero_battuta) {
    //String[] ids = new String[mb.getListaCampane().size()];
    List<String> ids = new ArrayList<>();
    for (Iterator<Integer> it = mb.getListaCampane().keySet().iterator(); it.hasNext();) {
      Integer numero_campana = it.next();
      ids.add(numero_battuta + "||" + numero_campana);      
    }
    
    Collection<Data> nodes = new ArrayList<>();
    for (Iterator<Data<Long, String>> it = this.serie.getData().iterator(); it.hasNext();) {
      Data<Long, String> data = it.next();
      String id = data.getNode().getId();
      
      if(ids.contains(id)){
        nodes.add(data);        
      }
    }
    this.serie.getData().removeAll(nodes);
    refreshPosizioneCampane();
  }

  public void refreshPosizioneCampane() {
    Map<String, Double> timeBattute = getModelSuonata().getTimeBattute();

    for (Iterator<Data<Long, String>> it = this.serie.getData().iterator(); it.hasNext();) {
      Data<Long, String> data = it.next();
      String id = data.getNode().getId();
      ModelBattuta mb = (ModelBattuta) data.getExtraValue();
      Integer nb_now = modelSuonata.getListaBattute().indexOf(mb);

      //Controllo se l'id è giusto
      String[] nodeId = id.split("\\|\\|");
      Integer nb = Integer.valueOf(nodeId[0]);
      Integer nc = Integer.valueOf(nodeId[1]);

      if (!Objects.equals(nb, nb_now)) {
        id = nb_now + "||" + nc;
        data.getNode().setId(id);
      }

      Double timeCampana = timeBattute.get(id);

      data.setXValue(timeCampana.longValue());
    }
  }

  public ScrollBar getScrollBarX() {
    return this.scrollBarX;
  }

  private void onScrollBarxX(Number value) {
    ((NumberAxis) this.getXAxis()).setUpperBound(value.longValue() + this.scrollBarX.getVisibleAmount() - 450);
    ((NumberAxis) this.getXAxis()).setLowerBound(value.longValue() - 450);
  }

  /**
   * @return the modelConcerto
   */
  public ModelConcerto getModelConcerto() {
    return modelConcerto;
  }

  /**
   * @param modelConcerto the modelConcerto to set
   */
  public void setModelConcerto(ModelConcerto modelConcerto) {
    this.modelConcerto = modelConcerto;
    this.updateYAxis();
  }

  /**
   * @return the modelSuonata
   */
  public ModelSuonata getModelSuonata() {
    return modelSuonata;
  }

  /**
   * @param modelSuonata the modelSuonata to set
   */
  public void setModelSuonata(ModelSuonata modelSuonata) {
    this.modelSuonata = modelSuonata;
  }

  /**
   * @return the spartitoBC
   */
  public SpartitoBaseController getSpartitoBC() {
    return spartitoBC;
  }

  /**
   * @param spartitoBC the spartitoBC to set
   */
  public void setSpartitoBC(SpartitoBaseController spartitoBC) {
    this.spartitoBC = spartitoBC;
  }

}
