package edu.uestc.transformation.nlp.nlpforehandle.mathnormalize.split;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import edu.uestc.transformation.nlp.nlpforehandle.regularexpressiontools.LTools;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ZZH on 2016/10/13.
 * 变量提取
 */
public class TemplateEntityExtract {

    public static List<String> savedList = new ArrayList<>();
    public static List<VarsSaveData> savedDataList = new ArrayList<>();

    public static String[] pointRegex = {
            "(圆锥曲线|椭圆|双曲线|抛物线) [A-Z](_[0-9]+|)&&line ((:) [^&\\s]+&&expr |)(的|)((左|右|上|下|一个(上|下|左|右|))焦点|(一个|)(上|下|左|右|)顶点)",
            "(圆锥曲线|椭圆|双曲线|抛物线) [A-Z](_[0-9]+|)&&line ((:) [^&\\s]+&&expr |)(与|和)((?<!椭)圆) [A-Z](_[0-9]+|)&&circle ((:) [^&\\s]+&&expr |)(的|)(一个交点)",
            "(圆锥曲线|椭圆|双曲线|抛物线) [A-Z](_[0-9]+|)&&line ((:) [^&\\s]+&&expr |)(的|)(焦点|中心(?!(在|为|是)(直角|)(坐标|)原点))",
            "(?<!((上|下|左|右)方)|象限内|此)(若|)(圆锥曲线|椭圆|双曲线|抛物线) [A-Z](_[0-9]+|)&&line ((:) [^&\\s]+&&expr |)(上(的|)(任意|)(一|动|)点)(?! [^&\\s]+&&point 处的切线)",
            "((?<!双)曲线) [A-Z](_[0-9]+|)&&line ((:) [^&\\s]+&&expr |)(上(的|)(一|动|)点)(?! [^&\\s]+&&point 处的切线)",
            "((?<!双)曲线) [A-Z](_[0-9]+|)&&line ((:) [^&\\s]+&&expr(\\s+)|)(和|与)((?<!双)曲线) [A-Z](_[0-9]+|)&&line ((:) [^&\\s]+&&expr(\\s+)|)(的|)交点",
            "(两条|两|)(直线|抛物线|) [^&\\s]+&&line ((:) [^&\\s]+&&expr(\\s+)|)(和|与)(直线|) [^&\\s]+&&line ((:) [^&\\s]+&&expr(\\s+)|)(的|)交点",

            "(线段|侧棱|(?<!侧)棱|直线|对角线|斜边|(?<!斜)边) [^&\\s]+&&line (的|)(中点)",
            "((直角|等腰|Rt|)(三角形|△) [^&\\s]+&&triangle |)(直角边|斜边) [^&\\s]+&&line (的|)(中点)",
            "(?<!(线段|侧棱|弦|(?<!侧)棱|对角线|边|腰)) [^&\\s]+&&line (的|)(中点)",
            "(函数|) [^&\\s]+&&expr (的|)(图象|)(和|与)(函数|) [^&\\s]+&&expr (的|)(图象|)(的|有一个|)(交点)(?!(的|)个数)",

            "(直线) [a-z](_\\d+|)&&line ((:) [^&\\s]+&&expr |)(的)(中点)",

            "((椭|半|单位|)圆) [A-Z](_[0-9]+|)&&circle ((:) [^&\\s]+&&expr |)(上(的|)(任意|各|)(一|动|)点)(?! [^&\\s]+&&point 处的切线)",
            "((?<!椭)圆) [A-Z](_[0-9]+|)&&circle ((:) [^&\\s]+&&expr |)(的|)(圆心(?!角))",
            "((?<!椭)圆) [A-Z](_[0-9]+|)&&circle ((:) [^&\\s]+&&expr |)(上存在一点)",
            "△ [^&\\s]+&&triangle (的|)(重心|外心|内心|垂心)",
            "((另|)(一条)(直线) )[a-z](_\\d+|)&&line ((:) [^&\\s]+&&expr |)(和|与)(x|y)轴(的|)(交点)",
            "((?<!(一条|另一条))(直线) )[a-z](_\\d+|)&&line ((:) [^&\\s]+&&expr |)(和|与)(x|y)轴(的|)(交点)",
            "(圆锥曲线|椭圆|双曲线|抛物线) [A-Z](_[0-9]+|)&&line ((:) [^&\\s]+&&expr |)((的|)图象|)(和|与)(x|y)轴(的|)(交点)",
            "(函数|)(?<!(:)) [^&\\s]+&&expr (的图象|)(和|与)(x|y)轴(的|)(交点)",
            "复数 [^&\\s]+&&expr (在复平面内|所)对应(的|)点",
            "满足条件 [^&\\s]+&&expr 的复数 [^&\\s]+&&expr (在复平面上|所)对应点",
            "((定|)点|) [^&\\s]+&&point (点|)关于(直线) [^&\\s]+&&line ((:) [^&\\s]+&&expr |)(的|)对称点",
            "((动|)点|) [^&\\s]+&&point (点|)关于(x轴|y轴|x轴正半轴|x轴负半轴|y轴正半轴|y轴负半轴)(的|)对称(的|)点",
            "直线 [a-z](_\\d+|)&&line ((:) [^&\\s]+&&expr |)上(的|)(动|一|定|)点",
            "不在(x|y)轴上的(动|定|)点",
            "(直线|) [A-Za-z](_[0-9]+|)&&line ((:) [^&\\s]+&&expr |)与曲线 [A-Za-z](_[0-9]+|)&&line ((:) [^&\\s]+&&expr |)的交点(?! [^&\\s]+&&point )",
            "(直线|) [A-Za-z](_[0-9]+|)&&line ((:) [^&\\s]+&&expr |)与(反比例|正比例)函数 [^&\\s]+&&expr 的((另|)一个|)交点(?! [^&\\s]+&&point )",
            "(反比例|正比例|)函数 [^&\\s]+&&expr 与(直线|) [A-Za-z](_[0-9]+|)&&line ((:) [^&\\s]+&&expr |)的((另|)一个|)交点(?! [^&\\s]+&&point )",
    };

    public static String[] lineRegex = {
            "(圆锥曲线|椭圆|双曲线|抛物线) [A-Z](_[0-9]+|)&&line ((:) [^&\\s]+&&expr |)(的|)(一条|)((左|右|)准线|渐近线)(?!(是|为) [^&\\s]+&&expr)",
            "(圆锥曲线|抛物线|曲线) [A-Z](_[0-9]+|)&&line ((:) [^&\\s]+&&expr |)(的|)(一条|)(对称轴)(?!(是|为|)( [^&\\s]+&&expr|(x|y)轴|直线))",
            "(椭圆|双曲线) [A-Z](_[0-9]+|)&&line ((:) [^&\\s]+&&expr |)(的|)(一条)(对称轴)",
//            "(直线|线段|边|弦|) [^&\\s]+&&line (的|)(垂直平分线|中垂线|垂线)(?!(( |),)垂足)",
            "(直线|线段|边|弦|对角线) [^&\\s]+&&line (的|)(垂直平分线|中垂线|垂线)(?!( [^&\\s]+&&line|交(边|) [^&\\s]+&&line 于(点|) [^&\\s]+&&point|) ,垂足)",
            "(?<!(直线|线段|边|弦|对角线)) [^&\\s]+&&line (的|)(垂直平分线|中垂线|垂线)(?!( [^&\\s]+&&line|) ,垂足)",
//            "(直线|线段|边|弦|) [^&\\s]+&&line (的|)(延长线)",
//            "(直线) [a-z](_\\d+|)&&line ((:) [^&\\s]+&&expr |)(的)(垂直平分线|中垂线|垂线)(?!(( |),)垂足)",
            "(直线) [a-z](_\\d+|)&&line ((:) [^&\\s]+&&expr |)(的)(垂直平分线|中垂线|垂线)(?!( [^&\\s]+&&line|) ,垂足)",
            "(直线) [a-z](_\\d+|)&&line (:)" +
                    " [^&\\s]+&&expr (的)(延长线)",
            "((?<!椭|半)圆) [A-Z](_[0-9]+|)&&circle (:) [^&\\s]+&&expr (的)(一条|)(切线)(?!((\\s|),切点))",
//            "((?<!双)曲线) [A-Z](_[0-9]+|)&&line ((:) [^&\\s]+&&expr |)(在|上)((点|) [^&\\s]+&&point (点|)| [^&\\s]+&&expr )(处|)(的|)(切线)(?!((\\s|),切点)|方程为)",
            "(圆锥曲线|椭圆|双曲线|抛物线) [A-Z](_[0-9]+|)&&line ((:) [^&\\s]+&&expr |)(在|上)(((定|)点|) [^&\\s]+&&point (点|)| [^&\\s]+&&expr )处(的|)(切线)(?!(\\s|),切点)",
            "(过)((点|) [^&\\s]+&&point ((定|)点|)| [^&\\s]+&&expr )作(圆锥曲线|椭圆|双曲线|抛物线) [A-Z](_[0-9]+|)&&line ((:) [^&\\s]+&&expr |)(的|)(切线)(?!(\\s|),切点)",
            "(?<!(circle|line) :)(函数|) [^&\\s]+&&expr (的图象|)(在|上)((点|) [^&\\s]+&&point (点|)| [^&\\s]+&&expr )处(的|)(切线)(?!((\\s|),切点|方程为))",
            "(函数|) [^&\\s]+&&expr (的|)(图象|)(在|上)任意一(点|)处(的|)(切线)(?!(\\s|),切点)",
            "((?<!双)曲线)( [A-Z](_[0-9]+|)&&line (:)|) [^&\\s]+&&expr (在|上)((点|) [^&\\s]+&&point (点|)| [^&\\s]+&&expr )(处|的|处的)(切线)(?!(\\s|),切点)",
            "(函数) [^&\\s]+&&expr (在|上)((点|) [^&\\s]+&&point (点|)| [^&\\s]+&&expr )(处|的|处的)(切线)(?!((\\s|),切点|方程))",
            "((?<!双|都是)曲线) [A-Z](_[0-9]+|)&&line (:) [^&\\s]+&&expr (的)(一条|)(切线)(?!(\\s|),切点)",
            "((?<!椭)圆) [^&\\s]+&&circle ((:) [^&\\s]+&&expr |)(在|上)((点|) [^&\\s]+&&point (点|)| [^&\\s]+&&expr )(处|的|处的)(切线)(?!(\\s|),切点)",
            "((?<!椭|半|&&point 作)圆) [^&\\s]+&&circle (的|)切线(?!(\\s|),切点)",
            "(不|)(经|)过(坐标|)原点( [^&\\s]+&&point |)的((另|)一条|任意|)(直线|切线)",
            "(不|)(经|)过((定|)点|) [^&\\s]+&&point (点|)的(另|)(一条|)(直线|切线)",
            "(经|)过((定|)点|) [^&\\s]+&&point (点|)(和|与)(点|) [^&\\s]+&&point (点|)(两点)的(两条|)(直线|切线)",
            "(经|)过(两|)(点|) [^&\\s]+&&point (点|)(和|与|,)(点|) [^&\\s]+&&point (两|)(点|)的直线",
            "(经|)过((定|)点|) [^&\\s]+&&point (点|)(任|)作(任意|)一条直线",
//            "(经|)过(点|) [^&\\s]+&&point (点|)作((x|y)(轴)|(直线|) [^&\\s]+&&line )(的|)(垂线)(?!(( |),)垂足)",
            "(经|)过((定|)点|) [^&\\s]+&&point (点|)(作|做)((x|y)(轴)|(直线|) [^&\\s]+&&line )(的|)(垂线)(?!( [^&\\s]+&&line|) ,垂足)",
            "(经|)过((定|)点|) [^&\\s]+&&point (点|)(作|做)((x|y)(轴)|(直线|) [^&\\s]+&&line )(的|)(平行线)",

            "((经|)过((定|)点|) [^&\\s]+&&point (点|)(且|))与((x|y)(轴)| [^&\\s]+&&line )(垂直|平行)的直线",
            //wzk新增
            "((经|)过((定|)点|) [^&\\s]+&&point (点|))(做|作|)(一条|)与((x|y)(轴)| [^&\\s]+&&line )(不|)(垂直|平行|重合)的(任意|)直线",
            "(?<!(经|)过((定|)点|) [^&\\s]{1,10}&&point (点|)(且|))与((x|y)(轴)| [^&\\s]+&&line )(垂直|平行)的直线",

            "((经|)过((定|)点|) [^&\\s]+&&point (点|)(且|))(不|)(垂直|平行)于((x|y)轴| [^&\\s]+&&line )的直线",
            "(?<!(经|)过((定|)点|) [^&\\s]{1,10}&&point (点|)(且|))(不|)(垂直|平行)于((x|y)轴| [^&\\s]+&&line )的直线",

            "((经|)过((动|)((定|)点|) [^&\\s]+&&point (点|)|原点)且)(斜率|倾斜角)(是|为) [^&\\s]+&&expr 的(直线)",
            "(?<!(经|)过((动|)(点|) [^&\\s]{1,10}&&point (点|)|原点)且)(斜率|倾斜角)(是|为) [^&\\s]+&&expr 的(直线)",

            "(不|)(((经|)过((原|定|)点|) [^&\\s]+&&point (点|)且)|)(斜率|倾斜角)(是|为) [^&\\s]+&&expr 的(直线)",
            "(经|)过((定|)点|) [^&\\s]+&&point (点|)((任(意|)|)作)(一|)(直线|切线)(?! [^&\\s]+&&line ((:) [^&\\s]+&&expr |)的垂线)",
            "平行于 [^&\\s]+&&line 的直线",
            "(不|)(((经|)过((原|定|)点|) [^&\\s]+&&point (点|)(且|))|)以 [^&\\s]+&&expr 为方向向量的直线",
            "(?<!(circle (:)|&&line (:)))(函数|) [^&\\s]+&&expr (的|)对称轴",

            "((?<!中)点|) [^&\\s]+&&point (点|)的(轨迹)(?!(记|)(是|为)(曲线|直线) [^&\\s]+&&line )",
            "与(直线|) [^&\\s]+&&line (:) [^&\\s]+&&expr 夹角为 [^&\\s]+&&expr (的|)直线",
            " [^&\\s]+&&line 在点 [A-Z]{1}+&&point 处的切线(?![^&\\s]+&&line)",
    };

    public static String[] segmentRegex = {
            "(圆锥曲线|椭圆|双曲线|抛物线) [A-Z](_[0-9]+|)&&line ((:) [^&\\s]+&&expr |)(的|)(一条|)(长轴|短轴|实轴(?!长)|虚轴(?!长)|(?<!动)弦)",
            "((?<!椭|半|外接)圆|⊙) [A-Z](_[0-9]+|)&&circle (:) [^&\\s]+&&expr (的)(一条|)(直径|半径|弦)(?! [^&\\s]+&&expr)",
            "((?<!椭|半|外接)圆|⊙) [A-Z](_[0-9]+|)&&circle ((:) [^&\\s]+&&expr |)(和|与)((?<!椭|半)圆|⊙) [A-Z](_[0-9]+|)&&circle ((:) [^&\\s]+&&expr |)(的)(公共弦)(?! [^&\\s]+&&expr)",
            "(?<!求)((?<!椭|半|外接|内切)圆|⊙) [A-Z](_[0-9]+|)&&circle (的)(直径|半径|弦)(?! [^&\\s]+&&expr)",
            "过原点的(半径|直径|弦)",
            "(直线) [a-z](_\\d+|)&&line ((:) [^&\\s]+&&expr |)被((?<!椭)圆) [A-Z](_[0-9]+|)&&circle ((:) [^&\\s]+&&expr |)截得(的|)(弦)(?! [^&\\s]+&&expr)",


            "((等腰直角|等腰Rt|等腰|等边|锐角|直角|钝角|Rt)(△|三角形)) [^&\\s]+&&triangle (的|中|上)((斜|底|)边|线段) [^&\\s]+&&line (上|)的(中线|高)",
            "((等腰直角|等腰Rt|等腰|等边|锐角|直角|钝角|Rt)(△|三角形)) [^&\\s]+&&triangle (的|中|上) [^&\\s]+&&line (边)(上|)的(中线|高)",
            "((等腰直角|等腰Rt|等腰|等边|锐角|直角|钝角|Rt)(△|三角形)) [^&\\s]+&&triangle (的|中(的|)|上(的|))(中线|高)",

            "(?<!(等腰直角|等腰Rt|等腰|等边|锐角|直角|钝角|Rt))(△|三角形) [^&\\s]+&&triangle (的|中|上)((斜|底|)边|线段) [^&\\s]+&&line (上|)的(中线|高)",
            "(?<!(等腰直角|等腰Rt|等腰|等边|锐角|直角|钝角|Rt))(△|三角形) [^&\\s]+&&triangle (的|中|上) [^&\\s]+&&line (边)(上|)的(中线|高)",
            "(?<!(等腰直角|等腰Rt|等腰|等边|锐角|直角|钝角|Rt))(△|三角形) [^&\\s]+&&triangle (的|中(的|)|上(的|))(中线|高)",

            "(?<!(&&triangle (中|的|上|)))((斜|底)边|线段) [^&\\s]+&&line (边|)(上|)的(中线|高)(?! ,垂足)",
            "(?<!(&&triangle (中|的|上|)))((?<!(斜|底))边|线段) [^&\\s]+&&line (边|)(上|)的(中线|高)(?! ,垂足)",
            "(?<!(&&triangle (中|的|上)))(?<!(边|线段)) [^&\\s]+&&line (边|)(上|)的(中线|高)(?! ,垂足)",

            "过(点|) [^&\\s]+&&point (点|)的(半径|直径|弦)",

            "(?<!(Rt|直角|等腰|等边))(△|三角形) [^&\\s]+&&triangle ((中|的|)(外(角|)|)(∠|角) [^&\\s]+&&angle |)(的|)(角|外角)平分线",
            "(?<!((△|三角形) [^&\\s]{1,6}&&triangle (中|的|)(外(角|)|)|(∠|角) [^&\\s]{1,6}&&angle (和|与|,)|(外角|邻补角)|三角形(中|内)))(∠|角) [^&\\s]+&&angle (的|)(角|外角|)平分线",
    };

    public static String[] functionRegex = {
            //求函数y=f(x)的反函数y=(f^(-1))(x)
            "(?<!求)(?<!(奇|偶|二次|)函数) [^&\\s]+&&expr (的)(反|导)函数(?=((的|)图象|(的|)定义域|(的|)值域))(?!(是_))",
            "(?<!求)((奇|偶|二次|)函数) [^&\\s]+&&expr (的)(反|导)函数(?=((的|)图象|(的|)定义域|(的|)值域))(?!(是_))",
            "(抛物线) [^&\\s]+&&line ((:) [^&\\s]+&&expr |)(的)(反|导)函数(?=((的|)图象))(?!(是_))",
            "(若|)((奇|偶|二次|)函数) [^&\\s]+&&expr (的)(反|导)函数(?=过点)",
    };

    public static String[] functionRegex1 = {
            "(?<!求)(与|和)((奇|偶|二次|一次|(反|正)比例|)函数)(?! [^&\\s]+&&expr)",
            "(?<!求)((奇|偶|二次|一次|)函数) [^&\\s]+&&expr (的|)(图象|)(与|和)(某|另(外|))(一(个|)|)((反|正)比例|一次|二次奇|偶|)函数(?! [^&\\s]+&&expr)",

    };

    public static String[] circleRegex = {
            "以(点|) [^&\\s]+&&point (点|)为圆心(,|且|)(以|) [^&\\s]+&&(expr|line) 为(半|直)径(的(半|)|作)圆(?!心)",
            "以(点|) [^&\\s]+&&point (点|)为圆心且与直线 [^&\\s]+&&line ((:) [^&\\s]+&&expr |)相切的(半|)圆(?!心)",
            "圆心(是|为) [^&\\s]+&&point 且与直线 [^&\\s]+&&line ((:) [^&\\s]+&&expr |)相切的(半|)圆(?!心)",
            "圆心(是|为) [^&\\s]+&&point 的(半|)圆(?!心)",
            "(以 |)[^&\\s]+&&expr 为(半|直)径的(半|)圆(?!心)",
            "以(线段|) [^&\\s]+&&line 为(半|直)径的(半|)圆(?!心)",
            "以(点|) [^&\\s]+&&point (点|)(为|是)圆心的(半|)圆(?!心)",
            "(经|)过(点|) [^&\\s]+&&point (点|)的(半|)圆(?!(心| [A-Z](_[0-9]+|)&&circle 的切线))",
            "(经|)过(点|) [^&\\s]+&&point (和|与|,) [^&\\s]+&&point (和|与|,) [^&\\s]+&&point (三点)的(半|)圆(?!心)",
            "(经|)过(点|) [^&\\s]+&&point (和|与|,) [^&\\s]+&&point (两点)的(半|)圆(?!心)",
            "(边长为) [^&\\s]+&&expr 的(等边)(△|三角形) [^&\\s]+&&triangle (的|)(外接圆|内切圆)",
            "(等腰直角|等腰Rt|等腰|等边|锐角|直角|钝角)(△|三角形) [^&\\s]+&&triangle (的|)(外接圆|内切圆)",
            "(?<!(等腰直角|等腰Rt|等腰|等边|锐角|直角|钝角))(△|三角形) [^&\\s]+&&triangle (的|)(外接圆|内切圆)",
            "(?<!等腰)(Rt)(△|三角形) [^&\\s]+&&triangle (的|)(外接圆|内切圆)",
            "(平行四边形|四边形|长方形|正方形|菱形|矩形|平行□|等腰梯形|等腰直角梯形|直角梯形|梯形|Rt梯形|□|4边形|平行4边形) [^&\\s]+&&quadrangle (的|)(外接圆|内切圆)",
            "(曲线) [^&\\s]+&&line ((:) [^&\\s]+&&expr |)(的|)(外接圆|内切圆)",
    };

    public static String[] triangleRegex = {
            "(直线) [^&\\s]+&&line ((:) [^&\\s]+&&expr |)(与|和)两?坐标轴(围成)的三角形"
    };

    public static String[] dualExprRegex = {
            "(圆锥曲线|椭圆|双曲线) [^&\\s]+&&line ((:) [^&\\s]+&&expr |)(和|与)(圆锥曲线|椭圆|双曲线) [^&\\s]+&&line ((:) [^&\\s]+&&expr |)的离心率",
            "(直线) [^&\\s]+&&line (与|和|,)(直线|) [^&\\s]+&&line (的|)(斜率|倾斜角)",
            "((等腰直角|等腰Rt|等腰|等边|锐角|直角|钝角|Rt|)(△|三角形)) [^&\\s]+&&triangle (和|与)((等腰直角|等腰Rt|等腰|等边|锐角|直角|钝角|Rt|)(△|三角形)) [^&\\s]+&&triangle (的|)(面积|周长)",
            "(平行四边形|四边形|长方形|正方形|菱形|矩形|等腰梯形|等腰直角梯形|直角梯形|梯形) [^&\\s]+&&quadrangle (和|与)(平行四边形|四边形|长方形|正方形|菱形|矩形|等腰梯形|等腰直角梯形|直角梯形|梯形) [^&\\s]+&&quadrangle (的|)(面积|周长)",
            "(平行四边形|四边形|长方形|正方形|菱形|矩形|等腰梯形|等腰直角梯形|直角梯形|梯形) [^&\\s]+&&quadrangle (和|与)((等腰直角|等腰Rt|等腰|等边|锐角|直角|钝角|Rt|)(△|三角形)) [^&\\s]+&&triangle (的|)(面积|周长)",
            "(((正|长)方体)|((直|正|斜|)((三|四|五|六|)棱(柱|锥|台)|四面体|五面体))|多面体) [^&\\s]+&&polyhedron (和|与)(((正|长)方体)|((直|正|斜|)((三|四|五|六|)棱(柱|锥|台)|四面体|五面体))|多面体) [^&\\s]+&&polyhedron (的|)(表面积|体积)",
            "(点|) [^&\\s]+&&point (点|)(和|与|,)(点|) [^&\\s]+&&point (点|)的(横|纵)坐标",
            "(函数|) [^&\\s]+&&expr (在(区间|) [^&\\s]+&&set 上|)的最大值(和|与|及)最小值",
//            "(动点|点|) [^&\\s]+&&point (到)(点|) [^&\\s]+&&point (点|)的距离与(点|) [^&\\s]+&&point (点|)到(直线) [^&\\s]+&&line ((:) [^&\\s]+&&expr |)的距离",
            "(动点|点|) [^&\\s]+&&point (到)(点|) [^&\\s]+&&point (点|)的距离与(点|) [^&\\s]+&&point (点|)到(点|) [^&\\s]+&&point (点|)(的|)距离",
            "(动点|点|) [^&\\s]+&&point (到)(点|) [^&\\s]+&&point (点|)的距离与到(直线) [^&\\s]+&&line ((:) [^&\\s]+&&expr |)的距离",
            "(动点|点|) [^&\\s]+&&point (到)(点|) [^&\\s]+&&point (点|)(及|和|与)(直线|) [^&\\s]+&&line ((:) [^&\\s]+&&expr |)的距离",
            "(动点|点|) [^&\\s]+&&point (和|,|与)(点|) [^&\\s]+&&point (点|)到(直线|) [^&\\s]+&&line ((:) [^&\\s]+&&expr |)的距离",
            "(动点|点|) [^&\\s]+&&point (到)(点|) [^&\\s]+&&point (点|)(和|与|,)(点|) [^&\\s]+&&point (点|)的距离",
            "(动点|点|) [^&\\s]+&&point (到)(直线|) [^&\\s]+&&line ((:) [^&\\s]+&&expr |)(和|与|,)(直线|) [^&\\s]+&&line ((:) [^&\\s]+&&expr |)的距离",
            " [^&\\s]+&&expr (的|)展开式中, [^&\\s]+&&expr 的系数与 [^&\\s]+&&expr 的系数",
            " [^&\\s]+&&expr (的|)展开式中,各项系数的和与其各项二项式系数的和",
            "(复数|) [^&\\s]+&&expr (的|)实部与虚部",
            "关于 [^&\\s]+&&expr (的|)(一元|)(二次|)方程 [^&\\s]+&&expr 的两根",
            "(一元|)(二次|)方程 [^&\\s]+&&expr 的两根",

    };

    public static String[] dualPointRegex = {
            "(圆) [^&\\s]+&&circle ((:) [^&\\s]+&&expr |)与直线 [^&\\s]+&&line ((:) [^&\\s]+&&expr |)的交点",
            "(圆锥曲线|椭圆|双曲线) [^&\\s]+&&line ((:) [^&\\s]+&&expr |)(的|)(两(个|))(焦点)(?!(在(y|x)轴上|分别(为|是)))",
            "(圆锥曲线|椭圆|双曲线|抛物线) [^&\\s]+&&line ((:) [^&\\s]+&&expr |)(的|)(上下)(两个顶点)",
            "(抛物线) [^&\\s]+&&line ((:) [^&\\s]+&&expr |)(和|与)(x|y)轴(的|)(两(个|)交点)",
            "(椭圆) [^&\\s]+&&line (的|)左右顶点",
    };

    public static String[] dualLineRegex = {
            "(双曲线) [^&\\s]+&&line ((:) [^&\\s]+&&expr |)(的|)(两条渐近线)(?!分别(为|是))",
            "过原点( [^&\\s]+&&point |)的两条直线(?!分别(为|是))",
            "(双曲线|椭圆) [^&\\s]+&&line ((:) [^&\\s]+&&expr |)(的|)(两(条|)准线)(?!分别(为|是))",
            "(抛物线|椭圆) [^&\\s]+&&line ((:) [^&\\s]+&&expr |)(在)(点|) [^&\\s]+&&point (点|)(和|与)(点|) [^&\\s]+&&point (点|)处(的|)切线",
            "(函数|) [^&\\s]+&&expr 上(两点|)(点|) [^&\\s]+&&point (点|)(和|与)(点|) [^&\\s]+&&point (点|)处(的|)切线",
            "(函数|) [^&\\s]+&&expr 在 [^&\\s]+&&expr (和|与) [^&\\s]+&&expr 处(的|)切线",
            "(圆) [^&\\s]+&&circle ((:) [^&\\s]+&&expr |)(和|与)(圆) [^&\\s]+&&circle ((:) [^&\\s]+&&expr |)在(点|) [^&\\s]+&&point (点|)处(的|)切线",
            "(过|经过) [^&\\s]+&&point (和|与) [^&\\s]+&&point 两点分别(作|做)(抛物线|椭圆) [^&\\s]+&&line ((:) [^&\\s]+&&expr |)(的|)切线",
    };
    public static String[] dualSegmentRegex = {
            "(∠|角) [^&s]+&&angle (,|和|与)(∠|角) [^&s]+&&angle 的(角|外角|)平分线",
            "((?<!椭|半)圆) [A-Z](_[0-9]+|)&&circle (和|与)(圆) [A-Z](_[0-9]+|)&&circle (的)(半径)",

    };

    public static String[] exprRegex = {
//            "(等比|等差|)(数列) [^&\\s]+&&sequ (的|)前 n&&expr 项和(?!(记为|(是|为|等于)_))",
//            "(?<!(等比|等差|)(数列)) [^&\\s]+&&sequ (的|)前 n&&expr 项和(?!(记为|(是|为|等于)_))",
            "(椭圆|双曲线) [A-Z](_[0-9]+|)&&line ((:) [^&\\s]+&&expr |)(的|)(焦距)(?!(和|相等|相同))",
            "(圆锥曲线|椭圆|双曲线|抛物线) [A-Z](_[0-9]+|)&&line ((:) [^&\\s]+&&expr |)(的|)(离心率)(?!(和|相等|相同))",
            "(圆锥曲线|椭圆|双曲线|抛物线) [A-Z](_[0-9]+|)&&line ((:) [^&\\s]+&&expr |)(的|)(一条|)(虚轴长|实轴长|(短|虚)半轴长|(长|实)半轴长)(?!(和|相等|相同))",
            "(直线) [a-z](_\\d+|)&&line (:) [^&\\s]+&&expr (的|)(斜率)",
            "(直线|线段|) [^&\\s]+&&line (的|)(斜率)(?!依次)",
            "(直线) [^&\\u4E00-\\u9FA5\\s]+&&expr (的|)(斜率)",
            "(?<!的)(直线)(?!(( [^&\\s]+&&expr)|( (([a-z](_\\d+|))|([A-Z](_\\d+|)){2})&&line))) (的|)(斜率)",
            "(直线|切线) [a-z](_\\d+|)&&line (:) [^&\\s]+&&expr 在(x|y)轴上(的|)(截距)",
            "(直线|切线) [^&\\s]+&&line 在(x|y)轴上(的|)(截距)",
            "[a-z](_\\d+|)&&line 在(x|y)轴上(的|)(截距)",
            "(?<!(弦|&&line (和|与|,)))(线段|(斜|)边|) [^&\\s]+&&line (的|)(长度)(?!(半|)轴)",
            "(?<!(弦|高|&&line (和|与|,)))(直线|线段|(斜|)边|) [^&\\s]+&&line (的|)(长)(?!(半|)轴|度)",
            "(((正|长)方体)|((直|正|斜|)((三|四|五|六|)棱(柱|锥|台)|四面体|五面体))|多面体) [^&\\s]+&&polyhedron (的|)(周长|面积|高|体积|底面积|表面积|侧面积|底面周长)(?!((和|及|与)(周长|面积|高|体积|底面积|表面积|侧面积|底面周长)))",
            "(?<!&&triangle (和|与|,))((等腰直角|等腰Rt|等腰|等边|锐角|直角|钝角|Rt|)(△|三角形)) [^&\\s]+&&triangle (的|)(周长|面积)(?!分为)",
            "(?<!&&line 平分)((平行|∥)四边形|四边形|□|正方形|菱形|矩形|(等腰Rt|等腰Rt|等腰|)梯形) [^&\\s]+&&quadrangle (的|)(周长|面积|高)(?!分为)",
            "((?<!半)(圆|⊙)|半圆) [^&\\s]+&&circle( : [^&\\s]+&&expr|) (的|)(周长|面积)",
            "((动|原|)点|) [^&\\s]+&&point (点|)(与|到|和)((动|)点|) [^&\\s]+&&point (两点|点|)((之|)间|)(的|)(长度|距离|间距|长)(?!之)",
            "((动|)点|) [^&\\s]+&&point (点|)(与|到|和)(x轴|y轴|x轴正半轴|x轴负半轴|y轴正半轴|y轴负半轴)((之|)间|)(的|)(长(度|)|距离|间距)",
            "((动|原|)点|) [^&\\s]+&&point (点|)(与|到|和)(直线|) [^&\\s]+&&line (边| )((:) [^&\\s]+&&expr |)(的|)(长(度|)|距离|间距)",
            "((动|原|)点|) [^&\\s]+&&point (点|)(与|到|和)(直线|) [a-z](_\\d+|)&&line ((:) [^&\\s]+&&expr |)(的|)(长(度|)|距离|间距)",
            "(原点)(与|到|和)((动|)点|) [^&\\s]+&&point (两点|点|)((之|)间|)(的|)(长度|距离|间距|长)(?!之)",
            "(原点)(与|到|和)(直线|) [^&\\s]+&&line ((:) [^&\\s]+&&expr |)(的|)(长(度|)|距离|间距)",
            "(直线|) [^&\\s]+&&line ((:) [^&\\s]+&&expr |)(和|与|到)(x轴|y轴)(之间|)(的|)(长(度|)|距离|间距)",
            "((动|原|)点|) [^&\\s]+&&point (点|)(与|到|和)((坐标|)原点)(的|)(长(度|)|距离|间距)",
            "(?<!(前 n&&expr 项和))(函数|) [^&\\s]+&&expr (的|)(最大值|最小值|极大值|极小值)(?!(点|记为|(和|与|及|)(解析(表达|)式|方程|定义域|值域|奇偶性|偶函数|奇函数|周期|最小正周期|最值|最大值|取(得|)(最大值|最小值)(时)|(相|对)应的|此时|函数最小值|最小值|极值|极大值|极小值|单调区间|(单调|)(递|)减(区间|函数|)|(单调|)(递|)增(区间|函数|)|单调性|斜率|开口方向|对称轴(方程|)|顶点|递增区间|递减区间|切线方程)))",
            "(直线) [a-z](_\\d+|)&&line ((:) [^&\\s]+&&expr |)被((?<!椭)圆) [A-Z](_[0-9]+|)&&circle ((:) [^&\\s]+&&expr |)截得(的|)(弦长)",
//            BJ201520L
            "集合 [^&\\s]+&&set (的|中(的|所含|)|)(元素|非空子集|子集|真子集|非空真子集|)(的|)个数",
            "集合 [^&\\s]+&&set (中|)存在一个元素",
            " [^&\\s]+&&expr (与|和) [^&\\s]+&&expr (之|的)(和|积|差|比)",
            " [^&\\s]+&&expr (除以) [^&\\s]+&&expr 的商",
            " [^&\\s]+&&expr (与|和) [^&\\s]+&&expr 的比(值|)",
            "(?<![^&\\s]{1,10}&&point (点|)(与|和|,))(点|圆心|) [^&\\s]+&&point (点|)(的|)((横|纵)坐标)(?!((和|与)(横|纵)坐标))",
            "(函数|方程|) [^&\\s]+&&expr 在(区间|) [^&\\s]+&&set 上的(最大值|最小值|极大值|极小值)(?!((和|与|是)(最大值|最小值|极大值|极小值))|点)",
            "(函数|方程|) [^&\\s]+&&expr 在(区间|) [^&\\s]+&&set 内解的个数",
            " [^&\\s]+&&expr 的展开式中, [^&\\s]+&&expr 的系数",
            "(直线|线段|) [^&\\s]+&&line (和|与|,)(直线|线段|) [^&\\s]+&&line (之间|)(的|)夹角(?= [^&\\s]+&&expr)",//此情况只能提取,不能引入
            "((单位|)向量|) [^&\\s]+&&expr (和|与|,)((单位|)向量|) [^&\\s]+&&expr (之间|)(的|)夹角",
            "向量 [^&\\s]+&&expr (的|)(模长|模)",
            "(集合) [^&\\s]+&&set (中)(的|)(最小整数|最大整数|最小元素|最大元素)",
//            " [^&\\s]+&&line (与|和)(平面|) [^&\\s]+&&plane 所成(的|)角",
            "球 [A-Za-z]{1}+&&Ball (的|)表面积",
            "(三棱锥|多面体) [^&\\s]+&&polyhedron (的|)外接球的?(体积|表面积|直径|半径)(?!((是|为) [^&\\s]+&&expr) )",
//            "(直线) [^&\\s]+&&line ((:) [^&\\s]+&&expr |)(与|和)两?坐标轴(围成)的(三角形|图形)的?(面积|周长)",
            "(平|底|侧|)面? [^&\\s]+&&plane (与|和)(平|底|侧|)面? [^&\\s]+&&plane (所成)二面角的(大小|值)(?!(是|为| [^&\\s]+&&expr))",
            "(二面角) [^&\\s]+&&angle 的?(余弦|正弦|余切|正切)值的?(大小|)(?!(是|为| [^&\\s]+&&expr))",
            "(圆|⊙) [^&\\s]+&&circle( : [^&\\s]+&&expr)? 在(x|y)轴上截得的弦长",
            "(圆|⊙) [^&\\s]+&&circle( : [^&\\s]+&&expr)? 在(x|y)轴上截得的弦长(?=为定值)",
            "函数 [^&\\s]+&&expr 极值点的个数(?!(是|为| [^&\\s]+&&expr))",
            "(函数|) [^&\\s]+&&expr 的零点(?!(的|)个数)",
//            "函数 [^&\\s]+&&expr 在区间 \\([^&\\s]+\\)&&set 上的零点个数",
    };

    public static String[] angleRegex = {
            //  "(直线|线段|) [^&\\s]+&&line (和|*/与)(平面|) [^&\\s]+&&plane 所成(的|)角",
            "(直线|射线|线段|线|边|棱|侧棱|对角线|切线|弦|) [^&\\s]+&&line (的|)倾斜角",
            "(?<!异面)(直线|线段) [^&\\s]+&&line (和|与|,)(直线|线段|) [^&\\s]+&&line (之间|)(的|)夹角",
            "(?<!异面直线) [^&\\s]+&&line (和|与|,) [^&\\s]+&&line (之间|)(的|)夹角",
            "(直线|线段|) [^&\\s]+&&line (和|与)(x轴|y轴|x轴正半轴|x轴负半轴|y轴正半轴|y轴负半轴) [^&\\s]+&&line (之间|)(的|)夹角",
    };


    /**
     * 循环两圈，先进行两个变量的引入，然后进行单个的引入
     *
     * @param sorString
     * @return
     */
    public static String handleEntitySentence(String sorString) {

        sorString = sorString.replace("  ", " ");
        String tempString = sorString;
        try {
            //循环两圈,测试题号:BJ1405
            for (int i = 0; i < 2; i++) {

                //两个变量引入
                tempString = dualPointMove1(tempString);
                tempString = dualLineMove1(tempString);
                tempString = dualExprMove1(tempString);
                tempString = dualSegmentMove1(tempString);

                //下面为单个变量的引入
                tempString = pointMove1(tempString);
                tempString = pointMove2(tempString);

                tempString = segmentMove1(tempString);
                tempString = segmentMove2(tempString);

                tempString = circleMove1(tempString);
                tempString = circleMove2(tempString);

                tempString = lineMove1(tempString);
                tempString = lineMove2(tempString);

                tempString = exprMove1(tempString);
                tempString = dualExprMove1(tempString);
                tempString = exprMove2(tempString);

                tempString = functionMove1(tempString);
                tempString = functionMove2(tempString);

                tempString = angleMove1(tempString);
                tempString = angleMove2(tempString);

            }
        } catch (Exception e) {
            tempString = sorString;
        }
        return tempString;
    }

    public static String lineMove2(String sorString) {

        if (sorString.contains("分别")) {
            return sorString;
        }
        String regexType11 = "(?<!(的|&&line (是|为)))" + LTools.createRegexOrByStringList(lineRegex) + "( [^&\\s]+&&line(\\s|)|)";
        Pattern patternC = Pattern.compile(regexType11);
        Matcher matcherC = patternC.matcher(sorString);
        if (matcherC.matches()) {
            //对于添加辅助线的整句匹配的句子,引入实体
            if(!StringUtils.endsWith(sorString,"&&line ")){
                String guide = "(作|做)(一条|)(与|和|)((x|y)轴|((直线|) [^&\\s]+&&line ))";
                Pattern pGuide = Pattern.compile(guide);
                Matcher mGuide = pGuide.matcher(sorString);
                if (mGuide.find()){
                    String line1 = " n_" + RandomVarsSave.count + "&&line ";
                    RandomVarsSave.count++;
                    sorString = sorString+line1;
                }
            }
            return sorString;
        }
//          HN200511W垂直平分线方程
        String regexType1 = "(?<!(的|&&line (是|为)))" + LTools.createRegexOrByStringList(lineRegex) + "(?!((是|为) [^&\\s]+&&line))";
        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;
        Pattern pattern = Pattern.compile(regexType1);
        Matcher matcher = pattern.matcher(sorString);
        while (matcher.find()) {
            int start = matcher.start();
            stringBuilder.append(sorString.substring(pos, start));
            String sava = matcher.group();
            VarsSaveData varsSaveData = checkRep(sava);
            String randomVar;
            String namedPoint;
            if (varsSaveData != null) {
                randomVar = varsSaveData.getName();
                namedPoint = " " + randomVar + "&&line ";
            } else {
                randomVar = "n_" + RandomVarsSave.count;
                RandomVarsSave.addToSave(randomVar);
                namedPoint = " n_" + RandomVarsSave.count++ + "&&line ";
                String total = sava + namedPoint;
                addToSave(total);
                savedDataList.add(new VarsSaveData(randomVar, sava, total));
            }
            if (sava.endsWith("轨迹")) {
                stringBuilder.append("轨迹").append(namedPoint);
            } else if (sava.endsWith("对称轴")) {
                stringBuilder.append("直线").append(namedPoint);
            } else {
                stringBuilder.append(namedPoint);
            }
            pos = matcher.end();
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }

    public static boolean addToSave(String addSave) {
        if (savedList.contains(addSave)) {
            return false;
        }
        return savedList.add(addSave);
    }


    //则双曲线C_0:((x^2)/(a^2))-((y^2)/(b^2))=1(a>0,b>0)的离心率的取值范围是
    public static String exprMove2(String sorString) {
        if (sorString.contains("分别")) {
            return sorString;
        }
        String regexType1C = "(?<!(的|&&expr (是|为)))" + LTools.createRegexOrByStringList(exprRegex) + " [^&\\s]+&&expr " + "(?!和最(大|小)值 [^&\\s]+&&expr)";
        Pattern patternC = Pattern.compile(regexType1C);
        Matcher matcherC = patternC.matcher(sorString);
        if (matcherC.matches()) {
            return sorString;
        }
        if (sorString.matches(" [^&\\s]+&&line (是|为)(((正|长)方体)|((直|正|斜|)((三|四|五|六|)棱(柱|锥|台)|四面体|五面体))|多面体) [^&\\s]+&&polyhedron (的|)(高)")) {
            return sorString;
        }
        String regexType1 = "(?<!(的|&&expr (是|为)))" + LTools.createRegexOrByStringList(exprRegex) + "((?!((是|为) [^&\\s]+&&expr))|(?=((是|为) [^&\\s]+&&expr (?=的))))";
        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;
        Pattern pattern = Pattern.compile(regexType1);
        Matcher matcher = pattern.matcher(sorString);
        if (matcher.matches()) {
            return sorString;
        }
        matcher.reset();
        while (matcher.find()) {
            int start = matcher.start();
            stringBuilder.append(sorString.substring(pos, start));
            String sava = matcher.group();

            VarsSaveData varsSaveData = checkRep(sava);
            String randomVar;
            String addExpr;
            if (varsSaveData != null) {
                randomVar = varsSaveData.getName();
                addExpr = " " + randomVar + "&&expr ";
            } else {
                randomVar = "v_" + RandomVarsSave.count;
                RandomVarsSave.addToSave(randomVar);
                addExpr = " v_" + RandomVarsSave.count++ + "&&expr ";
                String total = sava + "为" + addExpr;
                addToSave(total);
                savedDataList.add(new VarsSaveData(randomVar, sava, total));
            }
            stringBuilder.append(addExpr);
            pos = matcher.end();
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }

    public static String functionMove2(String sorString) {

        String regexType11 = "(?<!(的|&&expr (是|为)))" + LTools.createRegexOrByStringList(functionRegex) + " [^&\\s]+&&expr ";
        Pattern patternC = Pattern.compile(regexType11);
        Matcher matcherC = patternC.matcher(sorString);
        if (matcherC.matches()) {
            return sorString;
        }
        String regexType1 = "(?<!(的|&&expr (是|为)))" + LTools.createRegexOrByStringList(functionRegex) + "(?!((是|为) [^&\\s]+&&expr))";
        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;
        Pattern pattern = Pattern.compile(regexType1);
        Matcher matcher = pattern.matcher(sorString);
        while (matcher.find()) {
            int start = matcher.start();
            stringBuilder.append(sorString.substring(pos, start));
            String sava = matcher.group();
            String randomVar = "y=f_" + RandomVarsSave.count + "(x)";
            RandomVarsSave.addToSave(randomVar);
            String addExpr = " " + randomVar + "&&expr ";
            RandomVarsSave.count++;
            addToSave(sava + "为" + addExpr);
            stringBuilder.append(addExpr);
            pos = matcher.end();
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }


    //把  椭圆C的焦点F交AB于点C   变为   椭圆C的焦点F,F交AB于点C
    public static String pointMove1(String sorString) {

        if (sorString.contains("分别")) {
            return sorString;
        }
        String pointEntity = " [^&\\s]+&&point ";
        String regexType1 = "(?<!(的|&&point (是|为)))" + LTools.createRegexOrByStringList(pointRegex) + pointEntity;

        int pos = 0;

        String reString = sorString;
        Pattern pattern = Pattern.compile(regexType1);
        Matcher matcher = pattern.matcher(reString);

        //如果整句匹配则不提取
        if (matcher.matches()) {
            return sorString;
        }
        matcher.reset();
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String sava = matcher.group();
            addToSave(sava);
            String sinPoint = getSinglePoint(sava);
            if (start == pos && end == reString.length()) {
                addToSave(sava);
                return "";
            }
            reString = reString.substring(pos, start) + sinPoint + reString.substring(end, reString.length());
            matcher.reset(reString);
        }
        return reString;
    }


    //则双曲线C_0:((x^2)/(a^2))-((y^2)/(b^2))=1(a>0,b>0)的离心率e的取值范围是
    public static String exprMove1(String sorString) {

        if (sorString.contains("分别")) {
            return sorString;
        }
        String regexType1 = "(?<!(的|&&expr (是|为)))" + LTools.createRegexOrByStringList(exprRegex) + " [^&\\s]+&&expr " + "(?!和最(大|小)值 [^&\\s]+&&expr)";
        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;
        Pattern pattern = Pattern.compile(regexType1);
        Matcher matcher = pattern.matcher(sorString);
        //如果整句匹配则不提取
        if (matcher.matches()) {
            return sorString;
        }
        matcher.reset();
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String sava = matcher.group();
            if (start == pos && end == sorString.length()) {
                addToSave(sava);
                return "";
            }
            stringBuilder.append(sorString.substring(pos, start));
            addToSave(sava);
            String a = getSingleExpr(sava);
            stringBuilder.append(StringUtils.isNotEmpty(a) ? a : sava);
            pos = matcher.end();
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }

    public static String dualSegmentMove1(String sorString) {

        if (sorString.contains("分别")) {
            return sorString;
        }
        String pointEntity = "(?!((是|为|)((直)线|) [^&\\s]+&&line))";

        String regexType1 = "(?<!(的|&&line (是|为)))" + LTools.createRegexOrByStringList(dualSegmentRegex) + pointEntity;

        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;
        Pattern pattern = Pattern.compile(regexType1);
        Matcher matcher = pattern.matcher(sorString);
        while (matcher.find()) {
            int start = matcher.start();
            stringBuilder.append(sorString.substring(pos, start));
            String sava = matcher.group();


            RandomVarsSave.count++;
            String randomVar1 = "N_" + RandomVarsSave.count++ + "M_" + RandomVarsSave.count++;
            String randomVar2 = "N_" + RandomVarsSave.count++ + "M_" + RandomVarsSave.count++;
            RandomVarsSave.addToSave(randomVar1);
            RandomVarsSave.addToSave(randomVar2);

            String addExpr1 = " " + randomVar1 + "&&line ";
            String addExpr2 = " " + randomVar2 + "&&line ";
            String te = addExpr1 + "," + addExpr2;
            addToSave(sava + "为" + te);
//            savedList.add(sava + "为" + te);

            stringBuilder.append(te);
            pos = matcher.end();
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }


    public static String dualLineMove1(String sorString) {

        String pointEntity = "(?!((是|为|)((直)线|) [^&\\s]+&&line))";

        String regexType1 = "(?<!(的|&&line (是|为)))" + LTools.createRegexOrByStringList(dualLineRegex) + pointEntity;

        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;
        Pattern pattern = Pattern.compile(regexType1);
        Matcher matcher = pattern.matcher(sorString);
        while (matcher.find()) {
            int start = matcher.start();
            stringBuilder.append(sorString.substring(pos, start));
            String sava = matcher.group();

            String randomVar1 = "n_" + RandomVarsSave.count++;
            String randomVar2 = "n_" + RandomVarsSave.count++;
            RandomVarsSave.addToSave(randomVar1);
            RandomVarsSave.addToSave(randomVar2);

            String addExpr1 = " " + randomVar1 + "&&line ";
            String addExpr2 = " " + randomVar2 + "&&line ";
            String te = addExpr1 + "," + addExpr2;
            addToSave(sava + "为" + te);
//            savedList.add(sava + "为" + te);

            stringBuilder.append(te);
            pos = matcher.end();
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }


    public static String dualPointMove1(String sorString) {
//        题号:HAIN200920L
//        if (sorString.contains("分别")) {
//            return sorString;
//        }
        String pointEntity = "(?!((是|为|)(点|) [^&\\s]+&&point))";

        String regexType1 = "(?<!(的|&&point (是|为)))" + LTools.createRegexOrByStringList(dualPointRegex) + pointEntity;

        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;
        Pattern pattern = Pattern.compile(regexType1);
        Matcher matcher = pattern.matcher(sorString);
        while (matcher.find()) {
            int start = matcher.start();
            stringBuilder.append(sorString.substring(pos, start));
            String sava = matcher.group();

            String randomVar1 = "Q_" + RandomVarsSave.count++;
            String randomVar2 = "Q_" + RandomVarsSave.count++;
            RandomVarsSave.addToSave(randomVar1);
            RandomVarsSave.addToSave(randomVar2);

            String addExpr1 = " " + randomVar1 + "&&point ";
            String addExpr2 = " " + randomVar2 + "&&point ";
            String te = addExpr1 + "," + addExpr2;
            addToSave(sava + "为" + te);
//            savedList.add(sava + "为" + te);

            stringBuilder.append(te);
            pos = matcher.end();
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }


    public static String dualExprMove1(String sorString) {

        if (sorString.contains("分别")) {
            return sorString;
        }

        String regexType1 = LTools.createRegexOrByStringList(dualExprRegex) + "(?=之|都|(还|)相(同|等)|的|比|互|均)";

        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;
        Pattern pattern = Pattern.compile(regexType1);
        Matcher matcher = pattern.matcher(sorString);
        while (matcher.find()) {
            int start = matcher.start();
            stringBuilder.append(sorString.substring(pos, start));
            String sava = matcher.group();

            String randomVar1 = "v_" + RandomVarsSave.count++;
            String randomVar2 = "v_" + RandomVarsSave.count++;
            RandomVarsSave.addToSave(randomVar1);
            RandomVarsSave.addToSave(randomVar2);

            String addExpr1 = " " + randomVar1 + "&&expr ";
            String addExpr2 = " " + randomVar2 + "&&expr ";
            String te = addExpr1 + "和" + addExpr2;
            addToSave(sava + "为" + te);
//            savedList.add(sava + "为" + te);

            stringBuilder.append(te);
            pos = matcher.end();
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }


    public static String functionMove1(String sorString) {
        String regexType1 = "(?<!(的|&&expr (是|为)))" + LTools.createRegexOrByStringList(functionRegex) + " [^&\\s]+&&expr ";
        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;
        Pattern pattern = Pattern.compile(regexType1);
        Matcher matcher = pattern.matcher(sorString);
        //如果整句匹配则不提取
        if (matcher.matches()) {
            return sorString;
        }
        matcher.reset();
        while (matcher.find()) {
            int start = matcher.start();
            stringBuilder.append(sorString.substring(pos, start));
            String sava = matcher.group();
            addToSave(sava);
            String a = getSingleExpr(sava);
            stringBuilder.append(StringUtils.isNotEmpty(a) ? a : sava);
            pos = matcher.end();
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }


    public static VarsSaveData checkRep(final String test) {
        if (savedDataList.size() > 0) {
            List<VarsSaveData> fiData = new ArrayList<>(Collections2.filter(savedDataList, new Predicate<VarsSaveData>() {
                @Override
                public boolean apply(VarsSaveData input) {
                    return input.getDescription().equals(test);
                }
            }));
            if (fiData.size() == 1) {
                return fiData.get(0);
            }
        }
        return null;
    }


    //....的焦点F交。。。。
    public static String segmentMove1(String sorString) {

        if (sorString.contains("分别")) {
            return sorString;
        }
        String pointEntity = " [^&\\s]+&&line ";
        String regexType1 = "(?<!(的|&&line (都|)(是|为)))" + LTools.createRegexOrByStringList(segmentRegex) + pointEntity;
        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;
        Pattern pattern = Pattern.compile(regexType1);
        Matcher matcher = pattern.matcher(sorString);

        //如果整句匹配则不提取
        if (matcher.matches()) {
            return sorString;
        }
        matcher.reset();

        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String sava = matcher.group();

            if (start == pos && end == sorString.length()) {
                addToSave(sava);
                return "";
            }
            stringBuilder.append(sorString.substring(pos, start));
            addToSave(sava);
//            savedList.add(sava);
            stringBuilder.append(getSingleLine(sava));
            pos = matcher.end();
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }


    //....的焦点F交。。。。
    public static String lineMove1(String sorString) {

        if (sorString.contains("分别")) {
            return sorString;
        }
        String regexType1 = "(?<!(的|&&line (是|为)))" + LTools.createRegexOrByStringList(lineRegex) + " [^&\\s]+&&line(\\s|) (: [^&\\s]+&&expr)?";
        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;
        Pattern pattern = Pattern.compile(regexType1);
        Matcher matcher = pattern.matcher(sorString);
        //如果整句匹配则不提取
        if (matcher.matches()) {
            return sorString;
        }
        matcher.reset();
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String sava = matcher.group();
            if (start == pos && end == sorString.length()) {
                addToSave(sava);
                return "";
            }
            stringBuilder.append(sorString.substring(pos, start));
            addToSave(sava);
            stringBuilder.append(getSingleLine(sava));
            pos = matcher.end();
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }

    public static String circleMove1(String sorString) {
        String regexType1 = "(?<!(的|&&circle ((:) [^&\\s]{1,30}&&expr |)(是|为)))" + LTools.createRegexOrByStringList(circleRegex) + " [^&\\s]+&&circle ";
        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;
        Pattern pattern = Pattern.compile(regexType1);
        Matcher matcher = pattern.matcher(sorString);
        //如果整句匹配则不提取
        if (matcher.matches()) {
            return sorString;
        }
        matcher.reset();
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String sava = matcher.group();

            if (start == pos && end == sorString.length()) {
                addToSave(sava);
//                savedList.add(sava);
                return "";
            }
            stringBuilder.append(sorString.substring(pos, start));
            addToSave(sava);
//            savedList.add(sava);
            stringBuilder.append(getSingleCircle(sava));
            pos = matcher.end();
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }

    public static String circleMove2(String sorString) {

        String regexType11 = "(?<!(的|&&circle ((:) [^&\\s]{1,30}&&expr |)(是|为)))" + LTools.createRegexOrByStringList(circleRegex) + " [^&\\s]+&&circle ";
        Pattern patternC = Pattern.compile(regexType11);
        Matcher matcherC = patternC.matcher(sorString);
        if (matcherC.matches()) {
            return sorString;
        }

        String regexType1 = "(?<!(的|&&circle ((:) [^&\\s]{1,30}&&expr |)(是|为)))" + LTools.createRegexOrByStringList(circleRegex) + "(?!((是|为) [^&\\s]+&&circle))";
        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;
        Pattern pattern = Pattern.compile(regexType1);
        Matcher matcher = pattern.matcher(sorString);
        while (matcher.find()) {
            int start = matcher.start();


            stringBuilder.append(sorString.substring(pos, start));
            String sava = matcher.group();
            String randomVar = "O_" + RandomVarsSave.count;
            RandomVarsSave.addToSave(randomVar);

            String namedPoint = " O_" + RandomVarsSave.count++ + "&&circle ";
            addToSave(sava + namedPoint);
//            savedList.add(sava + namedPoint);
            stringBuilder.append("圆").append(namedPoint);
            pos = matcher.end();
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }


    public static String angleMove1(String sorString) {
        String pointEntity = " [^&\\s]+&&angle ";
        String regexType1 = "(?<!(的|&&angle (是|为)))" + LTools.createRegexOrByStringList(angleRegex) + pointEntity;
        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;
        Pattern pattern = Pattern.compile(regexType1);
        Matcher matcher = pattern.matcher(sorString);
        //如果整句匹配则不提取
        if (matcher.matches()) {
            return sorString;
        }
        matcher.reset();
        while (matcher.find()) {
            int start = matcher.start();
            stringBuilder.append(sorString.substring(pos, start));
            String sava = matcher.group();
            addToSave(sava);
            stringBuilder.append(getSingleAngle(sava));
            pos = matcher.end();
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }

    public static String triangleMove1(String sorString) {
        String pointEntity = " [^&\\s]+&&triangle ";
        String regexType1 = "(?<!(的|&&triangle (是|为)))" + LTools.createRegexOrByStringList(triangleRegex) + pointEntity;
        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;
        Pattern pattern = Pattern.compile(regexType1);
        Matcher matcher = pattern.matcher(sorString);

        //如果整句匹配则不提取
        if (matcher.matches()) {
            return sorString;
        }
        matcher.reset();

        while (matcher.find()) {
            int start = matcher.start();
            stringBuilder.append(sorString.substring(pos, start));
            String sava = matcher.group();
//            savedList.add(sava);
            addToSave(sava);
            stringBuilder.append(getSingleTriangle(sava));
            pos = matcher.end();
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }

    public static String segmentMove2(String sorString) {

        if (sorString.contains("分别")) {
            return sorString;
        }

        String regexType11 = "(?<!(的|&&line (都|)(是|为)))" + LTools.createRegexOrByStringList(segmentRegex) + " [^&\\s]+&&line ";
        Pattern patternC = Pattern.compile(regexType11);
        Matcher matcherC = patternC.matcher(sorString);
        if (matcherC.matches()) {
            return sorString;
        }

        String regexType1 = "(?<!(的|&&line (都|)(是|为)))" + LTools.createRegexOrByStringList(segmentRegex) + "(?!((是|为) [^&\\s]+&&(line|expr)|方程))";
        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;
        Pattern pattern = Pattern.compile(regexType1);
        Matcher matcher = pattern.matcher(sorString);
        while (matcher.find()) {
            int start = matcher.start();
            stringBuilder.append(sorString.substring(pos, start));
            String sava = matcher.group();

            String randomVar = "M_" + RandomVarsSave.count + "N_" + RandomVarsSave.count;
            RandomVarsSave.addToSave(randomVar);

            String namedPoint = " " + randomVar + "&&line ";
            RandomVarsSave.count++;
            addToSave(sava + namedPoint);
            stringBuilder.append(namedPoint);
            pos = matcher.end();
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }

    //焦点。。。。
    public static String pointMove2(String sorString) {
        if (sorString.contains("分别")) {
            return sorString;
        }

        String regexType11 = "(?<!(的|&&point (是|为)))" + LTools.createRegexOrByStringList(pointRegex) + " [^&\\s]+&&point ";
        Pattern patternC = Pattern.compile(regexType11);
        Matcher matcherC = patternC.matcher(sorString);
        if (matcherC.matches()) {
            return sorString;
        }

        String regexType1 = "(?<!(的|&&point (点|)(是|为)))" + LTools.createRegexOrByStringList(pointRegex) + "(?!((是|为)(点|) [^&\\s]+&&point))";
        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;
        Pattern pattern = Pattern.compile(regexType1);
        Matcher matcher = pattern.matcher(sorString);
        //如果整句匹配则不提取
        if (matcher.matches()) {
            return sorString;
        }
        matcher.reset();
        while (matcher.find()) {
            int start = matcher.start();
            stringBuilder.append(sorString.substring(pos, start));
            String sava = matcher.group();
            VarsSaveData varsSaveData = checkRep(sava);
            String randomVar;
            String namedPoint;
            if (varsSaveData != null) {
                randomVar = varsSaveData.getName();
                namedPoint = " " + randomVar + "&&point ";
            } else {
                randomVar = "Q_" + RandomVarsSave.count;
                RandomVarsSave.addToSave(randomVar);
                namedPoint = " Q_" + RandomVarsSave.count++ + "&&point ";
                String total = sava + namedPoint;
                savedDataList.add(new VarsSaveData(randomVar, sava, total));
                addToSave(total);
            }

            stringBuilder.append(namedPoint);
            pos = matcher.end();
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }


    public static String angleMove2(String sorString) {

        String regexType11 = "(?<!(的|&&angle (是|为)))" + LTools.createRegexOrByStringList(angleRegex) + " [^&\\s]+&&angle ";
        Pattern patternC = Pattern.compile(regexType11);
        Matcher matcherC = patternC.matcher(sorString);
        if (matcherC.matches()) {
            return sorString;
        }

        String regexType1 = "(?<!(的|&&angle (是|为)))" + LTools.createRegexOrByStringList(angleRegex) + "(?!((是|为) [^&\\s]+&&(angle|expr) ))";
        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;
        Pattern pattern = Pattern.compile(regexType1);
        Matcher matcher = pattern.matcher(sorString);
        while (matcher.find()) {
            int start = matcher.start();
            stringBuilder.append(sorString.substring(pos, start));
            String sava = matcher.group();

            String namedPoint = " A_" + RandomVarsSave.count + "B_" + RandomVarsSave.count + "C_" + RandomVarsSave.count++ + "&&angle ";
            addToSave(sava + namedPoint);
            stringBuilder.append("角").append(namedPoint);
            pos = matcher.end();
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }

    public static String triangleMove2(String sorString) {

        String pointEntity1 = " [^&\\s]+&&triangle ";
        String regexType11 = "(?<!(的|&&angle (是|为)))" + LTools.createRegexOrByStringList(triangleRegex) + pointEntity1;
        Pattern patternC = Pattern.compile(regexType11);
        Matcher matcherC = patternC.matcher(sorString);
        if (matcherC.matches()) {
            return sorString;
        }

        String pointEntity = "(?!((是|为) [^&\\s]+&&(triangle) ))";
        String regexType1 = "(?<!(的|&&triangle (是|为)))" + LTools.createRegexOrByStringList(triangleRegex) + pointEntity;
        StringBuilder stringBuilder = new StringBuilder();
        int pos = 0;
        Pattern pattern = Pattern.compile(regexType1);
        Matcher matcher = pattern.matcher(sorString);
        while (matcher.find()) {
            int start = matcher.start();
            stringBuilder.append(sorString.substring(pos, start));
            String sava = matcher.group();

            String namedPoint = " A_" + RandomVarsSave.count + "B_" + RandomVarsSave.count + "C_" + RandomVarsSave.count++ + "&&triangle ";
//            savedList.add(sava + namedPoint);
            addToSave(sava + namedPoint);
            stringBuilder.append("三角形").append(namedPoint);
            pos = matcher.end();
        }
        stringBuilder.append(sorString.substring(pos, sorString.length()));
        return stringBuilder.toString();
    }


    public static String getSingleExpr(String sorString) {
        String pointEntity = "(?<=(反|导)函数|离心率|斜率|周长|面积|高|体积|底面积|表面积|侧面积|底面周长|长度|距离|间距|截距|弦长|存在一个元素|长|最小值|最大值|极大值|极小值|(横|纵)坐标|项和|系数|夹角) [^&\\s]+&&expr ";
        Pattern pattern = Pattern.compile(pointEntity);
        Matcher matcher = pattern.matcher(sorString);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }


    public static String getSinglePoint(String sorString) {
        String pointEntity = " [^&\\s]+&&point ";
        Pattern pattern = Pattern.compile(pointEntity);
        Matcher matcher = pattern.matcher(sorString);
        List<String> points = new ArrayList<>();
        while (matcher.find()) {
            points.add(matcher.group());
        }
        if (points.size() > 0) {
            return points.get(points.size() - 1);
        }
        return "";
    }

    public static String getSingleLine(String sorString) {
        String pointEntity = "(?<!((双|)曲线|抛物线|椭圆))(轨迹|直线|) [^&\\s]+&&line (: [^&\\s]+&&expr)?";
        Pattern pattern = Pattern.compile(pointEntity);
        Matcher matcher = pattern.matcher(sorString);
        List<String> lines = new ArrayList<>();
        while (matcher.find()) {
            lines.add(matcher.group());
        }
        if (lines.size() > 0) {
            return lines.get(lines.size() - 1);
        }
        return "";
    }

    public static String getSingleCircle(String sorString) {
        String pointEntity = "(?<!(椭))圆 [^&\\s]+&&circle ";
        Pattern pattern = Pattern.compile(pointEntity);
        Matcher matcher = pattern.matcher(sorString);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

    public static String getSingleAngle(String sorString) {
        String pointEntity = " [^&\\s]+&&angle ";
        Pattern pattern = Pattern.compile(pointEntity);
        Matcher matcher = pattern.matcher(sorString);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

    public static String getSingleTriangle(String sorString) {
        String pointEntity = " [^&\\s]+&&triangle ";
        Pattern pattern = Pattern.compile(pointEntity);
        Matcher matcher = pattern.matcher(sorString);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }


}
