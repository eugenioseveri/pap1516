package ass06.seeds;

public class Controller implements InputListener {

	private SeedsModel model;
	private Flag stopFlag;
	private SeedsView view;
	private int frameRate;
	private int nTasks;
	
	public Controller(SeedsModel model, SeedsView view, int frameRate, int nTasks) {
		this.model = model;
		this.view = view;
		this.frameRate = frameRate;
		this.nTasks = nTasks;
	}
	
	public void startCmd() {
		if (this.stopFlag == null) {
			this.stopFlag = new Flag();
		} else {
			this.stopFlag.reset();
		}
		new Master(this.model, this.view, this.stopFlag, this.frameRate, this.nTasks).start();
	}

	public void stopCmd() {
		this.stopFlag.set();
	}

	@Override
	public void resetCmd() {
		this.model.reset();
		this.view.update(0);
		this.stopFlag.set();
	}

}
