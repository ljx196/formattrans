package edu.uestc.transformation.nlp.nlpforehandle.mathnormalize.latexparse;

public class SuperScriptParse {
    /**
     * latex 上标解析函数
     *
     * @return
     */
    public String superScriptParse(String str, int start, String replacestr) {
        if (replacestr != null && replacestr.startsWith("^{", start)) {
            int loop = 1;
            int end = -1;
            for (int i = start + 2; i < str.length(); i++) {
                if (str.charAt(i) == '}') {
                    loop--;
                }
                if (str.charAt(i) == '{') {
                    loop++;
                }
                if (loop == 0) {
                    end = i;
                    break;
                }
            }
            if (loop == 0) {
                String oldstring = str.substring(start, end + 1);
                String newstr = "^(" + str.substring(start + 2, end) + ")";
                replacestr = replacestr.replace(oldstring, newstr);
            }
        }
        return replacestr;
    }
}
