import java.util.HashMap;

public class Budget{

    private double monthlyLimit;
    private HashMap<String, Double> categoryLimits;

    public Budget(double monthlyLimit){
        setMonthlyLimit(monthlyLimit);
        categoryLimits = new HashMap<>();
    }

    // TODO: Exception handling
    public double getMonthlyLimit(){
        return monthlyLimit;
    }

    public void setMonthlyLimit(double monthlyLimit){
        this.monthlyLimit = monthlyLimit;
    }

    public void setCategoryLimit(String category, double limit){
        categoryLimits.put(category.trim().toLowerCase(), limit);
    }

    // Returns a copy, so outside code can't change the real map by accident.
    public HashMap<String, Double> getCategoryLimits(){
        return new HashMap<>(categoryLimits);
    }

    public boolean hasCategoryLimit(String category){
        return categoryLimits.containsKey(category.trim().toLowerCase());
    }

    // Returns 0 if the category has no limit.
    public double getCategoryLimit(String category){
        String key = category.trim().toLowerCase();

        if(categoryLimits.containsKey(key)){
            return categoryLimits.get(key);
        }
        return 0;
    }

    public double getRemainingBudget(double totalSpent){
        return monthlyLimit - totalSpent;
    }

    public double getRemainingCategoryBudget(String category, double spent){
        return getCategoryLimit(category) - spent;
    }

    // Returns spent / limit as a percent, like 65.0 for 65%.
    public double getUtilization(double spent, double limit){
        if(limit <= 0){
            return 0;
        }
        return (spent / limit) * 100.0;
    }

    // Returns "On Track", "Near Limit", "At Limit", or "Over Budget".
    public String getStatus(double spent, double limit){
        // Round to whole cents first so small decimal errors (like 0.1 + 0.2) don't matter.
        double spentCents = Math.round(spent * 100.0);
        double limitCents = Math.round(limit * 100.0);

        if(spentCents > limitCents){
            return "Over Budget";
        }
        if(spentCents == limitCents){
            return "At Limit";
        }
        if(spentCents >= limitCents * 0.80){
            return "Near Limit";
        }
        return "On Track";
    }

    public boolean isOverBudget(double totalSpent){
        return totalSpent > monthlyLimit;
    }
}