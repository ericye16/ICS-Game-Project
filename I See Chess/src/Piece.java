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
    public Piece(boolean white){
        this.isWhite = white;
    }
    public boolean isWhite(){
        return isWhite;
    }
    public int[] getPosition(){
        return position;
    }
    public abstract void move();
}