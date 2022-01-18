import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

public class AutoClicker extends javax.swing.JFrame {

	private final java.util.List<javax.swing.SwingWorker<Void, Void>> workers = new java.util.ArrayList<>();

	public AutoClicker() {
		initComponents();
	}

	private void initComponents() {
		startButton = new javax.swing.JButton();
		stopButton = new javax.swing.JButton();
		usageLabel = new javax.swing.JLabel();
		secondsLabel = new javax.swing.JLabel();
		millisecondsLabel = new javax.swing.JLabel();
		secondsTextField = new javax.swing.JTextField("0");
		millisecondsTextField = new javax.swing.JTextField("0");

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Auto Clicker [Paused]");

		startButton.setText("Start");
		startButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				startButtonActionPerformed(evt);
			}
		});

		stopButton.setText("Stop");
		stopButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				stopButtonActionPerformed(evt);
			}
		});

		usageLabel.setText("<html>Usage:<br>1. Set a speed<br>2. Hit start</html>");
		secondsLabel.setText("Seconds");
		millisecondsLabel.setText("Milliseconds");

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);

		layout.setHorizontalGroup(
			layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(layout.createSequentialGroup()
				.addContainerGap()
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(layout.createSequentialGroup()
						.addComponent(startButton)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(stopButton))
					.addGroup(layout.createSequentialGroup()
						.addComponent(usageLabel))
					.addGroup(layout.createSequentialGroup()
						.addComponent(secondsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(secondsLabel)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(millisecondsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(millisecondsLabel)))
				.addContainerGap(72, Short.MAX_VALUE))
		);

		layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {millisecondsTextField, secondsTextField});

		layout.setVerticalGroup(
			layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(layout.createSequentialGroup()
				.addContainerGap()
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
					.addComponent(startButton)
					.addComponent(stopButton))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
					.addComponent(usageLabel))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
					.addComponent(secondsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
					.addComponent(secondsLabel)
					.addComponent(millisecondsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
					.addComponent(millisecondsLabel))
				.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);

		pack();
	}

	private boolean isRunning = false;

	private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {
		if(isRunning == false) {
			start();
			isRunning = true;
			setTitle("Auto Clicker [Active]");
		}
	}

	private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {
		if(isRunning == true) {
			stop();
			isRunning = false;
			setTitle("Auto Clicker [Paused]");
		}
	}

	private void start() {
		javax.swing.SwingWorker<Void, Void> worker = new javax.swing.SwingWorker<Void, Void>() {
			protected Void doInBackground() throws java.lang.InterruptedException {
				try {
					java.util.concurrent.TimeUnit.SECONDS.sleep(1);
					while (true) {
						int seconds = Integer.parseInt(secondsTextField.getText());
						int milliseconds = Integer.parseInt(millisecondsTextField.getText());
						int timeToSleep = milliseconds + (seconds * 1000);

						Robot bot = new Robot();
						int leftMouseButton = InputEvent.BUTTON1_DOWN_MASK;
						bot.mousePress(leftMouseButton);
						Thread.sleep(100);
						bot.mouseRelease(leftMouseButton);
						Thread.sleep(timeToSleep);
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		};

		worker.execute();
		synchronized (workers) {
			workers.add(worker);
		}
	}

	private void stop() {
		synchronized (workers) {
			workers.forEach(worker -> worker.cancel(true));
			workers.clear();
		}
	}

	public static void main(String args[]) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new AutoClicker().setVisible(true);
			}
		});
	}

	private javax.swing.JButton startButton;
	private javax.swing.JButton stopButton;
	private javax.swing.JLabel usageLabel;
	private javax.swing.JLabel secondsLabel;
	private javax.swing.JLabel millisecondsLabel;
	private javax.swing.JTextField secondsTextField;
	private javax.swing.JTextField millisecondsTextField;
}
