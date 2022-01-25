package battleship;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.plaf.DimensionUIResource;

import java.awt.*;
import java.awt.event.*;

//All the image info for the ships
public class ShipInfo extends JPanel {
    private JButton shipImage;
    private JLabel status;
    
    //Function to get ship on the board
    public ShipInfo(ActionListener listener, int x, int y, String imageIconPath){
        setLayout(new GridLayout(1, 2));
        setBounds(x, y, 200, 50);
        
        shipImage = new JButton();
        shipImage.setFocusPainted(false);
        shipImage.addActionListener(listener);
        ImageIcon imageIcon = new ImageIcon(imageIconPath); // load the image to a
        Image image = imageIcon.getImage(); // transform it
        Image newimg = image.getScaledInstance(100, 40, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        imageIcon = new ImageIcon(newimg); // transform it back
        shipImage.setIcon(imageIcon);
        shipImage.setDisabledIcon(imageIcon);
        shipImage.setEnabled(false);
        this.add(shipImage);

        status = new JLabel(" X"); 
        this.add(status);
    }

    //Changes the status of if the ship is hit or not(X or Check mark)
    public void setStatus(String newStatus){
        status.setText(newStatus);
    }
}   
