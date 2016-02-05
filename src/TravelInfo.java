import javax.swing.*;

/**
 * @author Tobias Estefors
 * @version 2016-01-14
 */
public class TravelInfo {

    public static void main(String[] args){

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame gui=new MainFrame("Travel Info");
                gui.start();
            }
        });
    }
}
