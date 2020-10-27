package edu.uestc.transformation.nlp.nlpforehandle.mathnormalize.latexparse;



import edu.uestc.transformation.nlp.nlpforehandle.regularexpressiontools.RegularExpressionTools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liuyy on 2016/1/20.
 */
public class LogParse {

    int superscriptIndex = -1;
    trigonometricType firstType = null;
    trigonometricType type = null;
    String latinRegex = "(α|β|γ|δ|ε|ζ|η|θ|ι|κ|λ|μ|ν|ξ|ο|ρ|σ|τ|υ|φ|χ|ψ|ω|Δ)";
    char[] latins = {'α', 'β', 'γ', 'δ', 'ε', 'ζ', 'η', 'θ', 'ι', 'κ', 'λ', 'μ', 'ν', 'ξ', 'ο', 'ρ', 'σ', 'τ', 'υ', 'φ', 'χ', 'ψ', 'ω', 'Δ'};

    public String LogParse(String str, int start, String replacestr){
//        Matcher matcher= RegularExpressionTools.getRegexMatcher("\\{(\\{|)\\\\log(\\}|)_(.+?)\\}", str);
//        Matcher matcher2= RegularExpressionTools.getRegexMatcher("(\\\\|)log_\\{(.+?)\\}", str);
//        if (matcher.find(start)){
//            replacestr=replacestr.replace(str.substring(matcher.start(),matcher.end()),"log["+matcher.group(3)+"]");
//        }else if(matcher2.find(start)){
//            replacestr=replacestr.replace(str.substring(matcher2.start(),matcher2.end()),"log["+matcher2.group(2)+"]");
//        }else if(str.contains("log_(")){
//            start=start+"log".length();
//            int end=getEndBracketIndex(start,str);
//            String d=str.substring(start+1, end+1);
//            replacestr=replacestr.replace(d, "[" + d+ "]");
//        }
//
//
//        Matcher matcherx= RegularExpressionTools.getRegexMatcher("(?<=(\\]))(abs\\([a-z0-9\\.]+\\)|[a-z0-9\\.]+)",replacestr);
//        if (matcherx.find()){
//            String s="("+matcherx.group()+")";
//            replacestr=matcherx.replaceFirst(s);
//        }
        return replacestr;
    }


    /**
     *
     */
    public String LogParse2(String str, int start, String replacestr) {
//        Matcher matcher=RegularExpressionTools.getRegexMatcher("^(?<=[a-zA-Z\\d]+?)ln([a-z]+?)", str);
//        if (matcher.find()){
//            replacestr=replacestr.replace(str.substring(matcher.start(),matcher.end()),"ln("+matcher.group(1)+")");
//        }else{
//            int end = getEndIndex(start, str);
//            if (end != -1 && start < end + 1) {
//                String oldString = str.substring(start, end + 1);
//                    String foreStr = oldString.substring(0, 2).replace("\\", "");
//                    String backStr = oldString.substring(2);
//                replacestr = replace(foreStr, backStr, oldString, replacestr);
//            }
//        }
        return replacestr;
    }

    /**
     * 找出ln包含的数的最后一个字符所在位置
     * 如(lng(t))<4中找出的为g(t))的")"的位置
     * @param start ln开始的位置
     * @param str  原串
     */
    private int getEndIndex(int start, String str) {
        int end = -1;
        String abs = str.substring(start + 2 ,start + 5);
        for (int i = start + 2; i < str.length(); i++) {
            char c = str.charAt(i);
            if (i == start + 3 && c == ')'){
                end = getEndBraceIndex(i, str);
                firstType = trigonometricType.lowcase;
                break;
            }else if (i == start + 2 && c == '(') {
                end = getEndBracketIndex(i, str);
                firstType= trigonometricType.bracket;
                break;
            }else if (i == start + 2 && c == '{') {
                end = getEndBraceIndex(i, str);
                firstType= trigonometricType.brace;
                break;
            }else if("abs".equals(abs) && c == '('){
                end = getEndBracketIndex(i + 1, str);
                firstType = trigonometricType.lowcase;
                break;
            }
            Matcher matcher = RegularExpressionTools.getRegexMatcher("(\\+|-|\\*|/|,|=|\\$|<|>|≥|≤≠)", c + "");
            if (matcher.find()) {
                end = i - 1;
                firstType = trigonometricType.number;
                break;
            }
        }
        return end;
    }

    /**
     * 获取花括号的结束位置
     */
    private int getEndBraceIndex(int start, String str) {
        int len = str.length();
        int index = start + 2;
        int loop = 1;
        while (loop > 0 && ++index < len) {
            char ch = str.charAt(index);
            if (ch == '{')
                loop++;
            else if (ch == '}')
                loop--;
        }
        if (loop == 0)
            return index;
        return -1;
    }

    /**
     * 获取括号的结束位置
     */
    private int getEndBracketIndex(int start, String str) {

        int len = str.length();
        int index = start + 1;
        int loop = 1;
        while (loop > 0 && ++index < len) {
            if (str.charAt(index) == '(')
                loop++;
            else if (str.charAt(index) == ')')
                loop--;
        }
        if (loop == 0)
            return index;
        return -1;
    }

    private String replace(String foreStr, String backStr,
                           String oldString, String replacestr) {

        String newString = null;
        if (firstType == trigonometricType.number) {
            newString = replace(foreStr, "(" + backStr + ")");
        } else if (firstType == trigonometricType.lowcase) {
            newString = replace(foreStr, "(" + backStr + ")");
        } else if (firstType == trigonometricType.upcase) {
            newString = replace(foreStr, "(∠" + backStr) + ")";
        } else if (firstType == trigonometricType.anglesymbol) {
            newString = replace(foreStr, "(" + backStr + ")");
        } else if (firstType == trigonometricType.brace) {
            backStr = transToRadian(backStr);
            newString = replace(foreStr, backStr);
        } else if (firstType == trigonometricType.bracket) {
            //backStr = backStr.replace("{", "(").replace("}", ")");
            newString = replace(foreStr, backStr);
        } else if (firstType == trigonometricType.superscript) {
            newString = replaceSuperScriptCase(foreStr, backStr);
        }
        if (newString != null) {
            replacestr = replacestr.replace(oldString, newString);
        }
        return replacestr;
    }

    private String transToRadian(String backStr) {
        Matcher matcher = Pattern.compile("^\\{(\\d+)(°|)\\}$").matcher(backStr);
        if (matcher.find()) {
            backStr = "(" + matcher.group(1) + "*pi/180)";
        }
        return backStr;
    }

    /**
     * 对后面接(的类型进行替换
     */
    private String replace(String foreStr, String backStr) {
        String newString = foreStr + backStr;
        if (foreStr.equals("cot")) {
            newString = "(1" + "/tan" + backStr + ")";
        } else if (foreStr.equals("sec")) {
            newString = "(1" + "/cos" + backStr + ")";
        } else if (foreStr.equals("csc")) {
            newString = "(1" + "/sin" + backStr + ")";
        }
        return newString;
    }

    private String replaceSuperScriptCase(String foreStr, String backStr) {

        if (backStr.length() > 2) {
            String superScriptStr = backStr.substring(0, 2);
            String content = backStr.substring(2);

            if (Pattern.matches("[.[^a-zA-Z'_]]+", content) && !containedLatins(content)) {
                if (content.charAt(0) == '(') {
                    content = "(" + content + "*pi/(180))";
                } else {
                    content = "((" + content + ")*pi/(180))";
                }
            } else if (Pattern.matches("([A-Z]('+|_[0-9]+|))+", content)) {
                content = "∠" + content;
            } else if (Pattern.matches("([a-z]|" + latinRegex + ")('+|_[0-9]+|)", content)) {
                content = "(" + content + ")";
            }
            return "(" + foreStr + content + ")" + superScriptStr;
        }
        return null;

    }

    /**
     * 判断是否是数字
     */
    private boolean isNumber(char c) {
        return c - '0' >= 0 && c - '0' <= 9;
    }

    /**
     * 是否为小写字母
     */
    private boolean isLowerCase(char c) {
        if (Character.isLowerCase(c)) {
            return true;
        }
        for (char ch : latins) {
            if (ch == c)
                return true;
        }
        return false;
    }

    private boolean containedLatins(String string) {
        for (char ch : latins) {
            if (string.contains(String.valueOf(ch)))
                return true;
        }
        return false;
    }

    /**
     * 是否是大写字母
     */
    private boolean isUpperCase(char c) {
        return c - 'A' >= 0 && c - 'A' <= 26;
    }

    private enum trigonometricType {
        number,                //数字
        lowcase,            //小写字母
        upcase,                //大写字母
        anglesymbol,        //角∠
        subsymbol,            //下标_
        singlequote,        //单引号'
        superscript,        //上角标^
        numberaftersupscript,//^后面的数字
        bracket,                //括号
        brace                    //花括号
//		bracketaftersupscript	//^后面的括号

    }
}
