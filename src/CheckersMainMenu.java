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

    RedPlayer redPlayer = new RedPlayer();
    BluePlayer bluePlayer = new BluePlayer();

    public CheckersMainMenu() {
        super("Checkers Program");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new FlowLayout());

        // Sets up the game board
        board = new GameBoard(redPlayer.getPieces(), bluePlayer.getPieces());
        board.setUpGameBoard();

        // Button to test different features
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

    // Temporary test class to test features
    private class testclass implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            bluePlayer.getPieces()[5].movePiece(GameBoard.getTile()[4][3]);
            redPlayer.getPieces()[5].makeKing();
            redPlayer.getPieces()[11].movePiece(GameBoard.getTile()[3][6]);
        }
    }
}
