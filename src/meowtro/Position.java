package meowtro;

public class Position {
    
    public int i = 0;
    public int j = 0;
    
    public Position(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public double l2distance(Position other) {
        return Math.sqrt((this.i - other.i) * (this.i - other.i) + (this.j - other.j) * (this.j - other.j));
    }

    public String toString(){
        return String.format("(%d, %d)", i, j); 
    }
}
