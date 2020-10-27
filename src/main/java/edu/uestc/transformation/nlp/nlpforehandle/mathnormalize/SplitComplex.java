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
 * Created by ZZH on 2016/9/5.
 * 复杂拆分
 */
public class SplitComplex {


//    /**
//     * 总流程,对外调用函数
//     */
//    public static String handleComplexSentence(String sorString) {
//        String result = "";
//        if (sorString.contains("#%#")) {
//            String[] subQuestions = sorString.split("#%#");
//            for (String subQuestion : subQuestions)
//                //如果再加处理,则重构;使用责任链(或者别的),把每个单独动作放在独立的类中
//                result += processChain(subQuestion) + "#%#";
//        } else {
//            result = processChain(sorString);
//        }
//        return result;
//    }




    /**
     * 将几个处理函数统一在此调用
     *
     * @param text 被处理串
     * @return 处理后的串
     */
    public static String processChain(String text) {
        String result = text;
        try {
            result = removeChineseAndAddOut(text);
            result = extractParameters(result);
            result = extractCertainBrackesContentToSentenceHead(result);
            result = splitVariableAttribute(result);
        } catch (Exception e) {
            result = text;
        }
        return result;
    }

    /**
     * 把  S_(菱形ABCD)     这类型的替换为     菱形ABCD , S_(ABCD)
     */
    private static String removeChineseAndAddOut(String tempString) {
        String result = "";
        if (StringUtils.isEmpty(tempString)) {
            return result;
        }
        String regex = "(?<=S_\\()((平行|)四边形|正方形|矩形|菱形|梯形)[A-Z]{4}(?=\\))";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(tempString);
        while (matcher.find()) {
            result += matcher.group() + ",";
        }
        result = result + tempString.replaceAll("(?<=S_\\()((平行|)四边形|正方形|矩形|菱形|梯形)(?=[A-Z]{4}\\))", "");
        return result;
    }


    /**
     * 将变量属性从一个有多个关系的句子中拆分出来
     *
     * @param sorString 原始串
     * @return 提取变量属性后的串
     * @throws Exception
     */
    private static String splitVariableAttribute(String sorString) throws Exception {
        final String[] numAttribute = {
                "实常数",
                "正数",
                "负数",
                "非负数",
                "实数",
                "正实数",
                "有理数",
                "无理数",
                "整数",
                "正整数",
                "负整数",
                "非负整数",
                "奇数",
                "偶数",
                "常数",
                "自然数",
                "负实数"
        };

        String result = sorString;
        String operator = "=|<|>|≥|≤|∈";
        final String singleVar = "[a-zA-Zαβγδεζηθικλμνξορστυφχψω](_\\d+|)(?=" + operator + "|)";
        final String severalVar = "(([a-zA-Zαβγδεζηθικλμνξορστυφχψω](_\\d+|)([,和与][a-zA-Zαβγδεζηθικλμνξορστυφχψω](_\\d+|)|)+)(?=[,a-zA-Zαβγδεζηθικλμνξορστυφχψω\\u4E00-\\u9FA5](_\\d+|)))";
        //缺少非零实数等的属性,待后端加上后加上
//        String attribute = LTools.createRegexOrByStringList(numAttribute);
        String attribute = "(非零|)(" + LTools.createRegexOrByStringList(numAttribute) + ")";
        String regex = "(?<attribute>" + attribute + "(?!,|和|与))(?<variable>" + severalVar + "|" + singleVar + ")";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(result);
        while (matcher.find()) {
            String var = matcher.group("variable");
            String attr = matcher.group("attribute");
            int matchStart = result.indexOf(matcher.group());
            int matchEnd = matchStart + matcher.group().length();
            String beforeReminder = result.substring(0, matchStart);
            String afterReminfer = result.substring(matchEnd);
            var = var.endsWith(",") ? var.substring(0, var.length() - 1) : var;
            String is = RegularExpressionTools.isMatcher("和|与|,", var) ? "都是" : "是";
            result = String.format("%s%s%s%s%s%s%s", var, is, attr, ",", beforeReminder, var, afterReminfer);
//            if (var.endsWith(COMMA)) {
//                result = String.format("%s%s%s%s%s%s%s", var.substring(0, var.length() - 1), "是", attr, COMMA, beforeReminder, var, afterReminfer);
//            } else {
//                result = String.format("%s%s%s%s%s%s%s", var, "是", attr, COMMA, beforeReminder, var, afterReminfer);
//            }
        }
        return result;
    }

    /**
     * 将含中文字符的括号中的内容提取到句首
     *
     * @author PM
     */
    private static String extractParameters(String text) {
        List<Integer> start = new ArrayList<>();
        List<String> anwser = new ArrayList<>();
        char[] textchar = text.toCharArray();

        for (int i = 0; i < textchar.length; i++) {
            if (textchar[i] == '(' || textchar[i] == '[') {
                start.add(i);
            }
            if (textchar[i] == ')' || textchar[i] == ']' && start.size() > 0) {
                //输出括号中的值
                int temp = start.get(start.size() - 1);
                String tempString = new String(textchar, temp, i - temp + 1);
                start.remove(start.size() - 1);
                anwser.add(tempString);
            }
        }
        Pattern pattern = Pattern.compile("(.*)[\u4e00-\u9fa5]+(.*)");
//        Pattern pattern = Pattern.compile("(.*)[\u4e00-\u9fa5]+(.*)");
        for (String details : anwser) {
            Matcher matcher = pattern.matcher(details);
            while (matcher.find()) {
                String temp = details.substring(1,details.length()-1);
                text = temp + "," + text.replace(details, "");
            }
        }
        return text;
    }


    /**
     * 提取括号中包含的内容
     * 限制条件一:(含二元关系符)表达式旁边的括号内容不提取
     * 限制条件二:中文旁边的中文不提取
     * 限制条件三:括号包括圆括号和方括号
     * 限制条件四:括号中需要包含∈,>,<,≥,≤
     */
    private static String extractCertainBrackesContentToSentenceHead(String text) {
        String result = text;
        List<String> anwser = getBracketsAndContent(text);

        Pattern pattern = Pattern.compile("(.*)[∈><≥≤≠\u4e00-\u9fa5]+(.*)");
//        Pattern pattern = Pattern.compile("(.*)[∈]+(.*)");
        for (String details : anwser) {
            Matcher matcher = pattern.matcher(details);
            while (matcher.find()) {
                String s = matcher.group();
//                s = UtilFunction.mergeBrackets(s);
                if (!s.startsWith("(") || !s.endsWith(")") || result.contains("方程") || result.contains("不等式"))
                    break;
                String temp = details.substring(1, details.length() - 1);
                result = temp + "," + result.replace(details, "");
            }
        }
        return result;
    }


    /**
     * 根据括号的临接字符串截取某些字符串
     *
     * @param text 题目字符串
     * @return 括号及其内部内容
     */
    private static List<String> getBracketsAndContent(String text) {
        List<String> anwser = new ArrayList<>();
        List<Integer> start = new ArrayList<>();
        char[] textchar = text.toCharArray();

        for (int i = 0; i < textchar.length; i++) {
            if (textchar[i] == '(' || textchar[i] == '[') {
                start.add(i);
            }
            if ((textchar[i] == ')' || textchar[i] == ']') && start.size() > 0) {
                //输出括号中的值
                int temp = start.remove(start.size() - 1);
//                String te = text.substring(temp, i + 1);
                if (cheekTheFrontOfBracket(temp - 1, text) && cheekBackOfBracket(i, text)) {
                    //去除括号
                    String tempString = new String(textchar, temp, i - temp + 1);
                    anwser.add(tempString);
                }
            }
        }
        return anwser;
    }

    /**
     * 检查括号临接的字符串是否符合要求
     *
     * @param locEnd 左括号前一个字符所在位置
     * @param text   问题字符串
     * @return 括号临接的字符串是否为带二元关系符的表达式\中文或者无字符, 则失败;反之,则成功
     * 注:逗号要不要考虑在限制条件内?
     */
    private static boolean cheekTheFrontOfBracket(int locEnd, String text) {

        //在左括号前无字符,则失败
        if (locEnd == -1)
            return true;

        char[] textChar = text.toCharArray();
        int locStart = -1;
        for (int i = locEnd; i >= 0; i--) {
            boolean isChinese = RegularExpressionTools.isMatcher("[\u4e00-\u9fa5]", String.valueOf(textChar[i]));
            if (isChinese || i == 0) {
                locStart = i;
                break;
            }
        }
        String noChinese = text.substring(locStart, locEnd + 1);
        if (StringUtils.isEmpty(noChinese)) {
            return false;
        }
        //是否为带二元符的表达式
        return !RegularExpressionTools.isMatcher(RegularExpression.OPERATOR, noChinese);

    }

    private static boolean cheekBackOfBracket(int locStart, String text) {
        int textLength = text.length() - 1;
        if (locStart < textLength) {
            char afterWord = text.charAt(locStart + 1);
            return !RegularExpressionTools.isMatcher("(①|②|③|④|⑤|⑥|⑦|⑧|⑨|⑩|#|=|≥|>|＞|＜|≤|<|≠|\\+|\\-|\\*|\\/|\\)|\\()", afterWord + "");
        } else if (locStart == textLength) {
            return true;

        }
        return false;
    }

}
