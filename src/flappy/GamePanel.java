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
    private final Color bgLight = new Color(211, 217, 222);
    private final Color bg = new Color(0x79, 0xB0, 0xDF);

    private ArrayList<GameEntity> entities = new ArrayList<GameEntity>();

    public GamePanel(FlappyBird flappyBird, ArrayList<GameEntity> entities) {
        this.flappyBird = flappyBird;
        this.entities = entities;
        pauseFont = new Font("Arial", Font.BOLD, 48);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // background color
        int dist = FlappyBird.HEIGHT/2;
        g2d.setPaint(new GradientPaint(FlappyBird.WIDTH-50,50, bgLight, FlappyBird.WIDTH-dist, dist, bg));
        g.fillRect(0,0,FlappyBird.WIDTH,FlappyBird.HEIGHT);

        for(GameEntity go : entities) {
            go.paint(g2d);
        }

        if(flappyBird.paused()) {
            TextWriter.writeText(g2d,
                    FlappyBird.WIDTH/2-100, FlappyBird.HEIGHT/2-100,
                    pauseFont, new Color(0,0,0,200), new Color(0, 0, 0, 0),
                    "PAUSED");
            TextWriter.writeText(g2d,
                    FlappyBird.WIDTH/2-175, FlappyBird.HEIGHT/2+50,
                    pauseFont, new Color(0,0,0,200), new Color(0, 0, 0, 0),
                    "CLICK TO BEGIN");
        }

    }
}