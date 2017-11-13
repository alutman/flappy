package flappy;

import flappy.entity.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Main game setup and loop class
 */
public class FlappyBird implements ActionListener {
    //Settings
    public static final int FPS = 60, WIDTH = 640, HEIGHT = 480;
    public static final boolean DRAW_HITBOXES = false;
    public static final boolean GOD_MODE = false;

    //Game entities
    private Bird bird;
    private Pipes pipes;
    private ArrayList<GameEntity> entities = new ArrayList<GameEntity>();
    private int score = 0;

    //Panel entities
    private JFrame frame;
    private JPanel panel;

    //Loop entities
    private int tick;
    private boolean paused;

    private void makeFrame() {
        panel = new GamePanel(this, entities);

        frame = new JFrame("Flappy Bird");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.add(panel);
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(false);
        try {
            frame.setIconImage(ImageIO.read(FlappyBird.class.getResourceAsStream("/bird.png")));
        } catch (IOException e) {
            // Who cares
        }

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        InputListener inputListener = new InputListener(this);
        frame.addKeyListener(inputListener);
        frame.addMouseListener(inputListener);
    }

    public FlappyBird() {
        bird = new Bird(this);
        pipes = new Pipes(this);

        // Order is important here for painting
        entities.add(new Background());
        entities.add(bird);
        entities.add(pipes);
        entities.add(new Scoreboard(this));

        makeFrame();

        paused = true;
        Timer t = new Timer(1000 / FPS, this);
        t.start();
    }

    private boolean hasIntersected = false;
    @Override
    public void actionPerformed(ActionEvent e) {
        //Main loop
        if(!paused) {
            for(GameEntity go : entities) {
                go.tick();
            }
            boolean game = true;
            tick++;
            if(pipes.intersects(bird.hitbox) && !GOD_MODE) {
                JOptionPane.showMessageDialog(frame, "You lose!\n"+"Your score was: "+ score +".");
                game = false;
            }

            if(pipes.intersectsLine(bird.hitbox)) {
                if(!hasIntersected) {
                    score++;
                    hasIntersected = true;
                }
            }
            else {
                hasIntersected = false;
            }

            if(bird.y > HEIGHT) {
                if(score > 0) JOptionPane.showMessageDialog(frame, "You lose!\n"+"Your score was: "+ score +".");
                game = false;
            }
            if(!game) {
                for(GameEntity go : entities) {
                    go.reset();
                }
                tick = 0;
                score = 0;
                paused = true;
            }
        }
        panel.repaint();
    }
    public int getTick() {
        return tick;
    }
    public int getScore() { return score; }

    public Bird getBird () {
        return bird;
    }
    public boolean paused() {
        return paused;
    }
    public void setPaused(boolean b) {
        paused = b;
    }

    public static void main(String[] args) {
        new FlappyBird();
    }
}