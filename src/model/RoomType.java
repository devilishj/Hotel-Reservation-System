package model;

/**Room Type enum
 *
 * @author Jason Renek
 */
public enum RoomType {  // Create RoomType enumeration
    SINGLE("1"),
    DOUBLE("2");

    public final String label;

    private RoomType(String label) {
        this.label = label;
    }

    public static RoomType valueOfLabel(String label) {
        for (RoomType enumeration : values()) {
            if (enumeration.label.equals(label)) {
                return enumeration;
            }
        }
        throw new IllegalArgumentException();
    }
}

