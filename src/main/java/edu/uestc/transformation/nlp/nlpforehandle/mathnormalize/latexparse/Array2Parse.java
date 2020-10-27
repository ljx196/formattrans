package edu.uestc.transformation.nlp.nlpforehandle.mathnormalize.latexparse;

/**
 * 数组第二种表示方式
 * \{\begin{matrix}.*\end{matrix}
 *
 * @author B1-101
 */
public class Array2Parse {
    public String arrayParse(String match, String orign) {
        String newString = match;
        newString = newString.replace("\\{\\begin{matrix}", "");
        newString = newString.replace("\\\\", "#");
        newString = newString.replace("\\end{matrix}", "");
        return orign.replace(match, newString);
    }
}
