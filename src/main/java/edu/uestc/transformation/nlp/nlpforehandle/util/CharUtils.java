package edu.uestc.transformation.nlp.nlpforehandle.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CharUtils {

    public static boolean isNumber(char c) {
        if (c == 'π') return true;
        String charString = String.valueOf(c);
        Pattern p = Pattern.compile("([0-9]|')");
        Matcher match = p.matcher(charString);
        return match.find();
    }

    public static boolean isUpperCase(char c) {
        String charString = String.valueOf(c);
        Pattern p = Pattern.compile("[A-Z]");
        Matcher match = p.matcher(charString);
        return match.find();
    }

    /**
     * p判断是否为汉字 编码为utf-8格式
     */
    public static boolean isChinese(char c) {
        if (c == '$' || c == '，' || c == '。' || c == ',' || c == '.') {
            return true;
        }
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否为小写字母，包括希腊字母
     */
    public static boolean isLowerCase(char c) {
        String latin = "(α|β|γ|δ|ε|ζ|η|θ|ι|κ|λ|μ|ν|ξ|ο|ρ|σ|τ|υ|φ|χ|ψ|ω)";
        String charString = String.valueOf(c);
        Pattern p = Pattern.compile("([a-z]|" + latin + ")");
        Matcher match = p.matcher(charString);
        return match.find();
    }

    public static boolean isFrontBracket(char c) {
        return c == '(' || c == '（' || c == '[' || c == '【' || c == '{';
    }

    /**
     * 判断是否是负号或者正负号
     */
    public static boolean isPlusAndMinus(char c) {
        return c == '-' || c == '±' || c == '﹣' || c == '+';
    }

    public static boolean isBackbracket(char c) {
        return c == '）' || c == ')' || c == ']' || c == '】' || c == '}';
    }

    /**
     * 表达式中的关系运算符
     */
    public static boolean isRelationOperator(char c) {
        return c == '=' || c == '>' || c == '<' || c == '≥' || c == '≤' || c == '≠' || c == '≈' || c == '≡' ||
                c == '＜';
    }

    /**
     * 判断是否是数学表达式运算符
     */
    public static boolean isExpressionOperator(char c) {
        return c == '+' || c == '*' || c == '×' || c == '/' || c == '÷' || c == '-'
                || c == '^' || c == '﹣' || c == ':' || c == '−';
    }

    public static boolean isPercentSymbol(char c) {
        return c == '%';
    }

    public static boolean isSubscriptSymbol(char c) {
        return c == '_';
    }

    public static boolean isSuperScriptSymbol(char c) {
        return c == '^';
    }

    public static boolean isDegreeSymbol(char c) {
        return c == '°';
    }
}
