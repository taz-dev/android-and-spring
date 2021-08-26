package com.pjh.ex_springtest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {

    EditText edit_id, edit_pwd;
    Button btn_regi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edit_id = findViewById(R.id.edit_id);
        edit_pwd = findViewById(R.id.edit_pwd);
        btn_regi = findViewById(R.id.btn_regi);

        btn_regi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = edit_id.getText().toString();
                String pwd = edit_pwd.getText().toString();
                String result = "id="+id+"&pwd="+pwd; //서버로 전달하기 위한 파라미터

                new Task().execute(result, "type_regi");

            }
        });

    }//onCreate()

    //서버통신용 Async클래스
    class Task extends AsyncTask<String, Void, String>{

        public String ip = "172.30.1.254"; //내 pc 기본 게이트웨이
        String sendMsg, receiveMsg;
        String serverip = "http://"+ip+":9090/and/regi.do"; //spring으로 연결할 주소

        @Override
        protected String doInBackground(String... strings) {

            String str = "";

            try {

                URL url = new URL(serverip);

                //위의 경로로 서버접속
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST"); //반드시 POST방식으로 전달
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

                //osw라는 아웃풋 스트림을 통해 스프링 서버로 파라미터를 전달
                //regi.do?id=jh&pwd=1111&type=type_regi
                sendMsg = strings[0] + "&type=" + strings[1]; //strings[0] -> result

                //실제로 서버에 값을 전송
                osw.write(sendMsg);
                osw.flush(); //물리적으로 기록을 완료했음을 명시하는 메서드

                //서버통신이 완료되면 결과를 얻기위한 영역
                //conn.getResponseCode() : 200이면 정상
                //conn.getResponseCode() : 404, 500등... 비정상
                if( conn.getResponseCode() == conn.HTTP_OK ){

                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    
                    //서버에서 넘겨준 JSON형식의 데이터를 받는다
                    //{res:[{'result':'success'}]}
                    receiveMsg = reader.readLine();
                    Log.i("MY", receiveMsg);

                    //JSON형태의 배열을 받는 클래스
                    JSONArray jarray = new JSONObject(receiveMsg).getJSONArray("res");

                    //jarray : [{'result':'success'}]
                    JSONObject jObject = jarray.getJSONObject(0); //jarray의 0번 방

                    //jObject : {'result':'success'}
                    str = jObject.optString("result");

                }
                
            }catch (Exception e){

            }

            return str;
        }

        @Override
        protected void onPostExecute(String s) {

            if( s.equalsIgnoreCase("success") ){
                Toast.makeText(RegisterActivity.this, "Register Success!", Toast.LENGTH_SHORT).show();
            
            }else{
                Toast.makeText(RegisterActivity.this, "Duplicate ID", Toast.LENGTH_SHORT).show();
            }

        }

    }//Task

}