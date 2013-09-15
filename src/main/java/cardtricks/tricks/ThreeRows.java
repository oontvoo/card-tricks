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

package cardtricks.tricks;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;

/**
 * The trick: 
 * - pick a random 21 cards out of a 52-card deck.
 * - deal them into 3 rows/columns 
 * - ask the user to *stare* silently at one card
 * - ask them to point to the column that has the card 3 times
 * - correctly point out which card the user has chosen
 * 
 * @author Vy Thao Nguyen
 */
public class ThreeRows extends JPanel
{
    private static final JLabel INS[] = new JLabel[]
    {
        new JLabel("Step 0 - Mentally pick a card, stare (hard) at it, and select the row that it belongs to!"),
        new JLabel("Step 1 -  Select the row that has your card!"),
        new JLabel("Step 2 -  Select the row that has your card again!"),
        new JLabel("And ... the card you chose is: (Yeah, I know! ;) )")
    };
    private static int RESULT = 3;  // index of the result text in INS
    private static final int W = 60; // height of the card
    private static final int H = 87; // width of the card
    private static Random rand = new Random();
    
    private static final String WAIT_ICON_PATH = "/misc/hourglass.gif";
    
    // wait time (while the cards are being drawn)
    private static final long TIMED_OUT = 10000L;
    
    // splash screen shown while the cards are being drawn
    private final JWindow waitPopup = new WaitWindow();
    private final Object PAINT_LOCK = 0;
    
    private final InitBoardTask initBoardTask = new InitBoardTask();
    
    // pools of cards to choose from
    private List<Byte> nums = getList(52);
    
    // current stage in the game
    private int step;
    
    // the groups of cards
    private List<JLabel>[] rows = new List[3];
    
    private final ButtonGroup group;
    private final JRadioButton btn1;
    private final JRadioButton btn2;
    private final JRadioButton btn3;
    
    // parent frame (some tricks need to be done with the JFRame to get it 
    // to repain properly!!!)
    private final Component container;
    
    // reset (ie., go back to the stage as though the app has just started)
    private final JButton resetBtn = new JButton("Replay");
    private final JPanel resetPanel = new JPanel();

    public ThreeRows(Component c) throws IOException
    {
        container = c;   
        resetBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    //TODO: Why does the splash stil NOT show up on reset???????
                    init();
                }
                catch (IOException ex)
                {
                    Logger.getLogger(ThreeRows.class.getName()).log(Level.SEVERE, null, ex);
                }
            }   
        });
        resetPanel.add(resetBtn);
        
        // set layout
        setLayout(new BorderLayout());
        
        // set border
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        
        // three radio buttons for three rows
        group = new ButtonGroup();
        btn1 = new JRadioButton();
        btn1.addActionListener(new JBtnListener(2, 0, 1));
        
        btn2 = new JRadioButton();
        btn2.addActionListener(new JBtnListener(0, 1, 2));
        
        btn3 = new JRadioButton();
        btn3.addActionListener(new JBtnListener(0, 2, 1));
        
        group.add(btn1);
        group.add(btn2);
        group.add(btn3);

        // init the panel with random cards
        init();
    }

    private static List<Byte> getList(int count)
    {
        List<Byte> ret = new LinkedList<Byte>();
        for (byte n = 1; n <= (byte)count; ++n)
        {
            ret.add(n);
        }
        return ret;
    }
    
    /**
     * 
     * @param pool
     * @return a list of 7 randomly selected cards from the given pool
     * @throws IOException 
     */
    private static List<JLabel> getRandom(List<Byte> pool) throws IOException
    {
        if (pool.size()  < 7)
        {
            throw new IllegalArgumentException("Not enough items in pool! " + pool);
        }

        List<JLabel> ret = new ArrayList<JLabel>(7);
        for (int n = 0; n < 7; ++n)
        {
            BufferedImage cardPic = ImageIO.read(ThreeRows.class.getResourceAsStream("/deck/" + pool.remove(rand.nextInt(pool.size())) + ".png"));
            ret.add(new JLabel(new ImageIcon(cardPic.getScaledInstance(W, H, Image.SCALE_SMOOTH))));
        }
        return ret;
    }
    
    private static void mix(List<JLabel>[] rows, int a, int b, int c)
    {
        List<JLabel> first = new ArrayList<JLabel>(7);
        List<JLabel> second = new ArrayList<JLabel>(7);
        List<JLabel> third = new ArrayList<JLabel>(7);

        first.add(rows[a].get(0));
        first.add(rows[a].get(1));
        first.add(rows[a].get(2));
        first.add(rows[b].get(2));
        first.add(rows[b].get(5));
        first.add(rows[c].get(0));
        first.add(rows[c].get(1));

        second.add(rows[a].get(3));
        second.add(rows[a].get(4));
        second.add(rows[b].get(0));
        second.add(rows[b].get(3));
        second.add(rows[b].get(6));
        second.add(rows[c].get(2));
        second.add(rows[c].get(3));

        third.add(rows[a].get(5));
        third.add(rows[a].get(6));
        third.add(rows[b].get(1));
        third.add(rows[b].get(4));
        third.add(rows[c].get(4));
        third.add(rows[c].get(5));
        third.add(rows[c].get(6));

        rows[0] = first;
        rows[1]= second;
        rows[2] = third;

    }
    
    public int getStep()
    {
        return step;
    }

    private final void init() throws IOException
    {
        waitPopup.setVisible(true);
        new Thread(initBoardTask).start();
        try
        {
            synchronized(PAINT_LOCK)
            {
                PAINT_LOCK.wait(TIMED_OUT);
            }
            //waitPopup.dispose();
            waitPopup.setVisible(false);
        }
        catch (InterruptedException ex)
        {
            JOptionPane.showMessageDialog(null, "Something went wrong! Please try restart the app");
        }
    }

    private final void paintResult(JLabel card) throws IOException
    {
        this.removeAll();
        setLayout(new BorderLayout());
        
        // north
        JPanel n = new JPanel();
        n.add(INS[RESULT]);
        add(n, BorderLayout.NORTH);
        
        // central pn
        JPanel c = new JPanel();
        c.add(card);
        add(c, BorderLayout.CENTER);
        
        // south
        add(resetPanel, BorderLayout.SOUTH);

        container.repaint();
        container.revalidate();        
    }

    private final void repaintPanel() throws IOException
    {
        this.removeAll();
       // group.clearSelection(); probably leave the selected button as is?

        // cards
        JPanel cards = new JPanel(new GridLayout(3, 1));
        cards.add(new CardColumn(rows[0], btn1));
        cards.add(new CardColumn(rows[1], btn2));
        cards.add(new CardColumn(rows[2], btn3));
        
        // instruction
        JPanel textPanel = new JPanel();
        textPanel.add(INS[step]);
        
        add(cards, BorderLayout.CENTER);
        add(textPanel, BorderLayout.NORTH);

        container.repaint();
        container.revalidate();
    }

    private class JBtnListener implements ActionListener
    {
        private final int a, b, c;
        JBtnListener(int a, int b, int c)
        {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (step == 2) // last step
            {
                try
                {
                    paintResult(rows[b].get(3));
                }
                catch (IOException ex)
                {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
                return;
            }
            ++step;
            mix(rows, a, b, c);
            try
            {
                repaintPanel();
            }
            catch (IOException ex)
            {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }
    }

    private static class CardColumn extends JPanel
    {
        public CardColumn (final List<JLabel> cards, JRadioButton btn) throws IOException
        {
            super(new GridLayout(1, 8));
            if (cards.size() != 7)
            {
                throw new IllegalArgumentException("Expecting 7 cards only!");
            }
            
            setBorder(BorderFactory.createEtchedBorder());
            
            // pain the cards onto the panel
            for (JLabel card : cards)
            {
                add(card);
            }

            //  button
            JPanel btnPanel = new JPanel(new BorderLayout());
            btnPanel.add(btn, BorderLayout.CENTER);
            add(btnPanel);
        }
    }
    
    private static class WaitWindow extends JWindow
    {
        WaitWindow()
        {
            setSize(200, 100);
            this.add(getHourGlass());
            this.setLocationRelativeTo(null);
            this.setAlwaysOnTop(true);
        }
    }

    private class InitBoardTask implements Runnable
    {
        @Override
        public void run()
        {
            step = 0;
            if (nums.size() < 21)
                nums = getList(52);
            group.clearSelection();
            try
            {
                rows[0] = getRandom(nums);
                rows[1] = getRandom(nums);
                rows[2] = getRandom(nums);
                repaintPanel();
            }
            catch(IOException ex)
            {
                JOptionPane.showMessageDialog(null, "Error reading image-files");
            }
            
            synchronized(PAINT_LOCK)
            {
                PAINT_LOCK.notifyAll();
            }
        }   
    }

    private static JLabel getHourGlass()
    {
        try
        {
            return new JLabel(new ImageIcon(ThreeRows.class.getResource(WAIT_ICON_PATH)));
        }
        catch (Exception ex)
        {
            System.out.println("Image not found!");
            ex.printStackTrace();
            return new JLabel("PLEASE WAIT"); // use text instead
        }
    }
}
