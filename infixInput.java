import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;

public class infixInput {
	public static void main(String[] args ) throws FileNotFoundException{
		FileInputStream inputFile = new FileInputStream("input.txt");
		Scanner scnr = new Scanner(inputFile);

		String line = "";
		while (scnr.hasNext()) {
			line = scnr.nextLine();
			System.out.println(line);
		}
	}
}