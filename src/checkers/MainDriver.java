package checkers;

import neural.TrainingData;
import neural.TrainingHistory;
import org.encog.neural.neat.NEATNetwork;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by Darwin on 1/3/2017.
 *
 * 1/3/17 - Set up window for the GUI, added project to GitHub
 *
 * 1/7/17 - Set up checkerboard
 *        - Set up pieces with movement functionality
 *
 * 1/9/17 - Added functionality for kings (but no logic)
 *        - Changed control of tiles from board to player classes
 *
 * 1/11/17 - Experimented with system to check for legal moves
 *         - Created method to get all tiles a specific piece can jump too
 *              - Can only detect single jumps as of now
 * 1/12/17 - Experimented more with checking legal move system
 *         - Implemented really bad fake recursion to gather all legal moves
 *         - One specific piece can detect multi-jumps
 *
 * 1/16/17 - Fully working legal tile checker
 *         - Many annoying bugs fixed
 *
 * 1/25/17 - Added checkers.LegalMove Class
 *         - Moves placed in a a weird single noded tree data structure
 *         - Can trace moves back and forward
 *         - Can analyze all possible moves of a piece and player, with what pieces will be captured
 *         - No longer static beause it doesn't make sense anymore
 *
 */
public class MainDriver {

    public static final boolean HUMAN = true;
    private static TrainingData playerData;

    public static void main(String[] args) {


        if (HUMAN) {
            readFiles();

            CheckersGameHuman human = new CheckersGameHuman((NEATNetwork) playerData.pop.getCODEC().decode(playerData.pop.getBestGenome()));

            while (true) {
                while (!human.gameOver) {

                }

                human.initializeGame();
            }
        } else {
            CheckersMainMenu mainGUI = new CheckersMainMenu();
            mainGUI.display();
        }
    }

    public static void readFiles() {
        ObjectInputStream in;
        try {
            //Get player data
            in = new ObjectInputStream(new FileInputStream("training-data.td-new2500"));
            playerData  = (TrainingData) in.readObject();
            System.out.println("Found Training Data!");
            System.out.println(playerData);
            in.close();
        } catch (IOException e) {
            //When nothing found

            System.out.println("Something bad happened");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
