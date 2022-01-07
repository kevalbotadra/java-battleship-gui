package battleship;

import javax.print.attribute.standard.MediaSize.Other;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.DimensionUIResource;

import org.w3c.dom.events.MouseEvent;


import java.awt.*;
import java.awt.event.*;

public class Game implements ActionListener {
    private JFrame frame;
    private PlayerBoard playerBoard;
    private OtherBoard otherBoard;

    // variable to keep track of whether or not the user has placed ships
    private boolean shipsPlaced = false;

    public Game(){
        frame = new JFrame();

        frame.setLayout(null);
        frame.setSize(1050, 900);
        


        playerBoard = new PlayerBoard();
        frame.add(playerBoard);


        otherBoard = new OtherBoard();
        frame.add(otherBoard);

        

        setShipsPlacedLabel();        

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.pack();
        frame.setVisible(true);
    }

    public void setShipsPlacedLabel(){
        JLabel shipsPlacedLabel = new JLabel();
        if (!shipsPlaced){
            shipsPlacedLabel.setText("Tap to place ships on the board.");
        } else {
            shipsPlacedLabel.setText("This is your board to keep track of your ships.");
        }
        
        shipsPlacedLabel.setBounds(375, 10, 100, 100);
        frame.add(shipsPlacedLabel);
    }

    public void actionPerformed(ActionEvent e){
        System.out.println("yuh");
    }
}