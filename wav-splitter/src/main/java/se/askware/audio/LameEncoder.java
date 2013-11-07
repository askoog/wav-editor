package se.askware.audio;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import com.google.common.base.Optional;

public class LameEncoder {

	private File lameExec;

	public LameEncoder(File lameExec) {
		this.lameExec = lameExec;
		if (!lameExec.exists()) {
			throw new IllegalArgumentException("Lame.exe does not exist at path " + lameExec.getAbsolutePath());
		}
	}

	public void encode(File wavFile, File outFile, Track info, Project project) throws IOException,
			InterruptedException {
		List<String> commands = new ArrayList<String>();
		commands.add(lameExec.getAbsolutePath());
		commands.add("-h");
		commands.add("--nohist");
		commands.add("--disptime");
		commands.add("1");
		commands.add("--tt");
		commands.add(info.getName());
		commands.add("--tn");
		commands.add(info.getTrackId() + "/" + project.getNumTracks()); // TODO
		commands.add("--ta");
		commands.add("" + project.getAlbumArtist());
		commands.add("--ty");
		commands.add("" + Calendar.getInstance().get(Calendar.YEAR));
		commands.add("-b");
		commands.add("" + info.getBitRate());
		commands.add("--tl");
		commands.add("" + project.getAlbumName());
		commands.add("--tg");
		commands.add("" + project.getAlbumGenre());
		Optional<File> albumIconFile = project.getAlbumIcon();
		if (albumIconFile.isPresent()) {
			commands.add("--ti");
			commands.add("" + albumIconFile.get().getAbsolutePath());
		}
		commands.add(wavFile.getAbsolutePath());
		commands.add(outFile.getAbsolutePath());
		Process process = Runtime.getRuntime().exec(commands.toArray(new String[commands.size()]));
		BufferedReader r = new BufferedReader(new InputStreamReader(process.getErrorStream()));
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
