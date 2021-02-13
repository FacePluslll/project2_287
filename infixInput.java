import java.util.Scanner;
import java.util.Stack;
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
			System.out.println(infixToPostFix(line));
		}
	}

	public static int checkIfOperator(char c){
		if(c == '+' || c == '-'){
			return 1;
		}
		else if(c == '*' || c == '/' || c == '%'){
			return 2;
		}
		else if(c == '^'){
			return 3;
		}
		else
			return -1;
	}

	// converts the string sent into post fix expression, also removes spaces and ()
	public static String infixToPostFix(String input){
		Stack<Character> stack = new Stack<>();
		StringBuilder sb = new StringBuilder();

		// loop the entire string
		for(int i = 0; i < input.length(); i++){
			char c = input.charAt(i);
			//skip if it is a space
			if(c == ' '){
				continue;
			}
			//check if the char is an operator
			if(checkIfOperator(c)>0){
				//will start to pop off from the stack and add to sb if the stack peek is greater then the current operator
				while(stack.isEmpty() == false && checkIfOperator(stack.peek())>= checkIfOperator(c)){
					sb.append(stack.pop());
				}
				//push the new operator to stack
				stack.push(c);
			}
			//special case of the ()
			//found the end of ()
			else if(c == ')'){
				//special case where the () has no operators in it (often seen with bool operations)
				if(stack.isEmpty()){
					continue;
				}
				//get last operator added
				char temp = stack.pop();
				while(temp != '('){
					//keep appending until stack finds the ( operator
					sb.append(temp);
					temp = stack.pop();
				}
			}
			//found start of () add to stack
			else if(c == '('){
				stack.push(c);
			}
			//append to sb if all conditions before it dont pass (also check for bool cases)
			else{
				sb.append(c);

				// if the next char is a comparsion operator then panic dump the stack to sb
				char temp2 = ' ';
				//its 2 and not 1 because of the extra char of due to space
				if(i+2<input.length()){
					int x = i + 2;
					temp2 = input.charAt(x);
				}
				else{
					temp2 = input.charAt(i);
				}
				if(temp2 == '&'|| temp2 == '=' || temp2 == '>'|| temp2 == '<'|| temp2 == '|'){
					// append the rest of the stack
					while(!stack.isEmpty()){
						if(stack.peek() != '(' && stack.peek() != ')'){
							sb.append(stack.pop());
						}
						//pop off () since we will no longer need it (often seen with bool operations)
						else if(stack.peek() == '(' || stack.peek() == ')'){
							stack.pop();
						}
					}
				}
			}
		}
		// append the rest of the stack
		while(!stack.isEmpty()){
			sb.append(stack.pop());
		}

		return sb.toString();
	}
}