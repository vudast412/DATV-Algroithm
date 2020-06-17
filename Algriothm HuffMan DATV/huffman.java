// package Huffman;

import javax.swing.*;
import java.awt.event.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JFrame;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class huffman extends JFrame {

    JPanel panel;
    JLabel l1;
    JTextField t1;
    JButton b1, b2;

//Screen for taking the string input// 
    public huffman() {
        setTitle("STRING WINDOW");
        setLayout(new FlowLayout());
        setSize(1000, 500);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Để frame xuất hiện ở chính giữa màn hình

        panel = new JPanel(true);
        JPanel buttonsPanel = new JPanel();
        l1 = new JLabel("Nhập vào chuỗi ký tự");
        l1.setFont(new Font("Serif", Font.BOLD, 25));
        t1 = new JTextField(50);
        b1 = new JButton("Nhập");
        b2 = new JButton("Thoát");
        
        
        buttonsPanel.add(l1);
        buttonsPanel.add(t1);
        buttonsPanel.add(b1);
        buttonsPanel.add(b2);
        add(buttonsPanel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        b1.addActionListener(new mylistener());
        b2.addActionListener(new mylistener());
        panel.setVisible(true);
        setVisible(true);
    }

//Defining actions of the buttons when pressed//
    private class mylistener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == b2) {
                System.exit(0);
            } else {
                String s = t1.getText();
                Tree t = new Tree();

                NewWindow NW = new NewWindow(t.buildTree(s));
            }
        }
    }

    public static void main(String args[]) {

        huffman h = new huffman();
    }

}

// Node Class //
class Node implements Comparable<Node> {

    public char ch;
    public int freq;
    public Node left, right;
    public int count;

    Node(char ch, int freq, Node left, Node right, int count) {
        this.ch = ch;
        this.freq = freq;
        this.left = left;
        this.right = right;
        this.count = count;
    }

    private boolean isLeaf() {
        assert ((left == null) && (right == null)) || ((left != null) && (right != null));
        return (left == null) && (right == null);
    }

    public int compareTo(Node a) {

        if (freq < a.freq) {
            return -1;
        } else if (freq > a.freq) {
            return 1;
        } else {
            return 0;
        }
    }

    public void setcount(int c) {
        this.count = c;
    }
}

//Tree Class//
class Tree extends JFrame {

//counting frequency of every token in the string//
    public static int[] freqCount(String S) {

        int[] f = new int[10000];
        for (int i = 0; i < S.length(); i++) {

            f[S.charAt(i)]++;

        }

        return f;
    }

//building the huffman tree from a String S//
    public static Node buildTree(String S) {
        PriorityQueue<Node> queue = new PriorityQueue<Node>();
        int[] f = new int[10000];
        f = freqCount(S);
        for (char i = 0; i < f.length; i++) {
            if (f[i] > 0) {
                Node n = new Node(i, f[i], null, null, 0);
                queue.add(n);
            }
        }
        int c = 1;
        while (queue.size() > 1) {
            Node l1 = queue.poll();
            l1.setcount(c);
            c++;
            Node l2 = queue.poll();
            l2.setcount(c);
            c++;
            Node parent = new Node('\0', l1.freq + l2.freq, l1, l2, 0);
            queue.add(parent);
        }
        return queue.poll();
    }
}

class NewWindow {

    Node ab;
    ArrayList<Node> node = new ArrayList<Node>();
    public int width = 700; // this may be changed accodingly to fit the tree in the screen//
    public int height = 10;
    ArrayList<Integer> x = new ArrayList<Integer>(); //list of x coordinates of the nodes in the tree//
    ArrayList<Integer> y = new ArrayList<Integer>(); //list of y coordinates of the nodes in the tree//

    public int index(Node n) {
        int a = 0;
        for (int i = 0; i < node.size(); i++) {
            if (n == node.get(i)) {
                a = i;
                break;
            }
        }
        return a;
    }

//LevelFirstSearch traversal of the tree//
    public void bfs(Node root) {
        Queue<Node> q = new LinkedList<Node>();
        int c = 1;

        q.add(root);
        while (!q.isEmpty()) {
            Node n = (Node) q.remove();
            node.add(n);
            if (n.left != null) {
                q.add(n.left);
            }
            if (n.right != null) {
                q.add(n.right);
            }
        }
        for (int i = 0; i < node.size(); i++) {
            x.add(0);
            y.add(0);
        }
        x.set(0, width);
        y.set(0, height);

        for (int j = 0; j < node.size(); j++) {
            if (node.get(j).left != null && node.get(j).right != null) {
                x.set(index(node.get(j).left), x.get(j) - 25 * c);
                x.set(index(node.get(j).right), x.get(j) + 25 * c);

                y.set(index(node.get(j).left), y.get(j) + 100);
                y.set(index(node.get(j).right), y.get(j) + 100);
                c++;
            }

        }

        ab.setcount(node.size());
    }

//CREATING THE NEW WINDOW FOR TREE ANIMATION //
    public NewWindow(Node b) {

        ab = b;
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }

                JFrame frame = new JFrame("HUFFMAN TREE GENERATION");

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());

//ADDING TestPane() object //
                frame.add(new TestPane());
                frame.setLocationRelativeTo(null); // Để frame xuất hiện ở chính giữa màn hình
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Để frame hiển thị ra full màn hình
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

            }
        });
        bfs(ab);

    }

// TesPane Class ,animation done here //
    public final class TestPane extends JPanel {

        private int b = node.size();

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(2800, 2800);
        }

        public TestPane() {
            ActionListener animate = new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    repaint();
                    b--;
                }
            };
            Timer timer = new Timer(100, animate);
            timer.start();
            setBackground(Color.LIGHT_GRAY);
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g.setColor(Color.RED);

            for (int j = node.size() - 1; j >= 0; j--) {
                if (node.get(j).count < node.size() - b) {
                    g2d.setColor(Color.YELLOW);
                    // Thiết đặt độ lớn của vùng màu hiển trị trong cây
                    g2d.fillRect(x.get(j), y.get(j), 30, 30);

                    g2d.setColor(Color.BLUE);
                    // Thiết đặt độ lớn cho các ô vuông trong cây
                    g2d.drawRect(x.get(j), y.get(j), 30, 30);
                    g2d.setColor(Color.BLUE);
                    g2d.drawString(Integer.toString(node.get(j).freq), x.get(j) + 5, y.get(j) + 13);
                    g2d.drawString(Character.toString(node.get(j).ch), x.get(j) + 10, y.get(j) + 20);
                    g2d.setColor(Color.BLUE);
                    if ((node.get(j)).left != null) {
                        g2d.drawString("1", (x.get(j) + x.get(index(node.get(j).left))) / 2, (y.get(j) + y.get(index(node.get(j).left))) / 2);
                        g2d.drawLine(x.get(j) + 20, y.get(j) + 20, x.get(index(node.get(j).left)), y.get(index(node.get(j).left)));
                        g2d.setColor(Color.RED);

                    }
                    if ((node.get(j)).right != null) {
                        g2d.drawString("0", (x.get(j) + x.get(index(node.get(j).right))) / 2, (y.get(j) + y.get(index(node.get(j).right))) / 2);
                        g2d.drawLine(x.get(j) + 20, y.get(j) + 20, x.get(index(node.get(j).right)), y.get(index(node.get(j).right)));
                        g2d.setColor(Color.RED);
                    }
                }
            }

        }
    }
}