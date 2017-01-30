import java.util.ArrayList;

/**
 * Represents the Blue Player
 */
public class BluePlayer {

    private CheckerPiece[] bluePieces = new CheckerPiece[12];

    public BluePlayer() {

    }

    public CheckerPiece[] getPieces() {
        return bluePieces;
    }

    // Returns every single move that every single piece can make
    public ArrayList<ArrayList<LegalMove>> getAllPossibleMoves() {
        ArrayList<ArrayList<LegalMove>> allPossibleMoves = new ArrayList<>();

        for (CheckerPiece piece : bluePieces) {
            ArrayList<LegalMove> temp = piece.getAllMoves();
            if (!temp.isEmpty() && !piece.isCaptured())
                allPossibleMoves.add(temp);
        }

        return allPossibleMoves;
    }

    // Returns array of valid moves (factoring in force jumping)
    public ArrayList<LegalMove> getAllPossibleValidMoves() {
        ArrayList<LegalMove> allPossibleValidMoves = new ArrayList<>();

        for (ArrayList<LegalMove> movesArray : getAllPossibleMoves()) {
            for (LegalMove moves : movesArray) {
                if (moves.getMoveAfter() == null)
                    allPossibleValidMoves.add(moves);
            }
        }

        return allPossibleValidMoves;
    }

    public void movePiece(LegalMove move) throws InvalidMoveException {
        if (getAllPossibleValidMoves().contains(move)) { // Valid Move
            move.captureJumpedPieces();
            move.getRootMove().getOldPiece().movePiece(move.getNewTile());
        } else { // Not valid
            throw new InvalidMoveException("Move is not valid!");
        }
    }
}
