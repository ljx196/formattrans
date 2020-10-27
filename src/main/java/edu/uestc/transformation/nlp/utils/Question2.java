package edu.uestc.transformation.nlp.utils;

import java.io.Serializable;
import java.util.List;


/**
 * @author cyq7on
 * @description 重构了接口协议，因而重构Question模型
 * @create 2019/11/29
 * @see <a href="http://121.48.165.44:4000/mathnlp/transmission-format.html?tdsourcetag=s_pctim_aiomsg"/>
 **/
public class Question2 implements Serializable {

    /**
     * type : 0
     * questionId : unique id
     * commonText : all sentence
     * fakeText : sentence with introduced entities
     * entities : [{"name":"A","types":["Point"]},{"name":"ABC","types":["Triangle"]}]
     * stemRelations : [{"entity1":"e1","entity2":"e2","type1":"t1","type2":"t2","relationString":"relation","properties":[{"propertyName":"P","propertyType":"Point"},{"propertyName":"AB","propertyType":"Line"}],"isConclusion":0}]
     * subStemRelations : [{"isQuestion":"question type","relations":[{"entity1":"e1","entity2":"e2","type1":"t1","type2":"t2","relationString":"relation","properties":[{"propertyName":"P","propertyType":"Point"},{"propertyName":"AB","propertyType":"Line"}],"asking":0}]},{"isQuestion":"question type","relations":[{"entity1":"e1","entity2":"e2","type1":"t1","type2":"t2","relationString":"relation","properties":[{"propertyName":"P","propertyType":"Point"},{"propertyName":"AB","propertyType":"Line"}],"asking":0}]}]
     * options : ["choiceA","choiceB","choiceC","choiceD"]
     * solutionRelations : [[{"entity1":"e1","entity2":"e2","type1":"t1","type2":"t2","relationString":"relation","properties":[{"propertyName":"P","propertyType":"Point"}],"isConclusion":0}],[{"entity1":"e1","entity2":"e2","type1":"t1","type2":"t2","relationString":"relation","properties":[{"propertyName":"P","propertyType":"Point"}],"isConclusion":0}]]
     */

    private int type;
    private String questionId;
    private String commonText;
    private String fakeText;
    private List<EntityBean> entities;
    private List<StemRelationBean> stemRelations;
    private List<SubStemRelationBean> subStemRelations;
    private List<SubStemRelationBean> optionRelations;
    private List<String> options;

    //实例化相关
    //实例化的优先级，数字越大优先级越高，定义0-1000
    private int instantiatedLevel;
    //实例化的类别数，公用0，函数1，向量2，数列3，解析几何4，复数5，平面几何6，立体几何7
    private int instantiatedCategory;
    //实例化的简短描述，如函数求导，等差数列前n项和
    private String instantiatedDescription;

    public int getInstantiatedLevel() {
        return instantiatedLevel;
    }

    public void setInstantiatedLevel(int instantiatedLevel) {
        this.instantiatedLevel = instantiatedLevel;
    }

    public int getInstantiatedCategory() {
        return instantiatedCategory;
    }

    public void setInstantiatedCategory(int instantiatedCategory) {
        this.instantiatedCategory = instantiatedCategory;
    }

    public String getInstantiatedDescription() {
        return instantiatedDescription;
    }

    public void setInstantiatedDescription(String instantiatedDescription) {
        this.instantiatedDescription = instantiatedDescription;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getCommonText() {
        return commonText;
    }

    public void setCommonText(String commonText) {
        this.commonText = commonText;
    }

    public String getFakeText() {
        return fakeText;
    }

    public void setFakeText(String fakeText) {
        this.fakeText = fakeText;
    }

    public List<EntityBean> getEntities() {
        return entities;
    }

    public void setEntities(List<EntityBean> entities) {
        this.entities = entities;
    }

    public List<StemRelationBean> getStemRelations() {
        return stemRelations;
    }

    public void setStemRelations(List<StemRelationBean> stemRelations) {
        this.stemRelations = stemRelations;
    }

    public List<SubStemRelationBean> getSubStemRelations() {
        return subStemRelations;
    }

    public void setSubStemRelations(List<SubStemRelationBean> subStemRelations) {
        this.subStemRelations = subStemRelations;
    }

    public List<SubStemRelationBean> getOptionRelations() {
        return optionRelations;
    }

    public void setOptionRelations(List<SubStemRelationBean> optionRelations) {
        this.optionRelations = optionRelations;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }


    public static class EntityBean {
        /**
         * name : A
         * types : ["Point"]
         */

        private String name;
        private List<String> types;

        public String toString () {
            String type = "";
            for (String s : types) {
                type = type + "  " + s;
            }
            return name + "【" + type + "】";
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public EntityBean(String name, List<String> types) {
            this.name = name;
            this.types = types;
        }

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }
    }

    public static class StemRelationBean {
        /**
         * entity1 : e1
         * entity2 : e2
         * type1 : t1
         * type2 : t2
         * relationString : relation
         * properties : [{"propertyName":"P","propertyType":"Point"},{"propertyName":"AB","propertyType":"Line"}]
         * isConclusion : 0
         */

        private String entity1;
        private String entity2;
        private String type1;
        private String type2;
        private String relationString;
        private int isConclusion;
        private List<PropertyBean> properties;

        public String toString () {
            return entity1 + "【" + type1 + "】" + "-[" + relationString + "]->"
                    + entity2 + "【" + type2 + "】";
        }

        public StemRelationBean(String entity1, String entity2, String type1, String type2, String relationString, int isConclusion, List<PropertyBean> properties) {
            this.entity1 = entity1;
            this.entity2 = entity2;
            this.type1 = type1;
            this.type2 = type2;
            this.relationString = relationString;
            this.isConclusion = isConclusion;
            this.properties = properties;
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

        public String getRelationString() {
            return relationString;
        }

        public void setRelationString(String relationString) {
            this.relationString = relationString;
        }

        public int getIsConclusion() {
            return isConclusion;
        }

        public void setIsConclusion(int isConclusion) {
            this.isConclusion = isConclusion;
        }

        public List<PropertyBean> getProperties() {
            return properties;
        }

        public void setProperties(List<PropertyBean> properties) {
            this.properties = properties;
        }

    }

    public static class SubStemRelationBean {
        /**
         * isQuestion : question type
         * relations : [{"entity1":"e1","entity2":"e2","type1":"t1","type2":"t2","relationString":"relation","properties":[{"propertyName":"P","propertyType":"Point"},{"propertyName":"AB","propertyType":"Line"}],"asking":0}]
         */

        private int isQuestion;
        private List<RelationBean> relations;

        public String toString () {
            String res = "";
            for (RelationBean relationBean : relations) {
                res = res + "  " + relationBean.toString();
            }
            return res;
        }

        public int getIsQuestion() {
            return isQuestion;
        }

        public void setIsQuestion(int isQuestion) {
            this.isQuestion = isQuestion;
        }

        public List<RelationBean> getRelations() {
            return relations;
        }

        public void setRelations(List<RelationBean> relations) {
            this.relations = relations;
        }

        public static class RelationBean {
            /**
             * entity1 : e1
             * entity2 : e2
             * type1 : t1
             * type2 : t2
             * relationString : relation
             * properties : [{"propertyName":"P","propertyType":"Point"},{"propertyName":"AB","propertyType":"Line"}]
             * asking : 0
             */

            private String entity1;
            private String entity2;
            private String type1;
            private String type2;
            private String relationString;
            private int asking;
            private int isConclusion;
            private List<PropertyBean> properties;

            public String toString () {
                return entity1 + "【" + type1 + "】" + "-[" + relationString + "]->"
                        + entity2 + "【" + type2 + "】";
            }

            public RelationBean(String entity1, String entity2, String type1, String type2, String relationString, int asking, int isConclusion, List<PropertyBean> properties){
                this.entity1=entity1;
                this.entity2=entity2;
                this.type1=type1;
                this.type2=type2;
                this.relationString=relationString;
                this.asking=asking;
                this.isConclusion=isConclusion;
                this.properties=properties;
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

            public String getRelationString() {
                return relationString;
            }

            public void setRelationString(String relationString) {
                this.relationString = relationString;
            }

            public int getAsking() {
                return asking;
            }

            public void setAsking(int asking) {
                this.asking = asking;
            }

            public int getIsConclusion() {
                return isConclusion;
            }

            public void setIsConclusion(int isConclusion) {
                this.isConclusion = isConclusion;
            }

            public List<PropertyBean> getProperties() {
                return properties;
            }

            public void setProperties(List<PropertyBean> properties) {
                this.properties = properties;
            }

        }

    }

    public static class PropertyBean {
        /**
         * propertyName : P
         * propertyType : Point
         */

        private String propertyName;
        private String propertyType;

        public String getPropertyName() {
            return propertyName;
        }

        public void setPropertyName(String propertyName) {
            this.propertyName = propertyName;
        }

        public String getPropertyType() {
            return propertyType;
        }

        public void setPropertyType(String propertyType) {
            this.propertyType = propertyType;
        }
    }
public void addStemRelationBean(String entity1, String entity2, String type1, String type2, String relationString, int isConclusion, List<PropertyBean> properties){
    StemRelationBean stemRelationBean = new StemRelationBean(entity1, entity2, type1, type2, relationString, isConclusion, properties);
    stemRelations.add(stemRelationBean);
}

    @Override
    public String toString() {
        return "Question2{" +
                "type=" + type +
                ", questionId='" + questionId + '\'' +
                ", commonText='" + commonText + '\'' +
                ", fakeText='" + fakeText + '\'' +
                ", entities=" + entities +
                ", stemRelations=" + stemRelations +
                ", subStemRelations=" + subStemRelations +
                ", optionRelations=" + optionRelations +
                ", options=" + options +
                '}';
    }
}
