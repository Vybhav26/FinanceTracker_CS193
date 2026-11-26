import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;

public class FileManager{

    private String expenseFile = "data/expenses.txt";
    private String budgetFile = "data/budget.txt";

    public FileManager(){
        new File("data").mkdirs();
    }

    // Each expense is one line: date|category|amount|description
    public void saveExpenses(ArrayList<Expense> expenses){
        try(PrintWriter writer = new PrintWriter(new FileWriter(expenseFile))){
            for(Expense expense : expenses){
                writer.println(expense.getDate() + "|" + expense.getCategory() + "|" + expense.getAmount() + "|" + expense.getDescription());
            }
        }
        
        
        catch(IOException e){
            System.out.println("Error saving expenses: " + e.getMessage());
        }
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

            while(line != null){
                String[] parts = line.split("\\|", -1);

                LocalDate date = LocalDate.parse(parts[0]);
                String category = parts[1];
                double amount = Double.parseDouble(parts[2]);
                String description = parts[3];

                expenses.add(new Expense(date, category, amount, description));

                line = reader.readLine();
            }
        }
        
        catch(IOException e){
            System.out.println("Error loading expenses: " + e.getMessage());
        }

        return expenses;
    }

    // The budget file holds one line: MONTHLY|1000.0
    public void saveBudget(Budget budget){
        
        try(PrintWriter writer = new PrintWriter(new FileWriter(budgetFile))){
            writer.println("MONTHLY|" + budget.getMonthlyLimit());
        }
        
        catch(IOException e){
            System.out.println("Error saving budget: " + e.getMessage());
        }
    }

    public Budget loadBudget(){
        
        Budget budget = new Budget(1000.00);
        File file = new File(budgetFile);


        if(!file.exists()){
            return budget;
        }

        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line = reader.readLine();

            if(line != null){
 
                String[] parts = line.split("\\|");
                budget.setMonthlyLimit(Double.parseDouble(parts[1]));
 
            }
 
        }catch(IOException e){
            System.out.println("Error loading budget: " + e.getMessage());
 
 
        }

        return budget;
    }
}