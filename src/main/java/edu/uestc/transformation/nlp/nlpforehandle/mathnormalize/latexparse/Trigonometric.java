package edu.uestc.transformation.nlp.nlpforehandle.mathnormalize.latexparse;


import edu.uestc.transformation.nlp.nlpforehandle.regularexpressiontools.RegularExpressionTools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Trigonometric {

    int superscriptIndex = -1;
    trigonometricType firstType = null;
    trigonometricType type = null;
    String latinRegex = "(α|β|γ|δ|ε|ζ|η|θ|ι|κ|λ|μ|ν|ξ|ο|ρ|σ|τ|υ|φ|χ|ψ|ω|Δ)";
    char[] latins = {'α', 'β', 'γ', 'δ', 'ε', 'ζ', 'η', 'θ', 'ι', 'κ', 'λ', 'μ', 'ν', 'ξ', 'ο', 'ρ', 'σ', 'τ', 'υ', 'φ', 'χ', 'ψ', 'ω', 'Δ'};

    /**
     * 将三角函数的幂移至后端可以要求的位置
     * 例如:
     * "{cos}^{2}{x+1}"变为"({cos}{x+1})^{2}"
     */
    public String trigonometricParse(String str, int start, String replacestr) {
        String regex = "(?<head>(\\{|)(\\{|)(sin|cos|tan|cot|sec|csc)(\\}|))(?<index>\\^(\\{|\\(|)\\d+(\\}|\\)|))(?<content>(\\}|)(\\{|\\(|[αβγδεζηθικλμνξορστυφχψω\\da-z°]+))";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()){
            String origin;
            String content;
            if(matcher.group("content").endsWith("{") || matcher.group("content").endsWith("(")) {//三角函数内值为非单个元素时,寻找其值的边界
                char bracket = matcher.group("content").endsWith("{") ? '{' : '(';
                int contentStart = matcher.end() - 1;
                int contentEnd = findContentEnd(contentStart, str, bracket);
                if (contentEnd == -1)
                    continue;
                origin = str.substring(matcher.start(), contentEnd + 1);
                content = str.substring(matcher.group("content").startsWith("}") ?contentStart-1 : contentStart, contentEnd + 1);
            }else {
                origin = matcher.group();
                content = matcher.group("content");
            }
            String newstr = "(" + matcher.group("head") + "" + content+ ")" + matcher.group("index");

            replacestr = replacestr.replace(origin, newstr);
        }
        return replacestr;

    }

    /**
     *给定一个"{"或者'('在字符串中的位置,返回对应的"}"或')'在字符串中的位置
     * 若'}'或者')'后面有'°',则返回'°'的位置索引
     */
    private int findContentEnd(int contentStart, String str, char bracket){
        char rev_bracket = bracket == '{'? '}' : ')';
        char[] words = str.toCharArray();
        int deep = 0;
        int loc = -1;
        if(words[contentStart] != bracket)
            return -1;
        for(int i = contentStart; i < str.length(); i++){
            if(words[i] == bracket) deep++;
            if(words[i] == rev_bracket) deep--;
            if(deep == 0) {
                loc = i;
                break;
            }
        }
        if(words[loc+1] == '°'){
            loc++;
        }
        return loc;
    }


    private int getEndIndex2(int start, String str) {
        int end = -1;
        for (int i = start + 2; i < str.length(); i++) {
            char c = str.charAt(i);
            if (i == start + 2 && c == '(') {
                end = getEndBracketIndex(i, str);
                break;
            } else if (i == start + 2 && c == '{') {
                end = getEndBraceIndex(i, str);
                break;
            }
            Matcher matcher = RegularExpressionTools.getRegexMatcher("(\\+|-|\\*|/|,|=|\\$)", c + "");
            if (matcher.find()) {
                end = i - 1;
                firstType = trigonometricType.lowcase;
                break;
            }
        }
        return end;
    }

    /**
     * 获取结束位置
     */
    private int getEndIndex(int start, String str) {
        int end = -1;
        int UpcaseNum = 0;
        for (int i = start + 3; i < str.length(); i++) {
            char c = str.charAt(i);
            if (type == null) {
                if (isNumber(c)) {
                    type = trigonometricType.number;
                    end = i;
                } else if (isLowerCase(c)) {
                    type = trigonometricType.lowcase;
                    end = i;
                } else if (isUpperCase(c)) {
                    type = trigonometricType.upcase;
                    end = i;
                    UpcaseNum++;
                } else if (c == '∠') {
                    type = trigonometricType.anglesymbol;
                } else if (c == '(') {
                    type = trigonometricType.bracket;
                    end = getEndBracketIndex(i, str);
                    firstType = type;
                    break;
                } else if (c == '^') {
                    type = trigonometricType.superscript;
                    superscriptIndex = i;
                    end = -1;
                } else if (c == '{') {
                    type = trigonometricType.brace;
                    end = getEndBraceIndex(start, str);
                    firstType = type;
                    break;
                }
                firstType = type;
            } else if (type == trigonometricType.number) {
                if (isNumber(c)) {
                    end = i;
                } else {
                    break;
                }
            } else if (type == trigonometricType.lowcase) {
                if (c == '_') {
                    type = trigonometricType.subsymbol;
                    end = -1;
                } else if (c == '\'') {
                    type = trigonometricType.singlequote;
                    end = i;
                } else {
                    break;
                }
            } else if (type == trigonometricType.upcase) {
                if (c == '\'') {
                    type = trigonometricType.singlequote;
                    end = i;
                } else if (c == '_') {
                    type = trigonometricType.subsymbol;
                    end = -1;
                } else if (isUpperCase(c) && UpcaseNum == 1) {
                    end = -1;
                    UpcaseNum++;
                } else if (isUpperCase(c) && UpcaseNum == 2) {
                    end = i;
                    break;
                } else {
                    break;
                }
            } else if (type == trigonometricType.anglesymbol) {
                if (isUpperCase(c)) {
                    type = trigonometricType.upcase;
                    end = i;
                    UpcaseNum++;
                } else {
                    break;
                }
            } else if (type == trigonometricType.subsymbol) {
                if (isNumber(c)) {
                    type = trigonometricType.number;
                    end = i;
                } else if (isLowerCase(c)) {
                    type = trigonometricType.lowcase;
                    end = i;
                    break;
                } else if (isUpperCase(c)) {
                    type = trigonometricType.upcase;
                    end = i;
                    break;
                } else {
                    break;
                }
            } else if (type == trigonometricType.singlequote) {
                if (c == '\'') {
                    end = i;
                } else if (isUpperCase(c) && UpcaseNum == 1) {
                    type = trigonometricType.upcase;
                    end = -1;
                } else if (isUpperCase(c) && UpcaseNum == 2) {
                    end = i;
                    break;
                } else {
                    break;
                }
            } else if (type == trigonometricType.superscript) {    //^后面只接一个数字
                if (isNumber(c)) {
                    type = trigonometricType.numberaftersupscript;
                }
                end = -1;
            } else if (type == trigonometricType.numberaftersupscript) {
                if (isNumber(c)) {
                    type = trigonometricType.number;
                    end = i;
                } else if (isLowerCase(c)) {
                    type = trigonometricType.lowcase;
                    end = i;
                } else if (isUpperCase(c)) {
                    type = trigonometricType.upcase;
                    UpcaseNum++;
                    end = i;
                } else if (c == '(') {
                    type = trigonometricType.bracket;
                    end = getEndBracketIndex(i, str);
//					firstType = type;
                    break;
                } else if (c == '∠') {
                    type = trigonometricType.anglesymbol;
                    end = -1;
                } else {
                    break;
                }
            }
        }
        return end;
    }

    /**
     * 获取花括号的结束位置
     */
    private int getEndBraceIndex(int start, String str) {
        int len = str.length();
        int index = start + 3;
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
            if (backStr.contains("Pi") || backStr.contains("pi") || backStr.contains("PI")) {
                newString = replace(foreStr, "(" + backStr + ")");
            } else {
                newString = replace(foreStr, "(" + backStr + ")");
//                newString = replace(foreStr, "(" + backStr + "*Pi/180)");
            }
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
            backStr = backStr.replace("{", "(").replace("}", ")");
            String t = backStr.substring(1, backStr.length() - 1);//sin(600°)
            if (t.equals(transToRadian(t))) {
                newString = replace(foreStr, backStr);
            } else {
                newString = replace(foreStr, transToRadian(t));
            }
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
        Matcher matcher1 = Pattern.compile("^(\\d+)(°|)$").matcher(backStr);
        if (matcher.find()) {
            backStr = "(" + matcher.group(1) + ")";
//            backStr = "(" + matcher.group(1) + "*Pi/180)";
        }
        if (matcher1.find()) {
            backStr = "(" + matcher1.group(1) + ")";
//            backStr = "(" + matcher1.group(1) + "*Pi/180)";
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
                    content = "(" + content + ")";
//                    content = "(" + content + "*pi/(180))";
                } else {
                    content = "(" + content + ")";
//                    content = "((" + content + ")*pi/(180))";
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