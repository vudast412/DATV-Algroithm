import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class FrameGUI extends JFrame {
    private final int MAX_SPEED = 500;
	private final int MIN_SPEED = 1;
	private final int MAX_SIZE = 100;
	private final int MIN_SIZE = 10;
	private final int DEFAULT_SPEED = 10;
	private final int DEFAULT_SIZE = 50;
	

	private final String[] Sorts = {"Radix LSD", "Radix MSD"};
	private int sizeModifier;

	private JPanel wrapper;
	private JPanel arrayWrapper;
	private JPanel buttonWrapper;
	private JPanel[] squarePanels;
	private JButton start;
	private JComboBox<String> selection;
	private JSlider speed;
	private JSlider size;
	private JLabel speedVal;
	private JLabel sizeVal;
	private GridBagConstraints c;
	private JCheckBox stepped;
	
	public FrameGUI() {
		super("Sorting Visualizer");
		
		start = new JButton("Start");
		buttonWrapper = new JPanel();
		arrayWrapper = new JPanel();
		wrapper = new JPanel();
		selection = new JComboBox<String>();
		speed = new JSlider(MIN_SPEED, MAX_SPEED, DEFAULT_SPEED);
		size = new JSlider(MIN_SIZE, MAX_SIZE, DEFAULT_SIZE);
		speedVal = new JLabel("Speed: 10 ms");
		sizeVal = new JLabel("Size: 50 values");
		stepped = new JCheckBox("Stepped Values");
		c = new GridBagConstraints();
		
		for(String s : Sorts) selection.addItem(s);
		
		arrayWrapper.setLayout(new GridBagLayout());
		wrapper.setLayout(new BorderLayout());

		c.insets = new Insets(0,1,0,1);
		c.anchor = GridBagConstraints.SOUTH;
		
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.startSort((String) selection.getSelectedItem());
			}
		});
		
		stepped.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.stepped = stepped.isSelected();
			}
		});
		
		speed.setMinorTickSpacing(10);
		speed.setMajorTickSpacing(100);
		speed.setPaintTicks(true);
		
		speed.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				speedVal.setText(("Speed: " + Integer.toString(speed.getValue()) + "ms"));
				validate();
				Main.sleep = speed.getValue();
			}
		});
		
		size.setMinorTickSpacing(2);
		size.setMajorTickSpacing(10);
		size.setPaintTicks(true);
		
		size.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				sizeVal.setText(("Size: " + Integer.toString(size.getValue()) + " values"));
				validate();
				Main.sortDataCount = size.getValue();
			}
		});

		buttonWrapper.add(stepped);
		buttonWrapper.add(speedVal);
		buttonWrapper.add(speed);
		buttonWrapper.add(sizeVal);
		buttonWrapper.add(size);
		buttonWrapper.add(start);
		buttonWrapper.add(selection);
		
		wrapper.add(buttonWrapper, BorderLayout.SOUTH);
		wrapper.add(arrayWrapper);
		wrapper.setBackground(Color.blue);
		
		add(wrapper);
		

		setExtendedState(JFrame.MAXIMIZED_BOTH );
		
		addComponentListener(new ComponentListener() {

			@Override
			public void componentResized(ComponentEvent e) {
				// Reset the sizeModifier
				// 90% of the windows height, divided by the size of the sorted array.
				sizeModifier = (int) ((getHeight()*0.8)/(squarePanels.length));
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// Do nothing
			}

			@Override
			public void componentShown(ComponentEvent e) {
				// Do nothing
			}

			@Override
			public void componentHidden(ComponentEvent e) {
				// Do nothing
			}
			
		});
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	
	// preDrawArray reinitializes the array of panels that represent the values. They are set based on the size of the window.
	public void preDrawArray(Integer[] squares){
		squarePanels = new JPanel[Main.sortDataCount];
		arrayWrapper.removeAll();
		// 90% of the windows height, divided by the size of the sorted array.
		sizeModifier =  (int) ((getHeight()*0.8)/(squarePanels.length));
		for(int i = 0; i<Main.sortDataCount; i++){
			squarePanels[i] = new JPanel();
			squarePanels[i].setPreferredSize(new Dimension(Main.blockWidth, squares[i]*sizeModifier));
			squarePanels[i].setBackground(Color.darkGray);
			arrayWrapper.add(squarePanels[i], c);
		}
		repaint();
		validate();
	}
	
	public void reDrawArray(Integer[] x){
		reDrawArray(x, -1, 0);
	}
	
	public void reDrawArray(Integer[] x, int y, int yNum){
		reDrawArray(x, y, yNum, -1, 0);
	}
	
	public void reDrawArray(Integer[] x, int y, int yNum, int z, int zNum){
		reDrawArray(x, y, yNum, z, zNum, -1, 0);
	}
	
	// reDrawArray does similar to preDrawArray except it does not reinitialize the panel array.
	public void reDrawArray(Integer[] squares, int working, int workingNum, int comparing, int comparingNum, int reading, int readingNum){
		arrayWrapper.removeAll();
		for(int i = 0; i<squarePanels.length; i++){
			squarePanels[i] = new JPanel();
			LineBorder line = new LineBorder(Color.darkGray, 1, true); // color, thickness, rounded
			squarePanels[i].setBorder(line);
			JLabel num1 = new JLabel(Integer.toString(workingNum));
			JLabel num2 = new JLabel(Integer.toString(comparingNum));
			JLabel num3 = new JLabel(Integer.toString(readingNum));
			squarePanels[i].setPreferredSize(new Dimension(Main.blockWidth, squares[i]*sizeModifier));
			if (i == working){
				squarePanels[i].setBackground(Color.green);	
				squarePanels[i].add(num1);
			}else if(i == comparing){
				squarePanels[i].setBackground(Color.red);	
				squarePanels[i].add(num2);
			}else if(i == reading){
				squarePanels[i].setBackground(Color.cyan);
				squarePanels[i].add(num3);
			}else{
				squarePanels[i].setBackground(Color.darkGray);
			}
			arrayWrapper.add(squarePanels[i], c);
		}
		repaint();
		validate();
	}
}