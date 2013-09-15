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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
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
    private static int RESULT = 3;
    private int step = 0;
    List<String>[] rows = new List[3];
    private final ButtonGroup group;
    private final JRadioButton btn1;
    private final JRadioButton btn2;
    private final JRadioButton btn3;
    private final Component container;
    private final JButton resetBtn = new JButton("Replay");
    private final JPanel resetPanel = new JPanel();
    public ThreeRows(Component c) throws IOException
    {
        step = 0;
        container = c;
        
        resetBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    step = 0;
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

        // three rows
        init();
    }

    public int getStep()
    {
        return step;
    }

    private final void init() throws IOException
    {
        if (nums.size() < 21)
            nums = getList(52);
        rows[0] = getRandom(nums);
        rows[1] = getRandom(nums);
        rows[2] = getRandom(nums);

        repaintPanel();
    }
    
    private static List<Byte> nums = getList(52);
    private static Random rand = new Random();
    private static List<Byte> getList(int count)
    {
        List<Byte> ret = new LinkedList<Byte>();
        for (byte n = 1; n <= (byte)count; ++n)
        {
            ret.add(n);
        }
        return ret;
    }
    
    private static List<String> getRandom(List<Byte> pool)
    {
        if (pool.size()  < 7)
        {
            throw new IllegalArgumentException("Not enough items in pool! " + pool);
        }

        List<String> ret = new ArrayList<String>(7);
        for (int n = 0; n < 7; ++n)
        {
            ret.add("/deck/" + pool.remove(rand.nextInt(pool.size())) + ".png");
        }
        return ret;
    }
    
    static InputStream toStream(String path)
    {
        return ThreeRows.class.getResourceAsStream(path);
    }
    
    final void paintResult(String num) throws IOException
    {
        this.removeAll();
        setLayout(new BorderLayout());
        
        // north
        JPanel n = new JPanel();
        n.add(INS[RESULT]);
        add(n, BorderLayout.NORTH);
        
        // central pn
        JPanel c = new JPanel();
        BufferedImage cardPic = ImageIO.read(getClass().getResourceAsStream(num));
        JLabel cardLabel = new JLabel(new ImageIcon(cardPic.getScaledInstance(80, 116, Image.SCALE_SMOOTH)));
        c.add(cardLabel);
        add(c, BorderLayout.CENTER);
        
        // south
        add(resetPanel, BorderLayout.SOUTH);
        
        this.repaint();
        container.setSize(container.getWidth(), container.getHeight() + 1);
        container.repaint();
        
    }

    final void repaintPanel() throws IOException
    {
        this.removeAll();
       // group.clearSelection();

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
        
        // repaint
        int w = container.getWidth();
        int h = container.getHeight();
        container.setSize(w, h + 1); // TODO: hacky way to force the frame to repaint! Fix it!
        container.setSize(w, h);
        container.repaint();
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
            ++step;
            if (step == 3)
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
        
        private void mix(List<String>[] rows, int a, int b, int c)
        {
            List<String> first = new ArrayList<String>(7);
            List<String> second = new ArrayList<String>(7);
            List<String> third = new ArrayList<String>(7);
            
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
    }
    private static class CardColumn extends JPanel
    {
        private static final int W = 60;
        private static final int H = 87;
        public CardColumn (final List<String> cards, JRadioButton btn) throws IOException
        {
            super(new GridLayout(1, 8));
            if (cards.size() != 7)
            {
                throw new IllegalArgumentException("Expecting 7 cards only!");
            }
            
            setBorder(BorderFactory.createEtchedBorder());
            
            // cards
            for (String card : cards)
            {
                BufferedImage cardPic = ImageIO.read(ThreeRows.class.getResourceAsStream(card));
                JLabel cardLabel = new JLabel(new ImageIcon(cardPic.getScaledInstance(W, H, Image.SCALE_SMOOTH)));
                add(cardLabel);
            }

            //  button
            JPanel btnPanel = new JPanel(new BorderLayout());
            btnPanel.add(btn, BorderLayout.CENTER);
            add(btnPanel);
        }
    }
}
