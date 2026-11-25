import javax.swing.SwingUtilities;

public class Main{

    public static void main(String[] args){

        // TODO: create the FileManager and load saved data.

        ExpenseManager manager = new ExpenseManager();

        SwingUtilities.invokeLater(() -> {
            ExpenseTrackerGUI gui = new ExpenseTrackerGUI(manager);
            gui.setVisible(true);
        });
    }
}