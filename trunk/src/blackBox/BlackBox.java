package blackBox;

import java.util.HashMap;
import java.util.List;

/**
 * 
 * @author Richard Jenkin
 * @version 0.5
 *
 */
public interface BlackBox {

	public void iterate(int iterations);
	
	public int[][] getMatrixArray();

	public HashMap<String, List<Integer>> getHash();
}
