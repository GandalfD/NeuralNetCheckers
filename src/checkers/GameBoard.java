package checkers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * Represents the whole gameboard
 * Utilizes the checkers.CheckerPiece and checkers.GameBoardTile classes
 * Gets checkerpieces from player classes and initializes them
 */
public class GameBoard extends JPanel {

    // Sets up game board via multidimensional array of gameboard tiles (8 rows and 8 columns)
    private static final int ROWS = 8;
    private static final int COLUMNS = 8;
    private GameBoardTile[][] tile = new GameBoardTile[ROWS][COLUMNS];

    // Arrays to hold pieces
    private CheckerPiece[] bluePieces;
    private CheckerPiece[] redPieces;

    private Player redPlayer;
    private Player bluePlayer;

    // Layout manager *DONT USE GRIDLAYOUT*
    private GridBagConstraints c = new GridBagConstraints();

    private CheckersGameHuman badcoding;

    // Constructor
    public GameBoard(Player redPlayer, Player bluePlayer) {
        this.bluePieces = bluePlayer.getPieces();
        this.redPieces = redPlayer.getPieces();

        this.redPlayer = redPlayer;
        this.bluePlayer = bluePlayer;

        setLayout(new GridBagLayout());
        addMouseListener(new Mouse());
    }

    public GameBoard(Player redPlayer, Player bluePlayer, CheckersGameHuman bad) {
        this.bluePieces = bluePlayer.getPieces();
        this.redPieces = redPlayer.getPieces();

        this.redPlayer = redPlayer;
        this.bluePlayer = bluePlayer;

        setLayout(new GridBagLayout());
        addMouseListener(new Mouse());

        this.badcoding = bad;
    }

    // Set up the tiles on the gameboard (not the pieces)
    public void setUpGameBoard() {

        // Loops through each row
        for (int i = 1; i < ROWS; i+=2) {

            // Does the row that starts with black
            for (int j = 0; j < COLUMNS; j+=2) {
                c.gridy = i-1;

                tile[i-1][j] = new GameBoardTile(TileColors.BLACK, i-1, j);
                tile[i-1][j+1] = new GameBoardTile(TileColors.GREY, i-1, j+1);

                c.gridx = j;
                add(tile[i-1][j], c);
                c.gridx = j+1;
                add(tile[i-1][j+1], c);
            }

            // Does the row that starts with grey
            for (int j = 0; j < COLUMNS; j+=2) {
                c.gridy = i;
                tile[i][j] = new GameBoardTile(TileColors.GREY, i, j);
                tile[i][j+1] = new GameBoardTile(TileColors.BLACK, i, j+1);

                c.gridx = j;
                add(tile[i][j], c);
                c.gridx = j+1;
                add(tile[i][j+1], c);
            }
        }

        displayPieces();
    }

    // pinnacle of bad coding below
    private void displayPieces() {
        // Red Team Row
        displayFirstRowPattern(0);

        // https://www.youtube.com/watch?v=gH476CxJxfg
        redPieces[4] = new CheckerPiece(PieceColors.RED, tile[1][0], 4, this);
        redPieces[5] = new CheckerPiece(PieceColors.RED, tile[1][2], 5, this);
        redPieces[6] = new CheckerPiece(PieceColors.RED, tile[1][4], 6, this);
        redPieces[7] = new CheckerPiece(PieceColors.RED, tile[1][6], 7, this);
        tile[1][0].setUp(redPieces[4]);
        tile[1][2].setUp(redPieces[5]);
        tile[1][4].setUp(redPieces[6]);
        tile[1][6].setUp(redPieces[7]);

        // lol screw it, I'm just gonna hardcode everything from here on out with no loops
        redPieces[8] = new CheckerPiece(PieceColors.RED, tile[2][1], 8, this);
        redPieces[9] = new CheckerPiece(PieceColors.RED, tile[2][3], 9, this);
        redPieces[10] = new CheckerPiece(PieceColors.RED, tile[2][5], 10, this);
        redPieces[11] = new CheckerPiece(PieceColors.RED, tile[2][7], 11, this);
        tile[2][1].setUp(redPieces[8]);
        tile[2][3].setUp(redPieces[9]);
        tile[2][5].setUp(redPieces[10]);
        tile[2][7].setUp(redPieces[11]);

        // THE ABOVE WORKS BUT YOU DON'T WHY???!!?!?!?
        /* int counter = 0; // bad
         for (int i = 0; i < 4; i+=2) {
            redPieces[4 + counter] = new checkers.CheckerPiece(checkers.PieceColors.RED, tile[1][i]);
            tile[1][i].setUp(redPieces[counter]);
            System.out.println("Second " + counter);
            counter++;
        } */

        //Black Team Row
        displayFirstRowPatternBlue(6);
        bluePieces[4] = new CheckerPiece(PieceColors.BLUE, tile[5][0], 4, this);
        bluePieces[5] = new CheckerPiece(PieceColors.BLUE, tile[5][2], 5, this);
        bluePieces[6] = new CheckerPiece(PieceColors.BLUE, tile[5][4], 6, this);
        bluePieces[7] = new CheckerPiece(PieceColors.BLUE, tile[5][6], 7, this);
        tile[5][0].setUp(bluePieces[4]);
        tile[5][2].setUp(bluePieces[5]);
        tile[5][4].setUp(bluePieces[6]);
        tile[5][6].setUp(bluePieces[7]);

        bluePieces[8] = new CheckerPiece(PieceColors.BLUE, tile[7][0], 8, this);
        bluePieces[9] = new CheckerPiece(PieceColors.BLUE, tile[7][2], 9, this);
        bluePieces[10] = new CheckerPiece(PieceColors.BLUE, tile[7][4], 10, this);
        bluePieces[11] = new CheckerPiece(PieceColors.BLUE, tile[7][6], 11, this);
        tile[7][0].setUp(bluePieces[8]);
        tile[7][2].setUp(bluePieces[9]);
        tile[7][4].setUp(bluePieces[10]);
        tile[7][6].setUp(bluePieces[11]);

    }

    private void displayFirstRowPattern(int rowIndex) {
        int counter = 0; // <-- lol worse programming ever
        for (int j = 1; j < 8; j += 2) {
            redPieces[counter] = new CheckerPiece(PieceColors.RED, tile[rowIndex][j], counter, this);
            tile[rowIndex][j].setUp(redPieces[counter]);
            counter++;
        }
    }

    private void displayFirstRowPatternBlue(int rowIndex) {
        int counter = 0; // <-- lol worse programming ever
        for (int j = 1; j < 8; j+=2) {
            bluePieces[counter] = new CheckerPiece(PieceColors.BLUE, tile[rowIndex][j], counter, this);
            tile[rowIndex][j].setUp(bluePieces[counter]);
            counter++;
        }

    }

    public GameBoardTile[][] getTile() {
        return tile;
    }

    public GameBoardTile[] getTileOneArray() {
        GameBoardTile[] oneDTile = new GameBoardTile[ROWS * COLUMNS];

        int index = 0;
        for (GameBoardTile[] row : tile) {
            for (GameBoardTile rowTile : row) {
                oneDTile[index] = rowTile;
                index++;
            }
        }

        return oneDTile;
    }

    private void tileClicked(GameBoardTile tile) {

        ArrayList<GameBoardTile> tileToRemove = new ArrayList<>();

        boolean unselect = false;
        for (GameBoardTile[] tileArray : this.tile)
            for(GameBoardTile theTile : tileArray)
                if (theTile.isSelected()) {
                    unselect = true;
                    tileToRemove.add(theTile);
                }

        if (!unselect) { // Should I check for tiles, or should I examine a checkers.LegalMove
            for (LegalMove move : tile.getCurrentPiece().getAllMoves()) {
                //System.out.println("Legal Move: " + move.getNewTile());
                move.getNewTile().selectTile(move);
            }
        } else if (!tile.isSelected()){
            for (GameBoardTile theTile : tileToRemove)
                theTile.unselectTile();
        } else {
            try {
                if (!badcoding.isBlueTurn) {
                    redPlayer.movePiece(tile.getPossibleTileMove());

                    for (GameBoardTile theTile : tileToRemove)
                        theTile.unselectTile();

                    if (tile.getPossibleTileMove().getTotalJumpedTiles().get(0) != null)
                        forceJump(tile.getPossibleTileMove().getNewTile());


                        if (badcoding != null)
                            badcoding.isBlueTurn = true;

                } else
                    System.out.println("Neural Network Turn!");
            } catch (InvalidMoveException ime) {
                ime.printCustomError();
            }
        }
    }

    // more bad coding
    private void forceJump(GameBoardTile afterJump) {
        ArrayList<GameBoardTile> recursionJump = new ArrayList<>();
        for (LegalMove move : afterJump.getCurrentPiece().getAllMoves()) {
            if (move.getTotalJumpedTiles().get(0) != null) {
                recursionJump.add(move.getNewTile());
                move.getNewTile().selectTile(move);
            }
        }

        for (GameBoardTile tileRecrusion : recursionJump)
            forceJump(tileRecrusion);
    }
    // Returns winning player
    // Returns null if game is still going on
    public Player whoWon(LegalMove[] blueMoves, LegalMove[] redMoves) {
        if (redMoves.length == 0) {
            return bluePlayer;
        }
        else if (blueMoves.length == 0 ) {
            return redPlayer;
        }
        else
            return null;
    }

    private class Mouse implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                GameBoardTile tile = null;
                Component comp = findComponentAt(e.getPoint());
                if (comp instanceof CheckerPiece)
                    tile = ((CheckerPiece) comp).getCurrentTile();
                else if (comp instanceof GameBoardTile)
                    tile = (GameBoardTile) comp;
                else if (comp instanceof JLabel){
                    comp = comp.getParent();
                    while (!(comp instanceof GameBoardTile)) {
                        comp = comp.getParent();
                    }
                    tile = (GameBoardTile) comp;
                }


                System.out.println(tile);
                tileClicked(tile);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}

