package de.nmo.eclipse.ui.games.coffeehunter.gamestate;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import de.nmo.eclipse.ui.games.coffeehunter.audio.JukeBox;

public class PasswordState extends MenuState implements KeyListener {

  String password = "";

  public PasswordState(GameStateManager gsm) {
    super(gsm);
    this.options = new String[] { "", "Back" };
    gsm.panel.addKeyListener(this);
  }

  public void draw(Graphics2D g) {
    super.draw(g);
    if (this.currentChoice == 0) {
      g.setColor(Color.WHITE);
    } else {
      g.setColor(Color.YELLOW);
    }
    g.drawRect(120, 140, 80, 20);
    g.drawString(password, 125, 155);
  }

  protected void select() {
    JukeBox.play("menuselect");
    if (this.currentChoice == 0) {
      if ("Bohne".equals(this.password)) {
        this.gsm.panel.removeKeyListener(this);
        this.gsm.setState(GameStateManager.LEVEL1STATE);
        return;
      }
      if ("Röstung".equals(this.password)) {
        this.gsm.panel.removeKeyListener(this);
        this.gsm.setState(GameStateManager.LEVEL2STATE);
        return;
      }
      if ("Sandbox".equals(this.password)) {
        this.gsm.panel.removeKeyListener(this);
        System.setProperty("ch.debug", "true");
        this.gsm.setState(GameStateManager.SANDBOXSTATE);
        return;
      }
      if ("DebugON".equals(this.password)) {
        System.setProperty("ch.debug", "true");
      }
      if ("DebugOFF".equals(this.password)) {
        System.setProperty("ch.debug", "false");
      }
      this.password = "";
      return;
    }
    if (this.currentChoice == 1) {
      this.gsm.setState(GameStateManager.MENUSTATE);
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void keyPressed(KeyEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void keyReleased(KeyEvent e) {
    if (this.currentChoice == 0) {
      if (e.getKeyChar() == '\b' && this.password.length() > 0) {
        this.password = this.password.substring(0, this.password.length() -1);
      }
      if (Character.isLetter(e.getKeyChar()) || Character.isDigit(e.getKeyChar())) {
        this.password += e.getKeyChar();
      }
    }
  }

}
