package blackBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BlackBoxManual implements BlackBox {

	@Override
	public int[][] getMatrixArray() {
		// TODO Auto-generated method stub
		int[][] test = new int[4][4];
		return test;
	}

	@Override
	public void iterate(int iterations) {
		// TODO Auto-generated method stub

	}

	@Override
	public HashMap<String, List<Integer>> getHash() {
		HashMap<String, List<Integer>> hash = new HashMap<String, List<Integer>>();

		List<Integer> STAND_FORWARD = new ArrayList<Integer>();
		STAND_FORWARD.add(1240); // 30 frames, STAND_FORWARD
		List<Integer> STAND_BACK = new ArrayList<Integer>();
		STAND_BACK.add(2462); // 30 frames, STAND_BACK
		List<Integer> STAND_PUNCH = new ArrayList<Integer>();
		STAND_PUNCH.add(667); // 33 frames, STAND_PUNCH
		List<Integer> STAND_KICK = new ArrayList<Integer>();
		STAND_KICK.add(1797); // 50 frames, STAND_KICK
		List<Integer> STAND_BLOCK = new ArrayList<Integer>();
		STAND_BLOCK.add(1991); // 15 frames, STAND_BLOCK
		List<Integer> STAND_UNBLOCK = new ArrayList<Integer>();
		STAND_UNBLOCK.add(2012); // 12 frames, STAND_UNBLOCK
		List<Integer> STAND_REACT_HIGH = new ArrayList<Integer>();
		STAND_REACT_HIGH.add(920); // 23 frames, STAND_REACT_HIGH
		List<Integer> STAND_REACT_LOW = new ArrayList<Integer>();
		STAND_REACT_LOW.add(975); // 50 frames, STAND_REACT_LOW
		List<Integer> STAND_REACT_HIGH_KO = new ArrayList<Integer>();
		STAND_REACT_HIGH_KO.add(975); // 50 frames, STAND_REACT_HIGH_KO
		List<Integer> STAND_REACT_LOW_KO = new ArrayList<Integer>();
		STAND_REACT_LOW_KO.add(5156); // 80 frames, STAND_REACT_LOW_KO

		List<Integer> CROUCH_DOWN = new ArrayList<Integer>();
		CROUCH_DOWN.add(2462); // 20 frames, CROUCH_DOWN
		List<Integer> CROUCH_UP = new ArrayList<Integer>();
		CROUCH_UP.add(2967); // 28 frames, CROUCH_UP
		List<Integer> CROUCH_PUNCH = new ArrayList<Integer>();
		CROUCH_PUNCH.add(2608); // 20 frames, CROUCH_PUNCH
		List<Integer> CROUCH_BLOCK = new ArrayList<Integer>();
		CROUCH_BLOCK.add(2755); // 10 frames, CROUCH_BLOCK
		List<Integer> CROUCH_UNBLOCK = new ArrayList<Integer>();
		CROUCH_UNBLOCK.add(2811); // 15 frames, CROUCH_UNBLOCK
		List<Integer> CROUCH_REACT = new ArrayList<Integer>();
		CROUCH_REACT.add(3453); // 25 frames, CROUCH_REACT

		hash.put("STAND_FORWARD", STAND_FORWARD);
		hash.put("STAND_BACK", STAND_BACK);
		hash.put("STAND_PUNCH", STAND_PUNCH);
		hash.put("STAND_KICK", STAND_KICK);
		hash.put("STAND_BLOCK", STAND_BLOCK);
		hash.put("STAND_UNBLOCK", STAND_UNBLOCK);
		hash.put("STAND_REACT_HIGH", STAND_REACT_HIGH);
		hash.put("STAND_REACT_LOW", STAND_REACT_LOW);
		hash.put("STAND_REACT_HIGH_KO", STAND_REACT_HIGH_KO);
		hash.put("STAND_REACT_LOW_KO", STAND_REACT_LOW_KO);

		hash.put("CROUCH_DOWN", CROUCH_DOWN);
		hash.put("CROUCH_UP", CROUCH_UP);
		hash.put("CROUCH_PUNCH", CROUCH_PUNCH);
		hash.put("CROUCH_BLOCK", CROUCH_BLOCK);
		hash.put("CROUCH_UNBLOCK", CROUCH_UNBLOCK);
		hash.put("CROUCH_REACT", CROUCH_REACT);
		return hash;
	}

}
