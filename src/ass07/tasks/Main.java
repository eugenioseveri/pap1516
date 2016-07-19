package ass07.tasks;

import ass07.tasks.Controller;
import ass07.tasks.GameModel;
import ass07.tasks.MainView;

import java.awt.*;

public class Main {

    public static void main(String[] args) {
        int playersNumber = 0;
        try {
			playersNumber = Integer.parseInt(args[0]);
		} catch (ArrayIndexOutOfBoundsException ex) {
			System.out.println("Numero player non passato come parametro!");
			System.exit(1);
		} catch (NumberFormatException ex) {
			System.out.println("Il numero di player deve essere un intero!");
			System.exit(1);
		}
        GameModel model = new GameModel(playersNumber);
        MainView view = new MainView(new Dimension(600, 600));
        Controller controller = new Controller(view, model);
        view.setListener(controller);
        view.setVisible(true);
    }
}