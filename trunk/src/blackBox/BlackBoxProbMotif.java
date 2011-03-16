package blackBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class BlackBoxProbMotif implements BlackBox {
	List<Float> data;
	int kMotifs;
	int subsequenceLength;
	int errorRange;
	int maskSize;
	Vector<Vector<Integer>> matrix;
	int[][] matrixArray;

	public BlackBoxProbMotif() {
		this.kMotifs = 5;
		this.subsequenceLength = 5;
		this.errorRange = 5;
		this.maskSize = subsequenceLength / 2;
	}

	@Override
	public void createAnimations() {
		try {
			findMotif();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void findMotif() throws Exception {
		System.out.println("Finding motif!");
		int dataLength = data.size();
		int matrixSize = dataLength - subsequenceLength;
		Random randomGen = new Random();
		Boolean[] mask = new Boolean[subsequenceLength];
		HashMap<String, List<Integer>> hashMap = new HashMap<String, List<Integer>>();
		// TODO: Should matrix be initialised here? or outside?
		matrix = new Vector<Vector<Integer>>(matrixSize);
		matrixArray = new int[matrixSize][matrixSize];

		/* Input Checks */
		// Data length longer than single subsequence
		if (matrixSize < 0) {
			throw new Exception("Not enough data to analyse!");
		}
		// Subsequence longer than mask
		if (maskSize > subsequenceLength) {
			throw new Exception("Mask cannot be larger than a subsequence!");
		}

		/* Convert Data to Symbols */
		// TODO: split data up into symbols

		/* Generate Random Mask */
		System.out.println("Generating mask!");
		// Initialise the mask
		for (int maskNumber = 0; maskNumber < subsequenceLength; maskNumber++) {
			mask[maskNumber] = true;
		}
		// randomise mask (set to false)
		for (int i = 0; i < maskSize; i++) {
			int maskItem = randomGen.nextInt(subsequenceLength);
			if (!mask[maskItem]) {
				// try again
				i--;
			} else {
				mask[maskItem] = false;
			}
		}

		/* Sliding Window Hash */
		System.out.println("Sliding window!");
		String value;
		// For each starting frame
		for (int startFrame = 0; startFrame < matrixSize; startFrame++) {
			// TODO: Change to use data symbols, not actual data
			value = "";
			// get each of the frames of the correct length
			for (int subframe = 0; subframe < subsequenceLength; subframe++) {
				// apply the mask
				if (mask[subframe]) {
					value += data.get(startFrame + subframe);
				}
			}
			// hash it
			putIntoBucket(hashMap, value, startFrame);
		}
	}


	public int[][] getMatrixArray() {
		return matrixArray;
	}

	@Override
	public void setData(List<Float> dataList) {
		System.out.println("set data: " + dataList.size());
		data = dataList;
	}

	public void setMaskSize(int size) {
		this.maskSize = size;
	}

	public void setSubLength(int length) {
		this.subsequenceLength = length;
	}

	private void putIntoBucket(HashMap<String, List<Integer>> hash, String key, Integer value) {
		// Create a bucket
		List<Integer> bucket = new ArrayList<Integer>();
		// If bucket already exists
		if (hash.containsKey(key)) {
			System.out.println("Collision! " + value);
			// Copy existing bucket into new bucket
			bucket.addAll(hash.get(key));
			// update collision matrix
			matrixArray[hash.get(key).get(0)][value] += 1;
			System.out.println(matrixArray[hash.get(key).get(0)][value]);
		}
		// Add our new value
		bucket.add(value);
		// Replace old bucket with new bucket
		hash.put(key, bucket);
	}
}
