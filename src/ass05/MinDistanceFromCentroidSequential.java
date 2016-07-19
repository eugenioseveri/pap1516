package ass05;

import java.util.Comparator;
import java.util.List;

import ass03.P2d;

public class MinDistanceFromCentroidSequential extends AbstractMinDistanceFromCentroid {

	public MinDistanceFromCentroidSequential(List<P2d> points) {
		super(points);
	}
	
	public void calculate() {
		final P2d centroid = calculateCentroid(); // Calcola il baricentro
		final long startTime = System.currentTimeMillis(); // Tempo a inizio elaborazione a scopo di benchmark
		System.out.println("Nearest point from centroid (sequential): " +
							points.stream().min(Comparator.comparingDouble(point -> P2d.distance(point, centroid))) +
							". Time " + (System.currentTimeMillis()-startTime) + " ms.");
	}

}
