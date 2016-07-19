package ass08.rxjava;

import pap.ass08.pos.PosSensor;

public class Main {

	public static void main(String[] args) {
		int threshold = 0, k = 0;
		try {
			threshold = Integer.parseInt(args[0]);
			k = Integer.parseInt(args[1]);
		} catch (ArrayIndexOutOfBoundsException ex) {
			System.out.println("Parametri richiesti: threshold, K!");
			System.exit(1);
		} catch (NumberFormatException ex) {
			System.out.println("I parametri devono essere interi!");
			System.exit(1);
		}
		Model model = new Model(new HeartbeatStream(500), new PosSensor());
		View view = new View();
		Controller controller = new Controller(model, view, threshold, k);
		view.setListener(controller);
        view.setVisible(true);
	}

}
