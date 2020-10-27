package edu.uestc.transformation.nlp.nlpforehandle.Latex;
import	java.util.regex.Pattern;

import java.lang.Character.UnicodeBlock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author: tjc
 * @create: 2020/9/22
 * @description:
 */

public class GrammarCheckFunc {
    private static Pattern NUMS = Pattern.compile("([0-9]|')");
    private static Pattern CHARS = Pattern.compile("[A-Z]");
    public GrammarCheckFunc() {
    }

    public static boolean isNumber(char c) {
        if (c == 960) {
            return true;
        } else {
            String charString = String.valueOf(c);
            //Pattern  p = Pattern.compile("([0-9]|')");
            Matcher match = NUMS.matcher(charString);
            return match.find();
        }
    }

    public static boolean isUpperCase(char c) {
        String charString = String.valueOf(c);
        //Pattern p = Pattern.compile("[A-Z]");
        Matcher match = CHARS.matcher(charString);
        return match.find();
    }

    public static boolean isChinese(char c) {
        if (c != '$' && c != '，' && c != 12290 && c != ',' && c != '.') {
            UnicodeBlock ub = UnicodeBlock.of(c);
            return ub == UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == UnicodeBlock.GENERAL_PUNCTUATION || ub == UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
        } else {
            return true;
        }
    }

    public static boolean isLowerCase(char c) {
        String latin = "(α|β|γ|δ|ε|ζ|η|θ|ι|κ|λ|μ|ν|ξ|ο|ρ|σ|τ|υ|φ|χ|ψ|ω)";
        String charString = String.valueOf(c);
        Pattern p = Pattern.compile("([a-z]|" + latin + ")");
        Matcher match = p.matcher(charString);
        return match.find();
    }

    public static boolean isFrontBracket(char c) {
        return c == '(' || c == '（' || c == '[' || c == 12304 || c == '{';
    }

    public static boolean isdot(char c) {
        return c == '.';
    }

    public static boolean isAbsoluteSymbol(char c) {
        return c == '|';
    }

    public static boolean isPlusAndMinus(char c) {
        return c == '-' || c == 177 || c == '﹣' || c == '+';
    }

    public static boolean isBackbracket(char c) {
        return c == '）' || c == ')' || c == ']' || c == 12305 || c == '}';
    }

    public static boolean isRelationOperator(char c) {
        return c == '=' || c == '>' || c == '<' || c == 8805 || c == 8804 || c == 8800 || c == 8776 || c == 8801 || c == '＜';
    }

    public static boolean isExpressionOperator(char c) {
        return c == '+' || c == '*' || c == 215 || c == '/' || c == 247 || c == '-' || c == '^' || c == '﹣' || c == ':' || c == 8722;
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
        return c == 176;
    }
}
