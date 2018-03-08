package de.nmo.eclipse.ui.games.coffeehunter;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import de.nmo.eclipse.ui.games.coffeehunter.audio.JukeBox;
import de.nmo.eclipse.ui.games.coffeehunter.entities.MapObject;
import de.nmo.eclipse.ui.games.coffeehunter.gamestate.GameStateManager;
import de.nmo.eclipse.ui.games.coffeehunter.gamestate.IExit;
import de.nmo.eclipse.ui.games.coffeehunter.gamestate.handler.Keys;
import de.nmo.eclipse.ui.games.coffeehunter.tilemap.TileMap;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, KeyListener {

  // dimensions
  public static int        WIDTH      = 320;
  public static int        HEIGHT     = 240;
  public static double     SCALE      = 2;

  private static boolean   debug;

  // game thread
  private Thread           thread;
  private boolean          running;
  private int              FPS        = 60;
  private long             targetTime = 1000 / FPS;

  // image
  private BufferedImage    image;
  private Graphics2D       g;

  // game state manager
  private GameStateManager gsm;

  public static IExit      exit;

  public GamePanel() {
    super();
    setPreferredSize(new Dimension((int) (WIDTH * SCALE), (int) (HEIGHT * SCALE)));
    setFocusable(true);
    requestFocus();

    debug = true;
  }

  public static boolean isDebug() {
    return "true".equals(System.getProperty("ch.debug"));
  }

  public void addNotify() {
    super.addNotify();
    if (thread == null) {
      thread = new Thread(this);
      addKeyListener(this);
      thread.start();
    }
  }

  private void init() {

    image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    g = (Graphics2D) image.getGraphics();

    running = true;

    gsm = new GameStateManager(this);

  }

  public void run() {

    init();

    long start;
    long elapsed;
    long wait;

    // game loop
    while (running) {

      start = System.nanoTime();

      update();
      draw();
      drawToScreen();

      elapsed = System.nanoTime() - start;

      wait = targetTime - elapsed / 1000000;
      if (wait < 0)
        wait = 5;

      try {
        Thread.sleep(wait);
      } catch (Exception e) {
        e.printStackTrace();
      }

    }

  }

  private void update() {
    gsm.update();
  }

  private void draw() {
    gsm.draw(g);
  }

  private void drawToScreen() {
    Graphics g2 = getGraphics();
    if (g2 == null) {
      this.running = false;
      this.thread = null;
      JukeBox.stop();
      return;
    }
    g2.drawImage(image, 0, 0, (int) (WIDTH * SCALE), (int) (HEIGHT * SCALE), null);
    g2.dispose();
  }

  public static boolean onScreen(TileMap tm, MapObject m) {
    if ((m.getx() + m.getWidth() > (-1 * tm.getx()) && m.getx() < (-1 * tm.getx() + GamePanel.WIDTH + m.getWidth()))
        && ((m.gety() > (-1 * tm.gety()) && m.gety() < (-1 * tm.gety() + GamePanel.HEIGHT)))) {
      return true;
    }
    return false;
  }

  public void keyTyped(KeyEvent key) {
  }

  public void keyPressed(KeyEvent key) {
    Keys.keySet(key.getKeyCode(), true);
  }
  public void keyReleased(KeyEvent key) {
    Keys.keySet(key.getKeyCode(), false);
  }

  /**
   * 
   *
   * @author TJNKXAO
   * @since 04.09.2017
   */
  public static void exit() {
    if (exit == null) {
      System.exit(0);
    } else {
      exit.exit();
    }
  }

}
