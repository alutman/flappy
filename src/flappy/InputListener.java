package flappy;

import java.awt.event.*;

/**
 * Game input listener
 */
public class InputListener implements MouseListener {

    private FlappyBird flappyBird;
    public InputListener(FlappyBird flappyBird) {
        this.flappyBird = flappyBird;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            if(!flappyBird.hasGameStarted()) flappyBird.startGame();
            if(flappyBird.isGameOver()) flappyBird.reset();
            //only allow jumps during active gameplay
            if(flappyBird.hasGameStarted() && !flappyBird.isGameOver())flappyBird.getBird().jump();

        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
