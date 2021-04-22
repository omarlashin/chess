package chess;
import java.awt.*;
import java.util.ArrayList;

public class Board {
  private Color boardColor;
  private final ArrayList<Piece> pieces;
  private Point selected;
  public static Color myColor;
  
  public Board(Color myColor) {
    this.boardColor = Color.WHITE;
    Board.myColor = myColor;
    this.pieces = new ArrayList<>(32);
    
    for(int i = 1; i < 9; i++)
      this.pieces.add(new Pawn(new Point(i, 7), Color.WHITE));
    this.pieces.add(new Rook(new Point(1, 8), Color.WHITE));
    this.pieces.add(new Rook(new Point(8, 8), Color.WHITE));
    this.pieces.add(new Knight(new Point(2, 8), Color.WHITE));
    this.pieces.add(new Knight(new Point(7, 8), Color.WHITE));
    this.pieces.add(new Bishop(new Point(3, 8), Color.WHITE));
    this.pieces.add(new Bishop(new Point(6, 8), Color.WHITE));
    this.pieces.add(new Queen(new Point(4, 8), Color.WHITE));
    this.pieces.add(new King(new Point(5, 8), Color.WHITE));
    
    for(int i = 1; i < 9; i++)
      this.pieces.add(new Pawn(new Point(i, 2), Color.BLACK));
    this.pieces.add(new Rook(new Point(1, 1), Color.BLACK));
    this.pieces.add(new Rook(new Point(8, 1), Color.BLACK));
    this.pieces.add(new Knight(new Point(2, 1), Color.BLACK));
    this.pieces.add(new Knight(new Point(7, 1), Color.BLACK));
    this.pieces.add(new Bishop(new Point(3, 1), Color.BLACK));
    this.pieces.add(new Bishop(new Point(6, 1), Color.BLACK));
    this.pieces.add(new Queen(new Point(4, 1), Color.BLACK));
    this.pieces.add(new King(new Point(5, 1), Color.BLACK));
  }
  
  public Board(Color boardColor, ArrayList<Piece> pieces) {
    this.boardColor = boardColor;
    this.pieces = pieces;
  }
  
  public Color getBoardColor() {
    return this.boardColor;
  }
  
  public void invert() {
    for(Piece piece : this.pieces) {
      piece.position.x = 9 - piece.position.x;
      piece.position.y = 9 - piece.position.y;
    }
  }
  
  public Piece getPiece(Point point) {
    for(Piece piece : this.pieces)
      if(piece.position.equals(point))
        return piece;
    return null;
  }
  
  public Board copy() {
    ArrayList<Piece> pieces = new ArrayList<>(this.pieces.size());
    for(Piece piece : this.pieces)
      pieces.add(piece.copy());
    return new Board(this.boardColor, pieces);
  }
  
  public boolean isChecked(Piece moved, Point newPosition, Point oldPosition, Piece eaten) {
    moved.position = newPosition;
    if(eaten != null)
      this.pieces.remove(eaten);
    
    for(Piece piece : this.pieces)
      if((piece.color != this.boardColor) && (piece.isChecking(this))) {
        moved.position = oldPosition;
        if(eaten != null)
          this.pieces.add(eaten);
        return true;
      }
    
    moved.position = oldPosition;
    if(eaten != null)
      this.pieces.add(eaten);
    return false;
  }
  
  public boolean isChecked() {
    for(Piece piece : this.pieces)
      if((piece.color != this.boardColor) && (piece.isChecking(this)))
        return true;
    
    return false;
  }
  
  public void change(Piece moved, Point newPosition) {
    Piece inPlace = this.getPiece(newPosition);
    if(inPlace != null)
      this.pieces.remove(inPlace);
    moved.position = newPosition;
    moved.moves++;
  }
  
  public void replace(Piece removed, Piece added) {
    this.pieces.remove(removed);
    this.pieces.add(added);
  }
  
  public void changeBoardColor() {
    if(this.boardColor == Color.WHITE)
      this.boardColor = Color.BLACK;
    else
      this.boardColor = Color.WHITE;
  }
  
  public Board getLeftCastlingBoard() {
    Piece king, rook;
    int kingLocation = (this.boardColor == Color.WHITE)? 5 : 4;
    king = this.getPiece(new Point(kingLocation, 8));
    rook = this.getPiece(new Point(1, 8));
    if((king == null) || (!(king instanceof King)) || (rook == null) || (!(rook instanceof Rook)))
      return null;
    if((king.getMoves() != 0) || (rook.getMoves() != 0))
      return null;
    
    for(int i = 2; i < kingLocation; i++)
      if(this.getPiece(new Point(i, 8)) != null)
        return null;
    
    if(this.isChecked())
      return null;
    
    for(int i = kingLocation - 1; i > kingLocation - 3; i--)
      if(this.isChecked(king, new Point(i, 8), king.position, null))
        return null;
    
    Board castlingBoard = this.copy();
    king = castlingBoard.getPiece(new Point(kingLocation, 8));
    rook = castlingBoard.getPiece(new Point(1, 8));
    castlingBoard.change(king, new Point(kingLocation - 2, 8));
    castlingBoard.change(rook, new Point(kingLocation - 1, 8));
    castlingBoard.changeBoardColor();
    
    return castlingBoard;
  }
  
  public Board getRightCastlingBoard() {
    Piece king, rook;
    int kingLocation = (this.boardColor == Color.WHITE)? 5 : 4;
    king = this.getPiece(new Point(kingLocation, 8));
    rook = this.getPiece(new Point(8, 8));
    if((king == null) || (!(king instanceof King)) || (rook == null) || (!(rook instanceof Rook)))
      return null;
    if((king.getMoves() != 0) || (rook.getMoves() != 0))
      return null;
    
    for(int i = kingLocation + 1; i < 8; i++)
      if(this.getPiece(new Point(i, 8)) != null)
        return null;
    
    if(this.isChecked())
      return null;
    
    for(int i = kingLocation + 1; i < kingLocation + 3; i++)
      if(this.isChecked(king, new Point(i, 8), king.position, null))
        return null;
    
    Board castlingBoard = this.copy();
    king = castlingBoard.getPiece(new Point(kingLocation, 8));
    rook = castlingBoard.getPiece(new Point(8, 8));
    castlingBoard.change(king, new Point(kingLocation + 2, 8));
    castlingBoard.change(rook, new Point(kingLocation + 1, 8));
    castlingBoard.changeBoardColor();
    
    return castlingBoard;
  }
  
  public ArrayList<Board> getChildren() {
    ArrayList<Board> boards = new ArrayList<>();
    ArrayList<Point> possibleMoves;
    Point location;
    Board checker = this.copy();
    Board child;
    
    for(Piece piece : this.pieces) {
      if(piece.color == this.boardColor) {
        location = new Point(piece.position);
        possibleMoves = checker.getPiece(location).getPossibleMoves(checker);
        for(Point point : possibleMoves) {
          if((piece instanceof Pawn) && (point.y == 8)) {
            Piece eaten;
            child = this.copy();
            child.pieces.remove(child.getPiece(location));
            eaten = child.getPiece(point);
            if(eaten != null)
              child.pieces.remove(eaten);
            child.pieces.add(new Rook(point, piece.color));
            child.changeBoardColor();
            boards.add(child);
            
            child = this.copy();
            child.pieces.remove(child.getPiece(location));
            eaten = child.getPiece(point);
            if(eaten != null)
              child.pieces.remove(eaten);
            child.pieces.add(new Knight(point, piece.color));
            child.changeBoardColor();
            boards.add(child);
            
            child = this.copy();
            child.pieces.remove(child.getPiece(location));
            eaten = child.getPiece(point);
            if(eaten != null)
              child.pieces.remove(eaten);
            child.pieces.add(new Bishop(point, piece.color));
            child.changeBoardColor();
            boards.add(child);
            
            child = this.copy();
            child.pieces.remove(child.getPiece(location));
            eaten = child.getPiece(point);
            if(eaten != null)
              child.pieces.remove(eaten);
            child.pieces.add(new Queen(point, piece.color));
            child.changeBoardColor();
            boards.add(child);
          }
          else {
            child = this.copy();
            child.change(child.getPiece(location), point);
            child.changeBoardColor();
            boards.add(child);
          }
        }
      }
    }
    
    Board leftCastlingBoard = this.getLeftCastlingBoard();
    Board rightCastlingBoard = this.getRightCastlingBoard();
    if(leftCastlingBoard != null)
      boards.add(leftCastlingBoard);
    if(rightCastlingBoard != null)
      boards.add(rightCastlingBoard);
    
    return boards;
  }
  
  public boolean isGameOver() {
    Board copy = this.copy();
    for(Piece piece : this.pieces)
      if((piece.color == this.boardColor) && (!copy.getPiece(piece.position).getPossibleMoves(copy).isEmpty()))
        return false;
    
    return true;
  }
  
  public int utility() {
    int balance = 0;
    int value = 0;
    Board temp;
    int myMobility;
    int opponentMobility;
    
    for(Piece piece : this.pieces) {
      if(piece instanceof Pawn)
        value = 1;
      else if((piece instanceof Knight) || (piece instanceof Bishop))
        value = 3;
      else if(piece instanceof Rook)
        value = 5;
      else if(piece instanceof Queen)
        value = 9;
      
      if(piece.color != Board.myColor)
        value *= -1;
      balance += value;
    }
    
    temp = this.copy();
    if(this.boardColor == Board.myColor){
      myMobility = temp.getChildren().size();
      temp.changeBoardColor();
      opponentMobility = temp.getChildren().size();
    }
    else{
      opponentMobility = temp.getChildren().size();
      temp.changeBoardColor();
      myMobility = temp.getChildren().size();
    }
    
    return (myMobility - opponentMobility) + (balance * 100);
  }
  
  public void paint(Graphics g) {
    for(int i = 0; i < 8; i++)
      for(int j = 0; j < 8; j++) {
        if((i + j) % 2 == 0)
          g.setColor(java.awt.Color.WHITE);
        else
          g.setColor(java.awt.Color.GRAY);
        g.fillRect(100 * j, 100 * i, 100, 100);
      }
    
    if(selected != null) {
      g.setColor(java.awt.Color.WHITE);
      g.fillRect((selected.x - 1) * 100, (selected.y - 1) * 100, 100, 100);
      g.setColor(new java.awt.Color(255, 255, 0, 80));
      g.fillRect((selected.x - 1) * 100, (selected.y - 1) * 100, 100, 100);
    }
    
    for(Piece piece : this.pieces)
      piece.paint(g);
  }
  
  public void select(Point selected) {
    this.selected = selected;
  }
  
  public void unselect() {
    this.selected = null;
  }
}