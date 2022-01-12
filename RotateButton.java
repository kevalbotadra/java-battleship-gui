package battleship;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.plaf.DimensionUIResource;

import java.awt.*;
import java.awt.event.*;

public class RotateButton extends JButton {
    public RotateButton(ActionListener listener){
        super("Rotate Ship");
        setBounds(830, 5, 100, 50);

        setFocusPainted(false);
        setActionCommand("rotate");
        addActionListener(listener);
    }
}   
