package ass06.seeds;

import java.util.concurrent.Callable;

public class CalculateNextStateTask implements Callable<Integer> {

	private int x0;
	private int y0;
	private int width;
	private int height;
	private SeedsModel model;

	public CalculateNextStateTask(SeedsModel model, int x0, int y0, int width, int height) {
		this.x0 = x0;
		this.y0 = y0;
		this.width = width;
		this.height = height;
		this.model = model;
	}

	public Integer call() {
		int nAliveCells = 0;
		for(int i=0; i<this.width; i++) {
			for(int j=0; j<this.height; j++) {
				if(this.model.calculateNextCellState(this.x0+i, this.y0+j)) {
					nAliveCells++;
				}
			}
		}
		return nAliveCells;
	}
}
