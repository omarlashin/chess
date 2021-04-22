package chess;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ChessFrame extends JFrame implements ActionListener {
  private ChessPanel pnlChess;
  private JMenuBar mnb;
  private JMenu mnuGame;
  private JMenuItem mniNew;
  private JMenuItem mniExit;
  
  public ChessFrame() {
    this.init();
  }
  
  private void init() {
    try {
      UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    }
    catch(Exception ex){
    }
    
    String color = (String)JOptionPane.showInputDialog(null, "Select your side", "Ready!", JOptionPane.QUESTION_MESSAGE, null, new Object[]{"White", "Black"}, "White");
    if(color == null)
      System.exit(0);
    String difficulty = (String)JOptionPane.showInputDialog(null, "Select difficulty level", "Ready!", JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Beginner", "Intermediate", "Expert"}, "Beginner");
    if(difficulty == null)
      System.exit(0);
    
    Color myColor;
    int depth;
    if(color.equals("White"))
      myColor = Color.BLACK;
    else
      myColor = Color.WHITE;
    switch(difficulty) {
      case "Beginner":
        depth = 1;
        break;
      case "Intermediate":
        depth = 2;
        break;
      default:
        depth = 3;
    }
    
    this.pnlChess = new ChessPanel(myColor, depth);
    this.setTitle("Chess 2.0");
    this.setResizable(false);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.add(this.pnlChess);
    this.mnb = new JMenuBar();
    this.mnuGame = new JMenu("Game");
    this.mniNew = new JMenuItem("New game");
    this.mniExit = new JMenuItem("Exit");
    this.mniNew.addActionListener(this);
    this.mniExit.addActionListener(this);
    this.mnuGame.add(this.mniNew);
    this.mnuGame.add(this.mniExit);
    this.mnb.add(mnuGame);
    this.setJMenuBar(this.mnb);
    this.pack();
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    this.setLocation((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight()) / 2);
  }
  
  @Override
  public void actionPerformed(ActionEvent evt) {
    Object source = evt.getSource();
    if(source == this.mniNew) {
      this.setVisible(false);
      this.dispose();
      new ChessFrame().setVisible(true);
    }
    else if(source == this.mniExit)
      System.exit(0);
  }
}

class ChessPanel extends JPanel {
  private Board board;
  private final Color myColor;
  private final int depth;
  private final Engine engine;
  private boolean myTurn;
  private boolean playing;
  private Piece selected;
  
  public ChessPanel(Color myColor, int depth) {
    this.setPreferredSize(new Dimension(800, 800));
    this.myColor = myColor;
    this.depth = depth;
    this.board = new Board(this.myColor);
    this.engine = new Engine(this.depth);
    this.myTurn = false;
    this.playing = true;
    if(this.myColor == Color.WHITE) {
      this.board.invert();
      this.myTurn = true;
      this.play();
    }
    
    this.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent evt) {
        if(playing && (!myTurn) && (evt.getButton() == 1)) {
          int x = (int)Math.ceil(evt.getX() / 100) + 1;
          int y = (int)Math.ceil(evt.getY() / 100) + 1;
          Point target = new Point(x, y);
          
          if(selected != null) {
            boolean isValid = false;
            Board copy = board.copy();
            for(Point point : copy.getPiece(selected.position).getPossibleMoves(copy))
              if(target.equals(point)) {
                isValid = true;
                break;
              }
            
            if(isValid) {
              board.change(selected, target);
              board.changeBoardColor();
              board.unselect();
              repaint();
              if((selected instanceof Pawn) && (target.y == 1)) {
                String choice = (String)JOptionPane.showInputDialog(null, "Select your new rank", "Promotion", JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Rook", "Knight", "Bishop", "Queen"}, "Rook");
                if(choice == null)
                  choice = "Rook";
                Piece promoted;
                
                switch(choice) {
                  case "Rook":
                    promoted = new Rook(new Point(target), selected.color);
                    break;
                  case "Knight":
                    promoted = new Knight(new Point(target), selected.color);
                    break;
                  case "Bishop":
                    promoted = new Bishop(new Point(target), selected.color);
                    break;
                  default:
                    promoted = new Queen(new Point(target), selected.color);
                }
                promoted.moves = selected.moves;
                board.replace(selected, promoted);
                repaint();
              }
              selected = null;
              myTurn = true;
              play();
            }
            else {
              Piece king = copy.getPiece(selected.position);
              Piece rook = copy.getPiece(target);
              if((king != null) && (king instanceof King) && (king.color != myColor) && (rook != null) && (rook instanceof Rook) && (rook.color != myColor)) {
                Board castledBoard;
                if(king.position.x > rook.position.x)
                  castledBoard = copy.getLeftCastlingBoard();
                else
                  castledBoard = copy.getRightCastlingBoard();
                
                if(castledBoard != null) {
                  board = castledBoard;
                  selected = null;
                  board.unselect();
                  repaint();
                  myTurn = true;
                  play();
                }
                else {
                  selected = null;
                  board.unselect();
                  repaint();
                }
              }
              else {
                selected = null;
                board.unselect();
                repaint();
              }
            }
          }
          else {
            selected = board.getPiece(target);
            if(selected != null) {
              if(selected.color != myColor)
                board.select(target);
              else
                selected = null;
              repaint();
            }
          }
        }
      }
    });
  }
  
  @Override
  public void paint(Graphics g) {
    super.paint(g);
    this.board.paint(g);
  }
  
  public void play() {
    new Thread() {
      @Override
      public void run() {
        if(playing && myTurn) {
          Board trialBoard = board.copy();
          if(trialBoard.isGameOver()) {
            JOptionPane.showMessageDialog(null, "You Won!", "Game Over!", JOptionPane.PLAIN_MESSAGE);
            playing = false;
            return;
          }
          
          engine.minimax(trialBoard, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
          trialBoard = engine.getSelectedBoard();
          board = trialBoard;
          repaint();
          
          if(board.isGameOver()) {
            JOptionPane.showMessageDialog(null, "You Lost!", "Game Over!", JOptionPane.PLAIN_MESSAGE);
            playing = false;
          }
          myTurn = false;
        }
      }
    }.start();
  }
}