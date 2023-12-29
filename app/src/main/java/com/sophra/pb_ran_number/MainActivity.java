package com.sophra.pb_ran_number;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.TypedArrayUtils;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewManager;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private AdView adView;
    int num1 = 1; //첫 숫자
    int num2 = 10; //나중 숫자
    int num3 = 0;  //숫자 개수
    int temp = 0;  //첫 숫자 저장용
    int y = 0; // 뽑힌 숫자 돌리기 위해 있는 변수
    boolean overlap = false;  //중복 사용
    boolean isfinish = false;

    String minnb = "";
    String maxnb = "";

    String[] numseo = new String[0];         //전체 배열
    final ArrayList<String> numse = new ArrayList<>(Arrays.asList(numseo));

    String[] pick_number = new String[0];    //뽑힌 숫자 저장용 배열
    final ArrayList<String> pick_numbers = new ArrayList<>(Arrays.asList(pick_number));

    AdRequest request;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //final TextView test_txt = findViewById(R.id.test_txt); //테스트 코드

        request = new AdRequest.Builder().build();

        final TextView mNum_txt = findViewById(R.id.mNum_txt);               //메인 숫자 텍스트
        final TextView picked_num_txt = findViewById(R.id.picked_num_txt);   //뽑힌 숫자들 텍스트
        picked_num_txt.setMovementMethod(new ScrollingMovementMethod());
        Button btn_pick = findViewById(R.id.btn_pick);                       //뽑기 버튼
        Button btn_add = findViewById(R.id.btn_add);                         //추가 설정 버튼

        final SharedPreferences sp = getSharedPreferences("sp", MODE_PRIVATE);

        String main = sp.getString("main", "");
        final String min = sp.getString("min", "1");
        final String max = sp.getString("max", "10");
        final Boolean over = sp.getBoolean("over",false);

        num1 = Integer.parseInt(min);
        num2 = Integer.parseInt(max);

        minnb = min;
        maxnb = max;
        overlap = over;

        Main();

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog);


        TextView quit = dialog.findViewById(R.id.dialog_button_quit);
        TextView cancel = dialog.findViewById(R.id.dialog_button_quit_cancel);

        AdView adView = dialog.findViewById(R.id.dialog_native_ad);
        AdRequest request = new AdRequest.Builder().build();
        adView.loadAd(request);

        Log.v("test_ad", "" + request);

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        //test_txt.setText(""+ Arrays.toString(numbers));
        //test_txt.setText(""+ numse); //테스트 코드
        picked_num_txt.setText("");  // 뽑힌 숫자들 텍스트

        btn_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                     //뽑기 버튼 눌렀을때

                int num5 = (int)((Math.random()*numse.size()));  //num5는 배열의 크기에서 랜덤으로 정해진 수

                if (isfinish == true){
                    Toast.makeText(MainActivity.this, "초기화 해주세요", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(overlap == true)                             //중복이 허용 될 때
                    {
                        mNum_txt.setText(""+ numse.get(num5));
                        pick_numbers.add(numse.get(num5));
                        picked_num_txt.setText(picked_num_txt.getText() + "    " + pick_numbers.get(y));
                        y++;
                    }
                    else{                                          //중복허용 안될 때
                        if(numse.size() <= 0)                  //배열안의 수가 다 뽑혔을 때
                        {
                            isfinish = true;
                            Toast.makeText(MainActivity.this, "모두 뽑았습니다", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            mNum_txt.setText(""+ numse.get(num5));
                            pick_numbers.add(numse.get(num5));
                            picked_num_txt.setText(picked_num_txt.getText() + "    " + pick_numbers.get(y));
                            numse.remove(num5);
                            //test_txt.setText(""+ numse);
                            y ++;
                        }
                    }
                }
            }
        });


        btn_add.setOnClickListener(new View.OnClickListener() {       // 더하기 모양 버튼 눌렀을때
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                LinearLayout ll = (LinearLayout)inflater.inflate(R.layout.act_config, null);
                ll.setBackgroundColor(Color.parseColor("#99000000"));
                LinearLayout.LayoutParams paramll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT);
                addContentView(ll, paramll);
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                final EditText et_min = findViewById(R.id.et_min);
                final EditText et_max = findViewById(R.id.et_max);
                final CheckBox cb_overlap = findViewById(R.id.cb_overlap);

                et_min.setText(minnb);
                et_max.setText(maxnb);
                if(overlap == true)
                {
                    cb_overlap.setChecked(true);
                }
                else
                {
                    cb_overlap.setChecked(false);
                }

                Button btn_celar = findViewById(R.id.btn_clear);

                btn_celar.setOnClickListener(new View.OnClickListener() {             //X 버튼 눌렸을 때
                    @Override
                    public void onClick(View view) {                  // X 버튼 눌렀을 때

                        //Toast.makeText(getApplicationContext(), "클리어 버튼 누름", Toast.LENGTH_SHORT).show();

                        num1 = Integer.parseInt(et_min.getText().toString());
                        num2 = Integer.parseInt(et_max.getText().toString());
                        minnb = et_min.getText().toString();
                        maxnb = et_max.getText().toString();
                        SharedPreferences.Editor editor = sp.edit();
                        if(cb_overlap.isChecked())
                        {
                            overlap = true;
                            editor.putBoolean("over",true);
                        }
                        else
                        {
                            overlap=false;
                            editor.putBoolean("over",false);
                        }
                        editor.putString("min",minnb);
                        editor.putString("max",maxnb);
                        editor.apply();

                        num1 = Integer.parseInt(minnb);
                        num2 = Integer.parseInt(maxnb);
                        numse.clear();
                        Main();

                        //Toast.makeText(getApplicationContext(), max, Toast.LENGTH_SHORT).show();

                        //editor.putString("min",minnb);
                        //editor.putString("max",maxnb);

                        LinearLayout ll = (LinearLayout)findViewById(R.id.configgg);
                        ((ViewManager) ll.getParent()).removeView(ll);

                    }
                });

                Button btn_reset = findViewById(R.id.btn_reset);

                btn_reset.setOnClickListener(new View.OnClickListener() {    //리셋 버튼 눌렀을 때
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(), "초기화 완료", Toast.LENGTH_SHORT).show();
                        isfinish = false;
                        picked_num_txt.setText("");
                        mNum_txt.setText("00");
                        Main();
                    }
                });
            }
        });
    }



    @Override
    public void onBackPressed() {

        dialog.show();
    }

    public void Main()
    {
        num3 = (num2 - num1) + 1;
        //final int numbers[] = new int[num3];

        temp = num1;

        for(int i = 0; i < num3; i++)
        {
            //numbers[i] = num4;
            numse.add(String.valueOf(temp));
            temp += 1;
        }
    }
}