package App;

import java.util.List;

public interface IDatabase {
	
	public void save(Moves move);
	public void remove(int i);
	public void clear();
	public int getRow(int id);
	public int getColumn(int id);
	public Color getColor(int id);
	public long getCount();
	public List<Moves> getListOfMoves();
	public void setListOfMoves(List<Moves> moves);
	public boolean noConnection();
}
