package neural;

import checkers.GameBoardTile;
import checkers.LegalMove;
import checkers.MoveDirections;
import checkers.Player;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.neat.NEATNetwork;

import java.util.ArrayList;

/**
 * Created by darwin on 2/4/17.
 */
public class NeuralNet {
    public static int testint = 0;
    public static int otherint = 0;

    public static LegalMove getMoveNN(NEATNetwork network, double[] board, ArrayList<LegalMove> possibleValidMoves, Player player) {

        MLData boardData = new BasicMLData(board);
        MLData moveData = network.compute(boardData);

        double[][] moveTileWeight = new double[32][2]; // Contains data for which piece to move
        double[][] directionWeight = new double[32][4]; // Contains data for which direction to move in

        // Gather NN data and move to arrays
        int moveCount = 0;
        int directionCount = 0;
        for (int i = 0; i < MainTrain.OUTPUT_NEURONS; i++) {
            if (i % 5 == 0) { // If it's a "piece" data
                moveTileWeight[moveCount][0] = moveData.getData(i);
                moveTileWeight[moveCount][1] = moveCount;
                moveCount++;
                directionCount = 0;
            }
            else { // Direction data
                directionWeight[moveCount-1][directionCount] = moveData.getData(i);
                directionCount++;
            }

        }

        // Sort the arrays using insertion sort
        // Could make this much more efficient with merge sort but that's gonna take some work
        for (int i = 0; i < moveTileWeight.length-1; i++) {
            int max = i;
            for (int j = i+1; j < moveTileWeight.length; j++) {
                if (moveTileWeight[j][0] > moveTileWeight[max][0]) {
                    max = j;
                }
            }

            if (max != i) {
                double[] temp = moveTileWeight[i];
                moveTileWeight[i] = moveTileWeight[max];
                moveTileWeight[max] = temp;

                double[] directionTemp = directionWeight[i];
                directionWeight[i] = directionWeight[max];
                directionWeight[max] = directionTemp;
            }
        }

        // Choose the highest move possible
        GameBoardTile[] tileArray = player.getBoard().getTileOneArray();
        for (int i = 0; i < moveTileWeight.length; i++) {
            GameBoardTile tileToCheck = tileArray[(int)moveTileWeight[i][1]];

            // Gets the proper direction
            int highestDirection = 0;
            for (int j = 1; j < 4; j++) {
                if (directionWeight[i][j] > directionWeight[i][highestDirection])
                    highestDirection = j;
            }

            // Sets the proper direction
            MoveDirections direction;
            if (highestDirection == 0)
                direction = MoveDirections.UP_RIGHT;
            else if (highestDirection == 1)
                direction = MoveDirections.UP_LEFT;
            else if (highestDirection == 2)
                direction = MoveDirections.DOWN_LEFT;
            else
                direction = MoveDirections.DOWN_RIGHT;

            // Is the desired move legal?
            for (LegalMove moveToCheck : possibleValidMoves) {
                if (moveToCheck.getOldTile().equals(tileToCheck) && moveToCheck.getDirection() == direction) {
                    testint++;
                    if (MainTrain.AM_DEBUGGING) {
                        System.out.println("I am doing move " + moveToCheck);
                        System.out.println("Which is my " + i + " choice");
                    }
                    return moveToCheck;
                }
            }
        }

        // No legal move found, return first possible move
        otherint++;
        if (MainTrain.AM_DEBUGGING)
            System.out.println("First Move: " + possibleValidMoves.get(0));
        return possibleValidMoves.get(0);
    }

    public static double normalize(double x, double in_min, double in_max, double out_min, double out_max)
    {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
}
