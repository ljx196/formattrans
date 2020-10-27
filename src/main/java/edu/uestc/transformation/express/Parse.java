package edu.uestc.transformation.express;

/**
 * @author: tjc
 * @create: 2020/10/20
 * @description:
 */

public class Parse {
    private int id;
    private String parse;
    private String answer2;

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    private String title;
    private String json;
    private String triples;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getTriples() {
        return triples;
    }

    public void setTriples(String triples) {
        this.triples = triples;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getParse() {
        return parse;
    }

    public void setParse(String parse) {
        this.parse = parse;
    }

    @Override
    public String toString() {
        return "Parse{" +
                "id='" + id + '\'' +
                ", parse='" + parse + '\'' +
                '}';
    }
}
