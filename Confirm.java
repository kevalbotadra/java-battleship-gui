package battleship;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.plaf.DimensionUIResource;

import java.awt.*;
import java.awt.event.*;

public class Confirm extends JPanel {
    private JButton confirmButton;
    private JLabel tileLocation;
    
    public Confirm(ActionListener listener){
        setLayout(new GridLayout(1, 2));
        setBounds(830, 5, 300, 50);
        

        confirmButton = new JButton("<html><center>Confirm</center></html>");
        confirmButton.setFocusPainted(false);
        confirmButton.setActionCommand("confirm");
        confirmButton.addActionListener(listener);
        this.add(confirmButton);

        tileLocation = new JLabel(); 
        this.add(tileLocation);
    }

    public void setTileLocation(String newStatus){
        tileLocation.setText(newStatus);
    }
}  
