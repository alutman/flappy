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
        if(flappyBird.isGameOver()) {
            TextWriter.writeText(g, FlappyBird.WIDTH/2-125, FlappyBird.HEIGHT/2-50,
                    scoreFont, Color.WHITE, Color.BLACK,
                    "GAME OVER");
            TextWriter.writeText(g, FlappyBird.WIDTH/2-125, FlappyBird.HEIGHT/2+20,
                    scoreFont.deriveFont(32f), Color.BLACK, new Color(0,0,0,0),
                    "You scored: ");
            TextWriter.writeText(g, FlappyBird.WIDTH/2+50, FlappyBird.HEIGHT/2+25,
                    scoreFont, Color.WHITE, Color.BLACK,
                    ""+flappyBird.getScore());
        }
        else {
            TextWriter.writeText(g, FlappyBird.WIDTH/2-20, 50, scoreFont, Color.WHITE, Color.BLACK, flappyBird.getScore()+"");
        }

    }

    @Override
    public void reset() {

    }
}
