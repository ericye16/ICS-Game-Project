/**
 * Created with IntelliJ IDEA.
 * User: Andrey
 * Date: 25/10/13
 * Time: 9:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class Pawn extends Piece {
    public Pawn(boolean isWhite){
        super(isWhite);
    }
    public void move(){
        int[][] moves = {{0, super.getIsWhite() ? 1 : -1}, {1, super.getIsWhite() ? 1 : -1}, {-1, super.getIsWhite() ? 1 : -1}, {0, super.getIsWhite() ? 2 : -2}};
        for(int i = 0; i < moves.length; i++){
            if(ISeeChess.board[getPosition()[0] + moves[i][0]][getPosition()[1] + moves[i][1]] == null || super.getIsWhite() != ISeeChess.board[getPosition()[0] + moves[i][0]][getPosition()[1] + moves[i][1]].getIsWhite()){
                try{
                    ISeeChess.board[getPosition()[0] + moves[i][0]][getPosition()[1] + moves[i][1]] = new Knight(super.getIsWhite());
                    ISeeChess.board[getPosition()[0]][getPosition()[1]] = null;
                    if(ISeeChess.kingSafe(super.getIsWhite())){
                        ISeeChess.allowed(new int[] {getPosition()[0] + moves[i][0], getPosition()[1] + moves[i][1]});
                    }
                    ISeeChess.board[getPosition()[0]][getPosition()[1]] = new Knight(super.getIsWhite());
                }catch(ArrayIndexOutOfBoundsException e){
                }
            }
        }
    }
}
