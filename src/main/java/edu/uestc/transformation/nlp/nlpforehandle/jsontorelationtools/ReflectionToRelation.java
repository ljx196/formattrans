/*
package edu.uestc.transformation.nlp.nlpforehandle.jsontorelationtools;

import com.google.common.collect.Lists;
import com.tsinghuabigdata.common.logging.LogFactory;

import org.apache.logging.log4j.Logger;

import java.util.List;

*/
/**
 * 通过Json中的code反射到jsontorelation中的函数，转关系
 * Created by sxx on 2015/8/12.
 *//*

public class ReflectionToRelation {
    public static final Logger LOG = LogFactory.getLogger(ReflectionToRelation.class);

    */
/**
     * 题干的json转irelation方法
     *//*

    public static List<IRelation> findQuestionRelation(List<JsonInfo> jsonInfoList, GlobalInformation globalInformation) {
        List<IRelation> relations = Lists.newArrayList();
        for (JsonInfo jsonInfo : jsonInfoList) {
            IRelation relation = transRelationFromJsonInfo(jsonInfo, globalInformation);
            if (relation != null) {
                relations.add(relation);
            }
        }
        return relations;
    }

    */
/**
     * 单个JsonInfo转关系的方法
     *//*

    public static IRelation transRelationFromJsonInfo(JsonInfo jsonInfo, GlobalInformation globalInformation) {
        IRelation relation = null;
        String code = Constant.JSON_TO_RELATION_PACKAGE
                + jsonInfo.getJsonObject().get("code").toString().replace("\"", "");
        try {
            Class clazz = Class.forName(code);
            TransitionRelation transitionRelation = (TransitionRelation) clazz.newInstance();
            relation = transitionRelation.json2Relation(jsonInfo, globalInformation);
        } catch (Exception e) {
            LOG.info("fail to find question relation, due to " + e.getMessage());
        }
        return relation;
    }


    public static List<IRelation> findChoiceItemRelation(List<JsonInfo> jsonInfoList, GlobalInformation globalInformation) {
        List<IRelation> relations = Lists.newArrayList();

        for (int flag = 0; flag < jsonInfoList.size(); flag++) {
            JsonInfo jsonInfo = jsonInfoList.get(flag);
            IRelation relation;
            String code = Constant.JSON_TO_RELATION_PACKAGE
                    + jsonInfo.getJsonObject().get("code").toString().replace("\"", "");
            try {
                Class clazz = Class.forName(code);
                TransitionRelation transitionRelation = (TransitionRelation) clazz.newInstance();
                relation = transitionRelation.json2ConclusionProveRelation(jsonInfo, globalInformation);
                relations.add(relation);
            } catch (Exception e) {
                LOG.warn(e);
                return relations;
            }
        }
        return relations;
    }

}


*/
