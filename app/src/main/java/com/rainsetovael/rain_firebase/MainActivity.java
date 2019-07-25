package com.rainsetovael.rain_firebase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Path;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DatabaseReference reference;

    ValueLibrary _valueLibrary;
    DateController _dateController;

    final String saveFileName="list.txt";
    String loadText;

    Button setDateButton;

    boolean isSetUped=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUp();
        for(int i=0;i<_valueLibrary.list.size();i++){
            System.out.println(_valueLibrary.list.get(i));

        }
        //reference.orderByChild("").addChildEventListener() orderByChildを入れて消す

        reference.addChildEventListener(new ChildEventListener() {
            int count=0;
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(isSetUped) {
                    System.out.println(isSetUped);
                    System.out.println(dataSnapshot.getKey());
                    String key=dataSnapshot.getKey();
                    _valueLibrary.list.add(key);
                    _valueLibrary.maxYear=_dateController.searchMaxYear();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        setDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSetUped) {
                    System.out.println(_valueLibrary.maxYear);
                    Intent intent = new Intent(getApplication(), DateSetActivity.class);
                    startActivity(intent);
                }else{

                }
            }
        });
    }



    void setUp(){
        isSetUped=false;
        _dateController=new DateController();
        _valueLibrary.list=new ArrayList<String>();
        reference=FirebaseDatabase.getInstance().getReference("D-class");
        _valueLibrary=new ValueLibrary();
        _valueLibrary.list=new ArrayList<String>();
        setDateButton=(Button)findViewById(R.id.button);
        setDateButton.setEnabled(false);
        createFile();
        clearFile();
        loadText="";
        reference.addValueEventListener(new ValueEventListener() {
            int count=0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    count++;
                }
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!isSetUped) {
                            _valueLibrary._environmentInfo = new EnvironmentInfo[count];
                            for (int i = 0; i < count; i++) {
                                _valueLibrary._environmentInfo[i] = new EnvironmentInfo();
                            }
                            count = 0;
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String date = snapshot.getKey();
                                System.out.println(date);
                                _valueLibrary.list.add(date);
                                float temp = Float.parseFloat(String.valueOf(snapshot.child("temp").getValue()));
                                float humi = Float.parseFloat(String.valueOf(snapshot.child("humi").getValue()));
                                String[] splitText=new String[6];
                                splitText=snapshot.getKey().split(" ");
                                String box=splitText[0]+" ";
                                box+=splitText[1]+" ";
                                box+=splitText[2];
                                _valueLibrary._environmentInfo[count].date = box;
                                _valueLibrary._environmentInfo[count].temp = temp;
                                _valueLibrary._environmentInfo[count].humi = humi;
                                //System.out.println(_environmentInfo[count].date);
                                //System.out.println(_environmentInfo[count].temp);
                                //System.out.println(_environmentInfo[count].humi);
                                count++;
                            }
                            _valueLibrary.maxYear = _dateController.searchMaxYear();
                            _valueLibrary.minYear = _dateController.searchMinYear();
                            System.out.println(_dateController.searchMaxYear());
                            isSetUped = true;
                            setDateButton.setEnabled(true);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    void createFile(){
        File file=new File(getFilesDir()+"/"+saveFileName);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("cant create");
                e.printStackTrace();
            }
        }
    }
    void saveFile(String str){
        try {
            FileOutputStream fos=openFileOutput(saveFileName,MODE_APPEND);
            OutputStreamWriter osw=new OutputStreamWriter(fos);
            BufferedWriter writer=new BufferedWriter(osw);
            writer.write(str);
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("cant save");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("cant save");
            e.printStackTrace();
        }
    }
    void clearFile(){
        try {
            FileOutputStream fos = openFileOutput(saveFileName,MODE_PRIVATE);
            OutputStreamWriter osw=new OutputStreamWriter(fos);
            BufferedWriter writer=new BufferedWriter(osw);
            writer.write("");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    String readFile(){
        String txt="";
        try {
            FileInputStream fis=openFileInput(saveFileName);
            BufferedReader reader=new BufferedReader(new InputStreamReader(fis,"UTF-8"));
            String lineBuffer="";
            while((lineBuffer=reader.readLine())!=null){
                txt=lineBuffer;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return txt;
    }

    int countListNumber(){
        int count=0;
        for(int i=0;i<_valueLibrary.list.size();i++){
            count++;
        }
        return count;
    }
}

