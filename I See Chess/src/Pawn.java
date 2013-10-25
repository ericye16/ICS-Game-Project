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
        int[][] moves = {{1, isWhite() ? 1 : -1}, {-1, isWhite() ? 1 : -1}, {0, isWhite() ? 1 : -1}, {0, isWhite() ? 2 : -2}};
        for(int i = 0; i < moves.length - (getPosition()[1] == (isWhite() ? 2 : 8) ? 0 : 1); i++){
            try{
                if ((i < 2 && isWhite() != ISeeChess.board[getPosition()[0] + moves[i][0]][getPosition()[1] + moves[i][1]].isWhite()) || ((i >= 2 && (ISeeChess.board[getPosition()[0]][getPosition()[1] + (isWhite() ? 1 : -1)] == null || isWhite() != ISeeChess.board[getPosition()[0]][getPosition()[1] + (isWhite() ? 1 : -1)].isWhite())) && (i == 3 ? (ISeeChess.board[getPosition()[0]][getPosition()[1] + (isWhite() ? 2 : -2)] == null || isWhite() != ISeeChess.board[getPosition()[0]][getPosition()[1] + (isWhite() ? 2 : -2)].isWhite()) : true))){
                    ISeeChess.board[getPosition()[0] + moves[i][0]][getPosition()[1] + moves[i][1]] = new Knight(isWhite());
                    ISeeChess.board[getPosition()[0]][getPosition()[1]] = null;
                    if(ISeeChess.kingSafe(isWhite())){
                        ISeeChess.allowed(new int[] {getPosition()[0] + moves[i][0], getPosition()[1] + moves[i][1]});
                    }
                    ISeeChess.board[getPosition()[0]][getPosition()[1]] = new Pawn(isWhite());
                }
            }catch(ArrayIndexOutOfBoundsException e){
            }
        }
    }
}
