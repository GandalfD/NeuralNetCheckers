import javax.swing.*;
import java.awt.*;

/**
 * Created by Darwin on 1/29/2017.
 */
public class GenerationViewer extends JFrame {
    private GameBoard board;
    RedPlayer redPlayer = new RedPlayer();
    BluePlayer bluePlayer = new BluePlayer();

    public GenerationViewer() {
        super("Generation");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new GridLayout());

        board = new GameBoard(redPlayer, bluePlayer);
        board.setUpGameBoard();

        add(board);
    }

    public void display() {
        pack();
        setVisible(true);
    }
}
