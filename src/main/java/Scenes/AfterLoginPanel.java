package Scenes;

import TCPServer.Packets.LoginInfoResponse;

import javax.swing.*;
import java.awt.*;

public class AfterLoginPanel extends JPanel {
    private final AppWindow window;
    private final JButton btnWalkaKomputer = new JButton("Walka z komputerem");
    private final JButton btnWalkaGracz = new JButton("Walka z graczem");
    private final JButton btnEkwipunek = new JButton("Ekwipunek");
    private final JButton btnWyjscie = new JButton("Wyjście");

    private Runnable onWalkaKomputerClicked;
    private Runnable onWalkaGraczClicked;
    private Runnable onEkwipunekClicked;
    private Runnable onWyjscieClicked;


    public AfterLoginPanel(AppWindow window, Runnable onWalkaKomputerClicked, Runnable onWalkaGraczClicked, Runnable onEkwipunekClicked, Runnable onWyjscieClicked) {
        this.window = window;

        this.onWalkaKomputerClicked = onWalkaKomputerClicked;
        this.onWalkaGraczClicked = onWalkaGraczClicked;
        this.onEkwipunekClicked = onEkwipunekClicked;
        this.onWyjscieClicked = onWyjscieClicked;


        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        add(Box.createVerticalGlue());

        btnWalkaKomputer.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnWalkaKomputer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnWalkaKomputer.addActionListener(e -> handleWalkaKomputer());
        add(btnWalkaKomputer);
        add(Box.createRigidArea(new Dimension(0, 8)));

        btnWalkaGracz.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnWalkaGracz.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnWalkaGracz.addActionListener(e -> handleWalkaGracz());
        add(btnWalkaGracz);
        add(Box.createRigidArea(new Dimension(0, 8)));

        btnEkwipunek.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnEkwipunek.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnEkwipunek.addActionListener(e -> handleEkwipunek());
        add(btnEkwipunek);
        add(Box.createRigidArea(new Dimension(0, 8)));

        btnWyjscie.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnWyjscie.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnWyjscie.addActionListener(e -> handleWyjscie());
        add(btnWyjscie);

        add(Box.createVerticalGlue());
    }

    private void handleWyjscie() {

        onWyjscieClicked.run();
    }

    private void handleEkwipunek() {

        onEkwipunekClicked.run();
    }

    private void handleWalkaGracz() {

        onWalkaGraczClicked.run();
    }

    private void handleWalkaKomputer() {

        onWalkaKomputerClicked.run();
    }


}
