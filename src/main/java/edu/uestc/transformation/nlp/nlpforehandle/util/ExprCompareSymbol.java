package edu.uestc.transformation.nlp.nlpforehandle.util;

public enum ExprCompareSymbol {
	EQ("=","=","等","是","为"),
	INEQ("≠","≠","不等","不是"),
	GEA(">",">","大"),
	LE("<","<","小"),
	GEQ("≥","≥",">=","大于等","不小"),
	LEQ("≤","≤","=<","小于等","不大"),
	APPROX("≈","≈","约等"),
	;
	private String symbol;
	private String[] deses;
	private ExprCompareSymbol(String symbol, String... deses) {
		this.symbol = symbol;
		this.deses = deses;
	}
	
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public static ExprCompareSymbol parse(String word)
	{
		for(ExprCompareSymbol exprCompareSymbol:ExprCompareSymbol.values())
		{
			for(String des:exprCompareSymbol.deses)
			{
				if(des.equals(word) || (des+"于").equals(word))
					return exprCompareSymbol;
			}
		}
		return null;
	}
}
