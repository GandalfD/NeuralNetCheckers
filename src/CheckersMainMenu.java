import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * This is the class that the main interface will run off of.
 * It will contain the checkers board and other buttons / I/O methods to interact with
 */

// Main GUI interface for interacting with the program
// Will need to implement a Gameboard object that draws board and checker piece objects

public class CheckersMainMenu extends JFrame {

    // Main containers/components initialization
    GameBoard board;

    public CheckersMainMenu() {
        super("Checkers Program");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GridBagConstraints c = new GridBagConstraints();

        // Sets up the game board
        board = new GameBoard();
        board.setUpGameBoard();


        add(board);
    }


    public void display() {
        pack();
        setVisible(true);
    }
}
