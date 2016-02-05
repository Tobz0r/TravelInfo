/**
 * Class used to save all the information
 * from the XML document in strings
 * @author Tobias Estefors
 * @version 2016-01-14
 */
public class TravelElement {

    private String date;
    private String destination;
    private String city;
    private String cost;
    private String picture;

    /**
     * Rreturns an addres to a picture
     * @return string containing url
     */
    public String getPicture(){
        return picture;
    }

    /**
     * Adds a picture url to a string
     * @param picture a string containing URL
     */
    public void setPicture(String picture){
        this.picture=picture;
    }

    /**
     * Reurns the city name
     * @return a string with a cityname
     */
    public String getCity() {
        return city;
    }

    /**
     * Adds a cityname to a string
     * @param city a string containing cityname
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Returns boardingdate
     * @return a string with boardingdate
     */
    public String getDate() {
        return date;
    }

    /**
     * Adds the boardingdate to a string
     * @param date a string with a date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Returns the destination
     * @return a string with destination
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Adds the destination to a string
     * @param destination a string containing destination
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * Returns the cost of the travel
     * @return cost of travel
     */
    public String getCost() {
        return cost;
    }

    /**
     * Adds the cost to a string
     * @param cost
     */
    public void setCost(String cost) {
        this.cost = cost;
    }

}
