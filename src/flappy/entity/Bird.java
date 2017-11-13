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

    private float GRAVITY = 0.5f;
    private float JUMP_FORCE = -8f;
    private int WING_SPEED = FlappyBird.FPS / 2;
    private Image wingDown, wingMid, wingUp;
    public Rectangle hitbox = new Rectangle();

    private static final int PADDING = 8; // Hitbox reduction padding (PNG has some alpha padding)

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
        y+=vy;
        if(FlappyBird.GOD_MODE){
            y = Math.min(y, FlappyBird.HEIGHT-100);
            if(y == FlappyBird.HEIGHT-100) {
                vy = 0;
            }
        }
        vy+=GRAVITY;

    }

    @Override
    public void paint(Graphics2D g) {
        hitbox.setBounds(Math.round(x- WIDTH) + PADDING/2, Math.round(y- HEIGHT) + PADDING/2, WIDTH *2 - PADDING, HEIGHT *2 - PADDING);
        Image img;
        int tick = flappyBird.getTick();
        int segment = (WING_SPEED) / 3;
        int r = tick % (WING_SPEED / 2);
        if(r < segment) {
            img = wingUp;
        }
        else if(r < 2 * segment) {
            img = wingMid;
        }
        else {
            img = wingDown;
        }

        if(vy < JUMP_FORCE*(7f/8f)) {
            img=wingMid;
        }
        else if(vy < JUMP_FORCE*(6f/8f)) {
            img=wingDown;
        }


        AffineTransform transform = g.getTransform();
        g.translate(x, y);

        double rotation = -0.35; // Face slightly up while we're moving up

        if(vy >= 0) {
            rotation = Math.min(-0.35 * vy / JUMP_FORCE, Math.toRadians(80)); // Actual rotation based off velocity
        }

        g.rotate(rotation,0,0);
        g.drawImage(img, -WIDTH, -HEIGHT, 2 * WIDTH, 2 * HEIGHT, null);
        g.setTransform(transform);
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