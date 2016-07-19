package ass03;

import java.util.List;
import java.util.Optional;
import static java.util.stream.Collectors.toList;

import java.util.Comparator;

public class MyPointCloud implements PointCloud {

	private List<P2d> p;
	
	public MyPointCloud(List<P2d> p) {
		this.p = p;
	}

	@Override
	public void move(V2d v) {
		this.p = this.p.stream()
				.map(point -> point.sum(v)) // Somma ad ogni punto il vettore specificato
				.collect(toList());
	}

	@Override
	public List<P2d> getPointsInRegion(Region r) {
		return this.p.stream() // Per ogni punto, controlla che si trovi all'interno della region specificata
				.filter(point -> point.getX() >= r.getUpperLeft().getX())
				.filter(point -> point.getX() <= r.getBottomRight().getX())
				.filter(point -> point.getY() >= r.getBottomRight().getY())
				.filter(point -> point.getY() <= r.getUpperLeft().getY())
				.collect(toList());
	}

	@Override
	public Optional<P2d> getNearestPoint(P2d p) {
		return this.p.stream()
				.min(Comparator.comparingDouble(point -> P2d.distance(point, p))); // Ottiene il punto con distanza euclidea minima rispetto a quello sprcificato
	}
	
	@Override
	public String toString() {
		String pointsToString = this.p.stream()
				.map(P2d::toString) // Mappa ogni punto nella sua rappresentazione testuale
				.reduce((a, b) -> a + ", " + b) // Scrive i punti concatenandone le stringhe che li descrivono, e aggiunge un separatore
				.get();
		return "{ " + pointsToString + " }";
	}

}
