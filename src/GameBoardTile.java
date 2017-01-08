import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Represents one tile in the whole gameboard
 * Is JLabel with tile as an ImageIcon
 * Tiles can be occupied by checker piece
 * Hovering the mouse over a tile will give debug information
 */
public class GameBoardTile extends JLabel {

    private TileColors tilecolor;
    private JLabel label;

    private int yGrid;
    private int xGrid;

    private boolean isOccupied = false;
    private CheckerPiece currentPiece;


    BufferedImage tile;
    public GameBoardTile(TileColors color, int yGrid, int xGrid) {
        tilecolor = color;
        this.yGrid = yGrid;
        this.xGrid = xGrid;

        // Set image based on color tile
        try {
            if (tilecolor.equals(TileColors.BLACK)) {
                tile = ImageIO.read(new File("BlackTile.png"));
            } else {
                tile = ImageIO.read(new File("GreyTile.png"));
            }
        } catch (IOException io) {
            io.printStackTrace();
        }

        setIcon(new ImageIcon(tile));
        addMouseListener(new MouseComponentListener());
        setLayout(new GridBagLayout());
    }

    public void movePiece(CheckerPiece piece) throws InvalidMoveException {
        // Get the tile the piece was on before and remove it
        GameBoardTile previousTile = piece.getCurrentTile();
        previousTile.removePiece(piece);

        // Move the piece the new tile
        isOccupied = true;
        currentPiece = piece;
        add(piece);
        repaint();
    }

    // Removes the piece from this tile
    public void removePiece(CheckerPiece piece) throws InvalidMoveException {
        if (isOccupied) {
            isOccupied = false;
            currentPiece = null;
            remove(piece);
            repaint();
        } else
            throw new InvalidMoveException("Attempted to remove unoccupied tile");
    }

    // Setup method for resetting board. SHOULD ONLY BE CALLED ONCE DURING BOARD SETUP
    public void setUp(CheckerPiece piece) {
        isOccupied = true;
        currentPiece = piece;
        add(piece);
    }

    public int returnX() {
        return xGrid;
    }

    public int returnY() {
        return yGrid;
    }

    private class MouseComponentListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        // Displays information on the current tile the mouse is hovering over
        @Override
        public void mouseEntered(MouseEvent e) {
            System.out.println("(Row: " + yGrid + " Col: " + xGrid + ") \t" + "Current: " + currentPiece);
        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

}
