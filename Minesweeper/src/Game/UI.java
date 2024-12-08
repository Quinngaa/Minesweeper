package Game;

import java.awt.*;
import java.awt.event.MouseAdapter;
import javax.swing.*;
import java.awt.event.MouseEvent;

public class UI extends JFrame {
    private Minesweeper game;
    private JLabel mineCounterLabel;
    private JLabel timerLabel;
    private JPanel minesLeftPanel;
    private JPanel timerPanel;
    private JPanel buttonPanel;
    private JPanel gridPanel;
    private ImageIcon mineIcon;

    public UI(Minesweeper game, int rows, int cols, int mineCount) {
        this.game = game;
        initializeUI(rows, cols, mineCount);
        configureFrame();
    }

    // Method to initialize the UI components
    private void initializeUI(int rows, int cols, int mineCount) {
        setLayout(new BorderLayout());
        createMineCounterPanel(mineCount);
        createTimerPanel();
        createButtonPanel();
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
        undoButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    StateManager.getInstance().undo();
                    updateMineCounter();
                }
            }
        });
        buttonPanel.add(undoButton);
    }

    // Method to create and configure the grid panel
    private void createGridPanel(int rows, int cols) {
        gridPanel = new JPanel(new GridLayout(rows, cols));
        game.initButtons(gridPanel);
    }

    // Method to load the mine icon
    private void loadMineIcon() {
        mineIcon = new ImageIcon(getClass().getResource("/Image/mine.jpg"));
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
        setSize(600, 600);
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
}