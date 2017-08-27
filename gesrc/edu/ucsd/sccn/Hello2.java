package edu.ucsd.sccn;

import de.unistuttgart.vis.EyePositionListener2;

public class Hello2 {
	
public static void main(String[] args) {

		
//		testEyePosition.addEyePositionListener2(new MyClass());
//		testEyePosition.addEyePositionListener2(new MyOtherClass());
		
	
		
		
		
		TobiiEyePositionProvider2.startListeningToGazeData2(true);
	}

}

class MyClass implements EyePositionListener2 {

	@Override
	public void eyePositionUpdated(double x, double y) {
		System.out.println("Hello from MyClass, eyePositionUpdated");
		System.out.println(x + ", " + y);
	}
}

class MyOtherClass implements EyePositionListener2 {

	@Override
	public void eyePositionUpdated(double x, double y) {
		System.out.println("Hello from MyOtherClass, eyePositionUpdated");
		System.out.println(x + ", " + y);
	}
	

}
