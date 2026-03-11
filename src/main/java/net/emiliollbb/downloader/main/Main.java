package net.emiliollbb.downloader.main;

import java.io.FileOutputStream;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import net.emiliollbb.downloader.http.HttpDownloader;

public class Main {
	public static void main(String[] args) throws Exception {
		HttpDownloader downloader = new HttpDownloader();
		Map<String, String> headers = new TreeMap<>();
		try(FileOutputStream out = new FileOutputStream("/tmp/boat.png")) {
			downloader.downloadGet("http://www.emiliollbb.net/img/boat.png", headers, out);
		}
	}
	
	public static void mainz(String[] args) throws Exception {
		Options options = new Options();

		Option user = new Option("u", "url", true, "Url");
		user.setRequired(true);
		options.addOption(user);

		Option password = new Option("h", "header", true, "header");
		password.setRequired(true);
		options.addOption(password);

		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd;

		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			formatter.printHelp("utility-name", options);

			System.exit(1);
			return;
		}

		String u = cmd.getOptionValue("url");
		String h = cmd.getOptionValue("header");

	}
}
