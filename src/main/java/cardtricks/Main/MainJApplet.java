/**
 * Copyright (C) 2013 Vy Thao Nguyen
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package cardtricks.Main;

import cardtricks.tricks.ThreeRows;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import javax.swing.JApplet;
import javax.swing.JFrame;

/**
 *
 * @author Vy Thao Nguyen
 */
public class MainJApplet extends JApplet
{
    /**
     * Entry point for the applet-version
     */
    @Override
    public void init()
    {
        try
        {
            add(new ThreeRows(this));
        }
        catch (IOException ex)
        {
            javax.swing.JOptionPane.showMessageDialog(null, "Opps, something went wrong! Try reloading the page!");
        }
    }
    
    /**
     * Entry point for the desktop-app version
     * @param args 
     */
    public static void main(String args[])
    {
        try
        {
            float opac = 1;
            CheckOpacity:
            if (args != null && args.length == 1)
            {
                try
                {
                    opac = Float.parseFloat(args[0]);
                    if (opac < 0 || opac > 1)
                    {
                        System.err.println("Please supply value within [0.0, 1.0]");
                        opac = 1;
                        break CheckOpacity;
                    }
                    // Determine if the GraphicsDevice supports translucency.
                    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                    GraphicsDevice gd = ge.getDefaultScreenDevice();

                    if (gd.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.TRANSLUCENT))
                    {
                        JFrame.setDefaultLookAndFeelDecorated(true);
                    }
                }
                catch(NumberFormatException e)
                {
                    e.printStackTrace();
                }
            }
            JFrame frame = new MainJFrame();
            if (opac != 1.0f)
            {
                frame.setOpacity(opac);
            }
        }
        catch(IOException ex)
        {
            javax.swing.JOptionPane.showMessageDialog(null, "Opps, something went wrong! Try restarting the app!");
        }
    }
}
