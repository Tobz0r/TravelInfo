

import org.xml.sax.SAXException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;
import javax.xml.*;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

/**
 * A thread used to give the gui information from an url
 * @author Tobias Estefors
 * @version 2016-02-04
 */
public class InfoUpdater extends Observable implements Runnable {

    private Thread thread=null;

    private long interval;

    private Timer timer;

    private ReadXML readXML;

    private final String xmlURL=
            "http://www.fritidsresor.se/Blandade-Sidor/feeds/tradera/";

    public InfoUpdater(){
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader("settings.txt"));
            String x;
            x = br.readLine();
            interval=Integer.parseInt(x);
        } catch (IOException  e) {
            interval=1800000;
        }
        timer=new Timer();
        readXML=new ReadXML();
    }

    /**
     * Changes the updateinterval for the thread
     * @param interval a halfhour*X interval
     */
    public void changeInterval(long interval){
        this.interval=interval;
    }

    /**
     * Starts a thread with this runnable
     *
     */
    public synchronized void start(){
        if(thread==null){
            thread=new Thread(this);
            thread.start();
        }
    }

    /**
     * Stops this working thread
     */
    public synchronized void stop(){
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            thread=null;
        }
    }

    /**
     * Refreshes the gui on a new thread to avoid
     * the timertask to get interupted
     */
    public void refresh(){
        (new Thread(new Runnable() {
            @Override
            public void run() {
                updateInfo();
            }
        })).start();
    }

    /**
     * Runs the updatelogic for the thread
     * Adds a XML file to an inputstream and sends it
     * to the parser
     */
    @Override
    public void run(){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateInfo();
            }
        }, 0, interval);
    }

    /**
     * Validates XML document against a XML schema
     * @param url an url to the xml document
     * @throws IOException
     */
    private boolean validateXML(URL url){
        Source xmlFile=null;
        InputStream stream;
        try {
            URL schemaFile=new URL("http://www8.cs.umu.se/~dv13tes/LMSData.xsd");
            stream=url.openStream();
            xmlFile = new StreamSource(stream);
            SchemaFactory schemaFactory = SchemaFactory
                    .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(schemaFile);
            Validator validator = schema.newValidator();
            validator.validate(xmlFile);
        } catch (SAXException e) {
            JOptionPane.showMessageDialog(null, xmlFile.getSystemId() + " is NOT valid");
            JOptionPane.showMessageDialog(null, "Reason: " + e.getLocalizedMessage() +
                                          "\n" + "Try again by refreshing");
            return false;
        } catch (MalformedURLException e) {
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }
        return true;
    }

    /**
     * Uses a new version of an XML document,
     * parses it and passes it to the GUI.
     */
    private synchronized void updateInfo(){
        try {
            InputStream stream;
            URL url;
            url = new URL(xmlURL);
            if(validateXML(url)) {
                stream = url.openStream();
                update(readXML.getTravelInfo(stream));
            }
        }catch (IOException e){
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }
    }


    /**
     * Notifies the observer with a list from XML
     * @param list a list with TravelElements
     */
    private synchronized void update(ArrayList list){
        setChanged();
        notifyObservers(list);
    }
}
