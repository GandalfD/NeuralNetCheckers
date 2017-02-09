package checkers;

import neural.NeuralNet;
import org.encog.neural.neat.NEATNetwork;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by darwin on 2/5/17.
 */
public class CheckersGame extends JFrame {
    private GameBoard board;
    private Player redPlayer;
    private Player bluePlayer;

    private JButton start = new JButton("Start");
    private JButton startGUI = new JButton("Graphical Start");

    private boolean isBlueTurn;

    private int winner = -2;
    private int redGamesWon = 0;
    private int blueGamesWon = 0;

    private int turnNumber = 0;
    private int blueTurnNumber = 0;

    public CheckersGame() {
        super("Train Neural Network");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new GridLayout());

        start.addActionListener(new ButtonListener());
        startGUI.addActionListener(new ButtonListener());
        add(start);
        add(startGUI);
    }

    public void display() {
        pack();
        setVisible(true);
    }

    public void initializeGame() {
        winner = -2;
        redGamesWon = 0;
        blueGamesWon = 0;
        turnNumber = 0;
        blueTurnNumber = 0;

        redPlayer = new RedPlayer();
        bluePlayer = new BluePlayer();
        board = new GameBoard(redPlayer, bluePlayer);
        redPlayer.setBoard(board);
        bluePlayer.setBoard(board);

        isBlueTurn = false;
        board.setUpGameBoard();
    }

    public void playGame() {
        int i = 0;
        while (i != 500) {
            initializeGame();
            while (winner == -2) {
                turn();
            }

            i++;
        }
        System.out.println("Red Games: " + redGamesWon);
        System.out.println("Blue Games: " + blueGamesWon);
    }

    public void turn() {
        Random rng = new Random();
        try {
            if (turnNumber == 150) {
                winner = 0;
                System.out.println("Tie " + NeuralNet.testint + "/" + NeuralNet.otherint + " | " + blueTurnNumber);
                NeuralNet.testint = 0;
                NeuralNet.otherint = 0;
            }
            if (board.whoWon() == redPlayer) {
                winner = -1;
                System.out.println("Red Won " + NeuralNet.testint + "/" + NeuralNet.otherint + " | " + blueTurnNumber);
                NeuralNet.testint = 0;
                NeuralNet.otherint = 0;
                redGamesWon++;
            } else if (board.whoWon() == bluePlayer) {
                winner = 1;
                System.out.println("Blue Won " + NeuralNet.testint + "/" + NeuralNet.otherint + " | " + blueTurnNumber);
                NeuralNet.testint = 0;
                NeuralNet.otherint = 0;
                blueGamesWon++;
            } else {
                if (isBlueTurn) { //Blue turn (NN)
                    LegalMove nextMove = NeuralNet.getMoveNN(bluePlayer.getNetwork(), bluePlayer.convertBoard(), bluePlayer);
                    bluePlayer.movePiece(nextMove);
                    isBlueTurn = false;
                    blueTurnNumber++;
                    // TODO Uncomment the lines above and comment the things below @Ethan
                    /*ArrayList<LegalMove> possibleMoves = bluePlayer.getAllPossibleValidMoves();
                    int upperBound = possibleMoves.size();
                    LegalMove randomMove = possibleMoves.get(rng.nextInt(upperBound));
                    bluePlayer.movePiece(randomMove); // executes random move
                    isBlueTurn = false;*/

                } else { // Red's turn (random)
                    ArrayList<LegalMove> possibleMoves = redPlayer.getAllPossibleValidMoves();
                    int upperBound = possibleMoves.size();
                    LegalMove randomMove = possibleMoves.get(rng.nextInt(upperBound));
                    redPlayer.movePiece(randomMove); // executes random move
                    isBlueTurn = true;
                }
                turnNumber++;
            }
        } catch (InvalidMoveException ime) {
            ime.printCustomError();
        }

        // Comment this section to just run games forever
        // This is just for debugging
//            int choice = JOptionPane.showConfirmDialog(this, "View game?", "Question", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//            if (choice == 0) {
//                BoardViewer view = new BoardViewer(board);
//                int quitchoice = JOptionPane.showConfirmDialog(this, "Close?", "Question", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//                while (quitchoice != 0) {
//
//                }
//            }
    }

    public void playGUI() {
        new PlayGUI();
    }

    public Player getRedPlayer() {
        return redPlayer;
    }

    public Player getBluePlayer() {
        return bluePlayer;
    }

    public int getWinner() {
        return winner;
    }

    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == start) {
                playGame();
            } else if (e.getSource() == startGUI) {
                new PlayGUI();
            }
        }
    }

    private class BoardViewer extends JFrame {
        public BoardViewer(GameBoard board) {
            super("Viewer");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            add(board);
            pack();
            setVisible(true);

        }

    }

    private class PlayGUI extends JFrame implements ActionListener {

        private JButton turn = new JButton("Next Turn");

        public PlayGUI() {
            super("Checkers");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new GridLayout());

            turn.addActionListener(this);
            add(turn);

            add(board);

            pack();
            setVisible(true);

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == turn) {
                if (winner == -2)
                    turn();
                else {
                    int choice = JOptionPane.showConfirmDialog(this, "Start new game?", "Question", JOptionPane.YES_NO_OPTION);
                    if (choice == 0) {
//                        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
//                        new PlayGUI();
                    }
                }
            }
        }
    }

}