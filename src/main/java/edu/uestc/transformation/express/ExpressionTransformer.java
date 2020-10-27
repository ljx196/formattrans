package edu.uestc.transformation.express;

import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import edu.uestc.transformation.nlp.nlpforehandle.Latex.LatexPattern;
import edu.uestc.transformation.nlp.utils.LatexConvert;
import edu.uestc.transformation.nlp.utils.NlpString2;
import edu.uestc.transformation.nlp.utils.PostParam;
import edu.uestc.transformation.nlp.utils.Question2;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import com.google.gson.Gson;
import java.awt.*;
import java.io.InputStream;
import java.util.List;
/**
 * @author: tjc
 * @create: 2020/10/20
 * @description:
 */

public class ExpressionTransformer {
    public ParseMapper parseMapper;
    public static List<Question2.EntityBean> getEntitys(String text){
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String stem=text;
        List<String> subStems = Lists.newArrayList("");
        List<String> options = Lists.newArrayList("");
        int instantiatedLevel = 1;
        // 实例化必填，类别数，公用0，函数1，向量2，数列3，解析几何4，复数5，平面几何6，立体几何7
        String instantiatedCategory = "4";
        // 实例化必填，简单描述该实例化实现的功能
        String instantiatedDescription = "直线截圆弦长";
        List<String> solutions = Lists.newArrayList("");
        NlpString2 nlp = new NlpString2();
        PostParam postParam = new PostParam(new PostParam.Content("0", stem, subStems, options, solutions,
                instantiatedLevel, instantiatedCategory, instantiatedDescription));

        String json=nlp.getNlpJson(postParam);
        System.out.println(nlp.toPrettyFormat(json));
        Question2 question = gson.fromJson(json, Question2.class);
        return question.getEntities();
    }
    public static void main(String[] args) throws Exception {
        ExpressionTransformer et = new ExpressionTransformer();
        LatexConvert latex = new LatexConvert();
        String resource = "sqlMapConfig.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        et.parseMapper = sqlSession.getMapper(ParseMapper.class);
        int limit = 10;
        int offset = 0;
        int count = 0;
        int total = 10;
        while (count < total) {
            System.out.printf("\nlimit: " + limit + "offset: " + offset + "\n");
            List<Parse> parseList = et.parseMapper.queryParseByLimitAndOffset(limit, offset);
            int n = 0;
            for (Parse parse : parseList) {
                try {
                    n++;
                    if (n % 40 == 0) {
                        System.out.printf("\n");
                    }
                    System.out.printf(n + "\t");
                    String text = parse.getTitle()+";"+parse.getAnswer2() + ";" + parse.getParse();
                    System.out.println(text);
                    text = latex.getMapleString(text, "1");

                    System.out.println(text);
                    List<Question2.EntityBean> entitys = getEntitys(text);

                    for(Question2.EntityBean en:entitys){
                        System.out.println(en);
                    }
                    //
                    //parse.setParse(text);
                   // et.parseMapper.updateParse(parse);
                } catch (Exception E) {
                    E.printStackTrace();
                }
            }
            n = 0;
            //System.out.printf("limit: "+limit+"offset: "+offset+"\n");
            offset = offset + limit;
            count += limit;

        }

    }
}
