
package com.jarvis.assistant;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    private int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }

    private GradientDrawable shape(int color, int radius) {
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(color);
        bg.setCornerRadius(dp(radius));
        return bg;
    }

    private TextView text(String value, int size, int color) {
        TextView view = new TextView(this);
        view.setText(value);
        view.setTextSize(size);
        view.setTextColor(color);
        view.setGravity(Gravity.CENTER);
        return view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(Color.rgb(5, 12, 28));
        getWindow().setNavigationBarColor(Color.rgb(5, 12, 28));

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(dp(24), dp(24), dp(24), dp(24));

        GradientDrawable background = new GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            new int[] {
                Color.rgb(5, 12, 28),
                Color.rgb(12, 28, 55),
                Color.rgb(3, 8, 20)
            }
        );
        root.setBackground(background);

        TextView title = text("J A R V I S", 30, Color.WHITE);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        root.addView(title);

        TextView subtitle = text(
            "YOUR PERSONAL AI ASSISTANT",
            11,
            Color.rgb(120, 180, 255)
        );
        subtitle.setPadding(0, dp(8), 0, dp(55));
        root.addView(subtitle);

        TextView orb = text("J", 58, Color.WHITE);
        orb.setTypeface(Typeface.DEFAULT_BOLD);

        GradientDrawable circle = new GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            new int[] {
                Color.rgb(30, 130, 255),
                Color.rgb(10, 45, 110)
            }
        );
        circle.setShape(GradientDrawable.OVAL);
        circle.setStroke(dp(3), Color.rgb(90, 190, 255));
        orb.setBackground(circle);

        LinearLayout.LayoutParams orbParams =
            new LinearLayout.LayoutParams(dp(190), dp(190));
        orbParams.gravity = Gravity.CENTER;
        root.addView(orb, orbParams);

        TextView status = text(
            "JARVIS IS READY",
            17,
            Color.WHITE
        );
        status.setPadding(0, dp(45), 0, dp(10));
        root.addView(status);

        TextView instruction = text(
            "Your assistant is waiting for you",
            13,
            Color.rgb(155, 175, 205)
        );
        root.addView(instruction);

        setContentView(root);
    }
}
