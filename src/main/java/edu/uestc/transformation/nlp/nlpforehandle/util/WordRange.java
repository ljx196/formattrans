package edu.uestc.transformation.nlp.nlpforehandle.util;

import java.util.List;

public class WordRange implements Comparable<WordRange>{
	private int start=-1;
	private int end=-1;
	
	public WordRange(int start, int end) {
		super();
		this.start = start;
		this.end = end;
	}
	@Override
	public WordRange clone()
	{
		return new WordRange(start, end); 
	}
	
	public void joinStart(int startIndex)
	{
		if(-1==startIndex)
			return;
		if(-1==this.start)
		{
			this.start=startIndex;
			return;
		}
		this.start=Math.min(start, startIndex);
	}
	public void joinEnd(int endIndex)
	{
		this.end=Math.max(end, endIndex);
	}
	public void join(WordRange wordRange)
	{
		this.joinStart(wordRange.start);
		this.end=Math.max(end, wordRange.end);
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	
	/**
	 * 作为一个闭区间判断两个范围的关系
	 * 0. 相等
	 * 1. 包含于
	 * 2. 包含
	 * 3. 左相离
	 * 4. 右相离
	 * 5. 相交
	 * @param wordRange
	 * @return
	 */
	public int getRelate(WordRange wordRange)
	{
		if(this.start<wordRange.start)
		{
			if(this.end>=wordRange.end)
				return 2;
			if(this.end<wordRange.start)
				return 3;
			return 5;
		}
		if(this.start==wordRange.start)
		{
			if(this.end>wordRange.end)
				return 2;
			if(this.end==wordRange.end)
				return 0;
			if(this.end<wordRange.start)
				return 3;
			return 5;
		}
		if(this.end<=wordRange.end)
			return 1;
		if(this.start>wordRange.end)
			return 4;
		return 5;
	}
	public boolean isContainIn(WordRange wordRange)
	{
		return this.start>=wordRange.start && this.end<=wordRange.end;
	}
	
	public int getContainIndex(List<WordRange> wordRanges)
	{
		for(int i=wordRanges.size()-1;i>=0;i--)
		{
			if(isContainIn(wordRanges.get(i)))
				return i;
		}
		return -1;
	}
	@Override
	public String toString() {
		return "WordRange [start=" + start + ", end=" + end + "]";
	}
	@Override
	public int compareTo(WordRange o) {
		if(this.start!=o.start)
			return this.start-o.start;
		return this.end-o.end;
	}
	

}
