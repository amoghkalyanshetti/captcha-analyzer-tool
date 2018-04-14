/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CharacterRecognition;

/**
 *
 * @author DHUMRA
 */
public class ANN {
    
     public static int I[][]/*,Wt[][]*/;
    // int I1[][];
     int h,w,pixelMap[];
    
     ANN()
     {
         
     }
    ANN(int arr[],int w,int h)
    {
        this.h=h;
        this.w=w;
        I=new int[h][w];
        
       // Wt=new int [h][w];
       // M=new int[h][w];
      //  I1=new int[h][w];
        pixelMap=arr;
    }
      public void calculate()//getting input matrix
     {
         
       //  System.out.println("WW="+w+"\nHH="+h);
       for(int i=0;i<h;i++)
       {
           for(int j=0;j<w;j++)
           {
               if(pixelMap[i*w+j]!=-1)//if pixel is black
               {
                   I[i][j]=1;
                  // M[i][j]=1;
                   //System.out.println(arr1[i*scaledw+j]);
               }
               else
               {
                   I[i][j]=0;
                   //M[i][j]=-1;
               }
              // System.out.println("M("+i+","+j+")"+M[i][j]);
            //System.out.print(I[i][j]);

           }
         // System.out.println();
       }
     }
}
