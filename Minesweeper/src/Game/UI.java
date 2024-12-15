package Game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.net.URL;

public class UI extends JFrame {
    private static UI instance;
    private Minesweeper game;
    private JLabel mineCounterLabel;
    private JLabel timerLabel;
    private JPanel minesLeftPanel;
    private JPanel timerPanel;
    private JPanel buttonPanel;
    private JPanel gridPanel;
    private ImageIcon mineIcon;
    private ImageIcon flagIcon;
    private ImageIcon emptyCellIcon;
    private ImageIcon oneIcon;
    private ImageIcon twoIcon;
    private ImageIcon threeIcon;
    private ImageIcon fourIcon;
    private ImageIcon fiveIcon;
    private ImageIcon sixIcon;
    private ImageIcon sevenIcon;
    private ImageIcon eightIcon;
    private int tileSize;

    private UI() {
    }

    public static UI getInstance() {
        if (instance == null) {
            instance = new UI();
        }
        return instance;
    }

    public void setVariables(Minesweeper game, int rows, int cols, int mineCount, int tileSize) {
        this.game = game;
        this.tileSize = tileSize;
        initializeUI(rows, cols, mineCount);
        configureFrame();
    }

    // Method to initialize the UI components
    private void initializeUI(int rows, int cols, int mineCount) {
        setLayout(new BorderLayout());
        createMineCounterPanel(mineCount);
        createTimerPanel();
        createButtonPanel();
        loadIcons();
        createGridPanel(rows, cols);

        // Add all panels to the frame
        addComponentsToFrame();
    }
    
    // Method to create and configure the mine counter panel
    private void createMineCounterPanel(int mineCount) {
        minesLeftPanel = new JPanel();
        minesLeftPanel.setBackground(Color.BLACK);
        mineCounterLabel = new JLabel("Mines Left: " + mineCount);
        mineCounterLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mineCounterLabel.setForeground(Color.RED);
        mineCounterLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        minesLeftPanel.add(mineCounterLabel);
    }

    // Method to create and configure the timer panel
    private void createTimerPanel() {
        timerPanel = new JPanel();
        timerPanel.setBackground(Color.BLACK);
        timerLabel = new JLabel("Time: 0");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        timerLabel.setForeground(Color.RED);
        timerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        timerPanel.add(timerLabel);
    }

    // Method to create and configure the button panel
    private void createButtonPanel() {
        buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);

        JButton undoButton = createUndoButton();
        JButton newGameButton = createNewGameButton();

        buttonPanel.add(undoButton);
        buttonPanel.add(newGameButton);
    }

    private JButton createUndoButton() {
        JButton undoButton = new JButton("UNDO");
        undoButton.setFont(new Font("Arial", Font.BOLD, 17));
        undoButton.setForeground(Color.RED); // Set text color to red
        undoButton.setBackground(Color.BLACK); // Set button background to black
        undoButton.setBorder(BorderFactory.createLineBorder(Color.RED)); // Set border color to red
        undoButton.setFocusPainted(false); // Disable focus painting

        // Adjust the size of the button to fit the text
        undoButton.setPreferredSize(
                new Dimension(undoButton.getPreferredSize().width + 10, undoButton.getPreferredSize().height + 5));
        undoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StateManager.getInstance().undo();
                updateMineCounter();
            }
        });

        return undoButton;
    }

    private JButton createNewGameButton() {
        JButton newGameButton = new JButton("NEW GAME");
        newGameButton.setFont(new Font("Arial", Font.BOLD, 17));
        newGameButton.setForeground(Color.RED); // Set text color to red
        newGameButton.setBackground(Color.BLACK); // Set button background to black
        newGameButton.setBorder(BorderFactory.createLineBorder(Color.RED)); // Set border color to red
        newGameButton.setFocusPainted(false); // Disable focus painting

        // Adjust the size of the button to fit the text
        newGameButton.setPreferredSize(
                new Dimension(newGameButton.getPreferredSize().width + 10,
                        newGameButton.getPreferredSize().height + 5));
        newGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.restartGame();
            }
        });
        return newGameButton;
    }

    public void showGameWinDialog() {
        JLabel messageLabel = new JLabel("Congratulations! You won!");
        messageLabel.setFont(new Font("Arial", Font.BOLD, 24));
        messageLabel.setForeground(Color.GREEN);
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        messageLabel.setOpaque(true); // Make the label opaque to show background color

        // Create a custom dialog
        JDialog dialog = new JDialog((Frame) null, "Game Won", true);
        dialog.setLayout(new BorderLayout());
        dialog.add(messageLabel, BorderLayout.CENTER);

        // Create a custom "OK" button
        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Arial", Font.BOLD, 16));
        okButton.setFocusPainted(false);
        okButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE); // Set background color to white
        buttonPanel.add(okButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setSize(350, 100);
        dialog.setLocationRelativeTo(null);
        dialog.setUndecorated(true); // Remove the window decorations
        dialog.getRootPane().setBorder(BorderFactory.createLineBorder(Color.GREEN, 2)); // Add a custom border
        dialog.setVisible(true);
    }

    // Method to show the "Game Over" dialog
    public void showGameOverDialog() {
        JLabel messageLabel = new JLabel("Game Over!");
        messageLabel.setFont(new Font("Arial", Font.BOLD, 24));
        messageLabel.setForeground(Color.RED);
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        messageLabel.setOpaque(true); // Make the label opaque to show background color

        // Create a custom dialog
        JDialog dialog = new JDialog((Frame) null, "Game Over", true);
        dialog.setLayout(new BorderLayout());
        dialog.add(messageLabel, BorderLayout.CENTER);

        // Create a custom "OK" button
        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Arial", Font.BOLD, 16));
        okButton.setFocusPainted(false);
        okButton.addActionListener(e -> dialog.dispose());


        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE); // Set background color to white
        buttonPanel.add(okButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setSize(220, 100);
        dialog.setLocationRelativeTo(null);
        dialog.setUndecorated(true); // Remove the window decorations
        dialog.getRootPane().setBorder(BorderFactory.createLineBorder(Color.RED, 2)); // Add a custom border
        dialog.setVisible(true);
    }

    public void setButtonNumbers(JButton button, int mines) {
        switch (mines) {
            case 1:
                button.setIcon(oneIcon);
                break;
            case 2:
                button.setIcon(twoIcon);
                break;
            case 3:
                button.setIcon(threeIcon);
                break;
            case 4:
                button.setIcon(fourIcon);
                break;
            case 5:
                button.setIcon(fiveIcon);
                break;
            case 6:
                button.setIcon(sixIcon);
                break;
            case 7:
                button.setIcon(sevenIcon);
                break;
            case 8:
                button.setIcon(eightIcon);
                break;
            default:
                break;
        }
    }

    public void disableUndoButton() {
        buttonPanel.getComponent(0).setEnabled(false);
        System.out.println("Undo button disabled");
    }

    public void enableUndoButton() {
        buttonPanel.getComponent(0).setEnabled(true);
        System.out.println("Undo button enabled");
    }
    
    // Method to create and configure the grid panel
    private void createGridPanel(int rows, int cols) {
        gridPanel = new JPanel(new GridLayout(rows, cols));
        game.initButtons(gridPanel);
    }

    // Method to load the icons
    private void loadIcons() {
        mineIcon = loadAndScaleIcon("/Image/bomb.png");
        flagIcon = loadAndScaleIcon("/Image/flag.png");
        emptyCellIcon = loadAndScaleIcon("/Image/empty.png");
        oneIcon = loadAndScaleIcon("/Image/one.png");
        twoIcon = loadAndScaleIcon("/Image/two.png");
        threeIcon = loadAndScaleIcon("/Image/three.png");
        fourIcon = loadAndScaleIcon("/Image/four.png");
        fiveIcon = loadAndScaleIcon("/Image/five.png");
        sixIcon = loadAndScaleIcon("/Image/six.png");
        sevenIcon = loadAndScaleIcon("/Image/seven.png");
        eightIcon = loadAndScaleIcon("/Image/eight.png");
    }

    private ImageIcon loadAndScaleIcon(String path) {
        URL resource = getClass().getResource(path);
        if (resource == null) {
            System.err.println("Error: Unable to load icon from path: " + path);
            return null;
        }
        ImageIcon originalIcon = new ImageIcon(getClass().getResource(path));
        Image scaledImage = originalIcon.getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    // Method to add all the components to the frame
    private void addComponentsToFrame() {
        JPanel topPanel = new JPanel(new GridLayout(1, 3));
        JPanel minesLeftContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        minesLeftContainer.setBackground(Color.BLACK);
        minesLeftContainer.add(minesLeftPanel);

        JPanel timerContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timerContainer.setBackground(Color.BLACK);
        timerContainer.add(timerPanel);

        topPanel.add(minesLeftContainer);
        topPanel.add(timerContainer);
        topPanel.add(buttonPanel);
        topPanel.setBackground(Color.BLACK);

        // Add the top panel and grid panel to the frame
        add(topPanel, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);
    }

    // Method to configure the frame settings
    private void configureFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Minesweeper");
        setSize(tileSize * game.getCols(), tileSize * game.getRows());
        setLocationRelativeTo(null);
        Point location = getLocation();
        setLocation(location.x - 27, location.y - 30);
        pack();
        setVisible(true);

        ImageIcon icon = new ImageIcon(getClass().getResource("/Image/mine.jpg"));
        setIconImage(icon.getImage());
    }

    public void updateMineCounter() {
        int flaggedMines = 0;
        for (int row = 0; row < game.getBoard().getRows(); row++) {
            for (int col = 0; col < game.getBoard().getCols(); col++) {
                if (game.getBoard().getCell(row, col).isFlagged()) {
                    flaggedMines++;
                }
            }
        }
        mineCounterLabel.setText("Mines Left: " + (game.getBoard().getMineCount() - flaggedMines));
    }

    public void updateTimer(int secondsPassed) {
        timerLabel.setText("Time: " + secondsPassed);
    }

    public void resetUI() {
        mineCounterLabel.setText("Mines Left: " + game.getBoard().getMineCount());
        timerLabel.setText("Time: 0");

        // Clear the grid panel
        gridPanel.removeAll();
        gridPanel.revalidate();
        gridPanel.repaint();

        game.initButtons(gridPanel);

        // refresh the UI
        revalidate();
        repaint();
    }    

    public JFrame getFrame() {
        return this;
    }

    public ImageIcon getMineIcon() {
        return mineIcon;
    }

    public ImageIcon getFlagIcon() {
        return flagIcon;
    }

    public ImageIcon getEmptyCellIcon() {
        return emptyCellIcon;
    }
}