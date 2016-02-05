
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Parses an xml-file containing all the levels and adds them to an arraylist
 * @author Tobias Estefors
 * @version 2016-01-14
 */
public class ReadXML {

    private InputStream xmlFile;
    private File xml3File;
    private ArrayList<TravelElement> travelElements;

    private TravelElement travelElement;

    public ReadXML() {

    }

    public ArrayList getTravelInfo(InputStream xmlFile){
        this.xmlFile=xmlFile;
        travelElements=new ArrayList<>();
        parseXML();
        return travelElements;
    }

    /**
     * Parses an XML-document with SAX parser
     */
    private synchronized void parseXML() {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = factory.newSAXParser();
            xmlHandler handler = new xmlHandler();
            saxParser.parse(xmlFile, handler);
        } catch (ParserConfigurationException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } catch (SAXException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private class xmlHandler extends DefaultHandler {

        private boolean toBeAdded;
        private String qName;

        /**
         * Checks called every time a new element is found in xML
         * Checks the attrubyted and adds them to a new level.
         * @param uri Never used
         * @param localName Never used
         * @param qName the name of the element
         * @param attributes attributes if any in the element
         * @throws SAXException
         */
        @Override
        public void startElement(String uri, String localName,
                                 String qName, Attributes attributes)
                                 throws SAXException {
            this.qName=qName;
            if((qName.equals("CityName")) || (qName.equals("CurrentPrice")) ||
                    (qName.equals("OutDate")) ||(qName.equals("DestinationName")) ||
                    (qName.equals("HotelImage"))){
                toBeAdded=true;
            }
            else if (qName.equals("Offer")){
                toBeAdded=false;
                travelElement=new TravelElement();
            }
            else{
                toBeAdded=false;
            }
        }

        /**
         *  Adds the tile from every node to a matrix
         * @param ch, start, length compinds to the name of the node
         * @throws SAXException
         */
        @Override
        public void characters(char ch[], int start, int length)
                throws SAXException {
            String element = new String(ch, start, length);
            if(toBeAdded){
                switch(qName){
                    case "DestinationName":
                        travelElement.setDestination(element);
                        break;
                    case "CityName":
                        travelElement.setCity(element);
                        break;
                    case "CurrentPrice":
                        travelElement.setCost(element);
                        break;
                    case "OutDate":
                        travelElement.setDate(element);
                        break;
                    case "HotelImage":
                        travelElement.setPicture(element);
                        break;
                    default:
                        System.out.println(element + " someting went wrong..");
                        break;
                }
            }
        }

        /**
         * Gets called when a end tag"</>" is found
         * Only does something with mapData and adds the level to an arraylist
         * @param uri,qName,localName string that contain info of the node.
         *                            is never used in this method
         * @throws SAXException
         */
        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            if (qName.equals("Offer")) {
                travelElements.add(travelElement);
            }
            toBeAdded=false;
        }
    }
}
