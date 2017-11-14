package flappy.entity;

import flappy.FlappyBird;
import flappy.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Line2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static flappy.Settings.*;


/**
 * Creates & moves obstacle pipes
 */
public class Pipes implements GameEntity {

    private ArrayList<Rectangle> rects = new ArrayList<Rectangle>();
    private Image pipeHead, pipeLength;
    private final int PIPE_W = 90, PIPE_H = 60;

    private final int MIN_HEIGHT = 120;
    private final int MAX_HEIGHT = FlappyBird.HEIGHT-100-PIPE_GAP;

    private Random rand = new Random();

    private FlappyBird flappyBird;
    public Pipes(FlappyBird flappyBird) {
        this.flappyBird = flappyBird;
        try {
            pipeHead = ImageIO.read(GamePanel.class.getResourceAsStream("/pipe_end.png"));
            pipeLength = ImageIO.read(GamePanel.class.getResourceAsStream("/pipe_section.png"));
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reset() {
        this.rects.clear();
    }

    // Generate a line that represents the score trigger for a rectanlge
    public Line2D scoringLine(Rectangle r) {
        return new Line2D.Float(r.x + r.width/2, 0, r.x + r.width/2, FlappyBird.HEIGHT);
    }

    // Does a rectangle intersect the scoring line
    public boolean intersectsScoringLine(Rectangle other) {
        for(Rectangle r : rects) {
            if(other.intersectsLine(scoringLine(r))) return true;
        }
        return false;
    }

    // General intersection
    public boolean intersects(Rectangle other) {
        for(Rectangle r : rects) {
            if(other.intersects(r)) return true;
        }
        return false;
    }

    @Override
    public void tick() {
        if(flappyBird.hasGameStarted() && flappyBird.getTick() % PIPE_FREQUENCY == 0) {
            int gapHeight = rand.nextInt(MAX_HEIGHT-MIN_HEIGHT) + MIN_HEIGHT; // Pick a bounded random location to start the pipe game (top of gap y)
            Rectangle r = new Rectangle(FlappyBird.WIDTH, 0, PIPE_W, gapHeight-PIPE_GAP); // Top pipe
            Rectangle r2 = new Rectangle(FlappyBird.WIDTH, gapHeight+PIPE_GAP, PIPE_W, FlappyBird.HEIGHT-gapHeight+PIPE_GAP); // bottom pipe
            rects.add(r);
            rects.add(r2);
        }
        ArrayList<Rectangle> toRemove = new ArrayList<Rectangle>();
        for(Rectangle r : rects) {
            r.x += PIPE_VELOCITY;
            if(r.x + r.width <= 0) {
                toRemove.add(r);
            }
        }
        rects.removeAll(toRemove);
    }

    @Override
    public void paint(Graphics2D g2d) {
        for(Rectangle r : rects) {
            if(r.y == 0) { // TOP PIPES
                g2d.drawImage(pipeLength, r.x, r.y, r.width, r.height-PIPE_H, null);
                g2d.drawImage(pipeHead, r.x, r.y+r.height, PIPE_W, -PIPE_H, null);
            }
            else {
                // BOTTOM PIPES
                g2d.drawImage(pipeLength, r.x, r.y+PIPE_H, r.width, r.height-PIPE_H, null);
                g2d.drawImage(pipeHead, r.x, r.y, PIPE_W, PIPE_H, null);
            }

            if(DRAW_HITBOXES) {
                g2d.setColor(Color.RED);
                g2d.drawRect(r.x, r.y, r.width, r.height);
                g2d.setColor(Color.BLUE);
                Line2D line2D = scoringLine(r);
                g2d.drawLine((int)line2D.getX1(), (int)line2D.getY1(), (int)line2D.getX2(), (int)line2D.getY2());
            }
        }
    }
}
