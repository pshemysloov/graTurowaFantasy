// java
package Scenes;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {
    public MainMenuPanel(AppWindow window,
                         Runnable onLogin,
                         Runnable onCreateAccount,
                         Runnable onOptions,
                         Runnable onAuthors,
                         Runnable onExit) {

        setLayout(new BorderLayout());

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        center.setBackground(new Color(30, 30, 40));

        JButton btnLogin = createButton("Zaloguj się");
        JButton btnCreateAccount = createButton("Stwórz konto");
        JButton btnOptions = createButton("Opcje");
        JButton btnAuthors = createButton("Autorzy");
        JButton btnExit = createButton("Wyjście");

        btnLogin.addActionListener(e -> { if (onLogin != null) onLogin.run(); });
        btnCreateAccount.addActionListener(e -> { if (onCreateAccount != null) onCreateAccount.run(); });
        btnOptions.addActionListener(e -> { if (onOptions != null) onOptions.run(); });
        btnAuthors.addActionListener(e -> { if (onAuthors != null) onAuthors.run(); });
        btnExit.addActionListener(e -> { if (onExit != null) onExit.run(); });

        center.add(Box.createVerticalGlue());
        center.add(btnLogin);
        center.add(Box.createVerticalStrut(12));
        center.add(btnCreateAccount);
        center.add(Box.createVerticalStrut(12));
        center.add(btnOptions);
        center.add(Box.createVerticalStrut(12));
        center.add(btnAuthors);
        center.add(Box.createVerticalStrut(12));
        center.add(btnExit);
        center.add(Box.createVerticalGlue());

        add(center, BorderLayout.CENTER);


        TopBar tb = new TopBar(window,"");
        add(tb, BorderLayout.NORTH);
        tb.updateBackEnabled();
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(300, 48));
        btn.setPreferredSize(new Dimension(300, 48));
        btn.setFont(new Font("Dialog", Font.PLAIN, 16));
        return btn;
    }
}
