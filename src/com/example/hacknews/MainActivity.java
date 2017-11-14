package com.example.hacknews;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	
	ArrayList<String> titles;
	
	ListView listview;
	
	ArrayAdapter<String> adapter;
	
	SharedPreferences savedtitles;
	
	SharedPreferences.Editor editor;
	
	public class GetNews extends AsyncTask<String,Void,String>{

		@Override
		protected String doInBackground(String... urls) {
	
			
			try{
				
			
				
				
	String result = "";
	
	URL url = new URL(urls[0]);
	
	HttpURLConnection connection = (HttpURLConnection)
			url.openConnection();
	
	InputStream is = connection.getInputStream();
	
	BufferedReader br = new BufferedReader(new InputStreamReader(is));
	
	String data = br.readLine();
	
	while(data!=null){
		
		result = result + data;
		
		data = br.readLine();
		
	}
	
	//Toast.makeText(getApplicationContext(),"Data about to be sent",Toast.LENGTH_LONG).show();
	
	return result;
					
	}catch(Exception e){
			
			
			
			}
			
		return null;
		}
			
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		listview = (ListView)findViewById(R.id.listView1);
		
		titles = new ArrayList<String>();
		
		savedtitles = getSharedPreferences("titles",MODE_PRIVATE);
		
		editor = savedtitles.edit();
		
		try{
			GetNews gn = new GetNews();

			String news = gn.execute("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty").get();
				
			Log.i("News",news);
					
			JSONArray arr = new JSONArray(news);

			for(int i=0;i<10;i++){
				
                    GetNews getArticle = new GetNews();

                    String x1 = getArticle.execute("https://hacker-news.firebaseio.com/v0/item/"+arr.getString(i)+".json?print=pretty").get();

                    JSONObject jobj = new JSONObject(x1);

                    String xtitle = jobj.getString("title");
                    String xurl = jobj.getString("url");

                    Log.i("title 2",xtitle);

                    titles.add(xtitle);

                    editor.putString(xtitle, xurl);

                    editor.apply();
	
	
                 }

            adapter = new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_list_item_1,android.R.id.text1,
                    titles);

            listview.setAdapter(adapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // TODO Auto-generated method stub


                    String tag = ((TextView)view).getText().toString();

                    String u1 = savedtitles.getString(tag,"");

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(u1));
                    startActivity(intent);



                }

            });



		}catch(Exception e){
			
			
		}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
