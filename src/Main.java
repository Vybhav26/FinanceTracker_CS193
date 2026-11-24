import javax.swing.SwingUtilities;

public class Main{

    public static void main(String[] args){

        // TODO: create the FileManager
        // TODO: create the ExpenseManager + GUI Logic

        SwingUtilities.invokeLater(() -> {
        
            ExpenseTrackerGUI gui = new ExpenseTrackerGUI();
            gui.setVisible(true);
        
        });
    }
}