package de.nmo.eclipse.ui.games.coffeehunter.gamestate;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import de.nmo.eclipse.ui.games.coffeehunter.GamePanel;
import de.nmo.eclipse.ui.games.coffeehunter.audio.JukeBox;
import de.nmo.eclipse.ui.games.coffeehunter.entities.Enemy;
import de.nmo.eclipse.ui.games.coffeehunter.entities.Player;
import de.nmo.eclipse.ui.games.coffeehunter.entities.Teleport;
import de.nmo.eclipse.ui.games.coffeehunter.entities.Title;
import de.nmo.eclipse.ui.games.coffeehunter.entities.TriggerPoint;
import de.nmo.eclipse.ui.games.coffeehunter.entities.enemies.EnemyApplicationTree;
import de.nmo.eclipse.ui.games.coffeehunter.entities.enemies.EnemyDebugger;
import de.nmo.eclipse.ui.games.coffeehunter.entities.enemies.EnemyPushBar;
import de.nmo.eclipse.ui.games.coffeehunter.entities.enemies.EnemySaw;
import de.nmo.eclipse.ui.games.coffeehunter.entities.enemies.EnemySpirit;
import de.nmo.eclipse.ui.games.coffeehunter.entities.goddies.GoddieBlock;
import de.nmo.eclipse.ui.games.coffeehunter.entities.goddies.GoodieCoffee;
import de.nmo.eclipse.ui.games.coffeehunter.entities.goddies.GoodieFeather;
import de.nmo.eclipse.ui.games.coffeehunter.tilemap.Background;
import de.nmo.eclipse.ui.games.coffeehunter.tilemap.Tile;
import de.nmo.eclipse.ui.games.coffeehunter.tilemap.TileMap;

public class Level1State extends AbstractLevelGameState {

  public Level1State(GameStateManager gsm) {
    super(gsm);
    // sfx
    JukeBox.load("/SFX/teleport.mp3", "teleport");
    JukeBox.load("/SFX/explode.mp3", "explode");
    JukeBox.load("/SFX/enemyhit.mp3", "enemyhit");

    // music
    JukeBox.load("/Music/level1.mp3", "level1");
    JukeBox.load("/Music/level1boss.mp3", "level1boss");
    JukeBox.loop("level1", 600, JukeBox.getFrames("level1") - 2200);
    init();

  }

  @Override
  protected TileMap createTileMap() {
    TileMap tm = new TileMap(30);
    tm.loadTiles("/Tilesets/eclipse.png");
    tm.loadMap("/Maps/level1.map");
    tm.setPosition(0, 0);
    tm.setTween(1);
    return tm;
  }

  @Override
  protected Player createPlayer() {
    Player p = new Player(this.tileMap, "playertwo");
    p.setPosition(60, 100);
    return p;
  }

  @Override
  protected Background createBackground() {
    return new Background("/Backgrounds/level1bg.gif", 0.1);
  }

  @Override
  protected List<Enemy> createEnemies() {
    List<Enemy> es = new ArrayList<>();
    es.add(new EnemyDebugger(this.tileMap, 40, 290, 190));
    es.add(new EnemyDebugger(this.tileMap, 210, 380, 100));
    es.add(new EnemyDebugger(this.tileMap, 95, 500, 50));
    es.add(new EnemyDebugger(this.tileMap, 240, 530, 50));
    es.add(new EnemyDebugger(this.tileMap, 125, 580, 200));
    EnemyDebugger e1 = new EnemyDebugger(this.tileMap, 125, 580, 200);
    e1.setPosition(200, 580);
    es.add(e1);
    es.add(new EnemyDebugger(this.tileMap, 210, 710, 30));
    es.add(new EnemyDebugger(this.tileMap, 40, 770, 200));
    EnemyDebugger e2 = new EnemyDebugger(this.tileMap, 40, 770, 300);
    e2.setPosition(100, 770);
    es.add(e2);
    EnemyDebugger e3 = new EnemyDebugger(this.tileMap, 40, 770, 300);
    e3.setPosition(200, 770);
    es.add(e3);
    EnemyDebugger e4 = new EnemyDebugger(this.tileMap, 40, 770, 300);
    e4.setPosition(300, 770);
    es.add(e4);

    int sawline1_y = 770;
    es.add(new EnemySaw(this.tileMap, 370, sawline1_y, 500, false));

    EnemySaw s2 = new EnemySaw(this.tileMap, 370, sawline1_y, 500, false);
    s2.setPosition(520, sawline1_y);
    es.add(s2);

    EnemySaw s3 = new EnemySaw(this.tileMap, 370, sawline1_y, 500, false);
    s3.setPosition(670, sawline1_y);
    es.add(s3);

    int sawline2_y = 650;
    int range = 300;
    EnemySaw s4 = new EnemySaw(this.tileMap, 400, sawline2_y, range, false);
    //s4.setPosition(670, sawline2_y);
    es.add(s4);

    EnemySaw s5 = new EnemySaw(this.tileMap, 400, sawline2_y, range, false);
    s5.setPosition(550, sawline2_y);
    es.add(s5);

    EnemySaw s6 = new EnemySaw(this.tileMap, 400, sawline2_y, range, false);
    s6.setPosition(700, sawline2_y);
    es.add(s6);

    int sawline3_y = 580;
    EnemySaw s7 = new EnemySaw(this.tileMap, 475, sawline3_y, range, true);
    //s7.setPosition(670, sawline3_y);
    es.add(s7);

    EnemySaw s8 = new EnemySaw(this.tileMap, 475, sawline3_y, range, true);
    s8.setPosition(625, sawline3_y);
    es.add(s8);

    EnemySaw s9 = new EnemySaw(this.tileMap, 475, sawline3_y, range, true);
    s9.setPosition(775, sawline3_y);
    es.add(s9);

    es.add(new EnemyApplicationTree(this.tileMap, 550, 145));
    es.add(new EnemyApplicationTree(this.tileMap, 700, 145));

    es.add(new EnemyPushBar(this.tileMap, false, 839, 150, 0));
    es.add(new EnemyPushBar(this.tileMap, true, 811, 210, 2000));
    es.add(new EnemyPushBar(this.tileMap, false, 839, 270, 0));

    final EnemySpirit spirit = new EnemySpirit(this.tileMap, this.player, this.enemies, this.explosions, 555, 390);
    spirit.setFinishTrigger((enemy) -> {
      tileMap.setImage(14, 23, Tile.NORMAL, 6);
      enemies.add(new Teleport(this.tileMap, this.player, 600, 430, 1250, 250));
      JukeBox.stop("level1boss");
      JukeBox.loop("level1", 600, JukeBox.getFrames("level1boss") - 2200);
    });
    es.add(spirit);
    es.add(new TriggerPoint(this.tileMap, (enemy) -> {
      JukeBox.stop("level1");
      JukeBox.loop("level1boss", 600, JukeBox.getFrames("level1boss") - 2200);
      spirit.setActive();
      enemies.remove(enemy);
      tileMap.setImage(14, 23, Tile.BLOCKED, 56);
    }, 690, 450));

    es.add(new GoddieBlock(this.tileMap, 960, 721, new GoodieFeather(this.tileMap)));
    es.add(new GoddieBlock(this.tileMap, 780, 421, new GoodieCoffee(this.tileMap)));

    //Teleport to Boss Fight
    //es.add(new Teleport(this.tileMap, this.player, 100, 100, 800, 420));

    //Teleport to End
    //es.add(new Teleport(this.tileMap, this.player, 100, 100, 1250, 250));

    es.add(new TriggerPoint(this.tileMap, (enemy) -> {
      enemies.remove(enemy);
      gsm.setState(GameStateManager.LEVEL2STATE);
    }, 1370, 300));

    return es;
  }

  @Override
  protected Title createTitle() {
    // title and subtitle
    try {
      this.titleImage = ImageIO.read(getClass().getResourceAsStream("/HUD/level1.png"));
      this.title = new Title(this.titleImage.getSubimage(0, 0, 178, 46));
      this.title.sety(60);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return this.title;
  }

  //level started
  protected void eventStart() {
    this.eventCount++;
    if (this.eventCount == 1) {
      this.tb.clear();
      this.tb.add(new Rectangle(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT / 2));
      this.tb.add(new Rectangle(0, 0, GamePanel.WIDTH / 2, GamePanel.HEIGHT));
      this.tb.add(new Rectangle(0, GamePanel.HEIGHT / 2, GamePanel.WIDTH, GamePanel.HEIGHT / 2));
      this.tb.add(new Rectangle(GamePanel.WIDTH / 2, 0, GamePanel.WIDTH / 2, GamePanel.HEIGHT));
    }
    if (this.eventCount > 1 && this.eventCount < 60) {
      this.tb.get(0).height -= 4;
      this.tb.get(1).width -= 6;
      this.tb.get(2).y += 4;
      this.tb.get(3).x += 6;
    }
    if (this.eventCount == 30)
      this.title.begin();
    if (this.eventCount == 60) {
      this.eventStart = this.blockInput = false;
      this.eventCount = 0;
      this.tb.clear();
    }
  }

  // player has died
  protected void eventDead() {
  }

  // finished level
  protected void eventFinish() {

  }

  private void reset() {
    this.player.reset();
    this.player.setPosition(300, 161);
    this.enemies = createEnemies();
    this.blockInput = true;
    this.eventCount = 0;
    this.eventStart = true;
    eventStart();
    this.title = new Title(this.titleImage.getSubimage(0, 0, 178, 20));
    this.title.sety(60);
  }

}
