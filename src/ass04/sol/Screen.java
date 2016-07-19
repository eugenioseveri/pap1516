package ass04.sol;

public class Screen {

	private int minX, minY, maxX, maxY;

	public Screen(int minX, int minY, int maxX, int maxY) {
		super();
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
	}
	
	public int getMinX(){
		return minX;
	}
	public int getMinY(){
		return minY;
	}
	public int getMaxX(){
		return maxX;
	}
	public int getMaxY(){
		return maxY;
	}
}
