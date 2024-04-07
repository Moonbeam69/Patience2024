package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class OpeningPrompt extends JFrame {
        private JTextField textField;
        private JButton okButton;
        Player player;

        public OpeningPrompt(Player player) {

            this.player = player;

            // Set the title of the window
            setTitle("Simple Window");

            // Set the layout manager
            setLayout(new FlowLayout());

            // Initialize the text field and add it to the window
            textField = new JTextField(20);
            add(textField);

            // Initialize the OK button, add an action listener, and add it to the window
            okButton = new JButton("OK");
            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Action to be performed when the OK button is clicked
                    player.maxplays = Integer.valueOf(textField.getText());
                    setVisible(false);
                }
            });
            add(okButton);

            // Set the default close operation and window size, then make the window visible
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(300, 100);
            setVisible(true);
        }

}
