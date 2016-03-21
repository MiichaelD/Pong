package com.vidasconcurrentes.pong;

import android.graphics.Point;
import android.graphics.Rect;

public abstract class Rectangulo {

	protected Point origen;
	protected int ancho, alto;
	
	public Rectangulo(Point or, int ancho, int alto) {
		this.origen = or;
		this.ancho = ancho;
		this.alto = alto;
	}
	
	public int getX() {
		return origen.x;
	}
	
	public int getY() {
		return origen.y;
	}
	
	public int getAncho() {
		return ancho;
	}
 
	public void setX(int newX) {
		origen.x=newX;
	}
	
	public void setY(int newY) {
		origen.y=(newY);
	}
	
	public int getAlto() {
		return alto;
	}
	
	//checks if the ball is able to move within the screen
	public boolean puedoMover(int x, int y, Rect screen) {
		 return screen.contains(origen.x + x, origen.y + y, origen.x + ancho + x, origen.y + alto + y);
	}

	public Rect getRect() {
		return new Rect(getX(), getY(),getX()+ancho, getY()+alto);
	}
}