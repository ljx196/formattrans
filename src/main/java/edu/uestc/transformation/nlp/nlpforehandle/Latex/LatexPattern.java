package edu.uestc.transformation.nlp.nlpforehandle.Latex;

/**
 * @author: tjc
 * @create: 2020/9/23
 * @description:
 */

public class LatexPattern {
    private String tagName;
    private String patrern;

    public LatexPattern() {
    }

    public LatexPattern(String tagName, String patrern) {
        this.tagName = tagName;
        this.patrern = patrern;
    }

    public String getTagName() {
        return this.tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getPatrern() {
        return this.patrern;
    }

    public void setPatrern(String patrern) {
        this.patrern = patrern;
    }

    public class SimilarLatex {
        private String orginstr;
        private String newstr;

        public SimilarLatex(String orginstr, String newstr) {
            this.orginstr = orginstr;
            this.newstr = newstr;
        }

        public SimilarLatex() {
        }

        public String getOrginstr() {
            return this.orginstr;
        }

        public void setOrginstr(String orginstr) {
            this.orginstr = orginstr;
        }

        public String getNewstr() {
            return this.newstr;
        }

        public void setNewstr(String newstr) {
            this.newstr = newstr;
        }
    }
}
