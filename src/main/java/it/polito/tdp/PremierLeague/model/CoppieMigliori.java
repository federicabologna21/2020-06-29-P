package it.polito.tdp.PremierLeague.model;

public class CoppieMigliori {

	Match m1;
	Match m2;
	double  gioc;
	public CoppieMigliori(Match m1, Match m2, double gioc) {
		super();
		this.m1 = m1;
		this.m2 = m2;
		this.gioc = gioc;
	}
	public Match getM1() {
		return m1;
	}
	public void setM1(Match m1) {
		this.m1 = m1;
	}
	public Match getM2() {
		return m2;
	}
	public void setM2(Match m2) {
		this.m2 = m2;
	}
	public double getGioc() {
		return gioc;
	}
	public void setGioc(double gioc) {
		this.gioc = gioc;
	}
	@Override
	public String toString() {
		return m1 + " "+m2+" "+gioc ;
	}
	
	
}
