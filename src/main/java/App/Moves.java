package App;
import java.util.ArrayList;
import java.util.List;

import org.jongo.marshall.jackson.oid.MongoId;

public class Moves{
	@MongoId
    public long key;
	
    public int row;
    public int column;
    public Color color; 
    
    public Moves() {
    }
    
    public Moves(int row, int column, Color color) {
    	this.row = row;
    	this.column = column;
    	this.color = color;
    }
    
    public Moves(int key, int row, int column, Color color) {
    	this.key = key;
    	this.row = row;
    	this.column = column;
    	this.color = color;
    }
    
    public String toString() {
    	return row+"\n"+column+"\n"+color;
    }
    
    public static ArrayList<Moves> toList(List<String> list){
    	ArrayList<Moves> moves = new ArrayList<Moves>();
    	Moves move;
    	for(int i = 0 ; i < list.size(); i+=3) {
    		move = new Moves(
    				Integer.parseInt(list.get(i)),
    				Integer.parseInt(list.get(i+1)),
    				Color.toColor(list.get(i+2))
    				);
    		moves.add(move);
    	}
    	return moves;
    }
    
}