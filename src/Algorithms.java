import java.util.Arrays;
import java.util.Comparator;

public class Algorithms {

	public static int[] hillClimbing(int n) {
		int[] tempState;
		int[] newState;
		int[] board = Solver.getRandomState(n); // get random state as initial state
		int tempCost=0;
		int currentCost=0;
		int cost = Solver.heuristicCost(board); // cost of initial state
		
		for(int time=0; cost != 0 && time < 5000; time++) { // continue until cost becomes zero or threshold is exceeded
			newState = null;
			
			for(int i=0; i < n; i++) {
				for(int j=0; j < n; j++) {
					if(board[i] != j) {
						if(newState == null) { // set new state with a possible state for the first time
							newState = Arrays.copyOf(board, n);
							newState[i] = j;
							currentCost = Solver.heuristicCost(newState);
						}
						else { // after new state was set once with some possible state
							tempState = Arrays.copyOf(board, n);;
							tempState[i] = j;
							tempCost = Solver.heuristicCost(tempState);
							if(tempCost < currentCost) { // check whether the newest state is closer to solution
								newState = Arrays.copyOf(tempState, n); // set new state with the newest state
								currentCost = tempCost;
							}
						}
					}
				}
			}
			
			if(currentCost >= cost) { // if the algorithm got stuck in the local maxima 
				board = Solver.getRandomState(n);
			}
			else {
				board = newState;
				cost = currentCost;
			}
		}
		
		if(Solver.heuristicCost(board) == 0) { // if solution is found
			return board;
		}
		else {
			return null;
		}
	}
	
	public static int[] simulatedAnnealing(int n, double temp, double cooling) {
		int[] board = Solver.getRandomState(n); // get random state as an initial state
		int cost = Solver.heuristicCost(board);
		int[] randState;
		int row, col, currentCost, deltaCost;
		double probability;
		
		while(cost != 0 && temp > 0.00001) { // continue until solution is found or temperature becomes very low
			col = (int)(Math.random()*n);
			do {
				row = (int)(Math.random()*n);
			} while(board[col] == row);
			
			randState = Arrays.copyOf(board, n);
			randState[col] = row; // set randState with a new possible state from the current board by changing its one index
			
			currentCost = Solver.heuristicCost(randState);
			deltaCost = currentCost - cost; // calculate difference of the new state and initial board state
			
			if(deltaCost < 0) { // if better solution is found, set board with better solution
				board = Arrays.copyOf(randState, n);
				cost = currentCost;
			}
			else { // if no better solution
				probability = Math.exp((-1)*(deltaCost/temp));
				if(Math.random() <= probability) { // set board with worse solution with probability
					board = Arrays.copyOf(randState, n);
					cost = currentCost;
				}
			}
			
			temp = temp * cooling; // regulate temperature
		}
		
		if(cost == 0) // if solution is found
			return board;
		else
			return null; // if no solution
	}
	
	public static int[] localBeamSearch(int n, int stateNum) {
		int[][] currentStates = new int[stateNum][]; // there will be as many current states as state number
		int[][] successorStates;
		int successorNum = (n*n-n)*stateNum; // all states in total will have possible move from the current position as successor number
		
		for(int i=0; i < stateNum; i++) {
			currentStates[i] = Solver.getRandomState(n); // initial random states
		}
		successorStates = Arrays.copyOf(currentStates, stateNum); // initial successor states are initial current states
		
		for(int time=0; time < 5000; time++) {
			
			Arrays.sort(successorStates, new Comparator<int[]>(){ // sort successors according to heuristic numbers
				public int compare(int[] o1, int[] o2) {
					// TODO Auto-generated method stub
					Integer i1 = Solver.heuristicCost(o1);
					Integer i2 = Solver.heuristicCost(o2);
					return i1.compareTo(i2);
				}
			});
			
			if(Solver.heuristicCost(successorStates[0]) == 0) { //if goal position is found
				return successorStates[0];
			}
			
			currentStates = Arrays.copyOfRange(successorStates, 0, stateNum); // current states becomes best stateNum solutions in successors
			successorStates = new int[successorNum][];
			
			int k = 0;
				
			for(int currNum=0; currNum < stateNum; currNum++) {
				for(int i=0; i < n; i++) {
					for(int j=0; j < n; j++) {
						if(currentStates[currNum][i] != j) { // set successor states with possible moves
							successorStates[k] = Arrays.copyOf(currentStates[currNum], n);
							successorStates[k][i] = j;
							k++;
						}
					}
				}
			}
			
		}
		
		return null; // if no solution is found
		
	}
	
	public static int[] geneticAlgorithm(int n, int geneSize, double elitism, double crossover, double mutation, int geneNum) {
		geneSize = geneSize - (geneSize % 2);
		int[][] chromosomes = new int[geneSize][];
        for (int i = 0; i < geneSize; i++) {
        	chromosomes[i] = Solver.getRandomState(n); // create random states called chromosomes as many as generation size
        }
        
        int elitismNum = (int) (geneSize*elitism); // calculate elitism number with respect to generation size
        
        for(int time=0; time < geneNum; time++) { // try to find solution as many time as generation number
			Arrays.sort(chromosomes, new Comparator<int[]>(){ // sort chromosomes with respect to heuristics
				public int compare(int[] o1, int[] o2) {
					// TODO Auto-generated method stub
					Integer i1 = Solver.heuristicCost(o1);
					Integer i2 = Solver.heuristicCost(o2);
					return i1.compareTo(i2);
				}
			});        
			
			if(Solver.heuristicCost(chromosomes[0]) == 0) { // if any solution is found
				return chromosomes[0];
			}
			
			for(int i = 0; i < chromosomes.length; i += 2) {
				if(crossover >= Math.random()) { // if crossover probability satisfies
					int crossoverPosition = (int)(Math.random()*n); //get random position in chromosome
					 
	                for (int j = 0; j < crossoverPosition; j++) { // do crossover of indexes till crossover position in 2 consecutive chromosomes 
	                    int temp = chromosomes[i][j];            
	                    chromosomes[i][j] = chromosomes[i+1][j]; 
	                    chromosomes[i+1][j] = temp;
	                }
	                
	                if(Solver.heuristicCost(chromosomes[i]) == 0) //if some solutions are found
	                	return chromosomes[i];
	                else if(Solver.heuristicCost(chromosomes[i+1]) == 0)
	                	return chromosomes[i+1];
				}
			}
			
			for(int i = chromosomes.length-1; i >= elitismNum; i--) { // keep the best elitismNum chromosomes out of mutation
				if(mutation >= Math.random()) {
					chromosomes[i][(int)(Math.random()*n)] = (int)(Math.random()*n); // change one of the index of rest of the chromosomes
					if(Solver.heuristicCost(chromosomes[i]) == 0)
	                	return chromosomes[i];
				}
			}
        }
        
		return null; // if no solution is found
	}
}































