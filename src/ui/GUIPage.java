package ui;

import javax.swing.JPanel;

public abstract class GUIPage extends JPanel{
	
	/**
	 * Generated serial id
	 */
	private static final long serialVersionUID = -6357057473324946129L;

	public GUIPage() {
		prepareGUI();
	}
	
	public abstract void prepareGUI();
}
