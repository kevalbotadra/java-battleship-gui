package battleship;

import javax.print.attribute.standard.MediaSize.Other;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
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
    private SideButtons sideButtons;

    private Phase phase;

    // variable to keep track of whether or not the user has placed ships
    private boolean shipsPlaced = false;

    // set whos turn it is
    // 0 player turn
    // 1 robot turn
    private int turn = 0;


    public Game(){
        frame = new JFrame();

        // set the phase to set-up
        phase = Phase.SETUP;

        frame.setLayout(null);
        frame.setSize(1050, 900);
        


        playerBoard = new PlayerBoard(this);
        frame.add(playerBoard);


        otherBoard = new OtherBoard(this);
        // otherBoard.disableButtons();
        frame.add(otherBoard);

        sideButtons = new SideButtons(this);
        frame.add(sideButtons);
        

        setShipsPlacedLabel();     

        

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        

    }

   

    public void setShipsPlacedLabel(){
        JLabel shipsPlacedLabel = new JLabel();
        if (!shipsPlaced){
            shipsPlacedLabel.setText("<html>Tap anywhere on the grid to place your ships on the board.<br>Tap the space bar to rotate the ship.<br>Tap enter to confirm its location.</html>");
        } else {
            shipsPlacedLabel.setText("This is your board to keep track of your ships.");
        }
        
        shipsPlacedLabel.setBounds(500, 0, 400, 100);
        frame.add(shipsPlacedLabel);
    }

    public void setButtons(int turn){
        if (turn == 0){
            playerBoard.enableButtons();
            otherBoard.disableButtons();
        } else {
            otherBoard.enableButtons();
            playerBoard.disableButtons();
        }
    }

    
    public void actionPerformed(ActionEvent e){
        if (e.getActionCommand() == "restart"){
            otherBoard.resetBoard();
            playerBoard.clearBoard();
        }

        if(phase == Phase.SETUP){
            if (e.getActionCommand() == "playerBoardTile"){
                GameTile tappedTile = (GameTile)e.getSource();
                playerBoard.placeShip(tappedTile.row, tappedTile.column);
            }
        }

        if (phase == Phase.GAME){
            if (e.getActionCommand() == "playerBoardTile"){
                GameTile tappedTile = (GameTile)e.getSource();
                turn = 1;
                // setButtons(turn);
            }
    
            if (e.getActionCommand() == "otherBoardTile"){
                GameTile tappedTile = (GameTile)e.getSource();
                otherBoard.checkHitOrMiss(tappedTile.row, tappedTile.column);
                turn = 0;
                // setButtons(turn);
            }
        }
        
    }
}