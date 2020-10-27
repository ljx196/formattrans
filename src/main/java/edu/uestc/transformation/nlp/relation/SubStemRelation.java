package edu.uestc.transformation.nlp.relation;

import java.util.List;

/**
 * @author: yyWang
 * @create: 2019/12/16
 * @description: 题干小问关系提取
 */

public class SubStemRelation {
    // 小问包含的关系
    private List<RelationLtp> relations;

    public List<RelationLtp> getRelations() {
        return relations;
    }

    public void setRelations(List<RelationLtp> relations) {
        this.relations = relations;
    }
}
