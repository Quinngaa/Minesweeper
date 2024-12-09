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
    private ImageIcon flatIcon;
    private int tileSize;

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
        mineCounterLabel.setFont(new Font("Arial", Font.BOLD, 15));
        mineCounterLabel.setForeground(Color.RED);
        minesLeftPanel.add(mineCounterLabel);
    }

    // Method to create and configure the timer panel
    private void createTimerPanel() {
        timerPanel = new JPanel();
        timerPanel.setBackground(Color.BLACK);
        timerLabel = new JLabel("Time: 0");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 15));
        timerLabel.setForeground(Color.RED);
        timerPanel.add(timerLabel);
    }

    // Method to create and configure the button panel
    private void createButtonPanel() {
        buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        JButton undoButton = new JButton("UNDO");
        undoButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                StateManager.getInstance().undoState();
                updateMineCounter();
            } 
          } );
        buttonPanel.add(undoButton);
    }

    public void disableUndoButton() {
        buttonPanel.getComponent(0).setEnabled(false);
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
        flatIcon = loadAndScaleIcon("/Image/flat.png");
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
        topPanel.add(minesLeftPanel);
        topPanel.add(buttonPanel);
        topPanel.add(timerPanel);

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

    public ImageIcon getOneIcon() {
        return oneIcon;
    }

    public ImageIcon getTwoIcon() {
        return twoIcon;
    }

    public ImageIcon getThreeIcon() {
        return threeIcon;
    }

    public ImageIcon getFourIcon() {
        return fourIcon;
    }

    public ImageIcon getFiveIcon() {
        return fiveIcon;
    }

    public ImageIcon getSixIcon() {
        return sixIcon;
    }

    public ImageIcon getSevenIcon() {
        return sevenIcon;
    }

    public ImageIcon getEightIcon() {
        return eightIcon;
    }

    public ImageIcon getFlatIcon() {
        return flatIcon;
    }
}