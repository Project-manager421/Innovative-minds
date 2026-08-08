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
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class LandMarkPage extends JFrame {

    private JComboBox<String> sourceCombo;
    private JComboBox<String> landmarkCombo;
    private JComboBox<String> destinationCombo;

    private JTextArea initialPathArea;
    private JTextArea finalPathArea;

    private JLabel distanceDisplay;
    private JLabel travelTimeDisplay;

    private BufferedImage backgroundImage;

    private final Color purpleBackground =
            new Color(109, 111, 169);

    private final Color darkButton =
            new Color(84, 74, 74);

    private final Color lightBox =
            new Color(217, 217, 217, 190);

    public LandMarkPage() {
        setTitle("UG SMART-MAP - LANDMARKS");
        setSize(635, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        loadBackgroundImage();

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);

                if (backgroundImage != null) {
                    Image scaledImage =
                            backgroundImage.getScaledInstance(
                                    getWidth(),
                                    getHeight(),
                                    Image.SCALE_SMOOTH
                            );

                    graphics.drawImage(
                            scaledImage,
                            0,
                            0,
                            this
                    );
                } else {
                    graphics.setColor(purpleBackground);

                    graphics.fillRect(
                            0,
                            0,
                            getWidth(),
                            getHeight()
                    );
                }
            }
        };

        mainPanel.setLayout(null);
        setContentPane(mainPanel);

        createHeader(mainPanel);
        createBackButton(mainPanel);
        createLocationControls(mainPanel);
        createPathDisplays(mainPanel);
        createRouteButton(mainPanel);

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
                        "LANDMARKS",
                        SwingConstants.CENTER
                );

        title.setBounds(
                205,
                25,
                250,
                35
        );

        title.setFont(
                new Font(
                        "Inter",
                        Font.BOLD,
                        24
                )
        );

        title.setForeground(Color.WHITE);
        panel.add(title);
    }

    private void createBackButton(JPanel panel) {
        JButton backButton =
                new JButton("←  Back");

        backButton.setBounds(
                35,
                30,
                145,
                48
        );

        backButton.setFont(
                new Font(
                        "Inter",
                        Font.BOLD,
                        18
                )
        );

        backButton.setBackground(darkButton);
        backButton.setForeground(Color.BLACK);
        backButton.setFocusPainted(false);

        backButton.setBorder(
                BorderFactory.createLineBorder(
                        Color.BLACK,
                        1
                )
        );

        backButton.addActionListener(
                this::returnToMainPage
        );

        panel.add(backButton);
    }

    private void createLocationControls(JPanel panel) {
        JLabel currentLocationLabel =
                createLabel(
                        "Current location",
                        35,
                        100,
                        180,
                        25,
                        Color.BLACK,
                        18,
                        Font.BOLD
                );

        panel.add(currentLocationLabel);

        sourceCombo =
                new JComboBox<>(
                        new String[]{
                                "Select your current location"
                        }
                );

        sourceCombo.setBounds(
                35,
                128,
                565,
                48
        );

        styleComboBox(sourceCombo);
        panel.add(sourceCombo);

        JLabel landmarkLabel =
                createLabel(
                        "Select landmark",
                        35,
                        195,
                        220,
                        25,
                        Color.BLACK,
                        18,
                        Font.BOLD
                );

        panel.add(landmarkLabel);

        landmarkCombo =
                new JComboBox<>(
                        new String[]{
                                "Select a landmark"
                        }
                );

        landmarkCombo.setBounds(
                35,
                223,
                565,
                48
        );

        styleComboBox(landmarkCombo);
        panel.add(landmarkCombo);

        JLabel destinationLabel =
                createLabel(
                        "Select destination",
                        35,
                        290,
                        240,
                        25,
                        Color.BLACK,
                        18,
                        Font.BOLD
                );

        panel.add(destinationLabel);

        destinationCombo =
                new JComboBox<>(
                        new String[]{
                                "Select your destination"
                        }
                );

        destinationCombo.setBounds(
                35,
                318,
                565,
                48
        );

        styleComboBox(destinationCombo);
        panel.add(destinationCombo);

        loadLocations();

        sourceCombo.addActionListener(
                event -> clearRouteDisplay()
        );

        landmarkCombo.addActionListener(
                event -> clearRouteDisplay()
        );

        destinationCombo.addActionListener(
                event -> clearRouteDisplay()
        );
    }

    private void createPathDisplays(JPanel panel) {
        JLabel initialPathLabel =
                createLabel(
                        "Initial Path",
                        35,
                        390,
                        200,
                        28,
                        Color.BLACK,
                        20,
                        Font.BOLD
                );

        panel.add(initialPathLabel);

        initialPathArea =
                createTextArea(
                        35,
                        420,
                        565,
                        95
                );

        panel.add(
                new JScrollPane(initialPathArea)
        );

        JLabel finalPathLabel =
                createLabel(
                        "Final Path",
                        35,
                        535,
                        200,
                        28,
                        Color.BLACK,
                        20,
                        Font.BOLD
                );

        panel.add(finalPathLabel);

        finalPathArea =
                createTextArea(
                        35,
                        565,
                        565,
                        95
                );

        panel.add(
                new JScrollPane(finalPathArea)
        );

        distanceDisplay =
                createLabel(
                        "Distance: --",
                        35,
                        675,
                        250,
                        25,
                        Color.BLACK,
                        16,
                        Font.BOLD
                );

        panel.add(distanceDisplay);

        travelTimeDisplay =
                createLabel(
                        "Travel time: --",
                        300,
                        675,
                        280,
                        25,
                        Color.BLACK,
                        16,
                        Font.BOLD
                );

        panel.add(travelTimeDisplay);
    }

    private void createRouteButton(JPanel panel) {
        JButton routeButton =
                new JButton("Find Route");

        routeButton.setBounds(
                205,
                710,
                225,
                45
        );

        routeButton.setFont(
                new Font(
                        "Inter",
                        Font.BOLD,
                        18
                )
        );

        routeButton.setBackground(darkButton);
        routeButton.setForeground(Color.WHITE);
        routeButton.setFocusPainted(false);

        routeButton.setBorder(
                BorderFactory.createLineBorder(
                        Color.BLACK,
                        1
                )
        );

        routeButton.addActionListener(
                event -> getPaths()
        );

        panel.add(routeButton);
    }

    private void loadLocations() {
        String[] locations =
                Main.getLocationNames();

        if (locations == null
                || locations.length == 0) {
            return;
        }

        sourceCombo.setModel(
                new DefaultComboBoxModel<>(
                        locations
                )
        );

        landmarkCombo.setModel(
                new DefaultComboBoxModel<>(
                        locations
                )
        );

        destinationCombo.setModel(
                new DefaultComboBoxModel<>(
                        locations
                ));

        sourceCombo.setSelectedIndex(-1);
        landmarkCombo.setSelectedIndex(-1);
        destinationCombo.setSelectedIndex(-1);
    }

    private void styleComboBox(
            JComboBox<String> comboBox
    ) {
        comboBox.setFont(
                new Font(
                        "Inter",
                        Font.PLAIN,
                        16
                )
        );

        comboBox.setBackground(lightBox);
        comboBox.setForeground(Color.BLACK);
        comboBox.setOpaque(true);

        comboBox.setBorder(
                BorderFactory.createLineBorder(
                        new Color(0, 0, 0, 100),
                        1
                )
        );
    }

    private JTextArea createTextArea(
            int x,
            int y,
            int width,
            int height
    ) {
        JTextArea textArea =
                new JTextArea();

        textArea.setBounds(
                x,
                y,
                width,
                height
        );

        textArea.setFont(
                new Font(
                        "Inter",
                        Font.PLAIN,
                        14
                )
        );

        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setBackground(
                new Color(217, 217, 217, 220)
        );

        textArea.setForeground(Color.BLACK);

        textArea.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                Color.BLACK,
                                1
                        ),
                        BorderFactory.createEmptyBorder(
                                8,
                                8,
                                8,
                                8
                        )
                )
        );

        return textArea;
    }

    private JLabel createLabel(
            String text,
            int x,
            int y,
            int width,
            int height,
            Color color,
            int fontSize,
            int fontStyle
    ) {
        JLabel label =
                new JLabel(text);

        label.setBounds(
                x,
                y,
                width,
                height
        );

        label.setForeground(color);

        label.setFont(
                new Font(
                        "Inter",
                        fontStyle,
                        fontSize
                )
        );

        return label;
    }

    private void clearRouteDisplay() {
        if (initialPathArea != null) {
            initialPathArea.setText("");
        }

        if (finalPathArea != null) {
            finalPathArea.setText("");
        }

        if (distanceDisplay != null) {
            distanceDisplay.setText(
                    "Distance: --"
            );
        }

        if (travelTimeDisplay != null) {
            travelTimeDisplay.setText(
                    "Travel time: --"
            );
        }
    }
private void getPaths() {
    String source =
            getSelectedValue(sourceCombo);

    String landmark =
            getSelectedValue(landmarkCombo);

    String destination =
            getSelectedValue(destinationCombo);

    if (source == null
            || landmark == null
            || destination == null) {

        initialPathArea.setText(
                "Please select your current location, "
                        + "landmark, and destination."
        );

        finalPathArea.setText("");

        distanceDisplay.setText(
                "Distance: --"
        );

        travelTimeDisplay.setText(
                "Travel time: --"
        );

        return;
    }

    Main.RouteResult initialRoute =
            Main.calculateRoute(
                    source,
                    landmark
            );

    Main.RouteResult finalRoute =
            Main.calculateRoute(
                    landmark,
                    destination
            );

    initialPathArea.setText(
            initialRoute.getPathText()
    );

    finalPathArea.setText(
            finalRoute.getPathText()
    );

    double totalDistance =
            initialRoute.getDistanceKm()
                    + finalRoute.getDistanceKm();

    int totalTravelTime =
            initialRoute.getTravelTimeMin()
                    + finalRoute.getTravelTimeMin();

    distanceDisplay.setText(
            String.format(
                    "Distance: %.2f km",
                    totalDistance
            )
    );

    travelTimeDisplay.setText(
            "Travel time: "
                    + totalTravelTime
                    + " minutes"
    );
}
    
    private String getSelectedValue(
            JComboBox<String> comboBox
    ) {
        Object selectedItem =
                comboBox.getSelectedItem();

        if (selectedItem == null) {
            return null;
        }

        String value =
                selectedItem.toString();

        if (value.trim().isEmpty()
                || value.startsWith("Select")) {
            return null;
        }

        return value;
    }

    private void returnToMainPage(
            ActionEvent event
    ) {
        dispose();

        new UserInterface();
    }
}