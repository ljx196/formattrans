package edu.uestc.transformation.nlp.relation;

import java.util.Objects;

/**
 * @author: yyWang
 * @create: 2019/10/31
 * @description: 关系属性类
 */

public class Property {

    // 属性名
    private String propertyName;
    // 属性类型
    private String propertyType;

    public String getPropertyName() {
        return propertyName;
    }

    public Property(String propertyName, String propertyType) {
        this.propertyName = propertyName;
        this.propertyType = propertyType;
    }

    public Property() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Property property = (Property) o;
        return propertyName.equals(property.propertyName) &&
                propertyType.equals(property.propertyType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(propertyName, propertyType);
    }
}
