package edu.uestc.transformation.nlp.nlpforehandle.mathnormalize;

/**
 * 记录要替换的字符串在原始字符串中的位置
 * Created by Lizo on 2015/1/9.
 */
class ReplaceData implements Comparable {
    String target;
    int start;
    int end;


    public ReplaceData(String target, int start, int end) {
        this.target = target;
        this.start = start;
        this.end = end;
    }

    @Override
    public int compareTo(Object o) {
        ReplaceData other = (ReplaceData) o;
        if (this.end > other.end) {
            return 1;
        } else {
            return -1;
        }
    }
}
