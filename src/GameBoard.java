import javax.swing.*;
import java.awt.*;

/**
 * Created by Darwin on 1/7/2017.
 */
public class GameBoard extends JPanel {

    // Sets up game board via multidimensional array of gameboard tiles (8 rows and 8 columns)
    private static final int ROWS = 8;
    private static final int COLUMNS = 8;
    GameBoardTile[][] tile = new GameBoardTile[ROWS][COLUMNS];

    // Layout manager *DONT USE GRIDLAYOUT*
    private GridBagConstraints c = new GridBagConstraints();


    public GameBoard() {
        setLayout(new GridBagLayout());
    }

    public void setUpGameBoard() {

        // Loops through each row
        for (int i = 1; i < ROWS; i+=2) {

            // Does the column that starts with black
            for (int j = 0; j < COLUMNS; j+=2) {
                c.gridy = i;

                tile[i-1][j] = new GameBoardTile(GameColors.BLACK, j, i);
                tile[i-1][j+1] = new GameBoardTile(GameColors.GREY, j+1, i);

                c.gridx = j;
                add(tile[i-1][j].returnJLabel(), c);
                c.gridx = j+1;
                add(tile[i-1][j+1].returnJLabel(), c);
            }

            // Does the column that starts with grey
            for (int j = 0; j < COLUMNS; j+=2) {
                c.gridy = i-1;
                tile[i][j] = new GameBoardTile(GameColors.GREY, j, i-1);
                tile[i][j+1] = new GameBoardTile(GameColors.BLACK, j+1, i-1);

                c.gridx = j;
                add(tile[i][j].returnJLabel(), c);
                c.gridx = j+1;
                add(tile[i][j+1].returnJLabel(), c);
            }
        }


    }

    public GameBoardTile[][] getTile() {
        return tile;
    }
}
