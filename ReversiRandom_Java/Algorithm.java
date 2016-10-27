import java.util.ArrayList;
import java.util.List;

public class Algorithm {

	
	public static int AlphaBeta(int[][] state)
	{
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		return MaxV(state, alpha, beta, 1); //TODO: Check if this "team" is right
		
	}
	public static int MaxV(int[][] state, int alpha, int beta, int team)
	{
		//If is depth,
		  //return utility
		
		int v = Integer.MIN_VALUE;
		List<Integer> valids = getValidMoves(state, team);
		for (int n = 0; n < valids.size(); n++) {
			v = Math.max(v, MinV(applyMove(state, valids.get(n), team == 1 ? 2 : 1), alpha, beta, team));
			if (v >= beta)
				return v;
			alpha = Math.max(alpha, v);
		}
		return v;
		
	}
	public static int MinV(int[][] state, int alpha, int beta, int team)
	{
		//If is depth,
		  //return utility
		
		int v = Integer.MAX_VALUE;
		List<Integer> valids = getValidMoves(state, team);
		for (int n = 0; n < valids.size(); n++) {
			v = Math.max(v, MinV(applyMove(state, valids.get(n), team == 1 ? 2 : 1), alpha, beta, team));
			if (v <= alpha)
				return v;
			beta = Math.min(beta, v);
		}
		return v;
	}
	private static List<Integer> getValidMoves(int state[][], int team) {
		
		List<Integer> valids = new ArrayList<Integer>();
        int i, j;
        System.out.println("check");
        
        //TODO: I took out round because I think
        //we could handle the first 4 rounds somewhere else
        //to save passing the "round" variable around
        
        
        /*
        if (round < 4) {
            if (state[3][3] == 0) {
            	valids.add(3*8 + 3);
            }
            if (state[3][4] == 0) {
                valids.add(3*8 + 4);
            }
            if (state[4][3] == 0) {
                valids.add(4*8 + 3);
            }
            if (state[4][4] == 0) {
                valids.add(4*8 + 4);
            }
            System.out.println("Valid Moves:");
            for (i = 0; i < valids.size(); i++) {
                System.out.println(valids.get(i) / 8 + ", " + valids.get(i) % 8);
            }
        }
        else {
        */
            System.out.println("Valid Moves:");
            for (i = 0; i < 8; i++) {
                for (j = 0; j < 8; j++) {
                    if (state[i][j] == 0) {
                        if (couldBe(state, i, j, team)) {
                            valids.add(i*8 + j);
                            System.out.println(i + ", " + j);
                            System.out.println("valid move value: " + valids.get(valids.size()-1));
                        }
                    }
                }
            }
       // }
        
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
	
	/* TODO: Corbin, this might be a dupe of your function. */
	/* 10/27 2:47   nevermind, yours is better */
	private static int[][] applyMove(int[][] state, Integer move, int team) {
		int[][] newState = new int[8][8];
		for (int a = 0; a < 8; a++)
			for (int b = 0; b < 8; b++)
				newState[a][b] = state[a][b];
		
		newState[move / 8][move % 8] = team;
		return newState;
	}
}
