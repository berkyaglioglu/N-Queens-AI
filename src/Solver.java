
public class Solver {

	public static int[] getRandomState(int n) { // gives random state
		int[] board = new int[n];
		
		for(int i=0; i < n; i++) {
			board[i] = (int)(Math.random()*n); // set board's indexes with random values
		}
		
		return board;
	}
	
	public static int heuristicCost(int[] board) { // gives heuristic cost
		int cost = 0;
		int offset;
		
		for(int i=0; i < board.length; i++) {
			for(int j=i+1; j < board.length; j++) {
				if(board[i] == board[j]) { // if queens are able to attack each other in the same row
					cost++;
				}
				offset = j - i;
				if(board[i] == board[j] + offset || board[i] == board[j] - offset) { // if queens are able to attack each other in the same dioganal
					cost++;
				}
			}
		}
		
		return cost;
	}
}
