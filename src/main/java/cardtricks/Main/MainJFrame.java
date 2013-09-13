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
        setVisible(true);
    }
    
    public static void main(String args[]) throws IOException
    {
        // TODO  move main to another field and have a config
        // file to control which class to call
        
        new MainJFrame();
    }
}
