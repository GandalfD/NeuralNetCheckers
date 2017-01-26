import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
    private JButton getAllBlue = new JButton("Blue Moves");

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
        getAllBlue.addActionListener(new testclass());
        JPanel testPane = new JPanel();

        testPane.add(getAllBlue);
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
            if (e.getSource() == test) {
                bluePlayer.getPieces()[5].movePiece(GameBoard.getTile()[4][3]);
                redPlayer.getPieces()[5].makeKing();
                redPlayer.getPieces()[11].movePiece(GameBoard.getTile()[3][6]);
                bluePlayer.getPieces()[7].movePiece(GameBoard.getTile()[4][5]);
                bluePlayer.getPieces()[5].makeKing();
                redPlayer.getPieces()[9].movePiece(GameBoard.getTile()[3][4]);
                redPlayer.getPieces()[6].movePiece(GameBoard.getTile()[3][2]);
                redPlayer.getPieces()[6].movePiece(GameBoard.getTile()[2][3]);
                redPlayer.getPieces()[3].makeKing();
                redPlayer.getPieces()[8].movePiece(GameBoard.getTile()[4][1]);

                bluePlayer.getPieces()[5].movePiece(GameBoard.getTile()[3][2]);
                redPlayer.getPieces()[9].movePiece(GameBoard.getTile()[5][2]);
                redPlayer.getPieces()[10].movePiece(GameBoard.getTile()[3][4]);
                redPlayer.getPieces()[3].movePiece(GameBoard.getTile()[1][7]);


                redPlayer.getPieces()[1].movePiece(GameBoard.getTile()[2][1]);
                redPlayer.getPieces()[6].movePiece(GameBoard.getTile()[1][4]);
                bluePlayer.getPieces()[0].makeKing();
                bluePlayer.getPieces()[2].movePiece(GameBoard.getTile()[5][6]);
                bluePlayer.getPieces()[10].movePiece(GameBoard.getTile()[3][0]);

                for (int i = 0; i < 10; i++) {
                    System.out.print("Waiting\n");
                }

            /*for (GameBoardTile list : redPlayer.getPieces()[8].checkAvailableTiles(redPlayer.getPieces()[8].getCurrentTile()))
                System.out.println(list);
                */
            } else if (e.getSource() == getAllBlue) {
                for (ArrayList<LegalMove> moveSet : bluePlayer.getAllPossibleMoves()) {
                    System.out.println(moveSet + "\n");
                }

                System.out.println("MEME!");

                for (LegalMove move : bluePlayer.getAllPossibleValidMoves()) {
                    System.out.print(move);
                    System.out.print("Jump Tiles: ");
                    for (GameBoardTile jumpedTile : move.getTotalJumpedTiles())
                        System.out.println("\n\t" + jumpedTile);
                    System.out.println();
                }
            }
        }
    }
}
