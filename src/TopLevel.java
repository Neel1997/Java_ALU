import java.io.File;
import java.util.Scanner;

public class TopLevel {

    public static void main(String [] args) {
		Scanner sc;
		String temp;
		int[] line = new int[32];
		int numInstructions = 0;
    	Processor processor = new Processor();

    	try {
			sc = new Scanner(new File(args[0]));

			for(int i = 0; sc.hasNextLine(); i++) {
				temp = sc.nextLine();
				temp = temp.replaceAll("\\s", ""); // removes white spaces
				System.out.println(temp); // prints to make sure input values are correct
				line[i] = Integer.parseInt(temp, 2);
				numInstructions++;
			}
			System.out.println();
			processor.run(line, numInstructions);

			sc.close();
		} catch (Exception e) {
    		System.err.println("Could not execute from file");
    		e.printStackTrace();
		}
    }
}
