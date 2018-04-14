/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import com.thehowtotutorial.splashscreen.JSplash;
import java.awt.Color;
import javax.swing.JFrame;

/**
 *
 * @author amoghk
 */
public class Start extends JFrame
{
    /*Starting point of CAT*/
    public static void main(String args[])
    {
        
        Start s=new Start();
        s.splashScreen();
        s.setVisible(false);
        new GUI().setVisible(true);
        
        
        
    }
     public void splashScreen()
    {
        try
        {
      
        JSplash splash=new JSplash(GUI.class.getResource("splash.png"),true,true,false,"ver 2.1",null,Color.BLACK,new Color(38,39,61));
        splash.splashOn();
        //splash.setProgress(20,"init");
        
        Thread.sleep(1000);
         splash.setProgress(20,"Loading preprocessing module....");
        Thread.sleep(1000);
         splash.setProgress(40,"Loading segmentation module...");
        Thread.sleep(1000);
         splash.setProgress(60,"Loading character recognition module....");
        Thread.sleep(1000);
         splash.setProgress(90,"Setting up GUI...");
        Thread.sleep(1000);
        splash.setProgress(100,"All done!");
        Thread.sleep(1000);
        splash.splashOff();
        }
        catch(Exception e)
        {
            System.out.println("Error");
        }
    }
    
}
