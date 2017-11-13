package flappy.entity;

import flappy.FlappyBird;
import flappy.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import static flappy.FlappyBird.WIDTH;
import static flappy.FlappyBird.HEIGHT;

/**
 * Creates & moves obstacle pipes
 */
public class Pipes implements GameEntity {

    private ArrayList<Rectangle> rects = new ArrayList<Rectangle>();
    private Image pipeHead, pipeLength;
    private final int PIPE_W = 75, PIPE_H = 60;

    private final int VX = -3;

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

    public boolean intersects(Rectangle other) {
        for(Rectangle r : rects) {
            if(other.intersects(r)) return true;
        }
        return false;
    }


    @Override
    public void tick() {
        if(flappyBird.getScore() % 90 == 0) {
            Rectangle r = new Rectangle(WIDTH, 0, PIPE_W, (int) ((Math.random()*HEIGHT)/5f + (0.2f)*HEIGHT));
            int h2 = (int) ((Math.random()*HEIGHT)/5f + (0.2f)*HEIGHT);
            Rectangle r2 = new Rectangle(WIDTH, HEIGHT - h2, PIPE_W, h2);
            rects.add(r);
            rects.add(r2);
        }
        ArrayList<Rectangle> toRemove = new ArrayList<Rectangle>();
        for(Rectangle r : rects) {
            r.x += VX;
            if(r.x + r.width <= 0) {
                toRemove.add(r);
            }
        }
        rects.removeAll(toRemove);
    }

    @Override
    public void paint(Graphics2D g2d) {
        for(Rectangle r : rects) {
            if(r.y < FlappyBird.HEIGHT/2) {
                //TOP
                g2d.drawImage(pipeLength, r.x, r.y, r.width, r.height-PIPE_H, null);
                g2d.drawImage(pipeHead, r.x, r.y+r.height, PIPE_W, -PIPE_H, null);
            }
            else {
                //BOTTOM
                g2d.drawImage(pipeLength, r.x, r.y+PIPE_H, r.width, r.height-PIPE_H, null);
                g2d.drawImage(pipeHead, r.x, r.y, PIPE_W, PIPE_H, null);
            }

            if(FlappyBird.DRAW_HITBOXES) {
                g2d.setColor(Color.RED);
                g2d.drawRect(r.x, r.y, r.width, r.height);
            }
        }
    }
}
