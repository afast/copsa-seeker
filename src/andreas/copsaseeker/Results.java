package andreas.copsaseeker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class Results extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        ListView listView1 = (ListView)findViewById(R.id.listview1);
        ArrayList<Result> data= null;
        Bundle b = getIntent().getExtras();
        String linea = b.getString("linea");
        String desde = b.getString("desde");
		String hacia = b.getString("hacia");
		String por = b.getString("por");
		String from = b.getInt("fromHour") + ":" + b.getInt("fromMinute");
		String to = b.getInt("toHour") + ":" + b.getInt("toMinute");
        data = processHtml(postData(linea, desde, hacia, por), from, to);
        ResultAdapter adapter = new ResultAdapter(this, R.layout.listview, data);
        listView1.setAdapter(adapter);
    }
    
    private ArrayList<Result> processHtml(String postData, String from, String to) {
		ArrayList<Result> data = new ArrayList<Result>();
		int[] fromA, toA;
		System.out.println(from);
		System.out.println(to);
		fromA = parseTime(from, ":");
		toA = parseTime(to, ":");
		Pattern p = Pattern.compile("<tr align=\"center\" bgcolor=\"#ffffff\">\\s*<td>(.*)</td>\\s*<td>(.*)</td>\\s*<td>(.*)</td>\\s*<td>(.*)</td>\\s*<td>(.*)</td>\\s*<td>(.*)</td>\\s*<td>(.*)</td>\\s*.*?</tr>");
		Matcher m = p.matcher(postData);
		m = p.matcher(postData.replaceAll("&nbsp;", ""));
		while (m.find()) { // Find each match in turn; String can't do this.
			Result res = new Result();
			String last=null;
			res.desde= m.group(2);
			res.hacia= m.group(3);
			res.salida = m.group(5).replaceAll("n", "");
			res.por = m.group(7).replaceAll("\"x ", "").replaceAll("\"", "").replaceAll(" x ", "\n");
			Matcher time = Pattern.compile("\\d\\d\\.\\d\\d?").matcher(new StringBuilder(res.por).reverse());
			if (time.find())
				last = (new StringBuilder(time.group(0)).reverse()).toString().replaceAll("\\.", ":");
			
			if (isGreater(res.salida.replaceAll("\\.", ":"), fromA) && isGreater(toA, res.salida.replaceAll("\\.", ":")) || (last != null &&  isGreater(last, fromA) && isGreater(toA, last)))
				data.add(res);
		}
		System.out.println(data.size());
		return data;
	}
    
    private int[] parseTime(String t, String separator){
    	String[] res = t.split(separator);
    	int result[];
    	result = new int[2];
    	result[0] = Integer.parseInt(res[0]);
    	result[1] = Integer.parseInt(res[1]);
    	return result;
    }
    
    private boolean isGreater(String t1, int[] t2){
    	int h1, m1;
    	h1 = Integer.parseInt(t1.split(":")[0]);
    	m1 = Integer.parseInt(t1.split(":")[1]);
    	return h1 > t2[0] || h1 == t2[0] && m1 > t2[1];
    }
    
    private boolean isGreater(int[] t1, String t2){
    	int h2, m2;
    	h2 = Integer.parseInt(t2.split(":")[0]);
    	m2 = Integer.parseInt(t2.split(":")[1]);
    	return t1[0] > h2 || t1[0] == h2 && t1[1] > m2;
    }

	public String postData(String linea, String desde, String hacia, String por) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        String result="";
        HttpPost httppost = new HttpPost("http://copsa.com.uy/cgi/buscador_hab.cgi");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            if (linea != null && linea.length() > 0)
            	nameValuePairs.add(new BasicNameValuePair("linea", linea));
            if (desde != null && desde.length() > 0)
            	nameValuePairs.add(new BasicNameValuePair("desde", desde));
            if (hacia != null && hacia.length() > 0)
            	nameValuePairs.add(new BasicNameValuePair("hacia", hacia));
            if (por != null && por.length() > 0)
            	nameValuePairs.add(new BasicNameValuePair("por", por));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
            {
                result = EntityUtils.toString(response.getEntity());
            }
            
        } catch (ClientProtocolException e) {
            System.out.println("Protocol exception");
        } catch (IOException e) {
        	System.out.println("IO exception");
        }
		return result;
    } 
    // see http://androidsnippets.com/executing-a-http-post-request-with-httpclient
}