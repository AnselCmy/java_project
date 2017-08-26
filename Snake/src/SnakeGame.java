import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class SnakeGame extends JFrame {
    public static final int WIDTH = 800, HEIGHT = 600;
    GamePanel gamePanel = new GamePanel();

    public SnakeGame() {
        setLayout(null);
        setBounds(100, 100, WIDTH, HEIGHT);
        getContentPane().add(gamePanel);
        gamePanel.setFocusable(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String args[]) {
        new SnakeGame();
    }
}

class GamePanel extends JPanel {
    public static final int WIDTH = 600, HEIGHT = 600, SLEEPTIME = 200, L = 1,R = 2, U = 3, D = 4;
    int dir;
    SNode node;
    Snake snake = new Snake();
    public GamePanel() {
        dir = R;
        node = new SNode(30, 30, Color.blue);
        setLayout(null);
        setBounds(0, 0, WIDTH, HEIGHT);
        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent arg0) {
                switch (arg0.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        dir = L;
                        break;
                    case KeyEvent.VK_RIGHT:
                        dir = R;
                        break;
                    case KeyEvent.VK_UP:
                        dir = U;
                        break;
                    case KeyEvent.VK_DOWN:
                        dir = D;
                }
            }
        });
        new Thread(new ThreadUpadte()).start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // draw a rectangle border
        g.setColor(Color.black);
        g.drawRect(0, 0, WIDTH, HEIGHT);
        // draw it the food node
        node.draw(g);
        // move the snake and draw
        snake.move(dir);
        snake.draw(g);
        if(snake.detectEaten(node)) {
            snake.extend();
            node = getNewNode();
        }
    }

    public SNode getNewNode() {
        int x, y;
        random:
        while(true) {
            x = (int) (Math.random() * 600 / 15) * 15;
            y = (int) (Math.random() * 600 / 15) * 15;
            for (SNode tempNode : snake.snakeArray) {
                if (tempNode.x == x && tempNode.y == y) {
                    continue random;
                }
            }
            break;
        }
        SNode nextNode = new SNode(x, y, Color.blue);
        return nextNode;
    }

    class ThreadUpadte implements Runnable {
        public void run() {
            while (true) {
                repaint();
                try {
                    Thread.sleep(SLEEPTIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

class SNode {
    public static final int L = 1, R = 2, U = 3, D = 4;
    int x, y, width = 15, height = 15;
    Color color;

    public SNode(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(color);
        g2d.drawRect(x, y, width, height);
    }

    public void move(int dir) {
        switch (dir) {
            case U:
                y -= height;
                break;
            case D:
                y += height;
                break;
            case L:
                x -= width;
                break;
            case R:
                x += width;
        }
    }
}

class Snake {
    public List<SNode> snakeArray = new ArrayList<>();
    public static final int L = 1, R = 2, U = 3, D = 4;
    public static final Color snakeColor = Color.red;

    public Snake() {
        snakeArray.add(new SNode(150, 150, snakeColor));
        snakeArray.add(new SNode(165, 150, snakeColor));
        snakeArray.add(new SNode(170, 150, snakeColor));
    }

    public void draw(Graphics g) {
        for(SNode tempNode: snakeArray) {
            tempNode.draw(g);
        }
    }

    public void move(int dir) {
        snakeArray.remove(snakeArray.size()-1);
        SNode tempNode = new SNode(snakeArray.get(0).x, snakeArray.get(0).y, snakeColor);
        switch (dir) {
            case U:
                tempNode.y -= tempNode.height;
                break;
            case D:
                tempNode.y += tempNode.height;
                break;
            case L:
                tempNode.x -= tempNode.width;
                break;
            case R:
                tempNode.x += tempNode.width;
        }
        snakeArray.add(0, tempNode);
    }

    public boolean detectEaten(SNode foodNode) {
        if(snakeArray.get(0).x == foodNode.x && snakeArray.get(0).y == foodNode.y) {
            return true;
        }
        return false;
    }

    public void extend() {
        SNode last = snakeArray.get(snakeArray.size()-1);
        SNode secd = snakeArray.get(snakeArray.size()-2);
        int new_x = last.x;
        int new_y = last.y;
        int x_dir = secd.x - last.x;
        int y_dir = secd.y - last.y;
        if(x_dir > 0)
            new_x -= last.width;
        else if(x_dir < 0)
            new_x += last.width;
        else if(y_dir > 0)
            new_y -= last.height;
        else if(y_dir < 0)
            new_y += last.height;
        snakeArray.add(new SNode(new_x, new_y, snakeColor));
    }
}