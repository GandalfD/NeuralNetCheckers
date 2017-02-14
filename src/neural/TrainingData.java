package neural;

import org.encog.ml.MLMethod;
import org.encog.neural.neat.NEATPopulation;
import org.encog.neural.networks.BasicNetwork;

import java.io.Serializable;

/**
 * Created by Ethan on 1/31/2017.
 * Stores data for training
 */
public class TrainingData implements Serializable{
    private int epoch;
    private int bestFitness;
    private MLMethod best;
    private NEATPopulation pop;

    private static final long serialVersionUID = 666L;

    public void reset() {
        this.epoch = 0;
        this.bestFitness = -100;
        this.best = null;
        this.pop  = null;
    }

    public int getEpoch() {
        return epoch;
    }

    public int getBestFitness() {
        return bestFitness;
    }

    public MLMethod getBest() {
        return best;
    }

    public NEATPopulation getPop() {
        return pop;
    }

    public void incrementEpoch() {
        this.epoch++;
    }

    public void setBestFitness(int bestFitness) {
        this.bestFitness = bestFitness;
    }

    public void setBest(MLMethod best) {
        this.best = best;
    }

    public void setPop(NEATPopulation pop) {
        this.pop = pop;
    }

    @Override
    public String toString() {
        return "Epoch: " + epoch + "\nBest Fitness: " + bestFitness + "\nNetwork: " + best + "\nPopulation: " + pop;
    }
}
