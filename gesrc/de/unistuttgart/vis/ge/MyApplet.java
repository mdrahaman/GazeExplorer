package de.unistuttgart.vis.ge;


import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;


public  class MyApplet extends PApplet {

	UnfoldingMap map2;

//	public void settings() {
//
//		size(800, 400, P2D);
//	}

	    

	public void setup() {

		size(800, 400, P2D);
		
		map2 = new UnfoldingMap(this,0,0,800,400, new Microsoft.HybridProvider());
		MapUtils.createDefaultEventDispatcher(this, map2);
//		map2.zoomToLevel(2);

		
		
	}

	public void draw() {
		background(200);
		map2.draw();
	}
	
	
	public static void main(String args[]) {
		PApplet.main(new String[] { MyApplet.class.getName() });
	}
	
	
}
