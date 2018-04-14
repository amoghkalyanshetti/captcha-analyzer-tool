package CAT;

import CharacterRecognition.Recognition;
import CharacterRecognition.SymbolSet;
import Segmentation.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Vector;
import preprocessing.*;
public class Main {
    
    public static SymbolSet ss[]=new SymbolSet[62];
    public long startTime;//related to time
    public long preprocessingTime;
    public long segmentationTime;
    public long thinningTime;
    public long recognitionTime;
    public String decodedCaptcha,ptime,stime,thTime,rtime,ttime;
    public File file1;
    public BufferedImage img;
    ImageInfo imgInfo;
    public static Vector tracker=new Vector();
    @SuppressWarnings("null")
    public Main(ImageInfo img)
    {
        imgInfo=img;
    }
    public String backEnd(String web,BufferedImage img) 
    {
        this.img=img;
       // this.file1=file1;
        //Creating initial objects and variables
//     for(int k=1;k<=15;k++)
//     {
//       if(k==6) 
//           continue;
         //String imgpath = "D:\\ImgDatabase\\Old\\CAPTCHA   without  noise\\VODAFONE done - 25\\"+k+".jpg";
        //String imgpath = "D:\\ImgDatabase\\Old\\CAPTCHA   without  noise\\addhar - 25\\1.jpg";
        decodedCaptcha="";
        CommonUtilityClass util = new CommonUtilityClass();
        imgInfo= new ImageInfo();
        DisconnectedSegment segObj;
        EightConnectivity segObj2;
        
        
        imgInfo.originalImage=img;

        Binarization bnryObj = new Binarization();
        NoiseRemoval noiseRmvObj = new NoiseRemoval();
        Thinning thinning = new Thinning();
        @SuppressWarnings("UnusedAssignment")
        Scale s1 = null;
        //Taking name of website as user-input
       // Scanner s = new Scanner(System.in);
        //String website = (s.next()).toUpperCase();
        tracker.removeAllElements();
        
        ImageInfo.website webName;
      // webName = ImageInfo.website.valueOf((s.next()).toUpperCase());
        
        webName=ImageInfo.website.valueOf(web);
        System.out.println(""+webName.toString());
      // util.readDATFile("DATfiles/"+webName.toString()+".dat");
        //System.out.println(""+webName.toString());
        //Always Start from Here to measure the Time
        startTime=System.currentTimeMillis();
        switch (webName) 
        {
            case BOTDETECT:
                imgInfo.grayScaledImage = bnryObj.convertOrgToGray(imgInfo.originalImage);
                imgInfo.binarizedImage = bnryObj.getBinaryImage(imgInfo.grayScaledImage,127);
                imgInfo.preprocessedImage=noiseRmvObj.getEdgeRmvImg(imgInfo.binarizedImage);
                preprocessingTime=System.currentTimeMillis()-startTime;
                ////////////////////////////////////////////////////////////////////////
                //segmentation
                ConnectedSegment cobj = new ConnectedSegment(imgInfo.preprocessedImage); 
                imgInfo.segments=cobj.segmentImage();
 
                segmentationTime=System.currentTimeMillis()-preprocessingTime-startTime;
              //thinning
                imgInfo.thinnedSegments=thinning.thinSegments(imgInfo.segments);
                
                imgInfo.scaledSegments=util.scale(imgInfo.thinnedSegments);
                break;
            case FREEDNS:    
                imgInfo.grayScaledImage=bnryObj.convertOrgToGray(imgInfo.originalImage);
                imgInfo.preprocessedImage= noiseRmvObj.arcRemovalMSRCTC(imgInfo.grayScaledImage,-9539986);
                imgInfo.preprocessedImage=bnryObj.getBinaryImage(imgInfo.preprocessedImage,251); //251
                imgInfo.preprocessedImage=noiseRmvObj.getThickHCharObj(imgInfo.preprocessedImage);
                imgInfo.preprocessedImage=noiseRmvObj.boundaryNoiseRmvImg(imgInfo.preprocessedImage,7);
                preprocessingTime=System.currentTimeMillis()-startTime;
                //////////////////////////////////////////////////////////////
                //Segmentation
                segObj=new Overlapped(5,48, imgInfo.preprocessedImage);    
                segObj.getProjection();
                imgInfo.segments=segObj.generateSegments();
                 segmentationTime=System.currentTimeMillis()-preprocessingTime-startTime;
              //thinning
                imgInfo.thinnedSegments=thinning.thinSegments(imgInfo.segments);
                s1=new Scale(imgInfo.thinnedSegments);
                imgInfo.scaledSegments=s1.getScaledImg();
                thinningTime=System.currentTimeMillis()-preprocessingTime-segmentationTime-startTime;
                    break;
        
            case SBI:
               imgInfo.grayScaledImage = bnryObj.convertOrgToGray(imgInfo.originalImage);
                imgInfo.binarizedImage=bnryObj.getBinaryImage(imgInfo.grayScaledImage,60);
                imgInfo.preprocessedImage=noiseRmvObj.getEdgeRmvImg(imgInfo.binarizedImage);
                preprocessingTime=System.currentTimeMillis()-startTime;
                ////////////////////////////////////////////////////////////////////////
                segObj=new DisconnectedSegment(0,imgInfo.preprocessedImage);
                segObj.getProjection();
                imgInfo.segments=segObj.generateSegments();
                segmentationTime=System.currentTimeMillis()-preprocessingTime-startTime;
              //thinning
                imgInfo.thinnedSegments=thinning.thinSegments(imgInfo.segments);
                s1=new Scale(imgInfo.thinnedSegments);
                imgInfo.scaledSegments=s1.getScaledImg();
                thinningTime=System.currentTimeMillis()-preprocessingTime-segmentationTime-startTime;
                break; 
            case TEST:
                ////////////////////////////////////////////////////////////////////////
                //Preprocessing
                imgInfo.grayScaledImage  = bnryObj.convertOrgToGray(imgInfo.originalImage);
                imgInfo.preprocessedImage=noiseRmvObj.arcRemovalMSRCTC(imgInfo.grayScaledImage);
                imgInfo.binarizedImage=  bnryObj.getBinaryImage(imgInfo.preprocessedImage);
                util.printBufferedImg(imgInfo.binarizedImage);
                imgInfo.preprocessedImage=noiseRmvObj.getThickWCharObj(imgInfo.binarizedImage);
                imgInfo.preprocessedImage=noiseRmvObj.getThickHCharObj( imgInfo.preprocessedImage);
                imgInfo.preprocessedImage=noiseRmvObj.getAdvDotRmvObj( imgInfo.preprocessedImage,1070);
                util.printBufferedImg(imgInfo.preprocessedImage);          
                break;
                
           case MSRCTC:
                ////////////////////////////////////////////////////////////////////////
                //Preprocessing
                imgInfo.grayScaledImage  = bnryObj.convertOrgToGray(imgInfo.originalImage);
                imgInfo.preprocessedImage= noiseRmvObj.arcRemovalMSRCTC(imgInfo.grayScaledImage);
                imgInfo.binarizedImage   = bnryObj.getBinaryImage(imgInfo.preprocessedImage);
                imgInfo.preprocessedImage= noiseRmvObj.getThickWCharObj(imgInfo.binarizedImage);
                imgInfo.preprocessedImage= noiseRmvObj.getThickHCharObj(imgInfo.binarizedImage);
                imgInfo.preprocessedImage= noiseRmvObj.getAdvDotRmvObj(imgInfo.preprocessedImage,1070+255+255);
                preprocessingTime=System.currentTimeMillis()-startTime;
                /////////////////////////////////////////////////////////////////////////
                //segmentation
                segObj=new Overlapped(4,45,imgInfo.binarizedImage);
                //segObj.getProjection();
                imgInfo.segments=segObj.generateSegments();
                 segmentationTime=System.currentTimeMillis()-preprocessingTime-startTime;
              //thinning
                imgInfo.thinnedSegments=thinning.thinSegments(imgInfo.segments);
                s1=new Scale(imgInfo.thinnedSegments);
                imgInfo.scaledSegments=s1.getScaledImg();
                thinningTime=System.currentTimeMillis()-preprocessingTime-segmentationTime-startTime;
                //////////////////////////////////////////////////////////////////////
                
                break;
            case AADHAR://done by amogh
           
                 ////////////////////////////////////////////////////////////////////////
                //Preprocessing
                imgInfo.grayScaledImage = bnryObj.convertOrgToGray(imgInfo.originalImage);
                imgInfo.binarizedImage = bnryObj.getBinaryImage(imgInfo.grayScaledImage,127);
                preprocessingTime=System.currentTimeMillis()-startTime;
                //////////////////////////////////////////////////////////////////////////
                //Segmentation
                 segObj=new DisconnectedSegment(0,imgInfo.binarizedImage);//Threshold =0
                segObj.getProjection();
               // segObj=new Overlapped(19,imgInfo.binarizedImage);
                imgInfo.segments=segObj.generateSegments();//getting the segments
                
                segmentationTime=System.currentTimeMillis()-preprocessingTime-startTime;
              //thinning
                imgInfo.thinnedSegments=thinning.thinSegments(imgInfo.segments);
                s1=new Scale(imgInfo.thinnedSegments);
                imgInfo.scaledSegments=s1.getScaledImg();
                thinningTime=System.currentTimeMillis()-preprocessingTime-segmentationTime-startTime;
                //////////////////////////////////////////////////////////////////////
                
                break;
            case VODAFONE://done all by amogh
                ////////////////////////////////////////////////////////////////////////
                //Preprocessing
                imgInfo.grayScaledImage = bnryObj.convertOrgToGray(imgInfo.originalImage);
                imgInfo.binarizedImage = bnryObj.getBinaryImage(imgInfo.grayScaledImage,127);
                preprocessingTime=System.currentTimeMillis()-startTime;
                //////////////////////////////////////////////////////////////////////////
                //Segmentation
                 segObj=new DisconnectedSegment(0,imgInfo.binarizedImage);//Threshold =0
                segObj.getProjection();
                segObj=new Overlapped(19,imgInfo.binarizedImage);
                imgInfo.segments=segObj.generateSegments();//getting the segments
                
                segmentationTime=System.currentTimeMillis()-preprocessingTime-startTime;
              //thinning
                imgInfo.thinnedSegments=thinning.thinSegments(imgInfo.segments);
                s1=new Scale(imgInfo.thinnedSegments);
                //imgInfo.scaledSegments=s1.getScaledImg();
                imgInfo.scaledSegments=util.scale(imgInfo.thinnedSegments);
                thinningTime=System.currentTimeMillis()-preprocessingTime-segmentationTime-startTime;
                //////////////////////////////////////////////////////////////////////
                
                break;
             case ESCHOLARSHIP://done all by vinay
                ////////////////////////////////////////////////////////////////////////
                //Preprocessing
                imgInfo.grayScaledImage = bnryObj.convertOrgToGray(imgInfo.originalImage);
                imgInfo.binarizedImage = bnryObj.getBinaryImage(imgInfo.grayScaledImage,230);
                preprocessingTime=System.currentTimeMillis()-startTime;
                //////////////////////////////////////////////////////////////////////////
                //Segmentation
                // segObj=new DisconnectedSegment(4,0,imgInfo.binarizedImage);//Threshold =0
                //segObj.getProjection();
                segObj=new Overlapped(22,imgInfo.binarizedImage);
                imgInfo.segments=segObj.generateSegments();//getting the segments
                
                segmentationTime=System.currentTimeMillis()-preprocessingTime-startTime;
              //thinning
                imgInfo.thinnedSegments=thinning.thinSegments(imgInfo.segments);
                
                imgInfo.scaledSegments=util.scale(imgInfo.thinnedSegments);
                thinningTime=System.currentTimeMillis()-preprocessingTime-segmentationTime-startTime;
                //////////////////////////////////////////////////////////////////////
                
                break;
                
                
            case CITYALLY://done all by amogh
                imgInfo.grayScaledImage = bnryObj.convertOrgToGray(imgInfo.originalImage);
                imgInfo.binarizedImage = bnryObj.getBinaryImage(imgInfo.grayScaledImage,135);//threshold=135
                preprocessingTime=System.currentTimeMillis()-startTime;
                //////////////////////////////////////////////////////////////////////////
                //Segmentation
                segObj=new DisconnectedSegment(0,imgInfo.binarizedImage);//Threshold =0
                segObj.getProjection();
                imgInfo.segments=segObj.generateSegments();//getting the segments
                segmentationTime=System.currentTimeMillis()-preprocessingTime-startTime;
               //thinning
                imgInfo.thinnedSegments=thinning.thinSegments(imgInfo.segments);
                s1=new Scale(imgInfo.thinnedSegments);
                imgInfo.scaledSegments=s1.getScaledImg();
                thinningTime=System.currentTimeMillis()-preprocessingTime-segmentationTime-startTime;
                //////////////////////////////////////////////////////////////////////
                 
                break;
         case PCSTORE://done with segmentation
                ////////////////////////////////////////////////////////////////////////
                //Preprocessing
                imgInfo.grayScaledImage=bnryObj.convertOrgToGray(imgInfo.originalImage);
                imgInfo.binarizedImage=bnryObj.getBinaryImage(imgInfo.grayScaledImage);
                imgInfo.preprocessedImage=imgInfo.binarizedImage;
                
                preprocessingTime=System.currentTimeMillis()-startTime;
                //////////////////////////////////////////////////////////////
                //Segmentation
                segObj=new Overlapped(20, imgInfo.binarizedImage); 
               // segObj2=new EightConnectivity(imgInfo.binarizedImage);
               // segObj.getProjection();
                imgInfo.segments=segObj.generateSegments();
                 segmentationTime=System.currentTimeMillis()-preprocessingTime-startTime;
              //thinning
                imgInfo.thinnedSegments=thinning.thinSegments(imgInfo.segments);
                s1=new Scale(imgInfo.thinnedSegments);
                imgInfo.scaledSegments=s1.getScaledImg();
                thinningTime=System.currentTimeMillis()-preprocessingTime-segmentationTime-startTime;
                //////////////////////////////////////////////////////////////////////
                break;
            case TINYPIC:
                ////////////////////////////////////////////////////////////////////////
                //Preprocessing
                imgInfo.originalImage=noiseRmvObj.getEdgeRmvImg(imgInfo.originalImage);
                imgInfo.grayScaledImage = bnryObj.convertOrgToGray(imgInfo.originalImage);
                imgInfo.binarizedImage = bnryObj.getBinaryImage(imgInfo.grayScaledImage,127);
                preprocessingTime=System.currentTimeMillis()-startTime;
                //////////////////////////////////////////////////////////////
                //Segmentation
                segObj=new DisconnectedSegment(0, imgInfo.binarizedImage);    
                segObj.getProjection();
                imgInfo.segments=segObj.generateSegments();
                 segmentationTime=System.currentTimeMillis()-preprocessingTime-startTime;
              //thinning
                imgInfo.thinnedSegments=thinning.thinSegments(imgInfo.segments);
                s1=new Scale(imgInfo.thinnedSegments);
                imgInfo.scaledSegments=s1.getScaledImg();
                thinningTime=System.currentTimeMillis()-preprocessingTime-segmentationTime-startTime;
                //////////////////////////////////////////////////////////////////////
                break;

            case DOOSTANG://done by vinay
                ////////////////////////////////////////////////////////////////////////
                //Preprocessing
                imgInfo.grayScaledImage = bnryObj.convertOrgToGray(imgInfo.originalImage);
                imgInfo.binarizedImage = bnryObj.getBinaryImage(imgInfo.grayScaledImage, 127);
                preprocessingTime = System.currentTimeMillis() - startTime;
                ////////////////////////////////////////////////////////////////////////
                
                //segmentation
                segObj2 = new EightConnectivity(imgInfo.binarizedImage);
                imgInfo.segments = segObj2.generateSegments();
                segmentationTime = System.currentTimeMillis() - preprocessingTime - startTime;
                //thinning
                imgInfo.thinnedSegments = thinning.thinSegments(imgInfo.segments);
                s1 = new Scale(imgInfo.thinnedSegments);
                imgInfo.scaledSegments = s1.getScaledImg();
                thinningTime=System.currentTimeMillis()-preprocessingTime-segmentationTime-startTime;
                //////////////////////////////////////////////////////////////////////
                break;

            case IRCTC://done
                ////////////////////////////////////////////////////////////////////////
                //Preprocessing
                imgInfo.grayScaledImage = bnryObj.convertOrgToGray(imgInfo.originalImage);
                imgInfo.binarizedImage = bnryObj.getBinaryImage(imgInfo.grayScaledImage,127);
                preprocessingTime=System.currentTimeMillis()-startTime;
                ////////////////////////////////////////////////////////////////////////
                //segmentation
                SnakeSegmentation snake=new SnakeSegmentation(imgInfo.binarizedImage);//snake
                imgInfo.segments=snake.generateSegments();
                segmentationTime=System.currentTimeMillis()-preprocessingTime-startTime;
            //thinning
                imgInfo.thinnedSegments=thinning.thinSegments(imgInfo.segments);
                s1=new Scale(imgInfo.thinnedSegments);
                imgInfo.scaledSegments=s1.getScaledImg();
                thinningTime=System.currentTimeMillis()-preprocessingTime-segmentationTime-startTime;
                //////////////////////////////////////////////////////////////////////
                break;

            case WIKIPEDIA://done by Vinay
                ////////////////////////////////////////////////////////////////////////
                //Preprocessing
                imgInfo.grayScaledImage = bnryObj.convertOrgToGray(imgInfo.originalImage);
                imgInfo.binarizedImage = bnryObj.getBinaryImage(imgInfo.grayScaledImage,127);
                imgInfo.preprocessedImage=noiseRmvObj.getAdvDotRmvObj(imgInfo.binarizedImage,1700);
                preprocessingTime=System.currentTimeMillis()-startTime;
                //////////////////////////////////////////////////////////////////////////
                //Segmentation
              
                segObj=new Overlapped(4,28,imgInfo.preprocessedImage);//Remove segments of 4 X distance n width=28
                startTime = System.currentTimeMillis();
                imgInfo.segments=segObj.generateSegments();//getting the segments
                segmentationTime=System.currentTimeMillis()-preprocessingTime-startTime;
               //thinning
                imgInfo.thinnedSegments=imgInfo.segments;
                //imgInfo.thinnedSegments=thinning.thinSegments(imgInfo.segments);
                s1=new Scale(imgInfo.thinnedSegments);
                imgInfo.scaledSegments=s1.getScaledImg();
                thinningTime=System.currentTimeMillis()-preprocessingTime-segmentationTime-startTime;
                //////////////////////////////////////////////////////////////////////
                break;

            
            case SIXTEENBYTWO://partially
                 ////////////////////////////////////////////////////////////////////////
                //Preprocessing
                imgInfo.grayScaledImage =bnryObj.convertOrgToGray(imgInfo.originalImage);//Inverse image
                imgInfo.binarizedImage = bnryObj.getBinaryImage(imgInfo.grayScaledImage);
                imgInfo.preprocessedImage=bnryObj.getBinaryImage(noiseRmvObj.getRevGrayscaledObj(imgInfo.originalImage));
                preprocessingTime=System.currentTimeMillis()-startTime;
                //////////////////////////////////////////////////////////////////////////
                //Segmentation
                segObj=new Overlapped(19,imgInfo.preprocessedImage);//width=19
                //segObj.getProjection();
                imgInfo.segments=segObj.generateSegments();//getting the segments
                segmentationTime=System.currentTimeMillis()-preprocessingTime-startTime;
                imgInfo.scaledSegments=util.scale(imgInfo.segments);
                thinningTime=System.currentTimeMillis()-preprocessingTime-segmentationTime-startTime;
                break;
          
            case AOLMAIL://by manali
                ////////////////////////////////////////////////////////////////////////
                //Preprocessing
                imgInfo.grayScaledImage=bnryObj.convertOrgToGray(imgInfo.originalImage);
                imgInfo.binarizedImage=bnryObj.getBinaryImage(imgInfo.grayScaledImage);
                imgInfo.preprocessedImage=noiseRmvObj.getAdvDotRmvObj(imgInfo.binarizedImage,1570);                
                preprocessingTime=System.currentTimeMillis()-startTime;
                //////////////////////////////////////////////////////////////////////////
                //Segmentation
                SnakeSegmentation snake1=new SnakeSegmentation(imgInfo.preprocessedImage);
                imgInfo.segments=snake1.generateSegments();
                segmentationTime=System.currentTimeMillis()-preprocessingTime-startTime;
                //Thinning
                imgInfo.thinnedSegments=thinning.thinSegments(imgInfo.segments);
                s1=new Scale(imgInfo.thinnedSegments);
                imgInfo.scaledSegments=s1.getScaledImg();
                  thinningTime=System.currentTimeMillis()-preprocessingTime-segmentationTime-startTime;
                ///////////////////////////////////////////////////////////////////////
                break;
                                 
            case AVECMOBILE:// 14/20 coorect
                ////////////////////////////////////////////////////////////////////////
                //Preprocessing
                imgInfo.grayScaledImage=bnryObj.convertOrgToGray(imgInfo.originalImage);
                imgInfo.binarizedImage=bnryObj.getBinaryImage(imgInfo.grayScaledImage,215);
                imgInfo.preprocessedImage=noiseRmvObj.getThickWCharObj(imgInfo.binarizedImage);
                preprocessingTime=System.currentTimeMillis()-startTime;
                //////////////////////////////////////////////////////////////////////////
                //Segmentation
                segObj=new Overlapped(4,34,imgInfo.preprocessedImage);//remove segments of 4 Xdistance
               // segObj.getProjection();
                imgInfo.segments=segObj.generateSegments();//getting the segments
                segmentationTime=System.currentTimeMillis()-preprocessingTime-startTime;
              //thinning
                imgInfo.thinnedSegments=thinning.thinSegments(imgInfo.segments);
                s1=new Scale(imgInfo.thinnedSegments);
                imgInfo.scaledSegments=s1.getScaledImg();
                  thinningTime=System.currentTimeMillis()-preprocessingTime-segmentationTime-startTime;
                //////////////////////////////////////////////////////////////////////
                
                break;
                
            
                case BMACCESS://done full by kiran
                ////////////////////////////////////////////////////////////////////////
                //Preprocessing
                imgInfo.grayScaledImage=bnryObj.convertOrgToGray(imgInfo.originalImage);
                imgInfo.binarizedImage=bnryObj.getBinaryImage(imgInfo.grayScaledImage,95);
                imgInfo.preprocessedImage=noiseRmvObj.getAdvDotRmvObj(imgInfo.binarizedImage,1070);
                preprocessingTime=System.currentTimeMillis()-startTime;
                //////////////////////////////////////////////////////////////////////////
                //Segmentation
                segObj=new DisconnectedSegment(0,imgInfo.preprocessedImage);//Threshold =0
                segObj.getProjection();
                imgInfo.segments=segObj.generateSegments();//getting the segments
                segmentationTime=System.currentTimeMillis()-preprocessingTime-startTime;
                //thinning
                imgInfo.thinnedSegments=thinning.thinSegments(imgInfo.segments);
                s1=new Scale(imgInfo.thinnedSegments);
                imgInfo.scaledSegments=s1.getScaledImg();
                  thinningTime=System.currentTimeMillis()-preprocessingTime-segmentationTime-startTime;
                //////////////////////////////////////////////////////////////////////
                break;
                    
            case SHIKSHA://done full
                ////////////////////////////////////////////////////////////////////////
                //Preprocessing
                imgInfo.grayScaledImage=bnryObj.convertOrgToGray(imgInfo.originalImage);
                imgInfo.binarizedImage=bnryObj.getBinaryImage(imgInfo.grayScaledImage,100);
                imgInfo.preprocessedImage=noiseRmvObj.getAdvDotRmvObj(imgInfo.binarizedImage,1070);
                preprocessingTime=System.currentTimeMillis()-startTime;
                //////////////////////////////////////////////////////////////////////////
                //Segmentation
                segObj=new DisconnectedSegment(0,imgInfo.preprocessedImage);//Threshold =0
                segObj.getProjection();
                imgInfo.segments=segObj.generateSegments();//getting the segments
                segmentationTime=System.currentTimeMillis()-preprocessingTime-startTime;
                //thinning
                imgInfo.thinnedSegments=thinning.thinSegments(imgInfo.segments);
                s1=new Scale(imgInfo.thinnedSegments);
                imgInfo.scaledSegments=s1.getScaledImg();
                  thinningTime=System.currentTimeMillis()-preprocessingTime-segmentationTime-startTime;
                //////////////////////////////////////////////////////////////////////
                break;
            case YEBHI://done full by kiran
                ////////////////////////////////////////////////////////////////////////
                //Preprocessing
                imgInfo.grayScaledImage=bnryObj.convertOrgToGray(imgInfo.originalImage);
                imgInfo.binarizedImage=bnryObj.getBinaryImage(imgInfo.grayScaledImage,100);
                imgInfo.preprocessedImage=noiseRmvObj.getAdvDotRmvObj(imgInfo.binarizedImage,1070);
                preprocessingTime=System.currentTimeMillis()-startTime;
                //////////////////////////////////////////////////////////////////////////
                //Segmentation
                segObj=new DisconnectedSegment(0,imgInfo.preprocessedImage);//Threshold =0
                segObj.getProjection();
                imgInfo.segments=segObj.generateSegments();//getting the segments
                segmentationTime=System.currentTimeMillis()-preprocessingTime-startTime;
                //thinning
                imgInfo.thinnedSegments=thinning.thinSegments(imgInfo.segments);
                s1=new Scale(imgInfo.thinnedSegments);
                imgInfo.scaledSegments=s1.getScaledImg();
                  thinningTime=System.currentTimeMillis()-preprocessingTime-segmentationTime-startTime;
                //////////////////////////////////////////////////////////////////////
                break;
                
            case CCAVENUE:
                 ////////////////////////////////////////////////////////////////////////
                //Preprocessing
                imgInfo.grayScaledImage=bnryObj.convertOrgToGray(imgInfo.originalImage);
                imgInfo.binarizedImage=bnryObj.getBinaryImage(imgInfo.grayScaledImage);
                imgInfo.preprocessedImage =bnryObj.getBinaryImage(noiseRmvObj.getRevGrayscaledObj(imgInfo.originalImage),140);
                imgInfo.preprocessedImage=noiseRmvObj.getCharRepairedObj(imgInfo.preprocessedImage);
                preprocessingTime=System.currentTimeMillis()-startTime;
                //////////////////////////////////////////////////////////////////////////
                //Segmentation
                 segObj2=new EightConnectivity(imgInfo.preprocessedImage);
                imgInfo.segments=segObj2.generateSegments();
                segmentationTime=System.currentTimeMillis()-preprocessingTime-startTime;
                //Thinning
                imgInfo.thinnedSegments=thinning.thinSegments(imgInfo.segments);
                s1=new Scale(imgInfo.thinnedSegments);
                imgInfo.scaledSegments=s1.getScaledImg();
                  thinningTime=System.currentTimeMillis()-preprocessingTime-segmentationTime-startTime;
                //////////////////////////////////////////////////////////////////////
                break;
                
            case CHALLANSTATUS://11/20 images r correct Problem with H U and M
                ////////////////////////////////////////////////////////////////////////
                //Preprocessing
                imgInfo.grayScaledImage=bnryObj.convertOrgToGray(imgInfo.originalImage);
                imgInfo.binarizedImage=noiseRmvObj.getBinarizedImage(imgInfo.grayScaledImage);
                imgInfo.preprocessedImage=noiseRmvObj.getThickHCharObj(imgInfo.binarizedImage);
                imgInfo.preprocessedImage=noiseRmvObj.getThickHCharObj( imgInfo.preprocessedImage);
                            
                noiseRmvObj.getCharRepairedW2pxObj( imgInfo.preprocessedImage);
                noiseRmvObj.getCharRepairedW2pxObj( imgInfo.preprocessedImage);
                noiseRmvObj.getCharRepairedW2pxObj( imgInfo.preprocessedImage);
                            
                imgInfo.preprocessedImage=noiseRmvObj.getCharRepairedObj(imgInfo.preprocessedImage);
                noiseRmvObj.getCharRepairedW2pxObj(imgInfo.preprocessedImage);
                imgInfo.preprocessedImage=noiseRmvObj.getThickWCharObj( imgInfo.preprocessedImage);
                imgInfo.preprocessedImage=noiseRmvObj.getCharRepairedObj(imgInfo.preprocessedImage);
                imgInfo.preprocessedImage=noiseRmvObj.getThickWCharObj( imgInfo.preprocessedImage);
                imgInfo.preprocessedImage=noiseRmvObj.getThickHCharObj( imgInfo.preprocessedImage);
                preprocessingTime=System.currentTimeMillis()-startTime;
                //////////////////////////////////////////////////////////////////////////
                //Segmentation
                segObj=new Overlapped(8,44,imgInfo.preprocessedImage);//Threshold =0
                //segObj.getProjection();
                imgInfo.segments=segObj.generateSegments();//getting the segments
                segmentationTime=System.currentTimeMillis()-preprocessingTime-startTime;
               //thinning
                imgInfo.thinnedSegments=thinning.thinSegments(imgInfo.segments);
                s1=new Scale(imgInfo.thinnedSegments);
                imgInfo.scaledSegments=s1.getScaledImg();
                  thinningTime=System.currentTimeMillis()-preprocessingTime-segmentationTime-startTime;
                //////////////////////////////////////////////////////////////////////
                break;
                
          
              
           case EASYBM://done all by Kiran
                ////////////////////////////////////////////////////////////////////////
                //Preprocessing
                imgInfo.grayScaledImage = bnryObj.convertOrgToGray(imgInfo.originalImage);
                imgInfo.binarizedImage = bnryObj.getBinaryImage(imgInfo.grayScaledImage,182);
                imgInfo.preprocessedImage= imgInfo.binarizedImage ;
                preprocessingTime=System.currentTimeMillis()-startTime;
                //////////////////////////////////////////////////////////////////////////
                //Segmentation
                segObj=new DisconnectedSegment(0,imgInfo.binarizedImage);//Threshold =0
                segObj.getProjection();
                imgInfo.segments=segObj.generateSegments();//getting the segments
                segmentationTime=System.currentTimeMillis()-preprocessingTime-startTime;
               //thinning
               // imgInfo.thinnedSegments=thinning.thinSegments(imgInfo.segments);
                s1=new Scale(imgInfo.segments);
                imgInfo.scaledSegments=s1.getScaledImg();
                  thinningTime=System.currentTimeMillis()-preprocessingTime-segmentationTime-startTime;
                break;
 ////////////////////////////////////////////////////////////////////////
                
            case SPEAKINGTREE://done all by vin ay
               imgInfo.grayScaledImage =bnryObj.convertOrgToGray(imgInfo.originalImage);//Inverse image
                imgInfo.binarizedImage = bnryObj.getBinaryImage(imgInfo.grayScaledImage);
                imgInfo.preprocessedImage=bnryObj.getBinaryImage(noiseRmvObj.getRevGrayscaledObj(imgInfo.originalImage));
                preprocessingTime=System.currentTimeMillis()-startTime;
                //imgInfo.binarizedImage=(new InvertImage()).conversion(imgInfo.originalImage);
                //////////////////////////////////////////////////////////////////////////
                //Segmentation
                segObj=new DisconnectedSegment(0,imgInfo.preprocessedImage);//Threshold =0
                segObj.getProjection();
                imgInfo.segments=segObj.generateSegments();//getting the segments
                segmentationTime=System.currentTimeMillis()-preprocessingTime-startTime;
                 //thinning
                
                s1=new Scale(imgInfo.segments);
                imgInfo.scaledSegments=s1.getScaledImg();
                  thinningTime=System.currentTimeMillis()-preprocessingTime-segmentationTime-startTime;
                //////////////////////////////////////////////////////////////////////
                break;
            case CEOMAHARASHTRA://done all by Vinay
                imgInfo.grayScaledImage=bnryObj.convertOrgToGray(imgInfo.originalImage);//Inverse image
                imgInfo.binarizedImage= bnryObj.getBinaryImage(imgInfo.grayScaledImage);
                imgInfo.preprocessedImage=bnryObj.getBinaryImage(noiseRmvObj.getRevGrayscaledObj(imgInfo.originalImage));
                preprocessingTime=System.currentTimeMillis()-startTime;
                //imgInfo.binarizedImage=(new InvertImage()).conversion(imgInfo.originalImage);
                //////////////////////////////////////////////////////////////////////////
                //Segmentation
                 segObj=new DisconnectedSegment(0, imgInfo.preprocessedImage);//Threshold =0
                segObj.getProjection();
                imgInfo.segments=segObj.generateSegments();//getting the segments
                segmentationTime=System.currentTimeMillis()-preprocessingTime-startTime;
                 //thinning
                imgInfo.thinnedSegments=thinning.thinSegments(imgInfo.segments);
                s1=new Scale(imgInfo.thinnedSegments);
                imgInfo.scaledSegments=s1.getScaledImg();
                  thinningTime=System.currentTimeMillis()-preprocessingTime-segmentationTime-startTime;
                //////////////////////////////////////////////////////////////////////
                break;
                
            case SNAPSHOT://preprocessing needs to be done
                ////////////////////////////////////////////////////////////////////////
                //Preprocessing
                imgInfo.grayScaledImage = bnryObj.convertOrgToGray(imgInfo.originalImage);
                imgInfo.binarizedImage = bnryObj.getBinaryImage(imgInfo.grayScaledImage,200);//127
                preprocessingTime=System.currentTimeMillis()-startTime;
                //////////////////////////////////////////////////////////////////////////
                //Segmentation
                 segObj=new DisconnectedSegment(4,0,imgInfo.binarizedImage);//Threshold =0
                segObj.getProjection();
                imgInfo.segments=segObj.generateSegments();//getting the segments
                segmentationTime=System.currentTimeMillis()-preprocessingTime-startTime;
                imgInfo.scaledSegments=util.scale(imgInfo.segments);
                  thinningTime=System.currentTimeMillis()-preprocessingTime-segmentationTime-startTime;
                ///////////////////////////////////////////////////////////////////////
                break;
                
            case MYSPACE://not done segm
                ///////////////////////////////////////////////////////////////////////
                //Preprocessing
                imgInfo.grayScaledImage = bnryObj.convertOrgToGray(imgInfo.originalImage);
                imgInfo.binarizedImage = bnryObj.getBinaryImage(imgInfo.grayScaledImage,215);
                preprocessingTime=System.currentTimeMillis()-startTime;
                //////////////////////////////////////////////////////////////////////////
                //Segmentation
                segObj=new Overlapped(32,imgInfo.binarizedImage);
                imgInfo.segments=segObj.generateSegments();
                break;
                
                
             case WEBSPAMPROTECT://by Manali
                ////////////////////////////////////////////////////////////////////////
                //Preprocessing
                imgInfo.grayScaledImage = bnryObj.convertOrgToGray(imgInfo.originalImage);
                imgInfo.binarizedImage = bnryObj.getBinaryImage(imgInfo.grayScaledImage,200);
                imgInfo.preprocessedImage=noiseRmvObj.getEdgeRmvImg(imgInfo.binarizedImage);
                preprocessingTime=System.currentTimeMillis()-startTime;
                ////////////////////////////////////////////////////////////////////////////////
                // Segmentation
                segObj2=new EightConnectivity(imgInfo.preprocessedImage);
                imgInfo.segments=segObj2.generateSegments();
                segmentationTime=System.currentTimeMillis()-preprocessingTime-startTime;
                //Thinning
                imgInfo.thinnedSegments=thinning.thinSegments(imgInfo.segments);
                s1=new Scale(imgInfo.thinnedSegments);
                imgInfo.scaledSegments=s1.getScaledImg();
                  thinningTime=System.currentTimeMillis()-preprocessingTime-segmentationTime-startTime;
                //////////////////////////////////////////////////////////////////////
                break;
                 
            case DAILYMAILUK:
                ////////////////////////////////////////////////////////////////////////
                //Preprocessing
                imgInfo.grayScaledImage = bnryObj.convertOrgToGray(imgInfo.originalImage);
                imgInfo.binarizedImage = bnryObj.getBinaryImage(imgInfo.grayScaledImage);
                imgInfo.preprocessedImage= imgInfo.binarizedImage;
                preprocessingTime=System.currentTimeMillis()-startTime;
                break; 
                
           case EBAY://segmenation not done
                ////////////////////////////////////////////////////////////////////////
                //Preprocessing
                imgInfo.grayScaledImage = bnryObj.convertOrgToGray(imgInfo.originalImage);
                imgInfo.binarizedImage = bnryObj.getBinaryImage(imgInfo.grayScaledImage);
                imgInfo.preprocessedImage=imgInfo.binarizedImage ;
                preprocessingTime=System.currentTimeMillis()-startTime;
                break;
                
           case GATE://by manali
                ////////////////////////////////////////////////////////////////////////
                //Preprocessing
                imgInfo.grayScaledImage=bnryObj.convertOrgToGray(imgInfo.originalImage);
                imgInfo.binarizedImage=bnryObj.getBinaryImage(imgInfo.grayScaledImage,100);
                imgInfo.preprocessedImage=bnryObj.getBinaryImage(noiseRmvObj.getRevGrayscaledObj(imgInfo.originalImage),100);
                imgInfo.preprocessedImage=noiseRmvObj.getCharRepairedObj(imgInfo.preprocessedImage);
                preprocessingTime=System.currentTimeMillis()-startTime;
                ////////////////////////////////////////////////////////////////////////////////
                // Segmentation
                segObj2=new EightConnectivity(imgInfo.preprocessedImage);
                imgInfo.segments=segObj2.generateSegments();
                segmentationTime=System.currentTimeMillis()-preprocessingTime-startTime;
                //Thinning
                imgInfo.thinnedSegments=thinning.thinSegments(imgInfo.segments);
                s1=new Scale(imgInfo.thinnedSegments);
                imgInfo.scaledSegments=s1.getScaledImg();
                  thinningTime=System.currentTimeMillis()-preprocessingTime-segmentationTime-startTime;
                //////////////////////////////////////////////////////////////////////
                break;
               
           case HOTMAIL://by manali
                ////////////////////////////////////////////////////////////////////////
                //Preprocessing
                imgInfo.grayScaledImage = bnryObj.convertOrgToGray(imgInfo.originalImage);
                imgInfo.binarizedImage = bnryObj.getBinaryImage(imgInfo.grayScaledImage);
                imgInfo.preprocessedImage=imgInfo.binarizedImage ;
                preprocessingTime=System.currentTimeMillis()-startTime;
                ////////////////////////////////////////////////////////////////////////////////
                // Segmentation
                segObj2=new EightConnectivity(imgInfo.preprocessedImage);
                imgInfo.segments=segObj2.generateSegments();
                segmentationTime=System.currentTimeMillis()-preprocessingTime-startTime;
                //Thinning
                imgInfo.thinnedSegments=thinning.thinSegments(imgInfo.segments);
                s1=new Scale(imgInfo.thinnedSegments);
                imgInfo.scaledSegments=s1.getScaledImg();
                  thinningTime=System.currentTimeMillis()-preprocessingTime-segmentationTime-startTime;
                //////////////////////////////////////////////////////////////////////
                break;
            
                            
            case KUIZER://no segmenation
                ////////////////////////////////////////////////////////////////////////
                //Preprocessing
                imgInfo.grayScaledImage=bnryObj.convertOrgToGray(imgInfo.originalImage);
                imgInfo.binarizedImage=bnryObj.getBinaryImage(imgInfo.grayScaledImage);
                imgInfo.preprocessedImage=bnryObj.getBinaryImage(noiseRmvObj.getRevGrayscaledObj(imgInfo.originalImage)) ;
                preprocessingTime=System.currentTimeMillis()-startTime;
                break;
            
            
            case PIXELGROOVY://done all
                
                imgInfo.grayScaledImage=bnryObj.convertOrgToGray(imgInfo.originalImage);
                imgInfo.binarizedImage=bnryObj.getBinaryImage(imgInfo.grayScaledImage,70);
                imgInfo.preprocessedImage=bnryObj.getBinaryImage(noiseRmvObj.getRevGrayscaledObj(imgInfo.originalImage),70);
                imgInfo.preprocessedImage=noiseRmvObj.getEdgeRmvImg(imgInfo.preprocessedImage);
                preprocessingTime=System.currentTimeMillis()-startTime;
                ////////////////////////////////////////////////////////////////////////
                segObj=new DisconnectedSegment(0,imgInfo.preprocessedImage);
                segObj.getProjection();
                imgInfo.segments=segObj.generateSegments();
                segmentationTime=System.currentTimeMillis()-preprocessingTime-startTime;
                //thinning
                //imgInfo.thinnedSegments=thinning.thinSegments(imgInfo.segments);
                s1=new Scale(imgInfo.segments);
                imgInfo.scaledSegments=s1.getScaledImg();
                  thinningTime=System.currentTimeMillis()-preprocessingTime-segmentationTime-startTime;
                break;
                
            case INDIANEXPRESS: //done all
                 //Preprocessing
                imgInfo.grayScaledImage=bnryObj.convertOrgToGray(imgInfo.originalImage);
                imgInfo.binarizedImage=bnryObj.getBinaryImage(imgInfo.grayScaledImage,96);
                imgInfo.preprocessedImage=bnryObj.getBinaryImage(noiseRmvObj.getRevGrayscaledObj(imgInfo.originalImage),96);
                imgInfo.preprocessedImage=noiseRmvObj.getEdgeRmvImg(imgInfo.preprocessedImage);
                preprocessingTime=System.currentTimeMillis()-startTime;
                ////////////////////////////////////////////////////////////////////////
                segObj=new DisconnectedSegment(0,imgInfo.preprocessedImage);
                segObj.getProjection();
                imgInfo.segments=segObj.generateSegments();
                segmentationTime=System.currentTimeMillis()-preprocessingTime-startTime;
              //thinning
                //imgInfo.thinnedSegments=thinning.thinSegments(imgInfo.segments);
                s1=new Scale(imgInfo.segments);
                imgInfo.scaledSegments=s1.getScaledImg();
                  thinningTime=System.currentTimeMillis()-preprocessingTime-segmentationTime-startTime;
                //////////////////////////////////////////////////////////////////////
                
                break;
                
            case REDIFF://done
                ////////////////////////////////////////////////////////////////////////
                //Preprocessing
                imgInfo.grayScaledImage = bnryObj.convertOrgToGray(imgInfo.originalImage);
                imgInfo.binarizedImage = bnryObj.getBinaryImage(imgInfo.grayScaledImage);
                util.printBufferedImg(imgInfo.binarizedImage);
                imgInfo.preprocessedImage=noiseRmvObj.getVerLineRmvObj(imgInfo.binarizedImage) ;
              
                util.printBufferedImg(imgInfo.preprocessedImage);
                imgInfo.preprocessedImage=noiseRmvObj.getAdvDotRmvObj(imgInfo.preprocessedImage,1070);
                imgInfo.preprocessedImage=noiseRmvObj.getAdvDotRmvObj(imgInfo.preprocessedImage,1070);    
                imgInfo.preprocessedImage=noiseRmvObj.getAdvDotRmvObj(imgInfo.preprocessedImage,1070);
                imgInfo.preprocessedImage=noiseRmvObj.getAdvDotRmvObj(imgInfo.preprocessedImage,1070);
                imgInfo.preprocessedImage=noiseRmvObj.getSlant120ThickCharObj( imgInfo.preprocessedImage);
                imgInfo.preprocessedImage=noiseRmvObj.getSlant120ThickCharObj( imgInfo.preprocessedImage);
                imgInfo.preprocessedImage=noiseRmvObj.getSlant60ThickCharObj( imgInfo.preprocessedImage);
                imgInfo.preprocessedImage=noiseRmvObj.getSlant60ThickCharObj( imgInfo.preprocessedImage);
                
                
                imgInfo.preprocessedImage=util.addExtra(imgInfo.preprocessedImage, 50, 80);
                preprocessingTime=System.currentTimeMillis()-startTime;
                /////////////////////////////////////////////////////////////////////////////////
                //segmentation
                imgInfo.preprocessedImage=util.rotateCharacter(-45, imgInfo.preprocessedImage);
                segObj=new Overlapped(15,80,imgInfo.preprocessedImage);//Threshold =0
               // segObj.getProjection();
                imgInfo.segments=segObj.generateSegments();//getting the segments
                segmentationTime=System.currentTimeMillis()-preprocessingTime-startTime;
              //thinning
                imgInfo.thinnedSegments=thinning.thinSegments(imgInfo.segments);
                s1=new Scale(imgInfo.thinnedSegments);
                imgInfo.scaledSegments=s1.getScaledImg();
                  thinningTime=System.currentTimeMillis()-preprocessingTime-segmentationTime-startTime;
                //////////////////////////////////////////////////////////////////////
                /*BufferedImage [] b=new BufferedImage[1];
                b[0]=imgInfo.preprocessedImage;
                imgInfo.scaledSegments=util.scale(b,30,40);*/
                //util.printBufferedImg(imgInfo.preprocessedImage);
                break;
                
            default:
                System.out.println("Website not present.Check for spelling");
        }
        imgInfo.threshold=bnryObj.getThreshold();
        if(imgInfo.scaledSegments!=null)
        {
            decodedCaptcha=new Recognition().recognizeCharacters(imgInfo.scaledSegments);
           // System.out.println("Decoded Captcha image  ="+str);
          // System.out.println("Decoded Captcha "+k+"th image  ="+str);

        }
          recognitionTime=System.currentTimeMillis()-preprocessingTime-segmentationTime-thinningTime-startTime;
        
        long endTime = System.currentTimeMillis();
        util.fileWriter(imgInfo.binarizedImage, "ImgOutput\\PreprocessingImgOutput\\BinarizedImgOutput\\", "BinarizedImgOutput.jpg");
        util.fileWriter(imgInfo.grayScaledImage, "ImgOutput\\PreprocessingImgOutput\\GrayScaledImgOutput\\", "grayScaledOutput.jpg");
        util.fileWriter(imgInfo.segments, "ImgOutput\\SegmentationImgOutput\\SegmentedImg\\", "Segments");
         util.fileWriter(imgInfo.thinnedSegments,"ImgOutput\\ThinningImgOutput\\ThinnedImg\\","Thinned");
        util.fileWriter(imgInfo.scaledSegments, "ImgOutput\\ThinningImgOutput\\ScaledImg\\", "Segments");
        util.fileWriter(imgInfo.preprocessedImage,"ImgOutput\\PreprocessingImgOutput\\NoiseRmvImgOutput\\", "preProcessed.jpg");
      

        
        long total = endTime - startTime;
        ptime=Long.toString(preprocessingTime);
        stime=Long.toString(segmentationTime);
        thTime=Long.toString(thinningTime);
        rtime=Long.toString(recognitionTime);
        ttime=Long.toString(total);
//        StrengthImprovement si=new StrengthImprovement();
//si.getDBConnection();
//si.getSuggestion();
//        System.out.println("Preprocessing Time = " + preprocessingTime + " milliseconds");
//        System.out.println("Segmentation Time = " + segmentationTime + " milliseconds");
//        System.out.println("Total Time = " + total + " milliseconds");
   //}
     return decodedCaptcha;   
    }
    public ImageInfo getImageInfoObj()
    {
      return imgInfo;
    }
}