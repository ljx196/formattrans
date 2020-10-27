package edu.uestc.transformation.nlp.nlpforehandle.Latex;

/**
 * @author: tjc
 * @create: 2020/9/22
 * @description:
 */

public class LatexConstants {
    public static final String letextarget = "$";
    public static final String latexregex = "\\$[.[^@\\$]]*?\\$";
    public static final String array_flag = "#";
    public static final String BIG_WORD_CONTAIN = "([A-Z](_|)(\\{[0-9]+\\}|[0-9]+|)(\\{'+\\}|'+|))";
    public static final String CHAR_REGEX = "(([A-Z]|[a-z])(_|)(\\{[0-9]+\\}|[0-9]+|)(\\{'+\\}|'+|))";
    public static final String SINGLE_BIG_CHAR_FORM1 = "((?<!([A-Za-z0-9]|'|⊙|∠|△))[A-Z](?!([A-Za-z0-9]|'|_)))";
    public static final String SINGLE_BIG_CHAR_FORM2 = "((?<!([A-Za-z0-9]|'|⊙|∠|△))[A-Z]_(\\{[0-9]+\\}|[0-9]+)(\\{'+\\}|'+|)(?!([A-Za-z0-9])))";
    public static final String SINGLE_BIG_CHAR_FORM3 = "((?<!([A-Za-z0-9]|'|⊙|∠|△))[A-Z](\\{'+\\}|'+)(?!([A-Za-z0-9])))";
    public static final String SINGLE_BIG_CHAR = "(((?<!([A-Za-z0-9]|'|⊙|∠|△))[A-Z](?!([A-Za-z0-9]|'|_)))|((?<!([A-Za-z0-9]|'|⊙|∠|△))[A-Z]_(\\{[0-9]+\\}|[0-9]+)(\\{'+\\}|'+|)(?!([A-Za-z0-9])))|((?<!([A-Za-z0-9]|'|⊙|∠|△))[A-Z](\\{'+\\}|'+)(?!([A-Za-z0-9]))))";
    public static final String SEGMENT_REGEX = "(?<!([A-Za-z0-9]|'|⊙|∠|△))([A-Z](_|)(\\{[0-9]+\\}|[0-9]+|)(\\{'+\\}|'+|)){2}(?!([A-Za-z0-9]|'))";
    public static final String VERTICAL = "(?<!([A-Za-z0-9]|'|⊙|∠|△))([A-Z](_|)(\\{[0-9]+\\}|[0-9]+|)(\\{'+\\}|'+|)){2}(?!([A-Za-z0-9]|'))⊥(?<!([A-Za-z0-9]|'|⊙|∠|△))([A-Z](_|)(\\{[0-9]+\\}|[0-9]+|)(\\{'+\\}|'+|)){2}(?!([A-Za-z0-9]|'))";
    public static final String PARALLEL = "(?<!([A-Za-z0-9]|'|⊙|∠|△))([A-Z](_|)(\\{[0-9]+\\}|[0-9]+|)(\\{'+\\}|'+|)){2}(?!([A-Za-z0-9]|'))∥(?<!([A-Za-z0-9]|'|⊙|∠|△))([A-Z](_|)(\\{[0-9]+\\}|[0-9]+|)(\\{'+\\}|'+|)){2}(?!([A-Za-z0-9]|'))";

    public LatexConstants() {
    }
}
