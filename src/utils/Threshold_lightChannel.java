package utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class Threshold_lightChannel {
    Mat img;

    public Threshold_lightChannel(Mat img){
        this.img=img;
    }

    public double getThreholds(){
        int channels=img.channels();
        Mat grayImg=new Mat();
        if(channels==3){
            Imgproc.cvtColor(img,grayImg,Imgproc.COLOR_BGR2GRAY);
        }else{
            grayImg=img.clone();
        }
        int height=grayImg.rows(), width=grayImg.cols();
        int uThre=0,uThre_new=180;
        int nBack_count,nData_count;
        int nBack_sum,nData_sum;
        int nValue;
        while(uThre!=uThre_new){
            nBack_count=nData_count=0;
            nBack_sum=nData_sum=0;

            for(int j=0;j<height;j++){
                for(int i=0;i<width;i++){
                    nValue=(int) grayImg.get(j,i)[0];

                    if(nValue>uThre_new){
                        nBack_sum+=nValue;
                        nBack_count++;
                    }else{
                        nData_count++;
                        nData_sum+=nValue;
                    }
                }
            }

            nBack_sum/=nBack_count;
            nData_sum/=nData_count;
            uThre=uThre_new;
            uThre_new=(nBack_sum+nData_sum)/2;
        }
        return uThre;
    }
    public double getThreAvg(){
        int channels=img.channels();
        Mat grayImg=new Mat();
        if(channels==3){
            Imgproc.cvtColor(img,grayImg,Imgproc.COLOR_BGR2GRAY);
        }else{
            grayImg=img.clone();
        }
        int height=img.rows();
        int width=img.cols();
        double sum=0.0;

        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                sum+=grayImg.get(i,j)[0];
            }
        }
        sum/=(height*width);
        return sum;

    }

}

