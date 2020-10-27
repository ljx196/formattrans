package edu.uestc.transformation.nlp.nlpforehandle.mathnormalize.latexparse;


/**
 * 根式解析
 *
 * @author B1-101
 *         兼容了 /sqrt a 这种类型
 *         兼容了 带下表的形式
 *         modified by lizo 2014-09-26
 */
public class SqrtParse {
    private static final int LEN = "\\sqrt".length();

    /**
     * latex 根式解析函数
     *
     * @return
     */
    public String sqrtParse(String str, int start, String replacestr) {
        int saveindex = start;
        int sqrtendindex = -1;
        if (start + 5 >= str.length()) {
            return replacestr;
        }
        char targetChar = str.charAt(start + 5);
        String powerstr = "2";
        if (targetChar == '{')//开平方根的情况
        {
            start = start + 6;
        } else if (targetChar == '[') {
            int end = -1;
            for (int i = start + 6; i < str.length(); i++) {
                if (str.charAt(i) == ']') {
                    end = i;
                    break;
                }
            }
            if (end == -1) {
                return replacestr;
            }
            powerstr = str.substring(start + 6, end);
            start = end + 2;
        }
        //处理 /sqrt [0-9] 这种情况
        else if ((targetChar >= '0' && targetChar <= '9')) {
            sqrtendindex = start + 5;
            for (int i = start + 6; i < str.length(); i++) {
                targetChar = str.charAt(i);
                if (targetChar - '0' >= 0 && targetChar - '0' <= 9) {
                    sqrtendindex = i;
                } else {
                    break;
                }
            }
            start = start + 5;
        }
        //处理 /sqrt [a-z]_
        else if (targetChar >= 'a' && targetChar <= 'z') {
            sqrtendindex = start + LEN;
            //如果sqrtendindex不是最后一个字符的话
            sqrtendindex++;
            if (sqrtendindex < str.length() && str.charAt(sqrtendindex) == '_') {
                sqrtendindex++;
                //往后找数字
                while (sqrtendindex < str.length()) {
                    char currentChar = str.charAt(sqrtendindex);
                    if (!(currentChar >= '0' && currentChar <= '9')) {
                        break;
                    }
                    sqrtendindex++;
                }
            }
            sqrtendindex--;
            start = start + 5;
        } else if (targetChar == '▽') {
            sqrtendindex = start + 5;
            if (str.charAt(start + 6) != '(') {
                return replacestr;
            }
            int top = 1;
            sqrtendindex += 2;
            while (sqrtendindex < str.length() && top > 0) {
                if (str.charAt(sqrtendindex) == '(') {
                    top++;
                } else if (str.charAt(sqrtendindex) == ')') {
                    top--;
                }
                sqrtendindex++;
            }
            if (top > 0) {
                return replacestr;
            }
            sqrtendindex--;
            start = start + 5;
        }

        int loop = 1;
        int end = -1;
        for (int i = start; i < str.length() && sqrtendindex == -1; i++) {
            if (str.charAt(i) == '}') {
                loop--;
            }
            if (str.charAt(i) == '{') {
                loop++;
            }
            if (loop == 0) {
                end = i;
                break;
            }
        }


        if (saveindex != -1 && end != -1 && saveindex < end + 1) {
            String oldstring = str.substring(saveindex, end + 1);
            String newstr = "((" + str.substring(start, end) + ")^(1/(" + powerstr + ")))";
            replacestr = replacestr.replace(oldstring, newstr);
        } else if (saveindex != -1 && sqrtendindex != -1 && saveindex < sqrtendindex + 1) {
            String oldstring = str.substring(saveindex, sqrtendindex + 1);
            String newstr = "((" + str.substring(start, sqrtendindex + 1) + ")^(1/(" + powerstr + ")))";
            replacestr = replacestr.replace(oldstring, newstr);
        }

        return replacestr;
    }


}
