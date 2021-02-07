package Script;

import org.opencv.imgproc.Imgproc;
import utils.*;
import org.opencv.core.*;

public class Blind_Detection {
    Mat originImg;
    boolean Blind;
    Mat img;//after ROI construct

    public Blind_Detection(Mat Img){
        this.originImg=Img;
    }

    public void blindClassify(){
        //1.get ROI
        findROI blindROI = new findROI(originImg);
        this.img = blindROI.getblindROI();
        //2.simple detect Lanemark
        LaneMask laneMask = new LaneMask(img);
        Mat lines = laneMask.getLaneMask();
        //3.get the threshold of light Channels
        Threshold_lightChannel thresold_lightChannel = new Threshold_lightChannel(img);
        double threLightC = 1.8*thresold_lightChannel.getThreAvg();
        //4.get the light Channel
        Mat lightC= new Mat(img.size(), CvType.CV_8UC1);
        getLightC(lightC,threLightC);
        //5.remove lanemark
        removeLane(lightC,lines);
        //6.get area of reflection_region
        double area=blobDetection(lightC);
        //7.final decision
        if(area>8500){
            Blind=true;
        }else{
            Blind=false;
        }
    }

    public boolean isBlind() {
        return Blind;
    }

    private double blobDetection(Mat lightC) {
        int height = lightC.rows();
        int width = lightC.cols();
        double sum = 0.0;
        for(int i = 0; i< height;i++){
            for (int j = 0; j<width;j++){
                sum+=lightC.get(i,j)[0];
            }
        }
        return sum/255;
    }

    private void removeLane(Mat lightC, Mat lines) {
        Mat lineMask = Mat.zeros(img.size(),CvType.CV_8UC3);
        // Imgproc.cvtColor(lightC,lineMask,Imgproc.COLOR_GRAY2BGR);

        for (int x = 0; x < lines.rows(); x++) {
            double[] vec = lines.get(x, 0);

            double rho = vec[0]; //就是圆的半径r
            double theta = vec[1]; //就是直线的角度

            Point pt1 = new Point();
            Point pt2 = new Point();

            double a = Math.cos(theta);
            double b = Math.sin(theta);

            double x0 = a * rho;
            double y0 = b * rho;

            int lineLength = 1000;

            pt1.x = Math.round(x0 + lineLength * (-b));
            pt1.y = Math.round(y0 + lineLength * (a));
            pt2.x = Math.round(x0 - lineLength * (-b));
            pt2.y = Math.round(y0 - lineLength * (a));

            if (theta >= 0) {
                Imgproc.line(lineMask, pt1, pt2, new Scalar(255, 0, 0), 20, Imgproc.LINE_4, 0);
            }
        }
        for(int i = 0; i<lineMask.rows();i++){
            for(int j = 0;j<lineMask.cols();j++){
                if(lineMask.get(i,j)[0]!=0){
                    lightC.put(i,j,0);
                }
            }
        }
    }


    public void getLightC(Mat lightC, double Threshold) {
        int channels = img.channels();
        double[] pixel = new double[3];
        double light;

        for (int i = 0; i < lightC.rows(); i++) {
            for (int j = 0; j < lightC.cols(); j++) {
                if (channels == 3) {
                    // B,G,R
                    pixel = img.get(i, j).clone();
                    light = 0;
                    for (int k = 0; k < 3; k++) {
                        if (pixel[k] > light) light = pixel[k];
                    }
                    if (light < Threshold) light = 0;
                    lightC.put(i, j, light);
                } else {
                    lightC.put(i, j, img.get(i, j).clone());
                }
            }
        }
    }


}
