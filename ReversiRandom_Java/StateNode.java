import java.util.ArrayList;
import java.util.List;


/* Plan to use this to "cache" explored states
 * after the opponent takes their turn and it's your turn again.
 * Might help, might not.
 */
public class StateNode {

	public StateNode[] children;
	public int state[][] = new int[8][8];
	public StateNode() {
		
	
	}
	public StateNode(StateNode copy) {
		for (int a = 0; a < 8; a++)
			for (int b = 0; b < 8; b++)
				state[a][b] = copy.state[a][b];
	}
}
