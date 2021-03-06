package checkers;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Darwin on 1/23/2017.
 */
public class LegalMove {
    private GameBoardTile oldTile;
    private GameBoardTile newTile;
    private GameBoardTile jumpedTile;

    private LegalMove moveBefore;
    private LegalMove moveAfter;

    private MoveDirections direction;

    public LegalMove(GameBoardTile oldTile, GameBoardTile newTile, LegalMove moveBefore, LegalMove moveAfter, GameBoardTile jumpedTile, MoveDirections direction) {
        this.oldTile = oldTile;
        this.newTile = newTile;
        this.moveBefore = moveBefore;
        this.moveAfter = moveAfter;
        this.jumpedTile = jumpedTile;
        this.direction = direction;
    }

    public LegalMove(GameBoardTile oldTile, GameBoardTile newTile, LegalMove moveBefore, LegalMove moveAfter, MoveDirections direction) {
        this.oldTile = oldTile;
        this.newTile = newTile;
        this.moveBefore = moveBefore;
        this.moveAfter = moveAfter;
        this.direction = direction;
    }

    public ArrayList<GameBoardTile> getTotalJumpedTiles() {
        ArrayList<GameBoardTile> totalJumpedTiles = new ArrayList<>();

        LegalMove moveToCheck = this;
        while (moveToCheck != null) {
            totalJumpedTiles.add(moveToCheck.getJumpedTile());
            moveToCheck = moveToCheck.getMoveBefore();
        }

        return totalJumpedTiles;
    }

    public GameBoardTile getJumpedTile() {
        return jumpedTile;
    }

    public int returnNewY() {
        return newTile.returnY();
    }

    public int returnNewX() {
        return newTile.returnX();
    }

    public GameBoardTile getNewTile() {
        return newTile;
    }

    public GameBoardTile getOldTile() {
        return oldTile;
    }

    public void setMoveAfter(LegalMove moveAfter) {
        this.moveAfter = moveAfter;
    }

    public ArrayList<LegalMove> getPastMoves() {
        ArrayList<LegalMove> pastMoves = new ArrayList<>();
        LegalMove moveToCheck = moveBefore;
        while(moveToCheck != null) {
            pastMoves.add(moveToCheck);
            moveToCheck = moveToCheck.getMoveBefore();
        }

        Collections.reverse(pastMoves);
        pastMoves.add(this);
        return pastMoves;
    }

    public LegalMove getMoveBefore() {
        return moveBefore;
    }

    public LegalMove getMoveAfter() {
        return moveAfter;
    }

    public String toString() {
        if (oldTile != null)
            return "\nOld: " + oldTile + "\nNew: " + newTile + "\n";
        else
            return "\nOld: BEGINNING" + "\nNew: " + newTile + "\n";
    }

    // Returns the first that needs to be made in this move tree
    public LegalMove getRootMove() {
        LegalMove moveToCheck = this;
        while(moveToCheck.getMoveBefore() != null) {
            moveToCheck = moveToCheck.getMoveBefore();
        }

        return moveToCheck;
    }

    // Captures all "jumped" pieces up to this legal move
    // Returns ArrayList of pieces captured
    public ArrayList<CheckerPiece> captureJumpedPieces() {
        ArrayList<CheckerPiece> jumpedPieces = new ArrayList<>();
        LegalMove moveToCheck = this;

        while (moveToCheck != null) {
            if (moveToCheck.getJumpedTile() != null) {
                jumpedPieces.add(moveToCheck.getJumpedTile().getCurrentPiece());
                moveToCheck.getJumpedTile().getCurrentPiece().capturePiece();

                moveToCheck = moveToCheck.getMoveBefore();
            } else {
                //System.out.println("Move " + this + " has no jumped pieces");
                moveToCheck = moveToCheck.getMoveBefore();
            }
        }

        return jumpedPieces;
    }

    public CheckerPiece getOldPiece() {
        return oldTile.getCurrentPiece();
    }

    public MoveDirections getDirection() {
        return direction;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof LegalMove) {
            LegalMove move = (LegalMove) obj;
            return (oldTile == move.oldTile && newTile == move.newTile && jumpedTile == move.jumpedTile && direction == move.getDirection());
        } else {
            return false;
        }
    }

    public void clearTree() {
        LegalMove moveToCheck = moveBefore;

        while (moveToCheck != null) {
            LegalMove temp = moveToCheck;
            moveToCheck = moveToCheck.getMoveBefore();

            temp.moveAfter = null;
            temp.jumpedTile = null;
            temp.oldTile = null;
            temp.newTile = null;
            temp.moveAfter = null;
            temp = null;
        }
    }
}
