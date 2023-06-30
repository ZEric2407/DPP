package com.example.dpp;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DebtChartMarker extends MarkerView {
    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    TextView displayText;
    public DebtChartMarker(Context context, int layoutResource) {
        super(context, layoutResource);
        displayText = findViewById(R.id.marker);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CANADA);
        String date = sdf.format(e.getX() * 1000f);

        String text = "Debt: $" + AccountMainFragment.df.format(e.getY()) + "\nDate: " + date;
        displayText.setText(text);

        setChartView(getChartView());
        super.refreshContent(e, highlight);
    }

    @Override
    public void draw(Canvas canvas, float posX, float posY) {
        MPPointF offset = getOffsetForDrawingAtPoint(posX, posY);
        int saveID = canvas.save();
        if (posX < canvas.getWidth() * 2 / 3f){
            canvas.translate(posX + offset.x, posY + offset.y);
        } else {
            canvas.translate(posX - offset.x - 300, posY + offset.y);
        }
        draw(canvas);
        canvas.restoreToCount(saveID);
    }
}
