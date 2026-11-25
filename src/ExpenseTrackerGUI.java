import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;

public class ExpenseTrackerGUI extends JFrame{

    private ExpenseManager manager;

    private DefaultTableModel tableModel;
    private JTable table;

    private JTextField dateField;
    private JTextField categoryField;
    private JTextField amountField;
    private JTextField descriptionField;

    private JLabel totalLabel;

    public ExpenseTrackerGUI(ExpenseManager manager){
        this.manager = manager;

        setTitle("Expense Tracker");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createComponents();
        refreshTable();
    }

    private void createComponents(){
        setLayout(new BorderLayout(8, 8));

        // Top -- input fields
        JPanel inputPanel = new JPanel(new GridLayout(2, 4, 6, 4));

        dateField = new JTextField(LocalDate.now().toString());
        categoryField = new JTextField();
        amountField = new JTextField();
        descriptionField = new JTextField();

        inputPanel.add(new JLabel("Date (YYYY-MM-DD)"));
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
        JButton clearButton = new JButton("Clear Fields");
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        totalLabel = new JLabel();

        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        bottomPanel.add(totalLabel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        // Connect the buttons
        addButton.addActionListener(e -> addExpense());
        deleteButton.addActionListener(e -> deleteExpense());
        clearButton.addActionListener(e -> clearFields());
    }

    private void addExpense(){
        LocalDate date = LocalDate.parse(dateField.getText().trim());
        String category = categoryField.getText();
        double amount = Double.parseDouble(amountField.getText().trim());
        String description = descriptionField.getText();

        Expense expense = new Expense(date, category, amount, description);
        manager.addExpense(expense);

        // TODO: save with FileManager once it exists.

        clearFields();
        refreshTable();
    }

    private void deleteExpense(){
        int row = table.getSelectedRow();

        // The table rows are in the same order as the manager's list.
        Expense selected = manager.getExpenses().get(row);
        manager.removeExpense(selected);

        // TODO: save with FileManager once it exists.

        refreshTable();
    }

    private void clearFields(){
        dateField.setText(LocalDate.now().toString());
        categoryField.setText("");
        amountField.setText("");
        descriptionField.setText("");
        table.clearSelection();
    }

    private void refreshTable(){
        tableModel.setRowCount(0);

        for(Expense expense : manager.getExpenses()){
            Object[] row = {
                expense.getDate(),
                expense.getCategory(),
                String.format("$%.2f", expense.getAmount()),
                expense.getDescription()
            };
            tableModel.addRow(row);
        }

        // TODO: replace with a month selector once filtering exists.
        YearMonth thisMonth = YearMonth.now();
        double spent = manager.getTotalSpent(thisMonth);
        double remaining = manager.getRemainingBudget(thisMonth);

        totalLabel.setText(String.format("This month: $%.2f spent, $%.2f left  ", spent, remaining));
    }
}