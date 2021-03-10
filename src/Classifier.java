import org.opencv.core.Core;

import java.io.File;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.Arrays;
import java.text.DecimalFormat;

import utils.*;
import Script.*;

public class Classifier {
    public Classifier(){
        try {

            PrintStream print=new PrintStream("C:\\Users\\36546\\Documents\\Master\\4Semester\\Project\\results.txt");
            System.setOut(print);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        //Input
        Scanner pathfromUser = new Scanner(System.in);
        System.out.println("Please enter the image path: ");
        String ImagePath1 = pathfromUser.nextLine();

        Classifier o = new Classifier();
        File file = new File(ImagePath1);
        Mat srcImage = null;
        eConditions eCondition;
        //if only one picture
        if(file.isFile()){
            srcImage = Imgcodecs.imread(ImagePath1);
            eCondition=Classify(srcImage);
            System.out.println("the environment Condition of "+file.getName()+" is:");
            System.out.println("Lighting Condition: "+eCondition.getLight());
            System.out.println("Degree of Blur: "+eCondition.getBlur());
            System.out.println("The image is blind: "+eCondition.isBlind());
        }
        else if(file.isDirectory()){
            File[] fs = file.listFiles();

            String[] Lighting_condition = new String[fs.length];
            int[] Blured_Degree = new int[fs.length];
            boolean[] Blind_Detection = new boolean[fs.length];
            int counter = 0;//image counter

            for (File f:fs){
                if(!f.isDirectory()) {
                    srcImage = Imgcodecs.imread(f.getAbsolutePath());

                }
                eCondition=Classify(srcImage);

                Lighting_condition[counter]=eCondition.getLight();
                Blured_Degree[counter]=eCondition.getBlur();
                Blind_Detection[counter]=eCondition.isBlind();


                System.out.println("the environment Condition of "+f.getName()+" is:");
                System.out.println("Lighting Condition: "+Lighting_condition[counter]);
                System.out.println("Degree of Blur: "+Blured_Degree[counter]);
                System.out.println("The image is blind: "+Blind_Detection[counter]);
                counter++;
            }
            double lightDark=0,lightTwi=0,lightL=0;
            double blur1=0,blur2=0,blur3=0,blur4=0,blur5=0;
            double blind=0;
            for(String lightCondition: Lighting_condition){
                if(lightCondition.equals("Dark")) lightDark++;
                else if(lightCondition.equals("Twilight")) lightTwi++;
                else lightL++;
            }
            for(int blurDegree: Blured_Degree){
                if (blurDegree == 1) blur1++;
                else if (blurDegree == 2) blur2++;
                else if (blurDegree == 3) blur3++;
                else if (blurDegree == 4) blur4++;
                else blur5++;
            }
            for (boolean blindDetection:Blind_Detection){
                if(blindDetection){
                    blind++;
                }
            }
            DecimalFormat df = new DecimalFormat("0.00");
            System.out.println("Summary: ");
            System.out.println("Dark Ratio: " + df.format(lightDark/fs.length*100) +"%; Twilight Ratio: "+ df.format(lightTwi/fs.length*100)+"%; Light Ratio: "+df.format(lightL/fs.length*100)+"%");
            System.out.println("1 Degree of Blur: " + df.format(blur1/fs.length*100) +"%; 2 Degree of Blur: "+ df.format(blur2/fs.length*100)+"%; 3 Degree of Blur: "+df.format(blur3/fs.length*100)+"%; 4 Degree of Blur: "+ df.format(blur4/fs.length*100)+"%; 5 Degree of Blur: "+df.format(blur5/fs.length*100)+"%");
            System.out.println("Blind Ratio: "+ df.format(blind/fs.length*100)+"%");
        }
        else{
            System.out.println("The path is wrong!");
        }
    }

    private static eConditions Classify(Mat srcImage) {
        //classify the lighting condition
        String Light;
        Lighting_condition lighting_condition = new Lighting_condition(srcImage);
        lighting_condition.LightClassify();
        Light=lighting_condition.getLightCondition();

        //get blur degree of picture
        int Blur;
        Blured_Degree blured_degree = new Blured_Degree(srcImage);
        blured_degree.BlurClassify();
        Blur = blured_degree.getBlur();
        //decide if the picture is blind
        boolean Blind;
        Blind_Detection blind_detection = new Blind_Detection(srcImage);
        blind_detection.blindClassify();
        Blind=blind_detection.isBlind();

        return new eConditions(Light,Blur,Blind);
    }

}
