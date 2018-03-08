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
import de.nmo.eclipse.ui.games.coffeehunter.entities.goddies.GoddieBlock;
import de.nmo.eclipse.ui.games.coffeehunter.entities.goddies.GoodieCoffee;
import de.nmo.eclipse.ui.games.coffeehunter.entities.goddies.GoodieFeather;
import de.nmo.eclipse.ui.games.coffeehunter.tilemap.Background;
import de.nmo.eclipse.ui.games.coffeehunter.tilemap.TileMap;

public class LevelSandboxState extends AbstractLevelGameState {

  public LevelSandboxState(GameStateManager gsm) {
    super(gsm);
    init();
  }

  @Override
  protected TileMap createTileMap() {
    TileMap tm = new TileMap(30);
    tm.loadTiles("/Tilesets/grasstileset.gif");
    tm.loadMap("/Maps/testlevel.map");
    tm.setPosition(0, 0);
    tm.setTween(1);
    return tm;
  }

  @Override
  protected Player createPlayer() {
    Player p = new Player(this.tileMap, "playerone");
    p.setPosition(60, 100);
    return p;
  }

  @Override
  protected Background createBackground() {
    return new Background("/Backgrounds/menubg.gif", 0.1);
  }

  @Override
  protected List<Enemy> createEnemies() {
    List<Enemy> es = new ArrayList<>();
    es.add(new GoddieBlock(this.tileMap, 200, 370, new GoodieFeather(this.tileMap)));
    es.add(new GoddieBlock(this.tileMap, 300, 370, new GoodieCoffee(this.tileMap)));
    es.add(new Teleport(this.tileMap, this.player, 400, 400, 60, 100));
    es.add(new TriggerPoint(this.tileMap, (enemy) -> {
      JukeBox.stop("level1");
      reset();
    }, 400, 400));

    return es;
  }

  @Override
  protected Title createTitle() {
    // title and subtitle
    try {
      this.titleImage = ImageIO.read(getClass().getResourceAsStream("/HUD/level2.png"));
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
    this.player.setPosition(60, 100);
    this.enemies = createEnemies();
    this.blockInput = true;
    this.eventCount = 0;
    this.eventStart = true;
    eventStart();
    this.title = new Title(this.titleImage.getSubimage(0, 0, 178, 20));
    this.title.sety(60);
  }

}
