package utils;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

public class LaneMask {
    Mat img;
    public LaneMask(Mat img){
        this.img=img;
    }
    public Mat getLaneMask(){
        //Denoise
        Mat temp = new Mat(img.size(),img.type());
        Imgproc.GaussianBlur(img,temp,new Size(3,3),0,Core.BORDER_DEFAULT);

        //Convert to gray and Blur
        Mat grayImg = Mat.zeros(img.rows(),img.cols(),CvType.CV_8UC1);
        if(img.channels()==3){
            Imgproc.cvtColor(temp,grayImg,Imgproc.COLOR_BGR2GRAY);
        }else{
            grayImg=temp;
        }
        Imgproc.medianBlur(grayImg, grayImg, 3);

        //Binary
        Threshold_Canny thresCanny = new Threshold_Canny();
        thresCanny.FindAdaptiveThreshold(grayImg,3,0.80);
        double thCannyLow = thresCanny.getM_cannyLowTh();
        double thCannyHigh = thresCanny.getM_cannyHighTh();

        //Canny
        Mat maskImg = Mat.zeros(img.rows(),img.cols(),CvType.CV_8UC1);
        Imgproc.Canny(grayImg,maskImg,thCannyLow,thCannyHigh,3,false);

        //Hough Line detection
        Mat lines = new Mat();
        Imgproc.HoughLines(maskImg,lines,1,Math.PI/180, 140,0,0,0,10);
        return lines;
    }
}
