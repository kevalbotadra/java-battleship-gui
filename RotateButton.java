package battleship;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.plaf.DimensionUIResource;

import java.awt.*;
import java.awt.event.*;

//This is a button to rotate your ship
public class RotateButton extends JButton {
    //Function to roatate ship
    public RotateButton(ActionListener listener){
        //This will be what the button says on the user interface
        super("Rotate Ship");
        //Bounds of the button
        setBounds(830, 5, 100, 50);

        setFocusPainted(false);
        setActionCommand("rotate");
        addActionListener(listener);
    }
}   
