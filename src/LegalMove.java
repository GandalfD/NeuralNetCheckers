import java.lang.reflect.Array;
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

    public LegalMove(GameBoardTile oldTile, GameBoardTile newTile, LegalMove moveBefore, LegalMove moveAfter, GameBoardTile jumpedTile) {
        this.oldTile = oldTile;
        this.newTile = newTile;
        this.moveBefore = moveBefore;
        this.moveAfter = moveAfter;
        this.jumpedTile = jumpedTile;
    }

    public LegalMove(GameBoardTile oldTile, GameBoardTile newTile, LegalMove moveBefore, LegalMove moveAfter) {
        this.oldTile = oldTile;
        this.newTile = newTile;
        this.moveBefore = moveBefore;
        this.moveAfter = moveAfter;
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
}
