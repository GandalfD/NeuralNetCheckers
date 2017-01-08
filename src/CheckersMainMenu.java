import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    static GameBoard board;
    private JButton test = new JButton("Move");

    public CheckersMainMenu() {
        super("Checkers Program");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new FlowLayout());

        // Sets up the game board
        board = new GameBoard();
        board.setUpGameBoard();

        test.addActionListener(new testclass());
        JPanel testPane = new JPanel();
        testPane.add(test);

        add(testPane);
        add(board);
    }


    public void display() {
        pack();
        setVisible(true);
    }

    // Temporary test class to demonstrate a semi-working movement mechanic
    private class testclass implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            board.getBlueTeam()[5].movePiece(board.getTile()[4][2]);
        }
    }
}
