package neural;

import org.encog.ml.MLMethod;
import org.encog.ml.ea.genome.Genome;
import org.encog.ml.ea.species.Species;
import org.encog.ml.ea.train.basic.BasicEA;
import org.encog.ml.ea.train.basic.TrainEA;
import org.encog.neural.neat.NEATNetwork;
import org.encog.neural.neat.training.species.OriginalNEATSpeciation;

import org.encog.ml.ea.train.EvolutionaryAlgorithm;
import org.encog.neural.neat.NEATPopulation;
import org.encog.neural.neat.NEATUtil;

import javax.sound.sampled.*;
import java.io.*;
import java.util.List;


/**
 * Created by ethanm on 1/25/17.
 * Trains network using competitive unsupervised learning with NEAT
 *
 *
 * Handy guide to console output:
 * Winner    #moves from NN/#moves 1st | how many turns taken
 */
public class MainTrain {

    private static TrainingData playerData;
    private static TrainingHistory history;

    private static final int popSize = 500;

    public static final int INPUT_NEURONS = 33;
    public static final int OUTPUT_NEURONS = 32*5;

    public static boolean AM_DEBUGGING = false;
    public static boolean RANDOM_PLAYER = false;

    private static AudioInputStream in;
    private static Clip clip;

    private static TrainEA train;

    public static void main(String[] args) {

        readFiles();
        playerData.pop.setSurvivalRate(50);
        //Train
         //Create training
        System.out.println("Creating new trainer");
        PlayerScoreRandom trainingScore = new PlayerScoreRandom();
        train = NEATUtil.constructNEATTrainer(playerData.pop, trainingScore);
        OriginalNEATSpeciation speciation = new OriginalNEATSpeciation();
        train.setSpeciation(speciation);
        while(true) {
            System.out.println("Epoch " + playerData.epoch + " started.");
            System.out.println("Best Genome: " + playerData.pop.getBestGenome());
            System.out.println("Threads: " + train.getThreadCount());

            train.iteration();
            if (train.getBestGenome().getAdjustedScore() > playerData.pop.getBestGenome().getAdjustedScore())
                playerData.pop = (NEATPopulation) train.getPopulation();
            System.out.println("Epoch " + playerData.epoch + " finished with error of " + train.getError());

            writeFiles();
            // Add epoch history to archive
            history.addElement(playerData.epoch, train.getError(), playerData.pop.getBestGenome(), playerData.pop.determineBestSpecies());

            NeuralPlayerRandom.epochDone();
            playerData.epoch++;


        }
    }

    public static void mainNNvNN(String[] args) {
        readFiles();
        //Train
        while(true) {
            System.out.println("Epoch "+ playerData.epoch);
            trainIteration();

            if (RANDOM_PLAYER) break;

            playerData.epoch++;
            System.out.println("Writing files...");
            writeFiles();
        }
    }

    public static void readFiles() {
        ObjectInputStream in;
        try {
            //Get player data
            in = new ObjectInputStream(new FileInputStream("training-data.td-new"));
            playerData  = (TrainingData) in.readObject();
            System.out.println("Found Training Data!");
            System.out.println(playerData);
            in.close();
        } catch (IOException e) {
            //When nothing found
            playerData = new TrainingData();
            playerData.reset();
            playerData.pop = createPop(popSize);

            System.out.println("Using New Training Data");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            in = new ObjectInputStream(new FileInputStream("training-history1.ser-new"));
            history = (TrainingHistory) in.readObject();
            System.out.println("Found training history");
            in.close();
        } catch (EOFException eof) {
            eof.printStackTrace();
        } catch (IOException ioe) {
            history = new TrainingHistory();
            System.out.println("Creating new training history");
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }

    }

    public static void writeFiles() {
        ObjectOutputStream out;

        try {
            //Write previous bests
            System.out.println("Writing Data...");
            out = new ObjectOutputStream(new FileOutputStream("training-data.td-new"));
            out.writeObject(playerData);
            System.out.println(playerData);
            out.close();

            out = new ObjectOutputStream(new FileOutputStream("training-history1.ser-new"));
            out.writeObject(history);
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static NEATPopulation createPop(int size) { //Generate a template population
        NEATPopulation network = new NEATPopulation(INPUT_NEURONS, OUTPUT_NEURONS, size);
        network.reset();
        return network;
    }

    private static void trainIteration() {
        ShittyPopCopyThing popClone = new ShittyPopCopyThing(playerData.pop);//Set up training & Score
        EvolutionaryAlgorithm train; //Create training
        PlayerScore trainingScore = new PlayerScore(popClone.getPop());
        train = NEATUtil.constructNEATTrainer(playerData.pop, trainingScore);
        OriginalNEATSpeciation speciation = new OriginalNEATSpeciation();
        train.setSpeciation(speciation);
        trainingScore.setTrain(train);

        // Random player
        if (RANDOM_PLAYER) {
            PlayerScore testScore2 = new PlayerScore(playerData.pop);
            testScore2.setTrain(train);
            NeuralPlayerRandom npr = new NeuralPlayerRandom((NEATNetwork)customGetBestGenome(playerData.pop, testScore2, train));
            System.out.println(npr.scorePlayer());
        } else {
            //Run an iteration of training
            train.iteration();

            //This is just some stuff to show how it's going, comment out if you want
            PlayerScore testScore = new PlayerScore(popClone.getPop());
            testScore.setTrain(train);
            System.out.println("Competitive - " + " Score:" + testScore.calculateScore(customGetBestGenome(playerData.pop, testScore, train)) + " Population size: " + getPopSize(playerData.pop));
        }
        //Cleanup
        //train.finishTraining();
    }

    private static int getPopSize(NEATPopulation pop) {
        int num = 0;
        List<Species> speciesList = pop.getSpecies();

        for (int i=0; i<speciesList.size(); i++) {
            List<Genome> genomeList = speciesList.get(i).getMembers();

            if(genomeList.size() > 0) {
                for (int j=0; j<genomeList.size(); j++) {
                    num++;
                }
            }
        }

        return num;
    }

    private static MLMethod customGetBestGenome(NEATPopulation pop, PlayerScore score, EvolutionaryAlgorithm train) {
        MLMethod bestNet = null;
        int bestFitness = Integer.MIN_VALUE;
        List<Species> speciesList = pop.getSpecies();

        for (int i=0; i<speciesList.size(); i++) {
            List<Genome> genomeList = speciesList.get(i).getMembers();

            if(genomeList.size() > 0) {
                for (int j=0; j<genomeList.size(); j++) {
                    MLMethod currentMethod = train.getCODEC().decode(genomeList.get(j));
                    int currentFitness = (int) score.calculateScore(currentMethod);

                    if (currentFitness > bestFitness) {
                        bestNet = currentMethod;
                        bestFitness = currentFitness;
                    }
                }
            }
        }
        return bestNet;
    }

    private static MLMethod[] append(MLMethod[] oldArray, MLMethod toAppend) {
        MLMethod[] newArray = new MLMethod[oldArray.length + 1];
        for (int i = 0; i < oldArray.length; i++) {
            newArray[i] = oldArray[i];
        }
        newArray[newArray.length - 1] = toAppend;
        return newArray;
    }

    private static MLMethod[] limitLength(MLMethod[] oldArray, int length) {
        if (oldArray.length <= length)
            return oldArray;

        MLMethod[] newArray = new MLMethod[length];
        for (int i=0; i<length; i++)
            newArray[i] = oldArray[oldArray.length-1 - i];
        return newArray;
    }
}