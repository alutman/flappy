package flappy.entity;

import flappy.FlappyBird;
import flappy.TextWriter;

import java.awt.*;

public class Scoreboard implements GameEntity {
    private Font scoreFont = new Font("Impact", Font.BOLD, 48);
    private FlappyBird flappyBird;
    public Scoreboard(FlappyBird flappyBird) {
        this.flappyBird = flappyBird;
    }
    @Override
    public void tick() {

    }

    @Override
    public void paint(Graphics2D g) {
        TextWriter.writeText(g, FlappyBird.WIDTH/2-20, 50, scoreFont, Color.WHITE, Color.BLACK, flappyBird.getScore()+"");
    }

    @Override
    public void reset() {

    }
}
