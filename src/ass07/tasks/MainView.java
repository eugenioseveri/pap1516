package ass07.tasks;

import ass07.tasks.Controller;
import ass07.tasks.PlayerModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainView extends JFrame implements ActionListener {

	private static final long serialVersionUID = -8832244589219575813L;
    private Controller controller;
    private final JButton startButton = new JButton("Start");
    private final JButton stopButton = new JButton("Stop");
    private final JButton resetButton = new JButton("Reset");
    private final JTextField turnTextField = new JTextField(4);
    private final JTextField turnTimeTextField = new JTextField(6);
    private final JTextField magicNumberTextField = new JTextField(8);
    private final JTextArea textArea = new JTextArea(40,50);
    private final JPanel controlPanel = new JPanel();
    private final JPanel infoPanel = new JPanel();

    public MainView (Dimension windowSize){
        this.setSize(windowSize.width,windowSize.height);
        this.setTitle("Guess the Number");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.startButton.addActionListener(this);
        this.stopButton.addActionListener(this);
        this.resetButton.addActionListener(this);
        this.stopButton.setEnabled(false);
        this.setLayout(new BorderLayout());
        controlPanel.add(this.startButton);
        controlPanel.add(this.stopButton);
        controlPanel.add(this.resetButton);
        this.turnTextField.setEditable(false);
        this.turnTimeTextField.setEditable(false);
        this.magicNumberTextField.setEditable(false);
        infoPanel.add(new JLabel("Numero da individuare:"));
        infoPanel.add(this.magicNumberTextField);
        infoPanel.add(new JLabel("Iterazione:"));
        infoPanel.add(this.turnTextField);
        infoPanel.add(new JLabel("Tempo:"));
        infoPanel.add(this.turnTimeTextField);
        this.textArea.setEditable(false);
        this.add(controlPanel, BorderLayout.NORTH);
        this.add(new JScrollPane(this.textArea), BorderLayout.CENTER);
        this.add(infoPanel, BorderLayout.SOUTH);
    }

    public void setListener(Controller controller){
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(this.startButton)){
            this.stopButton.setEnabled(true);
            this.resetButton.setEnabled(false);
            this.startButton.setEnabled(false);
            this.controller.started();
        }
        if(e.getSource().equals(this.stopButton)){
            this.stopButton.setEnabled(false);
            this.resetButton.setEnabled(true);
            this.startButton.setEnabled(true);
            this.controller.stopped();
        }
        if(e.getSource().equals(this.resetButton)){
            this.turnTextField.setText("");
            this.turnTimeTextField.setText("");
            this.magicNumberTextField.setText("");
            this.textArea.setText("");
            this.controller.reset();
            this.startButton.setEnabled(true);
        }
    }

    public void updateView(List<PlayerModel> players, int turn, long time){
        SwingUtilities.invokeLater(() -> {
                this.textArea.append("Iterazione " + turn + "\n");
                players.stream().forEach(p -> textArea.append(p.log() + "\n"));
                this.turnTextField.setText(Long.toString(turn));
                this.turnTimeTextField.setText(Long.toString(time));
            }
        );
    }

    public void updateWinner(int winner, int playersNumber){
        SwingUtilities.invokeLater(() -> {
                this.textArea.append("\n");
                for(int i=0; i<playersNumber; i++){
                    if(i==winner){
                        this.textArea.append("player-"+ winner +": won!\n");
                    } else {
                        this.textArea.append("player-"+ i +": sob!\n");
                    }
                }
                this.stopButton.setEnabled(false);
                this.resetButton.setEnabled(true);
                this.startButton.setEnabled(false);
            }
        );
    }

    public void setMagicNumber(int number){
        SwingUtilities.invokeLater(() ->
            this.magicNumberTextField.setText("" + number)
        );
    }
}
