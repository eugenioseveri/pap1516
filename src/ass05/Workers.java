package ass05;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Workers {
	
	private final static Counters counters = new Counters(0, 0, 0, 0); // I quattro contatori sono inizializzati a 0 (si può variare)
	private final static Random rand = new Random();
	private final static int MIN_WAIT = 50; // Attesa minima tra due cicli di W1 (per rallentare l'output)
	private final static int MAX_WAIT = 500; // Attesa massima tra due cicli di W1 (per rallentare l'output)
	private final static Semaphore s1 = new Semaphore(0); // Semaforo usato da W1 per svegliare W2
	private final static Semaphore s2 = new Semaphore(0); // Semaforo usato da W1 per svegliare W3
	private final static Semaphore updatedC2 = new Semaphore(0); // Semaforo usato da W2 per svegliare W4
	private final static Semaphore updatedC3 = new Semaphore(0); // Semaforo usato da W3 per svegliare W5
	private final static Semaphore updatedC4 = new Semaphore(0); // Semaforo usato da W4 e W5 per svegliare W6
	private final static Semaphore printedC4 = new Semaphore(1); // Semaforo usato da W6 per svegliare W1. Deve partire da 1, altrimenti W1, che dà inizio a tutto, rimane bloccato

	private static void wasteRandomTime(long min, long max){ // Metodo che genera attese casuali comprese nell'intervallo
		try {
			double value = rand.nextDouble();
			double delay = min + value*(max-min);
			Thread.sleep((int)delay);
		} catch (InterruptedException ex){
			ex.printStackTrace();
		}
	}
	
	public class W1 extends Thread {
		@Override
		public void run() {
			while(true) {
				try {
					Workers.printedC4.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Workers.counters.getC1().inc();
				Workers.s1.release();
				Workers.s2.release();
				Workers.wasteRandomTime(MIN_WAIT, MAX_WAIT); // Utilizzata per rallentare l'output
			}
		}
	}
	
	public class W2 extends Thread {
		@Override
		public void run() {
			while(true) {
				try {
					Workers.s1.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Workers.counters.getC2().inc();
				Workers.updatedC2.release();
			}
		}
	}
	
	public class W3 extends Thread {
		@Override
		public void run() {
			while(true) {
				try {
					Workers.s2.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Workers.counters.getC3().inc();
				Workers.updatedC3.release();
			}
		}
	}
	
	public class W4 extends Thread {
		@Override
		public void run() {
			while(true) {
				try {
					Workers.updatedC2.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("C2 = " + Workers.counters.getC2().getValue());
				Workers.counters.getC4().inc();
				Workers.updatedC4.release();
			}
		}
	}
	
	public class W5 extends Thread {
		@Override
		public void run() {
			while(true) {
				try {
					Workers.updatedC3.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("C3 = " + Workers.counters.getC3().getValue());
				Workers.counters.getC4().inc();
				Workers.updatedC4.release();
			}
		}
	}
	
	public class W6 extends Thread {
		@Override
		public void run() {
			while(true) {
				try {
					Workers.updatedC4.acquire(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("C4 = " + Workers.counters.getC4().getValue());
				Workers.printedC4.release();
			}
		}
	}

}
