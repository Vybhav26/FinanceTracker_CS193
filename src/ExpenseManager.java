import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;

public class ExpenseManager{

    private ArrayList<Expense> expenses;
    private Budget budget;

    public ExpenseManager(){
        expenses = new ArrayList<>();
        budget = new Budget(1000.00);
    }

    public void addExpense(Expense expense){
        if(expense == null){
            throw new IllegalArgumentException("Expense is required.");
        }
        expenses.add(expense);
    }

    public void removeExpense(Expense expense){
        expenses.remove(expense);
    }

    //builds a new Expense first,
    // so bad input throws before the old expense is changed
    public void editExpense(Expense expense, LocalDate date, String category, double amount, String description){
        Expense updated = new Expense(date, category, amount, description);

        expense.setDate(updated.getDate());
        expense.setCategory(updated.getCategory());
        expense.setAmount(updated.getAmount());
        expense.setDescription(updated.getDescription());
    }

    // Returns a copy so outside code can't change the real list by accident.
    public ArrayList<Expense> getExpenses(){
        return new ArrayList<>(expenses);
    }

    // Only expenses in the given month and year count.
    public double getTotalSpent(YearMonth month){
        double total = 0;

        for(Expense expense : expenses){
            if(YearMonth.from(expense.getDate()).equals(month)){
                total += expense.getAmount();
            }
        }

        return total;
    }

    public double getTotalSpentByCategory(YearMonth month, String category){
        double total = 0;

        for(Expense expense : expenses){
            boolean sameMonth = YearMonth.from(expense.getDate()).equals(month);
            boolean sameCategory = expense.getCategory().equalsIgnoreCase(category);

            if(sameMonth && sameCategory){
                total += expense.getAmount();
            }
        }

        return total;
    }

    public double getRemainingBudget(YearMonth month){
        return budget.getRemainingBudget(getTotalSpent(month));
    }

    public double getRemainingCategoryBudget(YearMonth month, String category){
        double spent = getTotalSpentByCategory(month, category);
        return budget.getRemainingCategoryBudget(category, spent);
    }

    public Budget getBudget(){
        return budget;
    }

    public void setBudget(Budget budget){
        if(budget == null){
            throw new IllegalArgumentException("Budget is required.");
        }
        this.budget = budget;
    }
}