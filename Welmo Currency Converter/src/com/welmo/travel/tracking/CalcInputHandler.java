package com.welmo.travel.tracking;

public interface CalcInputHandler {
	public enum Operators {
	    SUM, SUB, MUL, DIVR, 		// SUM = somma, SUB = Sottrazione, MUL = Moltiplicazione, DIV = Divisione Reale
	    INV, EQ, MOD, DIV			// INV = cambia segno, EQ  = uguale , MOD = modulo o resto , DIV = Divisione Intera
	}
	
	public enum Symbols{
	    ZERO, ONE, TWO, THREE, 		// ZERO = zero, ONE = Uno, TwO = due, THREE = Tre
	    FOUR, FIVE, SIX, SEVEN, 	// FOUR = quattro, FIVE = cinque, SIX = sei, SEVEN = sette
	    HEIGHT, NINE, DOT, THOUSAND,// HEIGHT = otto, NINE = nove, DOT = punto decimale, THOUSAND = 000 
	    EXP 						// EXP = simbolo exponenziale
	}
	
	public enum Modifier{
	    C, AC, BS, CLOSE_SB 	// C = Cancel last number, CA = cancel all,  BS = cancell last input
	}
	
	public enum SpecialActions{
	    RETURN, HIDEKEYBOARD 
	}
	
	void inputSymboil(Symbols s);
	void inputOperators(Operators o);
	void inputModifier(Modifier m);
	void inputSpecialActions(SpecialActions m);
}
