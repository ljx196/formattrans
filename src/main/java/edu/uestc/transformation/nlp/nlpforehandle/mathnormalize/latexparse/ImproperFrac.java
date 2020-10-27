package edu.uestc.transformation.nlp.nlpforehandle.mathnormalize.latexparse;

import edu.uestc.transformation.nlp.nlpforehandle.Latex.GrammarCheckFunc;

public class ImproperFrac {
    /**
     * 假分式的解析
     *
     * @param str        循环开始原始的字符串
     * @param replacestr 中间经过替换的字符串
     * @return
     */

    public String improperFracParse(String str, int start, String replacestr) {
        StringBuffer number = new StringBuffer();
        int numberindex = 0;
        for (int i = start; i < str.length(); i++) {
            numberindex = i;
            if (GrammarCheckFunc.isNumber(str.charAt(i))) {
                number.append(str.charAt(i));
            } else {
                break;
            }
        }
        boolean frontpart = true;
        int loop = 1;
        int front = -1;
        int end = -1;
        for (int i = numberindex + 6; i < str.length(); i++) {
            if (str.charAt(i) == '}') {
                loop--;
            }
            if (str.charAt(i) == '{') {
                loop++;
            }
            if (loop == 0 && frontpart) {
                front = i;
                loop = 1;
                i++;
                frontpart = false;
            }
            if (loop == 0 && !frontpart) {
                end = i;
                break;
            }
        }//end for i
        if (end != -1 && start < end + 1) {
            String oldstring = str.substring(start, end + 1);
            String molecule = str.substring(numberindex + 6, front);
            String denominator = str.substring(front + 2, end);
            String newstring = "((" + number.toString() + "*(" + denominator + ")+(" + molecule + "))/(" + denominator + "))";
            replacestr = replacestr.replace(oldstring, newstring);
        }
        return replacestr;
    }
}
