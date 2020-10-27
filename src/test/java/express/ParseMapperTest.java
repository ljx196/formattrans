package express;

import edu.uestc.transformation.express.Parse;
import edu.uestc.transformation.express.ParseMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

/**
 * @author: tjc
 * @create: 2020/10/20
 * @description:
 */

public class ParseMapperTest {
    public ParseMapper parseMapper;
    @Before
    public void setUp() throws Exception{
        String resource ="sqlMapConfig.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        this.parseMapper = sqlSession.getMapper(ParseMapper.class);
    }

    @Test
     public void testInsrtLabel(){
         List<Parse> parseList = this.parseMapper.queryParseByLimitAndOffset(10,0);
         for(Parse parse:parseList){
             String par = parse.getParse();
             par=par.replaceAll("\r","");
             parse.setParse(par);
             System.out.println(parse);
         }
     }
}
