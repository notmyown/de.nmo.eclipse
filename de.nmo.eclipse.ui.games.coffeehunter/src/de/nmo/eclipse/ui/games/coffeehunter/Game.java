package de.nmo.eclipse.ui.games.coffeehunter;

import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;

public class Game {

  public static void main(String[] args) {

    final JFrame window = new JFrame("Coffee Hunter");
    final GamePanel panel = new GamePanel();
    window.setContentPane(panel);
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setResizable(true);
    window.pack();
    window.setVisible(true);

    window.addComponentListener(new ComponentAdapter() {
      public void componentResized(ComponentEvent evt) {
        Component c = (Component) evt.getSource();
        double height = (c.getSize().getHeight());
        panel.SCALE = height / panel.HEIGHT;
      }
    });

  }

}
