package cn.edu.buaa.sei.jdat.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.edu.buaa.sei.jdat.JarController.ProgressMonitor;

public class FileDownloader implements Runnable {
    
    private static final int BUFFER_SIZE = 1024;
    
    public static final int DOWNLOADING = 0;
    public static final int COMPLETE = 1;
    public static final int ERROR = 2;
    
    private URL url;
    private int size;
    private int downloaded;
    private int status;
    private String downloadDir;
    private String localFilePath;
    private ProgressMonitor monitor;
    
    // Constructor for Download.
    public FileDownloader(URL url, String dir, ProgressMonitor monitor) {
        this.url = url;
        this.monitor = monitor;
        downloadDir = dir;
        localFilePath = null;
        size = -1;
        downloaded = 0;
        status = DOWNLOADING;
    }
    
    public String getUrl() {
        return url.toString();
    }
    
    public int getSize() {
        return size;
    }
    
    public float getProgress() {
        return ((float) downloaded / size) * 100;
    }
    
    public int getStatus() {
        return status;
    }
    
    private String getFileName(URL url) {
        String fileName = url.getFile();
        return fileName.substring(fileName.lastIndexOf('/') + 1);
    }
    
    public void run() {
    	FileOutputStream fos = null;
        InputStream stream = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            connection.connect();
            
            // Make sure response code is in the 200 range.
            if (connection.getResponseCode() / 100 != 2) {
            	status = ERROR;
            }
            
            int contentLength = connection.getContentLength();
            if (contentLength < 1) {
            	status = ERROR;
            }
            
            if (size == -1) {
                size = contentLength;
            }

            localFilePath = downloadDir + getFileName(url);
            File tempFile = new File(localFilePath);
            fos = new FileOutputStream(tempFile);
            
            stream = connection.getInputStream();
            byte[] buffer = new byte[BUFFER_SIZE];
            int read;
            while ((read = stream.read(buffer)) != -1) {
            	fos.write(buffer, 0, read);
                downloaded += read;
                if (monitor != null) monitor.updateProgress((double) downloaded / size);
            }
            
            if (status == DOWNLOADING) {
                status = COMPLETE;
                if (monitor != null) monitor.updateProgress(1.0f);
            }
        } catch (Exception e) {
        	status = ERROR;
        } finally {
            if (fos != null) {
                try {
                	fos.close();
                } catch (Exception e) {
                }
            }
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {
                }
            }
        }
    }
    
    public String getLocalFilePath() {
		return localFilePath;
	}

}
