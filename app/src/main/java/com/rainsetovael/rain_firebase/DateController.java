package com.rainsetovael.rain_firebase;

import java.util.ArrayList;
import java.util.List;

public class DateController {
    int searchMaxYear(){
        int maxYear=0;
        int box=0;
        ValueLibrary _valueLibrary=new ValueLibrary();
        List<String> list= new ArrayList<>();
        list=_valueLibrary.list;
        String[] splitText=new String[6];
        splitText=list.get(0).split(" ");
        maxYear=Integer.parseInt(splitText[0]);
        for(int i=1;i<_valueLibrary.list.size();i++){
            splitText=list.get(i).split(" ");
            box=Integer.parseInt(splitText[0]);
            if(box>maxYear){
                maxYear=box;
            }
        }
        return maxYear;
    }


    int searchMinYear(){
        int minYear=0;
        int box=0;
        ValueLibrary _valueLibrary=new ValueLibrary();
        List<String> list=new ArrayList<>();
        list=_valueLibrary.list;
        String[] splitText=new String[6];
        splitText=_valueLibrary.list.get(0).split(" ");
        minYear=Integer.parseInt(splitText[0]);
        for(int i=0;i<list.size();i++){
            splitText=_valueLibrary.list.get(i).split(" ");
            box=Integer.parseInt(splitText[0]);
            if(box<minYear){
                minYear=box;
            }
        }
        return minYear;
    }
    public DateController(){

    }
}
