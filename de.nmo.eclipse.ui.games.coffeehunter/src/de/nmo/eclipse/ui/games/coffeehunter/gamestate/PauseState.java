package de.nmo.eclipse.ui.games.coffeehunter.gamestate;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import de.nmo.eclipse.ui.games.coffeehunter.gamestate.handler.Keys;

public class PauseState extends GameState {
	
	private Font font;
	
  BufferedImage pauseImage;

	public PauseState(GameStateManager gsm) {
		
		super(gsm);
		
		// fonts
		font = new Font("Century Gothic", Font.PLAIN, 14);
    try {
      pauseImage = ImageIO.read(getClass().getResourceAsStream("/Backgrounds/pause.png"));
    } catch (Exception e) {
      e.printStackTrace();
    }
		
	}
	
	public void init() {}
	
	public void update() {
		handleInput();
	}
	
	public void draw(Graphics2D g) {
    g.drawImage(pauseImage, 0, 0, null);
    //g.setColor(Color.BLACK);
    //g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
    //g.setColor(Color.WHITE);
    //g.setFont(font);
    //g.drawString("Game Paused", 110, 110);
	}
	
	public void handleInput() {
		if(Keys.isPressed(Keys.ESCAPE)) gsm.setPaused(false);
		if(Keys.isPressed(Keys.BUTTON1)) {
			gsm.setPaused(false);
			gsm.setState(GameStateManager.MENUSTATE);
		}
	}

}
