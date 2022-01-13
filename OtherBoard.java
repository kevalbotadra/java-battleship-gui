package battleship;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.DimensionUIResource;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class OtherBoard extends JPanel {
    private static int num = 11;

    private GameTile[][] gameTiles;

    private boolean[][] shipPlacements;

    private boolean[][] hitOrMiss;
    
    private Ship carrier;
    private Ship battleship;
    private Ship submarine;
    private Ship destroyer;
    private Ship cruiser;

    private Ship[] ships;

    public OtherBoard(ActionListener listener) {
        super(new GridLayout(num, num)); // created a grid layout 11 x 11

        this.setBounds(25, 20, 325, 325);

        hitOrMiss = new boolean[10][10];

        // define an array of GameTiles equal to 100 (the amount of game tiles that
        // should exist)
        ArrayList<GameTile> listGameTiles = new ArrayList<GameTile>();

        // iterate over the amount of tiles in this case 121
        for (int i = 0; i < num * num; i++) {
            // defines a string array that contains characters uptil J
            String[] alphabet = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" };

            // if i is a multiple of 11 and is not 0
            // add a new tile with the letter corresponding to the box
            // for example 1 is A, 2 is B, so on and so forth
            // gets our vertical A-J letter labels
            if (i % 11 == 0 && i != 0) {
                this.add(new Tile(alphabet[(i / 11) - 1]));
            } else if (i <= 10 && i != 0) {
                // if the number is less than or equal to 10 and not equal to 0
                // place a tile with the number of the column
                // this just gets the column numbers
                this.add(new Tile(Integer.toString(i)));
            } else if (i == 0) {
                // if we are at the first box just add a blank tile
                this.add(new Tile(""));
            } else {
                // create a new tile, add it to the gameTiles list, and place it at the i value
                // in the gridlayout
                GameTile gameTile = new GameTile("otherBoardTile",true);
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
        for (int i = 0; i < 10; i++) {
            // loop over every column
            for (int k = 0; k < 10; k++) {
                // get the tile at the counter value
                GameTile tile = listGameTiles.get(tileCounter);
                tile.row = i;
                tile.column = k;

                // assign the row value and the column value to the obtained tile
                arrayGameTiles[i][k] = tile;

                // increment the tile counter
                tileCounter++;
            }
        }

        

        // assign the newly created 2d Game Tile array to the class's game tile array
        gameTiles = arrayGameTiles;

        // create the boolean array which will hold our ship placements
        shipPlacements = new boolean[10][10];

        // create our 5 ships
        carrier = new Ship("Carrier", 5, 0, Color.decode("#18202e"));
        battleship = new Ship("Battleship", 4, 1, Color.decode("#415a74"));
        submarine = new Ship("Submarine", 3, 2, Color.decode("#d0d0d2"));
        cruiser = new Ship("Cruiser", 3, 3, Color.decode("#f3cba0"));
        destroyer = new Ship("Destroyer", 2, 4, Color.decode("#c3989e"));

        // set the ships list to all the ships
        ships = new Ship[5];
        ships[0] = carrier;
        ships[1] = battleship;
        ships[2] = submarine;
        ships[3] = cruiser;
        ships[4] = destroyer;

        setShips();
    }

    public void setShips() {
        // instantate a random class
        Random rand = new Random();

        // iterate over every ship
        for (Ship ship : ships) {
            while (true) {
                // generate a random x and y coordinate
                int x = rand.nextInt(10);
                int y = rand.nextInt(10);

                // generate a random direction (horizontal : 0 or vertical : 1)
                int directionIdx = rand.nextInt(2);

                Direction direction;
                if(directionIdx == 0){
                    direction = Direction.HORIZONTAL;
                } else {
                    direction = Direction.VERTICAL;
                }

                // overlap boolean indicates if overlap exists
                boolean overlap = false;
                if (direction == Direction.HORIZONTAL) {
                    if (y > (10 - ship.length)) {
                        y = y - ship.length;
                    }

                    for (int i = 0; i < ship.length; i++) {
                        if (shipPlacements[x][y + i]) {
                            overlap = true;
                        }
                    }
                }

                if (direction == Direction.VERTICAL) {
                    if (x < (10 - ship.length)) {
                        x = x + ship.length;
                    }

                    for (int i = 0; i < ship.length; i++) {
                        if (shipPlacements[x - i][y]) {
                            overlap = true;
                        }
                    }
                }

                if (!overlap) {
                    if (direction == Direction.HORIZONTAL) {
                        ship.direction = direction;
                        if (y > (10 - ship.length)) {
                            y = y - ship.length;
                        }

                        ship.setXAndY(x, y);

                        for (int i = 0; i < ship.length; i++) {
                            shipPlacements[x][y + i] = true;
                        }
                    }

                    if (direction == Direction.VERTICAL) {
                        ship.direction = direction;
                        if (x < (10 - ship.length)) {
                            x = x + ship.length;
                        }

                        ship.setXAndY(x, y);

                        for (int i = 0; i < ship.length; i++) {
                            shipPlacements[x - i][y] = true;
                        }
                    }
                    break;
                }
            }
        }
    }

    public void clearBoard(){
        // loops over every row
        for (int i = 0; i < 10; i++) {
            // loop over every column
            for (int k = 0; k < 10; k++) {
                shipPlacements[i][k] = false;
                gameTiles[i][k].setBackground(Color.decode("#206d99"));
            }
        }
    }

    public void resetBoard(){
        this.clearBoard();
        this.setShips();
    }
    
    public void setColorTile(int xCord, int yCord, Color color){
        gameTiles[xCord][yCord].setBackground(color);
    }

    public int checkHitOrMiss(int xCord, int yCord) {
        if(shipPlacements[xCord][yCord]){ // indicates if the xCord and yCord is a hit
            hitOrMiss[xCord][yCord] = true;
            int shipIdx = getShipFromXAndY(xCord, yCord);
            this.setColorTile(xCord, yCord, ships[shipIdx].getColor());
            return 1;
        } else {
            this.setColorTile(xCord, yCord, Color.GREEN);
            return 0;
        }
    }

    public int getShipFromXAndY(int xCord, int yCord){
        for (Ship ship : ships){
            if(ship.direction == Direction.HORIZONTAL){
                for (int i = 0; i < ship.length; i++){
                    if (ship.getX() == xCord && (ship.getY() + i) == yCord){
                        return ship.getIdx();
                    }
                }
            } else {
                for (int i = 0; i < ship.length; i++){
                    if ((ship.getX() - i) == xCord && ship.getY() == yCord){
                        return ship.getIdx();
                    }
                }   
            }
        }

        return 10;
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

}