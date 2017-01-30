import javax.swing.*;
import java.awt.*;

/**
 * Created by Darwin on 1/29/2017.
 */
public class GenerationViewer extends JFrame {

    public GenerationViewer() {
        super("Generation");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new GridLayout());
    }

    public void display() {
        pack();
        setVisible(true);
    }
}
