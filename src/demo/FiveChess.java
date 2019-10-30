package demo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public final class FiveChess extends JPanel implements Runnable {
    private static final long serialVersionUID = 4333473556657318990L;
    private final int width = 512, height = 512;
    private int $mouse_X, $mouse_Y;

    public FiveChess() {
        setPreferredSize(new Dimension(width, height));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(final MouseEvent e) {
                ChessBoard.downChess(e.getX(), e.getY(), 0, 0, width, height);
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(final MouseEvent e) {
                $mouse_X = e.getX();
                $mouse_Y = e.getY();
            }
        });
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(this);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(final String[] args) {
        new Thread(new FiveChess()).start();
    }

    @Override
    public void paint(final Graphics g) {
        super.paint(g);
        g.setColor(new Color(0, 0, 0));
        ChessBoard.drawChessBoard((Graphics2D) g);
        ChessBoard.drawChess((Graphics2D) g);
        g.setColor(new Color(0x0099ff));
        g.draw3DRect($mouse_X - 16, $mouse_Y - 16, 32, 32, false);
    }

    @Override
    public void repaint() {
        super.repaint();
    }

    @Override
    public void run() {
        while (true) {
            repaint();
        }
    }
}

class ChessBoard {
    /**
     * 行和列数
     */
    public static final int $column = 15, $row = 15;
    /**
     * 棋盘
     */
    public static final byte[][] chessboard = new byte[ChessBoard.$column + 1][ChessBoard.$row + 1];
    /**
     * 间距 = 2的$gap次方
     */
    public static final int $gap = 5, gap = 1 << ChessBoard.$gap,
            gap_helf = 1 << ChessBoard.$gap - 1;
    /**
     * 棋盘的顶点坐标
     */
    public static final int $location_x = 16, $location_y = 16;
    /**
     * 棋字大小
     */
    public static int w = 30, h = 30;
    /**
     * 阵营
     */
    public static byte camp = 1;
    /**
     * 颜色
     */
    public static Color color1 = new Color(0x000000);
    public static Color color2 = new Color(0xff0000);

    public static void drawChessBoard(Graphics2D g) {
        for (int i = 0; i <= ChessBoard.$column; i++) {
            g.drawString(Character.toString((char) ('A' + i)), 0,
                    ChessBoard.$location_y + (i << ChessBoard.$gap) + 4);
            g.drawLine(ChessBoard.$location_x, ChessBoard.$location_y
                            + (i << ChessBoard.$gap), ChessBoard.$location_x
                            + (ChessBoard.$row << ChessBoard.$gap),
                    ChessBoard.$location_y + (i << ChessBoard.$gap));// 横线
        }
        for (int i = 0; i <= ChessBoard.$row; i++) {
            g.drawString(Integer.toString(i + 1), ChessBoard.$location_x
                    + (i << ChessBoard.$gap) - 4, ChessBoard.$location_y);
            g.drawLine(ChessBoard.$location_x + (i << ChessBoard.$gap),
                    ChessBoard.$location_y, ChessBoard.$location_x
                            + (i << ChessBoard.$gap), ChessBoard.$location_y
                            + (ChessBoard.$column << ChessBoard.$gap));// 竖线
        }
    }

    public static void downChess(int mouse_x, int mouse_y, int start_x,
                                 int start_y, int end_x, int end_y) {
        if (mouse_x > start_x && mouse_x < end_x && mouse_y > start_y
                && mouse_x < end_y) {
            int x = mouse_x - ChessBoard.$location_x >> ChessBoard.$gap;
            final int residue_x = (mouse_x - ChessBoard.$location_x)
                    % ChessBoard.gap;
            int y = mouse_y - ChessBoard.$location_y >> ChessBoard.$gap;
            final int residue_y = (mouse_y - ChessBoard.$location_y)
                    % ChessBoard.gap;
            if (residue_x > ChessBoard.gap_helf) {
                if (residue_y > ChessBoard.gap_helf) {
                    x++;
                    y++;
                } else {
                    x++;
                }
            } else if (residue_y > ChessBoard.gap_helf) {
                y++;
            }
            System.out.println(x + "<-->" + y);
            if (ChessBoard.checkIndex(x, y) && ChessBoard.chessboard[y][x] == 0) {
                ChessBoard.chessboard[y][x] = ChessBoard.camp;
                if (ChessBoard.camp == 2) {
                    ChessBoard.camp = 1;
                } else {
                    ChessBoard.camp = 2;
                }
            }
            // 判断是否胜利
            if (ChessBoard.chessboard[y][x] != 0 & ChessBoard.checkWin(x, y, 5)) {
                System.out.println("胜利~~~");
            }
        }
    }

    public static boolean checkIndex(int x, int y) {
        return x >= 0 && y >= 0 && y < ChessBoard.chessboard.length
                && x < ChessBoard.chessboard[0].length;
    }

    // private static boolean checkWin(int x, int y, int wincase) {
    // boolean f1 = check2_8(x,y)>=wincase;
    // boolean f2 = check4_6(x,y)>=wincase;
    // boolean f3 = check3_7(x, y)>=wincase;
    // boolean f4 = check1_9(x, y)>=wincase;
    // System.out.println("1"+f1);
    // System.out.println("2"+f2);
    // System.out.println("3"+f3);
    // System.out.println("4"+f4);
    // return f1||f2||f3||f4;}
    private static boolean checkWin(int x, int y, int wincase) {// 检查是否5连子
        return ChessBoard.check2_8(x, y) >= wincase
                || ChessBoard.check4_6(x, y) >= wincase
                || ChessBoard.check3_7(x, y) >= wincase
                || ChessBoard.check1_9(x, y) >= wincase;
    }

    private static int check1_9(int x, int y) {
        int flag = 1;// 159斜角检测
        for (int temp_x = x, temp_y = y; ChessBoard.checkIndex(--temp_x,
                ++temp_y)
                && ChessBoard.chessboard[y][x] == ChessBoard.chessboard[temp_y][temp_x]; flag++) {
        }
        for (int temp_x = x, temp_y = y; ChessBoard.checkIndex(++temp_x,
                --temp_y)
                && ChessBoard.chessboard[y][x] == ChessBoard.chessboard[temp_y][temp_x]; flag++) {
        }
        return flag;
    }

    private static int check3_7(int x, int y) {
        int flag = 1;// 357斜角检测
        for (int temp_x = x, temp_y = y; ChessBoard.checkIndex(--temp_x,
                --temp_y)
                && ChessBoard.chessboard[y][x] == ChessBoard.chessboard[temp_y][temp_x]; flag++) {
        }
        for (int temp_x = x, temp_y = y; ChessBoard.checkIndex(++temp_x,
                ++temp_y)
                && ChessBoard.chessboard[y][x] == ChessBoard.chessboard[temp_y][temp_x]; flag++) {
        }
        return flag;
    }

    private static int check2_8(int x, int y) {
        int flag = 1;// 258竖直检测
        for (int temp_x = x, temp_y = y; ChessBoard
                .checkIndex(--temp_x, temp_y)
                && ChessBoard.chessboard[y][x] == ChessBoard.chessboard[temp_y][temp_x]; flag++) {
        }
        for (int temp_x = x, temp_y = y; ChessBoard
                .checkIndex(++temp_x, temp_y)
                && ChessBoard.chessboard[y][x] == ChessBoard.chessboard[temp_y][temp_x]; flag++) {
        }
        return flag;
    }

    private static int check4_6(int x, int y) {
        int flag = 1;// 456水平检测
        for (int temp_x = x, temp_y = y; ChessBoard
                .checkIndex(temp_x, --temp_y)
                && ChessBoard.chessboard[y][x] == ChessBoard.chessboard[temp_y][temp_x]; flag++) {
        }
        for (int temp_x = x, temp_y = y; ChessBoard
                .checkIndex(temp_x, ++temp_y)
                && ChessBoard.chessboard[y][x] == ChessBoard.chessboard[temp_y][temp_x]; flag++) {
        }
        return flag;
    }

    public static void drawChess(Graphics2D g) {
        for (int i = 0, len = ChessBoard.chessboard.length; i < len; i++) {
            for (int j = 0, length = ChessBoard.chessboard[i].length; j < length; j++) {
                switch (ChessBoard.chessboard[i][j]) {
                    case 0:
                        break;
                    case 1:
                        g.setColor(ChessBoard.color1);
                        g.fillOval(ChessBoard.$location_x
                                        + (j << ChessBoard.$gap) - (ChessBoard.w >> 1),
                                ChessBoard.$location_y + (i << ChessBoard.$gap)
                                        - (ChessBoard.h >> 1), ChessBoard.w,
                                ChessBoard.h);
                        break;
                    case 2:
                        g.setColor(ChessBoard.color2);
                        g.fillOval(ChessBoard.$location_x
                                        + (j << ChessBoard.$gap) - (ChessBoard.w >> 1),
                                ChessBoard.$location_y + (i << ChessBoard.$gap)
                                        - (ChessBoard.h >> 1), ChessBoard.w,
                                ChessBoard.h);
                        break;
                    default:
                        break;
                }
            }
        }
    }

}

class Chess {
    /**
     * 棋字坐标
     */
    private final int x, y;
    /**
     * 棋字大小
     */
    private final int w, h;
    /**
     * 阵营
     */
    private final byte camp;
    /**
     * 颜色
     */
    private Color color;

    /**
     * @param x    棋字坐标x
     * @param y    棋字坐标y
     * @param w    棋字大小w
     * @param h    棋字大小h
     * @param camp 阵营
     */
    public Chess(int x, int y, int w, int h, byte camp) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.camp = camp;
    }

    public Chess setColor(Color color) {
        this.color = color;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Chess [x=");
        builder.append(x);
        builder.append(", y=");
        builder.append(y);
        builder.append(", w=");
        builder.append(w);
        builder.append(", h=");
        builder.append(h);
        builder.append(", camp=");
        builder.append(camp);
        builder.append(", color=");
        builder.append(color);
        builder.append("]");
        return builder.toString();
    }

}