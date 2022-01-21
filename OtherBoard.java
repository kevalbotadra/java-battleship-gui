package battleship;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.DimensionUIResource;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

//Makes board for the computer to guess on
public class OtherBoard extends JPanel {
    private static int num = 11;

    private GameTile[][] gameTiles;

    //2-D boolean to place the ships
    private boolean[][] shipPlacements;

    //2-D String to check a hit or a miss
    private String[][] hitOrMiss;

    private int lastHits = 0;
    
    //initializing all the ships
    private Ship carrier;
    private Ship battleship;
    private Ship submarine;
    private Ship destroyer;
    private Ship cruiser;

    private Ship[] ships;
    private Ship[] hitShips;

    public OtherBoard(ActionListener listener) {
        super(new GridLayout(num, num)); // created a grid layout 11 x 11

        //Sets bounds of the board
        this.setBounds(25, 20, 325, 325);

        hitOrMiss = new String[10][10];


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

                hitOrMiss[i][k] = "None";

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

        hitShips = new Ship[5];

        setShips();
    }

    //Returns if it is a hit or a miss
    public String[][] getHitOrMiss(){
        return hitOrMiss;
    }

    public GameTile[][] getGameTiles(){
        return gameTiles;
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
                //If there is an overlap it adjusts the position of the ship to fit on the board
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

    //Clears board
    public void clearBoard(){
        // loops over every row
        for (int i = 0; i < 10; i++) {
            // loop over every column
            for (int k = 0; k < 10; k++) {
                shipPlacements[i][k] = false;
                gameTiles[i][k].setBackground(Color.decode("#206d99"));
                gameTiles[i][k].setIcon(null);
                gameTiles[i][k].setDisabledIcon(null);
                gameTiles[i][k].revalidate();
            }
        }
    }

    //Resets board
    public void resetBoard(){
        this.clearBoard();
        this.setShips();
    }
    
    //Changs the color of the tile
    public void setColorTile(int xCord, int yCord, Color color){
        gameTiles[xCord][yCord].setBackground(color);
    }

    //Checks if it is a hit or a miss and plays a sound if there is a hit
    public int checkHitOrMiss(int xCord, int yCord) {
        if(shipPlacements[xCord][yCord]){ // indicates if the xCord and yCord is a hit
            hitOrMiss[xCord][yCord] = "Hit";
            int shipIdx = getShipFromXAndY(xCord, yCord);
            this.setColorTile(xCord, yCord, ships[shipIdx].getColor());
            playSound("Boom");
            return 1;
        } else {
            hitOrMiss[xCord][yCord] = "Miss";
            this.setColorTile(xCord, yCord, Color.GREEN);
            playSound("Pop");
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

    //Disables the buttons
    public void disableButtons(){
        for (int i = 0; i < 10; i++){
            for (int k = 0; k < 10; k++){
                gameTiles[i][k].setEnabled(false);
            }
        }
    }

    //Enables the buttons
    public void enableButtons(){
        for (int i = 0; i < 10; i++){
            for (int k = 0; k < 10; k++){
                gameTiles[i][k].setEnabled(true);
            }
        }
    }

    public Ship[] getHitShips(){
        int shipHits = 0;
        for (Ship ship : ships){
            int numHits = 0;
            for (int i = 0; i < ship.length; i++){
                if (ship.getDirection() == Direction.VERTICAL){
                    if(hitOrMiss[ship.getX() - i][ship.getY()] == "Hit"){
                        numHits ++;
                    } 
                } else {
                    if(hitOrMiss[ship.getX()][ship.getY() + i] == "Hit"){
                        numHits ++;
                    } 
                }
                
            }

            System.out.println(numHits + ", " + lastHits);

            //Condition for when ship sinks so and image of the hit ship replaces the hit squares that represent the ship
            if (numHits == ship.length){
                shipHits ++;
                hitShips[ship.getIdx()] = ship;
                for(int i = 0; i < ship.length; i++){
                    if(ship.getDirection() == Direction.VERTICAL){
                        ImageIcon imageIcon = new ImageIcon("BattleshipImages/" + ship.name + "/" + (i + 1) + "_flip_hit.png"); // load the image to a
                        Image image = imageIcon.getImage(); // transform it
                        Image newimg = image.getScaledInstance(29, 29, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                        imageIcon = new ImageIcon(newimg); // transform it back
                        gameTiles[ship.getX() - i][ship.getY()].setIcon(imageIcon);
                        gameTiles[ship.getX() - i][ship.getY()].setDisabledIcon(imageIcon);
                    } else {
                        ImageIcon imageIcon = new ImageIcon("BattleshipImages/" + ship.name + "/" + (i + 1) + "_hit.png"); // load the image to a
                        Image image = imageIcon.getImage(); // transform it
                        Image newimg = image.getScaledInstance(29, 29, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                        imageIcon = new ImageIcon(newimg); // transform it back
                        gameTiles[ship.getX()][ship.getY() + i].setIcon(imageIcon);
                        gameTiles[ship.getX()][ship.getY() + i].setDisabledIcon(imageIcon);
                    }
                }

                System.out.println(shipHits + "- " + lastHits);
                
            }
        }

        if(shipHits > lastHits){
            playSound("Blow Up");
        }

        System.out.println(shipHits);

        lastHits = shipHits;

        return hitShips;
    }

    //Plays a sound
    public void playSound(String fileName){
        try {
            // create an input stream for audio with the file define above
            AudioInputStream audioInputStream = AudioSystem
                    .getAudioInputStream(new File("BattleshipSounds/" + fileName + ".wav")); // change "byebye" to "lilnasx" to play industry baby

            // create clip reference
            Clip clip = AudioSystem.getClip();

            // open audioInputStream to the clip
            clip.open(audioInputStream);

            // start the audio clip 
            clip.start();

        } catch (Exception e) {
            System.out.println("RIP NO MUSICA :(");
        }
    }
}
