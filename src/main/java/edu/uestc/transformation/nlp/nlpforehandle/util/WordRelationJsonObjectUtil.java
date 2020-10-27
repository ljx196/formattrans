package edu.uestc.transformation.nlp.nlpforehandle.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.uestc.transformation.nlp.nlpforehandle.Latex.LogFactory;
import org.apache.logging.log4j.Logger;

/**
 * 文字关系的JsonObject工具类
 * Created by renz on 2015/11/18.
 */
public class WordRelationJsonObjectUtil {
    private static final Logger logger = LogFactory.getLogger(WordRelationJsonObjectUtil.class);

    /**
     * 通过一个文字转成文字关系需要的JsonObject
     *
     * @param content 文字关系的内容
     * @return 文字关系需要的JsonObject
     */
    public static JsonObject getWordRelationJsonObject(String content) {
        String dealContent = content.replaceAll("\"", "\'").replaceAll("\\\\", "").replaceAll(" ", "");
        String str = "{\"" + Constant.JSON_CODE + "\":\"JsonWordRelation\",\"" + Constant.DATA + "\":{\""
                + Constant.SENTENCE + "\":\"" + dealContent + "\"}}";
        return new JsonParser().parse(str).getAsJsonObject();
    }
}
