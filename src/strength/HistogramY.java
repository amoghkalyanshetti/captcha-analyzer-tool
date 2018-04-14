package strength;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Manali
 */
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class HistogramY extends Frame 
{
    BufferedImage image=null;
    int recordX[];
    //int hh;
          
    public HistogramY(BufferedImage image)
    {
      
        this.image = image;
        this.setBackground(Color.WHITE);
        setSize(new Dimension(200,200));
        this.setTitle("Horizontal Projection");
        this.setLocation(850, 450);
        recordX=new int[image.getWidth()];
        setBounds(810,525,400,200);
        setVisible(true);
     //this.setSize(new Dimension(image.getWidth(), image.getHeight()));
        
        addWindowListener(new MyWindowAdapter());
    }
    synchronized public void changeImage(BufferedImage image)
    {
        this.image=image;
        repaint();
    }
    @Override
    public void paint(Graphics g)
    {
         g.clearRect(0,0, this.getWidth(),this.getHeight());
        for(int i=0;i<image.getWidth();i++)
        {
            recordX[i]=0;
            //System.out.println("hi there");
            for(int j=0;j<image.getHeight();j++)
            {
                if(image.getRGB(i,j) != -1)
                {
                    recordX[i]+=1;
                }
            }
       }
       
      
        for(int i=0; i<recordX.length;i++)
        {
          // System.out.println("X  "+recordX[i]);
         /*  if(recordX[i]==0)
           {
               g.setColor(Color.red);
               g.drawLine(i+10, 280, i+10, 150);
               g.setColor(Color.BLACK);
           }
           else*/
           
            g.drawLine(i+10, 180,i+10,180-recordX[i]);
          
           //System.out.println("dsfd"+(600-recordX[i]));
        }
    }
   /* public static void main(String args[])
    {
        BufferedImage image=null;  
        try
        {
            image= ImageIO.read(new File("C:\\Users\\Manali\\Dropbox\\Study Folder\\Vinay Desai 1306026\\NoiseRmvImgOutput(TINYPIC)\\preProcessed3.jpg"));
        }
        catch(Exception e)
        {
            System.out.println("Error");
        }
        HistogramY win = new HistogramY(image);
    }*/
    class MyWindowAdapter extends WindowAdapter
    {
        @Override
        public void windowClosing(WindowEvent e)
        {
            setVisible(false);
        }   
    }
}




