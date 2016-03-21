package com.vidasconcurrentes.pong;

public class Marcador {

	public static final int MAX_PUNT = 2;
	private static Marcador marcador;
	
	public static synchronized Marcador getInstance(){
		if(marcador==null)
			marcador=new Marcador();
		return marcador;
	}
	
	
	public void setMarcador(int L,int R){
		pointsL=L;
		pointsR=R;
	}

	private int pointsL, pointsR;
	
	public void initMarcador() {
		pointsL = 0;
		pointsR = 0;
	}

	public Marcador() {
		initMarcador();
	}
	
	public Marcador(int L,int R){
		setMarcador(L,R);
	}

	public int getL() {
		return pointsL;
	}

	public int getR() {
		return pointsR;
	}

	public void addL() {
		pointsL++;
	}

	public void addR() {
		pointsR++;
	}

	public boolean acabado() {
		return pointsR == MAX_PUNT || pointsL == MAX_PUNT;
	}
}
