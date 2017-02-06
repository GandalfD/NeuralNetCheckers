package checkers;

import jdk.nashorn.internal.scripts.JO;
import neural.NeuralNet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by darwin on 2/5/17.
 */
public class TrainCheckers extends JFrame {
    private GameBoard board;
    private RedPlayer redPlayer;
    private BluePlayer bluePlayer;

    private JButton start = new JButton("Start");
    public TrainCheckers() {
        super("Train Neural Network");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //setLayout(new GridLayout());

        start.addActionListener(new ButtonListener());

        add(start);
    }

    public void display() {
        pack();
        setVisible(true);
    }

    private void startTraining() {
        boolean isBlueTurn = true;

        int i = 0;
        int redGamesWon = 0;
        int blueGamesWon = 0;
        int tieGameScenario = 0;
        while (i != 1500) {
            int winner = -2;
            redPlayer = new RedPlayer(board);
            bluePlayer = new BluePlayer(board);
            board = new GameBoard(redPlayer, bluePlayer);
            board.setUpGameBoard();
            Random rng = new Random();

            while (winner == -2) {
                repaint();
                try {
                    if (board.whoWon() == redPlayer) {
                        winner = -1;
                        System.out.println("Red Won");
                        redGamesWon++;
                    } else if (board.whoWon() == bluePlayer) {
                        winner = 1;
                        System.out.println("Blue Won");
                        blueGamesWon++;
                    } else if (board.whoWon() instanceof DrawPlayer) {
                        winner = 4;
                        System.out.println("Weird draw impossible scenario");
                        tieGameScenario++;
                    } else {
                        if (isBlueTurn) {
//                            LegalMove nextMove = NeuralNet.getMoveNN(bluePlayer.getNetwork(), bluePlayer.convertBoard(), bluePlayer);
//                            bluePlayer.movePiece(nextMove);
                            // TODO Uncomment the lines above and comment the things below @Ethan
                            ArrayList<LegalMove> possibleMoves = bluePlayer.getAllPossibleValidMoves();
                            int upperBound = possibleMoves.size();
                            LegalMove randomMove = possibleMoves.get(rng.nextInt(upperBound));
                            bluePlayer.movePiece(randomMove); // executes random move
                            isBlueTurn = false;
                        } else { // Red's turn
                            ArrayList<LegalMove> possibleMoves = redPlayer.getAllPossibleValidMoves();
                            int upperBound = possibleMoves.size();
                            LegalMove randomMove = possibleMoves.get(rng.nextInt(upperBound));
                            redPlayer.movePiece(randomMove); // executes random move
                            isBlueTurn = true;
                        }
                    }
                } catch (InvalidMoveException ime) {
                    ime.printCustomError();
                }
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
            i++;
        }
        System.out.println("Red Games: " + redGamesWon);
        System.out.println("Blue Games: " + blueGamesWon);
        System.out.println("Tie Games: " + tieGameScenario);
    }

    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == start) {
                startTraining();
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

}
