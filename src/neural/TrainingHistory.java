package neural;

import org.encog.ml.ea.genome.Genome;
import org.encog.ml.ea.population.Population;
import org.encog.ml.ea.species.Species;
import org.encog.neural.neat.NEATPopulation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by darwin on 2/18/17.
 */
public class TrainingHistory implements Serializable{
    private static final long serialVersionUID = 42L;

    private List<Double> errorList = new ArrayList<>();
    private List<Integer> epochList = new ArrayList<>();
    private List<Species> bestSpecieList = new ArrayList<>();
    private List<Genome> bestGenomeList = new ArrayList<>();

    public void addElement(int epoch, double error, Genome bestGenome, Species bestSpecies) {
        errorList.add(error);
        epochList.add(epoch);
        bestGenomeList.add(bestGenome);
        bestSpecieList.add(bestSpecies);
    }

    public Population getBestPop() {
        Species bestSpecies = bestSpecieList.get(0);
        for (Species s : bestSpecieList) {
            if (s.getBestScore() > bestSpecies.getBestScore()) {
                bestSpecies = s;
            }
        }

        return bestSpecies.getPopulation();
    }

//    @Override
//    public String toString() {
//        String result = "";
//        for (int i = 0; i < popList.size(); i++) {
//            result += popList.get(i).get
//        }
//    }
}
