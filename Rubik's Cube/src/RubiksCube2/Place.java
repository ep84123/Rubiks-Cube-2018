package RubiksCube2;

public class Place {

    private Piece piece;

    public Place(){
        piece = new Piece();
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }
}
