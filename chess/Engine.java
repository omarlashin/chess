package chess;

public class Engine {
  private final int maxDepth;
  private Board selectedBoard;
  
  public Engine(int maxDepth) {
    this.maxDepth = maxDepth;
  }
  
  public Board getSelectedBoard() {
    return this.selectedBoard;
  }
  
  public int minimax(Board currentBoard, int depth, int alpha, int beta, boolean isMaximizingPlayer) {
    if(depth == 0)
      return currentBoard.utility();
    
    if(isMaximizingPlayer) {
      boolean unselected = true;
      int maximum = Integer.MIN_VALUE;
      int evaluation;
      for(Board child : currentBoard.getChildren()) {
        evaluation = this.minimax(child, depth - 1, alpha, beta, false);
        if(((evaluation > maximum) && (depth == this.maxDepth)) || ((depth == this.maxDepth) && unselected)) {
          this.selectedBoard = child;
          unselected = false;
        }
        maximum = Math.max(maximum, evaluation);
        alpha = Math.max(alpha, evaluation);
        if(beta <= alpha)
          break;
      }
      
      return maximum;
    }
    else {
      int minimum = Integer.MAX_VALUE;
      int evaluation;
      for(Board child : currentBoard.getChildren()) {
        evaluation = this.minimax(child, depth - 1, alpha, beta, true);
        minimum = Math.min(minimum, evaluation);
        beta = Math.min(beta, evaluation);
        if(beta <= alpha)
          break;
      }
      
      return minimum;
    }
  }
}