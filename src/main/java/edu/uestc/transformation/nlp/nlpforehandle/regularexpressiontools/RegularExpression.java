package edu.uestc.transformation.nlp.nlpforehandle.regularexpressiontools;

/**
 * Modified by ZZH on 2016/2/25.
 * 全部正则表达式
 */

public class RegularExpression {

    public static final String SPLITQUESTIONWITHELEMENTTARGET = "^(解|得|答|)(\\$|:|：)*(\\(|（)[0-9](\\)|）)";

    //提取latex的正则表达式
    public static final String latexregex = "\\$[.[^@\\$]]*?\\$";
    //点的正则表达式
    public static final String pointregex = "[A-Z]('+|_[0-9]|)";
    //角的正则表达式
    public static final String angleregex = "∠(" + pointregex + "|[0-9])+";

    /**
     * 句摸使用的正则表达式
     */
    public static final String eqRefer = "((\\{|)(①|②|③|④|⑤|⑥|⑦|⑧|⑨|⑩|⑪|⑫|⑬|⑭|⑮|⑯|⑰|⑱|⑲|⑳)(\\}|))";
    private static final String latin = "((α|β|γ|δ|ε|ζ|η|θ|ι|κ|λ|μ|ν|ξ|ο|ρ|σ|τ|υ|φ|χ|ψ|ω|Δ|▽)|" + eqRefer + ")";
    public static final String singleLatin = "(α|β|γ|δ|ε|ζ|η|θ|ι|κ|λ|μ|ν|ξ|ο|ρ|σ|τ|υ|φ|χ|ψ|ω|Δ|▽)";

    private static final String opchar = "(\\}|\\{|\\w|\\+|-|/|\\(|\\)|\\[|\\]|\\\\|\\*|\\||\\^|%|!|\\.|_|'|:|±|°|#)";
    public static final String eqchar = "=";
    public static final String ineqChar = "(≥|>|＜|≤|<|≠)";
    private static final String geoSpecChar = "(∠|△)";
    /*
    * 用于提取相关字符串的正则表达式
    */
    public static final String OPERATOR = "(=|≥|>|＞|＜|≤|<|≠)";
    //表达式里面能出现的字符集
    public static final String SYMBOL_IN_EXPR = "(" + opchar + "|" + latin + ")";
    public static final String SYMBOL_IN_EQ = "(" + opchar + "|" + eqchar + "|" + latin + ")";

    public static final String EXPR_CONTAIN = SYMBOL_IN_EXPR + "+";


    public static final String INEQUALITY1 = "(" + EXPR_CONTAIN + ineqChar + EXPR_CONTAIN + ")";					/*a>0*/
    public static final String INEQUALITY3 = "((" + EXPR_CONTAIN + ineqChar + ")+?" + EXPR_CONTAIN + ")";			/*a>b>0*/
    public static final String INEQUALITY2 = "((" + EXPR_CONTAIN + ",)+" + EXPR_CONTAIN + ineqChar + EXPR_CONTAIN + ")";	/*a,b>0*/
    public static final String SINGLE_INEQUALITY = "(" + INEQUALITY1 + "|" + INEQUALITY2 + "|" + INEQUALITY3 + ")";

    public static final String CONDITION_CONTAIN = "(\\((" + SINGLE_INEQUALITY + ",)*" + SINGLE_INEQUALITY + "\\))";

    public static final String EQ_CONTAIN = "(" + SYMBOL_IN_EQ + "+" + "(" + CONDITION_CONTAIN + "|))";
    //简单的表达式（不含条件）
    public static final String Simple_Eq_Regex = "(\\$" + RegularExpression.SYMBOL_IN_EQ + "+\\$|" + RegularExpression.SYMBOL_IN_EQ + "+)";

    // 不等式

    //方程、不等式的变量(可能为多个)
    public static final String VARIABLE_REGEX = "关于([a-z]('+|_[0-9]+|),)*([a-z]('+|_[0-9]+|))";

    public static final String INEQUATION_PREFIXION = "(" + VARIABLE_REGEX + "|)(的|)(.元|)(.次|)不等式(组|)(:|)";

    public static final String UNEQ_CONTAIN = "(" + opchar + "|" + eqchar + "|" + ineqChar + "|" + latin + ")+";
    public static final String UNEQ_REGEX = "(\\$" + UNEQ_CONTAIN + "\\$)";
    public static final String UNEQ_REGEX_NO_DOLLAR = "(\\$" + UNEQ_CONTAIN + "\\$|" + UNEQ_CONTAIN + ")";

    public static final String ABS_INEQUATION = "(" + INEQUATION_PREFIXION + UNEQ_REGEX_NO_DOLLAR + "|" + INEQUATION_PREFIXION + "|" + UNEQ_REGEX + ")";

    public static final String LATEX_CONTAIN = "(" + opchar + "|" + eqchar + "|" + ineqChar + "|" + latin + ")+";
    // 带$或者不带$的等式
    public static final String EQ_REGEX_NO_DOLLAR = "(\\$" + EQ_CONTAIN + "\\$|" + EQ_CONTAIN + ")";
    // 带$的等式
    public static final String EQ_REGEX = "(\\$" + EQ_CONTAIN + "\\$)";

    public static final String GEO_EQ_CONTAIN = "(" + geoSpecChar + "|" + opchar + "|" + ineqChar + "|" + eqchar + "|" + latin + ")+";
    // 带$或者不带$的几何等式

    public static final String GEO_EXPR_CONTAIN = "(" + geoSpecChar + "|" + opchar + "|" + latin + ")+";

    public static final String GEO_EQ_REGEX_NO_DOLLAR = "(\\$" + GEO_EQ_CONTAIN + "\\$|" + GEO_EQ_CONTAIN + ")";

    // 带$或不带$的几何表达式
    public static final String GEO_EXPR_EGEX_NO_DOLLAR = "(\\$" + GEO_EXPR_CONTAIN + "\\$|" + GEO_EXPR_CONTAIN + ")";

    public static final String BIG_WORD_CONTAIN = "([A-Z](?!i)(_|)(\\{[0-9n]+\\}|[0-9n]+|)(\\{'+\\}|'+|))";

    private static final String LOWER_CHAR_HELP = "\\}|\\{|\\w|\\+|-|/|\\(|\\)|\\[|\\]|\\\\|\\*|=|\\||\\^|%|!|\\.|'|:";

    public static final String LOW_WORD_CONTAIN = "([a-z](_|)(\\{[0-9a-z]+\\}|[0-9a-z]+|)(\\{'+\\}|'+|))";

    //坐标轴
    public static final String AXIS = "x轴|X轴|Y轴|y轴|x轴(的|)(正半轴|正向)|x轴(的|)(负半轴|负向)|y轴(的|)(正半轴|正向)|y轴(的|)(负半轴|负向)";

    // 一个字母（包括下标和上标，把小写字母表示的角排除了）
    public static final String CHAR_REGEX = "(([A-Z])(_|)(\\{[0-9a-z]+\\}|[0-9a-z]+|)(\\{'+\\}|'+|))";
    //直线的表示
    public static final String LINE_REGEX = "(?<!([A-Za-z0-9]|'|⊙))(" + BIG_WORD_CONTAIN + "{2}|" + LOW_WORD_CONTAIN + ")(轴|)(?!([A-Za-z0-9]|'))";
    //仅有一个小写字母
    public static final String SINGLE_LOWER_CHAR = "(?<!([A-Za-z0-9]|'|⊙|" + LOWER_CHAR_HELP + "))" + LOW_WORD_CONTAIN + "(?!([A-Za-z0-9]|'|" + LOWER_CHAR_HELP + "))";
    // 点坐标的形式
    public static final String POINT_COORD_REGEX = "\\$(([A-Z]|[a-z]|)(\\^|_|)(\\{[0-9]+\\}|[0-9]+|)(\\{'+\\}|'+|))\\([.[^\\$,]]+?,[.[^\\$,]]+?\\)\\$";
    public static final String POINT_COORD_REGEX_NO$ = "(([A-Z]|[a-z]|)(\\^|_|)(\\{[0-9]+\\}|[0-9]+|)(\\{'+\\}|'+|))\\([.[^\\$,]]+?,[.[^\\$,]]+?\\)";
    // 坐标形式
    public static final String COORD = "((\\$\\([.[^\\$,]]+?,[.[^\\$,]]+?\\)\\$)|(\\([.[^\\$,]]+?,[.[^\\$,]]+?\\)))";
    // 仅一个大学字母（前后不能是字母，前面不能是⊙或者'
    public static final String SINGLE_BIG_CHAR_FORM1 = "((?<!([A-Za-z0-9]|'|⊙|∠|△))" + "[A-Z]" + "(?!([A-Za-z0-9]|'|_)))";
    public static final String SINGLE_BIG_CHAR_FORM2 = "((?<!([A-Za-z0-9]|'|⊙|∠|△))" + "[A-Z]_(\\{[0-9]+\\}|[0-9]+)(\\{'+\\}|'+|)" + "(?!([A-Za-z0-9])))";
    public static final String SINGLE_BIG_CHAR_FORM3 = "((?<!([A-Za-z0-9]|'|⊙|∠|△))" + "[A-Z](\\{'+\\}|'+)" + "(?!([A-Za-z0-9])))";
    public static final String SINGLE_BIG_CHAR = "(" + SINGLE_BIG_CHAR_FORM1 + "|" + SINGLE_BIG_CHAR_FORM2 + "|" + SINGLE_BIG_CHAR_FORM3 + ")";
    //抽象的点 带或者不带坐标
    public static final String ABS_POINT_REGEX = "(" + POINT_COORD_REGEX + "|" + SINGLE_BIG_CHAR + "|原点(" + SINGLE_BIG_CHAR + "|))";
    public static final String ABS_POINT_REGEX_1 = "(" + POINT_COORD_REGEX_NO$ + "|" + SINGLE_BIG_CHAR + ")";
    public static final String REFERENCE_POINT_REGEX = "(" + SINGLE_BIG_CHAR + "|原点(" + SINGLE_BIG_CHAR + "|))";


    // 带$或不带$的表达式
    public static final String EXPR_EGEX_NO_DOLLAR = "(\\$" + EXPR_CONTAIN + "\\$|" + EXPR_CONTAIN + ")";

    //仅一个字母
    public static final String SINGLE_CHAR = "(?<!([A-Za-z0-9]|'|⊙∠|△))" + CHAR_REGEX + "(?!([A-Za-z0-9]|'))";

    // 不带$的三角形
    public static final String TRIANGLE_REGEX_NODOLLA = "△" + BIG_WORD_CONTAIN + "{3}";
    public static final String ANGLE_REGEX_NODOLLA = "∠" + BIG_WORD_CONTAIN + "{1,3}";
    // 带$或者不带$三角形


    //广泛的三角形定义
    public static final String TRIANGLE_REGEX_EXTENSIVE = "(等腰直角|等腰Rt|等腰|等边|锐角|直角|钝角|Rt|)△(" + BIG_WORD_CONTAIN + ")+";

    //折叠语句中出现的形状
    public static final String QUADRANGLE_SHAPE = "(平行|∥)四边形|四边形|□|正方形|菱形|矩形|(等腰Rt|等腰Rt|等腰|)梯形";
    public static final String FLOD_SHAPE = "(等边|((等腰|)(Rt|))|)(平行四边形|矩形|梯形|菱形|四边形|□|△)(" + BIG_WORD_CONTAIN + ")+";

    // 广泛的角定义
    public static final String ANGLE_REGEX_EXTENSIVE = "(锐角|直角|钝角|Rt|平角|)" + "∠(" + BIG_WORD_CONTAIN + ")+";

    
    
/* *函数图像方程有关（将函数和方程的识别分开处理） */

    //抽象的图形
//    public static final String ABS_GRAPH_REGEX= ReferenceWord.GRAPH_REFER_REGEX;


    //函数的变量(因变量和自变量：  y关于x的..)
    private static final String FUNCTION_VARIABLE_REGEX = "[a-z]('+|_[0-9]+|)关于[a-z]('+|_[0-9]+|)";
    //函数的直线
    private static final String FUNCTION_LINE = "直线((([A-Z]('+|_[0-9]+|)){2}|[a-z]('+|_[0-9]+|))|)";
    //函数的前缀
    public static final String FUNCTION_PREFIXION1 = "(一次|二次|(正|反)比例|抛物线|双曲线|" + FUNCTION_LINE + ")(的|)函数(的|)(解析式|图(像|象)|)";
    public static final String FUNCTION_PREFIXION2 = "(抛物线|双曲线|" + FUNCTION_LINE + ")(的|)(方程|)(的|)(解析式|图(像|象)|)";
    public static final String FUNCTION_PREFIXION3 = "函数(的|)(解析式|图(像|象)|)";

    public static final String FUNCTION_PREFIXION = "(" + FUNCTION_PREFIXION1 + "|" + FUNCTION_PREFIXION2 + "|" + FUNCTION_PREFIXION3 + ")";

    //函数的后缀
    public static final String FUNCTION_POSTFIX = "(的|)((函数|)图(像|象))";
    //抽象函数：	
    //	1、前缀+EQ+后缀  
    //	2、前缀+EQ 
    //  3、EQ+后缀 
    //  4、前缀(需指代)		确保ABS_FUNCTION不会匹配""  
    public static final String ABS_FUNCTION = "(" + FUNCTION_VARIABLE_REGEX + "|)" +
            "(" +
            "(" + FUNCTION_PREFIXION + "|)" + EQ_REGEX + "(" + FUNCTION_POSTFIX + "|)" +  //1、2、3

            "|" +
            FUNCTION_PREFIXION +

            ")";                                                                    //4

    public static final String LINEGRAPH_PREFIX = "(((((一元|)一次函数)|直线)|)" + LINE_REGEX + "|(((一元|)一次函数)|直线|))";

    public static final String LINEGRAPH1 = LINEGRAPH_PREFIX + "(:|)" + EQ_REGEX + "(" + FUNCTION_POSTFIX + "|)";    //一次函数l:$y=kx+b$的图像
    public static final String LINEGRAPH2 = "((一元|)一次函数)" + "(" + LINE_REGEX + "|)" + "(" + FUNCTION_POSTFIX + "|)";        //一次函数的图像l

    /*抽象直线（一次函数）*/
    public static final String ABS_LINEGRAPH = "(" + LINEGRAPH1 + "|" + LINEGRAPH2 + ")";


    //方程的变量(可能为多个)
    //public static final String VARIABLE_REGEX = "关于([a-z]('+|_[0-9]+|),)*([a-z]('+|_[0-9]+|))";
    //方程的前缀
    public static final String EQUATION_PREFIXION = "(" + VARIABLE_REGEX + "|)(的|)(.元|)(.次|)方程(组|)(:|)";
    //抽象方程：	带前缀的方程     或	方程   或  方程前缀 (需指代)		确保ABS_EQUATION不会匹配""
    public static final String ABS_EQUATION = "(" + EQUATION_PREFIXION + EQ_REGEX + "|" + EQUATION_PREFIXION + "|" + EQ_REGEX + ")";


    //***********旧版本的图像和方程的识别表达式，暂时保留******//
    public static final String GRAPH_NAME = "(一次|二次|反比例|直线(([A-Z]('+|_[0-9]+|)){2}|[a-z]('+|_[0-9]+|)|)|抛物线|双曲线|对称轴)";
    private static final String FUNCTION_GTAPH = "(函数|方程|方程组|)((函数|)图象|)";
    public static final String GRAPH_INFO = "(关于(" + CHAR_REGEX + "(,|))+(的|)|)(.元.次|)" + "(" + GRAPH_NAME + "|)" + FUNCTION_GTAPH;
    private static final String GRAPH_WITH_EQUATION = "(" + GRAPH_INFO + ")" + EQ_REGEX;
    private static final String GRAPH_WITHOUT_EQUATION = GRAPH_NAME + FUNCTION_GTAPH;

    //识别l:y=1之类的情况
    public static final String LINE_SPLIT_REGEX = "^[a-z](_[a-z0-9]|):[.[^:]]+=[.[^:]]+$";
    public static final String GRAPH_METHOD_REGEX = "((" + GRAPH_WITH_EQUATION + ")|(" + GRAPH_WITHOUT_EQUATION + ")|(" + LINE_REGEX + ")|图像)";

    public static final String PRO_EQ = "(关于" + SINGLE_LOWER_CHAR + "(的|)|)(一次|二次|反比例|直线([A-Z]{2}|[a-z]|)|抛物线|双曲线|对称轴|)(的|)(函数|方程|)(的|)(图象|)";
    static final String PRO_EQ_MUST_HAVE_NAME = "(一次|二次|反比例|直线([A-Z]{2}|[a-z]|)|抛物线|双曲线|对称轴)(的|)(函数|方程|)(的|)(图象|)";
    //    public static final String GRAPH_METHOD_REGEX="("+PRO_EQ+EQ_REGEX+"|"+PRO_EQ_MUST_HAVE_NAME+"|"+RegularExpression.LINE_REGEX+"|"+"图象"+")";
    //图形方程，  包括有或者没有方程
    public static final String GRAPH_METHOD_STRICT_REGEX = "(" + PRO_EQ + EQ_REGEX + "|" + PRO_EQ_MUST_HAVE_NAME + ")";

    public static final String Special_Number = Simple_Eq_Regex + "(是|为)(正|负|)(整|实|常|有理|)数";

    public static final String Chinese_Number = "(一|二|三|四|五|六|七|八|九|十)";
    public static final String SUBSCRIPT_REGEX = "[a-z]_[0-9]+";


    /**
     * Created by ZZH
     * 无Nlp匹配所需(勿动)
     */
    public static final String ABS_CIRCLE = "(?<=(圆|⊙))(" + RegularExpression.BIG_WORD_CONTAIN + ")(?![A-Z])";
    public static final String SECTOR = "(?<=(扇形))(" + RegularExpression.BIG_WORD_CONTAIN + "){3}(?![A-Z])";

    public static final String POLYHEDRON = "(?<=((正|长)方体)|((直|正|三|四|五|六|)棱(柱|锥|台)|(四面体)))(" + RegularExpression.BIG_WORD_CONTAIN + "|-){4,8}(?![A-Z])";
    public static final String ROTATOR = "(?<=((球|圆柱|圆锥|圆台)(体|)))(" + RegularExpression.BIG_WORD_CONTAIN + "){1,3}((-" + RegularExpression.BIG_WORD_CONTAIN + "){1,3}|)(?![A-Z])";
    public static final String SEGMENT_REGEX = "(?<=(直线|射线|线段|线|边|棱))" + BIG_WORD_CONTAIN + "{2}(?!([A-Za-z0-9]|'))";

    public static final String TRIANGLE_REGEX = "(?<=(等腰直角|等腰Rt|等腰|等边|锐角|直角|钝角|Rt|)(△|三角形))(" + BIG_WORD_CONTAIN + "){3}(?![A-Z])";
    public static final String QUADRANGLE_REGEX = "(?<=((平行|∥)四边形|四边形|□|正方形|菱形|矩形|(等腰Rt|等腰Rt|等腰|)梯形))(" + BIG_WORD_CONTAIN + "){4}(?![A-Z])";
    //    public static final String POINT_REGEX = "(([A-Z](_[0-9]|))|(?<=(坐标(为|是|))))\\(.+?,.+?\\)";
    public static final String POINT_REGEX = "(?<=[\\u4E00-\\u9FFF,])\\([^\\u4E00-\\u9FFF,=&]+,[^\\u4E00-\\u9FFF,=&]+\\)((?=[\\u4E00-\\u9FFF,])|$)";
    //    public static final String SET = "("+BIG_WORD_CONTAIN + ")(|(∩|∪)("+BIG_WORD_CONTAIN+"))=" + "\\{[^\\{\\}]+\\}";
//    public static final String SET = "((\\(|\\[)[^\\u4E00-\\u9FFF,=]+,[^\\u4E00-\\u9FFF,=]+(\\)|\\])|(?<=([\\u4E00-\\u9FFF,]))[A-Z](?=([\\u4E00-\\u9FFF,]))";
//    public static final String SET = "(?<=[\\u4E00-\\u9FFF,∈])((\\(|\\[)[^\\u4E00-\\u9FFF,#=∩∪]+,[^\\u4E00-\\u9FFF,#=∩∪]+(\\]|\\))((∩|∪)(\\(|\\[)[^\\u4E00-\\u9FFF,#=∩∪]+,[^\\u4E00-\\u9FFF,#=∩∪]+(\\]|\\)))*|[A-Z])((?=[\\u4E00-\\u9FFF,])|$)";
    public static final String SET = "(?<=[\\u4E00-\\u9FFF,∈])((\\(|\\[)[^\\u4E00-\\u9FFF,#=∩∪&]+,[^\\u4E00-\\u9FFF,#=∩∪&]+(\\]|\\))((∩|∪)(\\(|\\[)[^\\u4E00-\\u9FFF,#=∩∪&]+,[^\\u4E00-\\u9FFF,#=∩∪&]+(\\]|\\)))*|[A-Z](\\*|))((?=[\\u4E00-\\u9FFF,])|$)";
    public static final String VECTOR = "(\\$(↑[^\\$]+?|[^\\$]+?↑[^\\$]+?)\\$|(?<=((最|极)(大|小)值))[A-Z](?=([\\u4E00-\\u9FFF,])))";
    public static final String ARC = "(?<=(优⌒|劣⌒|优弧|劣弧|弧|(?<!\\=)⌒))(" + RegularExpression.BIG_WORD_CONTAIN + "){2,3}(?![A-Z\\=])";
    public static final String CIRCLE = "(?<=((?<!椭)圆))"+BIG_WORD_CONTAIN;
    public static final String ANGLE_REGEX = "((?<=(锐角|直角|钝角|Rt|平角|(?<!(\\>|\\<|≥|≤|\\=|cos|tan|sin|cot|\\+|\\*|\\(|\\)|\\-|[0-9]|[A-Z]))∠|角))" + "((" + BIG_WORD_CONTAIN + "){3}|" + BIG_WORD_CONTAIN + ")(?![0-9A-Z\\*\\-\\=_\\+\\(\\)\\>\\<≥≤])|(?<=(?<!(\\>|\\<|≥|≤|\\=|cos|tan|sin|cot|\\+|\\*|\\(|\\)|\\-|[0-9]|[A-Z]))∠)"+BIG_WORD_CONTAIN+"(?=(\\=_+)))";
    public static final String LINE = "(?<=(垂直|⊥))[a-z](_\\d|)(?![A-Za-z0-9])";
}
