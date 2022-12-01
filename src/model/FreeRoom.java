package model;

public class FreeRoom extends Room {
    public FreeRoom(String roomNumber, Double price, RoomType enumeration) {
        super(roomNumber, 0.00, enumeration);
    }

    @Override
    public String toString() {
        return "Room: "+ getRoomNumber() + ". Type: " + getRoomType()
                + ". Price: €" + getRoomPrice() + " (Free).";
    }
}
