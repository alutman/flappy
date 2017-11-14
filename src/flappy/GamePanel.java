package flappy;

import flappy.entity.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Panel for drawing the game
 */
public class GamePanel extends JPanel {
    private FlappyBird flappyBird;
    private Font pauseFont;
    private final Color bg = new Color(111, 186, 223);

    private ArrayList<GameEntity> entities = new ArrayList<GameEntity>();

    public GamePanel(FlappyBird flappyBird, ArrayList<GameEntity> entities) {
        this.flappyBird = flappyBird;
        this.entities = entities;
        pauseFont = new Font("Arial", Font.BOLD, 48);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        // background color
        g2d.setColor(bg);
        g.fillRect(0,0,FlappyBird.WIDTH,FlappyBird.HEIGHT);

        for(GameEntity go : entities) {
            go.paint(g2d);
        }


        // Flappy bird has a faint yellowy haze over it
        g.setColor(new Color(230, 220, 0, 30));
        g.fillRect(0, 0, FlappyBird.WIDTH, FlappyBird.HEIGHT);

        if(Settings.GOD_MODE) {
            TextWriter.writeText(g2d,
                    20, 20,
                    pauseFont.deriveFont(20f), new Color(0,0,0,200), new Color(0, 0, 0, 0),
                    "God Mode");
        }

        if(!flappyBird.hasGameStarted()) {
            TextWriter.writeText(g2d,
                    FlappyBird.WIDTH/2-175, FlappyBird.HEIGHT/2,
                    pauseFont, new Color(0,0,0,200), new Color(0, 0, 0, 0),
                    "CLICK TO BEGIN");
        }
        g.dispose();

    }
}