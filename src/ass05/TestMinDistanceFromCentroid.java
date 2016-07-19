package ass05;

import java.util.ArrayList;
import java.util.Random;

import ass03.P2d;

public class TestMinDistanceFromCentroid {

	public static void main(String[] args) {
		final Random rand = new Random();
		final ArrayList<P2d> points = new ArrayList<>();
		for(int i=0; i<8000000; i++) {
			points.add(new P2d(rand.nextDouble()*100, rand.nextDouble()*100));
		}
		new MinDistanceFromCentroidSequential(points).calculate();
		new MinDistanceFromCentroidParallel(points).calculate();
	}

}
