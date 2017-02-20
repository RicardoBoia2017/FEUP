package gameLogic;

import java.util.concurrent.ThreadLocalRandom;

public class CrazyOgre extends Character {
	
	
	
	public CrazyOgre(int level){
		this.symbol = 'O';
		this.under_char = ' ';
		
		Coord startingPos;
		switch (level) {
		case 1:
			this.symbol = 'X'; // Ogres doesn't appear on 1st level
			startingPos = new Coord(0,0); 
			this.position = startingPos;
			break;
			
		case 2:
			this.symbol = 'X'; // Ogres doesn't appear on 2nd level
			startingPos = new Coord(0,0); 
			this.position = startingPos;
			break;
		case 3:
			startingPos = new Coord(1,4); 
			this.position = startingPos;
			break;

		default:
			break;
		}
		
	}
	
	public CrazyOgre(Coord position){
		this.symbol = 'O';
		this.position = position;
		this.under_char = ' ';
	}

	public Action move(Board board, Direction direction){
		return Action.NOACTION;

	}
	
	public Action move(Board board){
		
		board.setBoardAt(this.position, ' ');
		
		int x = this.position.getX();
		int y = this.position.getY();
		
		boolean valid = false;
		
		
		do {
			Direction direction = randomDirection();
			
			int move = direction.getValue();
			
			if(direction == Direction.DOWN || direction == Direction.UP){
				char nextPos = board.getBoardAt(x+move, y);
				
				if(nextPos == 'X' || nextPos == 'I')
					valid = false;
				
				else{
					if(nextPos == 'k'){
						valid = true;
						this.symbol = '$';
					}
					else if(nextPos == ' '){
						valid = true;
						this.symbol = 'O';
					}
					
					Coord pos = new Coord(x+move, y);

					board.setBoardAt(pos, this.symbol);
					this.position = pos;
				}
			}
			
			else if(direction == Direction.RIGHT || direction == Direction.LEFT){
				char nextPos = board.getBoardAt(x, y+move);
				
				if(nextPos == 'X' || nextPos == 'I')
					valid = false;
				else{
					if(nextPos == 'k'){
						valid = true;
						this.symbol = '$';
					}
					else if(nextPos == ' '){
						valid = true;
						this.symbol = 'O';
					}
					
					Coord pos = new Coord(x, y+move);

					board.setBoardAt(pos, this.symbol);
					this.position = pos;
				}
			}
			
		} while (!valid);
		
		
		
		
		return Action.CRAZYOGRE;

	}
	
	public Direction randomDirection(){

		int randomNum = ThreadLocalRandom.current().nextInt(0, 3 + 1);
		
		switch (randomNum) {
		case 0:
			return Direction.UP;
		case 1:
			return Direction.DOWN;
		case 2:
			return Direction.RIGHT;
		case 3:
			return Direction.LEFT;
		default:
			return Direction.INVALID;
		}
	}
	
}
