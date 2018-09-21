package com.example.vijay.friends;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    EditText editName;
    SQLiteDatabase db;
    Button btnAdd, btnView, btnDelete, btnDeleteAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editName = (EditText) findViewById(R.id.e1);
        btnAdd = (Button) findViewById(R.id.b1);
        btnView = (Button) findViewById(R.id.b2);
        btnDelete = (Button) findViewById(R.id.b3);
        btnDeleteAll = (Button) findViewById(R.id.b4);
        btnAdd.setOnClickListener(this);
        btnView.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnDeleteAll.setOnClickListener(this);
        db = openOrCreateDatabase("FriendDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS friend(friendname VARCHAR);");
    }

    public void onClick(View view) {
        if (view == btnAdd) {
            if (editName.getText().toString().trim().length() == 0) {
                showMessage("Error", "Please enter a Name");
                return;
            }
            db.execSQL("INSERT INTO friend VALUES('" + editName.getText() + "' );");
            showMessage("Success", "Record added");
            clearText();
        }
        if (view == btnView) {
            Cursor c = db.rawQuery("SELECT * FROM friend", null);
            if (c.getCount() == 0) {
                showMessage("Error", "No records found");
                return;
            }
            StringBuffer buffer = new StringBuffer();
            while (c.moveToNext()) {
                buffer.append("Name: " + c.getString(0) + "\n");
            }
            showMessage("Friends", buffer.toString());
        }
        if(view==btnDelete) {
            if (editName.getText().toString().trim().length() == 0) {
                showMessage("Error", "Please enter a name");
                return;
            }

            Cursor c=db.rawQuery("SELECT * FROM friend WHERE friendname='"+editName.getText()+"'", null);
            if(c.moveToFirst())
            {
                db.execSQL("DELETE FROM friend WHERE friendname='"+ editName.getText()+"'");
                showMessage("Success", "Record Deleted");
            }
            else
            {
                showMessage("Error", "Invalid Employee id");
            }
            clearText();
        }
        if(view==btnDeleteAll)
        {
            db.delete("friend",null,null);
            showMessage("Success","All Records Deleted");
        }
    }


    public void showMessage(String title, String message) {
        Builder builder = new Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void clearText() {
        editName.setText("");
        editName.requestFocus();
    }
}
