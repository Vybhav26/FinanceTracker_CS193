import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class FileManager{

     private String expenseFile = "data/expenses.txt";
    private String budgetFile = "data/budget.txt";

    public FileManager(){
        new File("data").mkdirs();
    }

    // Each expense is one line: date|category|amount|description
    // Returns true if the save worked.
    public boolean saveExpenses(ArrayList<Expense> expenses){
        try(PrintWriter writer = new PrintWriter(new FileWriter(expenseFile))){
            for(Expense expense : expenses){
                writer.println(expense.getDate() + "|" + expense.getCategory() + "|" + expense.getAmount() + "|" + expense.getDescription());
            }
        }catch(IOException e){
            System.out.println("Error saving expenses: " + e.getMessage());
            return false;
        }

        return true;
    }

    public ArrayList<Expense> loadExpenses(){
        ArrayList<Expense> expenses = new ArrayList<>();
        File file = new File(expenseFile);

        // First run: no file yet, so there is nothing to load.
        if(!file.exists()){
            return expenses;
        }

        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line = reader.readLine();
            int lineNumber = 1;

            while(line != null){
                // Skip blank lines.
                if(!line.trim().isEmpty()){
                    Expense expense = parseExpense(line, lineNumber);

                    if(expense != null){
                        expenses.add(expense);
                    }
                }

                line = reader.readLine();
                lineNumber++;
            }
        }catch(IOException e){
            System.out.println("Error loading expenses: " + e.getMessage());
        }

        return expenses;
    }

    // Turns one line into an Expense. Returns null if the line is bad.
    private Expense parseExpense(String line, int lineNumber){
        String[] parts = line.split("\\|", -1);

        if(parts.length != 4){
            System.out.println("Skipping line " + lineNumber + ": expected 4 fields.");
            return null;
        }

        try{
            LocalDate date = LocalDate.parse(parts[0].trim());
            String category = parts[1];
            double amount = Double.parseDouble(parts[2].trim());
            String description = parts[3];

            // The Expense constructor rejects blank categories and non-positive amounts.
            return new Expense(date, category, amount, description);
        }catch(Exception e){
            System.out.println("Skipping line " + lineNumber + ": " + e.getMessage());
            return null;
        }
    }

    // The budget file looks like this:
    // MONTHLY|1000.0
    // CATEGORY|food|300.0
    // Returns true if the save worked.
    public boolean saveBudget(Budget budget){
        try(PrintWriter writer = new PrintWriter(new FileWriter(budgetFile))){
            writer.println("MONTHLY|" + budget.getMonthlyLimit());

            for(int i = 0; i < budget.getCategoryCount(); i++){
                String category = budget.getCategoryName(i);
                writer.println("CATEGORY|" + category + "|" + budget.getCategoryLimit(category));
            }
        }catch(IOException e){
            System.out.println("Error saving budget: " + e.getMessage());
            return false;
        }

        return true;
    }

    public Budget loadBudget(){
        Budget budget = new Budget(1000.00);
        File file = new File(budgetFile);

        if(!file.exists()){
            return budget;
        }

        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line = reader.readLine();

            while(line != null){
                String[] parts = line.split("\\|");
                String type = parts[0];

                if(type.equals("MONTHLY")){
                    double monthly = Double.parseDouble(parts[1]);
                    budget.setMonthlyLimit(monthly);
                }

                if(type.equals("CATEGORY")){
                    String category = parts[1];
                    double limit = Double.parseDouble(parts[2]);
                    budget.setCategoryLimit(category, limit);
                }

                line = reader.readLine();
            }
        }catch(Exception e){
            System.out.println("Error loading budget: " + e.getMessage());
        }

        return budget;
    }
}