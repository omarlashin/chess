package chess;
import java.awt.*;
import java.util.ArrayList;

public class Rook extends Piece {
  private static Image white;
  private static Image black;
  
  static {
    Rook.white = (new javax.swing.ImageIcon(Pawn.class.getResource("resources/white-rook.png"))).getImage();
    Rook.black = (new javax.swing.ImageIcon(Pawn.class.getResource("resources/black-rook.png"))).getImage();
  }
  
  public Rook(Point position, Color color) {
    super(position, color);
  }
  
  @Override
  public ArrayList<Point> getPossibleMoves(Board board) {
    ArrayList<Point> possibleMoves = new ArrayList<>();
    Point point;
    
    for(int i = position.x + 1; i < 9; i++) {
      point = new Point(this.position);
      point.x = i;
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
    
    for(int i = position.x - 1; i > 0; i--) {
      point = new Point(this.position);
      point.x = i;
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
    
    for(int i = position.y + 1; i < 9; i++) {
      point = new Point(this.position);
      point.y = i;
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
    
    for(int i = position.y - 1; i > 0; i--) {
      point = new Point(this.position);
      point.y = i;
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
    for(int i = position.x + 1; i < 9; i++) {
      point.x = i;
      Piece inPlace = board.getPiece(point);
      if(inPlace != null) {
        if((inPlace.color != this.color) && (inPlace instanceof King))
          return true;
        break;
      }
    }
    
    for(int i = position.x - 1; i > 0; i--) {
      point.x = i;
      Piece inPlace = board.getPiece(point);
      if(inPlace != null) {
        if((inPlace.color != this.color) && (inPlace instanceof King))
          return true;
        break;
      }
    }
    
    point = new Point(this.position);
    for(int i = position.y - 1; i > 0; i--) {
      point.y = i;
      Piece inPlace = board.getPiece(point);
      if(inPlace != null) {
        if((inPlace.color != this.color) && (inPlace instanceof King))
          return true;
        break;
      }
    }
    
    for(int i = position.y + 1; i < 9; i++) {
      point.y = i;
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
    Piece copy = new Rook(new Point(this.position), this.color);
    copy.moves = this.moves;
    return copy;
  }
  
  @Override
  public void paint(Graphics g) {
    if(this.color == Color.WHITE)
      g.drawImage(Rook.white, (this.position.x - 1) * 100, (this.position.y - 1) * 100, null);
    else
      g.drawImage(Rook.black, (this.position.x - 1) * 100, (this.position.y - 1) * 100, null);
  }
}