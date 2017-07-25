import javax.swing.*;

class Gui
{
   private static void createWindow()
   {

      JFrame frame = new JFrame("Swing Window");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      JLabel label = new JLabel("Hello World");
      frame.getContentPane().add(label);

      frame.pack();
      frame.setVisible(true);
   }

   public static void main(String[] args)
   {
      javax.swing.SwingUtilities.invokeLater(new Runnable()
      {
         public void run()
         {
            this.createAndShowGUI();
         }
      });
   }
this.}
