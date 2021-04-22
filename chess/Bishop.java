package chess;
import java.awt.*;
import java.util.ArrayList;

public class Bishop extends Piece {
  private static Image white;
  private static Image black;
  
  static {
    Bishop.white = (new javax.swing.ImageIcon(Pawn.class.getResource("resources/white-bishop.png"))).getImage();
    Bishop.black = (new javax.swing.ImageIcon(Pawn.class.getResource("resources/black-bishop.png"))).getImage();
  }
  
  public Bishop(Point position, Color color) {
    super(position, color);
  }
  
  @Override
  public ArrayList<Point> getPossibleMoves(Board board) {
    ArrayList<Point> possibleMoves = new ArrayList<>();
    Point point;
    
    for(int i = position.x + 1, j = position.y - 1; (i < 9 && j > 0); i++, j--) {
      point = new Point(i, j);
      Piece inPlace = board.getPiece(point);
      if(inPlace == null) {
        if(!(board.isChecked(this, point, this.position, inPlace)))
          possibleMoves.add(point);
      }
      else {
        if((inPlace.color != this.color) && !(inPlace instanceof King) && !(board.isChecked(this, point, this.position, inPlace)))
          possibleMoves.add(point);
        break;
      }
    }
    
    for(int i = position.x - 1, j = position.y + 1; (i > 0 && j < 9); i--, j++) {
      point = new Point(i, j);
      Piece inPlace = board.getPiece(point);
      if(inPlace == null) {
        if(!(board.isChecked(this, point, this.position, inPlace)))
          possibleMoves.add(point);
      }
      else {
        if((inPlace.color != this.color) && !(inPlace instanceof King) && !(board.isChecked(this, point, this.position, inPlace)))
          possibleMoves.add(point);
        break;
      }
    }
    
    for(int i = position.x - 1, j = position.y - 1; (i > 0 && j > 0); i--, j--) {
      point = new Point(i, j);
      Piece inPlace = board.getPiece(point);
      if(inPlace == null) {
        if(!(board.isChecked(this, point, this.position, inPlace)))
          possibleMoves.add(point);
      }
      else {
        if((inPlace.color != this.color) && !(inPlace instanceof King) && !(board.isChecked(this, point, this.position, inPlace)))
          possibleMoves.add(point);
        break;
      }
    }
    
    for(int i = position.x + 1, j = position.y + 1; (i < 9 && j < 9); i++, j++) {
      point = new Point(i, j);
      Piece inPlace = board.getPiece(point);
      if(inPlace == null) {
        if(!(board.isChecked(this, point, this.position, inPlace)))
          possibleMoves.add(point);
      }
      else {
        if((inPlace.color != this.color) && !(inPlace instanceof King) && !(board.isChecked(this, point, this.position, inPlace)))
          possibleMoves.add(point);
        break;
      }
    }
    
    return possibleMoves;
  }
  
  @Override
  public boolean isChecking(Board board) {
    Point point = new Point(this.position);
    
    for(int i = position.x + 1, j = position.y - 1; (i < 9 && j > 0); i++, j--) {
      point.x = i;
      point.y = j;
      Piece inPlace = board.getPiece(point);
      if(inPlace != null) {
        if((inPlace.color != this.color) && (inPlace instanceof King))
          return true;
        break;
      }
    }
    
    for(int i = position.x - 1, j = position.y + 1; (i > 0 && j < 9); i--, j++) {
      point.x = i;
      point.y = j;
      Piece inPlace = board.getPiece(point);
      if(inPlace != null) {
        if((inPlace.color != this.color) && (inPlace instanceof King))
          return true;
        break;
      }
    }
    
    for(int i = position.x - 1, j = position.y - 1; (i > 0 && j > 0); i--, j--) {
      point.x = i;
      point.y = j;
      Piece inPlace = board.getPiece(point);
      if(inPlace != null) {
        if((inPlace.color != this.color) && (inPlace instanceof King))
          return true;
        break;
      }
    }
    
    for(int i = position.x + 1, j = position.y + 1; (i < 9 && j < 9); i++, j++) {
      point.x = i;
      point.y = j;
      Piece inPlace = board.getPiece(point);
      if(inPlace != null) {
        if((inPlace.color != this.color) && (inPlace instanceof King))
          return true;
        break;
      }
    }
    
    return false;
  }
  
  @Override
  public Piece copy() {
    Piece copy = new Bishop(new Point(this.position), this.color);
    copy.moves = this.moves;
    return copy;
  }
  
  @Override
  public void paint(Graphics g) {
    if(this.color == Color.WHITE)
      g.drawImage(Bishop.white, (this.position.x - 1) * 100, (this.position.y - 1) * 100, null);
    else
      g.drawImage(Bishop.black, (this.position.x - 1) * 100, (this.position.y - 1) * 100, null);
  }
}