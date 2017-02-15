package neural; /**
 * Created by ethanm on 1/25/17.
 */

import checkers.CheckersGame;
import org.encog.neural.neat.NEATNetwork;

public class NeuralPlayer {
    private NEATNetwork network;
    private NEATNetwork[] opponents;

    public NeuralPlayer(NEATNetwork network, NEATNetwork[] opponents) {
        this.opponents = opponents;
        this.network = network;
    }

    public int scorePlayer() {
        int n = 0;
        for (int i = 0; i < this.opponents.length; i++) {
            n += wl(this.playGame(this.network, this.opponents[i]), +1);
            n -= wl(this.playGame(this.opponents[i], this.network), -1);
        }
        //System.out.print(n + " '");
        return n;
    }

    private int wl (int gameResult, int player) {
        if (gameResult == player*-1)
            return gameResult*5;
        else
            return gameResult;
    }

    private int playGame(NEATNetwork network1, NEATNetwork network2) {
        CheckersGame game = new CheckersGame();
        game.initializeGame();
        game.getBluePlayer().setNetwork(network1);
        game.getRedPlayer().setNetwork(network2);

        //game.playGUI();
        while (game.getWinner() == -2) {
            game.turn();
        }
        int winStatus = game.getWinner();
        game = null;


        // UNCOMMENT IN TIMES OF STRESS
        //System.gc();
        return winStatus;
    }
}

