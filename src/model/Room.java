package model;


import java.text.DecimalFormat;
import java.util.Objects;

/** Room model
 *
 * @author Jason Renek
 */
public class Room implements Iroom {        // implement the Iroom interface
    private final String roomNumber;        // Add variable String roomNumber
    private final double roomPrice;         // Add variable Double price
    private final RoomType roomType;        // Add variable roomType enumeration
    private boolean isFree;
    public Room(String roomNumber,
                double roomPrice,
                RoomType enumeration) {
        super();

        this.roomNumber = roomNumber;
        this.roomPrice = roomPrice;
        this.roomType = enumeration;    }

    @Override
    public String getRoomNumber() {         // Override variables
        return roomNumber;
    }
    @Override
    public double getRoomPrice() {
        return roomPrice;
    }
    @Override
    public RoomType getRoomType() {
        return roomType;
    }
    @Override
    public boolean getIsFree() {
        return isFree;
    }

    @Override
    public boolean equals (Object that) {                // Override equals and hashcode to prevent duplicate rooms
        if (this == that) return true;
        if(!(that instanceof Room thatRoom)) return false;

        return Objects.equals(this.roomNumber, ((Room) that).roomNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.roomNumber);
    }

    @Override
    public String toString() {
        return "Room#: " + this.roomNumber + ",  "
                +  this.roomType + " Bed,  "
                + "Price per night: $" + this.roomPrice  + ".";  // Override the toString for a better description
    }
}
