package ass05;

import java.util.List;

import ass03.P2d;

public abstract class AbstractMinDistanceFromCentroid {

	protected List<P2d> points;
	private double currentNearestPointX = 0, currentNearestPointY = 0, currentMinimumDistance = Double.MAX_VALUE;
	
	public AbstractMinDistanceFromCentroid(List<P2d> points) {
		super();
		this.points = points;
	}

	public List<P2d> getPoints() {
		return points;
	}
	
	protected double getCurrentNearestPointX() {
		return currentNearestPointX;
	}

	protected void setCurrentNearestPointX(double currentNearestPointX) {
		this.currentNearestPointX = currentNearestPointX;
	}

	protected double getCurrentNearestPointY() {
		return currentNearestPointY;
	}

	protected void setCurrentNearestPointY(double currentNearestPointY) {
		this.currentNearestPointY = currentNearestPointY;
	}

	protected double getCurrentMinimumDistance() {
		return currentMinimumDistance;
	}

	protected void setCurrentMinimumDistance(double currentMinimumDistance) {
		this.currentMinimumDistance = currentMinimumDistance;
	}

	protected P2d calculateCentroid() { // Calcola il baricentro dell'insieme di punti
		double centroidX = 0, centroidY = 0;
		for(P2d point: this.points) {
			centroidX += point.getX();
			centroidY += point.getY();
		}
		return new P2d(centroidX / this.points.size(), centroidY / this.points.size());
	}
}
