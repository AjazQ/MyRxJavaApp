package com.aqsoft.myrxjavaapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class HosDayView extends View {

    Paint mPaint;
    Paint mBoxStrokeColor;
    Paint mBoxStatusColor;
    Paint mRedStatusColor;
    Paint mBoxColor;
    Paint mBoxAlternateColor;
    TextPaint mTextPaint=new TextPaint();
    Rect mRect;
    int mSquareColor;

    public HosDayView(Context context) {
        super(context);
        init(null);
    }

    public HosDayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public HosDayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public HosDayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(@Nullable AttributeSet set){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBoxColor= new Paint(Paint.ANTI_ALIAS_FLAG);
        mBoxAlternateColor= new Paint(Paint.ANTI_ALIAS_FLAG);
        mBoxStrokeColor= new Paint(Paint.ANTI_ALIAS_FLAG);
        mBoxStatusColor= new Paint(Paint.ANTI_ALIAS_FLAG);
        mRedStatusColor= new Paint(Paint.ANTI_ALIAS_FLAG);
        mRect = new Rect();

        if(set == null){
            return;
        }

        TypedArray ta = getContext().obtainStyledAttributes(set, R.styleable.HosDayView);
        mSquareColor = ta.getColor(R.styleable.HosDayView_square_color, Color.WHITE);
        mPaint.setColor(mSquareColor);
        mBoxColor.setColor(0xFFCCCCCC);
        mBoxColor.setStyle(Paint.Style.FILL_AND_STROKE);

        mBoxAlternateColor.setColor(Color.WHITE);
        mBoxAlternateColor.setStyle(Paint.Style.FILL_AND_STROKE);

        mBoxStrokeColor.setColor(0xFF333333);
        mBoxStrokeColor.setStyle(Paint.Style.STROKE);

        mBoxStatusColor.setColor(Color.WHITE);
        mBoxStatusColor.setStyle(Paint.Style.FILL);

        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(8 * getResources().getDisplayMetrics().density);
        mTextPaint.setColor(0xFF000000);

        mRedStatusColor.setStyle(Paint.Style.FILL);
        mRedStatusColor.setColor(0xFFFC0000);
        ta.recycle();
        loadSampleData();
    }

    private void loadSampleData() {
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w=getWidth(), h=getHeight();
        float headingColumn=w/26;
        float boxWidth= w/26;
        float x= headingColumn;
        mRect.left = 0;
        mRect.right = getWidth();
        mRect.top = 0;
        mRect.bottom = getHeight();
        GridCell gridCell =new GridCell(canvas, 0,0,headingColumn,headingColumn,"ST",mBoxColor, mTextPaint,mBoxStrokeColor,false);
        gridCell.draw();
        canvas.drawRect(mRect, mPaint);
        drawGrid(canvas, x,0,w-headingColumn*2,headingColumn,24,boxWidth,true,true,true);
        gridCell =new GridCell(canvas, x+w-headingColumn*2,0,headingColumn,headingColumn,"T",mBoxColor, mTextPaint,mBoxStrokeColor,false);
        gridCell.draw();

        //drawGrid(canvas, x,0,w,headingColumn,24);
         boxWidth = w/(26);
        drawGrid(canvas, 0,boxWidth,w,headingColumn,26,boxWidth,false,false,true);
        drawGrid(canvas, 0,boxWidth*2,w,headingColumn,26,boxWidth,false,false,true);
        drawGrid(canvas, 0,boxWidth*3,w,headingColumn,26,boxWidth,false,false,true);
        gridCell =new GridCell(canvas, 0,boxWidth*1,headingColumn,headingColumn,"C",mBoxColor, mTextPaint,mBoxStrokeColor,false);
        gridCell.draw();
        (new GridCell(canvas, 0,boxWidth*2,headingColumn,headingColumn,"NC",mBoxColor, mTextPaint,mBoxStrokeColor,false)).draw();
        (new GridCell(canvas, 0,boxWidth*3,headingColumn,headingColumn,"CM",mBoxColor, mTextPaint,mBoxStrokeColor,false)).draw();
        for(int i=0;i<9;i++) {
            gridCell=new GridCell(canvas, (boxWidth*i)+boxWidth,boxWidth*1,headingColumn,headingColumn,"",mBoxAlternateColor, mTextPaint,mBoxStrokeColor,true);
            gridCell.statusPaint=mRedStatusColor;
            if(i==8){
                gridCell.fillPercent=50;
            }else{
                gridCell.fillPercent=100;
            }
            gridCell.draw();
        }
    }
    private void drawGridLine(Canvas canvas,float x, float y, float width, float height, int numberOfColumns){
        float boxWidth = width/(numberOfColumns);
        float _x= x;
        for(int i = 0; i < numberOfColumns; i++){
            GridCell gridCell =new GridCell(canvas,_x, y, boxWidth, height,String.valueOf(i+1),mBoxAlternateColor, mTextPaint,mBoxStrokeColor,true);
            gridCell.statusPaint=mBoxStatusColor;
            gridCell.draw();
            _x+=boxWidth;
        }

    }
    private void drawGrid(Canvas canvas,float x, float y, float width, float height, int numberOfColumns, float boxWidth,boolean showAlternateColor, boolean showAutoNumber,boolean showGrid){
        //float boxWidth = width/(numberOfColumns);
        mPaint.setStyle(Paint.Style.STROKE);
        int offset = 10;
        //canvas.drawRect(x,y,width,height, mBoxColor);
        //mBoxColor.setStyle(Paint.Style.FILL);
        float _x= x;
        for(int i = 0; i < numberOfColumns; i++){
            GridCell gridCell =new GridCell(canvas,_x, y, boxWidth, height,showAutoNumber?String.valueOf(i):"",showAlternateColor?(i % 2 ==0? mBoxColor:mBoxAlternateColor):mBoxAlternateColor, mTextPaint,mBoxStrokeColor,showGrid);
            gridCell.draw();
            _x+=boxWidth;
        }
    }
    private class Grid{
        ArrayList<GridRow> rows=new ArrayList<>();

        public void setRows(ArrayList<GridRow> rows) {
            this.rows = rows;
        }

        public ArrayList<GridRow> getRows() {
            return rows;
        }
        public void Grid(){
            
        }
    }
    private class GridRow{
        private ArrayList<GridCell> cells=new ArrayList<GridCell>();

        public void setCells(ArrayList<GridCell> cells) {
            this.cells = cells;
        }

        public ArrayList<GridCell> getCells() {
            return cells;
        }
        public void GridRow(){}
    }
    private class GridCell {
        public Canvas canvas;
        public float x=0.0f;
        public float y=0.0f;
        public float w=0.0f;
        public float h=0.0f;
        public String text="";
        public Paint paint;
        public TextPaint textPaint;
        public Paint strokePaint;
        public Paint statusPaint;
        public boolean showGrid=true;

        public int fillPercent=-1;
        public GridCell(Canvas canvas, float x, float y, float w, float h, String text, Paint paint , TextPaint textPaint, Paint strokePaint, boolean showGrid){
            this.canvas=canvas;
            this.x=x;
            this.y=y;
            this.h=h;
            this.w=w;
            this.paint=paint;
            this.text=text;
            this.textPaint=textPaint;
            this.showGrid=showGrid;
            this.strokePaint=strokePaint;

        }
        public void draw(){
            canvas.drawRect(x,y ,x+w,y+h, paint);
            canvas.drawRect(x,y ,x+w,y+h, strokePaint);
            if(fillPercent>=0 && statusPaint!=null){
                float fillPercentWidth = w * fillPercent/100;
                canvas.drawRect(x,y ,x+fillPercentWidth ,y+h, statusPaint);
            }

            if(!text.equals("")){
                Rect bounds = new Rect();
                textPaint.getTextBounds(text, 0, 1, bounds);
                float textWidth = textPaint.measureText(text);
                float textHeight = bounds.height();
                float textCol= x + (w/2) -(textWidth/2);
                canvas.drawText(text,textCol,y+textHeight/2+h/2,textPaint);
            }
            if(showGrid) {
                float quarterWidth = w * 0.25f;
                float quarterHeight = h * 0.15f;
                canvas.drawLine(x + quarterWidth * 2, y + h - quarterHeight, x + quarterWidth * 2, y + h, strokePaint);
                canvas.drawLine(x + quarterWidth * 1, y + h - quarterHeight / 2, x + quarterWidth * 1, y + h, strokePaint);
                canvas.drawLine(x + quarterWidth * 3, y + h - quarterHeight / 2, x + quarterWidth * 3, y + h, strokePaint);
            }
        }

    }

}
