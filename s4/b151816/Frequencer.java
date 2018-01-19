//package s4.b151808; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 
import java.lang.*;
//import s4.specification.*;


interface FrequencerInterface {     // This interface provides the design for frequency counter.
    void setTarget(byte[]  target); // set the data to search.
    void setSpace(byte[]  space);  // set the data to be searched target from.
    int frequency(); //It return -1, when TARGET is not set or TARGET's length is zero
                    //Otherwise, it return 0, when SPACE is not set or Space's length is zero
                    //Otherwise, get the frequency of TAGET in SPACE
    int subByteFrequency(int start, int end);
    // get the frequency of subByte of taget, i.e target[start], taget[start+1], ... , target[end-1].
    // For the incorrect value of START or END, the behavior is undefined.
}


public class Frequencer implements FrequencerInterface{
    // Code to Test, *warning: This code  contains intentional problem*
    byte [] myTarget;
    byte [] mySpace;
    
    boolean targetReady = false;	
    boolean spaceReady = false;
    
    int [] suffixArray;	
    
    public void setTarget(byte [] target) {myTarget = target; if(myTarget.length>0) targetReady = true; }
    
    public void setSpace(byte []space) { 
    	mySpace = space; 
    	if(mySpace.length>0) spaceReady = true;
    	suffixArray = new int[space.length];
    	// put all suffixes in suffixArray. Each suffix is expressed by one interger. 
    	for(int i = 0; i< space.length; i++) {suffixArray[i] = i; }
    	// Sorting is not implmented yet. 
    	/* Example from "Hi Ho Hi Ho"
    	0: Hi Ho
    	1: Ho
    	2: Ho Hi Ho 
    	3:Hi Ho
    	4:Hi Ho Hi Ho 
    	5:Ho
    	6:Ho Hi Ho 
    	7:i Ho
    	8:i Ho Hi Ho 
    	9:o
    	A:o Hi Ho
    	*/
    	for(int i=0;;i++){
    		int sw;
    		boolean f=true;	//並び替えが終了したかどうか
    		for(int j=0;j+1<suffixArray.length;j++){	//入れ替わりが起きたら初めからループ
    			if(suffixCompare(j,j+1)==1){
    				sw=suffixArray[j];
    				suffixArray[j]=suffixArray[j+1];
    				suffixArray[j+1]=sw;
    				f=false;
    				break;
    			}
    		}
    		if(f==true)break;	//一度も入れ替わらなかったらソート終了
    	}
    	printSuffixArray();
    }
    	
    public int frequency() {
    	if(targetReady == false) return -1; 
    	if(spaceReady == false) return 0;
    	return subByteFrequency(0, myTarget.length);
    }

    public int subByteFrequency(int start, int end) {
    	/* This method could be defined as follows though it is slow.
    	int spaceLength = mySpace.length;
    	int count = 0;
    	for(int offset = 0; offset< spaceLength - (end - start); offset++) {
	    	boolean abort = false;
	    	for(int i = 0; i< (end - start); i++) {
	    		if(myTarget[start+i] != mySpace[offset+i]) { abort = true; break; } 
	    	}
	    	if(abort == false) { count++; } 
	    }
	    */
    	int first = subByteStartIndex(start,end);
    	int last1 = subByteEndIndex(start,end);
    	//inspection code
    	for(int k=start;k<end;k++) { System.out.write(myTarget[k]); } 
    	System.out.printf(": first=%d last1=%d\n", first, last1);
    	return last1 - first;
    	}

    // The variable, "suffixArray" is the sorted array of all suffixes of mySpace.
    // Each suffix is expressed by a interger, which is the starting position in mySpace. 
    // The following is the code to print the variable
    private void printSuffixArray() {
    		if(spaceReady) {
    			for(int i=0; i< mySpace.length; i++) {
    				int s = suffixArray[i];
    				for(int j=s;j<mySpace.length;j++) {
    					System.out.write(mySpace[j]); 
    					}
    				System.out.write('\n'); }
    		}
    }
    
    private int suffixCompare(int i, int j) {
    	// comparing two suffixes by dictionary order. 
    	// i and j denoetes suffix_i, and suffix_j
    	// if suffix_i > suffix_j, it returns 1
    	// if suffix_i < suffix_j, it returns -1
    	// if suffix_i = suffix_j, it returns 0;
    	// It is not implemented yet,
    	// It should be used to create suffix array. 
    	// Example of dictionary order
	    	int si = suffixArray[i]; 
	    	int sj = suffixArray[j];
	    	int s =0;
	    	if(si >s)s=si;
	    	if(sj >s)s=sj;
	    	int n =mySpace.length-s;
	    	for(int k=0;k<n;k++) {
	    		if(mySpace[si+k]>mySpace[sj+k]) return 1;
	    		if(mySpace[si+k]<mySpace[sj+k]) return -1; 
	    	}
	    	if(si < sj) return 1; 
	    	if(si > sj) return -1;
    	    return 0;
    	}
    
    private int targetCompare(int i, int start, int end) {
    	// It is called from subBytesStarIndex, adn subBytesEndIndex.
    	// "start" and "end" are same as in subByteStartIndex, and subByteEndIndex ** 
    	// target_start_end is subBytes(start, end) of target **
    	// comparing suffix_i and target_start_end by dictonary order with limitationof length; ***
    	// if the beginning of suffix_i matches target_start_end, and suffix is longerthan target it returns 0;
    	// if suffix_i > target_start_end it return 1; **
    	// if suffix_i < target_start_end it return -1 ** //
    	// It should be used to search the apropriate index of some suffix.
    	for(int j=0;j<(end-start);j++){
    		if(mySpace[suffixArray[i]+j]>myTarget[j]){return -1;}
    		else if(mySpace[suffixArray[i]+j]<myTarget[j]){return 1;}
    	}
    	return 0;
    }

    private int subByteStartIndex(int start, int end) {
    	// It returns the index of the first suffix which is equal or greater than subBytes; 
    	// not implemented yet;
    	// For "Ho", it will return 5 for "Hi Ho Hi Ho".
    	// For "Ho ", it will return 6 for "Hi Ho Hi Ho".
    	int n=0;
    	for(int i=0;i<mySpace.length-1;i++){
    		if(targetCompare(i,start,end)==0){n=i;break;}
    	}
    	return n;
    }
    private int subByteEndIndex(int start, int end) {
    	// It returns the next index of the first suffix which is greater than subBytes; 
    	// not implemented yet
    	// For "Ho", it will return 7 for "Hi Ho Hi Ho".
    	// For "Ho ", it will return 7 for "Hi Ho Hi Ho".
    	int n=0;
    	for(int i=0;i<mySpace.length-1;i++){
    		if(targetCompare(i,start,end)==-1){n=i;break;}
    	}
    	return n;
   	}

    public static void main(String[] args) {
    	Frequencer frequencerObject; 
    	try {
    	frequencerObject = new Frequencer();
    	frequencerObject.setSpace("Hi Ho Hi Ho".getBytes()); 
    	frequencerObject.setTarget("Ho".getBytes());
    	int result = frequencerObject.frequency(); System.out.print("Freq = "+ result+" ");
    	if(2 == result) { System.out.println("OK"); }
    	else {System.out.println("WRONG"); } 
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	System.out.println("STOP"); }
    }
}	    
	    
