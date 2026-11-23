import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;

public class ExpenseTrackerGUI extends JFrame{

    // private ExpenseManager manager;
    // private FileManager fileManager;

    // Expenses currently shown in the table, in row order.
    private ArrayList<Expense> visibleExpenses;

    private DefaultTableModel tableModel;
    private JTable expenseTable;

    private JTextField dateField;
    private JComboBox<String> categoryInput;
    private JTextField amountField;
    private JTextField descriptionField;

    private JComboBox<Integer> yearBox;
    private JComboBox<String> monthBox;
    private JComboBox<String> categoryFilterBox;
    private JComboBox<String> sortBox;

    private JLabel totalLabel;
    private JLabel budgetLabel;
    private JLabel remainingLabel;
    private JLabel dailyAverageLabel;
    private JLabel projectedLabel;
    private JLabel overallStatusLabel;
    private JProgressBar overallBar;
    private JPanel categoryPanel;

    private boolean updatingControls;


}