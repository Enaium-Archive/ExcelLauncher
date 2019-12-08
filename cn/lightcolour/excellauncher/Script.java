package cn.lightcolour.excellauncher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Script {

	private String JAVAPATH;
	private String APPDATA;
	private String VERSION;
	private String MINMEMORY;
	private String MAXMEMORY;
	private String WIDTHWINDOW;
	private String HEIGHTWINDOW;
	private String ID;
	private String UUID;
	private String ACCESSTOKEN;

	public Script(String javapath, String appdata, String version, String minmemory, String maxmemory,
			String widthwindow, String height, String id, String uuid, String accesstoken) {
		this.JAVAPATH = javapath;
		this.APPDATA = appdata;
		this.VERSION = version;
		this.MINMEMORY = minmemory;
		this.MAXMEMORY = maxmemory;
		this.WIDTHWINDOW = widthwindow;
		this.HEIGHTWINDOW = height;
		this.ID = id;
		this.UUID = uuid;
		this.ACCESSTOKEN = accesstoken;
	}

	public void WriteScript() {

		StringBuilder s = new StringBuilder();
		StringBuilder ss = new StringBuilder();
		s.append("@echo off");
		s.append("\n");
		s.append("set APPDATA=");
		s.append(this.APPDATA);
		s.append("\n");
		ss.append("\"");
		ss.append(this.JAVAPATH);
		ss.append("\"");
		ss.append(" ");
		ss.append("-Dminecraft.client.jar=");
		ss.append(this.APPDATA + "\\.minecraft\\versions\\" + this.VERSION + "\\" + this.VERSION + ".jar");
		ss.append(" ");
		ss.append("-XX:+UnlockExperimentalVMOptions");
		ss.append(" ");
		ss.append("-XX:+UseG1GC");
		ss.append(" ");
		ss.append("-XX:G1NewSizePercent=20");
		ss.append(" ");
		ss.append("-XX:G1ReservePercent=20");
		ss.append(" ");
		ss.append("-XX:MaxGCPauseMillis=50");
		ss.append(" ");
		ss.append("-XX:G1HeapRegionSize=16M");
		ss.append(" ");
		ss.append("-XX:-UseAdaptiveSizePolicy");
		ss.append(" ");
		ss.append("-XX:-OmitStackTraceInFastThrow");
		ss.append(" ");
		ss.append("-Xmn").append(this.MINMEMORY).append("m");
		ss.append(" ");
		ss.append("-Xmx").append(this.MAXMEMORY).append("m");
		ss.append(" ");
		ss.append("-Dfml.ignoreInvalidMinecraftCertificates=true");
		ss.append(" ");
		ss.append("-Dfml.ignorePatchDiscrepancies=true");
		ss.append(" ");
		ss.append("-XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump");
		ss.append(" ");
		ss.append("-Djava.library.path=").append(this.APPDATA + "\\.minecraft\\versions\\" + this.VERSION + "\\" + this.VERSION + "-natives");
		ss.append(" ");
		ss.append("-Dminecraft.launcher.brand=EMCL");
		ss.append(" ");
		ss.append("-Dminecraft.launcher.version=1.0");
		ss.append(" ");
		ss.append("-cp");
		ss.append(" ");
		ss.append(getLibraries());
		ss.append(this.APPDATA + "\\.minecraft\\versions\\" + this.VERSION + "\\" + this.VERSION + ".jar");
		ss.append(" ");
		ss.append(getmainClass());
		ss.append(" ");
		ss.append("--width");
		ss.append(" ");
		ss.append(this.WIDTHWINDOW);
		ss.append(" ");
		ss.append("--height");
		ss.append(" ");
		ss.append(this.HEIGHTWINDOW);
		ss.append(" ");
		ss.append("--username");
		ss.append(" ");
		ss.append(this.ID);
		ss.append(" ");
		ss.append("--version");
		ss.append(" ");
		ss.append("\"EMCL 1.0\"");
		ss.append(" ");
		ss.append("--gameDir");
		ss.append(" ");
		ss.append(this.APPDATA + "\\.minecraft");
		ss.append(" ");
		ss.append("--assetsDir");
		ss.append(" ");
		ss.append(this.APPDATA + "\\.minecraft\\assets");
		ss.append(" ");
		ss.append("--assetIndex");
		ss.append(" ");
		ss.append(getassetIndex());
		ss.append(" ");
		ss.append("--uuid");
		ss.append(" ");
		ss.append(this.UUID);
		ss.append(" ");
		ss.append("--accessToken");
		ss.append(" ");
		ss.append(this.ACCESSTOKEN);
		ss.append(" ");
		ss.append("--userProperties");
		ss.append(" ");
		ss.append("{}");
		ss.append(" ");
		ss.append("--userType");
		ss.append(" ");
		ss.append("mojang");
		s.append(ss.toString());
		WriteBat(s.toString());
	}

	public void Run() {
		Process p;
		String cmd = APPDATA + "\\.minecraft\\ExcelLauncher.bat";
		try {
			p = Runtime.getRuntime().exec(cmd);
			InputStream fis = p.getInputStream();
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getLibraries() {
		String json = Utils.txtString(new File(APPDATA + "\\.minecraft\\versions\\"+ VERSION + "\\" + VERSION + ".json"));
		JSONObject jsonObject = JSON.parseObject(json);
		JSONArray jsonArray = JSON.parseArray(jsonObject.get("libraries").toString());
		String re = "";
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject name_object = JSONObject.parseObject(jsonArray.get(i).toString());
			String[] libraries_arr = name_object.get("name").toString().split(":");
			String lib_path = libraries_arr[0];
			String[] lib_path_arr = lib_path.split("\\.");
			String temp = "";
			for (int j = 0; j < lib_path_arr.length; j++) {
				temp += lib_path_arr[j] + "\\";
			}
			lib_path = APPDATA + "\\.minecraft\\libraries\\" + temp + libraries_arr[1] + "\\" + libraries_arr[2] + "\\"
					+ libraries_arr[1] + "-" + libraries_arr[2] + ".jar";
			re += lib_path + ";";
		}
		return re;
	}

	private String getassetIndex() {
		File file = new File(APPDATA + "\\.minecraft\\versions\\"+ VERSION + "\\" + VERSION + ".json");
		String json = Utils.txtString(file);
		JSONObject jsonObject = JSON.parseObject(json);
		JSONObject assetIndexObject = JSONObject.parseObject(jsonObject.getString("assetIndex"));
		return assetIndexObject.get("id").toString();
	}

	private String getmainClass() {
		File file = new File(APPDATA + "\\.minecraft\\versions\\"+ VERSION + "\\" + VERSION + ".json");
		JSONObject jsonObject = JSON.parseObject(Utils.txtString(file));
		return jsonObject.get("mainClass").toString();
	}

	private void WriteBat(String text) {

		try {
			FileOutputStream fileOutputStream = null;
			File file = new File(APPDATA + "\\.minecraft\\ExcelLauncher.bat");
			if (file.exists()) {
				file.createNewFile();
			}
			fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(text.getBytes());

			fileOutputStream.flush();
			fileOutputStream.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

}
