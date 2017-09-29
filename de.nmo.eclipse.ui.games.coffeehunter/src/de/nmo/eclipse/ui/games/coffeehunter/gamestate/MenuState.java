package de.nmo.eclipse.ui.games.coffeehunter.gamestate;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import de.nmo.eclipse.ui.games.coffeehunter.GamePanel;
import de.nmo.eclipse.ui.games.coffeehunter.audio.JukeBox;
import de.nmo.eclipse.ui.games.coffeehunter.gamestate.handler.Keys;
import de.nmo.eclipse.ui.games.coffeehunter.tilemap.Background;

public class MenuState extends GameState {

  protected Background bg;
  protected Background bg2;

  protected int        currentChoice = 0;
  protected String[]   options       = { "Start", "Password", "Options", "Quit" };

  protected Color      titleColor;
  protected Font       titleFont;

  protected Font       font;

  protected long       lastKey       = 0;

  public MenuState(GameStateManager gsm) {

    super(gsm);

    try {

      bg = new Background("/Backgrounds/menubg_coffeehunter.png", 1);
      bg.setVector(0, 0);
      bg2 = new Background("/Backgrounds/menubg_coffeehunter_overlay.png", 1);
      bg2.setVector(-0.3, 0);

      titleColor = new Color(128, 0, 0);
      titleFont = new Font("Century Gothic", Font.PLAIN, 28);

      font = new Font("Arial", Font.PLAIN, 12);

    } catch (Exception e) {
      e.printStackTrace();
    }
    init();

  }

  public void init() {
    JukeBox.load("/SFX/menuselect.mp3", "menuselect");
    JukeBox.load("/SFX/menuoption.mp3", "menuoption");
  }

  public void update() {
    bg.update();
    bg2.update();
    // check keys
    if (System.currentTimeMillis() - lastKey > 500) {
    handleInput();
    }
  }

  public void draw(Graphics2D g) {

    // draw bg
    bg.draw(g);
    bg2.draw(g);

    // draw title
    //    g.setColor(titleColor);
    //    g.setFont(titleFont);
    //    g.drawString("Dragon Tale", 80, 70);

    // draw menu options
    g.setFont(font);
    for (int i = 0; i < options.length; i++) {
      if (i == currentChoice) {
        g.setColor(Color.WHITE);
      } else {
        g.setColor(Color.YELLOW);
      }
      g.drawString(options[i], 145, 160 + i * 15);
    }

  }

  protected void select() {
    JukeBox.play("menuselect");
    if (currentChoice == 0) {
      gsm.setState(GameStateManager.LEVEL1STATE);
    }
    if (currentChoice == 1) {
      gsm.setState(GameStateManager.PASSWORDSTATE);
    }
    if (currentChoice == 2) {
      // Options
    }
    if (currentChoice == 3) {
      GamePanel.exit();
    }
  }

  public void handleInput() {
    if (Keys.anyKeyPress()) {
      lastKey = System.currentTimeMillis();
    }
    if (Keys.isPressed(Keys.ENTER)) {
      select();
    }
    if (Keys.isPressed(Keys.UP)) {
      if (currentChoice > 0) {
        JukeBox.play("menuoption");
        currentChoice--;
        return;
      }
    }
    if (Keys.isPressed(Keys.DOWN)) {
      if (currentChoice < options.length - 1) {
        JukeBox.play("menuoption");
        currentChoice++;
        return;
      }
    }
    Keys.update();
  }

}
