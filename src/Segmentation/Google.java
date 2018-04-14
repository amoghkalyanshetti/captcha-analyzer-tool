package Segmentation;

import CAT.ImageInfo;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import preprocessing.Binarization;

public class Google {

    public static ImageProp main, captcha1, captcha2;
    public static BufferedImage binOut;
    private static int sepPoint;

    public static void main(String[] args) throws IOException {
        BufferedImage timage;


       timage=getBinaryImage("D:\\ImgDatabase\\Old\\CAPTCHA   without  noise\\google - 112\\8.jpg");
       long startTime = System.currentTimeMillis();
       
        Google st = new Google(timage);

    

        st.getSeparationPoint(main);//sepPoint
        st.getSegments();//separates to segments
        st.crop();
        //st.connected(new ImageProp(timage));


        //System.out.println("\nsep Point=" + sepPoint);


        long endTime = System.currentTimeMillis();
        long total = endTime - startTime;
        System.out.println("Time = " + total + " milliseconds");
    }

    static void printImage(BufferedImage segments[]) {

        for (int i = 0; i < segments.length; i++) {
            try {

                ImageIO.write(segments[i], "jpg", new File("ImgOutput\\SegmentationImgOutput\\ProcessingOfImg\\" + DisconnectedSegment.index + i + ".jpg"));
            } catch (IOException e) {
                System.out.println("Failed");
            }
        }
    }
    public Google()
    {
        
    }

    public Google(BufferedImage img) {
        main = new ImageProp(img);
        main.Properties();
    }
     static BufferedImage getBinaryImage(String filename) throws IOException
     {
         ImageInfo imgInfo=new ImageInfo();
     try{ 
         
     imgInfo.originalImage=ImageIO.read(new File(filename));
     Binarization bnryObj = new Binarization();
     imgInfo.grayScaledImage = bnryObj.convertOrgToGray(imgInfo.originalImage);
     imgInfo.binarizedImage = bnryObj.getBinaryImage(imgInfo.grayScaledImage,85);
       
        try {

           ImageIO.write(imgInfo.binarizedImage, "jpg", new File("ImgOutput\\SegmentationImgOutput\\ProcessingOfImg\\" + "binarized" + ".jpg"));

        } catch (IOException e) {
            System.out.println("Failed");
        }    
            
     }
     catch(Exception e)
     {
           
     }
     return imgInfo.binarizedImage;
        
     }

    void getSeparationPoint(ImageProp captcha) {
        int n[] = captcha.arrayOfPixel;
        int start = 0, zeroes = 0;
        boolean got = false;
        for (int i = 0; i < n.length; i++) {
            if (n[i] == 0) {
                if (got == false) {
                    int k = 1;
                    int index = i;
                    got = true;
                    while (n[++i] == 0 && i < n.length) {
                        ++k;
                    }
                    if (k > zeroes) {
                        zeroes = k;
                        start = index;
                    }
                }
            } else {
                got = false;
            }
        }
        start = start + main.Xstart;
        sepPoint = start + (int) zeroes / 2;//middle point
    }

    void getSegments() {
        BufferedImage image = main.main;
        main.segments = new BufferedImage[2];
        main.segments[0] = (captcha1 = new ImageProp(image.getSubimage(main.Xstart, 0, sepPoint - main.Xstart, main.hh))).main;
        main.segments[1] = (captcha2 = new ImageProp(image.getSubimage(sepPoint, 0, main.Xend - sepPoint, main.hh))).main;
        captcha1.Properties();
        captcha2.Properties();
        captcha1.countOfZero();
        captcha2.countOfZero();
        try {

            ImageIO.write(captcha1.main, "jpg", new File("ImgOutput\\SegmentationImgOutput\\ProcessingOfImg\\" + 1 + ".jpg"));
            ImageIO.write(captcha2.main, "jpg", new File("ImgOutput\\SegmentationImgOutput\\ProcessingOfImg\\" + 2 + ".jpg"));
        } catch (IOException e) {
            System.out.println("Failed");
        }
    }

    void crop() {
        if (captcha1.noOfZero > captcha2.noOfZero)//counting the no. of segments found
        {
            System.out.println("c1 is greater");
            captcha1.segments = new Overlapped(20, captcha1.main).generateSegments();
            //dynamic method dispatch
            printImage(captcha1.segments);
            connected(captcha2);
        } else {
            System.out.println("c2 is greater");
            captcha2.segments = new Overlapped(20, captcha2.main).generateSegments();
            printImage(captcha2.segments);
            connected(captcha1);

        }
    }

    public void connected(ImageProp img) {
        img.main = addExtra(img.main, 50, 50);
       // img.getYCoord();
       // int middle = img.startYCoord + ((img.endYCoord - img.startYCoord + 1) / 2);
       // System.out.println("Middle..............." + middle);
        //   temp=rotateCharacter(-30,temp);to rotate the image 
       /* DisconnectedSegment st = new Overlapped(20, temp);
        /* st.getArrayOfPixels();
         st.getXCoord();
         st.getYCoord();
        System.out.println("========================================================================================");
        img.segments = st.generateSegments();
        System.out.println("========================================================================================");
        printImage(img.segments);*/
        img.Properties();
        int start=0,end=0;
        for(int i=0;i<img.hh;i++)//to find start Ycoord of first letter
        {
            if(img.main.getRGB(img.Xstart+5, i)==0xff000000)
            {
                start=i;
                break;
            }
        }
        for(int i=img.hh-1;i>=start;i--)//to find end Y coord of first letter
        {
            if(img.main.getRGB(img.Xstart+5, i)==0xff000000)
            {
                end=i;
                break;
            }
        }
        System.out.println("Start and end value: "+start+"  "+end );
        int x1,x2,y1,y2;
        x1=x2=y1=y2=0;
         for(int i=img.Xstart;i<img.ww;i++)//finding x coord
        {
            if(img.main.getRGB(i,start+2)==0xff000000)
            {
                x1=i;
                y1=start+2;
                break;
            }
        }
        for(int i=img.Xstart;i<img.ww;i++)
        {
            if(img.main.getRGB(i,end-2)==0xff000000)
            {
                x2=i;
                y2=end-2;
                break;
            }
        }
        double slope=(y2-y1)/(double)(x2-x1);
        double angle=Math.toDegrees(Math.atan(slope));
        System.out.println("Angle prev:"+angle);
        angle=90.00-angle;
       /* AffineTransform at = new AffineTransform();
         at.rotate(Math.toRadians(angle));
         Graphics2D g2d = img.main.createGraphics() ;
              g2d.drawImage(img.main, at, null);*/
       img.main=rotateCharacter(-(int)angle,img.main);
       // Overlapped o=new Overlapped(24,img.main);
       /* DisconnectedSegment ds=new DisconnectedSegment(0,img.main);
        ds.getProjection();
        System.out.println("===============second img===============");
        img.segments=ds.generateSegments();
        printImage(img.segments);*/
        System.out.println("x1:"+x1+" y1:"+y1+" x2:"+x2+" y2:"+y2+" angle="+angle+" slope="+slope);

        try {

           ImageIO.write(img.main, "jpg", new File("ImgOutput\\SegmentationImgOutput\\ProcessingOfImg\\" + 10000 + ".jpg"));

        } catch (IOException e) {
            System.out.println("Failed");
        }
    }

    BufferedImage addExtra(BufferedImage img, int f, int b) // add extra pixels to image 
    {
        int h = img.getHeight();
        int w = img.getWidth();
        BufferedImage out = new BufferedImage(w + f + b, h, BufferedImage.TYPE_INT_RGB);
        out = wightBackground(out);
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                out.setRGB(i + f, j, img.getRGB(i, j));
            }
        }
        return out;
    }
    ////////////////////////////////////wight background/////////////////////////////////////////////////////////////////////////////

    BufferedImage wightBackground(BufferedImage img) //
    {
        int i, j;
        int h = img.getHeight(), w = img.getWidth();
        for (i = 0; i < w; i++) {
            for (j = 0; j < h; j++) {
                img.setRGB(i, j, 0xffffff);
            }
        }
        return img;
    }

    BufferedImage rotateCharacter(int degrees, BufferedImage img) // rotate the captcha
    {
        int width = img.getWidth(), height = img.getHeight();
        BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        double rad = Math.toRadians(degrees);
        wightBackground(out);
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                int y = j;
                int x = (int) (i - (height - j) * Math.tan(rad));

                if (x >= 0 && x < width) {
                    out.setRGB(x, y, img.getRGB(i, j));
                }
            }
        }
        return out;
    }
}
