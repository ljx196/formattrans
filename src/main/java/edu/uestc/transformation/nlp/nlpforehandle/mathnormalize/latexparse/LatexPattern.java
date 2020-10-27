package edu.uestc.transformation.nlp.nlpforehandle.mathnormalize.latexparse;

/**
 * 识别latex 格式的正则表达式
 *
 * @author wangyong
 */
public class LatexPattern {

    private String tagName;
    private String patrern;

    public LatexPattern() {

    }

    public LatexPattern(String tagName, String patrern) {
        super();
        this.tagName = tagName;
        this.patrern = patrern;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getPatrern() {
        return patrern;
    }

    public void setPatrern(String patrern) {
        this.patrern = patrern;
    }

    /**
     * 预处理，之间转换相等的符号
     *
     * @author wangyong
     */
    public class SimilarLatex {
        private String orginstr;
        private String newstr;

        public SimilarLatex(String orginstr, String newstr) {
            super();
            this.orginstr = orginstr;
            this.newstr = newstr;
        }

        public SimilarLatex() {

        }

        public String getOrginstr() {
            return orginstr;
        }

        public void setOrginstr(String orginstr) {
            this.orginstr = orginstr;
        }

        public String getNewstr() {
            return newstr;
        }

        public void setNewstr(String newstr) {
            this.newstr = newstr;
        }
    }

}