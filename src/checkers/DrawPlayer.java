package checkers;

import java.util.ArrayList;

/**
 * This guy wins if there's a draaw, otherwise, he does nothing else!
 */
public class DrawPlayer implements Player{
    @Override
    public CheckerPiece[] getPieces() {
        return new CheckerPiece[0];
    }

    @Override
    public ArrayList<LegalMove> getAllPossibleValidMoves() {
        return null;
    }

    @Override
    public GameBoard getBoard() {
        return null;
    }

    @Override
    public boolean allPiecesCaptured() {
        return false;
    }
}
