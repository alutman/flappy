package flappy;

import java.awt.event.*;

/**
 * Game input listener
 */
public class InputListener implements KeyListener, MouseListener {

    private FlappyBird flappyBird;
    public InputListener(FlappyBird flappyBird) {
        this.flappyBird = flappyBird;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            if(flappyBird.paused()) flappyBird.setPaused(false);
            flappyBird.getBird().jump();
        }
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_UP) {
            flappyBird.getBird().jump();
        }
        else if(e.getKeyCode()==KeyEvent.VK_SPACE) {
            flappyBird.setPaused(false);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

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
