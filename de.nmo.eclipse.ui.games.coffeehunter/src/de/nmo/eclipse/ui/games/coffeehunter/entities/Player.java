package de.nmo.eclipse.ui.games.coffeehunter.entities;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.nmo.eclipse.ui.games.coffeehunter.GamePanel;
import de.nmo.eclipse.ui.games.coffeehunter.audio.JukeBox;
import de.nmo.eclipse.ui.games.coffeehunter.entities.goddies.GoddieBlock;
import de.nmo.eclipse.ui.games.coffeehunter.entities.goddies.Goodie;
import de.nmo.eclipse.ui.games.coffeehunter.tilemap.TileMap;

public class Player extends MapObject {

  // player stuff
  private int                        health;
  private int                        maxHealth;
  private int                        fire;
  private int                        maxFire;
  private boolean                    dead;
  private boolean                    flinching;
  private long                       flinchTimer;


  // fireball
  private boolean                    firing;
  private int                        fireCost;
  private int                        fireBallDamage;
  private List<FireBall>             fireBalls;

  // scratch
  private boolean                    scratching;
  private int                        scratchDamage;
  private int                        scratchRange;

  // gliding
  private boolean                    gliding;
  private boolean                    canGliding = false;

  // animations
  private ArrayList<BufferedImage[]> sprites;
  private final int[]                numFrames;

  private List<Enemy>                knownEnemies;

  // animation actions
  private static final int           IDLE       = 0;
  private static final int           WALKING    = 1;
  private static final int           JUMPING    = 2;
  private static final int           FALLING    = 3;
  private static final int           GLIDING    = 4;
  private static final int           FIREBALL   = 5;
  private static final int           SCRATCHING = 6;
  private static final int           DYING      = 7;

  public Player(TileMap tm, String id) {

    super(tm);

    PlayerStats stats = PlayerFactory.getPlayer(id);

    this.numFrames = stats.getNumFrames();

    this.width = stats.getWidth();
    this.height = stats.getHeight();
    this.cwidth = stats.getCWidth();
    this.cheight = stats.getCHeight();

    this.moveSpeed = stats.getMoveSpeed();
    this.maxSpeed = stats.getMaxSpeed();
    this.stopSpeed = stats.getStopSpeed();
    this.fallSpeed = stats.getFallSpeed();
    this.maxFallSpeed = stats.getMaxFallSpeed();
    this.jumpStart = stats.getJumpStart();
    this.stopJumpSpeed = stats.getStopJumpSpeed();

    this.facingRight = true;

    this.health = this.maxHealth = stats.getMaxHealth();
    this.fire = this.maxFire = stats.getMaxFire();

    this.fireCost = stats.getFireCost();
    this.fireBallDamage = stats.getFireBallDamage();
    this.fireBalls = new ArrayList<FireBall>();

    this.scratchDamage = stats.getScratchDamage();
    this.scratchRange = stats.getScratchRange();

    // load sprites
    try {

      BufferedImage spritesheet = stats.getImage();

      this.sprites = new ArrayList<BufferedImage[]>();
      for (int i = 0; i < 8; i++) {

        BufferedImage[] bi = new BufferedImage[this.numFrames[i]];

        for (int j = 0; j < this.numFrames[i]; j++) {

          bi[j] = spritesheet.getSubimage(j * this.width, i * this.height, this.width, this.height);

        }

        this.sprites.add(bi);

      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    this.animation = new Animation();
    this.currentAction = IDLE;
    this.animation.setFrames(this.sprites.get(IDLE));
    this.animation.setDelay(400);

    this.knownEnemies = new ArrayList<>();

    JukeBox.load("/SFX/playerjump.mp3", "playerjump");
    JukeBox.load("/SFX/playercharge.mp3", "playercharge");
    JukeBox.load("/SFX/playerattack.mp3", "playerattack");

  }

  public int getHealth() {
    return this.health;
  }

  public int getMaxHealth() {
    return this.maxHealth;
  }

  public int getFire() {
    return this.fire;
  }

  public int getMaxFire() {
    return this.maxFire;
  }

  public void setFiring() {
    JukeBox.play("playercharge");
    this.firing = true;
  }

  public void setScratching() {
    JukeBox.play("playerattack");
    this.scratching = true;
  }

  public void setGliding(boolean b) {
    this.gliding = this.canGliding && b;
  }

  public Rectangle getRectangle() {
    int offset = (width - cwidth) / 2;
    return new Rectangle((int) x - cwidth + offset, (int) y - cheight + (height - cheight), cwidth, cheight);
  }

  protected void getNextPosition() {

    super.getNextPosition();

    // cannot move while attacking, except in air
    if ((this.currentAction == SCRATCHING || this.currentAction == FIREBALL) && !(this.jumping || this.falling)) {
      this.dx = 0;
    }

    // jumping
    if (this.jumping && !this.falling) {
      this.dy = this.jumpStart;
      this.falling = true;
    }

    // falling
    if (this.falling) {

      if (this.dy > 0 && this.gliding)
        this.dy += this.fallSpeed * 0.1;
      else
        this.dy += this.fallSpeed;

      if (this.dy > 0)
        this.jumping = false;
      if (this.dy < 0 && !this.jumping)
        this.dy += this.stopJumpSpeed;

      if (this.dy > this.maxFallSpeed)
        this.dy = this.maxFallSpeed;

    }

  }

  public void update() {

    // update position
    getNextPosition();
    checkTileMapCollision();
    checkEnemyCollision();
    setPosition(this.xtemp, this.ytemp);


    if (this.currentAction == SCRATCHING) {
      if (this.animation.hasPlayedOnce()) {
        this.scratching = false;
      }
    }
    if (this.currentAction == FIREBALL) {
      if (this.animation.hasPlayedOnce()) {
        this.firing = false;
      }
    }

    //fireball attack
    this.fire += 1;
    if (this.fire > this.maxFire) {
      this.fire = this.maxFire;
    }
    if (this.firing && this.currentAction != FIREBALL) {
      if (this.fire > this.fireCost) {
        this.fire -= this.fireCost;
        FireBall ball = new FireBall(this.tileMap, this.facingRight);
        ball.setPosition(this.x, this.y);
        this.fireBalls.add(ball);
      }
    }
    List<FireBall> todel = new ArrayList<>();
    for (FireBall ball : this.fireBalls) {
      ball.update();
    }
    this.fireBalls.removeAll(this.fireBalls.stream().filter(f -> f.shouldRemove()).collect(Collectors.toList()));

    if (this.flinching) {
      long eleapsed = (System.nanoTime() - this.flinchTimer) / 1000000;
      if (eleapsed > 1000) {
        this.flinching = false;
      }
    }

    // set animation
    if (this.dead) {
      if (this.currentAction != DYING) {
        this.flinching = false;
        this.currentAction = DYING;
        this.animation.setFrame(-1);
        this.animation.setFrames(this.sprites.get(DYING));
        this.animation.setDelay(100);
      }
      if (this.animation.hasPlayedOnce()) {
        try {
          Thread.sleep(2000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        this.reset();
        this.setPosition(100, 100);
        return;
      }
    } else if (this.scratching) {
      if (this.currentAction != SCRATCHING) {
        this.currentAction = SCRATCHING;
        this.animation.setFrames(this.sprites.get(SCRATCHING));
        this.animation.setDelay(40);
        //this.width = 60;
      }
    } else if (this.firing) {
      if (this.currentAction != FIREBALL) {
        this.currentAction = FIREBALL;
        this.animation.setFrames(this.sprites.get(FIREBALL));
        this.animation.setDelay(100);
        //this.width = 30;
      }
    } else if (this.dy > 0) {
      if (this.gliding) {
        if (this.currentAction != GLIDING) {
          this.currentAction = GLIDING;
          this.animation.setFrames(this.sprites.get(GLIDING));
          this.animation.setDelay(100);
          //this.width = 30;
        }
      } else if (this.currentAction != FALLING) {
        this.currentAction = FALLING;
        this.animation.setFrames(this.sprites.get(FALLING));
        this.animation.setDelay(100);
        //this.width = 30;
      }
    } else if (this.dy < 0) {
      if (this.currentAction != JUMPING) {
        JukeBox.play("playerjump");
        this.currentAction = JUMPING;
        this.animation.setFrames(this.sprites.get(JUMPING));
        this.animation.setDelay(-1);
        //this.width = 30;
      }
    } else if (this.left || this.right) {
      if (this.currentAction != WALKING) {
        this.currentAction = WALKING;
        this.animation.setFrames(this.sprites.get(WALKING));
        this.animation.setDelay(100);
        //this.width = 30;
      }
    } else {
      if (this.currentAction != IDLE) {
        this.currentAction = IDLE;
        this.animation.setFrames(this.sprites.get(IDLE));
        this.animation.setDelay(400);
        //this.width = 30;
      }
    }

    this.animation.update();

    // set direction
    if (this.currentAction != SCRATCHING && this.currentAction != FIREBALL) {
      if (this.right)
        this.facingRight = true;
      if (this.left)
        this.facingRight = false;
    }

  }

  /**
   * 
   *
   * @author Bergen, Marco (I-EA-25, extern)
   * @since 28.07.2017
   */
  private void checkEnemyCollision() {
    double[] vector = null;
    for (Enemy e : this.knownEnemies) {
      if (GamePanel.onScreen(this.tileMap, e) && intersects(e)) {
        vector = e.checkPlayerCollision(this);
        if (vector != null && vector.length == 2) {
          this.xtemp = vector[0];
          this.ytemp = vector[1];
        }
      }
    }
  }

  public void draw(Graphics2D g) {

    setMapPosition();

    for (FireBall ball : this.fireBalls) {
      ball.draw(g);
    }

    // draw player
    if (this.flinching) {
      long elapsed = (System.nanoTime() - this.flinchTimer) / 1000000;
      if (elapsed / 100 % 2 == 0) {
        return;
      }
    }

    super.draw(g);

  }

  /**
   * @param enemies
   *
   * @author Bergen, Marco (I-EA-25, extern)
   * @since 27.07.2017
   */
  public void checkAttack(List<Enemy> enemies) {

    for (int i = 0; i < enemies.size(); i++) {
      Enemy e = enemies.get(i);
      if (this.scratching) {
        if (this.facingRight) {
          if (e.getx() > this.x && e.getx() < this.x + this.scratchRange && e.gety() > this.y - this.height / 2
              && e.gety() < this.y + this.height / 2) {
            e.hit(this.scratchDamage);
          }
        } else {
          if (e.getx() < this.x && e.getx() > this.x - this.scratchRange && e.gety() > this.y - this.height / 2
              && e.gety() < this.y + this.height / 2) {
            e.hit(this.scratchDamage);
          }
        }
      }
      for (int j = 0; j < this.fireBalls.size(); j++) {
        FireBall f = this.fireBalls.get(j);
        if (f.intersects(e)) {
          e.hit(this.fireBallDamage);
          f.setHit();
        }
      }

      if (intersects(e)) {
        e.intersectEvent(this);
        if (e.getDamage() > 0) {
          hit(e.getDamage());
        }
      }
      if (e instanceof GoddieBlock) {
        Goodie g = ((GoddieBlock) e).getGoodie();
        if (g != null && intersects(g)) {
          g.intersectEvent(this);
        }
      }

    }

  }

  public void hit(int damage) {
    if (this.dead || this.flinching) {
      return;
    }
    this.health -= damage;
    if (this.health < 0) {
      this.health = 0;
    }
    if (this.health == 0) {
      this.dead = true;
    }
    this.flinching = true;
    this.flinchTimer = System.nanoTime();
  }

  public void reset() {
    this.health = this.maxHealth;
    this.facingRight = true;
    this.currentAction = -1;
    stop();
  }

  public void stop() {
    this.left = this.right = this.up = this.down = this.flinching = this.jumping = this.dead = false;
  }

  /**
   * @param health Setzt den Wert von health.
   */
  public void setHealth(int health) {
    this.health = health;
  }

  /**
   * @param maxHealth Setzt den Wert von maxHealth.
   */
  public void setMaxHealth(int maxHealth) {
    this.maxHealth = maxHealth;
  }

  /**
   * @param fire Setzt den Wert von fire.
   */
  public void setFire(int fire) {
    this.fire = fire;
  }

  /**
   * @param maxFire Setzt den Wert von maxFire.
   */
  public void setMaxFire(int maxFire) {
    this.maxFire = maxFire;
  }

  public void setGlid(boolean cf) {
    this.canGliding = cf;
  }

  public List<Enemy> getKnownEnemies() {
    return this.knownEnemies;
  }

}
