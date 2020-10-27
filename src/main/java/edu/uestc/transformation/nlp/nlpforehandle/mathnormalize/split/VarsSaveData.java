package edu.uestc.transformation.nlp.nlpforehandle.mathnormalize.split;

/**
 * Created by ZZH on 2016/12/13.
 *
 * 为了检验引入变量重复不重名的data类
 * 例如:QG1201610L
 */
public class VarsSaveData {

    private String name = "";
    private String description = "";
    private String total = "";

    public VarsSaveData(String name, String description, String total) {
        this.name = name;
        this.description = description;
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
