package de.nmo.eclipse.ui.games.coffeehunter.entities.enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import de.nmo.eclipse.ui.games.coffeehunter.entities.Animation;
import de.nmo.eclipse.ui.games.coffeehunter.entities.Enemy;
import de.nmo.eclipse.ui.games.coffeehunter.gamestate.handler.Content;
import de.nmo.eclipse.ui.games.coffeehunter.tilemap.TileMap;

public class EnemySpiritEnergy extends Enemy {

  private BufferedImage[] startSprites;
  private BufferedImage[] sprites;

  private boolean         start;
  private boolean         permanent;

  private int             type        = 0;
  public static int       VECTOR      = 0;
  public static int       GRAVITY     = 1;
  public static int       BOUNCE      = 2;

  private int             bounceCount = 0;
  private long            lasting     = 5000;

  private long            creationTime;

  public EnemySpiritEnergy(TileMap tm) {

    super(tm);

    this.health = this.maxHealth = 1;

    this.width = 20;
    this.height = 20;
    this.cwidth = 12;
    this.cheight = 12;

    this.damage = 1;
    this.moveSpeed = 5;

    this.startSprites = Content.DarkEnergy[0];
    this.sprites = Content.DarkEnergy[1];
    this.animation = new Animation();
    this.animation.setFrames(this.startSprites);
    this.animation.setDelay(2);

    this.start = true;
    this.flinching = false;
    this.permanent = false;
    this.creationTime = System.currentTimeMillis();
  }

  public void setType(int i) {
    this.type = i;
  }

  public void setPermanent(boolean b) {
    this.permanent = b;
  }

  public void update() {
    if (health <= 0) {
      this.remove = true;
      return;
    }
    //checkTileMapCollision();
    if (this.start) {
      if (this.animation.hasPlayedOnce()) {
        this.animation.setFrames(this.sprites);
        //this.animation.setNumFrames(3);
        this.animation.setDelay(2);
        this.start = false;
      }
    }

    if (this.type == VECTOR) {
      this.x += this.dx;
      this.y += this.dy;
    } else if (this.type == GRAVITY) {
      this.dy += 0.2;
      this.x += this.dx;
      this.y += this.dy;
    } else if (this.type == BOUNCE) {
      double dx2 = this.dx;
      double dy2 = this.dy;
      checkTileMapCollision();
      if (this.dx == 0) {
        this.dx = -dx2;
        this.bounceCount++;
      }
      if (this.dy == 0) {
        this.dy = -dy2;
        this.bounceCount++;
      }
      this.x += this.dx;
      this.y += this.dy;
    }

    // update animation
    this.animation.update();

    if (!this.permanent) {
      if (this.x < 0 || this.x > this.tileMap.getWidth() || this.y < 0 || this.y > this.tileMap.getHeight()) {
        this.remove = true;
      }
      if (this.bounceCount == 3) {
        this.remove = true;
      }
      if (System.currentTimeMillis() - this.creationTime > this.lasting) {
        this.remove = true;
      }
    }

  }

  public void draw(Graphics2D g) {
    setMapPosition();
    super.draw(g);
  }

}
