package com.example.fitnesstraining;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Objects;

public class TrainingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        LinearLayout container = findViewById(R.id.container);

        container.addView(
                generateView("Push-ups",
                        "\tAllow you to pump the chest muscles, triceps and deltoid muscles. To perform, take the emphasis lying down, place your hands slightly wider than your shoulders. Keep an eye on the palms, they should be located parallel to each other. Gently bend your elbows, going down, and return to the starting position. Keep the body straight.\n\n\tTo simplify push-ups or increase the number of repetitions, rest your knees on the floor.",
                        R.drawable.otzhym)
        );

        container.addView(
                generateView("Reverse push-ups",
                        "The exercise helps to work out the pectoral muscles, triceps and shoulder muscles. First of all, choose a suitable support (for example, a stable chair). Turn your back to her, rest your palms and start doing push-ups. The legs can be bent at the knees or straightened. When performing, try to go lower, but do not overdo it, so as not to get injured.",
                        R.drawable.ob0tzhym)
        );

        container.addView(
                generateView("Swing dumbbells in the standing position",
                        "Stand up straight, grab the dumbbells and spread your arms out to the sides until they are parallel to the floor. The elbow joints are slightly bent, the shoulders should not rise when performing movements. Keep the body straight, make swings calmly, avoiding sudden movements.",
                        R.drawable.max)
        );

        container.addView(
                generateView("Breeding dumbbells in a tilt",
                        "Allows you to work out the deltoid muscles. Take the dumbbells and lean forward, slightly bending your knees. The back is straight. Now slightly bend the elbow joints and spread your arms to the sides. During the movement, use the shoulder blades. Return to the starting position, straighten up.",
                        R.drawable.naklone)
        );
    }

    private View generateView(String title, String desc, int resId){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(20, 20, 20, 20);

        CardView cardView = new CardView(this);
        cardView.setLayoutParams(layoutParams);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);


        TextView titleView = new TextView(this);
        titleView.setTextSize(24);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setLayoutParams(layoutParams);
        titleView.setText(title);
        linearLayout.addView(titleView);

        TextView descView = new TextView(this);
        descView.setTextSize(18);
        descView.setLayoutParams(layoutParams);
        descView.setText(desc);
        linearLayout.addView(descView);

        ImageView img = new ImageView(this);
        img.setImageResource(resId);
        img.setLayoutParams(layoutParams);
        img.setAdjustViewBounds(true);
        linearLayout.addView(img);

        cardView.addView(linearLayout);
        return cardView;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}