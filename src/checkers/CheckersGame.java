package checkers;

import neural.NeuralNet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
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
    private int redTurnNumber = 0;

    public boolean gameover = false;

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
        redTurnNumber = 0;

        NeuralNet.chosenMoveBlue = 0;
        NeuralNet.defaultMoveBlue = 0;
        NeuralNet.chosenMoveRed = 0;
        NeuralNet.defaultMoveRed = 0;

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
            LegalMove[] possibleMovesRed = redPlayer.getAllPossibleValidMoves();
            LegalMove[] possibleMovesBlue = bluePlayer.getAllPossibleValidMoves();

            if (turnNumber == 150) {
                winner = 0;
                System.out.format("%15s%15s%15s", "Tie", NeuralNet.chosenMoveBlue + "/" + NeuralNet.defaultMoveBlue + " | " + blueTurnNumber,
                        NeuralNet.chosenMoveRed + "/" + NeuralNet.defaultMoveRed + " | " + redTurnNumber + '\n');

            }
            if (board.whoWon(possibleMovesBlue, possibleMovesRed) == redPlayer) {
                winner = -1;
                System.out.format("%15s%15s%15s", "Red Won", NeuralNet.chosenMoveBlue + "/" + NeuralNet.defaultMoveBlue + " | " + blueTurnNumber,
                        NeuralNet.chosenMoveRed + "/" + NeuralNet.defaultMoveRed + " | " + redTurnNumber + '\n');
                redGamesWon++;
            } else if (board.whoWon(possibleMovesBlue, possibleMovesRed) == bluePlayer) {
                winner = 1;
                System.out.format("%15s%15s%15s", "Blue Won", NeuralNet.chosenMoveBlue + "/" + NeuralNet.defaultMoveBlue + " | " + blueTurnNumber,
                        NeuralNet.chosenMoveRed + "/" + NeuralNet.defaultMoveRed + " | " + redTurnNumber + '\n');
                blueGamesWon++;
            } else {
                if (isBlueTurn) { //Blue turn (NN)
                    LegalMove nextMove = NeuralNet.getMoveNN(bluePlayer.getNetwork(), bluePlayer.convertBoard(), possibleMovesBlue, bluePlayer);
                    bluePlayer.movePiece(nextMove);
                    isBlueTurn = false;
                    blueTurnNumber++;
                } else { // Red's turn (random)
                    if (redPlayer.getNetwork() != null) {
                        LegalMove nextMove = NeuralNet.getMoveNN(redPlayer.getNetwork(), redPlayer.convertBoard(), possibleMovesRed, redPlayer);
                        redPlayer.movePiece(nextMove);
                        isBlueTurn = true;
                        redTurnNumber++;
                    } else {
                        int upperBound = possibleMovesRed.length;
                        LegalMove randomMove = possibleMovesRed[(rng.nextInt(upperBound))];
                        redPlayer.movePiece(randomMove); // executes random move
                        isBlueTurn = true;
                        redTurnNumber++;
                    }
                }


                // gc
                for (LegalMove move : possibleMovesBlue) {
                    move.clearTree();
                }

                for (LegalMove move : possibleMovesRed) {
                    move.clearTree();
                }

                possibleMovesBlue = null;
                possibleMovesRed = null;
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
                        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
//                        new PlayGUI();
                        gameover = true;
                    }
                }
            }
        }
    }

}