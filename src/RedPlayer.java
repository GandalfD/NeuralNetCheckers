import java.util.ArrayList;

/**
 * Represents the Red Player
 */
public class RedPlayer {

    private CheckerPiece[] redPieces = new CheckerPiece[12];

    public RedPlayer() {

    }

    public CheckerPiece[] getPieces() {
        return redPieces;
    }

    // Returns every single move that every single piece can make
    private ArrayList<ArrayList<LegalMove>> getAllPossibleMoves() {
        ArrayList<ArrayList<LegalMove>> allPossibleMoves = new ArrayList<>();

        for (CheckerPiece piece : redPieces) {
            ArrayList<LegalMove> temp = piece.getAllMoves();

            if (temp == null) {
                System.out.println("Null moves");

            } else if (!temp.isEmpty() && !piece.isCaptured())
                allPossibleMoves.add(temp);
        }

        return allPossibleMoves;
    }

    // Returns array of valid moves (factoring in force jumping)
    public ArrayList<LegalMove> getAllPossibleValidMoves() {
        ArrayList<LegalMove> allPossibleValidMoves = new ArrayList<>();

        for (ArrayList<LegalMove> movesArray : getAllPossibleMoves()) { // Filter through each piece
            for (LegalMove moves : movesArray) { // Filter through each move
                if (moves.getMoveAfter().get(0) == null && moves.getMoveAfter().size() == 1)
                    allPossibleValidMoves.add(moves);
            }
        }

        return allPossibleValidMoves;
    }

    public void movePiece(LegalMove move) throws InvalidMoveException {
        boolean isValid = false;

        if (getAllPossibleValidMoves().isEmpty())
            throw new InvalidMoveException("Blue Player has no possible moves");

        for (LegalMove validMove : getAllPossibleValidMoves()) {
            if (validMove.equals(move)) {
                isValid = true;
                break;
            }
        }
        if (isValid) { // Valid Move
            CheckerPiece currentPiece = move.getRootMove().getOldPiece();

            move.captureJumpedPieces();
            currentPiece.movePiece(move.getNewTile());

            // Piece has reached king spot!
            if (move.getNewTile().returnY() == 7) {
                currentPiece.makeKing();
            }
        } else { // Not valid
            throw new InvalidMoveException("Move is not valid!");
        }
    }
}
