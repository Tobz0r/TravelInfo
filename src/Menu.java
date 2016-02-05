import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * Menuclass for the gui
 * @author Tobias Estefors
 * @version 2016-01-14
 */
public class Menu {

    private MainFrame frame;
    private JMenuItem  preferences, update, about, exit;
    private JMenuBar menuBar;
    private JMenu menu;
    private MainPanel panel;
    private final long halfHour=1800000;

    public Menu(MainFrame frame, MainPanel panel){
        this.panel=panel;
        this.menu=new JMenu("Menu");
        this.frame=frame;
        menuBar=new JMenuBar();
        setUp();
    }

    /**
     * Sets up the menubar and all its components
     */
    private void setUp(){
        frame.setJMenuBar(menuBar);
        menuBar.add(menu);
        preferences=menu.add("Preferences");
        preferences.setBackground(Color.white);
        preferences.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String string=null;
                string = JOptionPane.showInputDialog("Enter prefered countries" +
                        "(separate by commas (,))\n" +
                        "Leave blank to reset preferences");
                String[] destinations =null;
                if(string!=null) {
                    if (string.length() > 0) {
                        destinations = string.split(",");
                    }
                    panel.setDest(destinations);
                    panel.refreshForFilter();
                }
            }
        });

        update=menu.add("Change updateinterval");
        update.setBackground(Color.white);
        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    long interval = Integer.parseInt(JOptionPane.showInputDialog("" +
                            "Enter new interval in half-hours"));
                    if(interval!=JOptionPane.CANCEL_OPTION) {
                        interval *= halfHour;
                        if (interval < halfHour) {
                            JOptionPane.showMessageDialog(null,
                                    "Chose a value over 1");
                        } else {
                            PrintWriter writer = new PrintWriter("settings.txt",
                                    "UTF-8");
                            writer.println(interval);
                            writer.close();
                            frame.changeInterval(interval);
                        }
                    }
                }catch (NumberFormatException e1){
                    JOptionPane.showMessageDialog(null,"Invalid input!");
                } catch (FileNotFoundException e3) {
                    JOptionPane.showMessageDialog(null, e3.getLocalizedMessage());
                } catch (UnsupportedEncodingException e2) {
                    JOptionPane.showMessageDialog(null, e2.getLocalizedMessage());
                }

            }
        });

        about=menu.add("About");
        about.setBackground(Color.white);
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,"Made by Tobias Estefors");
            }
        });

        exit=menu.add("Exit");
        exit.setBackground(Color.white);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
}
