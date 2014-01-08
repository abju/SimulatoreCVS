/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package csvsimulator;

import csvsimulator.model.ModelCampana;
import csvsimulator.model.ModelConcerto;
import csvsimulator.spartito.controller.SpartitoBaseController;
import java.io.File;
import java.net.URI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

/**
 *
 * @author lion
 */
public class CSVsimulator extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        //inizializzo oggetti di test per lo svilupppo
        ModelConcerto concertoAlbaredo = new ModelConcerto();
        concertoAlbaredo.setNomeConcerto("Concerto di Albaredo d'Agide");
        ModelCampana mc = new ModelCampana("6", 0);
        mc.loadFromPath("C:\\Users\\lion\\Documents\\GitHub\\SimulatoreCVS\\Software\\JavaFX\\CSVsimulator\\resources\\test\\campanili\\albaredo\\Albaredo 6 - long.mp3");
        concertoAlbaredo.pushCampana(mc);
        mc = new ModelCampana("1", 1);
        mc.loadFromPath("C:\\Users\\lion\\Documents\\GitHub\\SimulatoreCVS\\Software\\JavaFX\\CSVsimulator\\resources\\test\\campanili\\albaredo\\Albaredo 1 - long.mp3");
        concertoAlbaredo.pushCampana(mc);
        mc = new ModelCampana("2", 2);
        mc.loadFromPath("C:\\Users\\lion\\Documents\\GitHub\\SimulatoreCVS\\Software\\JavaFX\\CSVsimulator\\resources\\test\\campanili\\albaredo\\Albaredo 2 - long.mp3");
        concertoAlbaredo.pushCampana(mc);
        mc = new ModelCampana("3", 3);
        mc.loadFromPath("C:\\Users\\lion\\Documents\\GitHub\\SimulatoreCVS\\Software\\JavaFX\\CSVsimulator\\resources\\test\\campanili\\albaredo\\Albaredo 3 - long.mp3");
        concertoAlbaredo.pushCampana(mc);
        mc = new ModelCampana("4", 4);
        mc.loadFromPath("C:\\Users\\lion\\Documents\\GitHub\\SimulatoreCVS\\Software\\JavaFX\\CSVsimulator\\resources\\test\\campanili\\albaredo\\Albaredo 4 - long.mp3");
        concertoAlbaredo.pushCampana(mc);
        mc = new ModelCampana("5", 5);
        mc.loadFromPath("C:\\Users\\lion\\Documents\\GitHub\\SimulatoreCVS\\Software\\JavaFX\\CSVsimulator\\resources\\test\\campanili\\albaredo\\Albaredo 5 - long.mp3");
        concertoAlbaredo.pushCampana(mc);
        
        
        
        
        //Inizializzo la scena
        SampleController root = new SampleController();
        
        SpartitoBaseController sbc = new SpartitoBaseController(concertoAlbaredo, root.getNavbar(), root.getBarraSinistra());
        root.getChildren().add(sbc);
        AnchorPane.setTopAnchor(sbc, 141.0);
        AnchorPane.setLeftAnchor(sbc, 300.0);
        
        /*
        root.getSpartito().setModelConcerto(concertoAlbaredo);
        root.getSpartito().setNavbar(root.getNavbar());
        root.getSpartito().setLeftBar(root.getBarraSinistra());
        */
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }
    

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}