package com.ebp.owat.app.gui;

import com.ebp.owat.app.config.Globals;

import javax.swing.*;
import java.awt.*;

/**
 * Main class for the GUI.
 * <p>
 * Partially generated using Intellij.
 * Guides:
 * https://stackoverflow.com/questions/12775170/how-do-i-create-a-new-swing-app-in-intellij-idea-community-edition
 * https://stackoverflow.com/questions/3899525/how-to-use-gui-form-created-in-itellij-idea
 */
public class MainGuiApp {
	private JPanel panel1;
	private JLabel mainLabel;
	
	private static final String TITLE_FORMAT = "%s v%s %s";
	
	private static final String appTitle = String.format(
		TITLE_FORMAT,
		Globals.getProp(Globals.PropertyKey.APP_NAME_PROP_KEY),
		Globals.getProp(Globals.PropertyKey.APP_VERSION_PROP_KEY),
		Globals.getProp(Globals.PropertyKey.APP_VERSION_NAME_PROP_KEY)
	);
	
	public static void main(String[] args) {
		JFrame frame = new JFrame(appTitle);
		frame.setContentPane(new MainGuiApp().panel1);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	{
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
		$$$setupUI$$$();
	}
	
	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$() {
		panel1 = new JPanel();
		panel1.setLayout(new GridBagLayout());
		panel1.setMinimumSize(new Dimension(300, 200));
		panel1.setName("");
		panel1.setPreferredSize(new Dimension(300, 200));
		mainLabel = new JLabel();
		mainLabel.setText("Hello World");
		GridBagConstraints gbc;
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		panel1.add(mainLabel, gbc);
		final JPanel spacer1 = new JPanel();
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel1.add(spacer1, gbc);
		final JPanel spacer2 = new JPanel();
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.VERTICAL;
		panel1.add(spacer2, gbc);
	}
	
	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() {
		return panel1;
	}
}
