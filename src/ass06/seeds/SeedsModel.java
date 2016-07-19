package ass06.seeds;

import java.util.Random;

public class SeedsModel {

	private int width, height;
	private boolean currentStateMatrix[][];
	private boolean nextStateMatrix[][];

	public SeedsModel(int width, int height) {
		this.width = width;
		this.height = height;
		this.currentStateMatrix = new boolean[this.height][this.width];
		this.nextStateMatrix = new boolean[this.height][this.width]; 
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}
	
	// Reinizializza le matrici degli stati
	public void reset() {
		for(int i=0; i<this.height; i++) {
			for(int j=0; j<this.width; j++) {
				this.currentStateMatrix[i][j] = false; 
				this.nextStateMatrix[i][j] = false;
			}
		}
	}
	
	// Inverte la matrice corrente con quella dello stato successivo
	public void swap() {
		boolean[][] aux = this.currentStateMatrix;
		this.currentStateMatrix = this.nextStateMatrix;
		this.nextStateMatrix = aux;
	}

	public boolean isAlive(int x, int y) {
		return this.currentStateMatrix[y][x];
	}

	public void changeState(int x, int y) {
		this.currentStateMatrix[y][x] = !this.currentStateMatrix[y][x];
	}

	public boolean calculateNextCellState(int x, int y) {
		int nAlives = 0;
		int xPrev = x==0 ? this.width-1 : x-1;
		int yPrev = y==0 ? this.height-1 : y-1;
		int xNext = (x+1) % this.width;
		int yNext = (y+1) % this.height;
		if (this.currentStateMatrix[yPrev][xPrev]) nAlives++;
		if (this.currentStateMatrix[yPrev][x]) nAlives++;
		if (this.currentStateMatrix[yPrev][xNext]) nAlives++;
		if (this.currentStateMatrix[y][xPrev]) nAlives++;
		if (this.currentStateMatrix[y][xNext]) nAlives++;
		if (this.currentStateMatrix[yNext][xPrev]) nAlives++;
		if (this.currentStateMatrix[yNext][x]) nAlives++;
		if (this.currentStateMatrix[yNext][xNext]) nAlives++;
		if (nAlives == 2) {
			this.nextStateMatrix[y][x] = true;
		} else {
			this.nextStateMatrix[y][x] = false;
		}
		return this.nextStateMatrix[y][x];
	}

	public void randomInitialization(int n) {
		for(int i=0; i<this.height; i++){ // Reinizializza la matrice
			for(int j=0; j<this.width; j++) {
				this.currentStateMatrix[i][j] = false;
			}
		}
		Random myRandom = new Random(System.currentTimeMillis());
		for(int i=0; i<n; i++) { // Riempie la matrice con n celle "alive"
			int x = myRandom.nextInt(this.width);
			int y = myRandom.nextInt(this.height);
			this.currentStateMatrix[y][x] = true;
		}
	}
}
