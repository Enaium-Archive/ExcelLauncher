package cn.lightcolour.excellauncher;

public class ExcelLauncher {
	public static void main(String[] args) {

		String JAVAPATH = System.getProperty("j");
		String APPDATA = System.getProperty("a");
		String VERSION = System.getProperty("v");
		String MINMEMORY = System.getProperty("mn");
		String MAXMEMORY = System.getProperty("mx");
		String WIDTHWINDOW = System.getProperty("w");
		String HEIGHTWINDOW = System.getProperty("h");
		String ID = System.getProperty("i");
		String UUID = System.getProperty("u");
		String ACCESSTOKEN = System.getProperty("at");

		Script s = new Script(JAVAPATH, APPDATA, VERSION,MINMEMORY, MAXMEMORY,
				WIDTHWINDOW,HEIGHTWINDOW, ID, UUID, ACCESSTOKEN);
		
		s.WriteScript();
		s.Run();

	}

}
