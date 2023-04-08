package com.bskcoobe.game23gg;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;

public class CustomTextView extends AppCompatTextView {

    private float strokeWidth;
    private int strokeColor;
    private Paint.Join strokeJoin;
    private float strokeMiter;

    public CustomTextView(Context context) {
        super(context);
        init(null);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void init(AttributeSet attrs) {

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomTextView);

            if (a.hasValue(R.styleable.CustomTextView_strokeColor)) {
                strokeWidth = a.getDimensionPixelSize(R.styleable.CustomTextView_strokeWidth, 1);
                strokeColor = a.getColor(R.styleable.CustomTextView_strokeColor, Color.BLACK);
                strokeMiter = a.getDimensionPixelSize(R.styleable.CustomTextView_strokeMiter, 10);
                int joinStyle = a.getInt(R.styleable.CustomTextView_strokeJoinStyle, 0);
                switch (joinStyle) {
                    case 0:
                        strokeJoin = Paint.Join.MITER;
                        break;
                    case 1:
                        strokeJoin = Paint.Join.BEVEL;
                        break;
                    case 2:
                        strokeJoin = Paint.Join.ROUND;
                        break;
                }
                setStroke(strokeWidth, strokeColor, strokeJoin, strokeMiter);
            }
            a.recycle();
        }
    }

    public void setStroke(float width, int color, Paint.Join join, float miter) {
        strokeWidth = width;
        strokeColor = color;
        strokeJoin = join;
        strokeMiter = miter;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        TextPaint textPaint = getPaint();
        textPaint.setStyle(Paint.Style.FILL);

        // Draw the text
        super.onDraw(canvas);

        // Draw the stroke
        if (strokeWidth > 0) {
            textPaint.setStyle(Paint.Style.STROKE);
            textPaint.setStrokeJoin(strokeJoin);
            textPaint.setStrokeMiter(strokeMiter);
            int previousColor = getCurrentTextColor();
            setTextColor(strokeColor);
            textPaint.setStrokeWidth(strokeWidth);
            super.onDraw(canvas);
            setTextColor(previousColor);
            textPaint.setStyle(Paint.Style.FILL);
        }
    }
//    @Override
//    public void setText(CharSequence text, BufferType type) {
//        super.setText(text, type);
//        invalidate();
//    }
}

