package blackBox;

import java.util.List;

public class BlackBoxProbMotif implements BlackBox {
	List<Float> data;
	int kMotifs;
	int subsequenceLength;
	int errorRange;
	
	public BlackBoxProbMotif(){
		this.kMotifs = 5;
		this.subsequenceLength = 5;
		this.errorRange = 5;
	}
	
	@Override
	public void setData(List<Float> dataList) {
		data = dataList;		
	}

	@Override
	public void createAnimations() {
		// TODO Auto-generated method stub
		
	}

	public void findMotif(){
		
	}
	
}
