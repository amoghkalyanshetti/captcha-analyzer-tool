/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package strength;

import CAT.CommonUtilityClass;
import CAT.ImageInfo;
import CAT.Main;
import GUI.StrengthImprovementCaptcha;
import Segmentation.FloodFill;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import preprocessing.Binarization;
import preprocessing.NoiseRemoval;

/**
 *
 * @author Shree Ganesh
 */
public class StrengthImprovement 
{
    Main main;
    Connection con;
    ImageInfo imgInfo;
    public StrengthImprovement(Main main)
    {
      this.main=main;
      imgInfo=this.main.getImageInfoObj();
    }
    public static void main(String args[])
    {
        
       // new StrengthImprovement().getDBConnection();
        File file1=new File("C:\\Users\\dell1\\Desktop\\1.jpg");
        BufferedImage originalImage=null;
        try {
            originalImage = ImageIO.read(file1);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        CommonUtilityClass util=new CommonUtilityClass();
        //util.fileWriter(changeColor(originalImage, 0, 0,4414042),"D:\\","img1.jpg");
//        for(int i=0;i>=-60;i-=5)
//        {
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(StrengthImprovement.class.getName()).log(Level.SEVERE, null, ex);
//            }
//       new StrengthImprovement(). rotate(originalImage,i);
//        }*/
    }
    public BufferedImage distortImage(BufferedImage orignalImg)
    {  
        Random r=new Random();
        BufferedImage img=new BufferedImage(orignalImg.getWidth(),orignalImg.getHeight(),BufferedImage.TYPE_INT_RGB);
        for(int i=0;i<img.getWidth();i++)
        {
          for(int j=0;j<img.getHeight();j++)
		{ 
                    if(r.nextInt(2)==1)
                    {
                    img.setRGB(i,j,orignalImg.getRGB(i, j));
                    }
                    else
                    {
                     img.setRGB(i,j,Color.WHITE.getRGB());
                    }
		}
        }
        r=null;
        return img;
    }
    public  BufferedImage changeBackground(BufferedImage img,BufferedImage backImg)
    {
       Binarization bnrObj=new Binarization();
       BufferedImage imgObj=bnrObj.convertOrgToGray(img);
       imgObj=bnrObj.getBinaryImage(imgObj,imgInfo.threshold);
       
       Image scaled=backImg.getScaledInstance(img.getWidth(),img.getHeight(), BufferedImage.SCALE_SMOOTH);
       BufferedImage temp=new BufferedImage(img.getWidth(),img.getHeight(),BufferedImage.TYPE_INT_RGB);
       Graphics2D g = temp.createGraphics();
       g.drawImage(scaled,0, 0, null);
       
       for(int i=0;i<imgObj.getHeight();i++)
          for(int j=0;j<imgObj.getWidth();j++)
          {
             if(new Color(imgObj.getRGB(j, i)).getRed()!=255)
                temp.setRGB(j,i,img.getRGB(j, i));
          }    
       return temp;
    }
    public BufferedImage changeColor(BufferedImage img,int x,int y,int c)
   {
       //System.out.println(img.getHeight()+"  "+img.getWidth()+"Orignal Entered Function");
      
       
       Binarization b=new Binarization();
      
       Color colorObj=new Color(img.getRGB(x,y));
       int  gray     = ((int)(0.21 * colorObj.getRed() + 0.71 * colorObj.getGreen() + 0.07 *  colorObj.getBlue()));
       int newPixel  =(new CommonUtilityClass()).colorToRGB(colorObj.getAlpha(), gray,gray,gray);
       if((new Color(newPixel).getRed())>imgInfo.threshold)
           newPixel=-1; 
       else
           newPixel=-16777216;
       
       
       //passing grayscaled image,desired color(green) ,
       FloodFill f=new FloodFill(img,c,newPixel);
       //scaling the boinarized image
       
       Image scaled=(imgInfo.binarizedImage).getScaledInstance(img.getWidth(),img.getHeight(), BufferedImage.SCALE_SMOOTH);
       BufferedImage temp=new BufferedImage(img.getWidth(),img.getHeight(),BufferedImage.TYPE_INT_RGB);
       Graphics2D g = temp.createGraphics();
       g.drawImage(scaled,0, 0, null); 
       
       
       f.fill(x,y,temp);
      // System.runFinalization();
       return img;
       
   }
    public void getDBConnection()
    {
        try
		{
		//System.out.println("df");
			Class.forName("com.mysql.jdbc.Driver");
			String database="jdbc:mysql://localhost:8082/CAT";
			 con=DriverManager.getConnection(database,"root","amogh123");
			
		}
		catch(Exception e)
		{
		 System.out.println("There was an exception "+e);
		  
		}
    }
    public void getSuggestion()
    {
        int i=0;
        String trackedMethods[]=new String[Main.tracker.size()];
        String suggestion;
        try
		{
                   // String trackedMethods[]=new String[Main.tracker.size()];
                    Main.tracker.copyInto(trackedMethods);//getting suggestion as string
                  
			Statement st=con.createStatement();
                     for(i=0;i<trackedMethods.length;i++)
                    {
                       // System.out.println(trackedMethods[i]);
                    
			ResultSet res=st.executeQuery("select suggestion from suggestiontable where id='"+trackedMethods[i]+"'");
                        while(res.next())
                        {
                           System.out.println(suggestion=res.getString("suggestion"));//<======String to be printed in TextArea
                           StrengthImprovementCaptcha.setSuggestion(suggestion);
                        }
                    }
			
		}
		catch(Exception e)
		{
		 System.out.println("There was an exception for entry "+trackedMethods[i]);
		  
		}
    }
    public BufferedImage drawLine(BufferedImage orignalImg,int x1,int y1,int x2,int y2,Color color,int pattern)
    {
        System.out.println("Width = "+orignalImg.getWidth()+"\nHeight = "+orignalImg.getHeight());
        //System.out.println("in drawLine 1");
        float dashingPattern[][]={
                                   {2f,2f},
                                   {10f, 4f},
                                   {10f, 10f, 1f, 10f}
                                 };
        Stroke s1=new BasicStroke(2f, BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER, 1.0f, dashingPattern[pattern], 2.0f);
        Graphics2D g=orignalImg.createGraphics();
        g.setColor(color);
        g.setStroke(s1);
        g.drawLine(x1, y1, x2, y2);
          //System.out.println("in drawLine 2");
        return orignalImg;
    }
    public BufferedImage getInvertImg(BufferedImage originalImg)
    {
        Binarization bnrObj=new Binarization();
        NoiseRemoval noiseRmvObj = new NoiseRemoval();
        return bnrObj.getBinaryImage(noiseRmvObj.getRevGrayscaledObj(originalImg));
    }
    public BufferedImage addDot(BufferedImage orignalImg,int x,int y)//manual
    {
       //temporary
       //BufferedImage img=new BufferedImage(orignalImg.getWidth(),orignalImg.getHeight(),BufferedImage.TYPE_INT_RGB);
//        for(int i=0;i<img.getWidth();i++)
//        {
//          for(int j=0;j<img.getHeight();j++)
//		{
        try
        {
             orignalImg.setRGB(x,y,Color.black.getRGB());
             orignalImg.setRGB(x,y+1,Color.black.getRGB());
             orignalImg.setRGB(x+1,y,Color.black.getRGB());
             orignalImg.setRGB(x-1,y,Color.black.getRGB());
             orignalImg.setRGB(x,y-1,Color.black.getRGB());
        }
        catch(Exception e){}
             
//          }
//        }
       return orignalImg;
		}
    public  BufferedImage addDot(BufferedImage orignalImg,int max_val)//automatic
    {
        BufferedImage img=new BufferedImage(orignalImg.getWidth(),orignalImg.getHeight(),BufferedImage.TYPE_INT_RGB);
        for(int i=0;i<img.getWidth();i++)
        {
          for(int j=0;j<img.getHeight();j++)
		{
                   img.setRGB(i,j,orignalImg.getRGB(i, j));
		}
        }
        int min=5;
        int max=max_val;
        for(int j=0;j<orignalImg.getHeight();j+=(int)(Math.random() * (max - min) + min))
        {
           for(int i=0;i<orignalImg.getWidth();i+=(int)(Math.random() * (max - min) + min))
            {
            try
		{
                Random rnd=new Random();
                if(rnd.nextBoolean())
                   img.setRGB(i,j,(int)(Math.random() * 0x1000000));
                if(rnd.nextBoolean())
                   img.setRGB(i,j-1,(int)(Math.random() * 0x1000000));
                if(rnd.nextBoolean())
                   img.setRGB(i,j+1,(int)(Math.random() * 0x1000000));
                if(rnd.nextBoolean())
                  img.setRGB(i-1,j,(int)(Math.random() * 0x1000000));
                if(rnd.nextBoolean())
                  img.setRGB(i+1,j,(int)(Math.random() * 0x1000000));
                if(rnd.nextBoolean())
                  img.setRGB(i+1,j+1,(int)(Math.random() * 0x1000000));
                if(rnd.nextBoolean())
                  img.setRGB(i-1,j-1,(int)(Math.random() * 0x1000000));
                if(rnd.nextBoolean())
                  img.setRGB(i+1,j-1,(int)(Math.random() * 0x1000000));
                if(rnd.nextBoolean())
                  img.setRGB(i-1,j+1,(int)(Math.random() * 0x1000000));
                  
                }
                catch(Exception e){}
            }
        }
        //CommonUtilityClass util=new CommonUtilityClass();
        //util.fileWriter(img, "D:\\Dots\\", "dotimg"+max_val+".jpg");
        return img;
    }

    public BufferedImage rotate(BufferedImage originalImg,int degree)//only for 60 to -60
    { 
        int f,b;
        if(degree >=0)//positive degree
        {
            f=degree*2;
            b=0;
        }
        else //negative degree
        {
            f=0;
            b=(-degree)*2;
        }
      /*  b=60*2;
        f=60*2;*/
        BufferedImage newImg = new BufferedImage(originalImg.getWidth()+f+b,originalImg.getHeight(), BufferedImage.TYPE_INT_RGB);
       CommonUtilityClass util=new CommonUtilityClass();
       newImg=util.addExtra(originalImg,f,b);
      // System.out.println("f  ="+f+" b="+b);
       newImg=util.rotateCharacter(degree, newImg);
       //util.fileWriter(newImg, "D:/", "RotatedImg.jpg");
       return newImg;
    }
    
}
