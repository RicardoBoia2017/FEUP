package gameLogic;

/**
 * Represents the Rookie Guard.
 * 
 * @author Afonso Pinto and Tom�s Oliveira
 * @see Guard
 * 
 */

public class Rookie extends Guard {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs and initializes a Rookie Guard.
	 * 
	 */
	
	public Rookie(){
		super();
	}
	
	/**
	 * Constructs and initializes a Rookie Guard with position of type Coord.
	 * 
	 * @param position
	 *            the position of the Rookie
	 * @see {@link Coord.java}
	 */
	
	public Rookie(Coord position){
		super(position);
	}
	
	/**
	 * Constructs and initializes a Rookie Guard with level of type int.
	 * 
	 * @param level
	 *           the level of the game
	 */
	
	public Rookie(int level){
		super(level);
		
	}
	
	/**
	 * Moves the Rookie Guard in a certain direction.
	 * 
	 * @param board
	 *            the board of the game
	 */
	
	public Action move(Board board){	
		
		if(++index == route.length)
			index = 0;
		
		board.setBoardAt(this.position, ' ');
		this.position = route[index];
		this.orientation = route_dir[index];
		board.setBoardAt(this.position, this.symbol);
		
		return Action.GUARD;
	}
	
	
}