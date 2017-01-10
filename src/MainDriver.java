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
 */
public class MainDriver {

    public static void main(String[] args) {
        CheckersMainMenu mainGUI = new CheckersMainMenu();
        mainGUI.display();

    }
}

