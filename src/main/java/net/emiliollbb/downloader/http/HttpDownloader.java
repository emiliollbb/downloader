package net.emiliollbb.downloader.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;

import net.emiliollbb.downloader.exception.DownloaderException;

public class HttpDownloader {
	private static final int BUFFER_SIZE=104857600;
	
	public void downloadGet(String url, Map<String, String> headers, OutputStream out) throws DownloaderException {
		byte[] buffer = new byte[BUFFER_SIZE];
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new NoRedirectStrategy()).build()) {
			HttpGet httpGet = new HttpGet(url);
			for(Entry<String, String> h : headers.entrySet()) {
				httpGet.setHeader(h.getKey(), h.getValue());
			}
			System.out.println("GET "+url);
			try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
				HttpEntity entity = response.getEntity();
				System.out.println("HTTP Status: "+response.getStatusLine().getStatusCode());
				if (entity != null) {
					try (InputStream instream = entity.getContent()) {
						System.out.print("Downloading");
						System.out.flush();
						int bytesRead;
						int dots=0;
						long totalBytes=0;
			            while ((bytesRead = instream.read(buffer)) != -1) {
			                out.write(buffer, 0, bytesRead);
							totalBytes+=bytesRead;
							if(dots++%10==0) {
								System.out.print(".");
								System.out.flush();
							}
							if(dots%1000==0) {
								System.out.print("\n");
							}
			            }
						System.out.println("\n"+totalBytes+" bytes read");
					}
				}
			}
		} catch (IOException e) {
			throw new DownloaderException(e);
		}
	}
	
	private class NoRedirectStrategy extends DefaultRedirectStrategy {
		@Override
		public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context)
				throws ProtocolException {
			return false;
		}

	}
}
