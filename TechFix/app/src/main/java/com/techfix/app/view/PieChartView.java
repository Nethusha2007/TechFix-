package com.techfix.app.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.content.ContextCompat;
import com.techfix.app.R;

public class PieChartView extends View {

    private float[] values = {1f};
    private int[] colors = {0xFFE5E7EB};
    private String centerValue = "";
    private String centerLabel = "";

    private final Paint slicePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint holePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint valuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF bounds = new RectF();

    public PieChartView(Context context) {
        super(context);
        init();
    }

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        slicePaint.setStyle(Paint.Style.FILL);
        holePaint.setColor(ContextCompat.getColor(getContext(), R.color.card));
        valuePaint.setColor(ContextCompat.getColor(getContext(), R.color.textPrimary));
        valuePaint.setTextAlign(Paint.Align.CENTER);
        valuePaint.setFakeBoldText(true);
        labelPaint.setColor(ContextCompat.getColor(getContext(), R.color.textSecondary));
        labelPaint.setTextAlign(Paint.Align.CENTER);
    }
 
    public void setData(float[] values, int[] colors) {
        if (values != null && values.length > 0 && colors != null && colors.length > 0) {
            this.values = values;
            this.colors = colors;
        }
        invalidate();
    }

    public void setCenterText(String value, String label) {
        this.centerValue = value == null ? "" : value;
        this.centerLabel = label == null ? "" : label;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float w = getWidth();
        float h = getHeight();
        float size = Math.min(w, h);
        float cx = w / 2f;
        float cy = h / 2f;
        float radius = size / 2f - dp(4);
        bounds.set(cx - radius, cy - radius, cx + radius, cy + radius);

        float total = 0f;
        for (float v : values) total += v;
        if (total <= 0f) total = 1f;

        float start = -90f;
        for (int i = 0; i < values.length; i++) {
            float sweep = 360f * (values[i] / total);
            slicePaint.setColor(colors[i % colors.length]);
            canvas.drawArc(bounds, start, sweep, true, slicePaint);
            start += sweep;
        }

        float holeRadius = radius * 0.60f;
        canvas.drawCircle(cx, cy, holeRadius, holePaint);

        if (!centerValue.isEmpty()) {
            valuePaint.setTextSize(sp(22));
            canvas.drawText(centerValue, cx, cy - sp(2), valuePaint);
        }
        if (!centerLabel.isEmpty()) {
            labelPaint.setTextSize(sp(11));
            canvas.drawText(centerLabel, cx, cy + sp(15), labelPaint);
        }
    }

    private float dp(float value) {
        return value * getResources().getDisplayMetrics().density;
    }

    private float sp(float value) {
        return value * getResources().getDisplayMetrics().scaledDensity;
    }
}
