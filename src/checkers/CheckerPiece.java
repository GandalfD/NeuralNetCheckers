package checkers;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Represents one checker piece in the whole game
 * A checker piece has an assigned team and starting position
 * It can move around the board
 * Is JLabel with piece as an ImageIcon
 */
public class CheckerPiece extends JLabel {

    private GameBoard board;

    // Team and Color
    private PieceColors color;
    private GameBoardTile currentTile;

    // Image icon
    private BufferedImage pieceImage;
    private BufferedImage kingImage;
    private JLabel kingLabel = new JLabel();

    // Data about piece
    private int pieceNum;
    private boolean isKing = false;
    private boolean isCaptured = false;

    // Variables for checking all legal moves
    private ArrayList<LegalMove> availableLegalMoves = new ArrayList<>();
    private boolean availableTileClearFlag = false;
    private CheckerPiece legalMoveCheckerPiece;

    public CheckerPiece(PieceColors color, GameBoardTile currentTile, int pieceNum, GameBoard board) {
        this.color = color;
        this.currentTile = currentTile;
        this.pieceNum = pieceNum;
        this.board = board;

        // Set pieceImage based on team
        try {
            if (color.equals(PieceColors.BLUE))
                pieceImage = ImageIO.read(getClass().getResource("BluePiece.png"));
            else
                pieceImage = ImageIO.read(getClass().getResource("RedPiece.png"));

            kingImage = ImageIO.read(getClass().getResource("TestKing.png"));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        kingLabel.setIcon(new ImageIcon(kingImage));
        setIcon(new ImageIcon(pieceImage));
        setLayout(new GridBagLayout());
    }

    // Method to move checker piece to new tile
    public void movePiece(GameBoardTile tile) {
        if (!isCaptured) {
            try {
                tile.movePiece(this);
                currentTile = tile;
            } catch (InvalidMoveException ime) {
                ime.printCustomError();
            }
        }
    }

    // Current piece is "captured"
    public void capturePiece() {
        try {
            isCaptured = true;
            getCurrentTile().removePiece(this);
        } catch (InvalidMoveException ime) {
            ime.printCustomError();
        }
    }

    public LegalMove[] getAllMoves() {
        if (!isCaptured)
            return checkAvailableTiles(getCurrentTile());
        else return null;
    }
    // Grabs all the available moves and piece on a specific tile can make
    private LegalMove[] checkAvailableTiles(GameBoardTile tile) {
        ArrayList<LegalMove> recursiveCheck = new ArrayList<>();

        if (availableTileClearFlag) {
            // Clear array to find new available tiles
            availableLegalMoves.clear();
            recursiveCheck.clear();
            availableTileClearFlag = false;
        }

        legalMoveCheckerPiece = tile.getCurrentPiece();
        // Find available pieces if piece is king
        if (tile.getCurrentPiece().getIsKing()) {
            if (tile.returnY() != 0 && tile.returnX() != 0 && !board.getTile()[tile.returnY() - 1][tile.returnX() - 1].isOccupied()) // Is top-left tile clear
                availableLegalMoves.add(new LegalMove(tile, board.getTile()[tile.returnY()-1][tile.returnX()-1], null, null, MoveDirections.UP_LEFT));

            else if (tile.returnY() != 0 && tile.returnX() != 0 && canJump(board.getTile()[tile.returnY()-1][tile.returnX()-1]) == 1) { // Else check if can jump
                LegalMove move = new LegalMove(tile, board.getTile()[tile.returnY()-2][tile.returnX()-2], null, null, board.getTile()[tile.returnY()-1][tile.returnX()-1], MoveDirections.UP_LEFT);
                availableLegalMoves.add(move);
                recursiveCheck.add(move);
                // Check
            }

            if (tile.returnY() != 0 && tile.returnX() != 7 && !board.getTile()[tile.returnY() - 1][tile.returnX() + 1].isOccupied()) // Is top-right tile clear
                availableLegalMoves.add(new LegalMove(tile, board.getTile()[tile.returnY()-1][tile.returnX()+1], null, null, MoveDirections.UP_RIGHT));
            else if (tile.returnY() != 0 && tile.returnX() != 7 && canJump(board.getTile()[tile.returnY()-1][tile.returnX()+1]) == 1) { // Else check if can jump
                LegalMove move = new LegalMove(tile, board.getTile()[tile.returnY()-2][tile.returnX()+2], null, null, board.getTile()[tile.returnY()-1][tile.returnX()+1], MoveDirections.UP_RIGHT);
                availableLegalMoves.add(move);
                recursiveCheck.add(move);
                // Check
            }

            if (tile.returnY() != 7 && tile.returnX() != 0 && !board.getTile()[tile.returnY() + 1][tile.returnX() - 1].isOccupied()) // Is bottom-left tile clear
                availableLegalMoves.add(new LegalMove(tile, board.getTile()[tile.returnY()+1][tile.returnX()-1], null, null, MoveDirections.DOWN_LEFT));
            else if (tile.returnY() != 7 && tile.returnX() != 0 && canJump(board.getTile()[tile.returnY()+1][tile.returnX()-1]) == 1) { // Else check if can jump
                LegalMove move = new LegalMove(tile, board.getTile()[tile.returnY()+2][tile.returnX()-2], null, null, board.getTile()[tile.returnY()+1][tile.returnX()-1], MoveDirections.DOWN_LEFT);
                availableLegalMoves.add(move);
                recursiveCheck.add(move);
                // Check
            }

            if (tile.returnY() != 7 && tile.returnX() != 7 && !board.getTile()[tile.returnY() + 1][tile.returnX() + 1].isOccupied()) // Is bottom-right tile clear
                availableLegalMoves.add(new LegalMove(tile, board.getTile()[tile.returnY()+1][tile.returnX()+1], null, null, MoveDirections.DOWN_RIGHT));
            else if (tile.returnY() != 7 && tile.returnX() != 7 && canJump(board.getTile()[tile.returnY()+1][tile.returnX()+1]) == 1) { // Else check if can jump
                LegalMove move = new LegalMove(tile, board.getTile()[tile.returnY()+2][tile.returnX()+2], null, null, board.getTile()[tile.returnY()+1][tile.returnX()+1], MoveDirections.DOWN_RIGHT);
                availableLegalMoves.add(move);
                recursiveCheck.add(move);
                // Check
            }
        } else {

            // Find available tiles for normal pieces
            if (tile.getCurrentPiece().getColor() == PieceColors.BLUE) {
                if (tile.returnY() != 0 && tile.returnX() != 0 && !board.getTile()[tile.returnY() - 1][tile.returnX() - 1].isOccupied()) // Is top-left tile clear and not off board
                    availableLegalMoves.add(new LegalMove(tile, board.getTile()[tile.returnY() - 1][tile.returnX() - 1], null, null, MoveDirections.UP_LEFT));
                else if (tile.returnY() != 0 && tile.returnX() != 0 && canJump(board.getTile()[tile.returnY() - 1][tile.returnX() - 1]) == 1) { // Else check if can jump
                    LegalMove move = new LegalMove(tile, board.getTile()[tile.returnY() - 2][tile.returnX() - 2], null, null, board.getTile()[tile.returnY() - 1][tile.returnX() - 1], MoveDirections.UP_LEFT);
                    availableLegalMoves.add(move);
                    recursiveCheck.add(move);
                    // Check
                }

                if (tile.returnY() != 0 && tile.returnX() != 7 && !board.getTile()[tile.returnY() - 1][tile.returnX() + 1].isOccupied()) // Is top-right tile clear
                    availableLegalMoves.add(new LegalMove(tile, board.getTile()[tile.returnY()-1][tile.returnX()+1], null, null, MoveDirections.UP_RIGHT));
                else if (tile.returnY() != 0 && tile.returnX() != 7 && canJump(board.getTile()[tile.returnY()-1][tile.returnX()+1]) == 1) { // Else check if can jump
                    LegalMove move = new LegalMove(tile, board.getTile()[tile.returnY()-2][tile.returnX()+2], null, null, board.getTile()[tile.returnY()-1][tile.returnX()+1], MoveDirections.UP_RIGHT);
                    availableLegalMoves.add(move);
                    recursiveCheck.add(move);
                    // Check
                }

            } else {
                // Red Team
                if (tile.returnY() != 7 && tile.returnX() != 0 && !board.getTile()[tile.returnY() + 1][tile.returnX() - 1].isOccupied()) // Is bottom-left tile clear
                    availableLegalMoves.add(new LegalMove(tile, board.getTile()[tile.returnY()+1][tile.returnX()-1], null, null, MoveDirections.DOWN_LEFT));
                else if (tile.returnY() != 7 && tile.returnX() != 0 && canJump(board.getTile()[tile.returnY()+1][tile.returnX()-1]) == 1) { // Else check if can jump
                    LegalMove move = new LegalMove(tile, board.getTile()[tile.returnY()+2][tile.returnX()-2], null, null, board.getTile()[tile.returnY()+1][tile.returnX()-1], MoveDirections.DOWN_LEFT);
                    availableLegalMoves.add(move);
                    recursiveCheck.add(move);
                    // Check
                }

                if (tile.returnY() != 7 && tile.returnX() != 7 && !board.getTile()[tile.returnY() + 1][tile.returnX() + 1].isOccupied()) // Is bottom-right tile clear
                    availableLegalMoves.add(new LegalMove(tile, board.getTile()[tile.returnY()+1][tile.returnX()+1], null, null, MoveDirections.DOWN_RIGHT));
                else if (tile.returnY() != 7 && tile.returnX() != 7 && canJump(board.getTile()[tile.returnY()+1][tile.returnX()+1]) == 1) { // Else check if can jump
                    LegalMove move = new LegalMove(tile, board.getTile()[tile.returnY()+2][tile.returnX()+2], null, null, board.getTile()[tile.returnY()+1][tile.returnX()+1], MoveDirections.DOWN_RIGHT);
                    availableLegalMoves.add(move);
                    recursiveCheck.add(move);
                    // Check
                }
            }
        }
        for (LegalMove tileToCheck : recursiveCheck) {
            checkAvailableTilesRecursion(tileToCheck);
        }
        availableTileClearFlag = true;
        LegalMove[] arrayMove = new LegalMove[availableLegalMoves.size()];
        return availableLegalMoves.toArray(arrayMove);
    }

    private void checkAvailableTilesRecursion(LegalMove newMove) {

        ArrayList<LegalMove> jumpedMoves = new ArrayList<>();
        jumpedMoves.add(newMove);

        // If its a king
        int i = 0;
        while (i < jumpedMoves.size()) {
            LegalMove currentMove = jumpedMoves.get(i);

            int oldRow = currentMove.getOldTile().returnY();
            int oldCol = currentMove.getOldTile().returnX();
            int newRow = currentMove.returnNewY();
            int newCol = currentMove.returnNewX();

            final int UP_LEFT = 0;
            final int UP_RIGHT = 1;
            final int DOWN_LEFT = 2;
            final int DOWN_RIGHT = 3;
            int moveSign = 1000; // temporary garbage value

            if (newRow > oldRow) { // Move down
                if (newCol > oldCol) { // Move right
                    moveSign = DOWN_RIGHT;
                } else {
                    moveSign = DOWN_LEFT;
                }
            } else {
                if (newCol > oldCol) { // Move right
                    moveSign = UP_RIGHT;
                } else {
                    moveSign = UP_LEFT;
                }
            }

            GameBoardTile upRight;
            GameBoardTile upLeft;
            GameBoardTile downRight;
            GameBoardTile downLeft;

            if (currentMove.returnNewX() <= 1) {
                if (currentMove.returnNewY() <= 1 || currentMove.returnNewY() >= 6) {
                    break;
                } else {
                    upRight = board.getTile()[currentMove.returnNewY() - 1][currentMove.returnNewX() + 1];
                    upLeft = null;
                    downRight = board.getTile()[currentMove.returnNewY() + 1][currentMove.returnNewX() + 1];
                    downLeft = null;
                }
            } else if (currentMove.returnNewX() >= 6) {
                if (currentMove.returnNewY() <= 1 || currentMove.returnNewY() >= 6) {
                    break;
                } else {
                    upRight = null;
                    upLeft = board.getTile()[currentMove.returnNewY() - 1][currentMove.returnNewX() - 1];
                    downRight = null;
                    downLeft = board.getTile()[currentMove.returnNewY() + 1][currentMove.returnNewX() - 1];
                }
            } else if (currentMove.returnNewY() <= 1) {
                if (currentMove.returnNewX() <= 1 || currentMove.returnNewY() >= 6) {
                    break;
                } else {
                    upRight = null;
                    upLeft = null;
                    downRight = board.getTile()[currentMove.returnNewY() + 1][currentMove.returnNewX() + 1];
                    downLeft = board.getTile()[currentMove.returnNewY() + 1][currentMove.returnNewX() - 1];
                }
            } else if (currentMove.returnNewY() >= 6) {
                if (currentMove.returnNewX() <= 1 || currentMove.returnNewY() >= 6) {
                    break;
                } else {
                    upRight = board.getTile()[currentMove.returnNewY() - 1][currentMove.returnNewX() + 1];
                    upLeft = board.getTile()[currentMove.returnNewY() - 1][currentMove.returnNewX() - 1];
                    downRight = null;
                    downLeft = null;
                }
            } else {
                upRight = board.getTile()[currentMove.returnNewY() - 1][currentMove.returnNewX() + 1];
                upLeft = board.getTile()[currentMove.returnNewY() - 1][currentMove.returnNewX() - 1];
                downRight = board.getTile()[currentMove.returnNewY() + 1][currentMove.returnNewX() + 1];
                downLeft = board.getTile()[currentMove.returnNewY() + 1][currentMove.returnNewX() - 1];
            }

            // Is king
            if (this.isKing) {
                if (moveSign != DOWN_LEFT && upRight != null && upRight.isOccupied() && canJump(currentMove.getNewTile(), upRight) == 1) { // Check Up right
                    LegalMove move = new LegalMove(currentMove.getNewTile(), board.getTile()[currentMove.returnNewY() - 2][currentMove.returnNewX() + 2], currentMove, null, upRight, MoveDirections.UP_RIGHT);
                    currentMove.setMoveAfter(move);
                    availableLegalMoves.add(move);
                    jumpedMoves.add(move);
                }

                if (moveSign != DOWN_RIGHT && upLeft != null && upLeft.isOccupied() && canJump(currentMove.getNewTile(), upLeft) == 1) { // Check up left
                    LegalMove move = new LegalMove(currentMove.getNewTile(), board.getTile()[currentMove.returnNewY() - 2][currentMove.returnNewX() - 2], currentMove, null, upLeft, MoveDirections.UP_LEFT);
                    currentMove.setMoveAfter(move);
                    availableLegalMoves.add(move);
                    jumpedMoves.add(move);
                }

                if (moveSign != UP_LEFT && downRight != null && downRight.isOccupied() && canJump(currentMove.getNewTile(), downRight) == 1) { // Check down right
                    LegalMove move = new LegalMove(currentMove.getNewTile(), board.getTile()[currentMove.returnNewY() + 2][currentMove.returnNewX() + 2], currentMove, null, downRight, MoveDirections.DOWN_RIGHT);
                    currentMove.setMoveAfter(move);
                    availableLegalMoves.add(move);
                    jumpedMoves.add(move);
                }

                if (moveSign != UP_RIGHT && downLeft != null && downLeft.isOccupied() && canJump(currentMove.getNewTile(), downLeft) == 1) { // Check down left
                    LegalMove move = new LegalMove(currentMove.getNewTile(), board.getTile()[currentMove.returnNewY() + 2][currentMove.returnNewX() - 2], currentMove, null, downLeft, MoveDirections.DOWN_LEFT);
                    currentMove.setMoveAfter(move);
                    availableLegalMoves.add(move);
                    jumpedMoves.add(move);
                }

            } else {
                if (this.color == PieceColors.BLUE) {
                    if (moveSign != DOWN_LEFT && upRight != null && upRight.isOccupied() && canJump(currentMove.getNewTile(), upRight) == 1) { // Check Up right
                        LegalMove move = new LegalMove(currentMove.getNewTile(), board.getTile()[currentMove.returnNewY() - 2][currentMove.returnNewX() + 2], currentMove, null, upRight, MoveDirections.UP_RIGHT);
                        currentMove.setMoveAfter(move);
                        availableLegalMoves.add(move);
                        jumpedMoves.add(move);
                    }

                    if (moveSign != DOWN_RIGHT && upLeft != null && upLeft.isOccupied() && canJump(currentMove.getNewTile(), upLeft) == 1) { // Check up left
                        LegalMove move = new LegalMove(currentMove.getNewTile(), board.getTile()[currentMove.returnNewY() - 2][currentMove.returnNewX() - 2], currentMove, null, upLeft, MoveDirections.UP_LEFT);
                        currentMove.setMoveAfter(move);
                        availableLegalMoves.add(move);
                        jumpedMoves.add(move);
                    }
                } else { // Red team
                    if (moveSign != UP_LEFT && downRight != null && downRight.isOccupied() && canJump(currentMove.getNewTile(), downRight) == 1) { // Check down right
                        LegalMove move = new LegalMove(currentMove.getNewTile(), board.getTile()[currentMove.returnNewY() + 2][currentMove.returnNewX() + 2], currentMove, null, downRight, MoveDirections.DOWN_RIGHT);
                        currentMove.setMoveAfter(move);
                        availableLegalMoves.add(move);
                        jumpedMoves.add(move);
                    }

                    if (moveSign != UP_RIGHT && downLeft != null && downLeft.isOccupied() && canJump(currentMove.getNewTile(), downLeft) == 1) { // Check down left
                        LegalMove move = new LegalMove(currentMove.getNewTile(), board.getTile()[currentMove.returnNewY() + 2][currentMove.returnNewX() - 2], currentMove, null, downLeft, MoveDirections.DOWN_LEFT);
                        currentMove.setMoveAfter(move);
                        availableLegalMoves.add(move);
                        jumpedMoves.add(move);
                    }
                }
            }
            i++;
        }
        jumpedMoves.clear();
        jumpedMoves = null;
    }

    // Returns whether occupied tile can be jumped
    // Assumes that tile being inputted is adjacent
    private int canJump(GameBoardTile tile) {
        int row = tile.returnY();
        int col = tile.returnX();
        int moveSign;
        final int UP_LEFT = 0;
        final int UP_RIGHT = 1;
        final int DOWN_LEFT = 2;
        final int DOWN_RIGHT = 3;

        int currentRow = legalMoveCheckerPiece.getCurrentTile().returnY();
        int currentCol = legalMoveCheckerPiece.getCurrentTile().returnX();

        if (row == 0 || row == 7 || col == 0 || col == 7) // Game piece to check on edge of board
            return 666;

        if (row < currentRow) { // Move Up
            if (col < currentCol) // Move left
                moveSign = UP_LEFT;
            else
                moveSign = UP_RIGHT;
        } else { // Move down
            if (col < currentCol)
                moveSign = DOWN_LEFT;
            else
                moveSign = DOWN_RIGHT;
        }

        if (legalMoveCheckerPiece.getColor() == PieceColors.RED) {
            if (tile.getCurrentPiece().getColor() == PieceColors.BLUE) { // Valid Enemy piece to jump
                if (legalMoveCheckerPiece.getIsKing()) {
                    if (moveSign == UP_LEFT) {
                        if (!(board.getTile()[row - 1][col - 1].isOccupied())) // Check upper left tile
                            return 1;
                    }
                    else if (moveSign == UP_RIGHT) {
                            if (!(board.getTile()[row - 1][col + 1].isOccupied())) // Check upper right tile
                                return 1;
                        }
                    else if (moveSign == DOWN_LEFT) {
                            if (!(board.getTile()[row + 1][col - 1].isOccupied())) // Check lower left tile
                                return 1;
                        }
                    else {
                            if (!(board.getTile()[row + 1][col + 1].isOccupied())) // Has to be lower right
                                return 1;
                        }

                } else { // Not king, check for default movement positions
                    if (moveSign == DOWN_LEFT) {
                        if (!(board.getTile()[row + 1][col - 1].isOccupied()))
                            return 1;
                    }
                    else {
                        if (!(board.getTile()[row + 1][col + 1].isOccupied()))
                            return 1;
                    }
                }
            } else {
                return 0; // Friendly piece / invalid piece
            }
        } else { // Blue team
            if (tile.getCurrentPiece().getColor() == PieceColors.RED) { // Valid Enemy piece to jump
                if (legalMoveCheckerPiece.getIsKing()) {
                    if (moveSign == UP_LEFT) {
                        if (!(board.getTile()[row - 1][col - 1].isOccupied())) // Check upper left tile
                            return 1;
                    }
                    else if (moveSign == UP_RIGHT) {
                        if (!(board.getTile()[row - 1][col + 1].isOccupied())) // Check upper right tile
                            return 1;
                    }
                    else if (moveSign == DOWN_LEFT) {
                        if (!(board.getTile()[row + 1][col - 1].isOccupied())) // Check lower left tile
                            return 1;
                    }
                    else {
                        if (!(board.getTile()[row + 1][col + 1].isOccupied())) // Has to be lower right
                            return 1;
                    }
                } else { // Not king, check for default movement positions
                    if (moveSign == UP_LEFT) {
                        if (!(board.getTile()[row - 1][col - 1].isOccupied()))
                            return 1;
                    }
                    else {
                        if (!(board.getTile()[row - 1][col + 1].isOccupied()))
                            return 1;
                    }
                }
            } else {
                return 0; // Friendly piece / invalid piece
            }
        }
        return 420; // something bad happened
    }

    private int canJump(GameBoardTile oldTile, GameBoardTile tile) {
        int row = tile.returnY();
        int col = tile.returnX();
        int moveSign;
        final int UP_LEFT = 0;
        final int UP_RIGHT = 1;
        final int DOWN_LEFT = 2;
        final int DOWN_RIGHT = 3;

        if (row == 0 || row == 7 || col == 0 || col == 7) // Game piece to check on edge of board
            return 666;

        if (row < oldTile.returnY()) { // Move Up
            if (col < oldTile.returnX()) // Move left
                moveSign = UP_LEFT;
            else
                moveSign = UP_RIGHT;
        } else { // Move down
            if (col < oldTile.returnX())
                moveSign = DOWN_LEFT;
            else
                moveSign = DOWN_RIGHT;
        }

        if (legalMoveCheckerPiece.getColor() == PieceColors.RED) {
            if (tile.getCurrentPiece().getColor() == PieceColors.BLUE) { // Valid Enemy piece to jump
                if (legalMoveCheckerPiece.getIsKing()) {
                    if (moveSign == UP_LEFT) {
                        if (!(board.getTile()[row - 1][col - 1].isOccupied())) // Check upper left tile
                            return 1;
                    }
                    else if (moveSign == UP_RIGHT) {
                        if (!(board.getTile()[row - 1][col + 1].isOccupied())) // Check upper right tile
                            return 1;
                    }
                    else if (moveSign == DOWN_LEFT) {
                        if (!(board.getTile()[row + 1][col - 1].isOccupied())) // Check lower left tile
                            return 1;
                    }
                    else {
                        if (!(board.getTile()[row + 1][col + 1].isOccupied())) // Has to be lower right
                            return 1;
                    }

                } else { // Not king, check for default movement positions
                    if (moveSign == DOWN_LEFT) {
                        if (!(board.getTile()[row + 1][col - 1].isOccupied()))
                            return 1;
                    }
                    else {
                        if (!(board.getTile()[row + 1][col + 1].isOccupied()))
                            return 1;
                    }
                }
            } else {
                return 0; // Friendly piece / invalid piece
            }
        } else { // Blue team
            if (tile.getCurrentPiece().getColor() == PieceColors.RED) { // Valid Enemy piece to jump
                if (legalMoveCheckerPiece.getIsKing()) {
                    if (moveSign == UP_LEFT) {
                        if (!(board.getTile()[row - 1][col - 1].isOccupied())) // Check upper left tile
                            return 1;
                    }
                    else if (moveSign == UP_RIGHT) {
                        if (!(board.getTile()[row - 1][col + 1].isOccupied())) // Check upper right tile
                            return 1;
                    }
                    else if (moveSign == DOWN_LEFT) {
                        if (!(board.getTile()[row + 1][col - 1].isOccupied())) // Check lower left tile
                            return 1;
                    }
                    else {
                        if (!(board.getTile()[row + 1][col + 1].isOccupied())) // Has to be lower right
                            return 1;
                    }
                } else { // Not king, check for default movement positions
                    if (moveSign == UP_LEFT) {
                        if (!(board.getTile()[row - 1][col - 1].isOccupied()))
                            return 1;
                    }
                    else {
                        if (!(board.getTile()[row - 1][col + 1].isOccupied()))
                            return 1;
                    }
                }
            } else {
                return 0; // Friendly piece / invalid piece
            }
        }
        return 420; // something bad happened
    }

    public void makeKing() {
        isKing = true;
        add(kingLabel);

        // Update GUI
        repaint();
        validate();
    }

    @Override
    public String toString() {
        return ("Team " + color + " \t Tile: (Row: " + currentTile.returnY() + " Col: " + currentTile.returnX() + ") Piece Num: " + pieceNum + " King: " + isKing);
    }

    public GameBoardTile getCurrentTile() {
        return currentTile;
    }

    public PieceColors getColor() {
        return color;
    }

    public boolean getIsKing() {
        return isKing;
    }

    public boolean isCaptured () {
        return isCaptured;
    }
}

