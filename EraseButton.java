package battleship;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.plaf.DimensionUIResource;

import java.awt.*;
import java.awt.event.*;

//This makes the button to erase your last ship placed or all the ships placed if you are not happy with your placements
public class EraseButton extends JButton {
    public EraseButton(ActionListener listener){
        super("<html><center>Erase<br>Last Ship</center></html>");
        setBounds(930, 5, 110, 50);

        setFocusPainted(false);
        setActionCommand("erase");
        addActionListener(listener);
    }
}   
