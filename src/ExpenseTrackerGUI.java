import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ExpenseTrackerGUI extends JFrame{

    private DefaultTableModel tableModel;
    private JTable table;

    private JTextField dateField;
    private JTextField categoryField;
    private JTextField amountField;
    private JTextField descriptionField;

    private JLabel totalLabel;

    public ExpenseTrackerGUI(){
        setTitle("Expense Tracker");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createComponents();
    }

    private void createComponents(){
        setLayout(new BorderLayout(8, 8));

        // Top -- input fields
        JPanel inputPanel = new JPanel(new GridLayout(2, 4, 6, 4));

        dateField = new JTextField();
        categoryField = new JTextField();
        amountField = new JTextField();
        descriptionField = new JTextField();

        inputPanel.add(new JLabel("Date"));
        inputPanel.add(new JLabel("Category"));
        inputPanel.add(new JLabel("Amount"));
        inputPanel.add(new JLabel("Description"));

        inputPanel.add(dateField);
        inputPanel.add(categoryField);
        inputPanel.add(amountField);
        inputPanel.add(descriptionField);

        add(inputPanel, BorderLayout.NORTH);

        // Center -- main interface
        String[] columns = {"Date", "Category", "Amount", "Description"};

        tableModel = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Bottom
        JPanel bottomPanel = new JPanel(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Expense");
        JButton deleteButton = new JButton("Delete Selected");
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);

        totalLabel = new JLabel("Total: $0.00  ");

        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        bottomPanel.add(totalLabel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        // TODO: connect basic Expenses stuff
    }

    // Keep till Main.java code exists
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            ExpenseTrackerGUI gui = new ExpenseTrackerGUI();
            gui.setVisible(true);
        });
    }
}