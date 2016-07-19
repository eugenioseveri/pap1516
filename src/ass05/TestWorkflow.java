package ass05;

public class TestWorkflow {

	public static void main(String[] args) {
		Workers workers = new Workers();
		workers.new W1().start();
		workers.new W2().start();
		workers.new W3().start();
		workers.new W4().start();
		workers.new W5().start();
		workers.new W6().start();
	}

}
