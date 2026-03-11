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
	
	public void downloadGet(String url, Map<String, String> headers, OutputStream out) throws DownloaderException {
		byte[] buffer = new byte[10240];
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new NoRedirectStrategy()).build()) {
			HttpGet httpGet = new HttpGet(url);
			for(Entry<String, String> h : headers.entrySet()) {
				httpGet.setHeader(h.getKey(), h.getValue());
			}
			try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
				HttpEntity entity = response.getEntity();
				System.out.println("HTTP Status: "+response.getStatusLine().getStatusCode());
				if (entity != null) {
					try (InputStream instream = entity.getContent()) {
						int bytesRead;
			            while ((bytesRead = instream.read(buffer)) != -1) {
			            	System.out.println(""+bytesRead + "Bytes read");
			                out.write(buffer, 0, bytesRead);
			            }
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
