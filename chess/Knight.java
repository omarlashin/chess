package chess;
import java.awt.*;
import java.util.ArrayList;

public class Knight extends Piece {
  private static Image white;
  private static Image black;
  
  static {
    Knight.white = (new javax.swing.ImageIcon(Pawn.class.getResource("resources/white-knight.png"))).getImage();
    Knight.black = (new javax.swing.ImageIcon(Pawn.class.getResource("resources/black-knight.png"))).getImage();
  }
  
  private static final Point[] TRANSLATIONS = {
    new Point(1, -2), new Point(2, -1), new Point(2, 1), new Point(1, 2), new Point(-1, 2), new Point(-2, 1), new Point(-2, -1), new Point(-1, -2)
  };
  
  public Knight(Point position, Color color) {
    super(position, color);
  }
  
  @Override
  public ArrayList<Point> getPossibleMoves(Board board) {
    ArrayList<Point> possibleMoves = new ArrayList<>();
    Point point;
    boolean withinRange;
    Piece inPlace;
    
    for(Point translation : Knight.TRANSLATIONS) {
      point = new Point(this.position);
      point.translate(translation.x, translation.y);
      withinRange = (point.x > 0 && point.x < 9) && (point.y > 0 && point.y < 9);
      if(!withinRange)
        continue;
      inPlace = board.getPiece(point);
      if(inPlace == null) {
        if(!(board.isChecked(this, point, this.position, inPlace)))
          possibleMoves.add(point);
      }
      else {
        if((inPlace.color != this.color) && !(inPlace instanceof King) && !(board.isChecked(this, point, this.position, inPlace)))
          possibleMoves.add(point);
      }
    }
    
    return possibleMoves;
  }
  
  @Override
  public boolean isChecking(Board board) {
    Point point;
    boolean withinRange;
    Piece inPlace;
    
    for(Point translation : Knight.TRANSLATIONS) {
      point = new Point(this.position);
      point.translate(translation.x, translation.y);
      withinRange = (point.x > 0 && point.x < 9) && (point.y > 0 && point.y < 9);
      inPlace = board.getPiece(point);
      if(withinRange && (inPlace != null) && (inPlace.color != this.color) && (inPlace instanceof King))
        return true;
    }
    
    return false;
  }
  
  @Override
  public Piece copy() {
    Piece copy = new Knight(new Point(this.position), this.color);
    copy.moves = this.moves;
    return copy;
  }
  
  @Override
  public void paint(Graphics g) {
    if(this.color == Color.WHITE)
      g.drawImage(Knight.white, (this.position.x - 1) * 100, (this.position.y - 1) * 100, null);
    else
      g.drawImage(Knight.black, (this.position.x - 1) * 100, (this.position.y - 1) * 100, null);
  }
}