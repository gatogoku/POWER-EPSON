package gatogoku.epsonapagar;

public class Constants {
	public interface ACTION {
		public static String MAIN_ACTION = "com.epson.foregroundservice.action.main";
		public static String PREV_ACTION = "com.epson.foregroundservice.action.prev";
		public static String PLAY_ACTION = "com.epson.foregroundservice.action.play";
		public static String NEXT_ACTION = "com.epson.foregroundservice.action.next";
		public static String STARTFOREGROUND_ACTION = "com.epson.foregroundservice.action.startforeground";
		public static String STOPFOREGROUND_ACTION = "com.epson.foregroundservice.action.stopforeground";
	}

	public interface NOTIFICATION_ID {
		public static int FOREGROUND_SERVICE = 101;
	}
}
