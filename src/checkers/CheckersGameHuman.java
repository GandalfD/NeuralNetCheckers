package checkers;

import neural.NeuralNet;
import org.encog.neural.neat.NEATNetwork;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by darwin on 2/27/17.
 */
public class CheckersGameHuman extends JFrame {
    private GameBoard board;
    private Player redPlayer;
    private Player bluePlayer;

    public boolean isBlueTurn;

    private int turnNumber = 0;
    private int blueTurnNumber = 0;
    private int redTurnNumber = 0;

    private NeuralNet net = new NeuralNet();
    private NEATNetwork network;

    private Player winner;

    public boolean gameOver = false;

    private JButton netTurn = new JButton("Neural Network Turn");

    public CheckersGameHuman(NEATNetwork network) {
        super("Checkers Game!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout());

        this.network = network;
        initializeGame();

        netTurn.addActionListener(new Mouse());
        add(netTurn);

        add(board);
        pack();

        setVisible(true);
    }

    public void initializeGame() {
        gameOver = false;
        winner = null;

        net.chosenMoveBlue = 0;
        net.defaultMoveBlue = 0;
        net.chosenMoveRed = 0;
        net.defaultMoveRed = 0;

        redPlayer = new RedPlayer();
        bluePlayer = new BluePlayer();
        board = new GameBoard(redPlayer, bluePlayer, this);
        redPlayer.setBoard(board);
        bluePlayer.setBoard(board);

        bluePlayer.setNetwork(network);

        isBlueTurn = false;
        board.setUpGameBoard();
    }

    private void turn() {

        try {
            LegalMove[] possibleMovesRed = redPlayer.getAllPossibleValidMoves();
            LegalMove[] possibleMovesBlue = bluePlayer.getAllPossibleValidMoves();

            if (board.whoWon(possibleMovesBlue, possibleMovesRed) == redPlayer) {
                System.out.format("%15s%15s%15s%20s", "Red Won", net.chosenMoveBlue + "/" + net.defaultMoveBlue + " | " + blueTurnNumber,
                        net.chosenMoveRed + "/" + net.defaultMoveRed + " | " + redTurnNumber, "Score: " + getBlueScore() + "!\n");
                winner = redPlayer;
                gameOver = true;

            } else if (board.whoWon(possibleMovesBlue, possibleMovesRed) == bluePlayer) {
                System.out.format("%15s%15s%15s%20s", "Blue Won", net.chosenMoveBlue + "/" + net.defaultMoveBlue + " | " + blueTurnNumber,
                        net.chosenMoveRed + "/" + net.defaultMoveRed + " | " + redTurnNumber, "Score: " + getBlueScore() + "!\n");
                winner = bluePlayer;
                gameOver = true;

            } else {
                if (isBlueTurn) { //Blue turn (NN)
                    LegalMove nextMove = net.getMoveNN(bluePlayer.getNetwork(), bluePlayer.convertBoard(), possibleMovesBlue, bluePlayer);
                    bluePlayer.movePiece(nextMove);
                    isBlueTurn = false;
                    blueTurnNumber++;

                } else { // Red's turn (human)
                    System.out.println("Human turn!");

                }

            }
        } catch (InvalidMoveException ime) {
            ime.printCustomError();
        }

    }

    private int getBlueScore() {
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

    private class Mouse implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            turn();
        }
    }
}
