package neural; /**
 * Created by ethanm on 1/25/17.
 */

import checkers.CheckersGame;
import org.encog.neural.neat.NEATNetwork;

public class NeuralPlayerRandom {
    private int wins = 0;
    private int losses = 0;
    private int ties = 0;
    private int wins2 = 0;
    private int losses2 = 0;
    private int ties2 = 0;
    private NEATNetwork network;

    public NeuralPlayerRandom(NEATNetwork network) {
        this.network = network;
    }


    public int scorePlayer() {
        int n = 0;
        for (int i=0; i<100; i++) {
            n += this.doIterationA();
           // n -= this.doIterationB();
        }
        System.out.println("W1:" + wins + " T1:" + ties + " L1:" + losses );
        System.out.println("W2:" + wins2+ " T2:" + ties2+ " L2:" + losses2);
        System.out.println();
        return n/2;
    }

    public int doIterationA() {
        CheckersGame game = new CheckersGame();
        game.initializeGame();
        game.getBluePlayer().setNetwork(network);
        
        if (MainTrain.AM_DEBUGGING) {
            game.playGUI();
        }

        while (!game.hasGameEnded()) {
            if (MainTrain.AM_DEBUGGING);

            else
                game.turn();
        }
        //Test stuff
        if (game.getWinner() == game.getBluePlayer()) wins++;
        if (game.getWinner() == null) ties++;
        if (game.getWinner() == game.getRedPlayer())losses++;
        if (MainTrain.AM_DEBUGGING)
            System.out.println(game.getBluePlayer());
        return game.getBlueScore();
    }

    public int doIterationB() {
        CheckersGame game = new CheckersGame();
        game.initializeGame();
        game.getBluePlayer().setNetwork(network);

        if (MainTrain.AM_DEBUGGING) {
            game.playGUI();
        }

        while (!game.hasGameEnded()) {
            if (MainTrain.AM_DEBUGGING);

            else
                game.turn();
        }

        //Test stuff
        if (game.getWinner() == game.getBluePlayer()) wins2++;
        if (game.getWinner() == null) ties2++;
        if (game.getWinner() == game.getRedPlayer())losses2++;
        if (MainTrain.AM_DEBUGGING)
            System.out.println(game.getBluePlayer());
        return game.getBlueScore();
    }
}

