package checkers;

import org.encog.ml.MLMethod;
import org.encog.neural.neat.NEATNetwork;

import java.util.ArrayList;

/**
 * Represents the Red checkers.Player
 */
public class RedPlayer extends Player {

    private CheckerPiece[] redPieces = new CheckerPiece[12];
    private GameBoard board;
    private boolean canJump = false;

    private final PieceColors teamColor = PieceColors.RED;

    private NEATNetwork network;

    public RedPlayer(GameBoard board) {
        this.board = board;
    }

    public RedPlayer() {

    }

    @Override
    public PieceColors getTeamColor() {
        return teamColor;
    }

    @Override
    public double[] convertBoard() {
        GameBoardTile[] tileArray = board.getTileOneArray();

        double[] boardData = new double[tileArray.length];
        for (int i = 0; i < tileArray.length; i++) {
            CheckerPiece piece = tileArray[i].getCurrentPiece();

            if (piece == null) {
                boardData[i] = 0;
            } else if (piece.getColor() == PieceColors.BLUE) {
                if (piece.getIsKing())
                    boardData[i] = -1;
                else
                    boardData[i] = -0.5; // normal piece
            } else { // Tile has red piece
                if (piece.getIsKing())
                    boardData[i] = 1;
                else
                    boardData[i] = 0.5; // normal piece
            }
        }

        return boardData;
    }


}
