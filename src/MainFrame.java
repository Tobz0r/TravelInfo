import javax.swing.*;
import java.awt.*;


/**
 * The GUI for this program
 * @author Tobias Estefors
 * @version 2016-01-14
 */
public class MainFrame extends JFrame {

    private InfoUpdater infoUpdater;
    private MainPanel mainPanel;
    private Menu menu;

    public MainFrame(String title){
        super(title);
        ImageIcon img = new ImageIcon(getClass().getClassLoader().getResource("icon.png"));
        setIconImage(img.getImage());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        mainPanel=new MainPanel(this);
        add(mainPanel,BorderLayout.CENTER);
        menu=new Menu(this, mainPanel);
        infoUpdater=new InfoUpdater();
        infoUpdater.addObserver(mainPanel);
        infoUpdater.start();
        pack();
    }

    /**
     * Changes the infoupdaters updateinterval
     * @param interval a long containing halfhour intervals
     */
    void changeInterval(long interval){
        infoUpdater.changeInterval(interval);
    }

    /**
     * Refreshing the gui
     */
    void refresh(){
        infoUpdater.refresh();
    }


    /**
     * Shows the gui
     */
    public void start(){
        setVisible(true);
    }



}
