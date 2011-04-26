package blackBox;

import java.util.HashMap;
import java.util.List;


public interface BlackBox {

	void iterate(int iterations);

	int[][] getMatrixArray();

	HashMap<String, List<Integer>> getHash();
}
