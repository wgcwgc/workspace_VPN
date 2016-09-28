package com.runcom.wgcwgc.business;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.runcom.wgcwgc.R;
import com.runcom.wgcwgc.md5.MD5;
import com.runcom.wgcwgc.web.SSLSocketFactoryEx;

public class Business extends Activity
{

	private Intent intent;
	private String contents;
	private TextView textView;
	String app;

	@Override
	protected void onCreate(Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.business);

		Toast.makeText(this ,"登录成功！！！" ,Toast.LENGTH_LONG).show();

		intent = getIntent();

		contents = "login:\t" + intent.getStringExtra("login") + "\n";
		contents += "pass:\t" + intent.getStringExtra("pass") + "\n";

		contents += "reslut:\t" + intent.getLongExtra("result" , -1) + "\n";
		contents += "mesg:\t" + intent.getStringExtra("mesg") + "\n";
		contents += "uid:\t" + intent.getStringExtra("uid") + "\n";
		contents += "expire:\t" + intent.getStringExtra("expire") + "\n";
		contents += "freetime:\t" + intent.getStringExtra("freetime") + "\n";
		contents += "flow:\t" + intent.getStringExtra("flow") + "\n";
		contents += "score:\t" + intent.getStringExtra("score") + "\n";
		contents += "coupon:\t" + intent.getStringExtra("coupon") + "\n";
		contents += "type:\t" + intent.getStringExtra("type") + "\n";
		contents += "email:\t" + intent.getStringExtra("email") + "\n";
		contents += "session:\t" + intent.getStringExtra("session") + "\n";

		textView = (TextView) findViewById(R.id.textView1);
		textView.setText(contents);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu )
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.business ,menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item )
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch(id)
		{
			case R.id.business_personInfromation:
				getPersonInfromation();
				break;

			case R.id.business_bind:
				bind();
				break;

			case R.id.business_cheakNewVersion:
				cheakNewVersion();
				break;

			case R.id.business_aboutUs:
				aboutUs();
				break;

			case R.id.business_settings:
				setting();

			default:
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	private void getPersonInfromation()
	{
		// TODO Auto-generated method stub

	}

	private void bind()
	{
		Toast.makeText(this ,"bind()" ,Toast.LENGTH_LONG).show();
		Intent intent = new Intent();
		intent.setClass(Business.this ,Bind.class);
		startActivity(intent);
	}

	@SuppressLint("DefaultLocale")
	private void cheakNewVersion()
	{

		GetThread_cheakNewVersion getThread_cheakNewVersion = new GetThread_cheakNewVersion();
		getThread_cheakNewVersion.start();

		// PostThread postThread = new PostThread();
		// postThread.start();

	}

	public String toURLEncoded(String paramString )
	{
		if(paramString == null || paramString.equals(""))
		{
			// LogD("toURLEncoded error:"+paramString);
			Log.d("LOG" ,"toURLEncoded error:" + paramString);
			return "";
		}

		try
		{
			String str = new String(paramString.getBytes() , "UTF-8");
			str = URLEncoder.encode(str ,"UTF-8");
			Log.d("LOG" ,"toURLEncoded:" + str);
			return str;
		}
		catch(Exception localException)
		{
			// LogE("toURLEncoded error:"+paramString, localException);
			Log.d("LOG" ,"toURLEncoded error:" + paramString);
		}

		return "";
	}

	// 子线程：使用POST方法向服务器发送用户名、密码等数据
	class PostThread extends Thread
	{

		String username;
		String password;

		public PostThread()
		{

		}

		public PostThread(String username , String password)
		{
			this.username = username;
			this.password = password;
		}

		@SuppressLint("DefaultLocale")
		@Override
		public void run()
		{

			PackageManager packageManager = Business.this.getPackageManager();
			PackageInfo packageInfo = null;
			try
			{
				packageInfo = packageManager.getPackageInfo(Business.this.getPackageName() ,0);
				int labelRes = packageInfo.applicationInfo.labelRes;
				app = Business.this.getResources().getString(labelRes);
			}
			catch(NameNotFoundException e)
			{
				Log.d("LOG" ,"serverJudge_package exception:" + e.toString());
			}
			String build = "57";
			String dev = android.provider.Settings.Secure.getString(Business.this.getContentResolver() ,android.provider.Settings.Secure.ANDROID_ID);
			String lang = Locale.getDefault().getLanguage();
			int market = 2;
			String os = Build.VERSION.RELEASE;
			int term = 0;
			String ver = packageInfo.versionName;
			// ver = "0." + ver;

			// 用HttpClient发送请求，分为五步
			// HttpClient httpClient = new DefaultHttpClient();
			HttpClient httpClient = SSLSocketFactoryEx.getNewHttpClient();
			// getNewHttpClient

			dev = android.provider.Settings.Secure.getString(Business.this.getContentResolver() ,android.provider.Settings.Secure.ANDROID_ID);

			String signValu = "tuoyouvpn" + app + build + dev + lang + market + os + term + ver;
			signValu = new MD5().md5(signValu).toUpperCase();
			String url = "https://a.redvpn.cn:8443/interface/getver.php?app=" + app + "&build=" + build + "&dev=" + dev + "&lang=" + lang + "&market=" + market + "&os=" + os + "&term=" + term + "&ver=" + ver + "&sign=" + signValu;
			// 第二步：创建代表请求的对象,参数是访问的服务器地址
			Log.d("LOG" ,url);

			// HttpClient httpClient = new DefaultHttpClient();
			// String url = "http://172.16.1.31:8080/test.jsp";
			// 第二步：生成使用POST方法的请求对象
			HttpPost httpPost = new HttpPost(url);
			// NameValuePair对象代表了一个需要发往服务器的键值对
			NameValuePair pair1 = new BasicNameValuePair("app" , app);
			NameValuePair pair2 = new BasicNameValuePair("build" , build);
			NameValuePair pair3 = new BasicNameValuePair("dev" , dev);
			NameValuePair pair4 = new BasicNameValuePair("lang" , lang);
			NameValuePair pair5 = new BasicNameValuePair("market" , market + "");
			NameValuePair pair6 = new BasicNameValuePair("os" , os);
			NameValuePair pair7 = new BasicNameValuePair("term" , term + "");
			NameValuePair pair8 = new BasicNameValuePair("ver" , ver);
			// 将准备好的键值对对象放置在一个List当中
			ArrayList < NameValuePair > pairs = new ArrayList < NameValuePair >();
			pairs.add(pair1);
			pairs.add(pair2);
			pairs.add(pair3);
			pairs.add(pair4);
			pairs.add(pair5);
			pairs.add(pair6);
			pairs.add(pair7);
			pairs.add(pair8);
			try
			{
				// 创建代表请求体的对象（注意，是请求体）
				HttpEntity requestEntity = new UrlEncodedFormEntity(pairs);
				// 将请求体放置在请求对象当中
				httpPost.setEntity(requestEntity);
				// 执行请求对象
				try
				{
					// 第三步：执行请求对象，获取服务器发还的相应对象
					HttpResponse response = httpClient.execute(httpPost);
					// 第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
					if(200 == response.getStatusLine().getStatusCode())
					{
						// 第五步：从相应对象当中取出数据，放到entity当中
						HttpEntity entity = response.getEntity();
						BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
						String json_result = reader.readLine();

						JSONObject jsonObject = new JSONObject(json_result);
						Log.d("LOG" ,jsonObject + "***");

						// Long result = jsonObject.getLong("result");
						// final String mesg = jsonObject.getString("mesg");
						// String min = jsonObject.getString("min");
						// String latest = jsonObject.getString("latest");
						// final String install =
						// jsonObject.getString("install");
						// String content = jsonObject.getString("content");

						Log.d("LOG" ,json_result);
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
					Log.d("LOG" ,"POST:服务器连接异常01！！！" + httpClient.execute(httpPost).getStatusLine().getStatusCode());
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				Log.d("LOG" ,"POST:服务器连接异常02！！！");
			}

		}
	}

	class GetThread_cheakNewVersion extends Thread
	{

		public GetThread_cheakNewVersion()
		{

		}

		@SuppressLint("DefaultLocale")
		@Override
		public void run()
		{
			PackageManager packageManager = Business.this.getPackageManager();
			PackageInfo packageInfo = null;
			try
			{
				packageInfo = packageManager.getPackageInfo(Business.this.getPackageName() ,0);
				int labelRes = packageInfo.applicationInfo.labelRes;
				app = Business.this.getResources().getString(labelRes);
			}
			catch(NameNotFoundException e)
			{
				Log.d("LOG" ,"serverJudge_package exception:" + e.toString());
			}
			String build = "57";
			String dev = android.provider.Settings.Secure.getString(Business.this.getContentResolver() ,android.provider.Settings.Secure.ANDROID_ID);
			String lang = Locale.getDefault().getLanguage();
			int market = 2;
			String os = Build.VERSION.RELEASE;
			int term = 0;
			String ver = packageInfo.versionName;
			// ver = "0." + ver;

			// 用HttpClient发送请求，分为五步
			// HttpClient httpClient = new DefaultHttpClient();
			HttpClient httpClient = SSLSocketFactoryEx.getNewHttpClient();// getNewHttpClient

			dev = android.provider.Settings.Secure.getString(Business.this.getContentResolver() ,android.provider.Settings.Secure.ANDROID_ID);

			String signValu = "tuoyouvpn" + app + build + dev + lang + market + os + term + ver;
			signValu = new MD5().md5(signValu).toUpperCase();
			String url = "https://a.redvpn.cn:8443/interface/getver.php?app=" + app + "&build=" + build + "&dev=" + dev + "&lang=" + lang + "&market=" + market + "&os=" + os + "&term=" + term + "&ver=" + ver + "&sign=" + signValu;
			// 第二步：创建代表请求的对象,参数是访问的服务器地址
			Log.d("LOG" ,url);
			// url = URLEncoder.encode(url);
			// Log.d("LOG" ,url);
			// url = toURLEncoded(url);
			HttpGet httpGet = new HttpGet(url);
			try
			{
				// 第三步：执行请求，获取服务器发还的相应对象
				HttpResponse response = httpClient.execute(httpGet);
				// String json_result = "";
				// System.out.println("test00");
				// 第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
				if(response.getStatusLine().getStatusCode() == 200)
				{
					// 第五步：从相应对象当中取出数据，放到entity当中
					HttpEntity entity = response.getEntity();
					BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));

					String json_result = reader.readLine();
					Log.d("LOG" ,"json_result:" + json_result + ":");
					JSONObject jsonObject = new JSONObject(json_result.toString());
					// Log.d("LOG" ,jsonObject + "***");

					// Long result = (long) 0;
					Long result = jsonObject.getLong("result");
					final String mesg = jsonObject.getString("mesg");
					String min = jsonObject.getString("min");
					String latest = jsonObject.getString("latest");
					final String install = jsonObject.getString("install");
					String content = jsonObject.getString("content");

//					Log.d("LOG" ,json_result);

					
					Log.d("LOG" ,json_result);
					// Long ver_first = Long.valueOf(ver.substring(0
					// ,ver.indexOf(".")));
					String [] ver_string = ver.split(".");
					String [] min_string = min.split(".");

					Long ver_first = Long.valueOf(ver_string[0]);
					Long ver_second = Long.valueOf(ver_string[1]);

					Long min_first = Long.valueOf(min_string[0]);
					Long min_second = Long.valueOf(min_string[1]);
					
					Log.d("LOG" ,"ver_first:" + ver_first + "\nver_second:" + ver_second + "\nmin_first:" + min_first + "\nmin_second:" + min_second);
					
					if(result == 0)
					{

						
						if(ver_first < min_first || (ver_first == min_first && ver_second < min_second))
						{
							Toast.makeText(Business.this ,"强制更新！！！" ,Toast.LENGTH_LONG).show();
						}
						else
						{
							AlertDialog.Builder builder = new AlertDialog.Builder(Business.this);
							builder.setTitle("更新");
							builder.setMessage(content);
							builder.setCancelable(false);
							// 确定按钮
							builder.setPositiveButton("确定" ,new DialogInterface.OnClickListener()
							{
								@Override
								public void onClick(DialogInterface dialog , int which )
								{
									Intent intent_cheackNewVersion = new Intent(Intent.ACTION_VIEW);
									intent_cheackNewVersion.setData(Uri.parse(install));
									startActivity(intent_cheackNewVersion);
									// Toast.makeText(Business.this ,"已删除"
									// ,Toast.LENGTH_LONG).show();
									// System.out.println("已删除-onClick,,,");
								}

							});
							// 取消按钮
							builder.setNegativeButton("取消" ,new DialogInterface.OnClickListener()
							{
								@Override
								public void onClick(DialogInterface dialog , int which )
								{
									Toast.makeText(Business.this ,"已取消更新！！！" ,Toast.LENGTH_SHORT).show();
									// System.out.println("已取消-onClick,,,");
								}
							});

							builder.show();
						}
					}
					else
					{
						Toast.makeText(Business.this ,mesg ,Toast.LENGTH_LONG).show();

					}

				}
				else
				{
					Toast.makeText(Business.this ,"网络异常！！！" ,Toast.LENGTH_LONG).show();
				}
			}
			catch(Exception e)
			{
				Log.d("LOG" ,"1.\u5f00\u901a\u652f\u4ed8\u5b9d\u63a5\u53e3,2.\u589e\u52a0\u6d88\u606f\u63a8\u9001" + "GetThread_http_bug");
				e.printStackTrace();
			}
		}
	}

	private void aboutUs()
	{
		// TODO Auto-generated method stub

	}

	private void setting()
	{
		// TODO Auto-generated method stub

	}
}
