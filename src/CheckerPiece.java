import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Represents one checker piece in the whole game
 * A checker piece has an assigned team and starting position
 * It can move around the board
 * Is JLabel with piece as an ImageIcon
 */
public class CheckerPiece extends JLabel {

    // Team and Color
    private PieceType type;
    private PieceColors color;
    private GameBoardTile currentTile;

    // Image icon
    private BufferedImage image;

    private int pieceNum;

    public CheckerPiece(PieceColors color, GameBoardTile currentTile, int pieceNum) {
        this.color = color;
        this.currentTile = currentTile;
        this.pieceNum = pieceNum;

        // Set image based on team
        try {
            if (color.equals(PieceColors.BLUE))
                image = ImageIO.read(new File("BluePiece.png"));
            else
                image = ImageIO.read(new File("RedPiece.png"));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        setIcon(new ImageIcon(image));
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

    @Override
    public String toString() {
        return ("Team " + color + " \t Tile: (Row: " + currentTile.returnY() + " Col: " + currentTile.returnX() + ") Piece Num: " + pieceNum);
    }

    public GameBoardTile getCurrentTile() {
        return currentTile;
    }
}
