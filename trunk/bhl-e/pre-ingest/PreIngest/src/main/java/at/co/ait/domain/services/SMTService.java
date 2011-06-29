package at.co.ait.domain.services;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.co.ait.domain.oais.DigitalObject;
import at.co.ait.web.common.UserPreferences;

public class SMTService extends ProcessbuilderService {
	
	private static final Logger logger = LoggerFactory
	.getLogger(SMTService.class);
	
	private List<String> commands;
	private UserPreferences prefs;

	public DigitalObject map(DigitalObject obj, String params)
			throws MalformedURLException, IOException, InterruptedException {
		commands = new ArrayList<String>();
		commands.add("java");
		commands.add("-jar");
		commands.add((new java.net.URL(
				"file://C:/SourceCode/smt-cli/SMT-CLI.jar")).getPath());
		for (String param : params.split(" ")) {
			commands.add(param);
		}
		commands.add("-if");
		commands.add(obj.getSubmittedFile().getAbsolutePath());
		commands.add("-of");
		commands.add(obj.getSubmittedFile().getAbsolutePath() + ".smt");
		process(commands);		
		// read temporary file contents
		File output = new File(obj.getSubmittedFile()
				.getAbsolutePath() + ".smt");
		// set ocr on digitalobject
		obj.setSmtoutput(FileUtils.readFileToString(output));
		// deleted temporary file
		output.delete();
		obj.setSmtServiceLog(stdout.toString());
		return obj;
	}

}
