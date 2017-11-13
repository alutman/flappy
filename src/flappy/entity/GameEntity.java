package flappy.entity;

import java.awt.*;

/**
 * Represents in-game objects
 */
public interface GameEntity {
    public void tick(); // Applied every tick of the game
    public void paint(Graphics2D g); // provides a graphics object for drawing the flappy.entity
    public void reset(); // Reset flappy.entity to start state
}
