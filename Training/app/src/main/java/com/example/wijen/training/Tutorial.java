package com.example.wijen.training;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import com.archive.wijen.myaarlibrary.LibraryClass;

import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;
import me.toptas.fancyshowcase.FocusShape;

public class Tutorial extends AppCompatActivity {

    private FancyShowCaseQueue mQueue;
    private FancyShowCaseView mFancyView, mFancyView2;
    public void AAR(){
        LibraryClass libraryClass = new LibraryClass();
        libraryClass.printLog("This is message");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        Button buttonFocus = findViewById(R.id.buttonFocus);
        TextView txtFocus = findViewById(R.id.textView3);
        EditText passFocus = findViewById(R.id.txtPasswordFocus);
        RadioButton radiobtnFocus = findViewById(R.id.radiobtnFocus);
       final FancyShowCaseView firstFocus= new FancyShowCaseView.Builder(this)
                .focusOn(buttonFocus)
                .title("Hanya terfokus 1x")
                .showOnce("test")
                .build();

        final FancyShowCaseView secondFocus = new FancyShowCaseView.Builder(this)
                .focusOn(txtFocus)
                .title("TextViewed")
                //.showOnce("test")
                .build();
        final FancyShowCaseView thirdFocus = new FancyShowCaseView.Builder(this)
                .focusOn(passFocus)
                .title("Fill the password here !")
                //.showOnce("test")
                .build();
        final FancyShowCaseView fourthFocus = new FancyShowCaseView.Builder(this)
                .focusOn(radiobtnFocus)
                .focusShape(FocusShape.ROUNDED_RECTANGLE)
                .roundRectRadius(90)
                .title("Click on the button to agree with everything")
                .titleStyle(R.style.MyTitleStyle, Gravity.BOTTOM | Gravity.CENTER)
                //.showOnce("test")
                .build();

        mQueue = new FancyShowCaseQueue()
                    .add(firstFocus)
                    .add(secondFocus)
                    .add(thirdFocus)
                    .add(fourthFocus);
        mQueue.show();

        buttonFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AAR();
               // startActivity(new Intent(Tutorial.this, Tutorial.class));
                setContentView(com.archive.wijen.myaarlibrary.R.layout.layout);
            }
        });


    }


}
