package App;

import java.util.ArrayList;
import java.util.List;

public class Mockobase implements IDatabase {

	public List<Moves> moves;

	public Mockobase(){
		moves = new ArrayList<Moves>();
	}
	
	public void save(Moves move) {
		if(move == null)
			throw new IllegalArgumentException();
		moves.add(move);
	}
	
	public void remove(int i) {
		moves.remove(i);
	}
	 
	public void clear() {
		moves.clear();
	}

	public int getRow(int id) {
		return moves.get(id).row;
	}

	public int getColumn(int id) {
		return moves.get(id).column;
	}

	public Color getColor(int id) {
		return moves.get(id).color;
	}

	public long getCount() {
		return moves.size();
	}

	public List<Moves> getListOfMoves() {
		return moves;
	}

	@Override
	public void setListOfMoves(List<Moves> moves) {
		this.moves = moves;
	}

	@Override
	public boolean noConnection() {
		return false;
	}		
	
}
