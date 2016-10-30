
public class Heuristic {

	public static int colorDiff(int[][] state, int team) {
	
		int enemy = (team % 2) + 1;
		int diff = 0;
		for (int n = 0; n < 8; n++) {
			for (int m = 0; m < 8; m++) {
			     if (state[n][m] == team)
			    	 diff++;
			     if (state[n][m] == enemy)
			    	 diff--;
			}
		}
		
		return diff;
	}
	
	static final int EDGE_BENEFIT = 2;
	static final int CORNER_BENEFIT = 1000;
	public static int colorDiffWithCorners(int[][] state, int team) {
		
		int enemy = (team % 2) + 1;
		int diff = 0;
		for (int n = 0; n < 8; n++) {
			for (int m = 0; m < 8; m++) {
			     if (state[n][m] == team)
			    	 diff++;
			     else if (state[n][m] == enemy)
			    	 diff--;
			     
			     if (n == 0 || n == 7 || m == 0 || m == 7) {
			    	 if (state[n][m] == team)
			    		 diff += EDGE_BENEFIT;
			    	 else if (state[n][m] == enemy)
			    		 diff -= EDGE_BENEFIT;
			    	 
			    	 if ( (n == m) || (n == 0 && m == 7) || (n == 7 && m == 0)) 
			    	 {
			    		 if (state[n][m] == team)
				    		 diff += CORNER_BENEFIT;
				    	 else if (state[n][m] == enemy)
				    		 diff -= 0; //TODO
			    	 }
			     }
			}
		}
		return diff;
		
	}
}
