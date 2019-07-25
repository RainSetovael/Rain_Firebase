package com.rainsetovael.rain_firebase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class GraphMakeActivity extends AppCompatActivity {

    ValueLibrary _valueLibrary;
    List<Float> temp,humi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_make);

        setUp();
    }

    void setUp(){
        _valueLibrary=new ValueLibrary();
        String setDate=_valueLibrary.setDate;
        temp=new ArrayList<>();
        humi=new ArrayList<>();
        for(int i=0;i<_valueLibrary._environmentInfo.length;i++){
            if(_valueLibrary._environmentInfo[i].date.equals(setDate)){
                temp.add(_valueLibrary._environmentInfo[i].temp);
                humi.add(_valueLibrary._environmentInfo[i].humi);
                System.out.println(_valueLibrary._environmentInfo[i].temp);
            }
        }
    }
}
