package Script;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

public class Lighting_condition {
    Mat img;
    String LightCondition;
    public Lighting_condition(Mat img){
        this.img=img;
    }
    public void LightClassify(){
        Mat image1 = img.clone();
        int height = image1.rows();
        int w = image1.cols();
        int h = height/2;
        Rect rect = new Rect(0,0,w,h);
        Mat image_roi = new Mat(image1, rect);

        Imgproc.cvtColor(image_roi, image_roi, Imgproc.COLOR_RGB2HSV);
        Scalar avgV =Core.mean(image_roi);

        double H = avgV.val[0];
        double S = avgV.val[1];
        double V = avgV.val[2];


        if ( V<72 ) {
            this.LightCondition="Dark";
        }
        else{
            if ((H < 55 && S > 95 && V < 152 || H < 62 && S < 95 && V > 140)){
                this.LightCondition="Twilight";
            }
            else {
                if (V < 95) {
                    this.LightCondition="Dark";
                }
                else {
                    if (H < 75 && H > 60 && S > 60 && S < 115 && V > 95 && V < 152|| H < 60 && H > 40 && S > 65 && S < 95 && V > 95 && V < 140){
                        this.LightCondition="Twilight";
                    }else this.LightCondition="Light";
                }
            }
        }
    }

    public String getLightCondition() {
        return LightCondition;
    }
}
