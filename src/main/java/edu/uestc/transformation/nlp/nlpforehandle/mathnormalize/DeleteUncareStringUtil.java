package edu.uestc.transformation.nlp.nlpforehandle.mathnormalize;


import edu.uestc.transformation.nlp.nlpforehandle.regularexpressiontools.LTools;

/**
 * 删除不重要的字符
 * 重构
 * Modified by ZZH on 2015-11-20
 */
public class DeleteUncareStringUtil {
    private static String[] uncareStringRegexs = {
            "\\\\left\\.",
            "\\\\left",
            "\\\\right(\\.|,)",         //删除录入可能混在$表达式$中的"."
            "\\\\right",
            "\\\\rm",
            "\\\\text",
            "\\\\quad",                    //空格
            "\\\\mathcal",              //花体
            "\\\\bf",                       //加粗
            "\\\\operatorname", //下标latex
            "\\\\mathbf",            //粗体
            "\\([0-9]{4}([\\u4e00-\\u9fa5]{1,4})\\)",
            "(?<=.)\\.$",
            "\\\\underset",
            "\\\\(\\\\|)mathrm",        //罗马
            "\\\\cdots(?=(①|②|③|④|⑤|⑥|\\\\cdots))",
            ",(&|)\\\\q(q|)uad\\\\cdots\\\\cdots",
            "\\\\begin\\{split\\}",
            "\\\\end\\{split\\}",

    };
    private static String[] uncareStringOfSolution = {
//            "^\\$([0-9])\\$",                                       //$4$
            "^\\([0-9]\\)(?!((,|，|)([0-9]+)))",
            "^\\$\\([0-9]\\)\\$",                                  //$(4)$
            "(?<=(#%#))\\([0-9]\\)(?!((,|，|)([0-9]+)))",
            "^(\\n|)\\([0-9]\\)(?!((,|，|)\\([0-9]+\\)))",
            "^(\\n|)\\((Ⅰ|Ⅲ|Ⅳ|Ⅱ)\\)",                        //(Ⅰ)
            "该(?=(抛物线|双曲线|椭圆))",
            "中心在原点的(?=(抛物线|双曲线|椭圆))",
//            "\\(i(为|是)虚数单位\\)",
            "\\((其中|)e(为|是)自然对数的底数\\)",
            "(,|)(其中|)e(为|是)自然对数的底数",
//            "(,|，)i(是|为)虚数单位",
//            "(设|)i(是|为)虚数单位(,|，)",
            "(并|请)说明理由(\\.|,|)",
            "并证明你的结论(\\.|)",
            "下列直线中",
            "那么",
            "推知",
            "巳知",
            "己知",
            "恰好",
            "你能",
            "大致",
            "如果错误[,，]",
            "请你(?=(说明|证明|确定))",
            "(?<!确)定(?=(直线|圆))",
            "动(?=(直线|圆|弦))",
            "恰(好|)(?=(为|有|是))",
            "目标函数",
            "其(?=垂足)",
            "另(?=一个焦点)",
            "纸片",
            "(?<=right)(\\.|,)(?=\\$)",
            "\\(本(小|)题(满分|共)[0-9]+分(,(\\s|)([0-9])(小|)问[0-9]分)+\\)",
            "\\(本(小|)题(满分|共|)[0-9]+(分|)\\)",
            "\\([0-9]+分\\)",
//            "\\([\\u4e00-\\u9fa5]+\\)",
            "(?<![A-Z],)其中(?!(点|(位|)线))",
            "(下列|下面|以下)(四个|所列|)(说法|结论|方程|不等式|表达式|关系式|等式|(关于函数(f|g)\\(x\\)的|)叙述|命题)(中|)(一定|恒|可能|)(成立|正确)的是",
            "(下(列|面)|)(判断|)正确的结论是",
            "(下(列|面)|)(判断|命题)(中|)(为|是)(正确|真命题)(的|)(为|是)",
            "下(列|面)(关系|选项)(中|)正确的(是|为)",
            "下(列|面)([,\"“”\\u4e00-\\u9fa5]+)(?<!不)正确的(是|为)",
            "根据([\\u4e00-\\u9fa5]+)(?=证明)",
            "若同时满足条件:",
//            "(根据|由)题意(得|可知)(出|)(:|)",    //后端需要这个话,所以不能删去
            "(?<=求)下列各式的值",
            "(,|，)i(为|是)虚数单位",                                                   //为了测题写的
            "(^如图[0-9](,|，|))|(\\(^如图\\([0-9]\\)\\))|(?<!图象)((^如图(,|，))|(^如图所示)|(\\(|)如图(\\)|)|如右图|右图为)",
            "(平移后的图象|)如图所示",
            "\\(n=1,2,3...\\)",
            "\\(n=1,2,3…\\)",
            "<img>",
            "\r",
            "\n",
           // "(?<!&)&(?!&)",
            "(?<=#%#),",
            "(?<=顶点)(坐标)(?=(为|是))",
            "(?<=抛物线的)(函数)(?=(表达式))",
            "(的|其|)图象(?!([A-Za-z0-9]|法|\\$))",
            "(的|其|)简图",
            "表示的直线",
            "并加以证明",
            "(?<=(直线|)([a-z](_\\d|)|[A-Z]{2}|)的)(函数)(?=表达式)",
            "(?<=直线的)(函数)(?=表达式)",
            "(?<=垂直平分线)(段)",
            "(不|)(使|利|)用计算器",
            "(并|)(要求|)(把|将)(它们|其|它|不等式(组|)|)(的|)解(集|)(分别|)(表示在数轴上|用数轴表示出来|在数轴上表示(出来|))(:|)",
            "(并|)(在数轴上)(把|将)(它们|解)(集|)(分别|)(表示出来)(:|)",
            "(并|)(在数轴上|用数轴)(表示)(出|)(它们|其|它|不等式(组|)|)(的|)解(集|)(:|)",
            "在数轴上的(对应点的|)位置(如图|)(所示|)",
            //处理yqzy数据问题
            "<imgsrc=.*?>",
            "<p>",
            "</p>",
            "<sup>",
            "</sup>",
            "\\(组\\)",
            "\\((结果|)精确到(0.1|0.01|0.001|个位|十分位|百分位|千分位)\\)",
            ",(?=(①|②|③|④|⑤|⑥))",
            ",(\\\\|)\\&(?=(①|②|③|④|⑤|⑥))",
            ",(?=(\\\\end|\\}\\\\))",
            "(?<=_)cm",
            "\uE009",
    };
    private static String uncareRegex = init();

    /**
     * 把数组表示的停用词转换成一个正则表达式
     */
    private static String init() {
        return LTools.createRegexOrByStringList(uncareStringRegexs);
    }

    public static String deleteUncare(String sentence) {
        sentence = sentence.replaceAll(uncareRegex, "");
        String uncareStrOfSolution = LTools.createRegexOrByStringList(uncareStringOfSolution);
        return sentence.replaceAll(uncareStrOfSolution, "");
    }

    public static StringBuffer deleteUncare(StringBuffer sentence) {
        LTools.stringBufferReplaceAll(sentence, uncareRegex, "");
        String uncareStrOfSolution = LTools.createRegexOrByStringList(uncareStringOfSolution);
        LTools.stringBufferReplaceAll(sentence, uncareStrOfSolution, "");
        return sentence;
    }

}
