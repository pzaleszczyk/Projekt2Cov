package App;

import org.jongo.Jongo;
import org.jongo.MongoCollection;
import java.rmi.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

public class Database implements IDatabase {
	private MongoCollection data;	
	
	public Database() throws UnknownHostException {
		this.setup();
	}
	
	@SuppressWarnings({ "deprecation", "resource" })
	private void setup() throws UnknownHostException{
		DB db = new MongoClient().getDB("Baza");
		Jongo jongo = new Jongo(db);
		data = jongo.getCollection("moves");
	}
	
	public void save(Moves move) {
		if(move == null)
			throw new IllegalArgumentException();
		data.save(move);
	}

	
	public void remove(int i) {
		data.remove("{_id: "+i+"}");
	}
	
	@SuppressWarnings({ "deprecation", "resource" })
	public void clear() {
		DB db = new MongoClient().getDB("Baza");
		db.getCollection("moves").drop();
	}

	public int getRow(int id) {
		Moves result = data.findOne("{_id: "+id+"}").as(Moves.class);
		return result.row;
	}

	public int getColumn(int id) {
		Moves result = data.findOne("{_id: "+id+"}").as(Moves.class);
		return result.column;
	}

	public Color getColor(int id) {
		Moves result = data.findOne("{_id: "+id+"}").as(Moves.class);
		return result.color;
	}

	public long getCount() {
		long moves = data.count();
		return moves;
	}

	public List<Moves> getListOfMoves() {
		List<Moves> list = new ArrayList<Moves>();
		data.find().as(Moves.class).iterator().forEachRemaining(list::add);
		return list;
	}

	@Override
	public void setListOfMoves(List<Moves> moves) {
		for(Moves move : moves) {
			save(move);
		}
	}

	@Override
	public boolean noConnection() {
		return false;
	}	
}
