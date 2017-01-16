/**
 * Created by Darwin on 1/3/2017.
 *
 * 1/3/17 - Set up window for the GUI, added project to GitHub
 *
 * 1/7/17 - Set up checkerboard
 *        - Set up pieces with movement functionality
 *
 * 1/9/17 - Added functionality for kings (but no logic)
 *        - Changed control of tiles from board to player classes
 *
 * 1/11/17 - Experimented with system to check for legal moves
 *         - Created method to get all tiles a specific piece can jump too
 *              - Can only detect single jumps as of now
 * 1/12/17 - Experimented more with checking legal move system
 *         - Implemented really bad fake recursion to gather all legal moves
 *         - One specific piece can detect multi-jumps
 *
 * 1/16/17 - Fully working legal tile checker
 *         - Many annoying bugs fixed
 *
 */
public class MainDriver {

    public static void main(String[] args) {
        CheckersMainMenu mainGUI = new CheckersMainMenu();
        mainGUI.display();
    }
}

