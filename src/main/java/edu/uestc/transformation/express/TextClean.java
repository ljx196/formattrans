package edu.uestc.transformation.express;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.uestc.transformation.nlp.nlpforehandle.Latex.LatexPattern;
import edu.uestc.transformation.nlp.utils.LatexConvert;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
/**
 * @author: tjc
 * @create: 2020/10/21
 * @description:
 */

public class TextClean {
    public static Logger LOG = LogManager.getLogger(TextClean.class);
    private static LatexConvert latexConvert = new LatexConvert();

    public static String JsoupDataProcess(String text) {

        // 清洗&nbsp;
        String regEx_special = "\\&[a-zA-Z]{1,10};";
        Pattern p_special = Pattern.compile(regEx_special, Pattern.CASE_INSENSITIVE);

        Matcher m_special = p_special.matcher(text);
        String res = m_special.replaceAll("");
        res.replaceAll("\r", "");
        res.replaceAll("<br>", "");
        res.replaceAll("<div>", "");
        res.replaceAll("</div>", "");
        //type参数无意义，但是为了不修改其他地方代码，因此保留该参数
        res = latexConvert.getMapleString(res, "1");
        return res;
    }

    public static void main(String[] args) {
       // String text = "解:∵$x+x^-1=3$，∴$n=1$时，$x^2+x^-2=(x+x^-1)^2-2=3^2-2=7$，$n=2$时，$x^4+x^-4=(x^2+x^-2)^2-2=7^2-2=47$，$n=3$时，$x^8+x^-8=(x^4+x^-4)^2-2=47^2-2=2207$，，，，归纳$(x^(2^n)+x^(-2^n)(n$∈$N*))$的个位数字$7$，故选$C$由已知中$x+x^-1=3$，结合完全平方公式，求出$n=1$，$2西王母3玃如，，$时，分析个位数的变化规律，可得答案，归纳推理的一般步骤是，(1)通过观察个别情况发现某些相同性质；(2)从已知的相同性质中推出一个明确表达的一般性命题(猜想)";
       String text="，根据对数的运算性质进行解答；(2)根据$α$的取值范围和同角三角函数关系解答；(3)原式分子分母除以$cosα$，利用同角三角函数间的基本关系化简，将$tanα$的值代入计算即可求出值，此题考查了同角三角函数基本关系的运用，对数的运算性质，考查计算能力，属于基础题";
        String res = latexConvert.getMapleString(text,"1");
        System.out.println(res);
    }
}
