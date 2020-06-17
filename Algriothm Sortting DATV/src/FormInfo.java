import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

public class FormInfo extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FormInfo frame = new FormInfo();
					frame.setVisible(true);
			        frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FormInfo() {
		setBounds(100, 100, 322, 257);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.menu);
		contentPane.setBorder(new TitledBorder(null, "!!!Infomation!!!", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		JTextArea txtInfo = new JTextArea();
		txtInfo.setEditable(false);
		txtInfo.setFont(new Font("Arial", Font.PLAIN, 14));
		txtInfo.setBackground(SystemColor.menu);
		txtInfo.setText("Software: Simulation Algorithm Sortting\r\nDiagram programming: Introductory Programming Simulation Algorithm!\r\nClass: 69DCTM21\r\nSemester: 2. School Years 2020 - 2021\r\nGVHD: ThS. Lương Hoàng Anh\r\nDesigner - DATV\r\n\tVũ Mạnh Đạt - 69DCTM20021\r\nEmail: vudat412@gmail.com\r\nGithub: https://github.com/vudast412/DATV-Algroithm.git\r\nThanks For Watching!!!");
		contentPane.add(txtInfo);
	}
}
