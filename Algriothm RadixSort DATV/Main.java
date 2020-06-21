import java.util.ArrayList;
import java.util.Collections;

public class Main {
    private static Thread sortingThread;

	public static FrameGUI frame;
	public static Integer[] toBeSorted;
	public static int sortDataCount = 50;
	public static int sleep = 10;
	public static int blockWidth;
	
	public static boolean stepped = false;

    private static boolean isSorting = false;
    public static void main(String[] args) {
        frame = new FrameGUI();
		resetArray();
		frame.setLocationRelativeTo(null);
    }

    public static void resetArray(){
		// Nếu chúng hiện đang trong một phương thức sắp xếp, thì isSorting phải là true
		// Chúng ta không muốn sắp xếp lại / đặt lại mảng mid sort.
		if (isSorting) return;
		toBeSorted = new Integer[sortDataCount];
		blockWidth = 15; //(int) Math.max(Math.floor(500/sortDataCount), 10);
		for(int i = 0; i<toBeSorted.length; i++){
			if (stepped) {
				toBeSorted[i] = i;
			} else {
				toBeSorted[i] = (int) (sortDataCount*Math.random());
			} 
		}
		// Nếu chúng ta đang sử dụng các giá trị gia tăng, chúng đã được sắp xếp. Điều này xáo trộn nó.
		if (stepped) {
			ArrayList<Integer> shuffleThis = new ArrayList<>();
			for (int i = 0; i < toBeSorted.length; i++) {
				shuffleThis.add(toBeSorted[i]);
			}
			Collections.shuffle(shuffleThis);
			toBeSorted = shuffleThis.toArray(toBeSorted);
		}
		frame.preDrawArray(toBeSorted);
	}
	
	public static void startSort(String type){
		
		if (sortingThread == null || !isSorting){
			
			resetArray();
			
			isSorting = true;

			switch(type){			

			case "Radix LSD": 
				sortingThread = new Thread(new RadixSort(toBeSorted, frame, true));
				break;
				
			case "Radix MSD":
				sortingThread = new Thread(new RadixSort(toBeSorted, frame, false));
				break;
				
			default:
				isSorting = false;
				return;
			}
			
			sortingThread.start();
			
		}
		
	}

    public static void setIsSorting(boolean b) {
        isSorting = b; 
    }
}