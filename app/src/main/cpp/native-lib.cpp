#include <jni.h>
#include <opencv2/opencv.hpp>
#include <android/log.h>

using namespace cv;
using namespace std;

extern "C"
JNIEXPORT void JNICALL
Java_org_height185_rubiksdetector_rubiksDetector_SurfaceDetector_drawRectangle(JNIEnv *env,
                                                                           jobject callingObject,
                                                                           jlong imageAddr,
                                                                           jobject rect,
                                                                           jobject scalar,
                                                                           jint thickness) {

    Mat &image          = *(Mat *)imageAddr;

    jclass rectClass     = env->GetObjectClass(rect);
    jfieldID fid_rectX = env->GetFieldID(rectClass, "x", "I");
    jfieldID fid_rectY = env->GetFieldID(rectClass, "y", "I");
    jfieldID fid_rectW = env->GetFieldID(rectClass, "width", "I");
    jfieldID fid_rectH = env->GetFieldID(rectClass, "height", "I");
    int rectX = env->GetIntField(rect, fid_rectX);
    int rectY = env->GetIntField(rect, fid_rectY);
    int rectW = env->GetIntField(rect, fid_rectW);
    int rectH = env->GetIntField(rect, fid_rectH);
    cv::Rect myRect(rectX, rectY, rectW, rectH);


    jclass scalarClass   = env->GetObjectClass(scalar);
    jfieldID  fid_B = env->GetFieldID(scalarClass, "B", "I");
    jfieldID  fid_G = env->GetFieldID(scalarClass, "G", "I");
    jfieldID  fid_R = env->GetFieldID(scalarClass, "R", "I");

    int B = env->GetIntField(scalar, fid_B);
    int G = env->GetIntField(scalar, fid_G);
    int R = env->GetIntField(scalar, fid_R);


    cv::Scalar myScalar(R, G, B);  // 이 부분 조심해야한다. 이렇게 넘겨야해.

    int thick = (int) thickness;
    cv::rectangle(image, myRect, myScalar, thick);

}



extern "C"
JNIEXPORT void JNICALL
Java_org_height185_rubiksdetector_rubiksDetector_SurfaceDetector_getImageROI(JNIEnv *env,
                                                                         jobject instance,
                                                                         jlong imageAddr,
                                                                         jlong resultAddr,
                                                                         jobject rect) {
    Mat &image          = *(Mat *)imageAddr;
    Mat &imageROI       = *(Mat *)resultAddr;
    jclass rectClass     = env->GetObjectClass(rect);
    jfieldID fid_rectX = env->GetFieldID(rectClass, "x", "I");
    jfieldID fid_rectY = env->GetFieldID(rectClass, "y", "I");
    jfieldID fid_rectW = env->GetFieldID(rectClass, "width", "I");
    jfieldID fid_rectH = env->GetFieldID(rectClass, "height", "I");
    int rectX = env->GetIntField(rect, fid_rectX);
    int rectY = env->GetIntField(rect, fid_rectY);
    int rectW = env->GetIntField(rect, fid_rectW);
    int rectH = env->GetIntField(rect, fid_rectH);
    cv::Rect myRect(rectX, rectY, rectW, rectH);

    imageROI = image(myRect); // 고작 이 한 줄을 위해서..
}

extern "C"
JNIEXPORT void JNICALL
Java_org_height185_rubiksdetector_rubiksDetector_MyStatistics_getHistogram(JNIEnv *env, jclass type,
                                                                       jlong histAddr,
                                                                       jlong imageAddr) {
    Mat &hist = *(Mat *) histAddr;
    Mat &image = *(Mat *) imageAddr;

    int histSize[1];         // number of bins in histogram
    float hranges[2];        // range of values
    const float *ranges[1];  // pointer to the different value ranges
    int channels[1];         // channel number to be examined

    histSize[0] = 256;   // 256 bins
    hranges[0] = 0.0;    // from 0 (inclusive)
    hranges[1] = 256.0;  // to 256 (exclusive)
    ranges[0] = hranges;
    channels[0] = 0;     // we look at channel 0

    cv::calcHist(&image,
                 1,            // histogram of 1 image only
                 channels,    // the channel used
                 cv::Mat(),    // no mask is used
                 hist,        // the resulting histogram
                 1,            // it is a 1D histogram
                 histSize,    // number of bins
                 ranges        // pixel value range
    );
}