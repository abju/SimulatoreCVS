/*
 * The MIT License
 *
 * Copyright 2014 Marco Dalla Riva <marco.dallariva@outlook.com>.
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
package csvsimulator.spartito.controller;

import csvsimulator.model.ModelBattuta;
import csvsimulator.model.ModelCampana;
import csvsimulator.model.ModelConcerto;
import csvsimulator.model.ModelSuonata;
import java.util.Iterator;
import java.util.Map;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
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
    
    ((NumberAxis) this.getXAxis()).setTickLabelFormatter(new NumberAxis.DefaultFormatter((NumberAxis) this.getXAxis()){

      @Override
      public String toString(Number number) {
        String s = String.format("%02d:%02d:%02d",
                (long) ((number.longValue() / (1000*60*60)) % 24),
                (long) ((number.longValue() / (1000*60)) % 60),
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
    for (Iterator<ModelCampana> it = modelConcerto.getListaCampane().iterator(); it.hasNext();) {
      ModelCampana modelCampana = it.next();
      ol.add(modelCampana.getNome());
    }
    ((CategoryAxis) this.getYAxis()).setCategories(ol);
  }

  public void updateScroll(){
    Double totTime = this.modelSuonata.getTotalTime();
    
    if(totTime > maxAxisX){
      this.scrollBarX.setMax(totTime);
    } else {
      this.scrollBarX.setMax(maxAxisX);
    }
  }
  
  public void pushBattuta(Integer numero_battuta) {

    ModelBattuta mb = this.modelSuonata.getListaBattute().get(numero_battuta);
    final NumberAxis xAxis = (NumberAxis)this.getXAxis();

    for (final Integer numeroCampana : mb.getListaCampane().keySet()) {

      Double timeCampana = getModelSuonata().getTimeCampana(numero_battuta, numeroCampana);

      ScatterChart.Data<Long, String> point = new ScatterChart.Data<Long, String>(timeCampana.longValue(), this.modelConcerto.getCampanaByNumero(numeroCampana).getNome(), mb);
      
      this.serie.getData().add(point);
      
      final Node node = point.getNode();
      node.setId(numero_battuta + "||" + numeroCampana);
      
      this.updateScroll();
      
      
      
      node.setOnMousePressed(new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent t) {
          
          spartitoBC.getOptionBar().setUpOptionBattuta(numero_battuta, numeroCampana);
          double contrattempoAttuale = mb.getTimeContrattempo(numeroCampana, modelSuonata.getTempoSuonata());          
          nodeTempoCorretto = (double)point.getXValue() - contrattempoAttuale;  
          nodeTempoMin = nodeTempoCorretto - modelSuonata.getMinContrattempoSec(numero_battuta, numeroCampana) * 1000;
          nodeTempoMax = nodeTempoCorretto + modelSuonata.getMaxContrattempoSec(numero_battuta, numeroCampana) * 1000;
        }
      });
      
      
      node.setOnMouseDragged(new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent t) {
          Number posX = xAxis.getValueForDisplay(t.getX());
          long newPos = point.getXValue() + posX.longValue() - xAxis.getValueForDisplay(radiusCircle).longValue();      
          
          if (newPos < nodeTempoMin) {
            newPos = nodeTempoMin.longValue();
          } else if (newPos > nodeTempoMax) {
            newPos = nodeTempoMax.longValue();
          }
          
          point.setXValue(newPos);

          mb.getListaCampane().put(numeroCampana, (newPos - nodeTempoCorretto) / getModelSuonata().getTempoSuonata());
          
          spartitoBC.getOptionBar().setUpOptionBattuta(numero_battuta, numeroCampana);
        }
      });
    }
  }
  
  public void popBattuta() {
    Integer index = modelSuonata.getListaBattute().size() - 1;
    if (index >= 0) {
      
      ModelBattuta mb = modelSuonata.getListaBattute().get(index);
      for (Iterator<Integer> it = mb.getListaCampane().keySet().iterator(); it.hasNext();) {
        Integer integer = it.next();
        this.serie.getData().remove(this.serie.getData().size()-1);
      }      
      
      refreshPosizioneCampane();
    }
  }

  public void refreshPosizioneCampane(){
    Map<String, Double> timeBattute = getModelSuonata().getTimeBattute();

    for (Iterator<Data<Long, String>> it = this.serie.getData().iterator(); it.hasNext();) {
      Data<Long, String> data = it.next();
      String id = data.getNode().getId();
      Double timeCampana = timeBattute.get(id);
      
      data.setXValue(timeCampana.longValue());
    }
  }
  
  
  public ScrollBar getScrollBarX() {
        return this.scrollBarX;
  }
  
  private void onScrollBarxX(Number value){
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
