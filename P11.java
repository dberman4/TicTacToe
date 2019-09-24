import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class P11 {
	private static final int STARTING_MOVE = 4;       // the first move of the player X
	private static char winner;
	private static int numOptimalSolutions = 0;

	public static void main(String[] args) {

		// contains 'X' or 'O' or '\0'(empty)
		char[][] board = new char[3][3];

		// put the first 'X' on the board according to the first move
		board[STARTING_MOVE/3][STARTING_MOVE%3] = 'X';

		// now solve for all optimal solutions of the game given initial state
		System.out.println("Printing all optimal solutions:");
		minPlayer(new Move(STARTING_MOVE, null), board);

		System.out.println("Total # of optimal solutions = " + numOptimalSolutions);
		//printBoard(board);
	}
	/**
	 * Checks if the game is over a
	 * @param board
	 * @return
	 */
	private static String gameStatus(char[][] board){
		
		for (int i = 0; i < 3; i++){
			// check horizontal lines && vertical lines for player x
			if ((board[i][0] == 'X' && board[i][1] == 'X' && board[i][2] == 'X') || 
					(board[0][i] == 'X' && board[1][i] == 'X' && board[2][i] == 'X')){
				winner = 'X';
				return "true";
			}
		}
		//check diags for x
		if ((board[0][0] == 'X' && board[1][1] == 'X' && board[2][2] == 'X') || 
				(board[0][2] == 'X' && board[1][1] == 'X' && board[2][0] == 'X')){
			winner = 'X';
			return "true";
		}
		
		for (int i = 0; i < 3 ; i++) {
			// check horizontal and vertical lines for player o
			if ((board[i][0] == 'O' && board[i][1] == 'O' && board[i][2] == 'O') || 
					(board[0][i] == 'O' && board[1][i] == 'O' && board[2][i] == 'O')){
				winner = 'O';
				return "true";
			}
		}
		//check diags for o
		if ((board[0][0] == 'O' && board[1][1] == 'O' && board[2][2] == 'O') || 
				(board[0][2] == 'O' && board[1][1] == 'O' && board[2][0] == 'O')){
			winner = 'O';
			return "true";
		}

		// check for tie
		int emptyCells = 9;
		for (int i = 0; i < 3; i++){
			for (int j = 0; j < 3; j++){
				if (board[i][j]!='\0') emptyCells -= 1;		
			}
		}
		// if all cells are filled, game is over with a tie
		if (emptyCells == 0) return "tie";

		// otherwise game is not over and return false
		//printBoard(board);

		return "false";
	}
	/**
	 * 
	 * @param board
	 * Testing method to print board
	 */
	private static void printBoard(char[][] board){
		for (int i=0; i<3; i++) {
			System.out.println(Arrays.toString(board[i]));
		}
	}
	/**
	 * 
	 * @param board
	 * @return
	 * Finds all available moves
	 */
	private static HashSet<Integer> findPossibleMoves(char[][] board){

		HashSet<Integer> possibleMoves = new HashSet<Integer>();

		for (int i=0; i<3; i++){
			for (int j=0; j<3; j++){
				if (board[i][j] == '\0') possibleMoves.add(3*i+j);
			}
		}
		return possibleMoves;
	}
	/**
	 * 
	 * @param n
	 * @param board
	 * Finds the move for the min player
	 */
	private static void minPlayer(Move n, char[][] board){

		// if game is over, print out the optimal path
		if (gameStatus(board).equals("true") || gameStatus(board).equals("tie")){

			
			StringBuilder sb = new StringBuilder();
			sb.append(String.valueOf(n.getLocation()));
			numOptimalSolutions += 1;
			
			// get the entire path for the game
			while(n.getBacktracker() != null){
				n = n.getBacktracker();
				sb.append(n.getLocation());
			}
			
			// print out the optimal path to the console
			System.out.println(sb.reverse());
			return;
		}

		// else if game isn't over, need to find next optimal move
		HashMap<Integer, HashSet<Integer>> Beta = new HashMap<Integer, HashSet<Integer>>();
		HashSet<Integer> possibleMoves = findPossibleMoves(board);

		for (Integer move: possibleMoves){

			char[][] updatedBoard = new char[3][3];
			// modifiedBoard should be a copy of the board
			for (int row = 0; row < 3; row++){
				for (int col = 0; col < 3; col++){
					updatedBoard[row][col] = board[row][col];
				}
			}

			// try putting 'O' on the empty cells
			updatedBoard[move/3][move%3]='O';

			// check the new game set up and find out which moves are optimal
			int currentScore = getMaxScore(updatedBoard);
			
			HashSet<Integer> hashSet;

			if (!Beta.keySet().contains(currentScore)) hashSet = new HashSet<Integer>();
			else hashSet = Beta.get(currentScore);
			
			hashSet.add(move);
			Beta.put(currentScore, hashSet);
		}

		int min_val = 100;
		for (Integer key: Beta.keySet()){
			if (key < min_val) min_val = key;
			
		}

		// get the positions for all optimal moves
		HashSet<Integer> optimalActions = Beta.get(min_val);

		// try all optimal moves
		for (Integer move: optimalActions){

			char[][] updatedBoard = new char[3][3];
			for (int row = 0; row < 3; row++){
				for (int col = 0; col < 3; col++){
					updatedBoard[row][col] = board[row][col];
				}
			}
			// make the move
			updatedBoard[move/3][move%3] = 'O';
			// give a chance to the opponent to play
			maxPlayer(new Move(move,n), updatedBoard);
		}
	}
	/**
	 * 
	 * @param n
	 * @param board
	 * Similar to minPlayer method w/ minor changes for max player
	 */
	private static void maxPlayer(Move n, char[][] board){

		// if game is over, print out the optimal path
		if (gameStatus(board).equals("true") || gameStatus(board).equals("tie")){

			
			StringBuilder sb = new StringBuilder();
			sb.append(String.valueOf(n.getLocation()));
			numOptimalSolutions += 1;
			
			while(n.getBacktracker() != null){
				n = n.getBacktracker();
				sb.append(n.getLocation());
			}

			System.out.println(sb.reverse());
			return;
		}

		// else if game isn't over, need to find next optimal move
		HashMap<Integer, HashSet<Integer>> Alpha = new HashMap<Integer, HashSet<Integer>>();
		HashSet<Integer> possibleMoves = findPossibleMoves(board);

		for (Integer move: possibleMoves){

			char[][] modifiedBoard = new char[3][3];
			for (int row = 0; row < 3; row++){
				for (int col = 0; col < 3; col++){
					modifiedBoard[row][col] = board[row][col];
				}
			}

			// try putting 'X' on an empty cell
			modifiedBoard[move/3][move%3]='X';

			// check the new game set up and find all optimal moves
			int currentScore = getMinScore(modifiedBoard);
			HashSet<Integer> hashSet;

			if (!Alpha.keySet().contains(currentScore)) hashSet = new HashSet<Integer>();
			else hashSet = Alpha.get(currentScore);	
			
			hashSet.add(move);
			Alpha.put(currentScore, hashSet);
		}

		int max_val = -100;
		for (Integer key: Alpha.keySet()){
			if (key > max_val) max_val = key;
			
		}

		// get the positions for all optimal moves
		HashSet<Integer> optimalActions = Alpha.get(max_val);

		// try all optimal moves
		for (Integer move: optimalActions){
			char[][] updatedBoard = new char[3][3];
			for (int row = 0; row < 3; row++){
				for (int col = 0; col < 3; col++){
					updatedBoard[row][col] = board[row][col];
				}
			}
			// make the move
			updatedBoard[move/3][move%3] = 'X';

			// give the chance to opponent to make a move
			minPlayer(new Move(move,n), updatedBoard);
		}
	}    
	/**
	 * 
	 * @param board
	 * @return
	 * Best score possible for min player
	 * 
	 */
	private static int getMinScore(char[][] board){

		String result = gameStatus(board);

		if (result.equals("true") && winner == 'X') return 1; //if x won
		else if (result.equals("true") && winner == 'O') return -1;//if o won
		else if (result.equals("tie")) return 0; //if tie
		else { 

			int bestScore = 10;
			HashSet<Integer> possibleMoves = findPossibleMoves(board);

			for (Integer move: possibleMoves){

				char[][] modifiedBoard = new char[3][3];
				for (int row = 0; row < 3; row++){
					for (int col = 0; col < 3; col++){
						modifiedBoard[row][col] = board[row][col];
					}
				}

				modifiedBoard[move/3][move%3]='O';

				int currentScore = getMaxScore(modifiedBoard);

				if (currentScore < bestScore) bestScore = currentScore;
				
			}
			return bestScore;
		}
	}
	/**
	 * Gets best possible score for max player
	 * @param board
	 * @return
	 */
	private static int getMaxScore(char[][] board){

		String result = gameStatus(board);

		if (result.equals("true") && winner == 'X') return 1; //if x won
		else if (result.equals("true") && winner == 'O') return -1;//if O won
		else if (result.equals("tie")) return 0; //if tie
		else { 

			int bestScore = -10;
			HashSet<Integer> possibleMoves = findPossibleMoves(board);

			for (Integer move: possibleMoves){

				char[][] modifiedBoard = new char[3][3];

				for (int row = 0; row < 3; row++){
					for (int col = 0; col < 3; col++){
						modifiedBoard[row][col] = board[row][col];
					}
				}

				modifiedBoard[move/3][move%3]='X';

				int currentScore = getMinScore(modifiedBoard);

				if (currentScore > bestScore){
					bestScore = currentScore;
				}

			}
			return bestScore;
		}
	}
}
