package de.fhpotsdam;

import processing.core.PApplet; 
import processing.core.PImage;

public class ImageLoadFromJarTestApp extends PApplet {

	PImage img;

	public void setup() {
		size(800, 600);
		img = loadImage("ag-lgflag.gif");
		
		
	}
	
	

	public void draw() {
		background(0);
		
		image(img, 0, 0);
	}
	
	public static void main(String args[]) {
		PApplet.main(new String[] { ImageLoadFromJarTestApp.class.getName() });
	}

}
