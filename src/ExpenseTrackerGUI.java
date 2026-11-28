import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.time.format.DateTimeParseException;

public class ExpenseTrackerGUI extends JFrame {

    private ArrayList<Expense> shownExpenses;

    private ExpenseManager manager;
    private FileManager fileManager;

    // Table fields
    private DefaultTableModel tableModel;
    private JTable table;

    // Input fields
    private JTextField dateField;
    private JTextField categoryField;
    private JTextField amountField;
    private JTextField descriptionField;

    // Month fields
    private JComboBox<String> monthBox;
    private JComboBox<Integer> yearBox;

    // Summary fields
    private JLabel budgetLabel;
    private JLabel spentLabel;
    private JLabel leftLabel;
    private JLabel statusLabel;
    private JLabel projectedLabel;
    private JTextArea categoryArea;

    private static final String[] MONTH_NAMES = {
        "January", "February", "March", "April",
        "May", "June", "July", "August",
        "September", "October", "November", "December"
    };

    public ExpenseTrackerGUI(ExpenseManager manager, FileManager fileManager) {
        this.manager = manager;
        this.fileManager = fileManager;
        this.shownExpenses = new ArrayList<>();

        setTitle("Expense Tracker");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createComponents();
    }

    private void createComponents() {
        setLayout(new BorderLayout(8, 8));

        // Input fields
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

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

        // Month and year selector
        JPanel viewPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));

        monthBox = new JComboBox<>(MONTH_NAMES);
        yearBox = new JComboBox<>();

        LocalDate today = LocalDate.now();

        for (int year = today.getYear() - 5;
             year <= today.getYear() + 1; year++) {
            yearBox.addItem(year);
        }

        yearBox.setSelectedItem(today.getYear());
        monthBox.setSelectedIndex(today.getMonthValue() - 1);

        viewPanel.add(new JLabel("Showing month:"));
        viewPanel.add(monthBox);
        viewPanel.add(new JLabel("Year:"));
        viewPanel.add(yearBox);

        topPanel.add(inputPanel);
        topPanel.add(viewPanel);

        add(topPanel, BorderLayout.NORTH);

        // Expense table
        String[] columns = {
            "Date", "Category", "Amount", "Description"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();

        JButton addButton = new JButton("Add Expense");
        JButton deleteButton = new JButton("Delete Selected");
        JButton updateButton = new JButton("Update Selected");
        JButton budgetButton = new JButton("Set Monthly Budget");
        JButton categoryButton = new JButton("Set Category Limit");

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(budgetButton);
        buttonPanel.add(categoryButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Summary panel
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Summary"));
        summaryPanel.setPreferredSize(new Dimension(320, 100));

        budgetLabel = new JLabel("Monthly budget:");
        spentLabel = new JLabel("Spent:");
        leftLabel = new JLabel("Left to spend:");
        statusLabel = new JLabel("Status:");
        projectedLabel = new JLabel("Projected month-end:");

        summaryPanel.add(budgetLabel);
        summaryPanel.add(spentLabel);
        summaryPanel.add(leftLabel);
        summaryPanel.add(statusLabel);
        summaryPanel.add(Box.createVerticalStrut(10));
        summaryPanel.add(projectedLabel);
        summaryPanel.add(Box.createVerticalStrut(10));

        categoryArea = new JTextArea();
        categoryArea.setEditable(false);

        JScrollPane categoryScroll = new JScrollPane(categoryArea);
        categoryScroll.setBorder(
            BorderFactory.createTitledBorder("Category limits")
        );

        summaryPanel.add(categoryScroll);

        add(summaryPanel, BorderLayout.EAST);

        // Button actions
        addButton.addActionListener(e -> addExpense());
        deleteButton.addActionListener(e -> deleteExpense());
        updateButton.addActionListener(e -> updateExpense());
        budgetButton.addActionListener(e -> setMonthlyBudget());
        categoryButton.addActionListener(e -> setCategoryLimit());

        // Month selection actions
        monthBox.addActionListener(e -> refreshAll());
        yearBox.addActionListener(e -> refreshAll());

        // Table selection action
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedIntoFields();
            }
        });
    }

    // Temporary Placeholders for the sake of orgonization
    // Refactoring some of the old methods so it relies on ArrayLists, not Hashmaps

    private void addExpense() {
    }

    private void deleteExpense() {
    }

    private void updateExpense() {
    }

    private void setMonthlyBudget() {
    }

    private void setCategoryLimit() {
    }

    private void loadSelectedIntoFields() {
    }

    // Refresh table and summary
    private void refreshAll() {
        refreshTable();
    }

    // Display expenses for the selected month
    private void refreshTable() {
        // TODO: write the implementation so the below line is valid
        // shownExpenses = manager.getExpensesForMonth(getSelectedMonth());

        tableModel.setRowCount(0);

        for (Expense expense : shownExpenses) {
            Object[] row = {
                expense.getDate(),
                expense.getCategory(),
                String.format("$%.2f", expense.getAmount()),
                expense.getDescription()
            };

            tableModel.addRow(row);
        }
    }
}
    /*

    Legacy Code when summary panel, and other pannels (except the main one) weren't available


    private void addExpense(){
        LocalDate date = LocalDate.parse(dateField.getText().trim());
        String category = categoryField.getText();
        double amount = Double.parseDouble(amountField.getText().trim());
        String description = descriptionField.getText();

        Expense expense = new Expense(date, category, amount, description);
        manager.addExpense(expense);

        fileManager.saveExpenses(manager.getExpenses());
        
        clearFields();
        refreshTable();
    }

    private void deleteExpense(){
        int row = table.getSelectedRow();

        // The table rows are in the same order as the manager's list.
        Expense selected = manager.getExpenses().get(row);
        manager.removeExpense(selected);

        fileManager.saveExpenses(manager.getExpenses());
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
    */
