package ass05;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import ass03.P2d;

import static ass03.P2d.distance;

public class MinDistanceFromCentroidParallel extends AbstractMinDistanceFromCentroid {

	private final static int cpuNum = Runtime.getRuntime().availableProcessors();
	private List<Thread> workersList = new ArrayList<Thread>();
	
	public MinDistanceFromCentroidParallel(List<P2d> points) {
		super(points);
	}
	
	public void calculate() {
		final int loadForEachThread = points.size() / cpuNum; // Calcola il numero di punti per ogni thread
		final P2d centroid = calculateCentroid(); // Calcola il baricentro
		Object lock = new Object(); // Lock per l'aggiornamento delle variabili condivise
		final long startTime = System.currentTimeMillis(); // Tempo a inizio elaborazione a scopo di benchmark
		IntStream.range(0, cpuNum).forEach(threadIndex -> { // Crea un thread per ogni cpu virtuale disponibile
			Thread worker = new Thread(() -> {
				final int startOffset = loadForEachThread * threadIndex; // Inizia il calcolo dall'offset corrispondente al thread
				double localCurrentNearestPointX = 0, localCurrentNearestPointY = 0, localCurrentMinimumDistance = Double.MAX_VALUE; // Variabili locali per memorizzare gli ottimi locali
				for (int i=startOffset; i<startOffset+loadForEachThread; i++) { // Esegue i calcoli relativi al thread corrente
					double thisDistance = distance(points.get(i), centroid); // Calcola la distanza del punto corrente dal baricentro
					if(thisDistance < localCurrentMinimumDistance) { // Se la distanza del punto attuale è ottima fra quelle trovate per ora, viene considerato come nuovo ottimo locale
						localCurrentNearestPointX = points.get(i).getX();
						localCurrentNearestPointY = points.get(i).getY();
						localCurrentMinimumDistance = thisDistance;
					}
				}
				synchronized(lock) { // Aggiornamento delle variabili condivise trai thread contenenti l'ottimo globale
					if(localCurrentMinimumDistance < super.getCurrentMinimumDistance()) {
						super.setCurrentMinimumDistance(localCurrentMinimumDistance);
						super.setCurrentNearestPointX(localCurrentNearestPointX);
						super.setCurrentNearestPointY(localCurrentNearestPointY);
					}
				}
			});
			worker.start();
			workersList.add(worker);
		});
		for(Thread worker: workersList) {
			try {
				worker.join(); // Attende la terminazione di ogni thread
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Nearest point from centroid (parallel): " + super.getCurrentNearestPointX() + "," + super.getCurrentNearestPointY() +
							". Time " + (System.currentTimeMillis()-startTime) + " ms.");
		System.out.println("Minimum distance from centroid: " + super.getCurrentMinimumDistance());
	}
}
