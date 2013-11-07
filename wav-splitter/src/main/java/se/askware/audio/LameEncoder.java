package se.askware.audio;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

public class LameEncoder {

	private File lameExec;

	public LameEncoder(File lameExec) {
		this.lameExec = lameExec;
		if (!lameExec.exists()) {
			throw new IllegalArgumentException(
					"Lame.exe does not exist at path "
							+ lameExec.getAbsolutePath());
		}
	}

	public void encode(File wavFile, File outFile, Track info, Properties props)
			throws IOException, InterruptedException {
		List<String> commands = new ArrayList<String>();
		commands.add(lameExec.getAbsolutePath());
		commands.add("-h");
		commands.add("--nohist");
		commands.add("--disptime");
		commands.add("1");
		commands.add("--tt");
		commands.add(info.getName());
		commands.add("--tn");
		commands.add(info.getTrackId() + "/" + props.getProperty("num.tracks")); // TODO
		commands.add("--ta");
		commands.add("" + props.getProperty("album.artist"));
		commands.add("--ty");
		commands.add("" + Calendar.getInstance().get(Calendar.YEAR));
		commands.add("-b");
		commands.add("192");
		commands.add("--tl");
		commands.add("" + props.getProperty("album.name"));
		commands.add("--tg");
		commands.add("" + props.getProperty("album.genre"));
		commands.add("--ti");
		commands.add("" + props.getProperty("album.icon"));

		commands.add(wavFile.getAbsolutePath());
		commands.add(outFile.getAbsolutePath());
		Process process = Runtime.getRuntime().exec(
				commands.toArray(new String[commands.size()]));
		BufferedReader r = new BufferedReader(new InputStreamReader(process
				.getErrorStream()));
		String s;
		while ((s = r.readLine()) != null) {
			System.err.println(s);
		}
		r = new BufferedReader(new InputStreamReader(process.getInputStream()));
		while ((s = r.readLine()) != null) {
			System.out.println(s);
		}
		process.waitFor();
	}

}
