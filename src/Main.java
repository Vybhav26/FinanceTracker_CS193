import javax.swing.SwingUtilities;

public class Main{

    public static void main(String[] args){

        FileManager fileManager = new FileManager();
        ExpenseManager manager = new ExpenseManager();

        manager.setBudget(fileManager.loadBudget());

        for(Expense expense : fileManager.loadExpenses()){
            manager.addExpense(expense);
        }

        SwingUtilities.invokeLater(() -> {
            ExpenseTrackerGUI gui = new ExpenseTrackerGUI(manager, fileManager);
            gui.setVisible(true);
        });
    }
}