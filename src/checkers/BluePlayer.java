package checkers;

import org.encog.ml.CalculateScore;
import org.encog.ml.MLMethod;
import org.encog.neural.neat.NEATNetwork;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Represents the Blue checkers.Player
 */
public class BluePlayer extends Player {

    private final PieceColors teamColor = PieceColors.BLUE;

    public BluePlayer(GameBoard board) {
         super(board);
    }

    public BluePlayer() {
        super();
    }

    @Override
    public PieceColors getTeamColor() {
        return teamColor;
    }

    @Override
    public double[] convertBoard() {
        GameBoardTile[] tileArray = getBoard().getTileOneArray();

        int count = 0;
        double[] boardData = new double[(tileArray.length / 2)];
        for (int i = 0; i < tileArray.length; i++) {
            CheckerPiece piece = tileArray[i].getCurrentPiece();

            if (piece == null) {
                if (tileArray[i].isMoveableTile()) {
                    boardData[count] = 0;
                    count++;
                }
                else;
            } else if (piece.getColor() == PieceColors.BLUE) {
                if (piece.getIsKing()) {
                    boardData[count] = 1;
                    count++;
                }
                else {
                    boardData[count] = 0.5; // normal piece
                    count++;
                }
            } else { // Tile has red piece
                if (piece.getIsKing()) {
                    boardData[count] = -1;
                    count++;
                }
                else {
                    boardData[count] = -0.5; // normal piece
                    count++;
                }
            }
        }

        return boardData;
    }

    @Override
    protected boolean checkKing(GameBoardTile tile) {
        return (tile.returnY() == 0);
    }


}
