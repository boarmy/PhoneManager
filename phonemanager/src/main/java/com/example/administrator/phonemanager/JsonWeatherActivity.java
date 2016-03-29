package com.example.administrator.phonemanager;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class JsonWeatherActivity extends ActionBarActivity {

    private TextView citynm;
    private TextView days;
    private TextView weather;
    private TextView temperature;
    private TextView wind;
    private TextView winp;
    private EditText editext;

    private TextView citynm2;
    private TextView days2;
    private TextView weather2;
    private TextView temperature2;
    private TextView wind2;
    private TextView winp2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_weather);
        editext = (EditText) findViewById(R.id.editext);

        citynm = (TextView) findViewById(R.id.citynm);
        days = (TextView) findViewById(R.id.days);
        weather = (TextView) findViewById(R.id.weather);
        temperature = (TextView) findViewById(R.id.temperature);
        wind = (TextView) findViewById(R.id.wind);
        winp = (TextView) findViewById(R.id.winp);

        citynm2 = (TextView) findViewById(R.id.citynm2);
        days2 = (TextView) findViewById(R.id.days2);
        weather2 = (TextView) findViewById(R.id.weather2);
        temperature2 = (TextView) findViewById(R.id.temperature2);
        wind2 = (TextView) findViewById(R.id.wind2);
        winp2 = (TextView) findViewById(R.id.winp2);

    }

    class myhandler extends AsyncHttpResponseHandler {

        private String scitynm;
        private String sdays;
        private String sweather;
        private String stemperature;
        private String swind;
        private String swinp;
        private String scitynm2;
        private String sdays2;
        private String sweather2;
        private String stemperature2;
        private String swind2;
        private String swinp2;

        @Override
        public void onSuccess(int statusCode, Header[] headers, String content) {
            super.onSuccess(statusCode, headers, content);
            if (statusCode == 200) {
                Log.i("我呜呜呜呜", "eee");
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");//得到名字为result数组的一个jsonarray数组列表
                    JSONObject result = (JSONObject) jsonArray.opt(0);  //  得到jsonArray的第0列
                    //今天的天气
                    scitynm = (String) result.get("citynm");
                    sdays = (String) result.get("days");
                    sweather = (String) result.get("weather");
                    stemperature = (String) result.get("temperature");
                    swind = (String) result.get("wind");
                    swinp = (String) result.get("winp");

                    citynm.setText("城市：" + scitynm);
                    days.setText("日期：" + sdays);
                    weather.setText("天气：" + sweather);
                    temperature.setText("温度：" + stemperature);
                    wind.setText("风向：" + swind);
                    winp.setText("风力：" + swinp);

                    //明天的天气
                    JSONObject result2 = (JSONObject) jsonArray.opt(1);
                    scitynm2 = (String) result2.get("citynm");
                    sdays2 = (String) result2.get("days");
                    sweather2 = (String) result2.get("weather");
                    stemperature2 = (String) result2.get("temperature");
                    swind2 = (String) result2.get("wind");
                    swinp2 = (String) result2.get("winp");

                    citynm2.setText("城市：" + scitynm2);
                    days2.setText("日期：" + sdays2);
                    weather2.setText("天气：" + sweather2);
                    temperature2.setText("温度：" + stemperature2);
                    wind2.setText("风向：" + swind2);
                    winp2.setText("风力：" + swinp2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Toast.makeText(JsonWeatherActivity.this, "请求成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable error, String content) {
            super.onFailure(statusCode, headers, error, content);
            Toast.makeText(JsonWeatherActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
        }
    }
    public void go(View v) {
        while (true) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    AsyncHttpClient client = new AsyncHttpClient();
                    //String url = new String("http://api.k780.com:88/?app=weather.future&weaid="+text+"&&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json");

                    //动态查询城市时使用
                    String text = editext.getText().toString();
//                    String url = new String("http://api.k780.com:88/?app=weather.future&weaid=" + text + "&&appkey=18178&sign=4c4e1d2106b3952839dfa17fdaf193d6&format=json");
                    String url = new String("http://api.k780.com:88/?app=weather.future&weaid=" +text +"&&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json");
//                    //app=weather.future&weaid=1&&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json
                    //静态查询城市时使用  http://api.k780.com:88/?app=weather.future&weaid=深圳&&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json

                    // String url = new String("http://api.k780.com:88/?app=weather.future&weaid=深圳&&appkey=18178&sign=4c4e1d2106b3952839dfa17fdaf193d6&format=json");

                    //访问本地文件地址
                    //String url = new String("http://192.168.3.42/Aday10Network/wea.jsp");
                    client.get(url, new myhandler());
                    try {
                        Thread.sleep(10000);
                        Log.i("hahahahahah", "" + new Date());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
