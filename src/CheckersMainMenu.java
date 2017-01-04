import javax.swing.*;

/**
 * This is the class that the main interface will run off of.
 * It will contain the checkers board and other buttons / I/O methods to interact with
 */

// Main GUI interface for interacting with the program
    // Will need to implement a Gameboard object that draws board and checker piece objects

public class CheckersMainMenu {

    // Main containers/components initialization
    private JFrame frame;

    public CheckersMainMenu() {
        frame = new JFrame("Checkers Program");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public void display() {
        frame.pack();
        frame.setVisible(true);
    }
}
