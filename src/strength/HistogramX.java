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
import javax.swing.JFrame;

public class HistogramX extends JFrame 
{
    BufferedImage image=null;
    int recordY[];

    public HistogramX(BufferedImage image)
    {
        this.image = image;
        this.setBackground(Color.WHITE);
        this.setTitle("Vertical Projection");
        setSize(new Dimension(200,200));
        this.setLocation(450, 450);
        recordY=new int[image.getHeight()];
        setBounds(400,525,400,200);
        setVisible(true);
        //addWindowListener(new MyWindowAdapter());
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
        for(int i=0;i<image.getHeight();i++)
        {
            recordY[i]=0;
            for(int j=0;j<image.getWidth();j++)
            {
           
                if(image.getRGB(j, i) != -1)
                {
                    recordY[i]+=1;
                }
            }
        }
   
       
        for(int i=0; i<recordY.length;i++)
        {
            //System.out.println(recordY[i]);
            //if(recordY[i]>0)
            g.drawLine(20, i+50,20+recordY[i],i+50);
        }
    }
    
  
    class MyWindowAdapter extends WindowAdapter
{
    @Override
    public void windowClosing(WindowEvent e)
    {
        setVisible(false);
    }
}
}


