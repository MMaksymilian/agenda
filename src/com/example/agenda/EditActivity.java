package com.example.agenda;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created with IntelliJ IDEA.
 * User: Maksymilian
 * Date: 21.01.13
 * Time: 12:18
 * To change this template use File | Settings | File Templates.
 */
public class EditActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dates);
    }

    /*TODO NAPISAĆ IMPLEMENTACJĘ I ZASTOWAĆ DO EDYCJI DATY WYBRANEJ PRZEZ UŻYTKOWNIKA*/
    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("returnKey", "return value for some kind of Query");
        setResult(RESULT_OK, data);
        super.finish();
    }
}
