import java.time.LocalDate;
import java.time.YearMonth;

public class ExpenseMathCheck {

    public static void main(String[] args){

        // Test 1: Expense stores its values
        Expense lunch = new Expense(LocalDate.of(2026, 11, 21), "Food", 20.00, "Lunch");
        check("Stores amount", lunch.getAmount() == 20.00);

        // Test 2: Expense rejects a negative amount
        boolean rejected = false;
        try{
            new Expense(LocalDate.of(2026, 11, 21), "Food", -5.00, "Bad");
        }catch(IllegalArgumentException e){
            rejected = true;
        }
        check("negative amount rejected", rejected);

        // Test 3: Budget status levels
        Budget budget = new Budget(1000);
        boolean statusesCorrect = budget.getStatus(500, 1000).equals("On Track")
            && budget.getStatus(850, 1000).equals("Near Limit")
            && budget.getStatus(1000, 1000).equals("At Limit")
            && budget.getStatus(1200, 1000).equals("Over Budget");
        check("Budget status levels", statusesCorrect);

        ExpenseManager manager = new ExpenseManager();

        // Test 4: ExpenseManager remaining budget (default budget is 1000)
        check("Remaining budget", manager.getRemainingBudget(YearMonth.of(2026, 11)) == 950.00);
    }

    private static void check(String name, boolean passed){
        if(passed){
            System.out.println("PASS: " + name);
        }else{
            System.out.println("FAIL: " + name);
        }
    }
}