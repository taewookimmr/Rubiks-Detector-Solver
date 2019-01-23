package org.height185.rubiksdetector.renderer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.height185.rubiksdetector.R;

import java.util.ArrayList;

public class CubeView2D extends View {

    public int surfaceColor[] = new int[6*9];
    public Point position[]   = new Point [6*9];
    public Button button[]    = new Button[6*9];

    public int size = 65;
    public int interval = size + 10;

    private Paint paint;
    private RectF rectF;


    public CubeView2D(Context context, int surfaceColor[]) {
        super(context);

        for(int i = 0; i < 6*9; i++){
            this.surfaceColor[i] = surfaceColor[i];
            this.position[i] = new Point();
            this.button[i] =  new Button(this.getContext());
        }


        this.position[4].x = 400;
        this.position[4].y = 900;

        this.position[0].x = this.position[4].x  - interval;
        this.position[0].y = this.position[4].y  - interval;

        this.position[1].x = this.position[4].x;
        this.position[1].y = this.position[4].y  - interval;

        this.position[2].x = this.position[4].x  + interval;
        this.position[2].y = this.position[4].y  - interval;

        this.position[3].x = this.position[4].x  - interval;
        this.position[3].y = this.position[4].y ;

        this.position[5].x = this.position[4].x  + interval;
        this.position[5].y = this.position[4].y ;

        this.position[6].x = this.position[4].x  - interval;
        this.position[6].y = this.position[4].y  + interval;

        this.position[7].x = this.position[4].x;
        this.position[7].y = this.position[4].y  + interval;

        this.position[8].x = this.position[4].x  + interval;
        this.position[8].y = this.position[4].y  + interval;


        for(int i = 0; i < 9; i++){
            this.position[1*9 + i].x = this.position[i].x + interval*3*2;
            this.position[1*9 + i].y = this.position[i].y;

            this.position[2*9 + i].x = this.position[i].x + interval*3*1;
            this.position[2*9 + i].y = this.position[i].y;

            this.position[3*9 + i].x = this.position[i].x - interval*3*1;
            this.position[3*9 + i].y = this.position[i].y;

            this.position[4*9 + i].x = this.position[i].x;
            this.position[4*9 + i].y = this.position[i].y - interval*3*1;

            this.position[5*9 + i].x = this.position[i].x;
            this.position[5*9 + i].y = this.position[i].y + interval*3*1;

        }

        paint = new Paint();
        rectF = new RectF();
    }


    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for(int i = 0; i <6*9; i++){
            drawSubSurface(canvas, paint, rectF, i);
        }

    }


    public void drawSubSurface(Canvas canvas, Paint paint, RectF rectF, int index){
        paint.setStyle(Paint.Style.FILL);
        rectF.set(position[index].x, position[index].y, position[index].x+size, position[index].y+size);
        int color =  -1;
        switch (surfaceColor[index]){
            case 0: color= R.color.BLUE; break;
            case 1: color= R.color.GREEN; break;
            case 2: color= R.color.RED; break;
            case 3: color= R.color.ORANGE; break;
            case 4: color= R.color.YELLOW; break;
            case 5: color= R.color.WHITE;  break;
            default: color=R.color.BLACK; break;
        }
        paint.setColor(getResources().getColor(color));

        canvas.drawRect(rectF, paint);

        button[index].setX(position[index].x);
        button[index].setY(position[index].y);
        button[index].setWidth(size*2);
        button[index].setHeight(size*2);
        button[index].setBackgroundResource(R.color.RED);
        button[index].setVisibility(VISIBLE);
    }


}
