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
	// iterate through the input file and get the first line(the line), call that
	// line line
	public static int getPredence(String A) { // sets the predence of the operators
		switch
		if (A == "||")
			return 1; // this would be a boolean, 1 is true
		else if (A == "&&")
			return 2; // this is boolean, would return 2 or 0 is false
		else if (A == "==" || A == "!=")
			return 3;// boolean? 1 is true
		else if (A == ">" || A == ">=" || A == "<" || A == "<=")
			return 4; // boolean? 1 is true
		else if (A == "+" || A == "-")
			return 5;
		else if (A == "*" || A == "/" || A == "%")
			return 6;
		else if (A == "^")
			return 7;
		return 0;
	} // change into a switch

	public static boolean isOperator(char a) {
		return (!(a >= 'a' && a <= 'z') && !(a >= '0' && a <= '9') && !(a >= 'A' && a <= 'Z'));
	}

	public static String lineToPostfix(String line) // accepts the line string
	{
		Stack<String> operators = new Stack<String>(); // make a stack that stores operators
		Stack<String> operands = new Stack<String>(); // stack that stores operands, like a and b

		for (int i = 0; i < line.length(); i++) {// iterator through and search for paranthesis
			if (line.charAt(i) == '(') {
				operators.push(line.substring(i, i + 1));// push paranthesis into the first stack
			} else if (line.charAt(i) == ')') {
				String temp="";
				while (!operators.empty() && operators.peek() != "(") {
					
					String operator = operators.pop(); // combines operands and operator into one expression

					temp += operator;
				}
				// removes remaining bracket from stack
				
				while(!operands.empty()){
					String operand1 = operands.pop();
					temp +=operand1;
				}
				operands.push(temp); // pushes paranthesis string into operands stack	
			} else if (!isOperator(line.charAt(i))) { // make isoperator method
				operands.push(line.charAt(i) + "");
			} else {
				String temp="";
				while (!operators.empty() && getPredence(line.substring(i, i + 1)) <= getPredence(operators.peek())) {

					String operator = operators.pop(); // combines operands and operator into one expression

					temp += operator;
				}
				while(!operands.empty()){
					String operand1 = operands.pop();
					temp +=operand1;
				}
				operands.push(temp);
				if (line.charAt(i) == '>' || line.charAt(i) == '<') {
					if (line.charAt(i + 1) == '=') {
						operators.push(line.substring(i, i + 2));
						i++;
					} else {
						operators.push(line.substring(i, i + 1));
					}
				} else if (line.charAt(i) == '=' || line.charAt(i) == '!') {
					if (line.charAt(i + 1) == '=') {
						operators.push(line.substring(i, i + 2));
						i++;
					}
				} else if (line.charAt(i) == '&' || line.charAt(i) == '|') {
					if (line.charAt(i) == line.charAt(i + 1)) {
						operators.push(line.substring(i, i + 2));
						i++;
					}
				} else {
					operators.push(line.substring(i, i + 1));
				}
				temp="";
				while(!operators.empty())
				{
					String operator = operators.pop(); // combines operands and operator into one expression

					temp += operator;
				}
				while(!operands.empty()){
					String operand1 = operands.pop();
					temp +=operand1;
				}
				operands.push(temp);
			}
			
		}
		System.out.println(operands);
		return operands.peek();
	}

}
