package neural; /**
 * Created by ethanm on 1/25/17.
 */

import checkers.CheckersGame;
import org.encog.neural.neat.NEATNetwork;
import org.encog.neural.networks.BasicNetwork;

public class NeuralPlayerRandom {
    private NEATNetwork network;
    private static int playerIteration = 1;

    public NeuralPlayerRandom(NEATNetwork network) {
        this.network = network;
    }


    public int scorePlayer() {
        if (MainTrain.AM_DEBUGGING) {
            return doIteration();
        } else {
            int n = 0;
            System.out.println("Player Iteration: " + playerIteration);

            for (int i = 0; i < 100; i++) {
                System.out.print("I: " + i + " ");
                n += this.doIteration();
            }
            playerIteration++;
            return n / 2;
        }
    }

    private int doIteration() {
        if (MainTrain.AM_DEBUGGING) {
            CheckersGame game = new CheckersGame();
            game.initializeGame();
            game.getBluePlayer().setNetwork(this.network);
            game.playGUI();
            while (game.getWinner() == -2) {
                //game.turn();
            }
            System.out.println("Exited game");
            return game.getWinner();

        } else {
            CheckersGame game = new CheckersGame();
            game.initializeGame();
            game.getBluePlayer().setNetwork(this.network);
            //game.playGUI();
            while (game.getWinner() == -2) {
                game.turn();
            }
            int winStatus = game.getWinner();
            game = null;


            // UNCOMMENT IN TIMES OF STRESS
            System.gc();
            return winStatus;
        }
    }

}

