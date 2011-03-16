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
	int framesPerLetter;
	int alphaSize; // size of alphabet

	public BlackBoxProbMotif() {
		this.kMotifs = 5;
		this.subsequenceLength = 5;
		this.errorRange = 5;
		this.maskSize = subsequenceLength / 2;
		this.framesPerLetter = 4;
		this.alphaSize = 5;
	}

	@Override
	public void createAnimations() {
		try {
			findMotif();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<String> symbolise() {
		List<Float> paa = new ArrayList<Float>();
		List<String> symbolData = new ArrayList<String>();
		int finalFrame = data.size() - framesPerLetter;

		/* Create Piecewise Aggregate Approximation (PAA) */
		int startFrame = 0;
		Float currentAvg;
		// Sliding window through data
		while (startFrame < finalFrame) {
			// Calculate average of current window
			currentAvg = 0.0f;
			for (int f = startFrame; f < startFrame + framesPerLetter; f++) {
				currentAvg += data.get(f);
			}
			currentAvg = currentAvg / framesPerLetter;

			// Store current average to PAA
			paa.add(currentAvg);

			// Move sliding window
			startFrame += framesPerLetter;
		}

		/* Discretise into symbols */
		// TODO: use gaussian for symbols
		

		return symbolData;
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
			System.out.println("Hashing (K, V): " + value + ", " + startFrame);
			// hash it
			putIntoBucket(hashMap, value, startFrame);
		}
	}


	public int[][] getMatrixArray() {
		return matrixArray;
	}

	public void setAlphaSize(int size) {
		this.alphaSize = size;
	}

	@Override
	public void setData(List<Float> dataList) {
		System.out.println("set data: " + dataList.size());
		data = dataList;
	}

	public void setFramesPerLetter(int w) {
		this.framesPerLetter = w;
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
			System.out.println("Collision! New: " + value);
			List<Integer> oldVals = hash.get(key);
			System.out.println("Collides with " + oldVals.size() + " values");
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
