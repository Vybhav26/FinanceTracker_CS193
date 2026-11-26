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

    // Add expenses within a given month
    public ArrayList<Expense> getExpensesForMonth(YearMonth month){
        ArrayList<Expense> result = new ArrayList<>();

        for(Expense expense : expenses){
            if(YearMonth.from(expense.getDate()).equals(month)){
                result.add(expense);
            }
        }

        return result;
    }

    // How many days of the month count as "spent so far".
    // Past month: the whole month. Current month: up to today. Future month: 0.
    public int getElapsedDays(YearMonth month, LocalDate today){
        YearMonth current = YearMonth.from(today);

        if(month.isBefore(current)){
            return month.lengthOfMonth();
        }
        if(month.equals(current)){
            return today.getDayOfMonth();
        }
        return 0;
    }

    public double getDailyAverage(YearMonth month, LocalDate today){
        int days = getElapsedDays(month, today);

        if(days == 0){
            return 0;
        }
        return getTotalSpent(month) / days;
    }

    // Projected spending = daily average x days in the month.
    // For a past month, the real total is already known, so return that instead.
    public double getProjectedSpending(YearMonth month, LocalDate today){
        if(month.isBefore(YearMonth.from(today))){
            return getTotalSpent(month);
        }
        return getDailyAverage(month, today) * month.lengthOfMonth();
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