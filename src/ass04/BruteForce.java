package ass04;

import java.util.stream.IntStream;

public class BruteForce {
	
	private final static int cpuNum = Runtime.getRuntime().availableProcessors();
	private final static byte minPrintableAsciiChar = 32;
	private final static byte maxPrintableAsciiChar = 127;
	/* "loadForEachThread"
	 * Calcolo suddivisione lavoro in base al numero di core disponibili. Nel caso in cui
	 * il numero di caratteri non sia perfettamente divisibile per il numero di core, il
	 * carico sarà leggermente sbilanciato tra i thread. Tuttavia tale sbilanciamento
	 * impatterebbe sulle performance in maniera marginale e solo nel caso pessimo,
	 * nel quale si rende necessario provare tutte le 96^n combinazioni, mentre nel caso
	 * medio ((96^n)/2) non sarà presente alcun rallentamento sul finire dell'elaborazione.
	*/
	private final static byte loadForEachThread = (byte) ((maxPrintableAsciiChar - minPrintableAsciiChar + 1) / cpuNum);
	private static byte passwordSize = 0;
	private static SecureSystem mySecSys;
	private static long startTime; // Tempo preso prima dell'inizio dell'elaborazione, per misurare la velocità d'esecuzione
	
	public static void main(String[] args) {
		// Acquisizione lunghezza password (passata come parametro)
		try {
			passwordSize = Byte.parseByte(args[0]);
		} catch (ArrayIndexOutOfBoundsException ex) {
			System.out.println("Lunghezza password non passata come parametro!");
			System.exit(1);
		} catch (NumberFormatException ex) {
			System.out.println("La lunghezza della password deve essere un intero!");
			System.exit(1);
		}
		
		mySecSys = new SecureSystem(passwordSize); // Creazione oggetto SecureSystem
		startTime = System.currentTimeMillis(); // Misura il tempo prima di avviare l'elaborazione
		
		IntStream.range(0, cpuNum).forEach(threadIndex -> { // Creazione di un thread per ogni core
			Thread worker = new Thread(() -> {
				final byte startOffset = (byte) (loadForEachThread*threadIndex); // Inizia il calcolo dall'offset corrispondente al thread
				final StringBuilder myStringBuilder = new StringBuilder();
				myStringBuilder.setLength(passwordSize);
				for (byte i=startOffset; i<(byte) (startOffset+loadForEachThread); i++) {
					myStringBuilder.setCharAt(0, (char) (i+minPrintableAsciiChar));
					tryPassword(myStringBuilder, 1);
				}
			});
			worker.start(); // Avvia il thread
		});
	}
	
	// Funzione ricorsiva che tenta le possibili combinazioni di caratteri ammissibili come password
	private static void tryPassword(StringBuilder myStringBuilder, int position) {
        if (position == myStringBuilder.length()) {
            String currentPassword = myStringBuilder.toString();
            if (mySecSys.login(currentPassword)) {
                System.out.println("Passowrd individuata: " + currentPassword + " da " + Thread.currentThread().getName());
    			System.out.println("Tempo di esecuzione: "+ (System.currentTimeMillis()-startTime) + " ms");
                System.exit(0);
            }
            return;
        }
        for (char character=minPrintableAsciiChar; character<=maxPrintableAsciiChar; character++) {
            myStringBuilder.setCharAt(position, character);
            tryPassword(myStringBuilder, position+1);
        }
    }
}
