/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package csvsimulator;

import csvsimulator.model.ModelConcerto;
import csvsimulator.model.ModelSuonata;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.annotation.Arg;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;

/**
 *
 * @author lion
 */
public class CSVsimulator extends Application {

  private static class Option {

    @Arg(dest = "filename")
    public String filename;

    @Arg(dest = "filesuonata")
    public String filesuonata;
  }

  private static Option optArgs;

  @Override
  public void start(Stage stage) throws Exception {
        
    stage.getIcons().add(new Image(CSVsimulator.class.getResourceAsStream("myBell.png")));
    stage.setTitle("Simulatore Concerti a Sistema Veronese");

    MainController root = new MainController();

    System.out.println(optArgs.filesuonata + " . "  + optArgs.filename);
    if (optArgs.filesuonata != null) {
      ModelSuonata ms = new ModelSuonata();
      File file = new File(optArgs.filesuonata);
      String path = file.getAbsolutePath();
      
      

      ObjectInputStream ois;
      try {
        ois = new ObjectInputStream(new FileInputStream(path));
        ms = (ModelSuonata) ois.readObject();
      } catch (ClassNotFoundException ex) {
        System.err.println(ex.toString());
      }
      root.apriNuovaSuonataCompleta(ms);
      
    } else if (optArgs.filename != null) {
      ModelConcerto mc = new ModelConcerto();
      File file = new File(optArgs.filename);
      String path = file.getAbsolutePath();

      ObjectInputStream ois;
      try {
        ois = new ObjectInputStream(new FileInputStream(path));
        mc = (ModelConcerto) ois.readObject();
      } catch (ClassNotFoundException ex) {
        System.err.println(ex.toString());
      }
      root.apriNuovaSuonata(mc);
    }

    Scene scene = new Scene(root);

    stage.setScene(scene);
    stage.show();
  }

  /**
   * The main() method is ignored in correctly deployed JavaFX application.
   * main() serves only as fallback in case the application can not be launched
   * through deployment artifacts, e.g., in IDEs with limited FX support.
   * NetBeans ignores main().
   *
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    optArgs = new Option();

    try {
      ArgumentParser parser = ArgumentParsers.newArgumentParser("prog");
      parser.addArgument("--filename").required(false);
      parser.addArgument("--filesuonata").required(false);
      parser.parseArgs(args, optArgs);
    } catch (ArgumentParserException ex) {

    }
    launch(args);
  }
}
