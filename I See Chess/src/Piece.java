/**
 * Created with IntelliJ IDEA.
 * User: Andrey
 * Date: 25/10/13
 * Time: 9:11 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Piece {
    private boolean isWhite;
    private int[] position;
    public Piece(boolean isWhite){
        this.isWhite = isWhite;
    }
    public boolean getIsWhite(){
        return isWhite;
    }
    public int[] getPosition(){
        return position;
    }
    public abstract void move();
}