import java.util.Scanner;
import java.util.Stack;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
			//System.out.println(infixToPostFix(line));
			int var = comparator(infixToPostFix(line));
			if(var != -999999999){
				System.out.println(var);
			}
			else{
				System.out.println("Cant divide by zero, skiping output.");
			}
			System.out.println(" ");
		}
	}

	/***
	* This method checks to see if a character is an operator and if it is it returns the precedence of that operator
	*@param char This is the expected character to check
	*@return returns an integer of the precendence
	*/
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
		// n2
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

			int returnNum = -1;
			int givNum = 3; 
			int givNum2 = 3;
			String givCom = "<=";

			Queue<String> queue = new LinkedList<String>();
			ArrayList<StringBuilder> arr = new ArrayList<StringBuilder>();
			StringBuilder booloperator = new StringBuilder();
			StringBuilder str = new StringBuilder();
			boolean beenAdded = false;

			// splits the input string into a multiple of sb's and strings
			for (int i = 0; i < line.length(); i ++) {
				//checks to see if this char is a operator
				if(line.charAt(i) == '>' || line.charAt(i) == '<'|| line.charAt(i) == '|'|| line.charAt(i) == '&'|| line.charAt(i) == '=' || line.charAt(i) == '!'){
					booloperator.append(line.charAt(i));
					//checks if next char is operator and if so adds the new string
					if(line.charAt(i+1) == '=' || line.charAt(i+1) == '|'|| line.charAt(i+1) == '&'){
						booloperator.append(line.charAt(i+1));
						i++;
					}
					//adds operator to queue
					queue.add(booloperator.toString());
					booloperator = new StringBuilder();
					//adds the new sb into arr 
					if(!beenAdded && str.length()!=0){
						arr.add(str);
					}
					//clears sb
					str = new StringBuilder();
					beenAdded = false;
				}
				else{
					//adds char to sb
					str.append(line.charAt(i));
					if(!beenAdded){
						arr.add(str);
						beenAdded = true;
					}
				}
			}

			List<Integer> andOrNums = new ArrayList<>(); 
			Queue<String> qandOr = new LinkedList<String>();
			ArrayList<StringBuilder> arrClone = new ArrayList<StringBuilder>();

			for(int i = 0; i<arr.size();i++){
				arrClone.add(arr.get(i));
			}

			for (int i = 0; i < arr.size(); i++) { //Iterates through the entire "line" which is stored in arr
				if (queue.size() == 0) { //Checks to see if the queue is empty to make sure no null's are being compared
					break; //If it is then it will exit the loop
				}

				givCom = queue.poll(); //Sets a variable, givCom equal to the top of the queue which is full of operators	

				boolean isAndOr = false; // Automatically creates and sets a boolean to will store if the most recent operator is an and or or statement

				if (givCom.equals("&&") || givCom.equals("||")) { //Checks to see if the variable, givCom is an and or an or statement
					isAndOr = true; // If it is then it will change the boolean to true
					qandOr.add(givCom); // Adds it to a new queue that stores and/or statements
					if (queue.size() > 0) { // fixes a case for 1&&1 or 0&&1
						givCom = queue.poll();
						if(givCom.equals("&&") || givCom.equals("||")){
							qandOr.add(givCom);
						}
					}
				}

				if (qandOr.size() == 1 && returnNum != -1) {
					andOrNums.add(returnNum); // Adds the resulting number from the needed evaluations to a list of integers to be evaluated
				}

				givNum = evluatePostFix(arr.get(i)); // Solves the infix version and sets it equal to givNum
				arrClone.remove(0);
				if (i < arr.size() - 1) {
					i++;
					givNum2 = evluatePostFix(arr.get(i)); // Solves the infix version and sets it equal to givNum2
					arrClone.remove(0);
				}
				//fixes odd case where the number is by itself past a && or a || operator 

				// Evlautes a greater than statement
				else{
					if(givNum > 0){
						returnNum = 1; 
					}
					else{
						returnNum = 0;
					}
				}

				if(givNum == -999999999 || givNum2 == -999999999){
					return -999999999;
				}

				// Evlautes a greater than statement
				if (givCom.equals(">")) {
					if (givNum > givNum2) {returnNum = 1;}
					else {returnNum = 0;}
				}
				// Evlautes a greater or equal to statement
				else if (givCom.equals(">=")) {
					if (givNum >= givNum2) {returnNum = 1;}
					else {returnNum = 0;}
				}
				// Evlautes a less than statement
				else if (givCom.equals("<")) {
					if (givNum < givNum2) {returnNum = 1;}
					else{returnNum = 0;}
				}
				// Evlautes a less than or equal to statement
				else if (givCom.equals("<=")) {
					if (givNum <= givNum2){returnNum = 1;}
					else{returnNum = 0;}
				}
				// Evlautes an equal to statement
				else if (givCom.equals("==")) {
					if (givNum == givNum2) {returnNum = 1;}
					else {returnNum = 0;}
				}
				// Evlautes an not equal to statement
				else if (givCom.equals("!=")) {
					if (givNum != givNum2) {returnNum = 1;}
					else {returnNum = 0;}
				}

				// Checks to see if the boolean true, which means the comparator was an and/or
				if (isAndOr) {
					if (returnNum != -1) { // If the retunNum has not changed
						andOrNums.add(returnNum); // Add it, the evaluated returnNum, to the ArrayList of integers
					}
					else {
						andOrNums.add(givNum); // Add the number at the index to the ArrayList of integers
						andOrNums.add(givNum2); // Add the number at the index to the ArrayList of integers
					}
				}
			}

			if(qandOr.size() > 0) {
				for(StringBuilder var : arrClone){
					andOrNums.add(evluatePostFix(var));
				}
				returnNum = solveAndOr(andOrNums, qandOr);
			}
			isBoolExpression = false;
			return returnNum;
		}
		else {
			StringBuilder str = new StringBuilder();
			for (int i = 0; i < line.length(); i ++) {
					str.append(line.charAt(i)); // Appends the str to concatenate with the line
				}
			return evluatePostFix(str); // Evaluates the string and calcualtes the result
		}
		
	}

	static int solveAndOr(List<Integer> nums, Queue<String> ops) {
		//Solves the and/or expressions in a line

		// Creates and initializes variables
		int curNum = -1;
		int retNum = -1;
		String curOp = "";
		for (int i = 0; i <nums.size(); i++) { // For loop to loop through all the numbers that were given
			if (i ==0) {
				curNum = nums.get(i); // Sets the variable to a number at the given index
				curOp = ops.poll(); // Sets the variable to the most recent comparator
				i++; // iterates i 
				retNum = nums.get(i); // gets the next number
				// Because of formatting and evaluation 1&&0, this is proper way to receive values
			}
			else {
				curNum = nums.get(i); // Sets the variable to a number at the given index
				curOp = ops.poll(); // Sets the variable to the most recent comparator
			}

			if(curOp != null){
				// Evaluates the expressions depending on the comparator and changes retNum to the evaluated value
				if (curOp.equals("&&")) {
					if (curNum  == 1 && retNum == 1) {retNum = 1;}
					else{retNum = 0;}
				}
				else if (curOp.equals("||")) {
					if (curNum == 1 || retNum == 1) {retNum = 1;}
					else{retNum = 0;}
				}
			}
		}
		return retNum; // Returns retNum which is the evaluated value
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
					try {
						newStck.push(val2 / val1);
					  }
					  catch(Exception e) {
						return -999999999;
					  }
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
					return -999999999;

				case '!':
					return -999999999;

				case '=':
					return -999999999;

				case '&':
					return -999999999;

				case '|':
					return -999999999;

				case '<':
					return -999999999;
				}
			}
		}
		int eval = newStck.pop();
		return eval;
	}

}