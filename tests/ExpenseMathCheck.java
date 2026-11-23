import java.time.LocalDate;

public class ExpenseMathCheck {

    public static void main(String[] args){

        Expense lunch = new Expense(LocalDate.of(2026, 11, 21), "Food", 20.00, "Lunch");
        System.out.println(lunch);

        Expense piped = new Expense(LocalDate.of(2026, 11, 21), "Food", 5.00, "fish|chips");
        System.out.println(piped.getDescription());

        try{
            new Expense(LocalDate.of(2026, 11, 21), "Food", -5.00, "Bad");
            System.out.println("FAIL: negative amount was accepted");
        }catch(IllegalArgumentException e){
            System.out.println("PASS: " + e.getMessage());
        }
    }
}