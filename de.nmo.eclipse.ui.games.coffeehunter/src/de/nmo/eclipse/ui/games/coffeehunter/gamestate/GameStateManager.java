package de.nmo.eclipse.ui.games.coffeehunter.gamestate;

import javax.swing.JPanel;

import de.nmo.eclipse.ui.games.coffeehunter.GamePanel;
import de.nmo.eclipse.ui.games.coffeehunter.audio.JukeBox;

public class GameStateManager {

  private GameState[]     gameStates;
  private int             currentState;

  private PauseState      pauseState;
  private boolean         paused;

  public static final int NUMGAMESTATES = 16;
  public static final int MENUSTATE     = 0;
  public static final int PASSWORDSTATE = 1;
  public static final int OPTIONSSTATE  = 2;
  public static final int LEVEL1STATE   = 3;
  public static final int LEVEL2STATE   = 4;

  public JPanel           panel;

  public GameStateManager(JPanel panel) {
    this.panel = panel;
    JukeBox.init();

    this.gameStates = new GameState[NUMGAMESTATES];

    this.pauseState = new PauseState(this);
    this.paused = false;

    this.currentState = MENUSTATE;
    loadState(this.currentState);

  }

  private void loadState(int state) {
    if (state == MENUSTATE)
      this.gameStates[state] = new MenuState(this);
    if (state == PASSWORDSTATE)
      this.gameStates[state] = new PasswordState(this);
    else if (state == LEVEL1STATE)
      this.gameStates[state] = new Level1State(this);
    else if (state == LEVEL2STATE)
      this.gameStates[state] = new Level2State(this);
  }

  private void unloadState(int state) {
    this.gameStates[state] = null;
  }

  public void setState(int state) {
    unloadState(this.currentState);
    this.currentState = state;
    loadState(this.currentState);
  }

  public void setPaused(boolean b) {
    this.paused = b;
  }

  public void update() {
    if (this.paused) {
      this.pauseState.update();
      return;
    }
    if (this.gameStates[this.currentState] != null)
      this.gameStates[this.currentState].update();
  }

  public void draw(java.awt.Graphics2D g) {

    if (this.gameStates[this.currentState] != null) {
      this.gameStates[this.currentState].draw(g);
    }
    else {
      g.setColor(java.awt.Color.BLACK);
      g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
    }
    if (this.paused) {
      this.pauseState.draw(g);
    }
  }

}