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

    private int[] konamiCode = new int[]{
            KeyEvent.VK_UP, KeyEvent.VK_UP,
            KeyEvent.VK_DOWN, KeyEvent.VK_DOWN,
            KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
            KeyEvent.VK_A, KeyEvent.VK_B };

    private int konamiIndex = 0;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_TAB) {
            Settings.DRAW_HITBOXES = !Settings.DRAW_HITBOXES;
        }
        if(e.getKeyCode() == konamiCode[konamiIndex]) {
            konamiIndex++;
        }
        else {
            konamiIndex = 0;
        }
        if(konamiIndex == konamiCode.length) {
            Settings.GOD_MODE = !Settings.GOD_MODE;
            konamiIndex = 0;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
