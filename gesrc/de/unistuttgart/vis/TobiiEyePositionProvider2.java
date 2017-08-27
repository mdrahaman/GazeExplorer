//package de.unistuttgart.vis;
//
//import java.io.IOException; 
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class TobiiEyePositionProvider2 {
//
//	private static final String rexLSLStreamerLocation = "D:\\Saied\\Eclipse\\Workspace\\RexLSLStreamer\\RexLSLStreamer\\build\\Release\\rexLSLStreamer.exe";
//	private static Process process = null;
//	public static boolean debug = false;
//
//	private static List<EyePositionListener2> eyePositionListeners = new ArrayList<>();
//
//	private static boolean start_rexLSLStreamer() {
//		try {
//			process = new ProcessBuilder(rexLSLStreamerLocation).start();
//		} catch (IOException e) {
//			e.printStackTrace();
//			return false;
//		}
//		return true;
//	}
//
//	private static void start_resLSLStreamer() {
//
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				while (true) {
//					if (process == null) {
//						start_rexLSLStreamer();
//					} else if (!process.isAlive()) {
//						start_rexLSLStreamer();
//					}
//
//					try {
//						Thread.sleep(100);
//					} catch (Exception e) {
//					}
//
//					if (debug) {
//						// System.out.println("resLSLStreamer is running...? " +
//						// (process != null && process.isAlive()));
//					}
//				}
//			}
//		}).start();
//	}
//
//	public static void startListeningToGazeData2(boolean debug) {
//		
//		TobiiEyePositionProvider2.debug = debug;
//
//		start_resLSLStreamer();
//
//		LSL.StreamInfo[] results = LSL.resolve_stream("type", "Gaze");
//		// open an inlet
//		LSL.StreamInlet inlet = new LSL.StreamInlet(results[0]);
//		// receive data
//		float[] sample;
//		try {
//			sample = new float[inlet.info().channel_count()];
//			while (true) {
//				inlet.pull_sample(sample);
//
//				while (true) {
//					inlet.pull_sample(sample);
//
//					// Need to read documentation
//					if (sample.length == 2) {
//						if (debug) {
//							System.out
//									.println("Data: " + Double.toString(sample[0]) + ", " + Double.toString(sample[1]));
//							System.out.println(eyePositionListeners.size());
//						}
//						for (EyePositionListener2 listener : eyePositionListeners) {
//							listener.eyePositionUpdated(sample[0], sample[1]);
//							if (debug) {
//								System.out.println(Double.toString(sample[0]) + ", " + Double.toString(sample[1]));
//							}
//						}
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	
//	
//	
////	class MyClass implements EyePositionListener2 {
////
////		@Override
////		public void eyePositionUpdated(double x, double y) {
////			System.out.println("Hello from MyClass, eyePositionUpdated");
////			System.out.println(x + ", " + y);
////
////		}
////	}
////
////	class MyOtherClass implements EyePositionListener2 {
////
////		@Override
////		public void eyePositionUpdated(double x, double y) {
////			System.out.println("Hello from MyOtherClass, eyePositionUpdated");
////			System.out.println(x + ", " + y);
////
////		}
////	}
//	
//	
//	
//	
//	
//	
//	
//	public static void main(String[] args) {
//		startListeningToGazeData2(true);
//	}
//
//
//}
