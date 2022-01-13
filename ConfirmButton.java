package battleship;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.plaf.DimensionUIResource;

import java.awt.*;
import java.awt.event.*;

public class ConfirmButton extends JButton {
    public ConfirmButton(ActionListener listener){
        super("<html><center>Confirm</center></html>");
        setBounds(830, 5, 100, 50);

        setFocusPainted(false);
        setActionCommand("confirm");
        addActionListener(listener);
    }
}   
