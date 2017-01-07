import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Darwin on 1/6/2017.
 */
public class GameBoardTile {

    private GameColors tilecolor;
    private JLabel label;

    private int yGrid;
    private int xGrid;

    BufferedImage tile;
    public GameBoardTile(GameColors color, int xGrid, int yGrid) {
        tilecolor = color;
        this.yGrid = yGrid;
        this.xGrid = xGrid;
    }

    public JLabel returnJLabel() {
        try {
            if (tilecolor.equals(GameColors.BLACK)) {
                tile = ImageIO.read(new File("BlackTile.png"));
            } else {
                tile = ImageIO.read(new File("GreyTile.png"));
            }
        } catch (IOException io) {
            io.printStackTrace();
        }

        label = new JLabel(new ImageIcon(tile));
        label.setLayout(new GridBagLayout());
        return label;
    }

    public int returnX() {
        return xGrid;
    }

    public int returnY() {
        return yGrid;
    }

}
