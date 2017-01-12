import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Represents one checker piece in the whole game
 * A checker piece has an assigned team and starting position
 * It can move around the board
 * Is JLabel with piece as an ImageIcon
 */
public class CheckerPiece extends JLabel {

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

    private ArrayList<GameBoardTile> availableTiles = new ArrayList<>();
    private boolean availableTileClearFlag = false;

    public CheckerPiece(PieceColors color, GameBoardTile currentTile, int pieceNum) {
        this.color = color;
        this.currentTile = currentTile;
        this.pieceNum = pieceNum;

        // Set pieceImage based on team
        try {
            if (color.equals(PieceColors.BLUE))
                pieceImage = ImageIO.read(new File("BluePiece.png"));
            else
                pieceImage = ImageIO.read(new File("RedPiece.png"));

            kingImage = ImageIO.read(new File("TestKing.png"));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        kingLabel.setIcon(new ImageIcon(kingImage));
        setIcon(new ImageIcon(pieceImage));
        setLayout(new GridBagLayout());
    }

    // Method to move checker piece to new tile
    public void movePiece(GameBoardTile tile) {
        try {
            tile.movePiece(this);
            currentTile = tile;
        } catch (InvalidMoveException ime) {
            ime.printCustomError();
        }
    }

    public ArrayList<GameBoardTile> checkAvailableTiles(GameBoardTile tile) {
        if (availableTileClearFlag) {
            // Clear array to find new available tiles
            availableTiles.clear();
            availableTileClearFlag = false;
        }

        // Find available pieces if piece is king
        if (isKing) {
            if (!GameBoard.getTile()[tile.returnY() - 1][tile.returnX() - 1].getIsOccupied()) // Is top-left tile clear
                availableTiles.add(GameBoard.getTile()[tile.returnY()-1][tile.returnX()-1]);

            else if (canJump(GameBoard.getTile()[tile.returnY()-1][tile.returnX()-1])) {
                availableTiles.add(GameBoard.getTile()[tile.returnY()-2][tile.returnX()-2]);
                // Check
            }

            if (!GameBoard.getTile()[tile.returnY() - 1][tile.returnX() + 1].getIsOccupied()) // Is top-right tile clear
                availableTiles.add(GameBoard.getTile()[tile.returnY()-1][tile.returnX()+1]);
            else if (canJump(GameBoard.getTile()[tile.returnY()-1][tile.returnX()+1])) {
                availableTiles.add(GameBoard.getTile()[tile.returnY()-2][tile.returnX()+2]);
                // Check
            }

            if (!GameBoard.getTile()[tile.returnY() + 1][tile.returnX() - 1].getIsOccupied()) // Is bottom-left tile clear
                availableTiles.add(GameBoard.getTile()[tile.returnY()+1][tile.returnX()-1]);
            else if (canJump(GameBoard.getTile()[tile.returnY()+1][tile.returnX()-1])) {
                availableTiles.add(GameBoard.getTile()[tile.returnY()+2][tile.returnX()-2]);
                // Check
            }

            if (!GameBoard.getTile()[tile.returnY() + 1][tile.returnX() + 1].getIsOccupied()) // Is bottom-right tile clear
                availableTiles.add(GameBoard.getTile()[tile.returnY()+1][tile.returnX()+1]);
            else if (canJump(GameBoard.getTile()[tile.returnY()+1][tile.returnX()+1])) {
                availableTiles.add(GameBoard.getTile()[tile.returnY()+2][tile.returnX()+2]);
                // Check
            }
        } else {

            // Find available tiles for normal pieces
            if (color == PieceColors.BLUE) {
                if (!GameBoard.getTile()[tile.returnY() - 1][tile.returnX() - 1].getIsOccupied()) // Is top-left tile clear
                    availableTiles.add(GameBoard.getTile()[tile.returnY() - 1][tile.returnX() - 1]);
                else if (canJump(GameBoard.getTile()[tile.returnY() - 1][tile.returnX() - 1])) {
                    availableTiles.add(GameBoard.getTile()[tile.returnY() - 2][tile.returnX() - 2]);
                    // Check
                }

                if (!GameBoard.getTile()[tile.returnY() - 1][tile.returnX() + 1].getIsOccupied()) // Is top-right tile clear
                    availableTiles.add(GameBoard.getTile()[tile.returnY()-1][tile.returnX()+1]);
                else if (canJump(GameBoard.getTile()[tile.returnY()-1][tile.returnX()+1])) {
                    availableTiles.add(GameBoard.getTile()[tile.returnY()-2][tile.returnX()+2]);
                    // Check
                }

            } else {
                // Red Team
                if (!GameBoard.getTile()[tile.returnY() + 1][tile.returnX() - 1].getIsOccupied()) // Is bottom-left tile clear
                    availableTiles.add(GameBoard.getTile()[tile.returnY()+1][tile.returnX()-1]);
                else if (canJump(GameBoard.getTile()[tile.returnY()+1][tile.returnX()-1])) {
                    availableTiles.add(GameBoard.getTile()[tile.returnY()+2][tile.returnX()-2]);
                    // Check
                }

                if (!GameBoard.getTile()[tile.returnY() + 1][tile.returnX() + 1].getIsOccupied()) // Is bottom-right tile clear
                    availableTiles.add(GameBoard.getTile()[tile.returnY()+1][tile.returnX()+1]);
                else if (canJump(GameBoard.getTile()[tile.returnY()+1][tile.returnX()+1])) {
                    availableTiles.add(GameBoard.getTile()[tile.returnY()+2][tile.returnX()+2]);
                    // Check
                }
            }
        }
        availableTileClearFlag = true;
        return availableTiles;
    }

    // Returns whether occupied tile can be jumped
    // Assumes that tile being inputted is adjacent
    private boolean canJump(GameBoardTile tile) {
        int row = tile.returnY();
        int col = tile.returnX();
        int moveSign;
        final int UP_LEFT = 0;
        final int UP_RIGHT = 1;
        final int DOWN_LEFT = 2;
        final int DOWN_RIGHT = 3;

        int currentRow = currentTile.returnY();
        int currentCol = currentTile.returnX();

        if (row == 0 || row == 7 || col == 0 || col == 7) // Game piece to check on edge of board
            return false;

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

        if (color == PieceColors.RED) {
            if (tile.getCurrentPiece().getColor() == PieceColors.BLUE) { // Valid Enemy piece to jump
                if (isKing) {
                    if (moveSign == UP_LEFT)
                        return !(GameBoard.getTile()[row-1][col-1].getIsOccupied()); // Check upper left tile
                    else if (moveSign == UP_RIGHT)
                        return !(GameBoard.getTile()[row-1][col+1].getIsOccupied()); // Check upper right tile
                    else if (moveSign == DOWN_LEFT)
                        return !(GameBoard.getTile()[row+1][col-1].getIsOccupied()); // Check lower left tile
                    else
                        return !(GameBoard.getTile()[row+1][col+1].getIsOccupied()); // Has to be lower right
                } else { // Not king, check for default movement positions
                    if (moveSign == DOWN_LEFT)
                        return !(GameBoard.getTile()[row+1][col-1].getIsOccupied());
                    else
                        return !(GameBoard.getTile()[row+1][col+1].getIsOccupied());
                }
            } else {
                return false; // Friendly piece / invalid piece
            }
        } else { // Blue team
            if (tile.getCurrentPiece().getColor() == PieceColors.RED) { // Valid Enemy piece to jump
                if (isKing) {
                    if (moveSign == UP_LEFT)
                        return !(GameBoard.getTile()[row-1][col-1].getIsOccupied()); // Check upper left tile
                    else if (moveSign == UP_RIGHT)
                        return !(GameBoard.getTile()[row-1][col+1].getIsOccupied()); // Check upper right tile
                    else if (moveSign == DOWN_LEFT)
                        return !(GameBoard.getTile()[row+1][col-1].getIsOccupied()); // Check lower left tile
                    else
                        return !(GameBoard.getTile()[row+1][col+1].getIsOccupied()); // Has to be lower right
                } else { // Not king, check for default movement positions
                    if (moveSign == UP_LEFT)
                        return !(GameBoard.getTile()[row-1][col-1].getIsOccupied());
                    else
                        return !(GameBoard.getTile()[row-1][col+1].getIsOccupied());
                }
            } else {
                return false; // Friendly piece / invalid piece
            }
        }
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
}
