package org.height185.rubiksdetector.rubiksDetector;

import org.height185.rubiksdetector.typedef.BGR_SDATA;
import org.height185.rubiksdetector.typedef.Scalar;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;

import java.util.Vector;

public class SurfaceDetector {

    public SurfaceDetector(){

        surfaceColor    = new int[9]; // 검출된 데이터(0~5에 해당하는 색 9개)가 저장되는 곳
        isDetectionDone = new boolean[9];

        color_inner = new Scalar[9];
        for(int i = 0 ; i < 9; i++){
            color_inner[i] = new Scalar(0,0,0);
        }

        center_interest = new Point();
        rectangle_outter = new Rect();
        relativePosition = new int[9][2];
        centers_inner   = new Vector<>();
        rectangle_inner = new Vector<>();
    }

    public boolean isSaveButtonClicked = false;   // 해당 면의 검출 결과를 저장하는 버튼이 눌렸는지 확인

    public int surfaceColor[]; // 9, and initial value is -1
    public boolean isDetectionDone[]; // 9, and initial value is false
    public Scalar color_inner[]; // 9, and initial value is (0,0,0)
    public Scalar color_ref[] = {
            new Scalar(255, 0, 0), // blue
            new Scalar(0, 255, 0), // green
            new Scalar(0, 0, 255), // red
            new Scalar(0, 125, 255), // orange
            new Scalar(0, 255, 255), // yellow
            new Scalar(255, 255, 255), // white
            new Scalar(0, 0, 0) // black
    };

    public Point center_interest ;
    public Rect rectangle_outter ;
    public Vector<Point> centers_inner;
    public Vector<Rect> rectangle_inner;

    public int size_outter = 0;
    public int size_inner = 0;
    public int interval = 5;
    public int relativePosition[][]; // 9,2

    public float bgr_temp[] = new float[3]; //

    public float separate_ro = 0.035f;
    public float separate_gy = 0.000f;




    public void setSize_outter(int setValue){
        if (setValue > 4 * interval) {
            size_outter = setValue;
            rectangle_outter.x = (int)(center_interest.x - size_outter / 2);
            rectangle_outter.y = (int)(center_interest.y - size_outter / 2);
            rectangle_outter.width = size_outter;
            rectangle_outter.height = size_outter;

            setSize_inner();
        }
        else {
            // std::cout << "size_outter는 " << 4 * interval << "보다 커야 합니다" << std::endl;
            // std::cout << "size_outter를 다시 설정해 주세요";
        }

    }

    public void setSize_inner() {
        size_inner = (size_outter - 4 * interval) / 3;
        if (size_inner > 0) {
            setRelativePosition();
        }
        else {
            // std::cout << "size_inner는 양수여야 합니다." << std::endl;
        }
    }

    public void setRelativePosition() {
        int temp[][] = {
            {-size_inner - interval, -size_inner - interval}, {0, -size_inner - interval}, {size_inner + interval, -size_inner - interval},
            {-size_inner - interval, 0}, {0, 0}, {size_inner + interval,0},
            {-size_inner - interval, +size_inner + interval}, {0, +size_inner + interval}, {size_inner + interval, +size_inner + interval},
        };

        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[0].length; j++) {
                relativePosition[i][j] = temp[i][j];
            }
        }
    }

    public void drawLattice(Mat image){
        setSize_outter(500);
        center_interest.x = image.cols() / 2;
        center_interest.y = image.rows() / 2;

        rectangle_outter.x = (int)(center_interest.x - size_outter/2);
        rectangle_outter.y = (int)(center_interest.y - size_outter/2);
        rectangle_outter.width = size_outter;
        rectangle_outter.height = size_outter;

        // 외각 정사각형(노랑)을 그림. 가이드라인 같은 느낌 , native!! 도움!
        drawRectangle(image.getNativeObjAddr(), rectangle_outter, new Scalar(0,0,0), 1);

        if(!centers_inner.isEmpty()){
            centers_inner.clear();
        }
        if(!rectangle_inner.isEmpty()){
            rectangle_inner.clear();
        }

        // 중심점 저장, Rect 객체 저장 및 그리기
        for(int i = 0; i < 9; i++){

            // 9개의 내부 정사각형(노랑)의 중심점을 찾아서 // vector에 저장
            Point temp = new Point();
            temp.x = center_interest.x + relativePosition[i][0];
            temp.y = center_interest.y + relativePosition[i][1];
            centers_inner.add(temp); // c++ vector의 pushback과 같은 기능을 하는 add.

            // 9개의 내부 정사각형의 위치와 크기를 나타내는 Rect 객체 생성 후// vector에 저장
            Rect rectangle  = new Rect();
            rectangle.x = (int)(temp.x - size_inner / 2);
            rectangle.y = (int)(temp.y - size_inner / 2);
            rectangle.width =(int) size_inner;
            rectangle.height =(int) size_inner;
            rectangle_inner.add(rectangle);
            drawRectangle(image.getNativeObjAddr(), rectangle, new Scalar(0,0,0), 1);

            // inner lattice에 검출된 색을 칠한다. 검출이 된경우에는 해당색으로 색칠한다.
            int size_temp = size_inner / 2;
            Rect rectTemp = new Rect();
            rectTemp.x = (int)(temp.x - size_temp / 2);
            rectTemp.y = (int)(temp.y - size_temp / 2);
            rectTemp.width =(int) size_temp;
            rectTemp.height =(int) size_temp;
            drawRectangle(image.getNativeObjAddr(), rectTemp, color_inner[i], -1);

            // 빨간색이면 테두리를 검은색으로 한번 더 그리도록 한다. 그래서 orange와 비교할 수 있게
            if(color_inner[i].B == 0 && color_inner[i].G == 0 && color_inner[i].R == 255){
                drawRectangle(image.getNativeObjAddr(), rectTemp, new Scalar(0,0,0), 2);
            }

        }

    }

    public void detectColor(Mat image){
        Mat colorImage = image.clone();

        for(int i = 0 ; i < 9; i++){
            int detectedColor = getDetectedColor(rectangle_inner.get(i), colorImage);
            if(detectedColor != -1){
                surfaceColor[i] = detectedColor;
                color_inner[i] = color_ref[detectedColor];
            }else{
                surfaceColor[i] = -1;
                color_inner[i] = color_ref[6];
            }
        }

        if(isSaveButtonClicked){
            for(int i = 0; i < 9; i++){
                isDetectionDone[i] = true;
            }
            // 만약 여기까지 왔다면 해당 객체는 제 기능을 다 한 것이다
            // isDetectedAll을 사용할 타임.
        }

    }

    public int getDetectedColor(Rect rect, Mat colorImage){
       int result = -1;
       Mat imageROI = new Mat(); // image에서 관심 rect 영역 부분만 처리하기 위해 imageROI 준비
       getImageROI(colorImage.getNativeObjAddr(), imageROI.getNativeObjAddr(), rect);

        BGR_SDATA bgr_sdata;
        bgr_sdata= MyStatistics.getBGR_SDATA(imageROI);
        float dinominator  = bgr_sdata.sdata[0].avg + bgr_sdata.sdata[1].avg + bgr_sdata.sdata[2].avg;
        float b_frac = bgr_sdata.sdata[0].avg / dinominator;
        float g_frac = bgr_sdata.sdata[1].avg / dinominator;
        float r_frac = bgr_sdata.sdata[2].avg / dinominator;

        if(b_frac > 0.45) {
            result = 0; // blue
        } else if(b_frac < 0.4 && g_frac >0.6+separate_gy){
            result = 1; // green
        } else if(b_frac < 0.25 && g_frac < 0.35){
            if( g_frac < b_frac + separate_ro ){
                result = 2; // red
            }else{
                result = 3; // orange
            }
        } else if(b_frac < 0.2 && g_frac > 0.35 && g_frac < 0.6+separate_gy){
            result =4; // yellow
        } else if(b_frac > 0.25 && b_frac < 0.45 && g_frac >0.2 && g_frac < 0.5){
            result= 5; // white
        }

        return result;
    }

    public int getDetectedColor_old(Rect rect, Mat colorImage){
        int result = -1;
        Mat imageROI = new Mat(); // image에서 관심 rect 영역 부분만 처리하기 위해 imageROI 준비
        getImageROI(colorImage.getNativeObjAddr(), imageROI.getNativeObjAddr(), rect);

        BGR_SDATA bgr_sdata;
        bgr_sdata= MyStatistics.getBGR_SDATA(imageROI);

//        bgr_temp[0] = bgr_sdata.sdata[0].avg;
//        bgr_temp[1] = bgr_sdata.sdata[1].avg;
//        bgr_temp[2] = bgr_sdata.sdata[2].avg;

        float gb = bgr_sdata.sdata[1].avg / bgr_sdata.sdata[0].avg;
        float rb = bgr_sdata.sdata[2].avg / bgr_sdata.sdata[0].avg;
        float rg = bgr_sdata.sdata[2].avg / bgr_sdata.sdata[1].avg;
        float min = bgr_sdata.sdata[0].avg;
        for(int i = 0; i < 3; i++){
            if(min >= bgr_sdata.sdata[i].avg){
                min = bgr_sdata.sdata[i].avg;
            }
        }
        float b_over_min = bgr_sdata.sdata[0].avg / min;
        float g_over_min = bgr_sdata.sdata[1].avg / min;
        float r_over_min = bgr_sdata.sdata[2].avg / min;
        float sum = b_over_min + g_over_min + r_over_min;


        float dinominator  = bgr_sdata.sdata[0].avg + bgr_sdata.sdata[1].avg + bgr_sdata.sdata[2].avg;
        float b_frac = bgr_sdata.sdata[0].avg / dinominator;
        float g_frac = bgr_sdata.sdata[1].avg / dinominator;
        float r_frac = bgr_sdata.sdata[2].avg / dinominator;

        // 흰색 먼저 분리
        if (sum > 2.5 && sum < 3.5) {
            result = 5;
        }
        // 파란색 분리
        else if (rb < 0.8 && gb < 0.8) {
            result = 0;
        }
        // 초록색 분리
        else if (rb < 1.7 && gb>1) {
            result = 1;
        }
        // 빨간색 분리
        else if (gb < 1.0) {
            result = 2;
        }

        // 주황색 분리, 여기 조금 수정필요
        else if (rg > 1.3 && gb > 1.0 && gb < 2.0) {
            result = 3;
        }

        // 마지막으로 노란색
        else {
            result = 4;
        }

        return result;
    }

    public boolean isDetectedAll() {
        boolean result = true;
        for (int i = 0; i < 9; i++) {
            if (!isDetectionDone[i]) {
                result = false;
                break;
            }
        }

        return result;
    }



    public native void drawRectangle(long imageAddr, Rect rect, Scalar scalar, int thickness);
    public native void getImageROI(long imageAddr, long resultAddr, Rect rect);


}
