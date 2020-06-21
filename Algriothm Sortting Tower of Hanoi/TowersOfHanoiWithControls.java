import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * Chương trình này cho thấy một hình ảnh động của vấn đề nổi tiếng của Tháp Hà Nội, cho một đống
 * trong mười đĩa. Ba nút điều khiển cho phép người dùng điều khiển hoạt hình.
 * Nút "Tiếp theo" cho phép người dùng chỉ nhìn thấy một động tác trong giải pháp. Nhấp chuột
 * nút "Chạy" sẽ cho phép hoạt hình tự chạy; trong khi nó đang chạy
 * "Chạy" thay đổi thành "Tạm dừng" và nhấp vào nút sẽ tạm dừng hoạt hình.
 * Nút "Bắt đầu lại" cho phép người dùng khởi động lại sự cố ngay từ đầu.
 *
 * Chương trình là một ví dụ về việc sử dụng các phương thức Wait () và notify (). Các
 Phương thức * Wait () được sử dụng để tạm dừng hoạt ảnh giữa các lần di chuyển. Khi người dùng
 * nhấp vào "Tiếp theo" hoặc "Chạy", phương thức notify () được gọi để thông báo cho luồng
 * thức dậy và tiếp tục. Một biến "trạng thái" được sử dụng để giao tiếp các lệnh tới
 * chủ đề.
 *
 * Một thường trình chính () cho phép lớp này được chạy như một ứng dụng. Nếu nó được sử dụng
 * trong một applet, chiều rộng của applet phải là 430 và chiều cao phải là
 * 143 cộng với đủ không gian cho các nút ở dưới cùng của bảng.
 */
class TowersOfHanoiWithControls extends JPanel implements Runnable, ActionListener {

    /**
     *
     */
    private static final long serialVersionUID = 1L;


    /**
     *Một thường trình chính () để cho phép lớp này được chạy dưới dạng một ứng dụng độc lập 
     *. Chỉ cần mở một cửa sổ chứa bảng điều khiển loại 
     * TowersOfHano With Controls.
     */
    public static void main(String[] args) {
        JFrame window = new JFrame("Towers Of Hanoi");
        window.setContentPane(new TowersOfHanoiWithControls());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        window.setResizable(true);
        window.setLocation(300,200);
        window.setVisible(true);
    }
        // 4 màu được sử dụng
    private static Color BACKGROUND_COLOR = new Color(255,255,180); 
    private static Color BORDER_COLOR = new Color(100,0,0);
    private static Color DISK_COLOR = new Color(0,0,180);
    private static Color MOVE_DISK_COLOR = new Color(180,180,255);

    private BufferedImage OSC;   // Canvas ngoài màn hình. Khung được vẽ ở đây, sau đó sao chép vào màn hình.

    private int status;   // Kiểm soát việc thực hiện của các chủ đề; giá trị là một trong các hằng số sau.

    private static final int GO = 0;       //một giá trị cho trạng thái, có nghĩa là luồng là chạy liên tục
    private static final int PAUSE = 1;    // như trên nhưng ko chạy
    private static final int STEP = 2;     // chạy 1 bước rồi tạm dừng
    private static final int RESTART = 3;  // chạy lại từ đầu

   /*
        Các biến sau đây là dữ liệu cần thiết cho hình ảnh động. Các
       ba "cọc" của đĩa được biểu thị bằng tháp biến và
       tháp cao towerHeight [i] là số lượng đĩa trên cọc số i.
       Với i = 0,1,2 và với j = 0,1, ..., towerHeight [i] -1, tower [i] [j] là một số nguyên
       đại diện cho một trong mười đĩa. (Các đĩa được đánh số từ 1 đến 10.)

       Trong giải pháp, khi một đĩa được chuyển từ đống này sang cọc khác,
       biến moveDisk là số lượng đĩa đang được di chuyển,
       và moveTower là số lượng cọc hiện tại.
       Đĩa này không được lưu trong biến tháp. Nó được vẽ trong một
       màu sắc khác với các đĩa khác.
    */

    private int[][] tower;
    private int[] towerHeight;
    private int moveDisk;
    private int moveTower;

    private Display display;  // Một subpanel nơi các khung hình của hình ảnh động được hiển thị.

    // 3 nút điều khiển
    private JButton runPauseButton;  
    private JButton nextStepButton;
    private JButton startOverButton;


    /**
     * Lớp này định nghĩa bảng được sử dụng làm màn hình, để hiển thị
      * các khung hình của hình ảnh động. Phương thức paintComponent () trong này
      * lớp chỉ đơn giản là sao chép khung vẽ ngoài màn hình, OSC, vào màn hình.
      * Màn hình này sẽ được cung cấp kích thước ưa thích 430 x 143, trong đó
      * có cùng kích thước với khung vẽ. Nhưng để cho phép nhỏ
      * các biến thể từ kích thước này, OSC được vẽ ở giữa trên bảng.
     */
    private class Display extends JPanel {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int x = (getWidth() - OSC.getWidth())/2;
            int y = (getHeight() - OSC.getHeight())/2;
            g.drawImage(OSC, x, y, null);
        }
    }


    /**
     *  Tạo bảng điều khiển, chứa bảng hiển thị và bên dưới bảng điều khiển
      * một bảng phụ chứa ba nút điều khiển. Điều này
      * constructor cũng tạo canvas ngoài màn hình và tạo
      * và bắt đầu chuỗi hoạt hình.
     */
    public TowersOfHanoiWithControls () {
        OSC = new BufferedImage(430,143,BufferedImage.TYPE_INT_RGB);
        display = new Display();
        display.setPreferredSize(new Dimension(430,143));
        display.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 2));
        display.setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout());
        add(display, BorderLayout.CENTER);
        JPanel buttonBar = new JPanel();
        add(buttonBar, BorderLayout.SOUTH);
        buttonBar.setLayout(new GridLayout(1,0));
        runPauseButton = new JButton("Run");
        runPauseButton.addActionListener(this);
        buttonBar.add(runPauseButton);
        nextStepButton = new JButton("Next Step");
        nextStepButton.addActionListener(this);
        buttonBar.add(nextStepButton);
        startOverButton = new JButton("Start Over");
        startOverButton.addActionListener(this);
        startOverButton.setEnabled(false);
        buttonBar.add(startOverButton);
        new Thread(this).start();
    }


    /**
     * Phương pháp xử lý sự kiện cho các nút điều khiển. Thay đổi trong
      * giá trị của biến trạng thái sẽ được xem bởi luồng hoạt hình,
      * sẽ trả lời thích hợp.
     */
    synchronized public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();
        if (source == runPauseButton) {  // Toggle between running and paused.
            if (status == GO) {  // Animation is running.  Pause it.
                status = PAUSE;
                nextStepButton.setEnabled(true);
                runPauseButton.setText("Run");
            }
            else {  // animation dừng,  nắt đầu nó chạy
                status = GO;
                nextStepButton.setEnabled(false);  // Vô hiệu hóa khi hoạt hình đang chạy
                runPauseButton.setText("Pause");
            }
        }
        else if (source == nextStepButton) {  //Đặt trạng thái để làm cho hình ảnh động chạy một bước.
            status = STEP;
        }
        else if (source == startOverButton) { //Đặt trạng thái để làm cho hoạt hình khởi động lại.
            status = RESTART;
        }
        notify();  // Thức dậy các chủ đề để nó có thể thấy giá trị trạng thái mới!
    }


    /**
     * Phương thức run () cho luồng hoạt hình. Chạy trong một vòng lặp vô hạn.
      * Trong vòng lặp, luồng đầu tiên thiết lập trạng thái ban đầu của "tháp"
      * và của các nút. Điều này bao gồm đặt trạng thái thành TRẢ TIỀN và
      * gọi checkStatus (), sẽ không quay lại cho đến khi người dùng nhấp vào
      * Nút "Chạy" hoặc nút "Tiếp theo". Một khi điều này xảy ra, nó gọi
      * phương thức giải quyết () để chạy thuật toán đệ quy giải quyết
      * Vấn đề tháp Hà Nội. Trong giải pháp, checkStatus () là
      * được gọi sau mỗi lần di chuyển. Nếu người dùng nhấp vào nút "Bắt đầu lại",
      * checkStatus () sẽ ném IllegalStateException, điều này sẽ gây ra
      * phương thức giải quyết () bị hủy bỏ. Ngoại lệ được bắt để ngăn chặn
      * nó từ sự cố chủ đề.
     */
    public void run() {
        while (true) {
            runPauseButton.setText("Run");
            nextStepButton.setEnabled(true);
            startOverButton.setEnabled(false);
            setUpProblem();  // Thiết lập trạng thái ban đầu của nó
            status = PAUSE;
            checkStatus(); //Chỉ trả về khi người dùng đã nhấp vào "Chạy" hoặc "Tiếp theo"
            startOverButton.setEnabled(true);
            try {                       // ****** số lượng đĩa có thể được thay đổi từ đây xuống ****//
                solve(10,0,1,2);  // Di chuyển 10 đĩa từ cọc 0 sang cọc 1.
            }
            catch (IllegalStateException e) {
               // Ngoại lệ bị ném vì người dùng nhấp vào "Bắt đầu lại".
            }
        }
    }


    /**
     * Phương pháp này được gọi trước khi bắt đầu giải pháp và sau mỗi giải pháp
      * di chuyển của giải pháp. Nếu trạng thái là PAUSE, nó sẽ đợi cho đến khi
      * trạng thái thay đổi. Nếu trạng thái là RESTART, nó sẽ ném
      * một IllegalStateException sẽ hủy bỏ giải pháp.
      * Khi phương thức này trả về, giá trị của trạng thái phải là
      * CHẠY BƯỚC BƯỚC.
      * (Lưu ý rằng phương pháp này yêu cầu đồng bộ hóa, vì
      * nếu không, gọi Wait () sẽ tạo ra IllegalMonitorStateException.
      * Tuy nhiên, trên thực tế, nó chỉ được gọi từ các phương thức được đồng bộ hóa khác,
      * vì vậy không cần thiết phải khai báo phương thức này được đồng bộ hóa.
      * Bất kỳ phương thức nào gọi nó đã sở hữu khóa đồng bộ hóa.)
     */
    synchronized private void checkStatus() {
        while (status == PAUSE) {
            try {
                wait();
            }
            catch (InterruptedException e) {
            }
        }
      // Tại thời điểm này, trạng thái là CHẠY, BƯỚC hoặc RESTART.
        if (status == RESTART)
            throw new IllegalStateException("Restart");
      // Tại thời điểm này, trạng thái là CHẠY hoặc BƯỚC.
    }


    /**
     * Thiết lập trạng thái ban đầu của câu đố Towers Of Hà Nội, với
      * tất cả các đĩa trên cọc đầu tiên.
     */
    synchronized private void setUpProblem() {
        moveDisk= 0;
        tower = new int[3][10];
        for (int i = 0; i < 10; i++)
            tower[0][i] = 10 - i;
        towerHeight = new int[10];
        towerHeight[0] = 10;     //xuống đây
        if (OSC != null) {
            Graphics g = OSC.getGraphics();
            drawCurrentFrame(g);
            g.dispose();
        }
        display.repaint();
    }


    /**
     * Giải quyết vấn đề TowersOfHanoi để di chuyển chỉ định
      * số lượng đĩa từ đống này sang cọc khác.
     * @param disks số đĩa di chuyển
     * @param from      số lượng cọc nơi các đĩa hiện nay
     * @param to số lượng cọc mà các đĩa sẽ được di chuyển.
     * @param spare số lượng cọc có thể được sử dụng như một phụ tùng.
     */
    private void solve(int disks, int from, int to, int spare) {
        if (disks == 1)
            moveOne(from,to);
        else {
            solve(disks-1, from, spare, to);
            moveOne(from,to);
            solve(disks-1, spare, to, from);
        }
    }


    /**
     * Di chuyển đĩa ở đầu số cọc từStack sang
      * đầu số cọc toStack. (Đĩa thay đổi thành
      * một màu mới, sau đó di chuyển, sau đó thay đổi trở lại tiêu chuẩn
      * color.) Phương thức delay () được gọi để chèn một số ngắn
      * sự chậm trễ vào hình ảnh động. Sau khi di chuyển, nếu giá trị của
      * trạng thái là BƯỚC, chỉ ra rằng chỉ có một bước là
      * được thực hiện trước khi tạm dừng, sau đó giá trị của TÌNH TRẠNG được thay đổi
      * để tạm dừng. Trong mọi trường hợp, ở cuối phương thức,
      * Phương thức checkStatus () được gọi.
     */
    synchronized private void moveOne(int fromStack, int toStack) {
        moveDisk = tower[fromStack][towerHeight[fromStack]-1];
        moveTower = fromStack;
        delay(120);
        towerHeight[fromStack]--;
        putDisk(MOVE_DISK_COLOR,moveDisk,moveTower);
        delay(80);
        putDisk(BACKGROUND_COLOR,moveDisk,moveTower);
        delay(80);
        moveTower = toStack;
        putDisk(MOVE_DISK_COLOR,moveDisk,moveTower);
        delay(80);
        putDisk(DISK_COLOR,moveDisk,moveTower);
        tower[toStack][towerHeight[toStack]] = moveDisk;
        towerHeight[toStack]++;
        moveDisk = 0;
        if (status == STEP)
            status = PAUSE;
        checkStatus();
    }


    /**
    * Phương pháp tiện ích đơn giản để chèn độ trễ của một chỉ định
      * số mili giây.
     */
    synchronized private void delay(int milliseconds) {
        try {
            wait(milliseconds);
        }
        catch (InterruptedException e) {
        }
    }


    /**
     * Vẽ một đĩa được chỉ định vào khung vẽ ngoài màn hình. Đây là
      * chỉ được sử dụng trong phương thức moveOne () để vẽ đĩa
      * đang được di chuyển. Gọi display.repaint () để vẽ lại
      * hiển thị bằng hình ảnh mới được sửa đổi.
     * @param color màu của đĩa (sử dụng màu nền để xóa).
     * @param disk số lượng đĩa sẽ được rút ra, từ 1 đến 10.
     * @param tsố lượng cọc trên đỉnh của đĩa được rút ra.
     */
    private void putDisk(Color color, int disk, int t) {
        Graphics g = OSC.getGraphics();
        g.setColor(color);
        g.fillRoundRect(75+140*t - 5*disk - 5, 116-12*towerHeight[t], 10*disk+10, 10, 10, 10);
        g.dispose();
        display.repaint();
    }


    /**
     *Được gọi để vẽ khung hiện tại, không bao gồm đĩa di chuyển,
      * nếu có, được vẽ như là một phần của phương thức moveOne ().
     */
    synchronized private void drawCurrentFrame(Graphics g) {
        //Được gọi để vẽ khung hiện tại. Nhưng nó không được rút ra trong
         // hình ảnh động của giải pháp. Trong hoạt hình,
        // Phương thức // moveDisk () chỉ sửa đổi hình ảnh hiện có.
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0,0,430,143);
        g.setColor(BORDER_COLOR);
        if (tower == null)
            return;
        g.fillRect(10,128,130,5);
        g.fillRect(150,128,130,5);
        g.fillRect(290,128,130,5);
        g.setColor(DISK_COLOR);
        for (int t = 0; t < 3; t++) {
            for (int i = 0; i < towerHeight[t]; i++) {
                int disk = tower[t][i];
                g.fillRoundRect(75+140*t - 5*disk - 5, 116-12*i, 10*disk+10, 10, 10, 10);
            }
        }
        if (moveDisk > 0) {
            g.setColor(MOVE_DISK_COLOR);
            g.fillRoundRect(75+140*moveTower - 5*moveDisk - 5, 116-12*towerHeight[moveTower],
                    10*moveDisk+10, 10, 10, 10);
        }
    }



} /// lớp kết thúc TowersOfHanoiWithControls