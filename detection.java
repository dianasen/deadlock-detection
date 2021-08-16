/*
 * written by Diana Sen 
 * this is class based on bankers algoritm 
 * it will find best way to avoid deadlock 
 * but if there is no way for deadlock to be avoided code will terminate one precess in order to get out of deadlock 
 * multiple methods made to make understanding of code easier
 * extra additional explanations above method name or inside on confusing parts 
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

//Java Program for Bankers Algorithm
public class detection
{
	int safeState = 0;
	int count=0;
    int n = 0; // Number of processes 
    int m = 0; // Number of resources
    int [][]req;
    int [][]alloc;
    int [][]allocCopy;
    int []avail;
    int work[];
    int safeSequence[];
  
    static String fileOne;
    static String fileOneCopy;
    //fileOneCopy = fileOne;
    static String fileTwo;
    static String fileThree;
    
    void initializeValues() throws IOException, FileNotFoundException {
    	alloc = readMatrix(fileOne);
    	allocCopy = readMatrix(fileOneCopy);
	    avail = readArray(fileTwo);
	    req = readMatrix(fileThree);

	    BufferedReader reader = new BufferedReader(new FileReader(fileOne));
	    BufferedReader readerCopy = new BufferedReader(new FileReader(fileOneCopy));
	    BufferedReader reader2 = new BufferedReader(new FileReader(fileTwo));
	    BufferedReader reader3 = new BufferedReader(new FileReader(fileThree));
	      
	    //from file 1
	    String processes = reader.readLine();
	    n = Integer.parseInt(processes);
	        
	    String resources = reader.readLine();
	    m = Integer.parseInt(resources);
	  		
	    reader.close();
	    readerCopy.close();
	    reader2.close();
	    reader3.close();
    }
    
	//Read a matrix from the file with the specified file name.
    public static int[][] readMatrix(String fileName) throws FileNotFoundException {
    	File file = new File(fileName);
    	int[] dimensions = readMatrixSize(file);
    	int numRows = dimensions[0];
    	int numCols = dimensions[1];

    	Scanner in = new Scanner(file);
    	in.nextLine();
    	in.nextLine();
    	int[][] matrix = new int[numRows][numCols];
    	for (int i = 0; i < numRows; i++) {
    		for (int j = 0; j < numCols; j++) {
    			matrix[i][j] = in.nextInt();
    		}
    	}

    	in.close();
    	return matrix;
    }

	/*
	 * Read a matrix from file and return a 2D array 
	 * array has number of rows(element1) columns(element2)
	 */
    public static int[] readMatrixSize(File file) throws FileNotFoundException {
    	Scanner inFile = new Scanner(file);
    	inFile.nextLine();
    	inFile.nextLine();
    	String line = inFile.nextLine();

    	int numCols = 0;
    	Scanner inLine = new Scanner(line);
    	while (inLine.hasNext()) {
    		inLine.next();
    		numCols++;
    	}
    
    	inLine.close();
    	int numRows = 1;
    	while (inFile.hasNext()) {
    		inFile.nextLine();
    		numRows++;
    	}
    	inFile.close();

    	return new int[] {numRows, numCols};
    }

 
    //The size of an array in the specified file
    public static int readArraySize(File file) throws FileNotFoundException {
    	Scanner in = new Scanner(file);
    	in.nextLine();

    	int size = 0;
    	while (in.hasNext()) {
    		in.next();
    		size++;
    	}
    	in.close();
    	return size;
    }

    //Read an array in the file with the specified file name
	public static int[] readArray(String fileName) throws FileNotFoundException {
		File file = new File(fileName);
	    int size = readArraySize(file);
	
	    Scanner in = new Scanner(file);
	    int[] arr = new int[size];
	    for (int i = 0; i < size; i++) {
	      arr[i] = in.nextInt();
	    }
	    in.close();
	    return arr;
	  }
	  
	//detects if deadlock exists
	void isSafe() {
		count=0;

		boolean finish[] = new boolean[n]; 
		for (int i = 0;i < n; i++) {
		    finish[i] = false;
		}

	    avail = new int[m];
	    work = avail;
	    for (int i = 0; i < m; i++) {
	        work[i] = avail[i];
	    }
	    safeSequence = new int[n];
	    while (count < n) {
	    	//allocCopy = alloc;
	        boolean checkFinish = false;
	        for (int i = 0;i < n; i++) {
	            if (finish[i] == false) {
	            int cols;
	            for (cols = 0; cols < m; cols++) {        
	                if (req[i][cols] > work[cols])
	                break;
	            }
	            if (cols == m) {
	               safeSequence[count++] = i;
	               safeState = i;
	               //System.out.println("safe state: " + safeState);
	               finish[i]=true;
	               checkFinish=true;
	                          
	               for (cols = 0;cols < m; cols++) {
	            	   work[cols] = work[cols]+alloc[i][cols];
	                }
	            }
	             }
	        }
	        if (checkFinish == false) {
	            break;
	        }
	    }
	    if (count < n) {
	        fulldeadlock();
	        KP();
	    }
	    else {
	    	for (int i = 0; i < n; i++) {
	    		safeState = i;
	    		System.out.print("Project: " + safeSequence[safeState]);
	            if (i != n-1)
	            System.out.print(" -> ");
	    	}
	    }
	}

    public int mostResources() {
    	// take allocation matrix 
    	//find row with highestnumbers 
        int highestRow = -1; //index to storre row 
      	int maxSum = Integer.MIN_VALUE;
      
      	
		for (int i =0; i<alloc.length; i++) {
          int sum = 0;
          for (int j = 0; j < alloc[i].length; j++) {
        	  sum = sum + alloc[i][j];
           }
          //update
          if (maxSum < sum ) {
        	  maxSum = sum;
       		  highestRow = i;  //row with highest number 
      	  }
        }

    	return highestRow;
    }
    
    public int mostResourcesCopy() {
    	// take allocation matrix 
    	//find row with highestnumbers 
        int highestRow = -1; //index to storre row 
      	int maxSum = Integer.MIN_VALUE;
      
		for (int i =0; i<allocCopy.length; i++) {
          int sum = 0;
          for (int j = 0; j < allocCopy[i].length; j++) {
        	  sum = sum + allocCopy[i][j];
           }
          //update
          if (maxSum < sum ) {
        	  maxSum = sum;
       		  highestRow = i;  //row with highest number 
      	  }
        }

    	return highestRow;
    }
    public int highestSum() {
    	// take allocation matrix 
    	//find row with highestnumbers 
        int highestRow = -1; //index to storre row 
      	int maxSum = Integer.MIN_VALUE;
      
		for (int i =0; i<alloc.length; i++) {
          int sum = 0;
          for (int j = 0; j < alloc[i].length; j++) {
        	  sum = sum + alloc[i][j];
           }
          //update
          if (maxSum < sum ) {
        	  maxSum = sum;
       		  highestRow = i;  //row with highest number 
       		  
      	  }
        }

    	return maxSum;
    }
    
    public int highestSumCopy() {
    	// take allocation matrix 
    	//find row with highestnumbers 
        int highestRow = -1; //index to storre row 
      	int maxSum = Integer.MIN_VALUE;
      
		for (int i =0; i<allocCopy.length; i++) {
          int sum = 0;
          for (int j = 0; j < allocCopy[i].length; j++) {
        	  sum = sum + allocCopy[i][j];
           }
          //update
          if (maxSum < sum ) {
        	  maxSum = sum;
       		  highestRow = i;  //row with highest number 
       		  
      	  }
        }

    	return maxSum;
    }
    	
    public int resourcesEqual() {
    	//make alloc Copy 
    	try {
			allocCopy = readMatrix(fileOneCopy);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	int allocSum = highestSum();  // highest sum of alloc 
        
        //System.out.println("allocSum : " + allocSum);
        
        int highestRowCopy = -1; //index to storre row 
      	int maxSumCopy = Integer.MIN_VALUE;
      
		for (int row =0; row<allocCopy.length; row++) {
			int sumCopy = 0;
			for (int col = 0; col < allocCopy[row].length; col++) {
				sumCopy = sumCopy + allocCopy[row][col];
			}
			if (sumCopy == allocSum) {
				highestRowCopy = row;
				return highestRowCopy;
			}
      	  
		}
			return highestRowCopy;
		
        }	
 		
 		/*
  		int processDeleted = 0;
  		for (int row =0; row < allocCopy.length; row++) {
  			//if (!Arrays.equals(allocCopy[row],alloc[row]) ) {
  			if (!Arrays.equals(allocCopy[row],alloc[row])) {
  				processDeleted = row;
  				System.out.println("process do not match on row: " + row);
  			}
  		}
		return processDeleted;
    }
    */
    
    public void fulldeadlock() {
    	if (n == 1) {
    		boolean[] finish;
    		//System.out.println("alloc in deadlock: ");
    		//print2D(alloc);
    		//System.out.println("req in deadlock: ");
            //print2D(req);
            
           // System.out.println("allocCopy: \n");
            try {
				allocCopy = readMatrix(fileOne);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            //    print2D(allocCopy);
    		//System.out.println();
            int processRemove = resourcesEqual();
    		System.out.println("This is your last process: "+processRemove+ " all previous ones been killed due to unable to be fulfilled");

    	}
    	
    }
    
    //used for debugging purposes
    public static void print2D(int mat[][]) {
        // Loop through all rows
        for (int[] row : mat)
    System.out.println(Arrays.toString(row));
    }
 
    //deletes row with highest resources and checks for deadlock
    public void KP() {
    	
    	try {
			allocCopy = readMatrix(fileOne);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	if (n == 1) {
    		System.out.println("Deadlock occured on all projects. Last one left to not "
    				+ "leave with out projects running.\nKilled all projects "
    				+ "one by to try to get out of deadlock\nNothing else to be done.");

    	} else {
    		//System.out.println("allocCopy: ");
    		//print2D(allocCopy);
    		
    		//System.out.println("alloc: ");
    		//print2D(alloc);
    		System.out.println(" ");
    		
    		
        	alloc = deleteRow(alloc);
            req = deleteRow(req);
            work = avail;
    		n = n-1;
    		int processRemove = resourcesEqual();
    		//	resourcesEqual();
        	//int processRemove = mostResources(); //row to remove
    		//int processRemove = resourcesEqual();
    		
        	System.out.println("Process: " + processRemove + " will "
        			+ "be killed to allow the other process to be complete");

        	isSafe(); 
    	}
    }
 

   
  	//void deleteRow(int[][] array) {
  	public int[][] deleteRow(int[][] array) {
  		int highestRow = mostResources(); //row to remove
  		int [][]newMatrix = new int[n-1][m]; //new matrix 
 
  		//copy everything up to highest row 
  		for (int i = 0; i<highestRow; i++) {
  			for (int j = 0; j < array[i].length; j++){
  				newMatrix[i][j] = array[i][j]; 
          }
        }
      
      for (int i = highestRow; i<newMatrix.length; i++) {
          for (int j = 0; j < array[i].length; j++){
             newMatrix[i][j] = array[i+1][j];
          }
        }
      return newMatrix;  
  	}

    public void run() throws FileNotFoundException, IOException {
	    Scanner scanner = new Scanner(System.in);

	    System.out.println("Enter file one(allocation): ");
        String input = scanner.nextLine();
	    if (input.equals("quit")) {
	    	System.out.println("Goodbye, Program terminated by user");
	    	System.exit(0);
	    } else {
	    	fileOne = input;
	    	fileOneCopy = fileOne;
	    	}
	    
	    System.out.println("Enter file two(available): ");
	    input = scanner.nextLine();
	    if (input.equalsIgnoreCase("quit")) {
	    	System.out.println("Goodbye, Program terminated by user");
	    	System.exit(0);
	    } else {
	    	//fileTwo = scanner.nextLine();
	    	fileTwo = input;
	    }
	    
	    System.out.println("Enter file three(requests): ");
	    input = scanner.nextLine();
	    if (input.equalsIgnoreCase("quit")) {
	    	System.out.println("Goodbye, Program terminated by user");
	    	System.exit(0);
	    
	    } else { 
	    	//fileThree = scanner.nextLine();
	    	fileThree = input;
	    }
	    scanner.close();
    }
     public void welcome() {
    	 System.out.println("Welcome to Workshop Manager\nThis program that "
    	 		+ "determines whether or not a number of projects in a "
    	 		+ "workshop may be deadlocked.\nIf tolls not deadlocked then program will "
    	 		+ "provide safe sequence on how to use them in order to get done with all "
    	 		+ "projects.\nIn case there is deadlock program will provode sequence on "
    	 		+ "wchich projects to execute.\n\n\nThis program will take in 3 files allocation,"
    	 		+ " availible, and requested.\nThey must be entered in this order due to "
    	 		+ "stracture of the code.\n\n\nMake sure you put absolute path to text file.\nYou "
    	 		+ "may terminate program by typing in quit at any time.");
     }

    public static void main(String[] args) throws FileNotFoundException, IOException {
    	
    	detection myCode = new detection();
    	myCode.welcome();
    	myCode.run();
    	myCode.initializeValues();     
    	myCode.isSafe();        
    }
}