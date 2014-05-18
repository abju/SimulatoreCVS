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
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;

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

        private final String name;
        private final Thread t;
        private boolean suspendFlag;

        private final List<PlayerCodaCampana> coda;
        private final Player p;
        private Timer timer;

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
            timer = new Timer(true);
            Long t = (long)0;
            try {
                while (it.hasNext()) {
                    PlayerCodaCampana obj;
                    obj = (PlayerCodaCampana) it.next();
                    System.out.println("Battuta: " + obj.getNumeroBattuta() + " - " + obj.getCampana().getNome() +"  Pausa " + (long) obj.getPausa());
                    
                    synchronized (this) {
                        /*new Thread(new Runnable() {
                            @Override public void run() {
                                for (int i=1; i<=(long) obj.getPausa(); i++) {
                                    final int counter = i;
                                    Platform.runLater(new Runnable() {
                                        @Override public void run() {
                                            //bar.setProgress(counter/1000000.0);
                                            //System.out.println(counter+"");
                                            //p.timePlayer(counter);
                                        }
                                    });
                                }
                            }
                        }).start();
                        */
                      
                        if((long) obj.getPausa()  > 0){
                          final long t1 = t;
                          t += (long) obj.getPausa();

                          final long t2 = t;
                          timer = new Timer(true);
                          timer.scheduleAtFixedRate(new TimerTask() {

                            private long time = 0;

                            @Override
                            public void run() {
                              if(time == 0){
                                time = t1;
                              }
                              Platform.runLater(new Runnable() {
                                  @Override public void run() {
                                      p.timePlayer((int)time);
                                  }
                              });

                              time += 20;
                            }
                          }, 0 , 20);
                        }
                      
                        Thread.currentThread().sleep((long) obj.getPausa());
                        while (suspendFlag) {
                            wait();
                        }
                        
                      if((long) obj.getPausa()  > 0){
                        timer.cancel();
                      }
                    }
                    
                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            System.out.println("Numero battuta : " + obj.getNumeroBattuta());
                            p.nowPlaying(obj.getNumeroBattuta());
                        }
                    });
                    //obj.getCampana().play();
                    
                    if(!obj.getReboto().equals("")){
                      obj.getCampana().playReboto(obj.getReboto());
                    } else {
                      obj.getCampana().play();
                    }
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
                timer.cancel();
            }
        }

        public void stop() {
            t.interrupt();
            timer.cancel();
            notifyPlayer();
        }

        private final void notifyPlayer() {
            p.stopListner();
        }
    }

}
