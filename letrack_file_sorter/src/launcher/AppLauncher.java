package launcher;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import ui.StartContainerPage;

public class AppLauncher {

	static JFrame frame;
	
	private static void createAndShowWindow() {
		// Setup window params
		frame = new JFrame("Letrack File Sorter");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(500, 400));
		
		// Construct tab pane for parameter selection and landing page
		frame.add(new StartContainerPage(frame));
		
		// Show frame
		frame.pack();
		frame.setVisible(true);
	}

	
	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowWindow();
			}
		});
	}
}
