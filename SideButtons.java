package battleship;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.plaf.DimensionUIResource;

import java.awt.*;
import java.awt.event.*;

public class SideButtons extends JPanel {
    public SideButtons(ActionListener listener){
        setLayout(new GridLayout(1, 2));
        setBounds(50, 650, 300, 100);

        JButton restart = new JButton("Restart");
        restart.setFocusPainted(false);
        restart.setActionCommand("restart");
        restart.addActionListener(listener);
        this.add(restart);

        JButton randomize = new JButton("Randomize"); 
        randomize.setFocusPainted(false);
        randomize.setActionCommand("close");
        randomize.addActionListener(listener);
        this.add(randomize);
    }
}   
