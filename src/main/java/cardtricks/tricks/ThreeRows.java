

package cardtricks.tricks;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;

/**
 * The trick: 
 * - pick a random 21 cards out of a 52-card deck.
 * - deal them into 3 columns
 * - ask the user to *stare* silently at one card
 * - ask them to point to the column that has the card
 * -
 * 
 * @author Vy Thao Nguyen
 */
public class ThreeRows extends JPanel
{
    private static int stage = 1;
    List<Byte>[] rows = new List[3];
   // private final JTextField instruction;
    private final ButtonGroup group;
    private final JRadioButton btn1;
    private final JRadioButton btn2;
    private final JRadioButton btn3;
    private final Component container;
    private static final JButton reset = new JButton("Replay");
    private static JPanel s = new JPanel();
    public ThreeRows(Component c) throws IOException
    {
        stage = 1;
        container = c;
        
        reset.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    stage = 1;
                    init();
                }
                catch (IOException ex)
                {
                    Logger.getLogger(ThreeRows.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        });
        s.add(reset);
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

    public int getStage()
    {
        return stage;
    }

    private final void init() throws IOException
    {
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
    
    private static List<Byte> getRandom(List<Byte> pool)
    {
        if (pool.size()  < 7)
        {
            throw new IllegalArgumentException("Not enough items in pool! " + pool);
        }
        List<Byte> ret = new ArrayList<Byte>(7);
        for (int n = 0; n < 7; ++n)
        {
            ret.add(pool.remove(rand.nextInt(pool.size())));
        }
        return ret;
    }
    
    static InputStream toStream(byte num)
    {
        return ThreeRows.class.getResourceAsStream("/deck/" + num + ".png");
    }
    
    final void paintResult(byte num) throws IOException
    {
        this.removeAll();
        setLayout(new BorderLayout());
        
        // north
        JPanel n = new JPanel();
        n.add(new JLabel("Your card is:"));
        add(n, BorderLayout.NORTH);
        
        // central pn
        JPanel c = new JPanel();
        BufferedImage cardPic = ImageIO.read(toStream(num));
        JLabel cardLabel = new JLabel(new ImageIcon(cardPic.getScaledInstance(80, 116, Image.SCALE_SMOOTH)));
        c.add(cardLabel);
        add(c, BorderLayout.CENTER);
        
        // south
        add(s, BorderLayout.SOUTH);
        
        this.repaint();
        container.setSize(container.getWidth(), container.getHeight() + 1);
        container.repaint();
        
    }

    final void repaintPanel() throws IOException
    {
        this.removeAll();
        setLayout(new BorderLayout());
        
        // cards
        JPanel cards = new JPanel(new GridLayout(3, 1));
        cards.add(new CardColumn(rows[0], btn1));
        cards.add(new CardColumn(rows[1], btn2));
        cards.add(new CardColumn(rows[2], btn3));
        
        // instruction
        JPanel textPanel = new JPanel();
        textPanel.add(new JLabel("Stage - " + stage + ": Select the row that has your card!"));
        
        add(cards, BorderLayout.CENTER);
        add(textPanel, BorderLayout.NORTH);
        this.repaint();
        container.setSize(container.getWidth(), container.getHeight() + 1);
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
            ++stage;
            if (stage == 4)
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
        
        private void mix(List<Byte>[] rows, int a, int b, int c)
        {
            List<Byte> first = new ArrayList<Byte>(7);
            List<Byte> second = new ArrayList<Byte>(7);
            List<Byte> third = new ArrayList<Byte>(7);
            
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
        public CardColumn (final List<Byte> cards, JRadioButton btn) throws IOException
        {
            super(new GridLayout(1, 8));
            if (cards.size() != 7)
            {
                throw new IllegalArgumentException("Expecting 7 cards only!");
            }
            
            setBorder(BorderFactory.createEtchedBorder());
            
            // cards
            for (Byte card : cards)
            {
                BufferedImage cardPic = ImageIO.read(toStream(card));
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
