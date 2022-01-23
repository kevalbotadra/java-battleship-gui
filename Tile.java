package battleship;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.plaf.DimensionUIResource;

import java.awt.*;
import java.awt.event.*;

class Tile extends JLabel {
    //Creates the outer tile labels for the boards(A,B,C and 1,2,3)
    public Tile(String label) {
        super(label);
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setVerticalAlignment(SwingConstants.CENTER);
    }
}