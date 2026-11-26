import java.time.LocalDate;

public class Expense{

    // Getters and Setter class for the below fields

    private LocalDate date;
    private String category;
    private double amount;
    private String description;

    public Expense(LocalDate date, String category, double amount, String description){
        setDate(date);
        setCategory(category);
        setAmount(amount);
        setDescription(description);
    }

    public LocalDate getDate(){
        return date;
    }

    public String getCategory(){
        return category;
    }

    public double getAmount(){
        return amount;
    }

    public String getDescription(){
        return description;
    }

    public void setDate(LocalDate date){
        if(date == null){
            throw new IllegalArgumentException("Date is required.");
        }
        this.date = date;
    }

    public void setCategory(String category){
        if(category == null || category.trim().isEmpty()){
            throw new IllegalArgumentException("Category is required.");
        }
        this.category = category.replace("|", "/").trim();
    }

    public void setAmount(double amount){
        if(Double.isNaN(amount) || Double.isInfinite(amount) || amount <= 0){
            throw new IllegalArgumentException("Amount must be a positive number.");
        }
        this.amount = amount;
    }

    public void setDescription(String description){
        if(description == null){
            this.description = "";
        }else{
            this.description = description.replace("|", "/").trim();
        }
    }

    @Override
    public String toString(){
        return date + " | " + category + " | $" + String.format("%.2f", amount) + " | " + description;
    }
}
