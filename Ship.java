package battleship;

public class Ship{
    String name;
    int length;
    int col;
    int row;
    int direction;

    public static final int unset = -1;
    public static final int horizontal = 0;
    public static final int vertical = 1;
    
    public Ship(String name, int length){
        this.name = name;
        this.length = length;
        this.row = -1;
        this.col = -1;
        this.direction = unset;
    }

    public boolean isLocationSet(){
        if(row == -1 && col == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public void setLocation(int row, int col){
        this.row = row;
        this.col = col;
    }

    public boolean isDirectionSet(){
        if(direction == unset){
            return false;
        }
        else{
            return true;
        }
    }
    
    public void setDirection(int direction){
        if(direction != unset && direction != horizontal && direction != vertical){
            this.direction = direction;
        }
    }

    public int getcol(){
        return col;
    }

    public int getrow(){
        return row;
    }

    public int getDirection(){
        return direction;
    }

    public int getLength(){
        return length;
    }

    //public String directionToString(){
            
    //// }
}