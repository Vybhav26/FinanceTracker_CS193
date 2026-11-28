import java.util.ArrayList;

public class Budget{

    private double monthlyLimit;
    private ArrayList<String> categoryNames;
    private ArrayList<Double> categoryLimits;

    public Budget(double monthlyLimit){
        setMonthlyLimit(monthlyLimit);
        categoryNames = new ArrayList<>();
        categoryLimits = new ArrayList<>();
    }

    // TODO: Exception handling
    public void setMonthlyLimit(double monthlyLimit){
        if(Double.isNaN(monthlyLimit) || Double.isInfinite(monthlyLimit) || monthlyLimit <= 0){
            throw new IllegalArgumentException("Budget must be a positive number.");
        }
        this.monthlyLimit = monthlyLimit;
    }

    public void setCategoryLimit(String category, double limit){
        if(category == null || category.trim().isEmpty()){
            throw new IllegalArgumentException("Category is required.");
        }
        if(Double.isNaN(limit) || Double.isInfinite(limit) || limit <= 0){
            throw new IllegalArgumentException("Category limit must be a positive number.");
        }

        String name = category.trim().toLowerCase();
        int index = -1;
        for(int i = 0; i < categoryNames.size(); i++){
            if(categoryNames.get(i).equals(name)){
                index = i;
            }
        }

        if(index >= 0){
            categoryLimits.set(index, limit);
        }else{
            categoryNames.add(name);
            categoryLimits.add(limit);
        }
    }

    public double getMonthlyLimit(){
        return monthlyLimit;
    }

    /* 
    public void setMonthlyLimit(double monthlyLimit){
        this.monthlyLimit = monthlyLimit;
    }

    public void setCategoryLimit(String category, double limit){
        categoryLimits.put(category.trim().toLowerCase(), limit);
    }
    */

    public int getCategoryCount(){
        return categoryNames.size();
    }

    public String getCategoryName(int index){
        return categoryNames.get(index);
    }

    // Returns 0 if the category has no limit
    public double getCategoryLimit(String category){
        String key = category.trim().toLowerCase();

        for(int i = 0; i < categoryNames.size(); i++){
            if(categoryNames.get(i).equals(key)){
                return categoryLimits.get(i);
            }
        }
        return 0;
    }

    // Returns "On Track", "Near Limit", "At Limit", or "Over Budget"
    public String getStatus(double spent, double limit){
        // Round to whole cents 
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
}