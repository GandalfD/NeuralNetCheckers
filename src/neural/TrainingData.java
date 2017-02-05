package neural;

import org.encog.ml.MLMethod;
import org.encog.neural.networks.BasicNetwork;

import java.io.Serializable;

/**
 * Created by Ethan on 1/31/2017.
 * Stores data for training
 */
public class TrainingData implements Serializable{
    // encapsulation doood
    private int epoch;
    private int bestFitness;
    private MLMethod[] previousBests;

    public void reset() {
        this.epoch = 0;
        this.bestFitness = -100;
        this.previousBests = new BasicNetwork[0];
    }

    public int getEpoch() {
        return epoch;
    }

    public int getBestFitness() {
        return bestFitness;
    }

    public MLMethod[] getPreviousBests() {
        return previousBests;
    }
}
