import java.util.Scanner;
import java.util.Stack;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class infixInput {
	public static boolean isBoolExpression = false;
	public static void main(String[] args ) throws FileNotFoundException{
		FileInputStream inputFile = new FileInputStream("input.txt");
		Scanner scnr = new Scanner(inputFile);
		

		String line = "";
		while (scnr.hasNext()) {
			line = scnr.nextLine();
			
			System.out.println(line);
			System.out.println(infixToPostFix(line));
			System.out.println(comparator(infixToPostFix(line)));
			System.out.println(" ");
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
					isBoolExpression = true;
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
	public static int comparator(String line) {
		if (isBoolExpression) {
			/*
			This program parses the input strng at the operator then each side of the operator is evaluated and stored in a variable as an integer.
			Following this evaluation is the comparison that correlates to the operator.
			*/

			int returnNum = 0;
			int result = 0;
			int givNum = 3; 
			int givNum2 = 3;
			String givCom = "<=";

			Queue<String> queue = new LinkedList<String>();
			ArrayList<StringBuilder> arr = new ArrayList<StringBuilder>();
			StringBuilder booloperator = new StringBuilder();
			StringBuilder str = new StringBuilder();
			boolean beenAdded = false;

			for (int i = 0; i < line.length(); i ++) {
				if(line.charAt(i) == '>' || line.charAt(i) == '<'|| line.charAt(i) == '|'|| line.charAt(i) == '&'|| line.charAt(i) == '=' || line.charAt(i) == '!'){
					booloperator.append(line.charAt(i));
					if(line.charAt(i+1) == '=' || line.charAt(i+1) == '|'|| line.charAt(i+1) == '&'){
						booloperator.append(line.charAt(i+1));
						i++;
					}
					queue.add(booloperator.toString());
					booloperator = new StringBuilder();
					if(!beenAdded && str.length()!=0){
						arr.add(str);
					}
					str = new StringBuilder();
					beenAdded = false;
				}
				else{
					str.append(line.charAt(i));
					if(!beenAdded){
						arr.add(str);
						beenAdded = true;
					}
				}
			}

			for (int i = 0; i < arr.size(); i++) {
				//System.out.println(arr.get(i).toString());
				System.out.println("Array value: " + arr.get(i).toString()+" Test case: "+evluatePostFix(arr.get(i)));

				givCom = queue.poll(); //if givCom -- && then givNum = returnNum
				givNum = evluatePostFix(arr.get(i));
				if (i < arr.size() - 1) {
					i++;
					givNum2 = evluatePostFix(arr.get(i));
				}

				if (givCom.equals(">")) {
					if (givNum > givNum2) {returnNum = 1;}
					else {returnNum = 0;}
				}
				else if (givCom.equals(">=")) {
					if (givNum >= givNum2) {returnNum = 1;}
					else {returnNum = 0;}
				}
				else if (givCom.equals("<")) {
					if (givNum < givNum2) {returnNum = 1;}
					else{returnNum = 0;}
				}
				else if (givCom.equals("<=")) {
					if (givNum <= givNum2){returnNum = 1;}
					else{returnNum = 0;}
				}
				else if (givCom.equals("==")) {
					if (givNum == givNum2) {returnNum = 1;}
					else {returnNum = 0;}
				}
				else if (givCom.equals("!=")) {
					if (givNum != givNum2) {returnNum = 1;}
					else {returnNum = 0;}
				}
				else if (givCom.equals("&&")) {
					givNum = returnNum;
					givNum2 = givNum = evluatePostFix(arr.get(i));
					if (givNum + givNum2 < 1) {
						return 0;
					}
					else{return 1;}
				}
				else if (givCom.equals("||")) {}
				}


			//System.out.println(givNum + givCom + givNum2);
			isBoolExpression = false;
			return returnNum;
		}
		else {
			StringBuilder str = new StringBuilder();
			for (int i = 0; i < line.length(); i ++) {
					str.append(line.charAt(i));
				}
			System.out.println("Test case: "+str);
			return evluatePostFix(str);
		}
		
	}
	static int evluatePostFix(StringBuilder sb) {

		// create a stack
		Stack<Integer> newStck = new Stack<>();

		// Scan all characters one by one
		for (int i = 0; i < sb.length(); i++) {
			char d = sb.charAt(i);

			// If the scanned character is an operand (number here),
			// push it to the stack.
			if (Character.isDigit(d))
				newStck.push(d - '0');

			// If the scanned character is an operator, pop two
			// elements from stack apply the operator
			else {
				int val1 = newStck.pop();
				int val2 = newStck.pop();

				switch (d) {
				case '+':
					newStck.push(val2 + val1);
					break;

				case '-':
					newStck.push(val2 - val1);
					break;

				case '/':
					newStck.push(val2 / val1);
					break;

				case '*':
					newStck.push(val2 * val1);
					break;

				case '^':
					newStck.push((int) Math.pow(val2, val1));
					break;

				case '%':
					newStck.push(val2 % val1);
					break;

				case '>':
					return (Integer) null;

				case '!':
					return (Integer) null;

				case '=':
					return (Integer) null;

				case '&':
					return (Integer) null;

				case '|':
					return (Integer) null;

				case '<':
					return (Integer) null;
				}
			}
		}
		int eval = newStck.pop();
		//System.out.println(eval); // may need to change this to a string, ask
									// them. change to static String and do
									// string eval=Integer.toString()
		return eval;
	}

}