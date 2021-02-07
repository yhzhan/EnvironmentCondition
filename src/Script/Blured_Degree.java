package Script;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

public class Blured_Degree {
    Mat img;
    int Blur;
    public Blured_Degree(Mat img){
        this.img=img;
    }
    public void BlurClassify(){
        Imgproc.GaussianBlur(img, img, new Size(3, 3), 0);
        Mat grayImage = new Mat();
        Imgproc.cvtColor(img, grayImage,Imgproc.COLOR_BGR2GRAY);

        Mat laplacianDstImage = new Mat();
        Imgproc.Laplacian(grayImage, laplacianDstImage, CvType.CV_16U, 3, 1, 0, Core.BORDER_DEFAULT);

        //Imgcodecs.imwrite(ImagePath+f.getName(), laplacianDstImage);

        MatOfDouble stddev = new MatOfDouble();
        Core.meanStdDev(laplacianDstImage, new MatOfDouble(), stddev);
        double clarityValue = stddev.toArray()[0];


        if (clarityValue<15)
            Blur = 5;
        else if (clarityValue<25)
            Blur = 4;
        else if (clarityValue<35)
            Blur = 3;
        else if (clarityValue<45)
            Blur = 2;
        else
            Blur = 1;
    }

    public int getBlur() {
        return Blur;
    }
}
