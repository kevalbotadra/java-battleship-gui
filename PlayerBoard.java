package battleship;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.DimensionUIResource;

import org.w3c.dom.events.MouseEvent;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

//This is for the player board where the computer guesses where your ships are
public class PlayerBoard extends JPanel {
    private static int num = 11;

    // variables to keep track of turns
    private boolean turn = false;

    private static GameTile[][] gameTiles;

    private boolean[][] shipPlacements;

    private String[][] hitOrMiss;

    private int lastHits = 0;

    //initializing all the ships
    private Ship carrier;
    private Ship battleship;
    private Ship submarine;
    private Ship destroyer;
    private Ship cruiser;

    private Ship[] ships;
    private Ship[] hitShips = new Ship[5];

    private boolean hit = false;
    private int hitCounter = 0;
    private int lastHitX = -1;
    private int lastHitY = -1;
    private String shipDirection = "None";
    private ArrayList<String> triedDirections = new ArrayList<String>();
    private boolean tryOtherWay = false;
    private boolean bothWaysTried = false;

    //Function that makes player board
    public PlayerBoard(ActionListener listener, Ship carrier, Ship battleship, Ship submarine, Ship destroyer, Ship cruiser) {
        setLayout(new GridLayout(num, num));
        //Sets bounds of board
        setBounds(375, 90, 650, 650);

        shipPlacements = new boolean[10][10];
        hitOrMiss = new String[10][10];

        // define an array of GameTiles equal to 100 (the amount of game tiles that should exist)
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
        for (int i = 0; i < 10; i++) {
            // loop over every column
            for (int k = 0; k < 10; k++) {
                // get the tile at the counter value
                GameTile tile = listGameTiles.get(tileCounter);

                // assign the row value and the column value to the obtained tile
                arrayGameTiles[i][k] = tile;

                hitOrMiss[i][k] = "null";

                tile.row = i;
                tile.column = k;

                // increment the tile counter
                tileCounter++;
            }
        }

        // assign the newly created 2d Game Tile array to the class's game tile array
        gameTiles = arrayGameTiles;

        // set the ships list to all the ships
        ships = new Ship[5];
        ships[0] = carrier;
        ships[1] = battleship;
        ships[2] = submarine;
        ships[3] = cruiser;
        ships[4] = destroyer;

    }
    
    //Disables the buttons
    public void disableButtons() {
        for (int i = 0; i < 10; i++) {
            for (int k = 0; k < 10; k++) {
                gameTiles[i][k].setEnabled(false);
            }
        }
    }

    //Enables the buttons
    public void enableButtons() {
        for (int i = 0; i < 10; i++) {
            for (int k = 0; k < 10; k++) {
                gameTiles[i][k].setEnabled(true);
            }
        }
    }

    //Resets the board
    public void resetBoard() {
        // loops over every row
        for (int i = 0; i < 10; i++) {
            // loop over every column
            for (int k = 0; k < 10; k++) {
                shipPlacements[i][k] = false;
                gameTiles[i][k].setIcon(null);
                gameTiles[i][k].setDisabledIcon(null);
                gameTiles[i][k].revalidate();
                gameTiles[i][k].setBackground(Color.decode("#206d99"));
            }
        }
    }

    //Function for player to place their ships on the player board
    public int placeShip(Ship ship, int xCord, int yCord, Direction direction) {
        ship.setXAndY(xCord, yCord);
        ship.setDirection(direction);
        //Checks if ships are outside the bounds of the board 
        if (direction == Direction.HORIZONTAL) {
            if ((yCord + ship.length) > 11) {
                return 0;
            }
        } else if (direction == Direction.VERTICAL) {
            if (((xCord + 1) - ship.length) < 0) {
                return 0;
            }
        }

        
        int checkCounter = 0;
        //This is to check of there is a ship in the desired spot you want to place your ship in
        for (int i = 0; i < ship.length; i++) {
            if (direction == Direction.HORIZONTAL) {
                if(gameTiles[xCord][(yCord + checkCounter)].getIcon() != null){
                    return 1;
                }

                checkCounter++;
            } else if (direction == Direction.VERTICAL) {
                if(gameTiles[(xCord - checkCounter)][yCord].getIcon() != null){
                    return 1;
                }

                checkCounter++;
            }
        }


        int counter = 0;
        //Adds ships to the tiles if there is no other ships in the way
        for (int i = 0; i < ship.length; i++) {
            if (direction == Direction.HORIZONTAL) {
                ImageIcon imageIcon = new ImageIcon("BattleshipImages/" + ship.name + "/" + (i + 1) + ".png"); // load the image to a
                Image image = imageIcon.getImage(); // transform it
                Image newimg = image.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                imageIcon = new ImageIcon(newimg); // transform it back
                gameTiles[xCord][yCord + counter].setIcon(imageIcon);
                gameTiles[xCord][yCord + counter].setDisabledIcon(imageIcon);


                counter++;
            } else if (direction == Direction.VERTICAL) {
                ImageIcon imageIcon = new ImageIcon("BattleshipImages/" + ship.name + "/" + (i + 1) + "_flip.png"); // load the image to a
                Image image = imageIcon.getImage(); // transform it
                Image newimg = image.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                imageIcon = new ImageIcon(newimg); // transform it back
                gameTiles[(xCord - counter)][yCord].setIcon(imageIcon);
                gameTiles[(xCord - counter)][yCord].setDisabledIcon(imageIcon);

                counter++;
            }
        }

        if(ship.name.equals("Destroyer")){return 3;}

        return 2;
    }

    //This funnction finds out if there is a ship on that tile
    public void gatherShipPlacements(){
        // loops over every row
        for (int i = 0; i < 10; i++) {
            // loop over every column
            for (int k = 0; k < 10; k++) {
                if(gameTiles[i][k].getIcon() != null){
                    shipPlacements[i][k] = true;
                } else {
                    shipPlacements[i][k] = false;
                }
            }
        }
    }

    //This function randomizes the ship placements for the randomize button
    public void randomizeBoard(){
        resetBoard();
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
                
                //Checks to see if there is overlap for horizontal ships
                if (direction == Direction.HORIZONTAL) {
                    if (y > (10 - ship.length)) {
                        y = y - ship.length;
                    }

                    for (int i = 0; i < ship.length; i++) {
                        System.out.println(x + ", " + (y + i));
                        if (gameTiles[x][y + i].getIcon() != null) {
                            System.out.println("overlap " + x + ", " + y);
                            overlap = true;
                        }
                    }
                }

                //Checks to see if there is overlap for horizontal ships
                if (direction == Direction.VERTICAL) {
                    if (x < (10 - ship.length)) {
                        x = x + ship.length;
                    }

                    for (int i = 0; i < ship.length; i++) {
                        if (gameTiles[x - i][y].getIcon() != null) {
                            System.out.println("overlap " + x + ", " + y);
                            overlap = true;
                        }
                    }
                }

                //Condition for when there is no overlap
                if (!overlap) {
                    //Places ship horizontally 
                    if (direction == Direction.HORIZONTAL) {
                        ship.direction = direction;
                        if (y > (10 - ship.length)) {
                            y = y - ship.length;
                        }

                        for (int i = 0; i < ship.length; i++) {
                            ImageIcon imageIcon = new ImageIcon("BattleshipImages/" + ship.name + "/" + (i + 1) + ".png"); // load the image to a
                            Image image = imageIcon.getImage(); // transform it
                            Image newimg = image.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                            imageIcon = new ImageIcon(newimg); // transform it back
                            gameTiles[x][y + i].setIcon(imageIcon);
                            gameTiles[x][y + i].setDisabledIcon(imageIcon);
                        }
                    }

                    //Places ship horizontally 
                    if (direction == Direction.VERTICAL) {
                        ship.direction = direction;
                        if (x < (10 - ship.length)) {
                            x = x + ship.length;
                        }

                        for (int i = 0; i < ship.length; i++) {
                            ImageIcon imageIcon = new ImageIcon("BattleshipImages/" + ship.name + "/" + (i + 1) + "_flip.png"); // load the image to a
                            Image image = imageIcon.getImage(); // transform it
                            Image newimg = image.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                            imageIcon = new ImageIcon(newimg); // transform it back
                            gameTiles[x - i][y].setIcon(imageIcon);
                            gameTiles[x - i][y].setDisabledIcon(imageIcon);
                        }
                    }

                    //Once the ships are randomly placed it sets a new x and y coordinate
                    ship.setXAndY(x, y);
                    break;
                }
            }
        }
    }

    //Sets all varibale back to default values
    public void resetRandomHitOrMissVariables(){
        hit = false;
        hitCounter = 0;
        lastHitX = -1;
        lastHitY = -1;
        shipDirection = "None";
        triedDirections.clear();
        tryOtherWay = false;
        bothWaysTried = false;
    }

    //This function checks to see if there are hits around a hit tile
    public int randomHitOrMiss() {  
        Random rand = new Random();

        String trialHitDirection = "";
        int x = -1;
        int y = -1;

        //Checks if there was a hit last time
        if (lastHitX == -1 && lastHitY == -1){
            x = rand.nextInt(10);
            y = rand.nextInt(10);

            if(hitOrMiss[x][y].equals("Miss") || hitOrMiss[x][y].equals("Hit")){
                resetRandomHitOrMissVariables();
                return 1;
            }

        } else {
            x = lastHitX;
            y = lastHitY;
        }

        
        if(hitCounter == 1){
            System.out.println("choosing a random spot around the last hit value of " + x + ", " + y);
            String[] sides = {"Up", "Down", "Left", "Right"};
            lastHitX = x;
            lastHitY = y;
            while (true){
                int i = 0;
                //Chooes a random side to check
                trialHitDirection = sides[rand.nextInt(sides.length)];
                int counter = 0;
                //Updates counter for each side that was tried
                for (String tried : triedDirections){
                    if(trialHitDirection == tried){
                        i = 1;
                        counter++;
                    }  
                }

                //Tries all sides until it tries all sides
                if (counter > 0){
                    continue;
                }
                
                //When it tries all sides it breaks the leep
                if(counter == 4){
                    hitCounter = 1;
                    break;
                }

                System.out.println("Trying side: " + trialHitDirection);
                System.out.println(x + ", " + y);
                //Checks if you can go up
                if (trialHitDirection.equals("Up")){
                    if (!((x - 1) < 0)){
                        x--;
                    } else {
                        triedDirections.add("Up");
                        i = 1;
                    }
                } else if (trialHitDirection.equals("Down")){//Checks if you can go down
                    if (!((x + 1) > 9)){
                        x++;
                    } else {
                        triedDirections.add("Down");
                        i = 1;
                    }
                } else if (trialHitDirection.equals("Right")){ //Checks if you can go right
                    if (!((y + 1) > 9)){
                        y++;
                    } else {
                        triedDirections.add("Right");
                        i = 1;
                    }
                } else if (trialHitDirection.equals("Left")){//Checks if you can go left
                    if (!((y - 1) < 0)){
                        y--;
                    } else {
                        triedDirections.add("Left");
                        i = 1;
                    }
                }

                //All the direction have been tried and i is still 0 so it breaks the loop
                if (i == 0){
                    break;
                }

                System.out.println("Already tried");


            }
        } 
        
        if (hitCounter == 2){
            System.out.println("detected that we have a " + shipDirection + " ship. Now gonna increment stuff.");
            System.out.println(x + ", " + y);
            //When ship is vertical
            if (shipDirection.equals("Vertical")){
                while(true){
                    System.out.println("tryOtherWay is " + tryOtherWay);
                    if(tryOtherWay){
                        if (!((x + 1) > 9)){
                            System.out.println("incremented down");
                            x++;
                        } else {
                            bothWaysTried = true;
                            break;
                        }
                    } else {
                        //Checks if you can increment up or will try the other way
                        if (!((x - 1) < 0)){
                            System.out.println("incremented up");
                            x--;
                        } else {
                            System.out.println("here");
                            tryOtherWay = true;
                            break;
                        }
                    }

                    for (int i = 0; i < 5; i++){
                        System.out.println("got here" + x + ", " + y);
                        System.out.println(hitOrMiss[x][y]);
                        if((hitOrMiss[x][y].equals("Hit"))){ // checks it the space has been checked already
                            if(tryOtherWay){
                                //Will try going down
                                if(trialHitDirection.equals("Down")){
                                    System.out.println("x + 1 is " + x + 1);
                                    //Keeps on going down because there is something there
                                    if (!(x == 9)){
                                        System.out.println("detected some shtuff so incremented the x down again");
                                        x++;
                                    } else {//Nothing there so breaks the llops
                                        bothWaysTried = true;
                                        System.out.println("broke da for loop");
                                        break;
                                    }
                                } else {
                                    System.out.println("x + 1 is " + x + 1);
                                    if (!((x + 1) > 9)){
                                        System.out.println("detected some shtuff so incremented the x down again");
                                        x++;
                                    } else {
                                        bothWaysTried = true;
                                        break;
                                    }
                                }
                            } else {
                                //Will try going up
                                if(trialHitDirection.equals("Up")){
                                    System.out.println("x - 1 is " + x + 1);
                                    if (!(x == 0)){
                                        System.out.println("detected some shtuff so incremented the x up again");
                                        x--;
                                    } else {
                                        tryOtherWay = true;
                                        break;
                                    }
                                } else {
                                    if (!((x - 1) < 0)){
                                        System.out.println("x - 1 is " + x + 1);
                                        System.out.println("detected some shtuff so incremented the x up again");
                                        x--;
                                    } else {
                                        tryOtherWay = true;
                                        break;
                                    }
                                }
                                
                            }
                        } else {
                            System.out.println("broke the for loop");
                            break;
                        }
                    }

                    break;
                }
            } else {
                while(true){
                    //Checks the right and left of the next tile
                    if(tryOtherWay){
                        if (!((y - 1) < 0)){
                            System.out.println("detected some shtuff so incremented the y right again");
                            y--;
                        } else {
                            System.out.println("set both ways tried to true");
                            bothWaysTried = true;
                            break;
                        }
                    } else {
                        if (!((y + 1) > 9)){
                            System.out.println("detected some shtuff so incremented the y left again");
                            y++;
                        } else {
                            tryOtherWay = true;
                            System.out.println("set try other way tried to true");
                            break;
                        }
                    }
                    for (int i = 0; i < 5; i++){
                        System.out.println("got here" + x + ", " + y);
                        System.out.println(hitOrMiss[x][y]);
                        if((hitOrMiss[x][y].equals("Hit") || hitOrMiss[x][y].equals("Miss"))){ // checks it the space has been checked already
                            if(tryOtherWay){
                                //Checks the right of the tile
                                if(trialHitDirection.equals("Right")){
                                    System.out.println("y - 1 is " + x + 1);
                                    if (!(y == 0)){
                                        System.out.println("incremented right");
                                        y--;
                                    } else {
                                        bothWaysTried = true;
                                        break;
                                    }
                                } else {
                                    System.out.println("y - 1 is " + x + 1);
                                    if (!((y - 1) <= 0)){
                                        System.out.println("incremented right");
                                        y--;
                                    } else {
                                        bothWaysTried = true;
                                        break;
                                    }
                                }
                            } else {
                                //Checks the left of the tile
                                if(trialHitDirection.equals("Left")){
                                    System.out.println("y + 1 is " + x + 1);
                                    if (!(y == 9)){
                                        System.out.println("incremented left");
                                        y++;
                                    } else {
                                        tryOtherWay = true;
                                        break;
                                    }
                                } else {
                                    System.out.println("y + 1 is " + x + 1);
                                    if (!((y + 1) > 9)){
                                        System.out.println("incremented left");
                                        y++;
                                    } else {
                                        tryOtherWay = true;
                                        break;
                                    }
                                }
                            }
                        } else {
                            break;
                        }
                    }
                    break;
                }
            }
        }
        
        if(shipPlacements[x][y]){ // indicates if the xCord and yCord is a hit
            if (hitCounter == 0){
                System.out.print("hitCounter equals 0 triggered");
                lastHitX = x;
                lastHitY = y;
                hitCounter ++;
            } else if (hitCounter == 1){
                System.out.println(x + ", " + y);
                if (trialHitDirection.equals("Up") || trialHitDirection.equals("Down")){
                    shipDirection = "Vertical";
                } else {
                    shipDirection = "Horizontal";
                }
                System.out.println("boom the ship is " + shipDirection);
                hitCounter++;
            }

            hitOrMiss[x][y] = "Hit";
            

            int shipIdx = getShipFromXAndY(x, y);
            gameTiles[x][y].setIcon(null);
            gameTiles[x][y].setDisabledIcon(null);
            gameTiles[x][y].setBackground(Color.RED);

            if (bothWaysTried){
                resetRandomHitOrMissVariables();
                return 0;
            }
            return 1;
        } else {
            if (hitCounter == 1){
                triedDirections.add(trialHitDirection);
            }

            if (hitCounter == 2){
                if (!tryOtherWay){
                    System.out.println("we changed tryOtherWay");
                    tryOtherWay = true;
                } else {
                    bothWaysTried = true;
                }
            }


            if (bothWaysTried){
                resetRandomHitOrMissVariables();
            }

            hitOrMiss[x][y] = "Miss";
            gameTiles[x][y].setBackground(Color.GREEN);

            return 0;
        }
    }

    //Checks if there is a hit or miss
    public boolean checkHitOrMiss(int x, int y){
        if (hitOrMiss[x][y].equals("Hit") || hitOrMiss[x][y].equals("Miss")){
            return true;         
        }
        return false;
    }
   
    //This gets ship's index from x and y coordinates
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

    //This is the button to erase the last ship
    public void eraseShip(Ship ship, int xCord, int yCord) {
        System.out.println(ship.name + ship.direction);
        int counter = 0;
        for (int i = 0; i < ship.length; i++) {
            if (ship.direction == Direction.HORIZONTAL) {
                gameTiles[xCord][(yCord + counter)].setIcon(null);
                gameTiles[xCord][(yCord + counter)].revalidate();

                counter++;
            } else if (ship.direction == Direction.VERTICAL) {
                gameTiles[(xCord - counter)][yCord].setIcon(null);
                gameTiles[(xCord - counter)][yCord].revalidate();

                counter++;
            }
        }
    }

    //Function to play audio clip
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

    //This takes the hit ships and replaces it with the hit ship images
    public Ship[] getHitShips(){
        for (Ship ship : ships){
            int numHits = 0;
            for (int i = 0; i < ship.length; i++){
                if (ship.getDirection() == Direction.VERTICAL){
                    if(hitOrMiss[ship.getX() - i][ship.getY()].equals("Hit")){
                        numHits ++;
                    } 
                } else {
                    if(hitOrMiss[ship.getX()][ship.getY() + i].equals("Hit")){
                        numHits ++;
                    } 
                }
                
            }

            if (numHits == ship.length){
                hitShips[ship.getIdx()] = ship;
                for(int i = 0; i < ship.length; i++){
                    //
                    if(ship.getDirection() == Direction.VERTICAL){
                        ImageIcon imageIcon = new ImageIcon("BattleshipImages/" + ship.name + "/" + (i + 1) + "_flip_hit.png"); // load the image to a
                        Image image = imageIcon.getImage(); // transform it
                        Image newimg = image.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                        imageIcon = new ImageIcon(newimg); // transform it back
                        gameTiles[ship.getX() - i][ship.getY()].setIcon(imageIcon);
                        gameTiles[ship.getX() - i][ship.getY()].setDisabledIcon(imageIcon);
                    } else {
                        ImageIcon imageIcon = new ImageIcon("BattleshipImages/" + ship.name + "/" + (i + 1) + "_hit.png"); // load the image to a
                        Image image = imageIcon.getImage(); // transform it
                        Image newimg = image.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                        imageIcon = new ImageIcon(newimg); // transform it back
                        gameTiles[ship.getX()][ship.getY() + i].setIcon(imageIcon);
                        gameTiles[ship.getX()][ship.getY() + i].setDisabledIcon(imageIcon);
                    }
                    
                }
            }
        }

        return hitShips;
    }
}


