import java.util.*;
import java.lang.*;
import java.io.*;
import java.net.*;

class RandomGuy {

	public Socket s;
	public BufferedReader sin;
	public PrintWriter sout;
	Random generator = new Random();

	double t1, t2;
	int me;
	int boardState;
	int state[][] = new int[8][8]; // state[0][0] is the bottom left corner of
									// the board (on the GUI)
	int turn = -1;
	int round;

	int validMoves[] = new int[64];
	int numValidMoves;

	// main function that (1) establishes a connection with the server, and then
	// plays whenever it is this player's turn
	public RandomGuy(int _me, String host) {
		me = _me;
		initClient(host);

		int myMove;

		while (true) {
			System.out.println("Read");
			readMessage();

			if (turn == me) {
				System.out.println("Move");
				getValidMoves(round, state);

				myMove = move();
				// myMove = generator.nextInt(numValidMoves); // select a move
				// randomly

				String sel = validMoves[myMove] / 8 + "\n" + validMoves[myMove] % 8;

				System.out.println("Selection: " + validMoves[myMove] / 8 + ", " + validMoves[myMove] % 8);

				sout.println(sel);
			}
		}
		// while (turn == me) {
		// System.out.println("My turn");

		// readMessage();
		// }
	}

	// You should modify this function
	// validMoves is a list of valid locations that you could place your "stone"
	// on this turn
	// Note that "state" is a global variable 2D list that shows the state of
	// the game
	private int move() {
		if (round < 4) {
			return getInitialRoundsMove();
		}

		if (numValidMoves > 1) {
			List<Integer> bestOptions = Corners();
			int myMove = 0;
			if(bestOptions.size() > 0){
				validMoves[0] = Algorithm.getBestMove(state, bestOptions, me, round);
				return 0;
			}

			//If we're in the earlier rounds, middle pieces are more important than edges
			/*if(round <= 25){
				bestOptions = SweetSixteen();
				if(bestOptions.size() > 0){
					validMoves[0] = Algorithm.getBestMove(state, bestOptions, me, round);
					return 0;
				}

				bestOptions = groundedEdges();
				if(bestOptions.size() > 0){
					validMoves[0] = Algorithm.getBestMove(state, bestOptions, me, round);
					return 0;
				}
			} else {*/
			bestOptions = groundedEdges();
			if(bestOptions.size() > 0){
				validMoves[0] = Algorithm.getBestMove(state, bestOptions, me, round);
				if(validMoves[0] >= 0) {
					return 0;
				}
			}

			bestOptions.addAll(goodEdges());
			/*if(bestOptions.size() > 0){
				validMoves[0] = Algorithm.getBestMove(state, bestOptions, me, round);
				return 0;
			}*/

			bestOptions.addAll(SweetSixteen());
			/*if(bestOptions.size() > 0){
				validMoves[0] = Algorithm.getBestMove(state, bestOptions, me, round);
				return 0;
			}*/
			//}

			if(round < 30 && bestOptions.size() > 0){
				System.out.println(bestOptions.toString());
				validMoves[0] = Algorithm.getBestMove(state, bestOptions, me, round);
				if(validMoves[0] >= 0) {
					return 0;
				}
			}

			bestOptions.addAll(acceptableMoves());
			if(bestOptions.size() > 0){
				validMoves[0] = Algorithm.getBestMove(state, bestOptions, me, round);
				if(validMoves[0] >=0) {
					return 0;
				}
			}

			for(int k = 0; k<numValidMoves; k++){
				bestOptions.add(validMoves[k]);
			}
			validMoves[0] = Algorithm.AlphaBeta(state, round, me, bestOptions);
			return 0;
		}

		// just move randomly for now
		//int myMove = generator.nextInt(numValidMoves);

		//int maximinval = Algorithm.AlphaBeta(state, round, me);
		//validMoves[0] = maximinval;
		return 0;
		
		//return myMove;
	}

		
	public int getInitialRoundsMove() {
		 if (state[3][3] == 0) {
         	validMoves[0] = 3*8 + 3;
         }
         if (state[3][4] == 0) {
        	 validMoves[0] = 3*8 + 4;
         }
         if (state[4][3] == 0) {
        	 validMoves[0] = 4*8 + 3;
         }
         if (state[4][4] == 0) {
        	 validMoves[0] = 4*8 + 4;
         }
      	return 0;
	}
		
	private List<Integer> Corners() {
		List<Integer> corners = new ArrayList<Integer>();
		for (int i = 0; i < numValidMoves; i++) {
			if (validMoves[i] == 0) {
				corners.add(0);
			} else if (validMoves[i] == 7) {
				corners.add(7);
			} else if (validMoves[i] == 56) {
				corners.add(56);
			} else if (validMoves[i] == 63) {
				corners.add(63);
			}
		}
		return corners;
	}

	private List<Integer> SweetSixteen() {
		List<Integer> middle = new ArrayList<>();
		for (int i = 0; i < numValidMoves; i++) {
			if(validMoves[i] / 8 > 1 && validMoves[i] /8 < 6 && validMoves[i] % 8 > 1 && validMoves[i] % 8 < 6){
				middle.add(validMoves[i]);
			}
		}
		return middle;
	}

	private List<Integer> goodEdges() {
		/*List<Integer> justedge = new ArrayList<Integer>();
		for (int i = 0; i< numValidMoves; i++){
			int row = validMoves[i] / 8;
			int col = validMoves[i] % 8;
			if (row == 0 || row == 7 || col == 0 || col == 7) {
				justedge.add(validMoves[i]);
			}
		}
		if(justedge.size() > 0) {
			return justedge;
		}*/
		List<Integer> group = new ArrayList<>();
		List<Integer> alone = new ArrayList<>();
		int enemy = me == 1 ? 2 : 1;
		for (int i = 0; i < numValidMoves; i++) {
			int row = validMoves[i] / 8;
			int col = validMoves[i] % 8;
			if (row == 0 || row == 7) {
				boolean goodLeft = true;
				boolean goodRight = true;
				boolean bestLeft = false;
				boolean bestRight = false;
				if(col > 0) {

					if(state[row][col -1] == enemy){
						goodLeft = false;
						for(int c = col; c>0; c--){
							if(state[row][c] == me){
								goodLeft = true;
								bestLeft = true;
							}
						}
					}
				} else { goodLeft = false;}

				if(col < 7) {
					if(state[row][col + 1] == enemy){
						goodRight = false;
						for(int c = col; c < 7; c++){
							if(state[row][c] == me){
								goodRight = true;
								bestRight = true;
							}
						}
					}
				} else { goodRight = false; }

				if((bestRight && goodLeft) || (goodRight && bestLeft)) {
					group.add(validMoves[i]);
				}
				else if(goodRight && goodLeft){
					alone.add(validMoves[i]);
				}
			}

			if ( col == 0 || col == 7){
				boolean goodUp = true;
				boolean goodDown = true;
				boolean bestUp = false;
				boolean bestDown = false;
				if (row > 0) {

					if(state[row - 1][col] == enemy){
						goodDown = false;
						for(int r = row; r>0; r--){
							if(state[r][col] == me){
								goodDown = true;
								bestDown = true;
							}
						}
					}
				} else {goodDown = false;}

				if( row < 7) {
					if(state[row + 1][col] == enemy){
						goodUp = false;
						for(int r = row; r < 7; r++){
							if(state[r][col] == me){
								goodUp = true;
								bestUp = true;
							}
						}
					}
				} else { goodUp = false;}

				if((bestDown && goodUp) || (bestUp && goodDown)){
					group.add(validMoves[i]);
				}
				else if(goodUp && goodDown){
					alone.add(validMoves[i]);
				}
			}
		}
		return group.size() > 0 ? group : alone;
	}

	// Still working on this guy
	// don't go to 1, 6, 8, 15, 48, 55, 57 or 62 unless it's grounded
	private List<Integer> groundedEdges() {
		List<Integer> options = new ArrayList<Integer>();
		for (int i = 0; i < numValidMoves; i++) {
			int row = validMoves[i] / 8;
			int col = validMoves[i] % 8;
			if (row == 0 || row == 7) {
				boolean groundedLeft = true;
				boolean groundedRight = true;
				for(int c = col; c < 8; c ++){
					if(state[row][c] != me){
						groundedRight = false;
					}
				}
				for(int c = col; c >= 0; c--){
					if(state[row][c] != me){
						groundedLeft = false;
					}
				}

				if(groundedLeft || groundedRight) {
					options.add(validMoves[i]);
				}
			}
			if (col == 0 || col == 7){
				boolean groundedDown = true;
				boolean groundedUp = true;
				for(int r = row; r < 8; r++){
					if(state[r][col] != me){
						groundedUp = false;
					}
				}

				for(int r = row; r >= 0; r--){
					if(state[r][col] != me){
						groundedDown = false;
					}
				}

				if(groundedUp || groundedDown){
					options.add(validMoves[i]);
				}
			}
		}
		return options;
	}

	private List<Integer> acceptableMoves(){
		List<Integer> acceptable = new ArrayList<>();
		for(int i = 0; i < numValidMoves; i++){
			int row = validMoves[i] / 8;
			int col = validMoves[i] % 8;
			if(row != 0 && row != 7 && col != 0 && col != 7){
				if(validMoves[i] != 9 && validMoves[i] != 14 &&
						validMoves[i] != 49 && validMoves[i] != 54){
					acceptable.add(validMoves[i]);
				}
			}
		}
		return acceptable;
	}

	/*
	 * TODO: Eric, this function returns a projected version of the state if you
	 * were to play the anticipated move Probably the most useful function I
	 * wrote...
	 */
	public int[][] getProjectedState(int row, int col, int turn, int[][] project) {
		int incx, incy;

		for (incx = -1; incx < 2; incx++) {
			for (incy = -1; incy < 2; incy++) {
				if ((incx == 0) && (incy == 0))
					continue;

				project = buildProjected(project, row, col, incx, incy, turn);
			}
		}
		return project;
	}

	private int[][] buildProjected(int[][] project, int row, int col, int incx, int incy, int turn) {
		int sequence[] = new int[7];
		int seqLen;
		int i, r, c;

		seqLen = 0;
		for (i = 1; i < 8; i++) {
			r = row + incy * i;
			c = col + incx * i;

			if ((r < 0) || (r > 7) || (c < 0) || (c > 7))
				break;

			sequence[seqLen] = project[r][c];
			seqLen++;
		}

		int count = 0;
		for (i = 0; i < seqLen; i++) {
			if (me == 0) {
				if (sequence[i] == 2)
					count++;
				else {
					if ((sequence[i] == 1) && (count > 0))
						count = 20;
					break;
				}
			} else {
				if (sequence[i] == 1)
					count++;
				else {
					if ((sequence[i] == 2) && (count > 0))
						count = 20;
					break;
				}
			}
		}

		if (count > 10) {
			if (me == 0) {
				i = 1;
				r = row + incy * i;
				c = col + incx * i;
				while (project[r][c] == 2) {
					project[r][c] = 1;
					i++;
					r = row + incy * i;
					c = col + incx * i;
				}
			} else {
				i = 1;
				r = row + incy * i;
				c = col + incx * i;
				while (project[r][c] == 1) {
					project[r][c] = 2;
					i++;
					r = row + incy * i;
					c = col + incx * i;
				}
			}
		}
		return project;
	}

	/*
	 * TODO: Eric, this is the function which will determine the best choice out
	 * of the given options The possible options are numbered starting at 0 in
	 * the lower left corner and increasing by 1 in the rows.
	 */
	private int findBest(List<Integer> options) {
		for (Integer option : options) {
			int col = option % 8;
			int row = option / 8;
			int project[][] = getProjectedState(row, col, me, state);
			// TODO: Use projected image to find best option
		}
		// Temporary return
		if (options.size() > 0) {
			return options.get(0);
		}
		return -1;
	}

	// generates the set of valid moves for the player; returns a list of valid
	// moves (validMoves)
	private void getValidMoves(int round, int state[][]) {
		int i, j;
		numValidMoves = 0;
		if (round < 4) {
			if (state[3][3] == 0) {
				validMoves[numValidMoves] = 3 * 8 + 3;
				numValidMoves++;
			}
			if (state[3][4] == 0) {
				validMoves[numValidMoves] = 3 * 8 + 4;
				numValidMoves++;
			}
			if (state[4][3] == 0) {
				validMoves[numValidMoves] = 4 * 8 + 3;
				numValidMoves++;
			}
			if (state[4][4] == 0) {
				validMoves[numValidMoves] = 4 * 8 + 4;
				numValidMoves++;
			}
			System.out.println("Valid Moves:");
			for (i = 0; i < numValidMoves; i++) {
				//System.out.println(validMoves[i] / 8 + ", " + validMoves[i] % 8);
			}
		} else {
			System.out.println("Valid Moves:");
			for (i = 0; i < 8; i++) {
				for (j = 0; j < 8; j++) {
					if (state[i][j] == 0) {
						if (couldBe(state, i, j)) {
							validMoves[numValidMoves] = i * 8 + j;
							numValidMoves++;
							//System.out.println(i + ", " + j);
						}
					}
				}
			}
		}

		// if (round > 3) {
		// System.out.println("checking out");
		// System.exit(1);
		// }
	}

	private boolean checkDirection(int row, int col, int incx, int incy) {
		int sequence[] = new int[7];
		int seqLen;
		int i, r, c;

		seqLen = 0;
		for (i = 1; i < 8; i++) {
			r = row + incy * i;
			c = col + incx * i;

			if ((r < 0) || (r > 7) || (c < 0) || (c > 7))
				break;

			sequence[seqLen] = state[r][c];
			seqLen++;
		}

		int count = 0;
		for (i = 0; i < seqLen; i++) {
			if (me == 1) {
				if (sequence[i] == 2)
					count++;
				else {
					if ((sequence[i] == 1) && (count > 0))
						return true;
					break;
				}
			} else {
				if (sequence[i] == 1)
					count++;
				else {
					if ((sequence[i] == 2) && (count > 0))
						return true;
					break;
				}
			}
		}

		return false;
	}

	private boolean couldBe(int state[][], int row, int col) {
		int incx, incy;

		for (incx = -1; incx < 2; incx++) {
			for (incy = -1; incy < 2; incy++) {
				if ((incx == 0) && (incy == 0))
					continue;

				if (checkDirection(row, col, incx, incy))
					return true;
			}
		}

		return false;
	}

	public void readMessage() {
		int i, j;
		String status;
		try {
			// System.out.println("Ready to read again");
			turn = Integer.parseInt(sin.readLine());

			if (turn == -999) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					System.out.println(e);
				}

				System.exit(1);
			}

			// System.out.println("Turn: " + turn);
			round = Integer.parseInt(sin.readLine());
			t1 = Double.parseDouble(sin.readLine());
			System.out.println(t1);
			t2 = Double.parseDouble(sin.readLine());
			System.out.println(t2);
			for (i = 0; i < 8; i++) {
				for (j = 0; j < 8; j++) {
					state[i][j] = Integer.parseInt(sin.readLine());
				}
			}
			sin.readLine();
		} catch (IOException e) {
			System.err.println("Caught IOException: " + e.getMessage());
		}

		System.out.println("Turn: " + turn);
		System.out.println("Round: " + round);
		for (i = 7; i >= 0; i--) {
			for (j = 0; j < 8; j++) {
				System.out.print(state[i][j]);
			}
			System.out.println();
		}
		System.out.println();
	}

	public void initClient(String host) {
		int portNumber = 3333 + me;

		try {
			s = new Socket(host, portNumber);
			sout = new PrintWriter(s.getOutputStream(), true);
			sin = new BufferedReader(new InputStreamReader(s.getInputStream()));

			String info = sin.readLine();
			System.out.println(info);
		} catch (IOException e) {
			System.err.println("Caught IOException: " + e.getMessage());
		}
	}

	// compile on your machine: javac *.java
	// call: java RandomGuy [ipaddress] [player_number]
	// ipaddress is the ipaddress on the computer the server was launched on.
	// Enter "localhost" if it is on the same computer
	// player_number is 1 (for the black player) and 2 (for the white player)
	public static void main(String args[]) {
		new RandomGuy(Integer.parseInt(args[1]), args[0]);
		//new RandomGuy(1, "localhost");
	}

}
