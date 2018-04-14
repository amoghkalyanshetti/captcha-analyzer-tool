/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CharacterRecognition;
import CAT.Main;
/**
 *
 * @author Shree Ganesh
 */
public class CharacterThreads extends Thread{
    int index;
    public CharacterThreads(int i)
    {
        this.index=i;
    }
    @Override
    public void run()
     {
          int Wt[][];
          
          int i=0,j=0,sum=0,sum1=0;
           // System.out.println("Thread for index"+index);
               Wt = Main.ss[index].wt;
               
           int I1[][]=new int[20][20];
                    for(i=0;i<20;i++)
                    {
                        for(j=0;j<20;j++)
                        {
                           //   System.out.print("Dhumssssss");
                     //       System.out.print("\n\n"+I[i][j]);
                           
                         //    ss[index].wt[i][j]=Wt[i][j];
                            I1[i][j]=ANN.I[i][j]*Wt[i][j];          
                            
                                sum = sum + I1[i][j];    //calculate Candidate Score

                            
                              if(Wt[i][j]>0)  
                            {
                           sum1 = sum1 + Wt[i][j];      //calculate idealWeightMatrixScore
                            }
                        }
                       
                    }
                  Wt=null;//changed
                Recognition.result[index]=(double)sum/sum1;             //calculate Recognition coefficient
              // System.out.println(" in method probability:" +result[index]+"              "+sum);
 
  
    }
    
}
