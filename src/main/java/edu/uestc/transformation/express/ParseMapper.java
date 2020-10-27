package edu.uestc.transformation.express;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: tjc
 * @create: 2020/10/20
 * @description:
 */

public interface ParseMapper {
    public List<Parse>queryParseByLimitAndOffset(@Param("limit") int limit, @Param("offset") int  offset);

    public void updateParse(Parse parse);
    public void updateTriples(Parse parse);
    public List<Parse>queryTitleByLimitAndOffset(@Param("limit") int limit,@Param("offset")int offset);
}
