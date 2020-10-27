package edu.uestc.transformation.nlp.relation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: yyWang
 * @create: 2019/9/14
 * @description: 数学实体
 */

public class Entity implements Cloneable {
    // 实体名
    private String name;
    // 实体类型
    private List<String> types;//如果实体在多个短句中都有出现，那么一个实体可能会出现多个类型
    // 实体替换成对应的类型加上下标
    private String replace_of_num;

    // 是否使用实体类型下标
    //如果一个实体有多个类型，那么会按照实体出现的顺序，依次加入到types中，因此我们可以使用isUseIndex和类型做个一一对应
    private int isUseIndex = 0;

    // 该实体在短句中出现的位置
    public int index = -1;

    // 短句中包含的实体，实体内容是 oe1 这种的
    public String originEntity;

    public Entity(String entity, List<String> typeName) {
        this.name = entity;
        this.types = typeName;
    }

    public Entity() {
    }

    public int getIsUseIndex() {
        return isUseIndex;
    }

    public void setIsUseIndex(int isUseIndex) {
        this.isUseIndex = isUseIndex;
    }

    public String showEntity() {
        return getName() + "【" + getTypeIndex() + "】\t\t";
    }

    public String getName() {
        return name;
    }

    public String replaceName() {
        return "$" + getName() + "$";
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTypes() {
        return types;
    }

    public String getTypeIndex() {
        return getTypes().get(0);
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public String getReplace_of_num() {
        return replace_of_num;
    }

    public void setReplace_of_num(String replace_of_num) {
        this.replace_of_num = replace_of_num;
    }

    public String priReplaced() {
        return getTypeIndex() + replaceName();
    }

    public void modifyTypeName(String type) {
        this.getTypes().set(0, type);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Entity)) {
            return false;
        }

        Entity entity = (Entity) o;

        return getTypeIndex().equals(entity.getTypeIndex());
    }

    @Override
    public Entity clone() {
        Entity e = null;
        try {
            // list 集合深拷贝
            List<String> newList = new ArrayList<>(this.types);
            e = (Entity) super.clone();
            e.setTypes(newList);
        } catch (CloneNotSupportedException en) {
            en.printStackTrace();
        }
        return e;
    }

    @Override
    public int hashCode() {
        return getTypes().hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}
