package edu.uestc.transformation.nlp.nlpforehandle.mathnormalize.latexparse;


public class ArrayParse {
    public String arrayParse(String match, String orign) {
        String newString = match;
        newString = newString.replace("\\{{\\begin{array}{*{20}{c}}", "");
        newString = newString.replace("\\\\", "#");
        newString = newString.replace("\\end{array}}", "");
        return orign.replace(match, newString);
    }
}
