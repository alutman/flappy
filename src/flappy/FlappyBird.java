package flappy;

import flappy.entity.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Main game setup and loop class
 */
public class FlappyBird implements ActionListener {
    //Settings
    public static final int FPS = 60, WIDTH = 640, HEIGHT = 480;
    public static final boolean DRAW_HITBOXES = true;
    public static final boolean GOD_MODE = false;

    //Game entities
    private Bird bird;
    private Pipes pipes;
    private Background background;
    private ArrayList<GameEntity> entities = new ArrayList<GameEntity>();

    //Panel entities
    private JFrame frame;
    private JPanel panel;

    //Loop entities
    private int time;
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
        frame.setVisible(true);

        InputListener inputListener = new InputListener(this);
        frame.addKeyListener(inputListener);
        frame.addMouseListener(inputListener);
    }

    public FlappyBird() {
        bird = new Bird(this);
        pipes = new Pipes(this);
        background = new Background();

        // Order is important here for painting
        entities.add(background);
        entities.add(bird);
        entities.add(pipes);

        makeFrame();

        paused = true;
        Timer t = new Timer(1000 / FPS, this);
        t.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Main loop
        if(!paused) {
            for(GameEntity go : entities) {
                go.tick();
            }
            boolean game = true;
            time++;
            if(pipes.intersects(bird.hitbox) && !GOD_MODE) {
                JOptionPane.showMessageDialog(frame, "You lose!\n"+"Your score was: "+time+".");
                game = false;
            }
            if(bird.y > HEIGHT || bird.y+bird.HEIGHT < 0) {
                game = false;
            }
            if(!game) {
                for(GameEntity go : entities) {
                    go.reset();
                }
                time = 0;
                paused = true;
            }
        }
        panel.repaint();
    }
    public int getScore() {
        return time;
    }

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