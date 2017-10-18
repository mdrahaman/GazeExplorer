package edu.ucsd.sccn;

public class Hello {

	public static void main(String[] args) {

		TobiiEyePositionProvider.addEyePositionListener(new MyClass());
		TobiiEyePositionProvider.addEyePositionListener(new MyOtherClass());
		TobiiEyePositionProvider.addEyePositionListener(new My3rdClass());
		
		TobiiEyePositionProvider.startListeningToGazeData(true);
	}

}

class MyClass implements EyePositionListener {

	@Override
	public void eyePositionUpdated(double x, double y) {
		System.out.println("Hello from MyClass, eyePositionUpdated");
		System.out.println(x + ", " + y);
	}
}

class MyOtherClass implements EyePositionListener {

	@Override
	public void eyePositionUpdated(double x, double y) {
		System.out.println("Hello from MyOtherClass, eyePositionUpdated");
		System.out.println(x + ", " + y);
	}
}

class My3rdClass implements EyePositionListener {

	@Override
	public void eyePositionUpdated(double x, double y) {
		System.out.println("Hello from My3rdClass, eyePositionUpdated");
		System.out.println(x + ", " + y);
	}
}