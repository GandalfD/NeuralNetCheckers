package neural;

import org.encog.neural.neat.NEATPopulation;

import java.io.Serializable;

/**
 * Created by Ethan on 1/31/2017.
 * Stores data for training
 */
public class TrainingData implements Serializable {
    public int epoch;
    public NEATPopulation pop;

    private static final long serialVersionUID = 666L;

    public void reset() {
        this.epoch = 0;
        this.pop = null;
    }

    @Override
    public String toString() {
        return "Epoch: " + epoch + "\nSurvive: " + pop.getSurvivalRate();
    }
}