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
import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author Vy Thao Nguyen
 */
public class MainJApplet extends Applet
{
    @Override
    public void init()
    {
        main(null);
    }
    
    public static void main(String[] args)
    {
        try
        {
            JFrame fr = new MainJFrame();
        }
        catch (IOException ex)
        {
            javax.swing.JOptionPane.showMessageDialog(null, "Opps, something went wrong! Try reloading the page!");
        }
    }
}
