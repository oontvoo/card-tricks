package cardtricks.Main;

import cardtricks.tricks.ThreeRows;
import java.awt.Dimension;
import java.io.IOException;
import javax.swing.JFrame;

/**
 *
 * @author Vy Thao Nguyen
 */
public class MainJFrame extends JFrame
{
    public MainJFrame() throws IOException
    {
        setTitle("Card tricks!");
        add(new ThreeRows(this));
        this.setMinimumSize(new Dimension(600, 500));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setAlwaysOnTop(true);
        setVisible(true);
    }
}
