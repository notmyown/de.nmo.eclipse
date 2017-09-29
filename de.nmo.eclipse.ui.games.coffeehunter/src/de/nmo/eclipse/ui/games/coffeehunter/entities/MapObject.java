package de.nmo.eclipse.ui.games.coffeehunter.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import de.nmo.eclipse.ui.games.coffeehunter.GamePanel;
import de.nmo.eclipse.ui.games.coffeehunter.tilemap.Tile;
import de.nmo.eclipse.ui.games.coffeehunter.tilemap.TileMap;

public abstract class MapObject {

  // tile stuff
  protected TileMap   tileMap;
  protected int       tileSize;
  protected double    xmap;
  protected double    ymap;

  // position and vector
  protected double    x;
  protected double    y;
  protected double    dx;
  protected double    dy;

  // dimensions
  protected int       width;
  protected int       height;

  // collision box
  protected int       cwidth;
  protected int       cheight;

  // collision
  protected int       currRow;
  protected int       currCol;
  protected double    xdest;
  protected double    ydest;
  protected double    xtemp;
  protected double    ytemp;
  protected boolean   topLeft;
  protected boolean   topRight;
  protected boolean   bottomLeft;
  protected boolean   bottomRight;
  protected boolean   curLeft;
  protected boolean   curRight;
  protected boolean   curTop;
  protected boolean   curBottom;

  // animation
  protected Animation animation;
  protected int       currentAction;
  protected int       previousAction;
  protected boolean   facingRight;
  protected boolean   facingDown = false;

  // movement
  protected boolean   left;
  protected boolean   right;
  protected boolean   up;
  protected boolean   down;
  protected boolean   jumping;
  protected boolean   falling;

  // movement attributes
  protected double    moveSpeed;
  protected double    maxSpeed;
  protected double    stopSpeed;
  protected double    fallSpeed;
  protected double    maxFallSpeed;
  protected double    jumpStart;
  protected double    stopJumpSpeed;

  // constructor
  public MapObject(TileMap tm) {
    tileMap = tm;
    tileSize = tm.getTileSize();
  }

  public boolean intersects(MapObject o) {
    Rectangle r1 = getRectangle();
    Rectangle r2 = o.getRectangle();
    return r1.intersects(r2);
  }

  public Rectangle getRectangle() {
    return new Rectangle((int) x - cwidth, (int) y - cheight, cwidth, cheight);
  }

  public Rectangle getObjectRectangle() {
    return new Rectangle((int) x - cwidth, (int) y - cheight, width, height);
  }

  public void calculateCorners(double x, double y) {

    int leftTile = (int) (x - cwidth / 2) / tileSize;
    int rightTile = (int) (x + cwidth / 2 - 1) / tileSize;
    int topTile = (int) (y - cheight / 2) / tileSize;
    int bottomTile = (int) (y + cheight / 2 - 1) / tileSize;
    int curHorizontal = (int) (x) / tileSize;
    int curVertical = (int) (y) / tileSize;

    int tl = tileMap.getType(topTile, leftTile);
    int tr = tileMap.getType(topTile, rightTile);
    int bl = tileMap.getType(bottomTile, leftTile);
    int br = tileMap.getType(bottomTile, rightTile);
    int chl = tileMap.getType(curVertical, leftTile);
    int chr = tileMap.getType(curVertical, rightTile);
    int cvt = tileMap.getType(topTile, curHorizontal);
    int cvb = tileMap.getType(bottomTile, curHorizontal);

    topLeft = tl == Tile.BLOCKED;
    topRight = tr == Tile.BLOCKED;
    bottomLeft = bl == Tile.BLOCKED;
    bottomRight = br == Tile.BLOCKED;
    curLeft = chl == Tile.BLOCKED;
    curRight = chr == Tile.BLOCKED;
    curTop = cvt == Tile.BLOCKED;
    curBottom = cvb == Tile.BLOCKED;

  }

  public void checkTileMapCollision() {

    currCol = (int) x / tileSize;
    currRow = (int) y / tileSize;

    xdest = x + dx;
    ydest = y + dy;

    xtemp = x;
    ytemp = y;

    calculateCorners(x, ydest);
    if (dy < 0) {
      if (topLeft || topRight) {
        dy = 0;
        ytemp = currRow * tileSize + cheight / 2;
      } else {
        ytemp += dy;
      }
    }
    if (dy > 0) {
      if (bottomLeft || bottomRight) {
        dy = 0;
        falling = false;
        ytemp = (currRow + 1) * tileSize - cheight / 2;
      } else {
        ytemp += dy;
      }
    }

    calculateCorners(xdest, y);
    if (dx < 0) {
      if (topLeft || bottomLeft) {
        dx = 0;
        xtemp = currCol * tileSize + cwidth / 2;
      } else {
        xtemp += dx;
      }
    }
    if (dx > 0) {
      if (topRight || bottomRight) {
        dx = 0;
        xtemp = (currCol + 1) * tileSize - cwidth / 2;
      } else {
        xtemp += dx;
      }
    }

    if (!falling) {
      calculateCorners(x, ydest + 1);
      if (!bottomLeft && !bottomRight) {
        falling = true;
      }
    }

  }

  public int getx() {
    return (int) x;
  }

  public int gety() {
    return (int) y;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public int getCWidth() {
    return cwidth;
  }

  public int getCHeight() {
    return cheight;
  }

  public void setPosition(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public void setVector(double dx, double dy) {
    this.dx = dx;
    this.dy = dy;
  }

  public double[] getVector() {
    return new double[] { dx, dy };
  }

  public void setMapPosition() {
    xmap = tileMap.getx();
    ymap = tileMap.gety();
  }

  public void setLeft(boolean b) {
    left = b;
  }

  public void setRight(boolean b) {
    right = b;
  }

  public void setUp(boolean b) {
    up = b;
  }

  public void setDown(boolean b) {
    down = b;
  }

  public void setJumping(boolean b) {
    jumping = b;
  }

  protected void getNextPosition() {
    // movement
    if (this.left) {
      this.dx -= this.moveSpeed;
      if (this.dx < -this.maxSpeed) {
        this.dx = -this.maxSpeed;
      }
    } else if (this.right) {
      this.dx += this.moveSpeed;
      if (this.dx > this.maxSpeed) {
        this.dx = this.maxSpeed;
      }
    } else {
      if (this.dx > 0) {
        this.dx -= this.stopSpeed;
        if (this.dx < 0) {
          this.dx = 0;
        }
      } else if (this.dx < 0) {
        this.dx += this.stopSpeed;
        if (this.dx > 0) {
          this.dx = 0;
        }
      }
    }
  }

  public boolean notOnScreen() {
    return x + xmap + width < 0 || x + xmap - width > GamePanel.WIDTH || y + ymap + height < 0
        || y + ymap - height > GamePanel.HEIGHT;
  }

  public void draw(Graphics2D g) {
    if (!this.facingDown) {
      if (this.facingRight) {
        g.drawImage(this.animation.getImage(), (int) (this.x + this.xmap - this.width / 2),
            (int) (this.y + this.ymap - this.height / 2), null);
      } else {
        g.drawImage(this.animation.getImage(), (int) (this.x + this.xmap - this.width / 2 + this.width),
            (int) (this.y + this.ymap - this.height / 2), -this.width, this.height, null);

      }
    } else {
      if (this.facingRight) {
        g.drawImage(this.animation.getImage(), (int) (this.x + this.xmap - this.width + this.width),
            (int) (this.y + this.ymap + this.height / 2), this.width, -this.height, null);
      } else {
        g.drawImage(this.animation.getImage(), (int) (this.x + this.xmap - this.width / 2 + this.width),
            (int) (this.y + this.ymap + this.height / 2), -this.width, -this.height, null);

      }
    }
    debugDraw(g);
  }

  /**
   * @param g
   *
   * @author TJNKXAO
   * @since  30.08.2017
   */
  public void debugDraw(Graphics2D g) {
    if (GamePanel.isDebug()) {
      Rectangle rect = getRectangle();
      g.setColor(Color.BLUE);
      g.drawRect((int) (rect.getX() + xmap), (int) (rect.getY() + ymap), (int) rect.getWidth(), (int) rect.getHeight());
      rect = getObjectRectangle();
      g.setColor(Color.RED);
      g.drawRect((int) (rect.getX() + xmap), (int) (rect.getY() + ymap), (int) rect.getWidth(), (int) rect.getHeight());
      g.setColor(Color.BLACK);
    }
  }

  public void intersectEvent(Player p) {

  }

}
