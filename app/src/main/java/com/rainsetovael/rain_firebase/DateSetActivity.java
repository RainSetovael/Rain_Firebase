package com.rainsetovael.rain_firebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import java.util.ArrayList;
import java.util.List;

public class DateSetActivity extends AppCompatActivity {

    NumberPicker yearPicker,monthPicker,dayPicker;
    ValueLibrary _valueLibrary;
    Button createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_set);

        setUp();

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date=searchDate();
                if(date.equals("none")){
                    System.out.println("none");
                }else{
                    _valueLibrary.setDate=date;
                    System.out.println(date);
                    Intent intent=new Intent(getApplication(),GraphMakeActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    void setUp(){
        _valueLibrary=new ValueLibrary();
        createButton=findViewById(R.id.createGraphButton);
        yearPicker=(NumberPicker)findViewById(R.id.numberPicker_year);
        monthPicker=(NumberPicker)findViewById(R.id.numberPicker_month);
        dayPicker=(NumberPicker)findViewById(R.id.numberPicker_day);
        yearPicker.setMinValue(_valueLibrary.minYear);
        yearPicker.setMaxValue(_valueLibrary.maxYear);
        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        dayPicker.setMinValue(1);
        dayPicker.setMaxValue(31);
    }
    String searchDate(){
        List<String> list=new ArrayList<>();
        String sample=yearPicker.getValue()+" "+monthPicker.getValue()+" "+dayPicker.getValue();
        String date="";
        String result="none";
        list=_valueLibrary.list;
        String[] splitText=new String[6];
        for(int i=0;i<list.size();i++){
            date="";
            splitText=list.get(i).split(" ");
            for(int r=0;r<2;r++){
                date+=splitText[r]+" ";
            }
            date+=splitText[2];
            //System.out.println(date);
            if(date.equals(sample)){
                result=date;
                break;
            }
        }
        return result;
    }
}
