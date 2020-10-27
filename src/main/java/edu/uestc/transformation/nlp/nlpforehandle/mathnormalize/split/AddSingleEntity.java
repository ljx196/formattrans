package edu.uestc.transformation.nlp.nlpforehandle.mathnormalize.split;


import edu.uestc.transformation.nlp.nlpforehandle.regularexpressiontools.LTools;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 为缺失变量的句子引入实体（只引入一次实体，后面再次出现则去指代）
 * Created by Administrator on 2018/5/8.
 */
public class AddSingleEntity {
    /**
     * 引入三角形实体
     *
     * @param text
     * @return
     */
    private static String addTriangle(String text, Set<String> entity) {
        if (text.contains("三角形") && !entity.contains("triangle")) {
            text = text.replaceFirst("三角形", "三角形 APQ&&triangle ");
        }
        return text;
    }

    /**
     * 引入四边形
     *
     * @param text
     * @return
     */
    private static String addQuadrangle(String text, Set<String> entity) {
        String quadrangleRegex = "(四边形|正方形|菱形|梯形|矩形)";
        Pattern pattern = Pattern.compile(quadrangleRegex);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            if (!entity.contains("quadrangle") && StringUtils.countMatches(text, "&&point") != 4) {
                text = text.replaceFirst(quadrangleRegex, matcher.group() + " MNPQ&&quadrangle ");
            }
            break;  //只添加一次
        }
        return text;
    }



    public static String[] regex = {
            "(上|下|)(平面|底面|侧面|面)( [^&\\s]+&&plane |)((与|和)(上|下|)(平面|底面|侧面|面)( [^&\\s]+&&plane |)(都|均|)|)(是|为)(边长(为|等于) [^&+]+&&expr 的|)(平行四边形|菱形|矩形|长方形|三角形|正方形|正三角形|等边三角形|等腰三角形|直角三角形|梯形|直角梯形|等腰梯形)",
            "(已知|)(((正|长)方体)|((直|正|斜|平行|)((三|四|五|六|)棱(柱|锥|台)|四面体|五面体|六面体))|多面体) [^&+]+&&polyhedron 的(上|下|)(底面|侧面)( [^&\\s]+&&plane |)(是|为)(边长(为|等于) [^&+]+&&expr 的|)(平行四边形|菱形|矩形|长方形|三角形|正方形|正三角形|等边三角形|等腰三角形|直角三角形|梯形|直角梯形|等腰梯形)",
            "(在|)(上|下|)(平面|底面|侧面|面)( [^&\\s]+&&plane |)((与|和)(上|下|)(平面|底面|侧面|面)( [^&\\s]+&&plane |)(都|均|)|)(是|为)(边长(为|等于) [^&+]+&&expr 的|)(平行四边形|菱形|矩形|长方形|三角形|正方形|正三角形|等边三角形|等腰三角形|直角三角形|梯形|直角梯形|等腰梯形)的(((正|长)方体)|((直|正|斜|)((三|四|五|六|)棱(柱|锥|台)|四面体|五面体))|多面体) [^&+]+&&polyhedron (中|)",
            "(上|下|)(平面|底面|侧面|面)( [^&\\s]+&&plane |)(是|为)以 [^&\\s]+&&point 为中心的(平行四边形|菱形|矩形|长方形|三角形|正方形|正三角形|等边三角形|等腰三角形|直角三角形|梯形|直角梯形|等腰梯形)",
    };

    public static boolean isPolyhedron(String str) {
        String regexType = LTools.createRegexOrByStringList(regex);
        Pattern pattern = Pattern.compile(regexType);
        Matcher matcher = pattern.matcher(str);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }


}
