package ass04;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import ass04.TextLib.Color;

public class BouncingWords {
	
	private final static TextLib textLib = TextLibFactory.getInstance();
	private final static byte consoleWidth = 79;
	private final static byte consoleHeight = 24;
	
	public static void main(String[] args) {
		// Acquisizione parole da stampare (passate come parametri)
		if(args.length==0) {
			System.out.println("Lista di parole non passata come parametro!");
			System.exit(1);
		}
		final List<String> words = new ArrayList<>();
		words.addAll(Arrays.asList(args));
		
		textLib.cls(); // Pulizia schermo
		words.stream().forEach(currentWord -> { // Crezione di un thread per ogni parola in input
			Thread worker = new Thread(() -> {
				
				// Randomizzatori per stabilire posizione iniziale della parola (x e y), stepping (1 o 2) e colore
				int currentX = ThreadLocalRandom.current().nextInt(0, consoleWidth);
				int currentY = ThreadLocalRandom.current().nextInt(0, consoleHeight);
				boolean step = (boolean) ThreadLocalRandom.current().nextBoolean();
				Color color = null;
				switch(ThreadLocalRandom.current().nextInt(0, 7)) {
					case 0: color = Color.BLACK; break;
					case 1: color = Color.RED; break;
					case 2: color = Color.GREEN; break;
					case 3: color = Color.YELLOW; break;
					case 4: color = Color.BLUE; break;
					case 5: color = Color.MAGENTA; break;
					case 6: color = Color.CYAN; break;
					case 7: color = Color.WHITE; break;
				}
				
				while(true) { // Ciclo infinito del thread che stampa
					if(step) {
						textLib.writeAt(++currentX, ++currentY, currentWord, color);
					} else {
						textLib.writeAt(currentX+=2, currentY+=2, currentWord, color);
					}
					// Controlli sul raggiungimento dei bordi
					if (currentX+currentWord.length() >= consoleWidth){
			            currentX = 0;
			        } else if (currentY >= consoleHeight){
			            currentY = 0;
			        }
					try {
						Thread.sleep(300); // Attesa
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			worker.start(); // Avvio thread
		});
	}
}
