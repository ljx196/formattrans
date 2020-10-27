package edu.uestc.transformation.nlp.relation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author: yyWang
 * @create: 2019/9/18
 * @description: 通过 Ltp前端传送过来的短句进行分配的关系
 */

public class RelationLtp {
    // 关系中对应的实体
    private Entity entity_1;
    private Entity entity_2;
    // 三元组中要求的内容
    private int asking;

    private int isConclusion;

    private String entity1;
    private String entity2;
    private String type1;
    private String type2;

    // 实体之间的关系
    private String relationString;
    // 实体之间关系的属性
    private List<Property> properties;

    public RelationLtp(Entity entity_1, Entity entity_2, String relationString) {
        this.entity_1 = entity_1;
        this.entity_2 = entity_2;
        this.relationString = relationString;
        properties = new ArrayList<>();
    }

    // 根据字段生成实体对象
    public void generateEntity() {
        entity_1 = new Entity();
        entity_1.setName(entity1);
        List<String> type = new ArrayList<>();
        type.add(type1);
        entity_1.setTypes(type);

        List<String> type1 = new ArrayList<>();
        entity_2 = new Entity();
        entity_2.setName(entity2);
        type1.add(type2);
        entity_2.setTypes(type1);
    }

    public Entity getEntity_1() {
        return entity_1;
    }

    public int getIsConclusion() {
        return isConclusion;
    }

    public void setIsConclusion(int isConclusion) {
        this.isConclusion = isConclusion;
    }

    public int getAsking() {
        return asking;
    }

    public void setAsking(int asking) {
        this.asking = asking;
    }

    public void setEntity_1(Entity entity_1) {
        this.entity_1 = entity_1;
    }

    public Entity getEntity_2() {
        return entity_2;
    }

    public void setEntity_2(Entity entity_2) {
        this.entity_2 = entity_2;
    }

    public String getEntity1() {
        return entity1;
    }

    public void setEntity1(String entity1) {
        this.entity1 = entity1;
    }

    public String getEntity2() {
        return entity2;
    }

    public void setEntity2(String entity2) {
        this.entity2 = entity2;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    /**
     * 输出实体之间的关系
     *
     * @return 返回关系字符串
     */
    public String showTriple(){
        return "("+entity_1.getTypeIndex()+" "+entity_2.getTypeIndex()+" "+relationString+")";
    }
    public String showRelations() {
        StringBuilder message = new StringBuilder(entity_1.showEntity() + entity_2.showEntity());
        // 输出关系的属性
        if (properties.size() > 0) {
            for (Property prop : properties) {
                message.append(prop.getPropertyName()).append("[").append(prop.getPropertyType()).append("]\t");
            }

        }
        message.append("关系是：").append(relationString);
        message.append("\t\tAsking内容： ").append(asking);
        return message.toString();
    }

     /**
     * 输出解答过程中实体之间的关系
     *
     * @return 返回关系字符串
     */

    public String showSolutions() {
        return showRelations().replaceAll("Asking内容：.*$","") + "isConclusion： " + isConclusion;
    }

    public RelationLtp() {
        properties = new ArrayList<>();
    }

    public String getRelationString() {
        return relationString;
    }

    public void setRelationString(String relationString) {
        this.relationString = relationString;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RelationLtp that = (RelationLtp) o;
        return asking == that.asking &&
                entity1.equals(that.entity1) &&
                entity2.equals(that.entity2) &&
                type1.equals(that.type1) &&
                type2.equals(that.type2) &&
                relationString.equals(that.relationString) &&
                Objects.equals(properties, that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(asking, entity1, entity2, type1, type2, relationString, properties);
    }
}
