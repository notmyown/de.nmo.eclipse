/**
 *
 ******************************************************************
 *     Copyright VW AG, Germany     *
 ******************************************************************
 *
 ******************************************************************
 *Administrative Information (automatically filled in by MKS)
 ******************************************************************
 *
 * $ProjectName: $
 * $Author: $
 * $Date: $
 * $Name:  $
 * $ProjectRevision: 1.81 $
 * $Revision: 1.141 $
 * $Source: central.mak $
 ******************************************************************
**/
package de.nmo.eclipse.ui.games.coffeehunter.gamestate;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import de.nmo.eclipse.ui.games.coffeehunter.GamePanel;
import de.nmo.eclipse.ui.games.coffeehunter.audio.JukeBox;
import de.nmo.eclipse.ui.games.coffeehunter.entities.Enemy;
import de.nmo.eclipse.ui.games.coffeehunter.entities.Explosion;
import de.nmo.eclipse.ui.games.coffeehunter.entities.HUD;
import de.nmo.eclipse.ui.games.coffeehunter.entities.Player;
import de.nmo.eclipse.ui.games.coffeehunter.entities.Title;
import de.nmo.eclipse.ui.games.coffeehunter.gamestate.handler.Keys;
import de.nmo.eclipse.ui.games.coffeehunter.tilemap.Background;
import de.nmo.eclipse.ui.games.coffeehunter.tilemap.TileMap;

/**
 * 
 *
 * @author Bergen, Marco (I-EA-25, extern)
 * @since 27.07.2017
 */
public abstract class AbstractLevelGameState extends GameState {

  /**
   * @param gsm
   *
   * @author Bergen, Marco (I-EA-25, extern)
   * @since 27.07.2017
   */
  public AbstractLevelGameState(GameStateManager gsm) {
    super(gsm);
    // TODO Auto-generated constructor stub
  }

  protected TileMap              tileMap;
  protected Background           bg;

  protected Player               player;

  protected List<Enemy>          enemies;
  protected List<Explosion>      explosions;

  protected HUD                  hud;

  protected Title                title;
  protected BufferedImage        titleImage;

  //events
  protected boolean              blockInput = false;
  protected int                  eventCount = 0;
  protected boolean              eventStart;
  protected ArrayList<Rectangle> tb;
  protected boolean              eventFinish;
  protected boolean              eventDead;

  public void init() {
    this.tileMap = createTileMap();
    this.bg = createBackground();
    this.enemies = Collections.synchronizedList(new ArrayList<Enemy>());
    this.explosions = new ArrayList<>();
    this.player = createPlayer();
    this.enemies.addAll(createEnemies());
    this.player.getKnownEnemies().addAll(this.enemies);
    this.hud = new HUD(this.player);
    this.title = createTitle();
    // start event
    eventStart = true;
    tb = new ArrayList<Rectangle>();
    eventStart();

  }

  abstract protected TileMap createTileMap();

  abstract protected Player createPlayer();

  abstract protected Background createBackground();

  abstract protected List<Enemy> createEnemies();

  abstract protected Title createTitle();

  abstract void eventStart();

  abstract void eventDead();

  abstract void eventFinish();

  public void update() {

    handleInput();

    // check if player dead event should start
    if (player.getHealth() == 0 || player.gety() > tileMap.getHeight()) {
      eventDead = true;
    }

    // play events
    if (eventStart) {
      eventStart();
    }
    if (eventDead) {
      eventDead();
    }
    if (eventFinish) {
      eventFinish();
    }

    // move title and subtitle
    if (title != null) {
      title.update();
      if (title.shouldRemove())
        title = null;
    }

    // update player
    this.player.update();
    this.tileMap.setPosition(GamePanel.WIDTH / 2 - this.player.getx(), GamePanel.HEIGHT / 2 - this.player.gety());

    this.bg.setPosition(this.tileMap.getx(), this.tileMap.gety());

    this.player.checkAttack(this.enemies);

    List<Enemy> tmpEnemies = new ArrayList<>();
    tmpEnemies.addAll(this.enemies);
    ListIterator<Enemy> iter = tmpEnemies.listIterator();
    List<Enemy> removes = new ArrayList<>();
    while (iter.hasNext()) {
      Enemy e = iter.next();
      e.update();
      if (e.isDead()) {
        this.explosions.add(new Explosion(e.getx(), e.gety()));
        removes.add(e);
        e.finishEvent();
        JukeBox.play("explode", 2000);
      }
      if (e.shouldRemove()) {
        removes.add(e);
      }
    }
    this.enemies.removeAll(removes);


    ListIterator<Explosion> iter2 = this.explosions.listIterator();
    while (iter2.hasNext()) {
      Explosion e = iter2.next();
      e.update();
      if (e.shouldRemove()) {
        iter2.remove();
      }
    }

  }

  public void draw(Graphics2D g) {

    // draw bg
    this.bg.draw(g);

    // draw tilemap
    this.tileMap.draw(g);

    // draw player
    this.player.draw(g);

    for (Enemy e : this.enemies) {
      if (GamePanel.onScreen(this.tileMap, e)) {
        e.draw(g);
      }
    }

    for (Explosion e : this.explosions) {
      e.setMapPosition((int) this.tileMap.getx(), (int) this.tileMap.gety());
      e.draw(g);
    }

    this.hud.draw(g);
    if (this.title != null) {
      this.title.draw(g);
    }

    // draw transition boxes
    g.setColor(java.awt.Color.BLACK);
    for (int i = 0; i < tb.size(); i++) {
      g.fill(tb.get(i));
    }
  }

  public void handleInput() {
    if (Keys.isPressed(Keys.ESCAPE))
      gsm.setPaused(true);
    if (blockInput || player.getHealth() == 0)
      return;
    player.setUp(Keys.keyState[Keys.UP]);
    player.setLeft(Keys.keyState[Keys.LEFT]);
    player.setDown(Keys.keyState[Keys.DOWN]);
    player.setRight(Keys.keyState[Keys.RIGHT]);
    player.setJumping(Keys.keyState[Keys.BUTTON1]);
    player.setGliding(Keys.keyState[Keys.BUTTON2]);

    if (Keys.isPressed(Keys.BUTTON3)) {
      player.setScratching();
    }
    if (Keys.isPressed(Keys.BUTTON4)) {
      player.setFiring();
    }
  }

}
