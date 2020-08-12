package org.height185.rubiksdetector.rubiksDetector;

import org.height185.rubiksdetector.typedef.BGR_SDATA;
import java.util.ArrayList;


public class MyStatistics {

    public static float getAverage(Mat hist) {
        float denominator = 0.0f;
        float numerator = 0.0f;
        for (int i = 0; i < hist.rows(); i++) {
            float temp = (float) (hist.get(i, 0)[0]);

            denominator += temp;
            numerator += i * temp;

        }
        return numerator / denominator;
    }

    public static float getDeviation(Mat hist) {
        float average = getAverage(hist);

        float denominator = 0;
        float numerator = 0;
        for (int i = 0; i < hist.rows(); i++) {
            float temp = (float) (hist.get(i, 0)[0]);
            denominator += temp;
            numerator += i * i * temp;
        }

        float temp = numerator / denominator;
        return (float) Math.sqrt(temp - average * average);

    }

    public static BGR_SDATA getBGR_SDATA(Mat imageROI) {

        Mat precursor = imageROI.clone();
        List<Mat> sub = new ArrayList<>();
        Core.split(precursor, sub);
        // split 될때 c++과 다른 점이 있다면
        // BGR 순서로 SPLIT 되는 것이 아니라 RGB 순서로 SPLIT 되는 것이다.

        Mat[] hist = new Mat[3];

        // 이 부분을 수정함. get(2-i)
        for (int i = 0; i < 3; i++) {
            hist[i] = new Mat();
            getHistogram(hist[i].getNativeObjAddr(), sub.get(2 - i).getNativeObjAddr());
        }

        // 일단 평균을 얻습니다.
        float B_avg = getAverage(hist[0]);
        float G_avg = getAverage(hist[1]);
        float R_avg = getAverage(hist[2]);

        // 표준 편차를 얻습니다.
        float B_dev = getDeviation(hist[0]);
        float G_dev = getDeviation(hist[1]);
        float R_dev = getDeviation(hist[2]);


        BGR_SDATA result = new BGR_SDATA();
        result.sdata[0].avg = B_avg;
        result.sdata[0].dev = B_dev;

        result.sdata[1].avg = G_avg;
        result.sdata[1].dev = G_dev;

        result.sdata[2].avg = R_avg;
        result.sdata[2].dev = R_dev;
        return result;
    }

}
