import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;

public class infixInput {
	public static void main(String[] args ) throws FileNotFoundException{
		ArrayList<String> inputs = new ArrayList<String>();
		inputs = readFile();

		for (int i = 0; i < inputs.size(); i++) {
			System.out.println(inputs.get(i).toString());
		}
	}

	public static ArrayList<String> readFile() throws FileNotFoundException{
		FileInputStream inputFile = new FileInputStream("input.txt");
		Scanner scnr = new Scanner(inputFile);

		ArrayList<String> input = new ArrayList<String>();

		String line = "";
		while (scnr.hasNext()) {
			line = scnr.nextLine().trim();
			input.add(line);
		}
		
		return input;
	}
}