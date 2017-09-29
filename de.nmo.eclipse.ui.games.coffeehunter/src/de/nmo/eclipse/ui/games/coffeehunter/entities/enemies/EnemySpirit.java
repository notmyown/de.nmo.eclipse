package de.nmo.eclipse.ui.games.coffeehunter.entities.enemies;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.imageio.ImageIO;

import de.nmo.eclipse.ui.games.coffeehunter.entities.Animation;
import de.nmo.eclipse.ui.games.coffeehunter.entities.Enemy;
import de.nmo.eclipse.ui.games.coffeehunter.entities.Explosion;
import de.nmo.eclipse.ui.games.coffeehunter.entities.Player;
import de.nmo.eclipse.ui.games.coffeehunter.tilemap.TileMap;

public class EnemySpirit extends Enemy {

  public BufferedImage[]  sprites;
  private Player          player;
  private List<Enemy>     enemies;
  private List<Explosion> explosions;

  private boolean         active;
  private boolean         finalAttack;

  private int             step;
  private int             stepCount;

  // attack pattern
  private int[]           steps = { 0, 1, 0, 1 };//{ 0, 1, 0, 1, 2, 1, 0, 2, 1, 2 };

  ////attacks:
  // fly around throwing dark energy (0)
  // floor sweep (1)
  // crash down on floor to create shockwave (2)
  //// special:
  // after half hp, create shield
  // after quarter hp, bullet hell

  private EnemySpiritEnergy[]    shield;
  private double          ticks;

  public EnemySpirit(TileMap tm, Player p, List<Enemy> enemies, List<Explosion> explosions, int x, int y) {

    super(tm);

    this.player = p;
    this.enemies = enemies;
    this.explosions = explosions;

    this.width = 40;
    this.height = 40;
    this.cwidth = 30;
    this.cheight = 30;

    this.health = this.maxHealth = 80;

    this.facingRight = true;

    this.moveSpeed = 1.4;

    try {
      BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/Spirit.png"));
      this.sprites = new BufferedImage[4];
      for (int i = 0; i < this.sprites.length; i++) {
        this.sprites[i] = spritesheet.getSubimage(i * this.width, 0, this.width, this.height);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    this.damage = 1;
    this.animation = new Animation();
    this.animation.setFrames(this.sprites);
    this.animation.setDelay(100);

    this.shield = new EnemySpiritEnergy[2];

    this.step = 0;
    this.stepCount = 0;
    setPosition(x, y);
  }

  public void setActive() {
    this.active = true;
  }

  public void update() {

    //checkTileMapCollision();

    //setPosition(500, 300);
    if (this.health == 0)
      return;

    // restart attack pattern
    if (this.step == this.steps.length) {
      this.step = 0;
    }

    this.ticks++;

    if (this.flinching) {
      this.flinchCount++;
      if (this.flinchCount >= 8) {
        this.flinching = false;
        this.flinchCount = 0;
      }
    }

    this.x += this.dx;
    this.y += this.dy;

    this.animation.update();
    if (!this.active)
      return;

    ////////////
    // special
    ////////////
    if (this.health <= this.maxHealth / 2) {
      if (this.shield[0] == null) {
        this.shield[0] = new EnemySpiritEnergy(this.tileMap);
        this.shield[0].setPermanent(true);
        this.enemies.add(this.shield[0]);
      }
      if (this.shield[1] == null) {
        this.shield[1] = new EnemySpiritEnergy(this.tileMap);
        this.shield[0].setPermanent(true);
        this.enemies.add(this.shield[1]);
      }
      double pos = this.ticks / 32;
      this.shield[0].setPosition(this.x + 30 * Math.sin(pos), this.y + 30 * Math.cos(pos));
      pos += 3.1415;
      this.shield[1].setPosition(this.x + 30 * Math.sin(pos), this.y + 30 * Math.cos(pos));
    }

    if (!this.finalAttack && this.health <= this.maxHealth / 4) {
      this.stepCount = 0;
      this.finalAttack = true;
    }

    if (this.finalAttack) {
      this.stepCount++;
      if (this.stepCount == 1) {
        this.explosions.add(new Explosion((int) this.x, (int) this.y));
        this.x = -9000;
        this.y = 9000;
        this.dx = this.dy = 0;
      }
      if (this.stepCount == 60) {
        this.x = this.tileMap.getWidth() / 2;
        this.y = this.tileMap.getHeight() / 2;
        this.explosions.add(new Explosion((int) this.x, (int) this.y));
      }
      if (this.stepCount >= 90 && this.stepCount % 30 == 0) {
        EnemySpiritEnergy de = new EnemySpiritEnergy(this.tileMap);
        de.setPosition(this.x, this.y);
        de.setVector(3 * Math.sin(this.stepCount / 32), 3 * Math.cos(this.stepCount / 32));
        de.setType(EnemySpiritEnergy.BOUNCE);
        this.enemies.add(de);
      }
      return;
    }

    ////////////
    // attacks
    ////////////

    // fly around dropping bombs
    if (this.steps[this.step] == 0) {
      calculateCorners(x, y - cheight / 2);
      this.stepCount++;
      if (!this.topLeft && !this.topRight && !this.curTop) {
        this.dy = -1;
      } else {
        this.dy = 0;
      }
      if (this.dy == 0) {
        if (this.facingRight) {
          this.dx = 1;
        } else {
          this.dx = -1;
        }
        if (this.curLeft) {
          this.facingRight = true;
        }
        if (this.curRight) {
          this.facingRight = false;
        }
      }
      if (this.stepCount % 60 == 0) {
        EnemySpiritEnergy de = new EnemySpiritEnergy(this.tileMap);
        de.setType(EnemySpiritEnergy.GRAVITY);
        de.setPosition(this.x, this.y);
        int dir = Math.random() < 0.5 ? 1 : -1;
        de.setVector(dir, 0);
        this.enemies.add(de);
      }
      if (this.stepCount == 559) {
        this.step++;
        this.stepCount = 0;
        this.right = this.left = false;
      }
    }
    // floor sweep
    else if (this.steps[this.step] == 1) {
      this.stepCount++;
      if (this.stepCount == 1) {
        this.explosions.add(new Explosion((int) this.x, (int) this.y));
        this.x = -9000;
        this.y = 9000;
        this.dx = this.dy = 0;
      }
      if (this.stepCount == 60) {
        this.y = this.player.gety();
        if (this.player.getx() > this.x) {

          double tmpx = this.player.getx();
          if (tmpx < 0) {
            tmpx = 0;
          }
          while (tmpx < this.tileMap.getWidth()) {
            calculateCorners(tmpx, this.y);
            if (this.topRight || this.bottomRight) {
              break;
            }
            tmpx++;
          }
          this.x = tmpx;
          this.dx = -2;
        } else {
          double tmpx = this.player.getx();
          while (tmpx > 0) {
            calculateCorners(tmpx, this.y);
            if (this.topLeft || this.bottomLeft) {
              break;
            }
            tmpx--;
          }
          this.dx = 2;
        }

        this.explosions.add(new Explosion((int) this.x, (int) this.y));
      }
      if ((this.x > 0 && this.x < this.tileMap.getWidth()) && (this.y > 0 && this.y < this.tileMap.getHeight())) {
        calculateCorners(this.x, this.y);
        if ((this.dx == 2 && this.topRight || (this.dx == -2 && this.topLeft))) {
          this.stepCount = 0;
          this.step++;
          this.dx = this.dy = 0;
        }
      }

    }
    // shockwave
    else if (this.steps[this.step] == 2) {
      this.stepCount++;
      if (this.stepCount == 1) {
        this.x = this.tileMap.getWidth() / 2;
        this.y = 40;
      }
      if (this.stepCount == 60) {
        this.dy = 7;
      }
      if (this.y >= this.tileMap.getHeight() - 30) {
        this.dy = 0;
      }
      if (this.stepCount > 60 && this.stepCount < 120 && this.stepCount % 5 == 0 && this.dy == 0) {
        EnemySpiritEnergy de = new EnemySpiritEnergy(this.tileMap);
        de.setPosition(this.x, this.y);
        de.setVector(-3, 0);
        this.enemies.add(de);
        de = new EnemySpiritEnergy(this.tileMap);
        de.setPosition(this.x, this.y);
        de.setVector(3, 0);
        this.enemies.add(de);
      }
      if (this.stepCount == 120) {
        this.stepCount = 0;
        this.step++;
      }
    }
    //getNextPosition();
    setPosition(this.x + this.dx, this.y + this.dy);
  }

  public Rectangle getRectangle() {
    int xoffset = (width - cwidth) / 2;
    int yoffset = (height - cheight) / 2;
    return new Rectangle((int) x - cwidth + xoffset, (int) y - cheight + (height - cheight) - yoffset, cwidth, cheight);
  }

  public void draw(Graphics2D g) {
    if (this.flinching) {
      if (this.flinchCount % 4 < 2)
        return;
    }
    setMapPosition();
    if (this.facingRight) {
      g.drawImage(this.animation.getImage(), (int) (this.x + this.xmap - (this.width * 0.75)),
          (int) (this.y + this.ymap - this.height * 0.75), null);
    } else {
      g.drawImage(this.animation.getImage(), (int) (this.x + this.xmap + (this.width * 0.25)),
          (int) (this.y + this.ymap - (this.height * 0.75)), -this.width, this.height, null);

    }
    debugDraw(g);
  }

}
