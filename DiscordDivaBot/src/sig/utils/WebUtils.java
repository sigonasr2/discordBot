package sig.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

public class WebUtils {
	public static void POSTimage(String url,File file,Map<String,String> params) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost uploadFile = new HttpPost(url);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		for (String s : params.keySet()) {
			builder.addTextBody(s, params.get(s), ContentType.TEXT_PLAIN);
		}
		try {
			// This attaches the file to the POST:
			builder.addBinaryBody(
			    "file",
			    new FileInputStream(file),
			    ContentType.APPLICATION_OCTET_STREAM,
			    file.getName()
			);

			HttpEntity multipart = builder.build();
			uploadFile.setEntity(multipart);
			CloseableHttpResponse response;
			response = httpClient.execute(uploadFile);
			HttpEntity responseEntity = response.getEntity();
			String result = "";
			if (responseEntity != null) {
			    try (InputStream instream = responseEntity.getContent()) {
			    	Scanner s = new Scanner(instream).useDelimiter("\\A");
			    	result = s.hasNext() ? s.next() : "";
			    	System.out.println(result);
			    	instream.close();
			    } catch (UnsupportedOperationException | IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
