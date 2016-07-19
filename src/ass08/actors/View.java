package ass08.actors;

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

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import pap.ass08.pos.P2d;

public class View extends UntypedActor implements ActionListener {

	//private ass08.rxjava.View intermediateView; // Riutilizzo della gui sviluppata per la prima parte dell'esercizio
	private JFrame gui;
	private ActorRef controller;
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
	
	@Override
    public void preStart() {
		// Inizializzazione GUI
		this.gui = new JFrame();
		this.gui.setSize(800, 700);
    	this.gui.setTitle("TrackBeat");
    	this.gui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    	this.gui.setLocationRelativeTo(null);
        this.startButton.addActionListener(this);
        this.stopButton.addActionListener(this);
        this.stopButton.setEnabled(false);
        this.gui.setLayout(new BorderLayout());
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
        this.gui.add(topPanel, BorderLayout.NORTH);
        this.gui.add(gridPanel, BorderLayout.CENTER);
        this.gui.add(bottomPanel, BorderLayout.SOUTH);
        this.gui.setVisible(true);
	}
	
	@Override
    public void actionPerformed(ActionEvent e) {
    	if(e.getSource().equals(this.startButton)){
    		this.stopButton.setEnabled(true);
    		this.startButton.setEnabled(false);
    		this.controller.tell(new RunMessage(true), getSelf());
    	}
    	if(e.getSource().equals(this.stopButton)) {
    		this.stopButton.setEnabled(false);
    		this.startButton.setEnabled(true);
    		this.controller.tell(new RunMessage(false), getSelf());
    	}
    }
	
	@Override
	public void onReceive(Object message) throws Exception {
		if(message instanceof setListenerMessage) {
			this.controller = ((setListenerMessage) message).getMessage();
		} else if(message instanceof updateCurrentHeartbeatValueMessage) {
			this.currentHeartbeatValue.setText(((updateCurrentHeartbeatValueMessage) message).getMessage());
		} else if(message instanceof updateAverageHeartbeatValueMessage) {
			this.averageHeartbeatValue.setText(((updateAverageHeartbeatValueMessage) message).getMessage());
		} else if(message instanceof updateAlarmNotificationMessage) {
			this.alarmNotification.setText(((updateAlarmNotificationMessage) message).getMessage());
		} else if(message instanceof updateCurrentSpeedMessage) {
			this.currentSpeed.setText(((updateCurrentSpeedMessage) message).getMessage());
		} else if(message instanceof updatePositionWithMaxHeartbeatValueMessage) {
			this.positionWithMaxHeartbeatValue.setText(((updatePositionWithMaxHeartbeatValueMessage) message).getMessage());
		} else if(message instanceof updateGridPositionMessage) {
			this.gridPanel.updatePosition(((updateGridPositionMessage) message).getMessage());
		} else {
			unhandled(message);
		}
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
