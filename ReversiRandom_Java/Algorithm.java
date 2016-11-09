import java.util.ArrayList;
import java.util.List;

public class Algorithm {

	public static int MAX_DEPTH = 6;
	static int lastOptimalChoice;
	public static int AlphaBeta(int[][] state, int turn, int team)
	{
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		int v =  MaxV(state, alpha, beta, team, 0, turn); //TODO: Check if this "team" is right
		System.out.println("AlphaBeta max value: " + v);
		return lastOptimalChoice;
	}
	public static int MaxV(int[][] state, int alpha, int beta, int team, int depth, int turn)
	{
		//If hit max depth,
		  //return utility
		if (depth == MAX_DEPTH)
			return Heuristic.colorDiffWithCorners(state, 1, turn);
		
		int v = Integer.MIN_VALUE;
		List<Integer> valids = getValidMoves(state, team);
		valids = getBestValids(valids, turn);
		turn++;
		for (int n = 0; n < valids.size(); n++) {
			int nextV = MinV(getProjectedState(state, valids.get(n) / 8, valids.get(n) % 8, team), alpha, beta, team == 2 ? 1 : 2, depth + 1, turn);
			
			//Remember the best option. 
			//Only necessary for MaxV
			//because we only care about the immediate option for the AI
			if (nextV >= v)
			{
				if (depth == 0)
					lastOptimalChoice = valids.get(n);
				v = nextV;
			}
			
			if (v >= beta)
				return v;
			
			alpha = Math.max(alpha, v);
		}
		return v;
		
	}

	public static List<Integer> getBestValids(List<Integer> current, int round) {
		List<Integer> best = new ArrayList<Integer>();
		for(int i=0; i<current.size(); i++){
			int spot = current.get(i);
			//Corners And Edges
			if(spot <= 7 || spot >= 56 || spot % 8 == 0 || spot % 8 == 1){
				best.add(spot);
			}
			//Sweet Sixteen
			int rem = spot % 8;
			if(rem > 1 && rem <6 && spot > 17 && spot < 46) {
				best.add(spot);
			}
			if(round >= 20) {
				if(spot != 9 && spot != 14 && spot != 49 && spot !=54) {
					best.add(spot);

				}
			}
		}
		if(best.size() == 0 && round < 20) {
			for(int i=0; i<current.size(); i++){
				int spot = current.get(i);
				if(spot != 9 && spot != 14 && spot != 49 && spot !=54) {
					best.add(spot);
				}
			}
		}
		if(best.size() == 0){
			return current;
		}
		return best;
	}

	public static int MinV(int[][] state, int alpha, int beta, int team,  int depth, int turn)
	{
		//If hit max depth,
		  //return utility
		if (depth == MAX_DEPTH)
			return Heuristic.colorDiffWithCorners(state, 1, turn);
		
		int v = Integer.MAX_VALUE;
		List<Integer> valids = getValidMoves(state, team);
		turn++;
		for (int n = 0; n < valids.size(); n++) {
			v = Math.min(v, MaxV(getProjectedState(state, valids.get(n) / 8, valids.get(n) % 8, team), alpha, beta, team == 2 ? 1 : 2, depth + 1, turn));
			if (v <= alpha)
				return v;
			beta = Math.min(beta, v);
		}
		return v;
	}
	private static List<Integer> getValidMoves(int state[][], int team) {
		
		List<Integer> valids = new ArrayList<Integer>();
        int i, j;
        //System.out.println("check");
        
        
        //The first 4 rounds are handled in RandomGuy
        
           // System.out.println("Valid Moves:");
            for (i = 0; i < 8; i++) {
                for (j = 0; j < 8; j++) {
                    if (state[i][j] == 0) {
                        if (couldBe(state, i, j, team)) {
                            valids.add(i*8 + j);
                          //  System.out.println(i + ", " + j);
                         //   System.out.println("valid move value: " + valids.get(valids.size()-1));
                        }
                    }
                }
            }
       
        
        return valids;
    }
	
	
	/* All of this was just copied from RandomGuy.java (with only minor modifications) */
	
	private static boolean checkDirection(int state[][], int row, int col, int incx, int incy, int team) {
	        int sequence[] = new int[7];
	        int seqLen;
	        int i, r, c;
	        
	        seqLen = 0;
	        for (i = 1; i < 8; i++) {
	            r = row+incy*i;
	            c = col+incx*i;
	        
	            if ((r < 0) || (r > 7) || (c < 0) || (c > 7))
	                break;
	        
	            sequence[seqLen] = state[r][c];
	            seqLen++;
	        }
	        
	        int count = 0;
	        for (i = 0; i < seqLen; i++) {
	            if (team == 1) {
	                if (sequence[i] == 2)
	                    count ++;
	                else {
	                    if ((sequence[i] == 1) && (count > 0))
	                        return true;
	                    break;
	                }
	            }
	            else {
	                if (sequence[i] == 1)
	                    count ++;
	                else {
	                    if ((sequence[i] == 2) && (count > 0))
	                        return true;
	                    break;
	                }
	            }
	        }
	        
	        return false;
	    }
	    
	private static boolean couldBe(int state[][], int row, int col, int team) {
	        int incx, incy;
	        
	        for (incx = -1; incx < 2; incx++) {
	            for (incy = -1; incy < 2; incy++) {
	                if ((incx == 0) && (incy == 0))
	                    continue;
	            
	                if (checkDirection(state, row, col, incx, incy, team))
	                    return true;
	            }
	        }
	        
	        return false;
	    }
	
	public static int[][] getProjectedState(int[][] state, int row, int col, int turn) {
	        int incx, incy;

	        int[][] projectedState = new int[8][8];
	        for (int a = 0; a < 8; a++)
	        	for (int b = 0; b < 8; b++)
	        		projectedState[a][b] = state[a][b];
	        
	        
	        for (incx = -1; incx < 2; incx++) {
	            for (incy = -1; incy < 2; incy++) {
	                if ((incx == 0) && (incy == 0))
	                    continue;

	                 buildProjected2(projectedState, row, col, incx, incy, turn);
	            }
	        }
	        projectedState[col][row] = turn;
	        return projectedState;
	    }

	static int[] changeSequence = new int[7];
	private static void buildProjected2(int[][] project, int row, int col, int incx, int incy, int team) {
		int r,c,i;
	
		int enemy = team == 1 ? 2 : 1;
		boolean bounded = false;
		for (i = 1; i < 8; i++) {
			r = row + incy * i;
			c = col + incx * i;
			if (r < 0 || r > 7 || c < 0 || c > 7)
				break;
			
			if (project[c][r] != enemy) {
				if (i > 1 || project[c][r]  == team)
					bounded = true;
				
				break;
			}
			
		}
		if (!bounded)
			return;
		
		for (i = 1; i < 8; i++) {
			r = row + incy * i;
			c = col + incx * i;
			if (r < 0 || r > 7 || c < 0 || c > 7)
				break;
			if (project[c][r] == team)
				break;
			
			project[c][r] = team;
			
		}
		
	}
	
	
	//OBSOLETE
	/*
	private static int[][] buildProjected(int[][] project, int row, int col, int incx, int incy, int team) {
	        int sequence[] = new int[7];
	        int seqLen;
	        int i, r, c;

	        seqLen = 0;
	        for (i = 1; i < 8; i++) {
	            r = row+incy*i;
	            c = col+incx*i;

	            if ((r < 0) || (r > 7) || (c < 0) || (c > 7))
	                break;

	            sequence[seqLen] = project[r][c];
	            seqLen++;
	        }

	        int count = 0;
	        for (i = 0; i < seqLen; i++) {
	            if (team == 1) {
	                if (sequence[i] == 2)
	                    count ++;
	                else {
	                    if ((sequence[i] == 1) && (count > 0))
	                        count = 20;
	                    break;
	                }
	            }
	            else {
	                if (sequence[i] == 1)
	                    count ++;
	                else {
	                    if ((sequence[i] == 2) && (count > 0))
	                        count = 20;
	                    break;
	                }
	            }
	        }

	       // if (count > 10) {
	            if (team == 1) {
	                i = 1;
	                r = row+incy*i;
	                c = col+incx*i;
	                while (project[r][c] == 2) {
	                    project[r][c] = 1;
	                    i++;
	                    r = row+incy*i;
	                    c = col+incx*i;
	                }
	            }
	            else {
	                i = 1;
	                r = row+incy*i;
	                c = col+incx*i;
	                while (project[r][c] == 1) {
	                    project[r][c] = 2;
	                    i++;
	                    r = row+incy*i;
	                    c = col+incx*i;
	                }
	            }
	      //  }
	        return project;
	    }
	    */
}
