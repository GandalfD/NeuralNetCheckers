package neural;

import org.encog.ml.MLMethod;

import org.encog.ml.ea.train.EvolutionaryAlgorithm;
import org.encog.neural.neat.NEATNetwork;
import org.encog.neural.neat.NEATPopulation;
import org.encog.neural.neat.NEATUtil;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;


/**
 * Created by ethanm on 1/25/17.
 * Trains network using competitive unsupervised learning with NEAT
 */
public class MainTrain {

    private static TrainingData playerData;

    private static final int numPops = 15;
    private static final int popSize = 1000;
    private static final int numExtraRounds = 250;

    public static final int INPUT_NEURONS = 32;
    public static final int OUTPUT_NEURONS = 32*5;

    public static boolean AM_DEBUGGING = false;

    public static void main(String[] args) {
        readFiles();
        //Train
        System.out.println("Starting to train");
        actuallyTrain();
    }

    public static void readFiles() {
        ObjectInputStream in;
        try {
            //Get player data
            in = new ObjectInputStream(new FileInputStream("training-data.td"));
            playerData  = (TrainingData) in.readObject();
            System.out.println("Found Training Data!");
            System.out.println(playerData);
        } catch (IOException e) {
            //When nothing found
            playerData = new TrainingData();
            playerData.reset();
            System.out.println("Using New Training Data");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void writeFiles() {
        ObjectOutputStream out;

        try {
            //Write previous bests
            out = new ObjectOutputStream(new FileOutputStream("training-data.td"));
            out.writeObject(playerData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static NEATPopulation createPop(int size) { //Generate a template population
        NEATPopulation network = new NEATPopulation(INPUT_NEURONS, OUTPUT_NEURONS, size);
        network.reset();
        return network;
    }

    private static void actuallyTrain() {
        NEATPopulation pop;
        //Create population
        pop = createPop(popSize);

        EvolutionaryAlgorithm train; //Create training
        train = NEATUtil.constructNEATTrainer(pop, new PlayerScoreRandom());


        //Train to beat

        while (true) {
            System.out.println("Starting iteration");
            train.iteration();
            System.out.println("Iteration Complete");
            playerData.setPop(pop);
            playerData.setBest(train.getCODEC().decode(pop.getBestGenome()));
            playerData.incrementEpoch();

            System.out.println("Creating testScore");
            NeuralPlayerRandom testScore = new NeuralPlayerRandom((NEATNetwork) playerData.getBest());
            System.out.println("Beat");
            playerData.setBestFitness(testScore.scorePlayer());

            System.out.println("Epoch " + playerData.getEpoch() + " Score: " + playerData.getBestFitness());
            writeFiles();

        }
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