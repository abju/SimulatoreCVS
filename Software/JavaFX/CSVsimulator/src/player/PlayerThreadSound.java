/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package player;

import java.io.InputStream;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

/**
 *
 * @author lion
 */
public class PlayerThreadSound extends Thread {

    private InputStream is;

    public PlayerThreadSound() {
    }

    public PlayerThreadSound(String name) {
        super(name);
    }

    public PlayerThreadSound(String name, InputStream _is) {
        super(name);
        is = _is;
    }

    @Override
    public void run() {
        try {
            AdvancedPlayer player = new AdvancedPlayer(is);
            player.play();
        } catch (JavaLayerException ex) {
        }
    }
}
