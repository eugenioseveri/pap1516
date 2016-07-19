package ass06.seeds;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class SeedsView extends JFrame implements ActionListener {

	private static final long serialVersionUID = -478493669333668944L;
	private final JButton startButton;
	private final JButton stopButton;
	private final JButton resetButton;
	private final JTextField nGeneration;
	private final JTextField nAliveCells;
	private final ViewPanel gridPanel;
	private final SeedsModel model;
	private final JPanel controlPanel;
	private final JPanel infoPanel;
	private final JPanel containerPanel;
	private InputListener listener;

	public SeedsView(int width, int height, SeedsModel model) {
		super("Seeds");
		this.model = model;
		this.setSize(width,height);
		this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - getWidth()) / 2, 
				(Toolkit.getDefaultToolkit().getScreenSize().height - getHeight()) / 2);
		this.startButton = new JButton("Start");
		this.stopButton = new JButton("Stop");
		this.resetButton = new JButton("Reset");
		this.stopButton.setEnabled(false);
		this.resetButton.setEnabled(false);
		this.controlPanel = new JPanel();
		this.controlPanel.add(this.startButton);
		this.controlPanel.add(this.stopButton);
		this.controlPanel.add(this.resetButton);
		this.gridPanel = new ViewPanel(); 
		this.gridPanel.setSize(width,height);
		this.nAliveCells = new JTextField(10);
		this.nAliveCells.setText("0");
		this.nAliveCells.setEditable(false);
		this.nGeneration = new JTextField(10);
		this.nGeneration.setText("0");
		this.nGeneration.setEditable(false);
		this.infoPanel = new JPanel();
		this.infoPanel.add(new JLabel("Generazione:"));
		this.infoPanel.add(this.nGeneration);
		this.infoPanel.add(new JLabel("Celle vive:"));
		this.infoPanel.add(this.nAliveCells);
		this.containerPanel = new JPanel();
		this.containerPanel.setLayout(new BorderLayout());
		this.containerPanel.add(BorderLayout.NORTH, this.controlPanel);
		this.containerPanel.add(BorderLayout.CENTER, this.gridPanel);
		this.containerPanel.add(BorderLayout.SOUTH, this.infoPanel);
		setContentPane(this.containerPanel);		
		this.startButton.addActionListener(this);
		this.stopButton.addActionListener(this);
		this.resetButton.addActionListener(this);
		this.setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void update(int numAliveCells) {
		SwingUtilities.invokeLater(() -> {
			this.nAliveCells.setText(Integer.toString(numAliveCells));
			int generationNum = Integer.parseInt(this.nGeneration.getText()) + 1;
			this.nGeneration.setText(Integer.toString(generationNum));
			this.repaint();
		});
	}

	public void attachObserver(InputListener listener) {
		this.listener = listener;
	}

	public void actionPerformed(ActionEvent ev) {
		Object source = ev.getSource(); 
		if (source.equals(this.startButton)) {
			this.startButton.setEnabled(false);
			this.stopButton.setEnabled(true);
			this.resetButton.setEnabled(true);
			this.listener.startCmd();
		} else if (source.equals(this.stopButton)) {
			this.startButton.setEnabled(true);
			this.stopButton.setEnabled(false);
			this.listener.stopCmd();
		} else if (source.equals(this.resetButton)) {
			this.startButton.setEnabled(true);
			this.stopButton.setEnabled(false);
			this.resetButton.setEnabled(false);
			this.listener.resetCmd();
		}
	}

	private class ViewPanel extends JPanel implements MouseListener, MouseMotionListener {

		private static final long serialVersionUID = -2151904547205538250L;
		private static final int dx = 10;
		private static final int dy = 10;
		private int nCellsX;
		private int nCellsY;

		public ViewPanel(){
			this.addMouseListener(this);
			this.addMouseMotionListener(this);
			this.nCellsX = 80;
			this.nCellsY = 50;
		}

		@Override
		public void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;

			g2.setBackground(Color.WHITE);
			g2.clearRect(0, 0, nCellsX * dx, nCellsY * dy);

			int x = 0;
			g2.setColor(Color.GRAY);
			for (int i = 0; i <= nCellsX; i++) {
				g2.drawLine(x, 0, x, nCellsY * dy);
				x += dx;
			}

			int y = 0;
			for (int j=0; j<=nCellsY; j++) {				
				g2.drawLine(0, y, nCellsX * dx, y);
				y += dy;
			}

			g2.setColor(Color.GREEN);
			for (int i=0; i<nCellsX; i++) {
				for (int j=0; j<nCellsY; j++) {
					if (model.isAlive(i, j)) {
						g2.fillRect(i * dx + 1, j * dy + 1, dx - 1, dy - 1);
					}
				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			this.drawPoints(e);
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			this.drawPoints(e);
		}

		private void drawPoints(MouseEvent e) {
			int x = e.getX() / dx;
			int y = e.getY() / dy;
			if (SwingUtilities.isLeftMouseButton(e)) {
				if (x < nCellsX && y < nCellsY && !model.isAlive(x, y)) {
					model.changeState(x, y);
					this.repaint();
				}
			} else if (SwingUtilities.isRightMouseButton(e)) {
				if (x < nCellsX && y < nCellsY && model.isAlive(x, y)) {
					model.changeState(x, y);
					this.repaint();
				}
			}
		}

		// Metodi di cui è necessario fare override per contratto dell'interfaccia, ma non necessari nel programma
		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseMoved(MouseEvent e) {}

	}
}

