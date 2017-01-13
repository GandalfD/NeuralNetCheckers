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

    // Grabs all the available moves and piece on a specific tile can make
    public ArrayList<GameBoardTile> checkAvailableTiles(GameBoardTile tile) {
        ArrayList<GameBoardTile> recursiveCheck = new ArrayList<>();
        if (availableTileClearFlag) {
            // Clear array to find new available tiles
            availableTiles.clear();
            availableTileClearFlag = false;
        }

        // Find available pieces if piece is king

        if (isKing) {
            if (tile.returnY() != 0 && tile.returnX() != 0 && !GameBoard.getTile()[tile.returnY() - 1][tile.returnX() - 1].getIsOccupied()) // Is top-left tile clear
                availableTiles.add(GameBoard.getTile()[tile.returnY()-1][tile.returnX()-1]);

            else if (tile.returnY() != 0 && tile.returnX() != 0 && canJump(GameBoard.getTile()[tile.returnY()-1][tile.returnX()-1]) == 1) { // Else check if can jump
                availableTiles.add(GameBoard.getTile()[tile.returnY()-2][tile.returnX()-2]);
                recursiveCheck.add(GameBoard.getTile()[tile.returnY()-2][tile.returnX()-2]);
                // Check
            }

            if (tile.returnY() != 0 && tile.returnX() != 7 && !GameBoard.getTile()[tile.returnY() - 1][tile.returnX() + 1].getIsOccupied()) // Is top-right tile clear
                availableTiles.add(GameBoard.getTile()[tile.returnY()-1][tile.returnX()+1]);
            else if (tile.returnY() != 0 && tile.returnX() != 7 && canJump(GameBoard.getTile()[tile.returnY()-1][tile.returnX()+1]) == 1) { // Else check if can jump
                availableTiles.add(GameBoard.getTile()[tile.returnY()-2][tile.returnX()+2]);
                recursiveCheck.add(GameBoard.getTile()[tile.returnY()-2][tile.returnX()+2]);
                // Check
            }

            if (tile.returnY() != 7 && tile.returnX() != 0 && !GameBoard.getTile()[tile.returnY() + 1][tile.returnX() - 1].getIsOccupied()) // Is bottom-left tile clear
                availableTiles.add(GameBoard.getTile()[tile.returnY()+1][tile.returnX()-1]);
            else if (tile.returnY() != 7 && tile.returnX() != 0 && canJump(GameBoard.getTile()[tile.returnY()+1][tile.returnX()-1]) == 1) { // Else check if can jump
                availableTiles.add(GameBoard.getTile()[tile.returnY()+2][tile.returnX()-2]);
                recursiveCheck.add(GameBoard.getTile()[tile.returnY()+2][tile.returnX()-2]);
                // Check
            }

            if (tile.returnY() != 7 && tile.returnX() != 7 && !GameBoard.getTile()[tile.returnY() + 1][tile.returnX() + 1].getIsOccupied()) // Is bottom-right tile clear
                availableTiles.add(GameBoard.getTile()[tile.returnY()+1][tile.returnX()+1]);
            else if (tile.returnY() != 7 && tile.returnX() != 7 && canJump(GameBoard.getTile()[tile.returnY()+1][tile.returnX()+1]) == 1) { // Else check if can jump
                availableTiles.add(GameBoard.getTile()[tile.returnY()+2][tile.returnX()+2]);
                recursiveCheck.add(GameBoard.getTile()[tile.returnY()+2][tile.returnX()+2]);
                // Check
            }
        } else {

            // Find available tiles for normal pieces
            if (color == PieceColors.BLUE) {
                if (tile.returnY() != 0 && tile.returnX() != 0 && !GameBoard.getTile()[tile.returnY() - 1][tile.returnX() - 1].getIsOccupied()) // Is top-left tile clear and not off board
                    availableTiles.add(GameBoard.getTile()[tile.returnY() - 1][tile.returnX() - 1]);
                else if (tile.returnY() != 0 && tile.returnX() != 0 && canJump(GameBoard.getTile()[tile.returnY() - 1][tile.returnX() - 1]) == 1) { // Else check if can jump
                    availableTiles.add(GameBoard.getTile()[tile.returnY() - 2][tile.returnX() - 2]);
                    recursiveCheck.add(GameBoard.getTile()[tile.returnY() - 2][tile.returnX() - 2]);
                    // Check
                }

                if (tile.returnY() != 0 && tile.returnX() != 7 && !GameBoard.getTile()[tile.returnY() - 1][tile.returnX() + 1].getIsOccupied()) // Is top-right tile clear
                    availableTiles.add(GameBoard.getTile()[tile.returnY()-1][tile.returnX()+1]);
                else if (tile.returnY() != 0 && tile.returnX() != 7 && canJump(GameBoard.getTile()[tile.returnY()-1][tile.returnX()+1]) == 1) { // Else check if can jump
                    availableTiles.add(GameBoard.getTile()[tile.returnY()-2][tile.returnX()+2]);
                    recursiveCheck.add(GameBoard.getTile()[tile.returnY()-2][tile.returnX()+2]);
                    // Check
                }

            } else {
                // Red Team
                if (tile.returnY() != 7 && tile.returnX() != 0 && !GameBoard.getTile()[tile.returnY() + 1][tile.returnX() - 1].getIsOccupied()) // Is bottom-left tile clear
                    availableTiles.add(GameBoard.getTile()[tile.returnY()+1][tile.returnX()-1]);
                else if (tile.returnY() != 7 && tile.returnX() != 0 && canJump(GameBoard.getTile()[tile.returnY()+1][tile.returnX()-1]) == 1) { // Else check if can jump
                    availableTiles.add(GameBoard.getTile()[tile.returnY()+2][tile.returnX()-2]);
                    recursiveCheck.add(GameBoard.getTile()[tile.returnY()+2][tile.returnX()-2]);
                    // Check
                }

                if (tile.returnY() != 7 && tile.returnX() != 7 && !GameBoard.getTile()[tile.returnY() + 1][tile.returnX() + 1].getIsOccupied()) // Is bottom-right tile clear
                    availableTiles.add(GameBoard.getTile()[tile.returnY()+1][tile.returnX()+1]);
                else if (tile.returnY() != 7 && tile.returnX() != 7 && canJump(GameBoard.getTile()[tile.returnY()+1][tile.returnX()+1]) == 1) { // Else check if can jump todo fix this
                    availableTiles.add(GameBoard.getTile()[tile.returnY()+2][tile.returnX()+2]);
                    recursiveCheck.add(GameBoard.getTile()[tile.returnY()+2][tile.returnX()+2]);
                    // Check
                }
            }
        }
        for (GameBoardTile tileToCheck : recursiveCheck) {
            checkAvailableTilesRecursion(tile, tileToCheck); // todo fix this probably wrong
        }
        availableTileClearFlag = true;
        return availableTiles;
    }

    private void checkAvailableTilesRecursion(GameBoardTile oldTile, GameBoardTile newTile) {
        ArrayList<GameBoardTile> recursiveCheck = new ArrayList<>();
        int oldRow = oldTile.returnY();
        int oldCol = oldTile.returnX();
        int newRow = newTile.returnY();
        int newCol = newTile.returnY();
        final int UP_LEFT = 0;
        final int UP_RIGHT = 1;
        final int DOWN_LEFT = 2;
        final int DOWN_RIGHT = 3;
        int moveSign = 1000; // temporary garbage value

        if (newRow > oldRow) { // Move down

        } else {
            if (newCol > oldCol) { // Move right
                moveSign = UP_RIGHT;
            }
        }

        if (newTile.returnX() == 7 || newTile.returnX() == 0 || newTile.returnY() == 7 || newTile.returnX() == 0)
            return; // border piece: get out

        // Find available pieces if piece is king
        if (isKing) {

        } else {

            // Find available tiles for normal pieces
            if (color == PieceColors.BLUE) {
                if (moveSign == UP_RIGHT) {
                    if (canJump(GameBoard.getTile()[newTile.returnY() - 1][newTile.returnX() + 1]) == 1) {
                        availableTiles.add(GameBoard.getTile()[newTile.returnY() - 2][newTile.returnX() + 2]);
                        recursiveCheck.add(GameBoard.getTile()[newTile.returnY() - 2][newTile.returnX() + 2]);
                    }
                }
            } else {
                // Red Team
            }
        }
        for (GameBoardTile tileToCheck : recursiveCheck) {
            checkAvailableTilesRecursion(newTile, tileToCheck);
        }
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

        int currentRow = currentTile.returnY();
        int currentCol = currentTile.returnX();

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

        if (color == PieceColors.RED) {
            if (tile.getCurrentPiece().getColor() == PieceColors.BLUE) { // Valid Enemy piece to jump
                if (isKing) {
                    if (moveSign == UP_LEFT) {
                        if (!(GameBoard.getTile()[row - 1][col - 1].getIsOccupied())) // Check upper left tile
                            return 1;
                    }
                    else if (moveSign == UP_RIGHT) {
                            if (!(GameBoard.getTile()[row - 1][col + 1].getIsOccupied())) // Check upper right tile
                                return 1;
                        }
                    else if (moveSign == DOWN_LEFT) {
                            if (!(GameBoard.getTile()[row + 1][col - 1].getIsOccupied())) // Check lower left tile
                                return 1;
                        }
                    else {
                            if (!(GameBoard.getTile()[row + 1][col + 1].getIsOccupied())) // Has to be lower right
                                return 1;
                        }

                } else { // Not king, check for default movement positions
                    if (moveSign == DOWN_LEFT) {
                        if (!(GameBoard.getTile()[row + 1][col - 1].getIsOccupied()))
                            return 1;
                    }
                    else {
                        if (!(GameBoard.getTile()[row + 1][col + 1].getIsOccupied()))
                            return 1;
                    }
                }
            } else {
                return 0; // Friendly piece / invalid piece
            }
        } else { // Blue team
            if (tile.getCurrentPiece().getColor() == PieceColors.RED) { // Valid Enemy piece to jump
                if (isKing) {
                    if (moveSign == UP_LEFT) {
                        if (!(GameBoard.getTile()[row - 1][col - 1].getIsOccupied())) // Check upper left tile
                            return 1;
                    }
                    else if (moveSign == UP_RIGHT) {
                        if (!(GameBoard.getTile()[row - 1][col + 1].getIsOccupied())) // Check upper right tile
                            return 1;
                    }
                    else if (moveSign == DOWN_LEFT) {
                        if (!(GameBoard.getTile()[row + 1][col - 1].getIsOccupied())) // Check lower left tile
                            return 1;
                    }
                    else {
                        if (!(GameBoard.getTile()[row + 1][col + 1].getIsOccupied())) // Has to be lower right
                            return 1;
                    }
                } else { // Not king, check for default movement positions
                    if (moveSign == UP_LEFT) {
                        if (!(GameBoard.getTile()[row - 1][col - 1].getIsOccupied()))
                            return 1;
                    }
                    else {
                        if (!(GameBoard.getTile()[row - 1][col + 1].getIsOccupied()))
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
}
