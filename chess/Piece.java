package chess;
import java.awt.*;
import java.util.ArrayList;

public abstract class Piece {
  protected Point position;
  protected final Color color;
  protected int moves;
  
  public Piece(Point position, Color color) {
    this.position = position;
    this.color = color;
    this.moves = 0;
  }
  
  public int getMoves() {
    return this.moves;
  }
  
  public abstract ArrayList<Point> getPossibleMoves(Board board);
  
  public abstract boolean isChecking(Board board);
  
  public abstract Piece copy();
  
  public abstract void paint(Graphics g);
}