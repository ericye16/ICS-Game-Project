import sun.reflect.generics.reflectiveObjects.NotImplementedException;
/**
 * Created with IntelliJ IDEA.
 * User: Andrey
 * Date: 25/10/13
 * Time: 9:11 AM
 */
public enum Piece {
    Pawn,Knight,Rook,Queen,King,Bishop;

    int[][] getPossibleMoves() {
        int[][] moves;
        switch (this) {
            case Pawn:
                break;
            case Knight:
                break;
            case Bishop:
                break;
            case Rook:
                break;
            case Queen:
                break;
            case King:
                break;
        }
        throw new NotImplementedException();
    }
}