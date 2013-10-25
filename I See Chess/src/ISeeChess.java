/**
 * Created with IntelliJ IDEA.
 * User: Andrey
 * Date: 25/10/13
 * Time: 8:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class ISeeChess {
    public static Piece[][] board = {{new Rook(true), new Knight(true), new Bishop(true), new Queen(true), new King(true), new Bishop(true), new Knight(true), new Rook(true)}, {new Pawn(true), new Pawn(true), new Pawn(true), new Pawn(true), new Pawn(true), new Pawn(true), new Pawn(true), new Pawn(true)}, {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null}, {new Pawn(false), new Pawn(false), new Pawn(false), new Pawn(false), new Pawn(false), new Pawn(false), new Pawn(false), new Pawn(false)}, {new Rook(false), new Knight(false), new Bishop(false), new Queen(false), new King(false), new Bishop(false), new Knight(false), new Rook(false)}};
    public static void main(String[] args){
    }
    public static void allowed(int[] location){
    }
    public static boolean kingSafe(boolean white){
        return true;
    }
}