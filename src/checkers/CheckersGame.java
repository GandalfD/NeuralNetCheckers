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

    private int winnerScore = -2;
    private int redGamesWon = 0;
    private int blueGamesWon = 0;

    private int turnNumber = 0;
    private int blueTurnNumber = 0;
    private int redTurnNumber = 0;
    
    private NeuralNet net = new NeuralNet();

    public boolean gameOver = false;
    private Player winner;



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
        winnerScore = -2;
        redGamesWon = 0;
        blueGamesWon = 0;
        turnNumber = 0;
        blueTurnNumber = 0;
        redTurnNumber = 0;
        gameOver = false;
        winner = null;

        net.chosenMoveBlue = 0;
        net.defaultMoveBlue = 0;
        net.chosenMoveRed = 0;
        net.defaultMoveRed = 0;

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
            while (winnerScore == -2) {
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

            if (turnNumber == 100) {
                winner = null;
                winnerScore = 0;
                System.out.format("%15s%15s%15s%20s", "Tie", net.chosenMoveBlue + "/" + net.defaultMoveBlue + " | " + blueTurnNumber,
                        net.chosenMoveRed + "/" + net.defaultMoveRed + " | " + redTurnNumber, "Score: " + getBlueScore() + "!\n");
                gameOver = true;


            }
            if (board.whoWon(possibleMovesBlue, possibleMovesRed) == redPlayer) {
                winner = redPlayer;
                winnerScore = -1;
                System.out.format("%15s%15s%15s%20s", "Red Won", net.chosenMoveBlue + "/" + net.defaultMoveBlue + " | " + blueTurnNumber,
                        net.chosenMoveRed + "/" + net.defaultMoveRed + " | " + redTurnNumber, "Score: " + getBlueScore() + "!\n");
                redGamesWon++;
                gameOver = true;

            } else if (board.whoWon(possibleMovesBlue, possibleMovesRed) == bluePlayer) {
                winner = bluePlayer;
                winnerScore = 1;
                System.out.format("%15s%15s%15s%20s", "Blue Won", net.chosenMoveBlue + "/" + net.defaultMoveBlue + " | " + blueTurnNumber,
                        net.chosenMoveRed + "/" + net.defaultMoveRed + " | " + redTurnNumber, "Score: " + getBlueScore() + "!\n");
                blueGamesWon++;
                gameOver = true;
            } else {
                if (isBlueTurn) { //Blue turn (NN)
                    LegalMove nextMove = net.getMoveNN(bluePlayer.getNetwork(), bluePlayer.convertBoard(), possibleMovesBlue, bluePlayer);
                    bluePlayer.movePiece(nextMove);
                    isBlueTurn = false;
                    blueTurnNumber++;
                } else { // Red's turn (random)
                    if (redPlayer.getNetwork() != null) {
                        LegalMove nextMove = net.getMoveNN(redPlayer.getNetwork(), redPlayer.convertBoard(), possibleMovesRed, redPlayer);
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

    public int getBlueScore() {
        int score = 0;

        if (winner == bluePlayer)
            score += 20;
        else if (winner == redPlayer)
            score -= 20;
        else
            score -= 10;

        //score += net.chosenMoveBlue;
        //score -= net.defaultMoveBlue;

        score += (net.chosenMoveBlue - net.defaultMoveBlue);
        score -= (turnNumber / 2);

        return score;
    }

    public int getRedScore() {
        int score = 0;

        if (winner == bluePlayer)
            score -= 20;
        else if (winner == redPlayer)
            score += 20;
        else
            score -= 5;

        score += net.chosenMoveRed;
        score -= net.defaultMoveRed;

        return score;
    }

    public Player getWinner() {
            return winner;
    }

    public boolean hasGameEnded() {
        return gameOver;
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
                if (winnerScore == -2)
                    turn();
                else {
                    int choice = JOptionPane.showConfirmDialog(this, "Start new game?", "Question", JOptionPane.YES_NO_OPTION);
                    if (choice == 0) {
                        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
//                        new PlayGUI();
                        gameOver = true;
                    }
                }
            }
        }
    }

}