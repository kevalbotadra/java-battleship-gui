package battleship;

import java.awt.Color;

//This has to do with all the ship data
public class Ship{
    //Initialzing some variable
    String name;
    int index;
    int length;
    Color color;
    
    Direction direction;

    int startingX;
    int startingY;
    
    //Ship constructor 
    public Ship(String name, int length, int idx, Color c){
        this.name = name;
        this.length = length;
        this.index = idx;
        this.direction = Direction.UNSET;
        this.color = c;
    }
    
    //Function to set direction of ship
    public void setDirection(Direction direction){
        this.direction = direction;
    }

    //Function to set X and Y coordinates of ship
    public void setXAndY(int x, int y){
        startingX = x;
        startingY = y;
    }

    //Function to get the x-coordinate of ship
    public int getX(){
        return startingX;
    }

    //Function to get the y-coordinate of ship
    public int getY(){
        return startingY;
    }

    //Function to get the index
    public int getIdx(){
        return index;
    }

    //Function to get the direction of the shiip
    public Direction getDirection(){
        return direction;
    }

    //Function to get length of ship
    public int getLength(){
        return length;
    }

    //Function to get color of ship
    public Color getColor(){
        return color;
    }
}