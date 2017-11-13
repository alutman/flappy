package flappy.entity;

import flappy.FlappyBird;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;

/**
 * Represents the player
 */
public class Bird implements GameEntity {
    public float x, y, vx, vy;
    public final int WIDTH = 30;
    public final int HEIGHT = 20;

    private final float GRAVITY = 0.5f;
    private final float JUMP_FORCE = -8f;
    private Image wingDown, wingMid, wingUp;
    public Rectangle hitbox = new Rectangle();

    private static final int PADDING = 15; // Hitbox reduction padding (PNG has some alpha padding)

    private FlappyBird flappyBird;

    public Bird(FlappyBird flappyBird) {
        this.flappyBird = flappyBird;
        x = FlappyBird.WIDTH/2;
        y = FlappyBird.HEIGHT/2;
        hitbox.setBounds(Math.round(x - WIDTH), Math.round(y - HEIGHT), WIDTH * 2, HEIGHT * 2);
        try {
            wingDown = ImageIO.read(Bird.class.getResourceAsStream("/wing_down.png"));
            wingMid = ImageIO.read(Bird.class.getResourceAsStream("/wing_mid.png"));
            wingUp = ImageIO.read(Bird.class.getResourceAsStream("/wing_up.png"));
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void tick() {
        x+=vx;
        if(!FlappyBird.GOD_MODE){
            y+=vy;
        }
        vy+=GRAVITY;
    }

    @Override
    public void paint(Graphics2D g) {
        hitbox.setBounds(Math.round(x- WIDTH) + PADDING/2, Math.round(y- HEIGHT) + PADDING/2, WIDTH *2 - PADDING, HEIGHT *2 - PADDING);
        Image img;
        int tick = flappyBird.getTick();
        int r = tick % (FlappyBird.FPS / 2);
        if(r < 10) {
            img = wingUp;
        }
        else if(r < 20) {
            img = wingMid;
        }
        else {
            img = wingDown;
        }

        g.drawImage(img, Math.round(x - WIDTH), Math.round(y - HEIGHT), 2 * WIDTH, 2 * HEIGHT, null);
        if(FlappyBird.DRAW_HITBOXES) {
            g.setColor(Color.YELLOW);
            g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        }

    }

    @Override
    public void reset() {
        x = 640/2;
        y = 640/2;
        vx = vy = 0;
    }

    public void jump() {
        vy = JUMP_FORCE;
    }
}