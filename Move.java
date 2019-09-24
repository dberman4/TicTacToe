
public class Move {

	private int location;
	private Move backtracker;
	
	public Move(int location, Move backtracker){
		this.location = location;
		this.backtracker = backtracker;
	}

	public int getLocation() {
		return location;
	}

	public void setLocation(int location) {
		this.location = location;
	}

	public Move getBacktracker() {
		return backtracker;
	}

	public void setBacktracker(Move backtracker) {
		this.backtracker = backtracker;
	}
}
