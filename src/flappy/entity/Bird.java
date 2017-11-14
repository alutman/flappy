package flappy.entity;

import flappy.FlappyBird;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;

import static flappy.Settings.*;

/**
 * Represents the player
 */
public class Bird implements GameEntity {
    public float x, y, vx, vy;
    public final int WIDTH = 30;
    public final int HEIGHT = 20;

    private int WING_SPEED = FlappyBird.FPS / 2;
    private Image wingDown, wingMid, wingUp;
    public Rectangle hitbox = new Rectangle();
    private boolean alive = true;
    private int hit = 0;


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

    public void kill() {
        this.alive = false;
    }

    // Trigger a hit flash
    public void hit() {
        if(hit == 0) hit = 10;
    }

    @Override
    public void tick() {
        x+=vx;
        y+=vy;
        if(GOD_MODE){ // Stop bird from falling off map
            y = Math.min(y, FlappyBird.HEIGHT-100);
            if(y == FlappyBird.HEIGHT-100) {
                vy = 0;
            }
        }
        if(!flappyBird.hasGameStarted()) { // Stable starting flight
            if(y > FlappyBird.HEIGHT/2+30) {
                vy -= 0.5;
            }
            else {
                vy += 0.5;
            }
        }
        else {
            vy+=GRAVITY;
        }
    }

    @Override
    public void paint(Graphics2D g) {
        RenderingHints originalRendering = g.getRenderingHints();
        // Bird style is pixel art, don't want to anti-alias here
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_DEFAULT);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        if(y > FlappyBird.HEIGHT) return; // Off map, stop rendering

        if(!alive) { //Game over, make bird fall
            vy+=GRAVITY;
            y+=vy;
        }
        hitbox.setBounds(Math.round(x- WIDTH) + HITBOX_TOLERANCE /2, Math.round(y- HEIGHT) + HITBOX_TOLERANCE /2, WIDTH *2 - HITBOX_TOLERANCE, HEIGHT *2 - HITBOX_TOLERANCE);
        Image img;
        int tick = flappyBird.getTick();
        // Divide tick into discrete intervals of 3 (3 bird frames)
        int segment = (WING_SPEED) / 3;
        int r = tick % (WING_SPEED);
        if(r < segment) {
            img = wingUp;
        }
        else if(r < (2 * segment)) {
            img = wingMid;
        }
        else {
            img = wingDown;
        }

        // Override frame if doing a jump
        if(vy < JUMP_FORCE*(6f/8f)) {
            img=wingMid;
        }
        else if(vy < JUMP_FORCE*(3f/8f)) {
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

        if(DRAW_HITBOXES) {
            g.setColor(Color.YELLOW);
            g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        }

        // Show a hit register flash
        if(hit > 0) {
            //TODO properly flash the image rather than draw a circle on it
            int alpha = (int)(200 * ((float)hit/10f));
            Color intensity = new Color(255,255,255, alpha);
            Color alphaColor = new Color(0,0,0,0);
            RadialGradientPaint radialGradientPaint = new RadialGradientPaint((float) (hitbox.x + (hitbox.width / 2)), ((float) (hitbox.y + (hitbox.height / 2))),
                    ((float) HEIGHT), new float[]{0.7f, 1f}, new Color[]{intensity, alphaColor});
            g.setPaint(radialGradientPaint);
            g.fillOval(hitbox.x, hitbox.y, hitbox.width-5, hitbox.height);
            hit--;
        }

        g.setRenderingHints(originalRendering);
    }

    @Override
    public void reset() {
        x = FlappyBird.WIDTH/2;
        y = FlappyBird.HEIGHT/2;
        vx = 0;
        vy = 0;
        alive = true;
    }

    public void jump() {
        vy = JUMP_FORCE;
    }

}