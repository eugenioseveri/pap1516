package ass08.rxjava;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import pap.ass08.pos.P2d;


public class View extends JFrame implements ActionListener {

	private static final long serialVersionUID = 806866336457502717L;
	private Controller controller;
	private final JButton startButton = new JButton("Start");
    private final JButton stopButton = new JButton("Stop");
    private final JLabel currentHeartbeatValue = new JLabel("---");
    private final JLabel averageHeartbeatValue = new JLabel("---");
    private final JLabel alarmNotification = new JLabel("False");
    private final JLabel currentSpeed = new JLabel("---");
    private final JLabel positionWithMaxHeartbeatValue = new JLabel("---");
    private final JPanel topPanel = new JPanel();
    private final JPanel bottomPanel = new JPanel();
    private final PositionViewPanel gridPanel = new PositionViewPanel();
    
    public View() {
    	this.setSize(800, 700);
    	this.setTitle("TrackBeat");
    	this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    	this.setLocationRelativeTo(null);
        this.startButton.addActionListener(this);
        this.stopButton.addActionListener(this);
        this.stopButton.setEnabled(false);
        this.setLayout(new BorderLayout());
        topPanel.add(this.startButton);
        topPanel.add(this.stopButton);
        topPanel.add(new JLabel("Valore battito corrente:"));
        topPanel.add(this.currentHeartbeatValue);  
        topPanel.add(new JLabel("Valore battito medio:"));
        topPanel.add(this.averageHeartbeatValue);
        topPanel.add(new JLabel("Allarme:"));
        topPanel.add(this.alarmNotification);
        bottomPanel.add(new JLabel("Velocità corrente:"));
        bottomPanel.add(this.currentSpeed);
        bottomPanel.add(new JLabel("Posizione con battito maggiore:"));
        bottomPanel.add(this.positionWithMaxHeartbeatValue);
        this.add(topPanel, BorderLayout.NORTH);
        this.add(gridPanel, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);
    }
    
    public void setListener(Controller controller){
        this.controller = controller;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
    	if(e.getSource().equals(this.startButton)){
    		this.stopButton.setEnabled(true);
    		this.startButton.setEnabled(false);
    		this.controller.started();
    	}
    	if(e.getSource().equals(this.stopButton)) {
    		this.stopButton.setEnabled(false);
    		this.startButton.setEnabled(true);
    		this.controller.stopped();
    	}
    }
    
    public void updateCurrentHeartbeatValue(String value) {
    	this.currentHeartbeatValue.setText(value);
    }
    
    public void updateAverageHeartbeatValue(String value) {
    	this.averageHeartbeatValue.setText(value);
    }
    
    public void updateAlarmNotification(String value) {
    	this.alarmNotification.setText(value);
    }
    
    public void updateCurrentSpeed(String value) {
    	this.currentSpeed.setText(value);
    }
    
    public void updatePositionWithMaxHeartbeatValue(String value) {
    	this.positionWithMaxHeartbeatValue.setText(value);
    }
    
    public void updateGridPosition(P2d currentPoint) {
    	this.gridPanel.updatePosition(currentPoint);
    }
    
    private class PositionViewPanel extends JPanel {
    	
		private static final long serialVersionUID = -5493703615214052005L;
		private static final int dx = 10;
		private static final int dy = 10;
		private static final int nCellsX = 800;
		private static final int nCellsY = 600;
		private P2d currentActivePoint;

		public PositionViewPanel(){
			super();
		}

		@Override
		public void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setBackground(Color.WHITE);
			g2.clearRect(0, 0, nCellsX * dx, nCellsY * dy);
			g2.setColor(Color.RED);
			if(this.currentActivePoint!=null) {
				g2.fillRect((int)Math.round(this.currentActivePoint.getX())+PositionViewPanel.nCellsX/2,
						(int)Math.round(this.currentActivePoint.getY())+PositionViewPanel.nCellsY/2,
						dx-1, dy-1);
			}
		}
		
		public void updatePosition(P2d currentPoint) {
			this.currentActivePoint = currentPoint;
			this.repaint();
		}

    }
    
}
