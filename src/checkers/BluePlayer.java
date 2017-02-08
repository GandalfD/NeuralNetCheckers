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

    private CheckerPiece[] bluePieces = new CheckerPiece[12];
    private GameBoard board;
    private boolean canJump = false;

    private final PieceColors teamColor = PieceColors.BLUE;

    private NEATNetwork network;

    public BluePlayer(GameBoard board) {
        this.board = board;
    }

    public BluePlayer() {

    }

    @Override
    public PieceColors getTeamColor() {
        return teamColor;
    }

    @Override
    public double[] convertBoard() {
        GameBoardTile[] tileArray = board.getTileOneArray();

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


}
