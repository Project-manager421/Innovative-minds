package app;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class UserInterface extends JFrame {

    private JComboBox<String> sourceCombo;
    private JComboBox<String> destinationCombo;

    private JTextArea shortestPathDisplay;
    private JLabel distanceDisplay;
    private JLabel travelTimeDisplay;

    private BufferedImage backgroundImage;

    public UserInterface() {
        setTitle("UG SMART-MAP");
        setSize(635, 760);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        loadBackgroundImage();

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);

                if (backgroundImage != null) {
                    Image image =
                            backgroundImage.getScaledInstance(
                                    getWidth(),
                                    getHeight(),
                                    Image.SCALE_SMOOTH
                            );

                    graphics.drawImage(
                            image,
                            0,
                            0,
                            this
                    );
                } else {
                    graphics.setColor(
                            new Color(90, 100, 150)
                    );

                    graphics.fillRect(
                            0,
                            0,
                            getWidth(),
                            getHeight()
                    );
                }
            }
        };

        panel.setLayout(null);
        setContentPane(panel);

        createHeader(panel);
        createLocationControls(panel);
        createPathDisplay(panel);
        createButtons(panel);

        setVisible(true);
    }

    private void loadBackgroundImage() {
        try {
            backgroundImage =
                    ImageIO.read(
                            new File("app/assets/ug.jpg")
                    );

            if (backgroundImage != null) {
                RescaleOp darkener =
                        new RescaleOp(
                                0.55f,
                                0,
                                null
                        );

                backgroundImage =
                        darkener.filter(
                                backgroundImage,
                                null
                        );
            }

        } catch (IOException exception) {
            System.out.println(
                    "Background image could not be loaded."
            );
        }
    }

    private void createHeader(JPanel panel) {
        JLabel title =
                new JLabel(
                        "UG SMART-MAP",
                        SwingConstants.LEFT
                );

        title.setBounds(
                28,
                15,
                350,
                35
        );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        title.setForeground(Color.WHITE);
        panel.add(title);

        JLabel menu =
                new JLabel(
                        "MENU",
                        SwingConstants.CENTER
                );

        menu.setBounds(
                520,
                15,
                80,
                35
        );

        menu.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        menu.setForeground(Color.WHITE);
        panel.add(menu);
    }

    private void createLocationControls(
            JPanel panel) {

        JLabel sourceLabel =
                createLabel(
                        "Your current location",
                        50,
                        65,
                        350,
                        30
                );

        panel.add(sourceLabel);

        sourceCombo =
                new JComboBox<>(
                        Main.getLocationNames()
                );

        styleComboBox(sourceCombo);

        sourceCombo.setBounds(
                50,
                100,
                535,
                40
        );

        panel.add(sourceCombo);

        JLabel destinationLabel =
                createLabel(
                        "Select destination",
                        50,
                        155,
                        350,
                        30
                );

        panel.add(destinationLabel);

        destinationCombo =
                new JComboBox<>(
                        Main.getLocationNames()
                );

        styleComboBox(destinationCombo);

        destinationCombo.setBounds(
                50,
                190,
                535,
                40
        );

        panel.add(destinationCombo);
    }

    private void createPathDisplay(
            JPanel panel) {

        JLabel pathLabel =
                createLabel(
                        "Shortest Path",
                        50,
                        245,
                        350,
                        30
                );

        panel.add(pathLabel);

        JPanel pathPanel =
                new JPanel();

        pathPanel.setLayout(null);

        pathPanel.setBounds(
                50,
                280,
                535,
                180
        );

        pathPanel.setBackground(
                new Color(
                        230,
                        230,
                        230,
                        225
                )
        );

        pathPanel.setBorder(
                BorderFactory.createLineBorder(
                        new Color(242, 72, 34),
                        3
                )
        );

        shortestPathDisplay =
                new JTextArea();

        shortestPathDisplay.setEditable(false);
        shortestPathDisplay.setLineWrap(true);
        shortestPathDisplay.setWrapStyleWord(true);

        shortestPathDisplay.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        shortestPathDisplay.setBackground(
                new Color(
                        245,
                        245,
                        245
                )
        );

        JScrollPane scrollPane =
                new JScrollPane(
                        shortestPathDisplay
                );

        scrollPane.setBounds(
                15,
                15,
                505,
                105
        );

        pathPanel.add(scrollPane);

        distanceDisplay =
                new JLabel(
                        "Distance: --"
                );

        distanceDisplay.setBounds(
                15,
                130,
                250,
                25
        );

        distanceDisplay.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        distanceDisplay.setForeground(Color.BLACK);
        pathPanel.add(distanceDisplay);

        travelTimeDisplay =
                new JLabel(
                        "Travel time: --"
                );

        travelTimeDisplay.setBounds(
                275,
                130,
                240,
                25
        );

        travelTimeDisplay.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        travelTimeDisplay.setForeground(Color.BLACK);
        pathPanel.add(travelTimeDisplay);

        panel.add(pathPanel);
    }

    private void createButtons(
            JPanel panel) {

        JButton routeButton =
                new JButton(
                        "Get Possible Paths"
                );

        styleButton(routeButton);

        routeButton.setBounds(
                110,
                500,
                415,
                50
        );

        routeButton.addActionListener(
                this::getPaths
        );

        panel.add(routeButton);

        JButton landmarkButton =
                new JButton(
                        "Landmarks"
                );

        styleButton(landmarkButton);

        landmarkButton.setBounds(
                110,
                575,
                415,
                50
        );

        landmarkButton.addActionListener(
                this::openLandmarkPage
        );

        panel.add(landmarkButton);
    }

    private JLabel createLabel(
            String text,
            int x,
            int y,
            int width,
            int height) {

        JLabel label =
                new JLabel(text);

        label.setBounds(
                x,
                y,
                width,
                height
        );

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        label.setForeground(Color.WHITE);

        return label;
    }

    private void styleComboBox(
            JComboBox<String> comboBox) {

        comboBox.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        comboBox.setBackground(
                new Color(
                        230,
                        230,
                        230
                )
        );

        comboBox.setForeground(Color.BLACK);
        comboBox.setFocusable(false);
    }

    private void styleButton(
            JButton button) {

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );

        button.setBackground(
                new Color(
                        97,
                        73,
                        53
                )
        );

        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createLineBorder(
                        new Color(242, 72, 34),
                        2
                )
        );
    }

    private void getPaths(
            ActionEvent event) {

        Object sourceObject =
                sourceCombo.getSelectedItem();

        Object destinationObject =
                destinationCombo.getSelectedItem();

        if (sourceObject == null
                || destinationObject == null) {

            shortestPathDisplay.setText(
                    "Please select a source and destination."
            );

            return;
        }

        String sourceName =
                sourceObject.toString();

        String destinationName =
                destinationObject.toString();

        Locations source =
                Main.graph.getNodeByName(
                        sourceName
                );

        Locations destination =
                Main.graph.getNodeByName(
                        destinationName
                );

        if (source == null
                || destination == null) {

            shortestPathDisplay.setText(
                    "Selected location was not found."
            );

            return;
        }

        if (source.equals(destination)) {
            shortestPathDisplay.setText(
                    "Source and destination are the same."
            );

            distanceDisplay.setText(
                    "Distance: 0 km"
            );

            travelTimeDisplay.setText(
                    "Travel time: 0 min"
            );

            return;
        }

        Dijkstra.findShortestPath(
                Main.graph,
                source,
                destination
        );

        String path =
                Dijkstra.getShortestPath(
                        source,
                        destination
                );

        float distance =
                Dijkstra.getTotalDistance(
                        destination
                );

        long travelTime =
                Dijkstra.getTotalTravelTime(
                        destination
                );

        shortestPathDisplay.setText(path);

        if (distance < 0) {
            distanceDisplay.setText(
                    "Distance: unavailable"
            );
        } else {
            distanceDisplay.setText(
                    "Distance: "
                            + distance
                            + " km"
            );
        }

        if (travelTime < 0) {
            travelTimeDisplay.setText(
                    "Travel time: unavailable"
            );
        } else {
            travelTimeDisplay.setText(
                    "Travel time: "
                            + travelTime
                            + " min"
            );
        }
    }

    private void openLandmarkPage(
            ActionEvent event) {

        dispose();
        new LandMarkPage();
    }

    public static void main(String[] args) {
        Main.buildGraph();

        SwingUtilities.invokeLater(
                () -> new UserInterface()
        );
    }
}