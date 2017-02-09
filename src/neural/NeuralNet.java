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
    public static LegalMove getMoveNN(NEATNetwork network, double[] board, Player player) {
        ArrayList<LegalMove> possibleValidMoves = player.getAllPossibleValidMoves();

        MLData boardData = new BasicMLData(board);
        MLData moveData = network.compute(boardData);

        int indexOfHighestValue    =  0 ;
        int indexOfHighestMoveValue=  0 ;
        double ignoreValuesAbove   =  99;
        double currentHighestValue = -1;
        double currentMoveHighestValue = -1;
        boolean isFirstIteration   = true;
        boolean foundAnything      = false;


        do {
            //Exclude values above if this isn't the first iteration
            if (!isFirstIteration)
                ignoreValuesAbove = currentHighestValue;

            //Reset current highest
            currentHighestValue = -1;
            currentMoveHighestValue = -1;
            foundAnything = false;

            //Find highest move not excluded
            int moveCount = 0;
            for (int i = 0; i < MainTrain.OUTPUT_NEURONS; i+=5) {
                double weight = moveData.getData(moveCount);
                if (weight > currentHighestValue && weight < ignoreValuesAbove) {
                    indexOfHighestValue = i;
                    currentHighestValue = weight;
                    foundAnything = true;
                    moveCount++;
                    // Find highest direction

                    indexOfHighestMoveValue = 0;
                    for (int j = i+1; j < i+5; j++) {
                        double moveWeight = moveData.getData(j);

                        if (moveWeight > currentMoveHighestValue) {
                            currentMoveHighestValue = moveWeight;
                            indexOfHighestMoveValue++;
                        }
                    }
                }
            }

            isFirstIteration = false;

            //If nothing was found, return first valid move
            if(!foundAnything) {
                otherint++;
                return possibleValidMoves.get(0);
            }

            //Gets direction
            MoveDirections direction;
            if (indexOfHighestMoveValue == 0)
                direction = MoveDirections.UP_RIGHT;
            else if (indexOfHighestMoveValue == 1)
                direction = MoveDirections.UP_LEFT;
            else if (indexOfHighestMoveValue == 2)
                direction = MoveDirections.DOWN_LEFT;
            else
                direction = MoveDirections.DOWN_RIGHT;

            // Checks if highest move is valid
            GameBoardTile tileToCheck = player.getBoard().getTileOneArray()[indexOfHighestValue];
            for (LegalMove moveToCheck : possibleValidMoves) {
                if (moveToCheck.getNewTile().equals(tileToCheck) && moveToCheck.getDirection() == direction) {
                    testint++;
                    return moveToCheck;
                }
            }

        } while (true);

    }

    public static double normalize(double x, double in_min, double in_max, double out_min, double out_max)
    {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
}
