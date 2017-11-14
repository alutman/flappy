package flappy;

import flappy.entity.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

import static flappy.Settings.GOD_MODE;

/**
 * Main game setup and loop class
 */
public class FlappyBird implements ActionListener {
    //Settings
    public static final int FPS = 60, WIDTH = 640, HEIGHT = 480;

    //Game entities
    private Bird bird;
    private Pipes pipes;
    private ArrayList<GameEntity> entities = new ArrayList<GameEntity>();
    private int score = 0;

    //Panel entities
    private JFrame frame;
    private JPanel panel;

    //Loop control
    private int tick;
    private boolean gameOver = false;
    private boolean gameStarted = false;
    private long endTime = -1;

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
        frame.addMouseListener(inputListener);
    }

    public FlappyBird() {
        bird = new Bird(this);
        pipes = new Pipes(this);

        // Order is important here for painting
        entities.add(new Background());
        entities.add(pipes);
        entities.add(bird);
        entities.add(new Scoreboard(this));

        makeFrame();

        Timer t = new Timer(1000 / FPS, this);
        t.start();
    }

    private boolean hasIntersected = false;
    @Override
    public void actionPerformed(ActionEvent e) {
        //Main loop
        if(!gameOver)  {
            for(GameEntity go : entities) {
                go.tick();
            }
            tick++;
        }

        // Hit a pipe?
        if(pipes.intersects(bird.hitbox)) {
            if(!gameOver)bird.hit();
            if(!GOD_MODE) gameOver = true;
        }

        // Fell off map?
        if(bird.y > HEIGHT) {
            gameOver = true;
        }

        // Hit a score trigger?
        if(pipes.intersectsScoringLine(bird.hitbox)) {
            //Only score once per line by using hasIntersected
            // Without this, we'd get a score for each frame the bird intersects the line
            if(!hasIntersected) {
                score++;
                hasIntersected = true;
            }
        }
        else { // We've left the trigger, can re-fire scoring now
            hasIntersected = false;
        }

        if(gameOver && endTime < 0) { // End time condition stops this firing constantly during game over
            bird.kill();
            endTime = System.currentTimeMillis();
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

    public void reset() {
        if(System.currentTimeMillis() - endTime < 1000) { return; } // prevent accidental gameover close from clicking too fast
        for(GameEntity go : entities) {
            go.reset();
        }
        endTime = -1;
        tick = 0;
        score = 0;
        gameOver = false;
        gameStarted = false;
    }

    public boolean isGameOver() {
        return gameOver;
    }
    public void startGame() { gameStarted = true; }
    public boolean hasGameStarted() {
        return gameStarted;
    }

    public static void main(String[] args) {
        new FlappyBird();
    }
}