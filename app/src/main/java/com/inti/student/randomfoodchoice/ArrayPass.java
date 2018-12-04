package com.inti.student.randomfoodchoice;

import android.os.Bundle;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by C on 11/7/2018.
 */

public class ArrayPass implements Serializable{

    private String items;

    public ArrayPass(String item){
        items = item;
    }

    public String getItem(){
        return items;
    }

}
