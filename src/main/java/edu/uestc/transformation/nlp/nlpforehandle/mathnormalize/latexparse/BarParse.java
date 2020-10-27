package edu.uestc.transformation.nlp.nlpforehandle.mathnormalize.latexparse;


public class BarParse {
	
	private static final int LEN = "\\bar".length();
	
	public String barParse(String str, int start, String repalceStr){
		
		int savedIndex = start;
		int barIndex = -1;
		if(savedIndex + LEN >= str.length())
			return repalceStr;
		
		char targetChar = str.charAt(savedIndex + LEN);
		if(targetChar=='{'){
			barIndex = savedIndex+LEN + 1;
			int top = 1;
			while(top>0 && barIndex<str.length()){
				if(str.charAt(barIndex)=='{')
					top++;
				else if(str.charAt(barIndex)=='}')
					top--;
				barIndex++;
			}
			if(top>0)
				return repalceStr;
			
		}else if((targetChar>'0' && targetChar<'9') || targetChar>'a' && targetChar<'z'){
			barIndex = savedIndex + LEN + 1;
			
		}else{
			return repalceStr;
		}
		
		String tempString = str.substring(savedIndex+LEN, barIndex);
		String newTempString = tempString;
		if(newTempString.charAt(0)=='{' && newTempString.charAt(newTempString.length()-1)=='}')
			newTempString = newTempString.substring(1,newTempString.length()-1);
		String newString = "▽(" + newTempString + ")";
		repalceStr = repalceStr.replace("\\bar" + tempString, newString);
		
		return repalceStr;
	}
	
	

}
