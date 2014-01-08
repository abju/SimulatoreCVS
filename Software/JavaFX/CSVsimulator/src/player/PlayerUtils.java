/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package player;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author lion
 */
public class PlayerUtils {

    /**
     *
     * @param buffer
     * @return
     */
    public final static ArrayList<InputStream> cloneInputStream(InputStream buffer) {
        ArrayList<InputStream> a;
        a = new ArrayList<>();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int len;
            while ((len = buffer.read(b)) > -1) {
                baos.write(b, 0, len);
            }
            baos.flush();
            //InputStream is1 = new ByteArrayInputStream(baos.toByteArray());
            a.add(new ByteArrayInputStream(baos.toByteArray()));
            a.add(new ByteArrayInputStream(baos.toByteArray()));
            //buffer = new ByteArrayInputStream(baos.toByteArray());
            return a;
        } catch (IOException ex) {
        }
        return null;
    }

    public interface ThreadCompleteListener {

        void notifyOfThreadComplete(final Thread thread);
    }

    public static class runThreadCampane implements Runnable {

        private String name;
        private Thread t;
        private boolean suspendFlag;

        private List<PlayerCodaCampana> coda;
        private Player p;

        public runThreadCampane(String tName, List<PlayerCodaCampana> coda, Player p) {
            name = tName;
            this.coda = coda;
            this.p = p;
            t = new Thread(this, name);
            suspendFlag = false;

        }

        @Override
        public void run() {
            Iterator it = coda.iterator();
            try {
                while (it.hasNext()) {
                    PlayerCodaCampana obj;
                    obj = (PlayerCodaCampana) it.next();
                    System.out.println("Battuta: " + obj.getNumeroBattuta() + " - " + obj.getCampana().getNome() +"  Pausa " + (long) obj.getPausa());
                    synchronized (this) {
                        Thread.currentThread().sleep((long) obj.getPausa());
                        while (suspendFlag) {
                            wait();
                        }
                    }

                    obj.getCampana().play();
                }
            } catch (InterruptedException ex) {
                System.err.println(ex.toString());
            } finally {
                stop();
            }
        }

        public void start() {
            t.start();
        }

        public void suspend() {
            suspendFlag = true;
        }

        public synchronized void resume() {
            suspendFlag = false;
            notify();
        }

        public void pause() {
            if (suspendFlag) {
                resume();
            } else {
                suspend();
            }
        }

        public void stop() {
            t.interrupt();
            notifyPlayer();
        }

        private final void notifyPlayer() {
            p.stopListner();
        }
    }

}
