/*
 * Author: Mark Huang
 * Email: huang.mark31@gmail.com
 * Title: RallyHealthCodingChallenge
 * Date: 10/16/2017
 */

import java.io.*;
import java.util.*;  
import javafx.util.Pair;

class Node{
	//public instance variables...just for this assignment
	public int weight;
	public String value;
	public int distance;

	public Node() {
	    this.value = "";
	    this.distance = 999999;
	}
	public Node(String value) {
	    this.value = value   ;
	    this.distance = 999999;
	}
	public Node(String value, int distance) {
	    this.value = value;
	    this.distance = distance;
	}
	public String getValue() {
	    return value;
	}
  @Override
  public String toString() {
      return value;
  }
  @Override
  public int hashCode() {
		return value.hashCode();
  }
 	@Override
  public boolean equals(Object obj) {
  	Node temp = (Node) obj;
  	return this.value.equals(temp.value);
  }
}

class NodeDistanceComparator implements Comparator<Node>
{
  @Override
  public int compare(Node x, Node y)
  {
    if (x.distance < y.distance)
    {
      return -1;
    }
    if (x.distance > y.distance)
    {
      return 1;
    }
    return 0;
  }
}

class RallyHealthCodingChallenge{
	private char[] alphabet = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
	private HashMap<Node, ArrayList<Pair<String,Integer>>> graph = new HashMap<Node, ArrayList<Pair<String,Integer>>>();
	private HashMap<String, ArrayList <String>> anagrams = new HashMap<String, ArrayList <String>>();
	private Set<String> set = new HashSet<String>();	

  public void computeAnagram(){
  	//gets all the words in the dictionarys and creates the hash map of the related anagrams 
  	for (String temp : set) {
    	//anagram computation
			char[] chars = temp.toCharArray();
    	Arrays.sort(chars);
    	String sorted = new String(chars);
    	ArrayList <String> holder = anagrams.get(sorted);
    	//if there is no arraylist yet create it
    	if (holder == null){
    		holder = new ArrayList <String> ();
    	}
    	if (!holder.contains(temp)){
    		holder.add(temp);
    		anagrams.put(sorted, holder);
    	}
    }
  }
  public void compute(int addWeight, int delWeight, int replWeight, int anagramWeight, String file){
  	System.out.println("Computing the shortest distance tree... Please Wait");
  	String fileName = file;
 	  try{
	 	  FileReader fileReader = new FileReader(fileName); 
	 	  String line = null;
	 	  //Creates a hash set that stores all the strings in the dictionary
	 	  ArrayList<Pair<String,Integer>> emp = new ArrayList<Pair<String,Integer>>();
	 	  // brings the bytes into memory so that it is much faster than reading it directly from the disk
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while((line = bufferedReader.readLine()) != null) {
	      ArrayList<Pair<String,Integer>> emptyList = new ArrayList<Pair<String,Integer>>();
	      set.add(line);
	  		Node ins = new Node(line);
	      graph.put(ins, emptyList);
	    }   
	    bufferedReader.close();
  		computeAnagram();
	    //gets all the words in the dictionary
		  for (String temp : set) {
		    for(int i = 0; i< temp.length(); i++){
		      StringBuilder holder = new StringBuilder(temp);
		      holder = holder.deleteCharAt(i);
		      String holder2 = holder.toString();
  		    //edges for when there a deletion of char difference
		      if (set.contains(holder2) && (!holder2.equals(temp))){
		      	Node node = new Node(temp,delWeight);
		      	emp = graph.get(node);
		      	emp.add(new Pair<String,Integer>(holder2,delWeight));
		      }
		    }
	 			//edges for when you you replace a character
	    	for(int i =0; i<temp.length();i++){
	       	StringBuilder hi = new StringBuilder(temp);
	       	for(char c : alphabet ){
	      	  hi.setCharAt(i,c);

	      	 	String hello = hi.toString();
	       		if(set.contains(hello) && (!hello.equals(temp))){
		      		Node node = new Node(temp,replWeight);
		      		emp = graph.get(node);
		      	  emp.add(new Pair<String,Integer>(hello,replWeight));
	       		}
	       	}
		    }
		    //edges for when you add a character
	    	for(int i =0; i<temp.length()+1;i++){
	       	for(char c : alphabet ){
	       		StringBuilder hi = new StringBuilder(temp);
	      	  hi.insert(i,c);
	      	 	String hello = hi.toString();
	       		if(set.contains(hello) && (!hello.equals(temp))){
		      		Node node = new Node(temp,addWeight);
		      		emp = graph.get(node);
		      	  emp.add(new Pair<String,Integer>(hello,addWeight));
	       		}
	       	}
		    }
				//retrieve all the anagrams of the word you are looking at
				char[] chars = temp.toCharArray();
    		Arrays.sort(chars);
    		String sorted = new String(chars);
    		ArrayList <String> holder = anagrams.get(sorted);		    
    		for (String anagram : holder){
    			//if the anagram of the word is not itself
    			if (!anagram.equals(temp)){
    				Node node = new Node(temp,anagramWeight);
    				emp = graph.get(node);
    				emp.add(new Pair<String,Integer>(anagram,anagramWeight));
    			}
    		}		    
		  }  
  	}	
  	catch(FileNotFoundException ex){
  		System.out.println("File Does Not Exist");
  	}
    catch(IOException ex) {
      System.out.println("Error reading file '" + fileName + "'");                  
    }
  }

	public void printPath(HashMap<String, String> parent, String j)
	{
    // base case for when j is source
    if ((parent.get(j)).equals("!"))
      return;
    printPath(parent, parent.get(j)); 
    System.out.print("->"+j);
	}
 
  public int minDistance(String word1, String word2){
  	System.out.println("Finding the shortest distance... Please Wait... May take up to 2 Minutes");
  	Comparator<Node> comparator = new NodeDistanceComparator();
  	PriorityQueue<Node> queue = new PriorityQueue<Node>(comparator);
  	ArrayList<Pair<String,Integer>> holder = new ArrayList<Pair<String,Integer>>();
		HashMap<String, Integer> distance = new HashMap<String, Integer>();
  	HashMap<String, String> parent = new HashMap<String, String>();
  	//sets the distances to all nodes to infinity
 		parent.put(word1,"!");
  	for(String s : set){
  		distance.put(s,999999);
  	}
  	distance.put(word1,0);
  	queue.add(new Node(word1,distance.get(word1)));
    while (queue.size() != 0)
    	{
        Node temp = queue.remove();
  			//contains edges of the node word1
  			holder = graph.get(temp);
  			//no edges edge case
  			if (holder == null){
  				return -1;
  			}
  			//goes through the edges of the node and adds it to a priority queue
  			for( Pair<String,Integer> s : holder){
  		    if((distance.get(temp.value) + s.getValue()) < distance.get(s.getKey())){
            distance.put(s.getKey(),distance.get(temp.value)+s.getValue());
  					parent.put(s.getKey(),temp.value);
  					queue.add(new Node(s.getKey(),s.getValue()));
          }
  			}
    	}
  	if(distance.get(word2)==999999){
  		return -1;
  	}
  	//important print statements don't delete
  	System.out.print(word1);
  	printPath(parent,word2);
  	System.out.println();
  	
  	return distance.get(word2);
  }
  
  public static void main(String [] args){
  	int [] weight = new int [4];
  	int count = 0;
  	String sourceWord = "";
  	String endWord = "";
		Set<String> set = new HashSet<String>();	
    RallyHealthCodingChallenge test = new RallyHealthCodingChallenge();
    try{	
			FileReader fileReader = new FileReader(args[0]); 
	 	  String line = null;
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while((line = bufferedReader.readLine()) != null) {
	      set.add(line);
	    }   
	    bufferedReader.close();
    }
  	catch(FileNotFoundException e){
  		System.out.println("ERROR: Please enter a vaild .txt from working directory");
  		return;
  	}
    catch(ArrayIndexOutOfBoundsException e){
      System.out.println("ERROR: Please input a word list.");
      return;
    }
  	catch(IOException e){
  		System.out.println("ERORR: Problem with reading the file.");
  		return;
  	}
    Scanner scanner = new Scanner( System.in );
    System.out.print("Weight of ADD/DEL/CHANGE/ANAGRAM operations (e.g. 1 3 1 4): ");
    String input = scanner.nextLine();
    String[] splited = input.split(" ");
    if(splited.length==4){
    	for(String s : splited ){
    		try{
    			Integer result = Integer.parseInt(s);
    			weight[count] = result;
    			count ++;   	
    		}
    		catch(NumberFormatException ex){
    			System.out.println("ERROR: Input contains a value that is not an Integer");
    			return;
    		}
    	}
    }
    else{
    	System.out.println("ERROR: There must be exactly 4 arguments!");
    	return;
    }

    System.out.print("Enter the source word: ");
    input = scanner.nextLine();
    splited = input.split(" ");
    if (splited.length == 1){
    	if(set.contains(input)){
    		sourceWord = input;
    	}
    	else{
    		System.out.println("ERROR: Please enter a valid word.");
    		return;
    	}
    }
    else{
    	System.out.println("ERORR: Too many inputs.");
    	return;
    }

    System.out.print("Enter the end word: ");
    input = scanner.nextLine();
    splited = input.split(" ");
    if (splited.length == 1){
    	if(set.contains(input)){
    		endWord = input;
    	}
    	else{
    		System.out.println("ERROR: Please enter a valid word.");
    		return;
    	}
    }
    else{
    	System.out.println("ERORR: Too many inputs.");
    	return;
    }
    test.compute(weight[0],weight[1],weight[2],weight[3],args[0]);
    System.out.println("The minimum distance from "+sourceWord+" to "+endWord+" is: "+test.minDistance(sourceWord,endWord));
  }
}