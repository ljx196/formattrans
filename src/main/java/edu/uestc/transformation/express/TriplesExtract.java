package edu.uestc.transformation.express;

import edu.uestc.transformation.nlp.relation.RelationExtract;
import edu.uestc.transformation.nlp.utils.LatexConvert;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.List;

/**
 * @author: tjc
 * @create: 2020/10/21
 * @description:
 */

public class TriplesExtract {
    public ParseMapper parseMapper;
    public static void main(String[] args) throws Exception{
        RelationExtract re = new RelationExtract();
        ExpressionTransformer et = new ExpressionTransformer();
        LatexConvert latex = new LatexConvert();
        String resource ="sqlMapConfig.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        et.parseMapper = sqlSession.getMapper(ParseMapper.class);
        int limit=100;
        int offset=180000;
        int count=0;
        int total=20000;
        while(count<total){
            List<Parse> parseList = et.parseMapper.queryTitleByLimitAndOffset(limit,offset);
            for(Parse parse:parseList) {
                String text = latex.getMapleString(parse.getTitle(), "1");
                String triples = re.triples(text);
                parse.setJson(re.getJson());
                parse.setTriples(triples);
                et.parseMapper.updateTriples(parse);
            }
            System.out.printf("limit: "+limit+"offset: "+offset);
            offset = offset+limit;
            count += limit;
        }

    }
}
