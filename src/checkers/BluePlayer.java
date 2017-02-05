package checkers;

import org.encog.neural.neat.NEATNetwork;

import java.util.ArrayList;

/**
 * Represents the Blue checkers.Player
 */
public class BluePlayer implements Player {

    private CheckerPiece[] bluePieces = new CheckerPiece[12];
    private GameBoard board;
    private boolean canJump = false;

    private final PieceColors teamColor = PieceColors.BLUE;

    private NEATNetwork network;

    public BluePlayer(GameBoard board) {
        this.board = board;
    }

    @Override
    public CheckerPiece[] getPieces() {
        return bluePieces;
    }

    // Returns every single move that every single piece can make
    private ArrayList<ArrayList<LegalMove>> getAllPossibleMoves() {
        ArrayList<ArrayList<LegalMove>> allPossibleMoves = new ArrayList<>();

        for (CheckerPiece piece : bluePieces) {
            ArrayList<LegalMove> temp = piece.getAllMoves();

            if (temp == null) {
                System.out.println("Null moves");

            } else if (!temp.isEmpty() && !piece.isCaptured()) {
                allPossibleMoves.add(temp);

                for (LegalMove move : temp) {
                    if (move.getJumpedTile() != null) { //Is there a jumped move
                        canJump = true;
                        break;
                    }
                }
            } else {
                for (LegalMove move : temp) {
                    if (move.getJumpedTile() != null) { //Is there a jumped move
                        canJump = true;
                        break;
                    }
                }
            }
        }

        return allPossibleMoves;
    }

    // Returns array of valid moves (factoring in force jumping)
    @Override
    public ArrayList<LegalMove> getAllPossibleValidMoves() {
        ArrayList<LegalMove> allPossibleValidMoves = new ArrayList<>();

        for (ArrayList<LegalMove> movesArray : getAllPossibleMoves()) {
            for (LegalMove moves : movesArray) {
                if (moves.getMoveAfter().get(0) == null && moves.getMoveAfter().size() == 1) // If move is "final move" (has no more jumps)
                    if (canJump) { //Am I forced to jump or not
                        if (moves.getJumpedTile() != null) { // Am forced to jump, only add pieces that can jump
                            allPossibleValidMoves.add(moves);
                        }
                    } else {
                        allPossibleValidMoves.add(moves); // Not forced to jump, add non jumped piece
                    }
            }
        }

        // Return values and reset jump flag
        canJump = false;
        return allPossibleValidMoves;
    }

    @Override
    public GameBoard getBoard() {
        return board;
    }

    public void movePiece(LegalMove move) throws InvalidMoveException {
        boolean isValid = false;

        if (getAllPossibleValidMoves().isEmpty())
            throw new InvalidMoveException("Blue checkers.Player has no possible moves");

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
            if (move.getNewTile().returnY() == 0) {
                currentPiece.makeKing();
            }
        } else { // Not valid
            throw new InvalidMoveException("Move is not valid!");
        }
    }

    public PieceColors getTeamColor() {
        return teamColor;
    }

}
