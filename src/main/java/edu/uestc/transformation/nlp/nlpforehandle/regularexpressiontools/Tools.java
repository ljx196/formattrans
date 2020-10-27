package edu.uestc.transformation.nlp.nlpforehandle.regularexpressiontools;



import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {
    //latex的标志
    public static final String letextarget = "$";
    public static final String LATEX_BEGIN = "$";
    public static final String LATEX_END = "$";
    //分行的标志
    public static final String linesplit = "@@";
    //分原子的标志
    public static final String elmentsplit = "@@";

    public static final String SENTANCESPLIT_STRING = "@_";
    //提取latex的正则表达式
    public static final String latexregex = "\\$[.[^@\\$]]*?\\$";
    //方程组的标志
    public static final String arraytarget = "\\begin{array}{*{20}{c}}";
    //点的正则表达式
    public static final String pointregex = "[A-Z]('+|_[0-9]|)";
    //不能是连续字母的点的正则表达式
    public static final String pointRegexWithOutConflict = "(?<!" + pointregex + ")" + pointregex + "(?!" + pointregex + ")";
    //角的正则表达式
    public static final String angleregex = "∠(" + pointregex + "|[0-9])+";
    //线段的正则表达式
    public static final String segmentregex = "(?<!" + pointregex + ")(" + pointregex + "){2}(?!" + pointregex + ")";
    //坐标的正则表达式
    public static final String coordinateregex = "(" + pointregex + "|坐标(是|)|)\\(.*?,.*\\)";
    //否命题标志
    public static final String notflag = "@&@";
    //作图语句标识符
    public static final String draw = "";//@D@";
    //否命题包含的词语
    public static final String negativeWords = "(不在|不是|不为|不经过|(?<!个)不相等|不等(?!式)|不相似|不全等|都不|不共线|不相交|不平行|不垂直)";
    public static final String ANGLETARGET = "∠";
    public static final String array_flag = "#";
    //该关键字需要被替换的内容(最远匹配)
//	public static final String SMFvalue = "[.[^@#]]*";
//	//句模中的关键字(最远匹配)
//	public static final String SMFtarget = "@TARGETFAR";
//	//该关键字需要被替换的内容(最近匹配)
//	public static final String SMNvalue = "[.[^@#]]*?";
//	//句模中的关键字(最近匹配)
//	public static final String SMNtarget = "@TARGETNEAR";
    public static final String EXPR_CONTAIN = RegularExpression.EXPR_CONTAIN;

    public static final String CONDITION_CONTAIN = RegularExpression.CONDITION_CONTAIN;
    public static final String EQ_CONTAIN = RegularExpression.EQ_CONTAIN;

    public static final String UNEQ_CONTAIN = RegularExpression.UNEQ_CONTAIN;

    public static final String PRO_EQ = RegularExpression.PRO_EQ;


    public static final String[] symbol = {
            "@TARGETFAR@",                                    //0
            "@TARGETNEAR@",                                    //1
            "@POINTCO@",        //点坐标的点					//2
            "@EQ@",                //等式						//3
            "@EXPR!$@",            //不带$表达式					//4
            "@EXPR@",            //带$表达式					//5
            "@UNEQ@",        //不等式 带$					//6
            "@SINGLEWORD@",    //单个字母   前后都不是字母			//7
            "@WORD@",            //单个字母，前后可能是另外的字母	//8
            "@POINT@",            //点                     					//9
            "@TRIANGLE@",        //三角形，带坐标				//10
            "@ANGLE@",        //角，带坐标					//11
            "@COORD@",        //坐标						//12
            "@IS@",                //是							//13
            "@AT@",                //在							//14
            "@AND@",            //(和|与|,)					//15
            "@PROEQ@",            //一个直线表达式前面可能出现的一些词语	//16
            "@EQ!$@",            //等式(不含$)						//17
            "@UNEQ!$@",            //不等式(不含$)					//18
            "@SYMBOLINEXPR@",    //一个表达式里面能出现的符号		//19
            "@$ORNOT@",            //(\\$|)					//20
            "@NOT@",            //否							//21
            "@SEGMENT@",        //线段						//22
            "@LINE@",            //直线						//23
            "@CROSSPOINT@",        //过点						//24
            "@GEOEQ@",            //几何等式					//25
            "@GEOEXPR@",        //几何表达式					//26
            "@NUMBER@",            //数字（含一些单位）				//27
            "@UNIT@",            //单位						//28
            "@GRAPH@",          //包含指代的方程				//29
            "@LOWCHAR@",        //小写字母					//30
            "@UPPERCHAR@",        //大写字母					//31
            "@PROFUZZ@",        //,(并|)(且|)				//32
            "@SINGLELOWCHAR@",  //单个小写字母					//33
            "@WORDUNIT@",    //文字的单位					//34
            "@ANDALSO@",        //(,|)(且|并|)				//35
            "@CIRCLENUMBER@",    //圈数字						//36
            "@AXIS@",                                        //37
                            /*38*/      "@FOLDSHAPE@",       //折叠语句中出现的形状
            "@EXTRIANGLE@",        //广泛的三角形					//39
            "@EXANGLE@",        //广泛的角					//40
                            /*41*/        "@CIRCLE@",
            "@REALNUMBER@",        //实数						//42
            "@SPECNUM@",    //特殊数						//43
            "@COMPLETEEQ@",                            //44
            "@QUADRANGLE@",                                //45
            "@CHNUM@",    //中文数字                                 //46
            "@GRAPH_NAME@",            //47
            "@LINEGRAPH@",            //48
            "@QUADRANGLE_SHAPE@"  //49
    };


    /**
     * 获得正则表达式的match
     *
     * @param regex 正则
     * @param dataString 源字符串
     * @return
     */
    public static Matcher getRegexMatcher(String regex, String dataString) {
        Pattern p = Pattern.compile(regex);
        return p.matcher(dataString);
    }

    public static Matcher getRegexMatcher(String regex, StringBuilder dataString) {
        return getRegexMatcher(regex, dataString.toString());
    }

    public static Matcher getRegexMatcher(String regex, StringBuffer dataString) {
        return getRegexMatcher(regex, dataString.toString());
    }

    /**
     * 判断是否是代数表达式
     * 首先判断表达式中含有∠、△、⊥、∥、▽等几何元素时，则为几何表达式
     * 如果不含上述的几何表达式时，判断表达式中是否含有两个即两个以上连续大写字母的形式，如果有，则为几何表达式
     * 否则，为代数表达式
     */
    public static boolean isAlgebraExpression(String expressString) {
        String geometrysymbol = "(∠|△|⊥|∥|▽)";
        Matcher match = Pattern.compile(geometrysymbol).matcher(expressString);
        if (match.find()) {
            return false;
        } else {
            Matcher matcher = Pattern.compile("([A-Z]('+|_[0-9]+|)){2}").matcher(expressString);
            return !matcher.find();
        }
    }


}
