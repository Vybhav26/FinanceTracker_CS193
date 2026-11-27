import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;

public class ExpenseTrackerGUI extends JFrame {

    // Shown expenses on the table, in row order
    // copy, of sorts
    private ArrayList<Expense> shownExpenses;

    
    private ExpenseManager manager;
    private FileManager fileManager; 

    // Table fields
    private DefaultTableModel tableModel;
    private JTable table;

    // Input Fields
    private JTextField dateField;
    private JTextField categoryField;
    private JTextField amountField;
    private JTextField descriptionField;

    private JLabel totalLabel;

    // Month GUI fields
    private JComboBox<String> monthBox;
    private JComboBox<Integer> yearBox;

    // Summary Fields
    private JLabel budgetLabel;
    private JLabel spentLabel;
    private JLabel leftLabel;
    private JLabel statusLabel;
    private JLabel averageLabel;
    private JLabel projectedLabel;
    private JTextArea categoryArea;

    private static final String[] MONTH_NAMES = {
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    };


    public ExpenseTrackerGUI(ExpenseManager manager, FileManager fileManager){
        this.manager = manager;
        this.fileManager = fileManager;
        this.shownExpenses = new ArrayList<>();

        setTitle("Expense Tracker");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createComponents();
        refreshTable();
        //refreshAll();
    }

    private void createComponents(){
        setLayout(new BorderLayout(8, 8));

        // Top -- input fields
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

        // Input Fields + Month Selector via combo box

        JPanel viewPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));

        monthBox = new JComboBox<>(MONTH_NAMES);
        yearBox = new JComboBox<>();

        LocalDate today = LocalDate.now();

        for(int year = today.getYear() - 5; year <= today.getYear() + 1; year++){
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


        // Right -- Summary Panel + Budget Limits

        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Summary"));
        summaryPanel.setPreferredSize(new Dimension(320, 100));

        budgetLabel = new JLabel();
        spentLabel = new JLabel();
        leftLabel = new JLabel();
        statusLabel = new JLabel();
        averageLabel = new JLabel();
        projectedLabel = new JLabel();

        summaryPanel.add(budgetLabel);
        summaryPanel.add(spentLabel);
        summaryPanel.add(leftLabel);
        summaryPanel.add(statusLabel);
        summaryPanel.add(Box.createVerticalStrut(10));
        summaryPanel.add(averageLabel);
        summaryPanel.add(projectedLabel);
        summaryPanel.add(Box.createVerticalStrut(10));

        categoryArea = new JTextArea();
        categoryArea.setEditable(false);

        JScrollPane categoryScroll = new JScrollPane(categoryArea);
        categoryScroll.setBorder(BorderFactory.createTitledBorder("Category limits"));
        summaryPanel.add(categoryScroll);

        add(summaryPanel, BorderLayout.EAST);




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
}