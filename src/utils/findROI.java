package utils;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.highgui.HighGui;


public class findROI {
    Mat img;
    public findROI(Mat img){
        this.img=img;
    }

    public Mat getblindROI() {
        int h=img.rows();
        int w=img.cols();
        Rect rect =new Rect(w/3,h*3/5,w/3,h*1/4);
//        Mat ROI= new Mat(img,rect);
//        HighGui.imshow("reflection test",ROI);
//        HighGui.waitKey(0);
        return new Mat(img, rect);
    }
}
