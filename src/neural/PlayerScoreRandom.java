package neural;

import org.encog.ml.CalculateScore;
import org.encog.ml.MLMethod;
import org.encog.neural.neat.NEATNetwork;

import java.io.Serializable;


/**
 * Created by ethanm on 1/25/17.
 */
public class PlayerScoreRandom implements CalculateScore, Serializable {

    public static final long serialVersionUID = 411L;

    @Override
    public double calculateScore(MLMethod network) {
        NeuralPlayerRandom player = new NeuralPlayerRandom((NEATNetwork) network);
        return player.scorePlayer();
    }

    //testing purposes only
    public double calculateScore(NEATNetwork network) {
        NeuralPlayerRandom player = new NeuralPlayerRandom(network);
        return player.scorePlayer();
    }


    public boolean shouldMinimize() {
        return false;
    }


    @Override
    public boolean requireSingleThreaded() {
        return MainTrain.AM_DEBUGGING;
    }
}
