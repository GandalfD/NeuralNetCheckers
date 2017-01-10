import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
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
    private PieceColors color;
    private GameBoardTile currentTile;

    // Image icon
    private BufferedImage pieceImage;
    private BufferedImage kingImage;
    private JLabel kingLabel = new JLabel();

    // Data about piece
    private int pieceNum;
    private boolean isKing = false;

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
            kingLabel.setIcon(new ImageIcon(kingImage));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

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

    public void makeKing() {
        isKing = true;
        add(kingLabel);
        repaint();
    }

    @Override
    public String toString() {
        return ("Team " + color + " \t Tile: (Row: " + currentTile.returnY() + " Col: " + currentTile.returnX() + ") Piece Num: " + pieceNum + " King: " + isKing);
    }

    public GameBoardTile getCurrentTile() {
        return currentTile;
    }
}
