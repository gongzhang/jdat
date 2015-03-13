package cn.edu.buaa.sei.jdat.io;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import cn.edu.buaa.sei.jdat.JarController.ProgressMonitor;

public class JarLoader {
	
	private String downloadDir = "";
	
	public JarLoader() {
	}
	
	public File load(String url, ProgressMonitor monitor) throws IOException {
		if (url.startsWith("http://")) {
			FileDownloader downloader = new FileDownloader(new URL(url), downloadDir, monitor);
			downloader.run();
			if (downloader.getStatus() == FileDownloader.ERROR) {
				throw new IOException("download error");
			}
			return new File(downloader.getLocalFilePath());
		} else {
			return new File(url);
		}
	}
	
	public void setDownloadDir(String dir) {
		this.downloadDir = dir;
	}
	
	public File[] openDirectory(File directory, boolean recursively) {
		ArrayList<File> files = new ArrayList<File>();
		if (recursively)
			openDirRecursively(files, directory);
		else {
			File[] fs = directory.listFiles();
			for (File f : fs) {
				if (f.getName().toLowerCase().endsWith(".jar"))
					files.add(f);
			}
		}
		return files.toArray(new File[0]);
	}
	
	private void openDirRecursively(ArrayList<File> files, File dir) {
		File[] fs = dir.listFiles();
		for (File f : fs) {
			if (f.getName().toLowerCase().endsWith(".jar"))
				files.add(f);
			else if (f.isDirectory())
				openDirRecursively(files, f);
		}
	}

}
