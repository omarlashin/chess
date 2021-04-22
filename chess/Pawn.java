package chess;
import java.awt.*;
import java.util.ArrayList;

public class Pawn extends Piece {
  private static Image white;
  private static Image black;
  
  static {
    Pawn.white = (new javax.swing.ImageIcon(Pawn.class.getResource("resources/white-pawn.png"))).getImage();
    Pawn.black = (new javax.swing.ImageIcon(Pawn.class.getResource("resources/black-pawn.png"))).getImage();
  }
  
  public Pawn(Point position, Color color) {
    super(position, color);
    this.moves = 0;
  }
  
  @Override
  public ArrayList<Point> getPossibleMoves(Board board) {
    ArrayList<Point> possibleMoves = new ArrayList<>();
    int sign = (board.getBoardColor() == Board.myColor)? 1 : -1;
    Point point = new Point(this.position);
    boolean withinRange;
    Piece inPlace;
    
    point.translate(0, 1 * sign);
    withinRange = (point.x > 0 && point.x < 9) && (point.y > 0 && point.y < 9);
    if(withinRange && (board.getPiece(point) == null)) {
      if(!(board.isChecked(this, point, this.position, null)))
        possibleMoves.add(point);
      
      if(this.moves == 0) {
        point = new Point(this.position);
        point.translate(0, 2 * sign);
        withinRange = (point.x > 0 && point.x < 9) && (point.y > 0 && point.y < 9);
        if(withinRange && (board.getPiece(point) == null) && !(board.isChecked(this, point, this.position, null)))
          possibleMoves.add(point);
      }
    }
    
    point = new Point(this.position);
    point.translate(1, 1 * sign);
    withinRange = (point.x > 0 && point.x < 9) && (point.y > 0 && point.y < 9);
    inPlace = board.getPiece(point);
    if(withinRange && (inPlace != null) && (inPlace.color != this.color) && (!(inPlace instanceof King)) && !(board.isChecked(this, point, this.position, inPlace)))
        possibleMoves.add(point);
    
    point = new Point(this.position);
    point.translate(-1, 1 * sign);
    withinRange = (point.x > 0 && point.x < 9) && (point.y > 0 && point.y < 9);
    inPlace = board.getPiece(point);
    if(withinRange && (inPlace != null) && (inPlace.color != this.color) && (!(inPlace instanceof King)) && !(board.isChecked(this, point, this.position, inPlace)))
        possibleMoves.add(point);
    
    return possibleMoves;
  }
  
  @Override
  public boolean isChecking(Board board) {
    int sign = (board.getBoardColor() == Board.myColor)? -1 : 1;
    Point point;
    boolean withinRange;
    Piece inPlace;
    
    point = new Point(this.position);
    point.translate(-1, 1 * sign);
    withinRange = (point.x > 0 && point.x < 9) && (point.y > 0 && point.y < 9);
    inPlace = board.getPiece(point);
    if(withinRange && (inPlace != null) && (inPlace.color != this.color) && (inPlace instanceof King))
      return true;
    
    point = new Point(this.position);
    point.translate(1, 1 * sign);
    withinRange = (point.x > 0 && point.x < 9) && (point.y > 0 && point.y < 9);
    inPlace = board.getPiece(point);
    if(withinRange && (inPlace != null) && (inPlace.color != this.color) && (inPlace instanceof King))
      return true;
    
    return false;
  }
  
  @Override
  public Piece copy() {
    Piece copy = new Pawn(new Point(this.position), this.color);
    copy.moves = this.moves;
    return copy;
  }
  
  @Override
  public void paint(Graphics g) {
    if(this.color == Color.WHITE)
      g.drawImage(Pawn.white, (this.position.x - 1) * 100, (this.position.y - 1) * 100, null);
    else
      g.drawImage(Pawn.black, (this.position.x - 1) * 100, (this.position.y - 1) * 100, null);
  }
}