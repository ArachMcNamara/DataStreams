import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataStreamsGUI extends JFrame {

    private JTextArea originalArea;
    private JTextArea filteredArea;
    private JTextField searchField;

    private Path currentFile;

    public DataStreamsGUI() {

        setTitle("Java Data Streams");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        buildGUI();

        setVisible(true);
    }

    private void buildGUI() {

        JPanel topPanel = new JPanel();

        JLabel lblSearch = new JLabel("Search String:");

        searchField = new JTextField(20);

        JButton btnSearch = new JButton("Search");

        topPanel.add(lblSearch);
        topPanel.add(searchField);
        topPanel.add(btnSearch);

        originalArea = new JTextArea();
        filteredArea = new JTextArea();

        JScrollPane leftPane = new JScrollPane(originalArea);
        JScrollPane rightPane = new JScrollPane(filteredArea);

        JSplitPane splitPane =
                new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                        leftPane,
                        rightPane);

        splitPane.setDividerLocation(430);

        JButton btnLoad = new JButton("Load File");
        JButton btnQuit = new JButton("Quit");

        JPanel bottomPanel = new JPanel();

        bottomPanel.add(btnLoad);
        bottomPanel.add(btnQuit);

        add(topPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        btnLoad.addActionListener(e -> loadFile());

        btnSearch.addActionListener(e -> searchFile());

        btnQuit.addActionListener(e -> System.exit(0));
    }

    private void loadFile() {

        JFileChooser chooser = new JFileChooser();

        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {

            currentFile = chooser.getSelectedFile().toPath();

            try {
                List<String> lines = Files.readAllLines(currentFile);

                originalArea.setText("");

                for (String line : lines) {
                    originalArea.append(line + "\n");
                }

                filteredArea.setText("");

            }
            catch (IOException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Error reading file.",
                        "File Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void searchFile() {

        if (currentFile == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please load a file first.");

            return;
        }

        String searchText = searchField.getText();

        try (Stream<String> lines = Files.lines(currentFile)) {

            List<String> matches =
                    lines.filter(line ->
                                    line.toLowerCase()
                                            .contains(searchText.toLowerCase()))
                            .collect(Collectors.toList());

            filteredArea.setText("");

            for (String line : matches) {
                filteredArea.append(line + "\n");
            }
        }
        catch (IOException ex) {


            JOptionPane.showMessageDialog(
                    this,
                    "Search failed.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(DataStreamsGUI::new);
    }
}