package neural;

import org.encog.ml.CalculateScore;
import org.encog.ml.MLMethod;
import org.encog.neural.neat.NEATNetwork;

/**
 * Created by darwin on 2/5/17.
 */
public class RedPlayerScore implements CalculateScore {
    private MLMethod[] opponents;

    public RedPlayerScore(MLMethod[] opponents) {
        this.opponents = opponents;
    }
    @Override
    public double calculateScore(MLMethod mlMethod) {
        //Cast opponents
        NEATNetwork[] opponentsCast = new NEATNetwork[opponents.length];
        for (int i=0; i<opponents.length; i++)
            opponentsCast[i] = (NEATNetwork) opponents[i];
        //Make player
        NeuralPlayer player = new NeuralPlayer((NEATNetwork) network, opponentsCast);
        //Score player
        return player.scorePlayer();
    }

    @Override
    public boolean shouldMinimize() {
        return false;
    }

    @Override
    public boolean requireSingleThreaded() {
        return false;
    }
}
