/**
 * Created with IntelliJ IDEA.
 * User: Andrey
 * Date: 25/10/13
 * Time: 9:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class Bishop extends Piece {
    public Bishop(boolean isWhite){
        super(isWhite);
    }
    public void move(){
        int[][] moves = {{0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}};
        for(int i = 0; i < moves.length; i++){
            try{
                if(ISeeChess.board[getPosition()[0] + moves[i][0]][getPosition()[1] + moves[i][1]] == null || isWhite() != ISeeChess.board[getPosition()[0] + moves[i][0]][getPosition()[1] + moves[i][1]].isWhite()){
                    Piece placeholder = ISeeChess.board[getPosition()[0] + moves[i][0]][getPosition()[1] + moves[i][1]];
                    ISeeChess.board[getPosition()[0] + moves[i][0]][getPosition()[1] + moves[i][1]] = new Knight(isWhite());
                    ISeeChess.board[getPosition()[0]][getPosition()[1]] = null;
                    if(ISeeChess.kingSafe(isWhite())){
                        ISeeChess.allowed(new int[] {getPosition()[0] + moves[i][0], getPosition()[1] + moves[i][1]});
                    }
                    ISeeChess.board[getPosition()[0] + moves[i][0]][getPosition()[1] + moves[i][1]] = placeholder;
                    ISeeChess.board[getPosition()[0]][getPosition()[1]] = new Bishop (isWhite());
                }
            }catch(ArrayIndexOutOfBoundsException e){
            }
        }
    }
}