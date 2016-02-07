import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

/**
 * The maincontainer containing the table wich shows
 * all the information
 * @author Tobias Estefors
 * @version 2016-02-04
 */
public class MainPanel extends JPanel implements Observer {

    private JScrollPane scroll;
    private static Object lockObject=new Object();
    private JTable table;
    private JButton refreshButton;
    private JPanel refreshPanel;
    private MainFrame frame;
    private DefaultTableModel model;
    private ArrayList<TravelElement> travelElements;
    private String[] destAll=null;
    private boolean hasDest=false;

    public MainPanel(MainFrame frame){
        super(new BorderLayout());
        this.frame=frame;
        setUpRefresh();
        setLayout(new GridLayout(1,1));
        table=new JTable();
        model = new DefaultTableModel(0, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if(getRowCount() > 0){
                    return getValueAt(0,columnIndex).getClass();
                }
                return super.getColumnClass(columnIndex);
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setDragEnabled(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.setModel(model);

        model.addColumn("Destination");
        model.addColumn("Cost");
        model.addColumn("Arrival");
        model.addColumn("Picture");
        table.setRowHeight(200);

        scroll = new JScrollPane(table);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scroll, BorderLayout.CENTER);

    }

    /**
     * Clear the table of all its rows
     */
    private synchronized void clearTable(){
        int rowCount=model.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            final int j=i;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    model.removeRow(j);
                }
            });
        }
    }

    /**
     *  Takes a object from an observable and
     *  does something with it depends on wich
     *  oberservable who sent it
     * @param o the obeservable sending a object
     * @param arg a object sent from an oberservable
     */
    @Override
    public void update(Observable o, Object arg) {
        synchronized (lockObject) {

            refreshButton.setEnabled(false);
            if (o instanceof InfoUpdater) {
                clearTable();
                boolean tempDest = hasDest;
                travelElements = (ArrayList) arg;
                for (TravelElement element : travelElements) {
                    if (contains(element.getDestination(), tempDest)) {
                        updateRows(element);
                    }
                }
            }
            refreshButton.setEnabled(true);
        }
    }

    /**
     * Sets the destination array
     * @param destAll a string array with destinations
     */
    public void setDest(String[] destAll){
        if(destAll!=null) {
            this.destAll = destAll;
            hasDest = destAll.length > 0 ? true : false;
        }
        else{
            hasDest=false;
        }
    }

    /**
     * Adds all the travelements to the table
     * @param element a travelelement
     */
    private void updateRows(TravelElement element){

        Vector newRow = new Vector();
        newRow.add(element.getCity() + ", " + element.getDestination());
        newRow.add(element.getCost());
        newRow.add(element.getDate());
        BufferedImage hotel = null;
        URL url;
        try {
            url=new URL(element.getPicture());
            hotel = ImageIO.read(url);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }
        hotel=(BufferedImage) resizeImage(hotel,200,200);
        ImageIcon hotelCon= new ImageIcon(hotel);
        newRow.add(hotelCon);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                model.addRow(newRow);
            }
        });
    }
    /**
     * Checks if current destination is in the
     * destinations array, if the array is empty
     * or doesnt exist it returns true
     * @param dest a string with one destination
     * @param hasDest boolean to return true instantly
     *                if destAll is empty
     * @return true if destAll contains dest or destAll
     * is empty, else false
     */
    private boolean contains(String dest, boolean hasDest){
        if(destAll==null || !hasDest) {
            return true;
        }
        for(String temp: destAll){
            if(temp.equals(dest)){
                return true;
            }
        }
        return false;
    }

    /**
     * Resizes a buffered image for it to fit in the table
     * @param myImg the img to be resized
     * @param w new width of the image
     * @param h new height of the image
     * @return a resized image
     */
    private Image resizeImage(Image myImg, int w, int h){
        BufferedImage resizeImg = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizeImg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                           RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(myImg,0,0,w,h,null);
        g.dispose();

        return resizeImg;
    }

    /**
     * Adds a button with a refreshoption to a bottompanel
     */
    private void setUpRefresh() {
        refreshPanel=new JPanel();
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setBorder(BorderFactory.createLineBorder(Color.black));
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        refreshButton.setEnabled(false);
                    }
                });

                frame.refresh();
                refreshButton.setEnabled(true);
            }
        });
        refreshButton.setEnabled(false);
        refreshPanel.add(refreshButton);
        frame.add(refreshPanel,BorderLayout.SOUTH);
    }

    /**
     * Refreshmethod for filtering, refreshes the GUI without
     * getting a new list of TravelElements.
     */
    void refreshForFilter(){
        (new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lockObject) {
                    refreshButton.setEnabled(false);
                    clearTable();
                    boolean tempDest = hasDest;
                    for (TravelElement element : travelElements) {
                        if (contains(element.getDestination(), tempDest)) {
                            updateRows(element);
                        }
                    }
                    refreshButton.setEnabled(true);
                }
            }
        })).start();
    }
}