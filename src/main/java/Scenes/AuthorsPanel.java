// java
package Scenes;

import javax.swing.*;
import java.awt.*;

public class AuthorsPanel extends JPanel {
    public AuthorsPanel(AppWindow window) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(30, 30, 40));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        TopBar tb = new TopBar(window,"");
        add(tb, BorderLayout.NORTH);

        JLabel title = new JLabel("Autorzy", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Serif", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);

        add(Box.createVerticalStrut(20));

        addCenteredField("Przemysław Błaszczyk");
        add(Box.createVerticalStrut(10));

        addCenteredField("Mateusz Biskup");
        add(Box.createVerticalStrut(10));


        add(Box.createVerticalGlue());
    }

    private void addCenteredField(String text) {
        JTextField tf = new JTextField(text);
        tf.setMaximumSize(new Dimension(420, 36));
        tf.setPreferredSize(new Dimension(420, 36));
        tf.setAlignmentX(Component.CENTER_ALIGNMENT);
        tf.setHorizontalAlignment(JTextField.CENTER);
        tf.setEditable(false);
        tf.setFont(new Font("Dialog", Font.PLAIN, 16));
        tf.setBackground(new Color(50, 50, 60));
        tf.setForeground(Color.WHITE);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 90)),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        add(tf);
    }
}
