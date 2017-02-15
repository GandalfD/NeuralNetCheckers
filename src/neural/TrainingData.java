package neural;

import org.encog.neural.neat.NEATPopulation;

import java.io.Serializable;

/**
 * Created by Ethan on 1/31/2017.
 * Stores data for training
 */
public class TrainingData implements Serializable {
    public int epoch;
    public int bestFitness;
    public NEATPopulation pop;

    public void reset() {
        this.epoch = 0;
        this.bestFitness = -100;
    }
}
