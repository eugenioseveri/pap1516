package ass06.seeds;

public class TestSeeds {
	
	// Valori di default (modificabili da command line)
	private static int modelWidth, modelHeight, viewWidth, viewHeight, taskNum, frameRate, randomAliveCells;
	
	public static void main(String[] args) {
		if(args.length==7) { // Se tutti i parametri sono specificati da command line, li imposta
			try {
				modelWidth = Integer.parseInt(args[0]);
				modelHeight = Integer.parseInt(args[1]);
				viewWidth = Integer.parseInt(args[2]);
				viewHeight = Integer.parseInt(args[3]);
				taskNum = Integer.parseInt(args[4]);
				frameRate = Integer.parseInt(args[5]);
				randomAliveCells = Integer.parseInt(args[6]);
			} catch (NumberFormatException ex) {
				System.out.println("I parametri devono essere interi!");
				System.exit(1);
			}
		} else if (args.length==0) { // Imposta i parametri di default
			modelWidth = 80;
			modelHeight = 50;
			viewWidth = 810;
			viewHeight = 600;
			taskNum = 10;
			frameRate = 10;
			randomAliveCells = 150;
		} else {
			System.out.println("Il numero di parametri specificati non è valido. Verranno caricati i valori di default.");
		}
		
		SeedsModel model = new SeedsModel(modelWidth, modelHeight);
		model.randomInitialization(randomAliveCells); // Inizializza le celle con valori random
		SeedsView view = new SeedsView(viewWidth, viewHeight, model);
		Controller controller = new Controller(model, view, frameRate, taskNum);
		view.attachObserver(controller);
		view.setVisible(true);
	}

}
