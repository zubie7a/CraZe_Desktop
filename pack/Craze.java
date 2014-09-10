package pack;
import javax.swing.*;
import java.awt.*   ;

	public class Craze extends JFrame{
		public static final long serialVersionUID = -1L; //What does this do?
		public Panopt panopt;                            //The panel for the options
		public Pangra pangra;                            //The panel for the graphics/canvas
		public Dimension dim;                            //The dimensions of the minimum window size
		
		public Craze(){	     
			dim = new Dimension(1164,742)               ;//Please, don't ask why the semicolons are like this. I don't really understand why I do it
			this.setTitle("CraZe")                      ;//The name of the window
			//this.setMinimumSize(dim)                  ;//This program was started on Monday, January 30th, 2012, and the current date is Monday, February 13th, 2012
			panopt = new Panopt(this)                   ;//I think I should be studying a bit more for Calculus/Physics/Compilers exams, but doing this is just too fun
			pangra = new Pangra(this)                   ;//I also don't really understand why I'm doing this kind of comments. 
			setLayout(new BorderLayout())               ;	
			this.add(panopt, BorderLayout.WEST)         ;//Option panel is located at the WEST. Change it to EAST if it makes you feel comfortable
			this.add(pangra, BorderLayout.CENTER)       ;//Graphic panel is located at the CENTER, after all, it takes most of the window space possible
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
			this.setResizable(true)                     ;//By the way, my name is Santiago Zubieta
			this.setFocusable(true)                     ;//I'm a student of Systems Engineering (sort of CS) in EAFIT University, 3rd Semester
			this.setVisible(true)                       ;//If you are reading this, then you thought my program was nice, and so are you
		}
		
		public static void main(String[]Args){
			Craze craze = new Craze();                   //This pretty much sets the whole thing into motion 
		}  
}
