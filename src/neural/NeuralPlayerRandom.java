package neural; /**
 * Created by ethanm on 1/25/17.
 */

import checkers.CheckersGame;
import org.encog.neural.neat.NEATNetwork;
import org.encog.neural.networks.BasicNetwork;

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
            n -= this.doIterationB();
        }
        System.out.println("W1:" + wins + " T1:" + ties + " L1:" + losses );
        System.out.println("W2:" + wins2+ " T2:" + ties2+ " L2:" + losses2);
        return n/2;
    }

    public int doIterationA() {
        CheckersGame game = new CheckersGame();
        game.initializeGame();
        game.getBluePlayer().setNetwork(network);
        
        if (MainTrain.AM_DEBUGGING) {
            game.playGUI();
        }

        while (game.getWinner() == -2) {
            if (MainTrain.AM_DEBUGGING);

            else
                game.turn();
        }
        //Test stuff
        if (game.getWinner() == 1) wins++;
        if (game.getWinner() == 0) ties++;
        if (game.getWinner() == -1)losses++;

        return game.getWinner();
    }

    public int doIterationB() {
        CheckersGame game = new CheckersGame();
        game.initializeGame();
        game.getBluePlayer().setNetwork(network);

        if (MainTrain.AM_DEBUGGING) {
            game.playGUI();
        }

        while (game.getWinner() == -2) {
            if (MainTrain.AM_DEBUGGING);

            else
                game.turn();
        }

        //Test stuff
        if (game.getWinner() == -1)wins2++;
        if (game.getWinner() == 0) ties2++;
        if (game.getWinner() == 1) losses2++;
        return game.getWinner();
    }
}

