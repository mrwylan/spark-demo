package wylan.ch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import org.junit.BeforeClass;
import org.junit.Test;

public class SparkDemoTest {

	@BeforeClass
	public static void setup() {
		SparkDemo.main(new String[] { "8082" });
	}

	@Test
	public void shouldWork() throws IOException {
		URL url = new URL("http://localhost:8082/sparkdemo");
		HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
		openConnection.setRequestMethod("GET");

		int success = openConnection.getResponseCode();

		assertTrue(success == 200);
	}

	@Test
	public void shouldFail() throws IOException {
		URL url = new URL("http://localhost:8082/sparknodemo");
		HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
		openConnection.setRequestMethod("GET");

		int success = openConnection.getResponseCode();

		assertTrue(success == 404);
	}

	@Test
	public void shouldPut() throws IOException {
		byte[] dataBytes = "Nicer job.".getBytes(Charset.forName("UTF-8"));

		URL url = new URL("http://localhost:8082/comment");
		HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
		openConnection.setDoOutput(true);
		openConnection.setRequestMethod("PUT");
		openConnection.setRequestProperty("Content-Length", Integer.toString(dataBytes.length));
		openConnection.setRequestProperty("charset", "utf-8");
		try (DataOutputStream dataStream = new DataOutputStream(openConnection.getOutputStream())) {
			dataStream.write(dataBytes);
		}

		int success = openConnection.getResponseCode();
		assertTrue(success == 200);

		URL urlr = new URL("http://localhost:8082/comments");
		HttpURLConnection openConnectionR = (HttpURLConnection) urlr.openConnection();
		openConnectionR.setRequestMethod("GET");

		int successr = openConnectionR.getResponseCode();

		assertTrue(successr == 200);
		String response = "";
		try (BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(openConnectionR.getInputStream()))) {
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				response = response.concat(line);
			}
		}
		assertEquals("Nicer job.", response);
	}

	@Test
	public void shouldLoadFile() throws IOException {
		URL url = new URL("http://localhost:8082/logo.svg");
		HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
		openConnection.setRequestMethod("GET");

		int success = openConnection.getResponseCode();
		assertTrue("response was " + success, success == 200);
		String response = "";
		try (BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(openConnection.getInputStream()))) {
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				response = response.concat(line);
			}
		}
		assertTrue(response.startsWith("<svg"));
	}

	@Test
	public void shouldPostDemoComment() throws IOException {
		{
			URL url = new URL(
					"http://localhost:8082/democomments?comment=".concat(URLEncoder.encode("What else?", "UTF-8")));
			HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
			openConnection.setRequestMethod("POST");
			int success = openConnection.getResponseCode();
			assertTrue(success == 200);
		}
		{
			URL url = new URL("http://localhost:8082/democomments/1?comment=".concat(URLEncoder.encode("Something else.", "UTF-8")));
			HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
			openConnection.setRequestMethod("PUT");
			int success = openConnection.getResponseCode();
			assertTrue(success == 200);

			String response = "";
			try (BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(openConnection.getInputStream()))) {
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					response = response.concat(line);
				}
			}
			assertTrue(response.contains("What else?"));
		}
		{
			URL url = new URL("http://localhost:8082/democomments/1");
			HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
			openConnection.setRequestMethod("GET");
			int success = openConnection.getResponseCode();
			assertTrue(success == 200);

			String response = "";
			try (BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(openConnection.getInputStream()))) {
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					response = response.concat(line);
				}
			}
			assertFalse(response.contains("What else?"));
		}

	}

}
