package edu.uestc.transformation.nlp.nlpforehandle.mathnormalize;


import edu.uestc.transformation.nlp.nlpforehandle.regularexpressiontools.LTools;
import edu.uestc.transformation.nlp.nlpforehandle.regularexpressiontools.RegularExpression;
import edu.uestc.transformation.nlp.nlpforehandle.regularexpressiontools.RegularExpressionTools;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 在进入句模处理之前移除受干扰的Latex符号
 * 如
 * 1、$△$
 *
 * @author B1-101
 *         <p/>
 *         重构 用StringBuffer代替String
 *         Modified by Lizo on  2015-01-08
 */
public class FormatAnswerStringUtil {
    private static final String[] operatorRegex = {"\\+", "\\*", "×", "/", "÷", "-", "^", "﹣", "−", "=", ">", "<", "≥", "≤", "≠", "≈",
            "≡", "＜"};
    private static final String subQuestionNumRegex = LTools.createRegexOrByStringList(operatorRegex);

    /*带坐标的点*/
    private static final String POINTWITHCOORDREGEX_STRING = "(" + RegularExpression.CHAR_REGEX + "|)\\(" + RegularExpression.EXPR_CONTAIN + "," + RegularExpression.EXPR_CONTAIN + "\\)";

    public static void format(StringBuffer ansStringBuffer) {

//		if(qType==QuestionType.FILL_IN)
//			 this.deleteContinuedEquality(ansStringBuffer);

        removeBracesOfSubPoint(ansStringBuffer);
        //找出坐标没有带 latex符号的情况
        findCoordinate(ansStringBuffer);
        removeBracesOfCoordinate(ansStringBuffer);
        //removeEmptyLatext(ansStringBuffer);
        splitTwoPoints(ansStringBuffer);
//        correctThePosOfDollar(ansStringBuffer);
//		filterForTrigonometric(ansStringBuffer);
        corrctFormOfArea(ansStringBuffer);
        //absoluteParse(ansStringBuffer);

    }

    //统计字符在字符串中出现的次数
    private static int statisticalCharInString(String str, char c) {
        int flag = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c) {
                flag++;
            }
        }
        return flag;
    }

    //替换绝对值 包含复合情况
    private static String replaceAbsolute(String originalString, Pattern pattern) {
        originalString = originalString.substring(1, originalString.length());
        Matcher match1 = pattern.matcher(originalString);
        String result = "";
        if (statisticalCharInString(originalString, '|') > 2 && match1.find()) {
            String local = match1.group();
            String begin = originalString.substring(0, match1.start());
            result = "|" + begin + replaceAbsolute(local, pattern);
        }
        if (statisticalCharInString(originalString, '|') == 2 && match1.find()) {
            String begin = originalString.substring(0, match1.start());
            result = "|" + begin + replaceAbsoluteSingle(match1.group());
        }
        return result;
    }

    //替换单个绝对值
    private static String replaceAbsoluteSingle(String originalString) {
        originalString = "abs(" + originalString.replace("|", "") + ")";
        return originalString;
    }

    private static String multiAbsoluteReplace(String originalString, Pattern pattern) {
        String targetString = "";
        if (originalString.startsWith("||") && originalString.endsWith("|")) {
            targetString = "|abs(" + originalString.substring(2, originalString.length() - 1) + ")";
        } else if (originalString.startsWith("|") && originalString.endsWith("|")) {
            targetString = "abs(" + originalString.substring(1, originalString.length() - 1) + ")";
        } else if (originalString.contains("||")) {
            if (originalString.substring(0, 2).equals("||")) {
                targetString = "|abs(" + originalString.replace("|", "") + ")";
            } else if (originalString.substring(originalString.length() - 2, originalString.length()).equals("||")) {
                targetString = "abs(" + originalString.replace("|", "") + ")|";
            } else {
                return "";
            }
        } else if (statisticalCharInString(originalString, '|') > 2) {
            if (!originalString.substring(0, 1).equals("|")) {
                return "";
            }
            String local = replaceAbsolute(originalString, pattern);
            if (StringUtils.isEmpty(local)) {
                return "";
            }
            targetString = local;

        }
        return targetString;
    }

    /**
     * 将|x-1|--->  abs(x-1)
     */
    private static void absoluteParse(StringBuffer ansStringBuffer) {
        //String reg2 = "\\|.+?(?<!(\\+|\\-|\\*|\\/))\\|";//绝对值符号之间必须要用内容,而且第二个绝对值符号前不能是运算符号
        //String reg2 = "\\|[^,⊆⊂∈∩∪#%#]+?(?<!(\\+|\\-|\\*|/))\\|";//绝对值符号之间必须要用内容,而且第二个绝对值符号前不能是运算符号
        // $A=\{(x,y)|y≥\frac{1}{2}\left|x-2\right|\}$
        String reg2 = "(?<!(\\([a-z],[a-z]\\)))\\|[^,⊆⊂∈∩∪#%#]+?(?<!(\\+|\\-|\\*|/|lg|ln))\\|(?!\\|)";//绝对值符号之间必须要用内容,而且第二个绝对值符号前不能是运算符号
        Pattern pattern = Pattern.compile(reg2);
        Matcher match = pattern.matcher(ansStringBuffer);
        int count = 0;                //防止死循环的计数器
        while (match.find() && count++ < 8) {
            String originalString = match.group();
            String targetString;
            if (originalString.contains("||") || statisticalCharInString(originalString, '|') > 2) {//复合绝对值关系
                targetString = multiAbsoluteReplace(originalString, pattern);
                if (StringUtils.isEmpty(targetString)) {
                    continue;
                }
            } else {
                targetString = "abs(" + originalString.replace("|", "") + ")";
            }
            ansStringBuffer.replace(match.start(), match.end(), targetString);
            match = pattern.matcher(ansStringBuffer);
        }
    }

    private static void absoluteParseOld(StringBuffer ansStringBuffer) {
        String reg2 = "\\|.*?\\|";
        Matcher match = Pattern.compile(reg2).matcher(ansStringBuffer);
        int count = 0;                //防止死循环的计数器
        while (match.find() && count++ < 5) {
            int start = match.start();
            int end = match.end();
            String originalString = ansStringBuffer.substring(start, end);
            String targetString = "abs(" + originalString.replace("|", "") + ")";
//        	originalString = originalString.replace("|", "\\|");
            ansStringBuffer.replace(start, end, targetString);
//        	ansStringBuffer.setCharAt(end-1, ')');
//			ansStringBuffer.delete(start, start+1);
//			ansStringBuffer.insert(start,"abs(");
//			match = Pattern.compile(reg2).matcher(ansStringBuffer);
//        	LTools.stringBufferReplaceAll(ansStringBuffer, originalString, targetString);
            //TODO
//        	globalInformation.getLocationlocal().get().replaceString(start, end, targetString, globalInformation);
            match = Pattern.compile(reg2).matcher(ansStringBuffer);
        }

    }


    /**
     * 兼容S△ABC的格式   --> S_△ABC
     */
    private static void corrctFormOfArea(StringBuffer stringBuffer) {

        //兼容$S△ABC=2$的形式
        Matcher matcher = Pattern.compile("([SC])△").matcher(stringBuffer);
        int count = 0;
        while (matcher.find() && count++ < 10) {
            int end = matcher.end(1);
            stringBuffer.insert(end, "_");
            matcher = Pattern.compile("([SC])△").matcher(stringBuffer);
        }
    }

    /**
     * 处理带下标的点的花括号的问题
     */
    private static void removeBracesOfSubPoint(StringBuffer ansStringBuffer) {
        String regex = "(?<!(bar|frac|sqrt|\\}|\\]))\\{[a-zA-Z]_[0-9]+?\\}";
        Matcher subMatcher = Pattern.compile(regex).matcher(ansStringBuffer);
        int count = 0;
        while (subMatcher.find() && count++ < 10) {
            String matchStr = subMatcher.group();
            ansStringBuffer.replace(subMatcher.start(), subMatcher.end(), matchStr.replace("{", "").replace("}", ""));
            subMatcher = Pattern.compile(regex).matcher(ansStringBuffer);
        }
    }

    /**
     * 找出没有带latex标识符坐标形式
     * A$B$C$D$E  在 A C E区间找
     */
    private static void findCoordinate(StringBuffer ansStringBuffer) {
        int start = 0;
        List<Integer> insertList = new ArrayList<>();
        Matcher matcher = RegularExpressionTools.getRegexMatcher(RegularExpression.latexregex, ansStringBuffer);
        while (matcher.find()) {
            String subString = ansStringBuffer.substring(start, matcher.start());
            //从subString里面找有没有坐标形式，并记录下插入的位置
            findCoordinateHelper(subString, start, insertList);
            start = matcher.end();
        }
        if (start < ansStringBuffer.length()) {
            String subString = ansStringBuffer.substring(start, ansStringBuffer.length());
            findCoordinateHelper(subString, start, insertList);
        }
        LTools.insertStringInStringBuffer(ansStringBuffer, insertList, "$");
    }

    private static void findCoordinateHelper(String subString, int start, List<Integer> insertList) {
        Matcher matcher = RegularExpressionTools.getRegexMatcher(POINTWITHCOORDREGEX_STRING, subString);
        while (matcher.find()) {
            String groupString = matcher.group();
            if (LTools.isCoordForm(groupString)) {
                if(subString.charAt(start+matcher.start()-1)!='{'){
                    insertList.add(start + matcher.start());
                    insertList.add(start + matcher.end());
                }
            }
        }
    }

    /**
     * 消除$$
     */
    private static void removeEmptyLatext(StringBuffer ansStringBuffer) {
        List<Integer> removePos = new ArrayList<>();
        Matcher latexMatcher = RegularExpressionTools.getRegexMatcher(RegularExpression.latexregex, ansStringBuffer);
        while (latexMatcher.find()) {
            if (latexMatcher.end() - latexMatcher.start() == 2) {
                removePos.add(latexMatcher.start());
                removePos.add(latexMatcher.end() - 1);
            }
        }
        if (removePos.size() == 0) {
            return;
        }
        for (int i = removePos.size() - 1; i >= 0; i--) {
            ansStringBuffer.deleteCharAt(removePos.get(i));
        }
    }

    /**
     * $A({1,2})$  -->  $A(1,2)$
     */
    private static void removeBracesOfCoordinate(StringBuffer ansStringBuffer) {
        String regexWithDollar = "\\$" + POINTWITHCOORDREGEX_STRING + "\\$";
        Matcher matcher = RegularExpressionTools.getRegexMatcher(regexWithDollar, ansStringBuffer);
        int count = 0;
        while (matcher.find() && count++ < 10) {
            int start = matcher.start();
            int end = matcher.end();
            String oldString = ansStringBuffer.substring(start, end);
            String coordinate = oldString.replace("$", "");
            int firstIndex = coordinate.indexOf('(');
            int lastIndex = coordinate.lastIndexOf("");
            if (LTools.isCoordForm(coordinate) && isBraceMatch(firstIndex + 1, lastIndex - 2, coordinate)) {
                String newString = "$" + coordinate.substring(0, firstIndex + 1) + coordinate.substring(firstIndex + 2, lastIndex - 2) + ")$";
                ansStringBuffer.replace(start, end, newString);
                matcher = Pattern.compile(regexWithDollar).matcher(ansStringBuffer);
            }
        }
    }

    private static boolean isBraceMatch(int leftIndex, int rightIndex, String coordinateStr) {
        if (coordinateStr.charAt(leftIndex) != '{') return false;
        int odd = 1;
        for (int it = leftIndex + 1; leftIndex <= rightIndex; it++) {
            if (odd < 1) return false;
            if (coordinateStr.charAt(it) == '{') odd++;
            if (coordinateStr.charAt(it) == '}') odd--;
        }
        return 0 == odd;
    }

    /**
     * AB两点-->A,B两点
     */
    private static void splitTwoPoints(StringBuffer stringBuffer) {

        String regex = "([A-Z](('+|_[0-9]+)|)){2,10}(两|三|四|五|六|七|八|九|十)点";
        Matcher matcher = RegularExpressionTools.getRegexMatcher(regex, stringBuffer);
        int count = 0;
        while (matcher.find() && count++ < 5) {
            int start = matcher.start();
            int end = matcher.end();
            String tempString = stringBuffer.substring(start, end);
            Matcher matcher2 = RegularExpressionTools.getRegexMatcher("[A-Z]", tempString);
            String newString = "";
            int startIndex = 0;
            int endIndex = startIndex;
            if (matcher2.find()) ;
            while (matcher2.find()) {
                endIndex = matcher2.start();
                newString += tempString.substring(startIndex, endIndex) + ",";
                startIndex = endIndex;
            }
            newString += tempString.substring(endIndex);
            stringBuffer.replace(start, end, newString);
            matcher = Pattern.compile(regex).matcher(stringBuffer);
        }
    }


    /**
     * {sin^2}  -->		sin^2
     */
    private static void filterForTrigonometric(StringBuffer ansStringBuffer) {

        String regex = "\\{(sin|cos|tan|cot|sec|csc)\\^[0-9]+\\}";
        Matcher matcher = Pattern.compile(regex).matcher(ansStringBuffer);
        int count = 0;
        while (matcher.find() && count++ < 10) {
            int start = matcher.start();
            int end = matcher.end();
            String tempString = ansStringBuffer.substring(start, end).replace("{", "").replace("}", "");
            ansStringBuffer = ansStringBuffer.replace(start, end, tempString);
            matcher = Pattern.compile(regex).matcher(ansStringBuffer);
        }
        regex = "(?<!(\\frac|\\}))\\{[a-z]('+|_[0-9]+|)\\^[0-9]+\\}";
        matcher = Pattern.compile(regex).matcher(ansStringBuffer);
        count = 0;
        while (matcher.find() && count++ < 10) {
            int start = matcher.start();
            int end = matcher.end();
            String tempString = ansStringBuffer.substring(start, end).replace("{", "(").replace("}", ")");
            ansStringBuffer = ansStringBuffer.replace(start, end, tempString);
            matcher = Pattern.compile(regex).matcher(ansStringBuffer);
        }
    }

    /**
     * 对每个过滤函数单独测试
     */
   /* public static String[] singleFilter(String[] res, GlobalInformation globalInformation) {
        if (res == null) {
            return null;
        }

        String[] tar = new String[res.length];

        StringBuffer stringBuffer = new StringBuffer(res[0]);
        removeBracesOfSubPoint(stringBuffer);
        tar[0] = stringBuffer.toString();

        stringBuffer = new StringBuffer(res[1]);
        findCoordinate(stringBuffer);
        tar[1] = stringBuffer.toString();

        stringBuffer = new StringBuffer(res[2]);
        removeBracesOfCoordinate(stringBuffer);
        tar[2] = stringBuffer.toString();

        stringBuffer = new StringBuffer(res[3]);
        removeEmptyLatext(stringBuffer);
        tar[3] = stringBuffer.toString();

        stringBuffer = new StringBuffer(res[4]);
        splitTwoPoints(stringBuffer);
        tar[4] = stringBuffer.toString();

        stringBuffer = new StringBuffer(res[5]);
//        correctThePosOfDollar(stringBuffer);
        tar[5] = stringBuffer.toString();

        stringBuffer = new StringBuffer(res[6]);
        filterForTrigonometric(stringBuffer);
        tar[6] = stringBuffer.toString();

        stringBuffer = new StringBuffer(res[7]);
        corrctFormOfArea(stringBuffer);
        tar[7] = stringBuffer.toString();

        stringBuffer = new StringBuffer(res[8]);
        absoluteParse(stringBuffer);
        tar[8] = stringBuffer.toString();
        return tar;
    }*/
}
