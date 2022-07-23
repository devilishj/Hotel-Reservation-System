package model;


/**Free Room model
 *
 * @author Jason Renek
 */
public class FreeRoom extends Room {        // Extends Room

    public FreeRoom(String roomNumber,
                    double roomPrice,
                    RoomType enumeration) {
        super(roomNumber,
                0.0,                //Change constructor to set the price to $0
                enumeration);
    }


    @Override
    public String toString() {
        return "Room#: " + this.getRoomNumber() + ",  "
                + this.getRoomType() + " Bed,  "
                + "Price per night: " + "**FREE**"  + ".";  // Override the toString for a better description
    }
}
