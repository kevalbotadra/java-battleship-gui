package battleship;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.DimensionUIResource;

import org.w3c.dom.events.MouseEvent;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class PlayerBoard extends JPanel {
    private static int num = 11;

    // variables to keep track of turns
    private boolean turn = false;

    private static GameTile[][] gameTiles;

    private boolean[][] shipPlacements;

    private boolean[][] hitOrMiss;
 

    public PlayerBoard(ActionListener listener) {
        setLayout(new GridLayout(num, num));
        setBounds(375, 75, 650, 650);


        shipPlacements = new boolean[10][10];

        // define an array of GameTiles equal to 100 (the amount of game tiles that should exist)
        ArrayList<GameTile> listGameTiles = new ArrayList<GameTile>();

        // iterate over the amount of tiles in this case 121
        for (int i = 0; i < num * num; i ++){
            // defines a string array that contains characters uptil J
            String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};

            
            // if i is a multiple of 11 and is not 0
            // add a new tile with the letter corresponding to the box 
            // for example 1 is A, 2 is B, so on and so forth
            // gets our vertical A-J letter labels
            if (i % 11 == 0 && i !=0){
                this.add(new Tile(alphabet[(i/11) - 1]));
            } else if (i <= 10 && i != 0){
                // if the number is less than or equal to 10 and not equal to 0
                // place a tile with the number of the column
                // this just gets the column numbers
                this.add(new Tile(Integer.toString(i)));
            } else if (i == 0){
                // if we are at the first box just add a blank tile
                this.add(new Tile(""));
            } else {
                // create a new tile, add it to the gameTiles list, and place it at the i value in the gridlayout
                GameTile gameTile = new GameTile("playerBoardTile", true);
                gameTile.addActionListener(listener);
                listGameTiles.add(gameTile);
                this.add(gameTile);
            }
        }

        // reshape the gameTiles arraylist so it is 2D
        GameTile[][] arrayGameTiles = new GameTile[num][num];

        // create a counter for the tiles
        int tileCounter = 0;

        // loops over every row
        for (int i = 0; i < 10; i++){
            // loop over every column
            for (int k = 0; k < 10; k++){
                // get the tile at the counter value
                GameTile tile = listGameTiles.get(tileCounter);

                // assign the row value and the column value to the obtained tile
                arrayGameTiles[i][k] = tile;

                tile.row = i;
                tile.column = k;

                // increment the tile counter
                tileCounter++;
            }
        }

        // assign the newly created 2d Game Tile array to the class's game tile array
        gameTiles = arrayGameTiles;
        
    }

    public void disableButtons(){
        for (int i = 0; i < 10; i++){
            for (int k = 0; k < 10; k++){
                gameTiles[i][k].setEnabled(false);
            }
        }
    }

    public void enableButtons(){
        for (int i = 0; i < 10; i++){
            for (int k = 0; k < 10; k++){
                gameTiles[i][k].setEnabled(true);
            }
        }
    }

    public void placeShip(Ship ship, int xCord, int yCord, Direction direction){
        if (direction == Direction.HORIZONTAL){
            if((xCord + ship.length) > 10 || (xCord - ship.length) < 0){
                return; 
            }
        } else if (direction == Direction.VERTICAL){
            if((yCord + ship.length) > 10 || (yCord - ship.length) < 0){
                return;
            }
        }

        int counter = 1;
        for (int i = 0; i < ship.length; i++){
            if (direction == Direction.HORIZONTAL){
                
            } else if (direction == Direction.VERTICAL){

            }
        }


    }
}
