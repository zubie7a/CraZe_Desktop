package pack;

import java.awt.event.MouseMotionListener;
import java.awt.image.WritableRaster     ;
import java.awt.image.RenderedImage      ;
import java.awt.event.MouseListener      ;
import java.awt.image.BufferedImage      ;
import java.awt.image.ColorModel         ;
import java.awt.event.MouseEvent         ;
import javax.swing.JOptionPane           ;
import java.awt.RenderingHints           ;
import javax.swing.JComponent            ;
import javax.imageio.ImageIO             ;
import java.io.IOException               ;
import java.awt.Graphics2D               ;
import java.util.ArrayList               ;
import java.awt.Graphics                 ;
import java.util.Stack                   ;
import java.awt.Color                    ;
import java.awt.Image                    ;
import java.io.File                      ;

	public class Pangra extends JComponent implements MouseListener, MouseMotionListener {
		private static final long serialVersionUID = 1L;
		private ArrayList<Integer> listX; // stores all the X coordinates the mouse is registered at, for the Free-Style fill function
		private ArrayList<Integer> listY; // stores all the Y coordinates, because Free-Style fill takes advantage of the fillPolygon function 
		public ArrayList<Integer> centX ; // stores the X coordinate of the center
		public ArrayList<Integer> centY ; // stores the Y coordinate of the center
		                                  // ArrayLists because in a possible future I'm contemplating the idea of having multiple centers.. Kaleidoscope?!
		private Stack<Image> stackIzq   ; // left stack for Undo operations
		private Stack<Image> stackDer   ; // right stack for Redo operations
		private double bordLineH[][]    ; // a 2x2 grid, storing the x,y values of 2 different points, which are a rect, for 'Horizontal Line'
		private double prevLineH[][]    ; // a 2x2 grid, storing the x,y values of 2 different points, which are a rect, for 'Horizontal Line'
		private double bordLine[][]     ; // a 2x2 grid, storing the x,y values of 2 different points, which are a rect, for 'Vertical Line'
		private double prevLine[][]     ; // a 2x2 grid, storing the x,y values of 2 different points, which are a rect, for 'Vertical Line'
		                                  // the 'prev's are because they store the value of the previous line, for the sake of connecting the centers/borders
		private boolean visit[][]       ; // a grid with the size of the image to store visited cells while filling. I don't like this filling method...
		private double trip[][]         ; // a 3x2 grid which stores the three x,y points of a triangle (original)
		private double trid[][]         ; // a 3x2 grid which stores the three x,y points of a triangle (rotation purposes)
		private double quad[][]         ; // a 4x2 grid which stores the four x,y points of a rectangle (original)
		private double quap[][]         ; // a 4x2 grid which stores the four x,y points of a rectangle (rotation purposes)
		private int vertexCount         ; // amount of points in the 'polygon' described by the 'Free-Style Fill' function
		private int angleCount          ; // the varying angle when Rotating Brush is selected
		private double lind[]           ; // storing the first mouse position purposes
		private double pnnv[]           ; // storing the clone of that point for rotating purposes
		private double posd[]           ; // another rotating purpose array
		private double pred[]           ; // more rotating purpose array
		private double angle            ; // the angle used for rotation
		private boolean turn            ; // you did not see this. this really causes a intentional lag (one time it paints, the other it does not,
		                                  // because the program registers too much points so images are so close to another that shapes are indistinguishable 
		private boolean hold            ; // indicates you are pressing the center and may move it
		private boolean move            ; // indicates you may move the image around
		private Image image             ; // temporal image that takes the first image in the Undo stack, allows you to edit, and then pushes the new image inside
		private String temp             ; // indicates in a ###(X),###(Y) way the position of the center
		private Graphics k              ; // graphic component that will be related to the underlying image
		private int origin              ; // value of the first cell that was clicked while using.... BUCKET FILL
		private int posv[]              ; // stores current position of the mouse
		private int posc[]              ; // stores current position of the center (may be different, lets say, at 'Lines From Start', its the half point between
		                                  // the starting coordinates and the mouse coordinates)
		private int prev[]              ; // stores previous position of the mouse
		private int prec[]              ; // stores previous position of the center
		private int linc[]              ; // stores starting point for 'XXXX From Start' purposes
		private double ny1              ; // these will manage the coordinates of the rotated points of many brushes
		private double ny2              ;
		private double ny3              ;
		private double ny4              ;
		private double nx1              ;
		private double nx2              ;
		private double nx3              ;
		private double nx4              ;
		private int xShift              ; // this indicates the horizontal shift between the center of the real image, and the center of the window. it happens that
		                                  // on the window, the northwest corner, is the 0,0 point, and when you use 'Drag Image Around', you must calculate a shift
		                                  // so what you draw on the window (in respect to the static 0,0 point) gets properly translated to the underlying image point
		public int yShift              ;
		public int orXShf              ; // these do not change when moving, they are the ideal xShift and yShift to have the centers of the window's canvas
		public int orYShf              ; // and the underlying (real) image's center aligned
		public int recT                ; // stores a copy of the Brush Size value, in case you want to alter it with 'Variable Size' without modifying the original
		public int mosX                ; // the real x position of the current center's coordinates in respect to the real image center's x
		public int mosY                ; // the real x position of the current center's coordinates in respect to the real image center's x
		
		private int deltaX              ; // these six are used for doing calculations while dragging the center or the image around
		private int deltaY              ; // deltas are calculated according to the mouse movement
		private int w                   ; 
		private int x                   ;
		private int y                   ;
		private int z                   ;

		
		public int horizontal           ; // width of the real image
		public int vertical             ; // heigth of the real image
		public Color currBCol           ; // brush color value
		public Color currWCol           ; // background color value
		public Color tempBCol           ; // stores brush color value for 'Shifting Colors' use
		public boolean press            ; // indicates the mouse is pressed
		public boolean symme            ; // indicates symmetry is enabled
		public boolean limit            ; // indicates the limits of the Variable Size'd brush are reach
		public boolean rotar            ; // indicates that 'Rotating Brush' is enabled
		public boolean varia            ; // indicates that 'Variable Size' is enabled
		public boolean bordr            ; // indicates that 'Draw Borders' is enabled
		public boolean conne            ; // indicates that 'Connected Center' is enabled
		public boolean color            ; // indicates that 'Shifting Colors' is enabled
		public double value             ; // copy of the Spin amount number 
		public String state             ; // indicates currently selected tool
		public boolean alt              ; // indicates that an altered angle may be used or not
		public Craze craze              ; // the (God)Father
		public int tempCol              ; // temporal color for drawing the center in white without losing the current color
		public int recS                 ; // unalterable value of the original Brush Size value, for limit calculating purposes
		public int num                  ; // value of real rotations to be done (for 3 spins, it rotates what you draw two other times, 120 degrees apart)
		public int tmp                  ; // stores the recT value for a little trick of a 1 pixel original side at the 'Square From Start' tool
		                                  // otherwise, when doing 'Square From Start', it would work normally but the very first click would draw a square
		                                  // with side length equal to Brush Size 
		
		public boolean redi;
		public boolean grei;
		public boolean blui;
		
		public Pangra(Craze craze){
			alt = false;
			hold = false;
			move = false;
			turn = true;
			horizontal =  960;
			vertical   =  960;
			xShift     =    0;
			yShift     = -120; 
			orXShf     =    0;
			orYShf     = -120; // seed value for yShift is -120, because the default canvas size is 960x720, and the default image size is 960x960, so in order
			                   // to have both centered, the real image must be moved upwards by 120 pixels, cropping it 120 pixels above, and 120 below equally, 
			                   // resulting in both the window canvas, and the real image sharing the same center
			                   // when the New Image's width/height values are set to something smaller than the active window, the shift will be positive, and
			                   // it will still cause the image to be properly centered
			
			visit     = new boolean[horizontal][vertical];
			centX     = new ArrayList<Integer>();
			centY     = new ArrayList<Integer>();
			listX     = new ArrayList<Integer>();
			listY     = new ArrayList<Integer>();
			stackIzq  = new Stack<Image>()      ;
			stackDer  = new Stack<Image>()      ;
			bordLine  = new double [2][2]       ;
			prevLine  = new double [2][2]       ;
			bordLineH = new double [2][2]       ;
			prevLineH = new double [2][2]       ;
			trid      = new double [3][2]       ;
			trip      = new double [3][2]       ;	
			quad      = new double [4][2]       ;
			quap      = new double [4][2]       ;
			lind      = new double [2]          ;
			posd      = new double [2]          ;
			pred      = new double [2]          ;
			pnnv      = new double [2]          ;
			posv      = new int    [2]          ;
			prev      = new int    [2]          ;
			prec      = new int    [2]          ;
			linc      = new int    [2]          ;
			posc      = new int    [2]          ;
			limit     = false                   ;
			press     = false                   ;
			rotar     = false                   ; 
			symme     = true                    ;
			recS      = 7                       ;
			state     = ""                      ;
			value     = 1                       ;	
			currBCol  = Color.white             ;
			currWCol  = Color.black             ;
			centX.add(480);
			centY.add(360);
			mosX = 480;
			mosY = 360;

			for(int i=0; i<960; ++i){
				for(int j=0; j<960; ++j){
					visit[i][j] = false;
				}
			}
			
			image= new BufferedImage(960, 960, BufferedImage.TYPE_INT_RGB);	
			k = image.getGraphics()                                       ;
			((Graphics2D) k).setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			k.setColor(currWCol)                                          ;
			k.fillRect(0,0,960,960)                                       ;
			stackIzq.push(image)                                          ;
			k = stackIzq.peek().getGraphics()                             ;
			addMouseMotionListener(this)                                  ;
			addMouseListener(this)                                        ;
			this.craze = craze                                            ;
		}
		
		public void paint(Graphics g){                        // after a operation is finished, the screen is repainted
			g.drawImage(stackIzq.peek(),xShift,yShift,null);  // the image that is painted is the one located at the top of the left stack
			if(state.equals("CENTER")){                       // when you select 'Change Center'...
				if(hold==true){                               // if you are clicking the center... 
					g.setColor(currBCol)                    ; 
					g.fillOval(posv[0]-5,posv[1]-5,10,10)   ; // the center will follow the mouse
					g.setColor(Color.WHITE)                 ;
					temp = Integer.toString(posv[0]-this.getWidth()/2-xShift+orXShf)+"(X), "+Integer.toString(-posv[1]+this.getHeight()/2+yShift-orYShf)+"(Y)";
					g.drawString(temp,posv[0]-10,posv[1]-10); // and its coordinates will be shown
					g.setColor(currBCol)                    ;
				}
				else{                                                   //if you are not clicking it
					g.setColor(Color.WHITE);
					temp = Integer.toString(centX.get(0)-this.getWidth()/2-xShift+orXShf)+"(X), "+Integer.toString(-centY.get(0)+this.getHeight()/2+yShift-orYShf)+"(Y)";
					g.drawString(temp,centX.get(0)-10,centY.get(0)-10); //the coordinates will still be there
					g.fillOval(centX.get(0)-5, centY.get(0)-5, 10, 10); //but it won't follow the mouse's position
					g.setColor(currBCol)                              ;
				}
			}
			else{                                                  //if you hadn't selected 'Change Center'...
				g.setColor(Color.WHITE)                           ;
				g.fillOval(centX.get(0)-5, centY.get(0)-5, 10, 10);//the center would still be drawn, but no coordinates to show (they aren't needed here)
				g.setColor(currBCol)                              ;
			}
		}
		
		public void mousePressed(MouseEvent arg0){                 // when you press the click    
			if(state.equals("DRAG")){
				move = true;                                       // if you chose 'Drag Image Around', a boolean 'move' will become true, and the image will simply
				                                                   // follow the movement of the mouse
			}
			else{                                                  // for every other possible state
				if(color==true){
					initShift();
				}
				image = deepCopy((BufferedImage) stackIzq.peek()); // the image at the top of the undo stack is assigned to a temporal image
				k = image.getGraphics();
				((Graphics2D) k).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // this sort of enables anti-aliasing
				press = true;                                      // this indicates the mouse is pressed
				prev[0] = linc[0] = prec[0] = (int)arg0.getX();    // the position of the mouse at the moment of pressing is stored
				prev[1] = linc[1] = prec[1] = (int)arg0.getY();    // linc works to make the 'Lines/Square/Triangle from start, because one of the vertices will
				                                                   // be permanently fixed to the starting point. prev works to storage the previous position, which
				                                                   // at the first click is the same than the current position. afterwards, it will work to connect
				                                                   // a previously drawn image with the new one
				recT = recS                                   ;    // recS is the intended Brush Size value. recT exists so we can modify it (for Variable Size) without
				angleCount = 1                                ;    // affecting the original value. 
				num = (int)(value-1)                          ;    // num is the number of rotations to paint. its value-1 because the 'first' rotation is what you draw
				if(alt==false){                                    // if the 'Alter Angle' checkbox is unselected
					angle = 2*Math.PI/value                   ;    // the rotation angle will be set to default (360/spins, in radians)
					craze.panopt.angle.setValue(360/(value))  ;
				}
				else{
					angle = (Double)(craze.panopt.angle.getValue())*Math.PI/180; //Otherwise, it will be set to the desired angle, in radians
				}
				
				if(state.equals("FREESTYLE")){
					vertexCount = 1   ; // for the free style brush, one important thing to take into account is the fillPolygon method
					                    // it works pretty much like fillPolygon(array of x, array of y, number of vertices), so at the beginning there is just one 
					listX.clear()     ; // the previous list of x
					listY.clear()     ; // and y points are cleared
					listX.add(prev[0]); // and the new x and y are 
					listY.add(prev[1]); // now added as seed values
				}
				if(state.equals("SPIRAL")){
					listX.clear()     ; // the previous list of x
					listY.clear()     ; // and y points are cleared
					listX.add(prev[0]); // and the new x and y are 
					listY.add(prev[1]); // now added as seed values
				}
				if(state.equals("VERTICAL") || state.equals("CROSS")){
					prevLine[0][0] = (int)arg0.getX()       ; // as its a vertical line, 
					prevLine[1][0] = (int)arg0.getX()       ; // the x values remain untouched
					prevLine[0][1] = (int)arg0.getY()-recT/2; // the y values on the other side do change
					prevLine[1][1] = (int)arg0.getY()+recT/2; // half of the desired brush size upwards, and half downwards 
					linc[0]        = (int)arg0.getX()       ; // the point where the click was made, is
					linc[1]        = (int)arg0.getY()       ; // stored for the sake of Connecting Centers
					bordLine[0][0] = prevLine[0][0]			;
					bordLine[0][1] = prevLine[0][1]			;
					bordLine[1][0] = prevLine[1][0]			;
					bordLine[1][1] = prevLine[1][1]			;
					// as seed values, prevLine contains the same as bordLine, but from now on it will store what was there previously to connect stuff
				}
				if(state.equals("HORIZONTAL") || state.equals("CROSS")){
					prevLineH[0][0] = (int)arg0.getX()-recT/2; // the x values are changed according to the brush size
					prevLineH[1][0] = (int)arg0.getX()+recT/2; // half of the desired brush size to the left, and half to the right
					prevLineH[0][1] = (int)arg0.getY()       ; // on the other side, as its a horizontal line
					prevLineH[1][1] = (int)arg0.getY()       ; // the values on the y axis remains untouched
					linc[0]         = (int)arg0.getX()       ; // stored for the sake of Connecting Centers
					linc[1]         = (int)arg0.getY()       ; 
					bordLineH[0][0] = prevLineH[0][0]        ;
					bordLineH[0][1] = prevLineH[0][1]        ;
					bordLineH[1][0] = prevLineH[1][0]        ;
					bordLineH[1][1] = prevLineH[1][1]        ;
					// as seed values, prevLineH contains the same as bordLineH, 
					// but from now on it will store what was there previously to connect stuff
				}
				if(state.equals("TRIANGLE")||state.equals("TRIANGLEROT")){
					trip[0][1] = 0;                    //the starting point value is set to 0,0 
					trip[0][0] = 0;                    //because the kind of coordinate transform here rotates with that point, then we'll just add up the real x,y
					if(!state.equals("TRIANGLEROT")){
						trip[0][1] = -recT/2;          //the y of the point will be then shifted up by the half of the desired brush size
					}
					trip[1][0] =  trip[0][0]*Math.cos(2*Math.PI/3) - trip[0][1]*Math.sin(2*Math.PI/3); // this is pure trigonometrical wizardry
					trip[1][1] =  trip[0][0]*Math.sin(2*Math.PI/3) + trip[0][1]*Math.cos(2*Math.PI/3); // thank you geometry, I love you
					trip[2][0] =  trip[1][0]*Math.cos(2*Math.PI/3) - trip[1][1]*Math.sin(2*Math.PI/3);
					trip[2][1] =  trip[1][0]*Math.sin(2*Math.PI/3) + trip[1][1]*Math.cos(2*Math.PI/3);
					trip[0][0] += arg0.getX();       // because the rotations were as if the center was 0,0
					trip[0][1] += arg0.getY();       // there has to be a compensation for the real center of rotation
					trip[1][0] += arg0.getX();       // which can be done by adding the X and Y of the current 
					trip[1][1] += arg0.getY();       // mouse position (the three vertices spin around this)
					trip[2][0] += arg0.getX();
					trip[2][1] += arg0.getY();   
					lind[0] = trip[0][0]     ;       // the final coordinate of the original x,y point is stored 
					lind[1] = trip[0][1]     ;       // for the sake of drawing triangles from the center
				}
				if(state.equals("SQUARE") || state.equals("SQUAREROT")){
					quad[0][0] = arg0.getX()-recT/2; //the first point of the square is at the northwest corner.
					quad[0][1] = arg0.getY()-recT/2;
					tmp = recT;
					if(state.equals("SQUAREROT")){
						quad[0][0] = arg0.getX();    // if 'Square from start is selected, it will be the place where the mouse is
						quad[0][1] = arg0.getY();
						recT = 1;
					}
					quad[1][0] = quad[0][0]+recT;    // then the other three points are found 
					quad[1][1] = quad[0][1]     ;    // by moving a whole side-length from each other
					quad[2][0] = quad[1][0]     ;
					quad[2][1] = quad[1][1]+recT;
					quad[3][0] = quad[2][0]-recT;
					quad[3][1] = quad[2][1]     ;
					lind[0] = quad[0][0]        ;    // the mouse coordinates are stored for the sake of Connecting Centers
					lind[1] = quad[0][1]        ;
					recT = tmp;
				}
			    if(state.equals("SQUARTILT")){
					quad[0][0] = arg0.getX()       ; // the points of the diamond are easier to find    
					quad[0][1] = arg0.getY()-recT/2; // just go half of the brush size to each of the 4 directions
					quad[1][0] = arg0.getX()+recT/2; // I just had an idea about mixing this with the Great Cross. Oh my.
					quad[1][1] = arg0.getY()       ;
					quad[2][0] = arg0.getX()       ;
					quad[2][1] = arg0.getY()+recT/2;
					quad[3][0] = arg0.getX()-recT/2;
					quad[3][1] = arg0.getY()       ;
	
			    }
			    if(state.equals("CIRCLE")){
			    	lind[0] = arg0.getX() - recT/2; // the circle is easy, the method for drawing it is drawOval(x,y,radius,radius)
			    	lind[1] = arg0.getY() - recT/2; // that x,y is the left most corner, as if a rectangle was encompassing it
			    }
				if(state.equals("BUCKET")){         // this is evil, don't pay attention to it, it is evil!
					posv[0] = (int) arg0.getX();
					posv[1] = (int) arg0.getY();
					origin = ((BufferedImage) image).getRGB(posv[0],posv[1]-yShift);
					bucketFill(posv[0],posv[1]-yShift);
					for(int i=0; i<960; ++i){
						for(int j=0; j<960; ++j){
							visit[i][j] = false;
						}
					}
					repaint();
				}
				if(state.equals("CENTER")){
					if(arg0.getX() <= centX.get(0)+5 && arg0.getX() >= centX.get(0)-5 && arg0.getY() <= centY.get(0)+5 && arg0.getX() >= centX.get(0)-5){
						hold = true;   // when the 'Change Center' tool is selected, and the mouse clicks inside the acceptable range of the center 
						               // (which really is a little square range) hold will become true, allowing the center to move, until the click is released 
					}
				}
				else{
					lapiZ();	
				}
			}
		}
		public void lapiZ(){                  // this method is called at every moment the mouse has a registered move
			Graphics g  = this.getGraphics();
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			g.setColor(currBCol)            ; // g is the graphics component that paints to the screen
			k.setColor(currBCol)            ; // k is the graphics component that paints to the real image
			
			if(state == "LINCEN"){	
				for(int i=0; i<centX.size();++i){                    // this cycle is meaningless for now, as there is only a possible center,  
					                                                 // just adding more centers won't make it magically work, sadly.
					drawLinesFromCenter(g,centX.get(i),centY.get(i));
					if(conne==true){                                 // Connected Center really calls a Line Drawer method. 
						posv[0] = (linc[0]+posv[0])/2      ;
						posv[1] = (linc[1]+posv[1])/2      ;
						drawSimpleLine(g,centX.get(i),centY.get(i));
					}
				}
			}

			if(state == "VERTICAL"){
				
				for(int i=0; i<centX.size();++i){
					drawVerticalLines(g,centX.get(i),centY.get(i));
					if(conne==true){
						drawSimpleLine(g,centX.get(i),centY.get(i))   ;
						drawVerticalLines(g,centX.get(i),centY.get(i));
					}
				}
			}
				
			if(state == "HORIZONTAL"){
				for(int i=0; i<centX.size();++i){
					drawHorizontalLines(g,centX.get(i),centY.get(i));
					if(conne==true){
						drawSimpleLine(g,centX.get(i),centY.get(i))     ;
						drawHorizontalLines(g,centX.get(i),centY.get(i));
					}
				}
			}
			
			if(state == "CROSS"){ // Here is evidence of mixing two different brushes into one. Will definitely do this with Great Cross + Diamond, just need 
				                  // to fix a little detail, but it does work if I just made a brush that called Horizontal/Vertical Lines, and Diamond
				for(int i=0; i<centX.size();++i){
					drawVerticalLines(g,centX.get(i),centY.get(i))  ;
					drawHorizontalLines(g,centX.get(i),centY.get(i));	
					if(conne==true){
						drawSimpleLine(g,centX.get(i),centY.get(i))     ;
						drawVerticalLines(g,centX.get(i),centY.get(i))  ;
						drawHorizontalLines(g,centX.get(i),centY.get(i));	
					}
				}
			}
			
			if(state == "LINE"){
				for(int i=0; i<centX.size();++i){
					drawSimpleLine(g,centX.get(i),centY.get(i));
				}
			}
			if(state == "SPIRAL"){
				for(int i=0; i<centX.size();++i){
					drawSpiralPower(g,centX.get(i),centY.get(i));
				}
			}
			
			if(state == "FREESTYLE"){
				for(int i=0; i<centX.size();++i){
					drawFreeStyleFill(g,centX.get(i),centY.get(i));
				}
			}
			
			if(state == "TRIANGLE" || state == "TRIANGLEROT"){
				for(int i=0; i<centX.size();++i){
					drawTriangles(g,centX.get(i),centY.get(i));
					if(conne==true){
						drawSimpleLine(g,centX.get(i),centY.get(i));
						drawTriangles(g,centX.get(i),centY.get(i)) ;
					}
				}
			}
			if(state == "SQUARE" || state == "SQUAREROT" || state == "SQUARTILT"){
				for(int i=0; i<centX.size();++i){
					drawSquares(g,centX.get(i),centY.get(i));
					if(conne==true){
						drawSimpleLine(g,centX.get(i),centY.get(i));
						drawSquares(g,centX.get(i),centY.get(i)) ;
					}
				}
			}	
			if(state == "CIRCLE"){
				for(int i=0; i<centX.size();++i){
					drawCircle(g,centX.get(i),centY.get(i));
					if(conne==true){
						drawSimpleLine(g,centX.get(i),centY.get(i));
					}
				}
			}
		}
		
		// this is the perfect method to explain all others
		// because it makes use of all the possible parameters
		public void drawVerticalLines(Graphics g, int cenX, int cenY){
			if(bordr==true){ // when bordr is true, its because Draw Borders is selected, so it proceeds 
				             // to draw a line between the old points and the new points of the line
				g.drawLine((int)prevLine[0][0]       , (int)prevLine[0][1]       , (int)bordLine[0][0]       , (int)bordLine[0][1]       );
				g.drawLine((int)prevLine[1][0]       , (int)prevLine[1][1]       , (int)bordLine[1][0]       , (int)bordLine[1][1]       );
				k.drawLine((int)prevLine[0][0]-xShift, (int)prevLine[0][1]-yShift, (int)bordLine[0][0]-xShift, (int)bordLine[0][1]-yShift);
				k.drawLine((int)prevLine[1][0]-xShift, (int)prevLine[1][1]-yShift, (int)bordLine[1][0]-xShift, (int)bordLine[1][1]-yShift);
			}
			
			// then it normally draws the line between the upper and lower points
			g.drawLine((int)bordLine[0][0]       , (int)bordLine[0][1]       , (int)bordLine[1][0]       , (int)bordLine[1][1]       );				
			k.drawLine((int)bordLine[0][0]-xShift, (int)bordLine[0][1]-yShift, (int)bordLine[1][0]-xShift, (int)bordLine[1][1]-yShift);
			
			if(symme==true){     // if symmetry is enabled
				if(bordr==true){ // it will proceed to do the same Draw Borders process with the mirrored image (2*cenX - coordinate in X)
					g.drawLine((int)(2*cenX-prevLine[0][0])       , (int)prevLine[0][1]       , (int)(2*cenX-bordLine[0][0])       , (int)bordLine[0][1]       );
					g.drawLine((int)(2*cenX-prevLine[1][0])       , (int)prevLine[1][1]       , (int)(2*cenX-bordLine[1][0])       , (int)bordLine[1][1]       );
					k.drawLine((int)(2*cenX-prevLine[0][0])-xShift, (int)prevLine[0][1]-yShift, (int)(2*cenX-bordLine[0][0])-xShift, (int)bordLine[0][1]-yShift);
					k.drawLine((int)(2*cenX-prevLine[1][0])-xShift, (int)prevLine[1][1]-yShift, (int)(2*cenX-bordLine[1][0])-xShift, (int)bordLine[1][1]-yShift);
				}
				// and draw the mirrored line as usual
				g.drawLine((int)(2*cenX-bordLine[0][0])       ,(int)bordLine[0][1]       ,(int)(2*cenX-bordLine[1][0])       ,(int)bordLine[1][1]       );
				k.drawLine((int)(2*cenX-bordLine[0][0])-xShift,(int)bordLine[0][1]-yShift,(int)(2*cenX-bordLine[1][0])-xShift,(int)bordLine[1][1]-yShift);
			}
			// then it proceeds to substract the center position to the x and y coordinates, and store them apart, because for rotation purposes
			// they have to be positioned in respect to the 0,0 cartesian position. after rotating, it works by simply adding back the center x,y
			posd[0] = bordLine[0][0]-cenX;
			posd[1] = bordLine[0][1]-cenY;
			pred[0] = bordLine[1][0]-cenX;
			pred[1] = bordLine[1][1]-cenY;
			lind[0] = prevLine[0][0]-cenX;
			lind[1] = prevLine[0][1]-cenY;
			pnnv[0] = prevLine[1][0]-cenX;
			pnnv[1] = prevLine[1][1]-cenY;

			for(int i=0; i<num; ++i){//there are n-1 rotations, 0th step inclusive
				// this is black magic, without this there is no program
				// x' = x*cos(angle) - y*sin(angle)
				// y' = x*sin(angle) + x*cos(angle)
				nx1 = pred[0]*Math.cos(angle) - pred[1]*Math.sin(angle);
				ny1 = pred[0]*Math.sin(angle) + pred[1]*Math.cos(angle);
				nx2 = posd[0]*Math.cos(angle) - posd[1]*Math.sin(angle);
				ny2 = posd[0]*Math.sin(angle) + posd[1]*Math.cos(angle);
				nx3 = lind[0]*Math.cos(angle) - lind[1]*Math.sin(angle);
				ny3 = lind[0]*Math.sin(angle) + lind[1]*Math.cos(angle);
				nx4 = pnnv[0]*Math.cos(angle) - pnnv[1]*Math.sin(angle);
				ny4 = pnnv[0]*Math.sin(angle) + pnnv[1]*Math.cos(angle);
			   	
				//the rotated points are now connected by lines
				g.drawLine((int) nx1+cenX       , (int) ny1+cenY       , (int) nx2+cenX       , (int) ny2+cenY       );	    	
				k.drawLine((int) nx1+cenX-xShift, (int) ny1+cenY-yShift, (int) nx2+cenX-xShift, (int) ny2+cenY-yShift);	
		    	
				// notice that also the previous points were rotated, because if Draw Borders is selected, it must join the edges of
				// the rotated line, and the previous line (which could also be rotated)
				if(bordr==true){	
					g.drawLine((int)nx3+cenX       , (int)ny3+cenY       , (int)nx2+cenX       , (int)ny2+cenY       );
					g.drawLine((int)nx4+cenX       , (int)ny4+cenY       , (int)nx1+cenX       , (int)ny1+cenY       );
					k.drawLine((int)nx3+cenX-xShift, (int)ny3+cenY-yShift, (int)nx2+cenX-xShift, (int)ny2+cenY-yShift);
					k.drawLine((int)nx4+cenX-xShift, (int)ny4+cenY-yShift, (int)nx1+cenX-xShift, (int)ny1+cenY-yShift);
				}
				if(symme==true){ //again, symmetry demands to do the same thing at the other side, by subtracting the coordinate in x to the center point
					if(bordr==true){
						g.drawLine((int)-nx3+cenX       , (int)ny3+cenY       , (int)-nx2+cenX       , (int)ny2+cenY       );
						g.drawLine((int)-nx4+cenX       , (int)ny4+cenY       , (int)-nx1+cenX       , (int)ny1+cenY       );	
						k.drawLine((int)-nx3+cenX-xShift, (int)ny3+cenY-yShift, (int)-nx2+cenX-xShift, (int)ny2+cenY-yShift);
						k.drawLine((int)-nx4+cenX-xShift, (int)ny4+cenY-yShift, (int)-nx1+cenX-xShift, (int)ny1+cenY-yShift);	
					}
					g.drawLine((int)-nx1+cenX       , (int) ny1+cenY       , (int)-nx2+cenX       , (int) ny2+cenY       );
					k.drawLine((int)-nx1+cenX-xShift, (int) ny1+cenY-yShift, (int)-nx2+cenX-xShift, (int) ny2+cenY-yShift);
				}
				//the actual rotated points are stored as seeds for the next rotation cycle step
				posd[0] = nx2;
				posd[1] = ny2;
				pred[0] = nx1;
				pred[1] = ny1;
				lind[0] = nx3;
				lind[1] = ny3;
				pnnv[0] = nx4;
				pnnv[1] = ny4;
			}
			// now that the rotation madness has ended, it proceeds to store the current line as an old one
			prevLine[0][0] = bordLine[0][0];
			prevLine[0][1] = bordLine[0][1];
			prevLine[1][0] = bordLine[1][0];
			prevLine[1][1] = bordLine[1][1];
		    
			if(varia==true){        // if Variable Size is selected
				if(limit == false){ // it will increase the size of the brush until 7*4ths of its original size
					recT++;
				}
				if(limit == true){  // when it reaches the limit, it will decrease the size of the brush until 1/4th of its original size
					recT--;
				}
				if(recT==recS*7/4){  // both these ifs are the ones that toggle between ascending or descending brush size
					limit = true;
				}
				if(recT==recS/4){
					limit = false;
				}
			}   
			if(rotar==true){ // if Rotating Brush is enabled, then the new line points will be rotated by angleCount, which is a incremental value
				bordLine[0][0] =  recT/2*Math.sin(angleCount*2*Math.PI/360);
				bordLine[0][1] = -recT/2*Math.cos(angleCount*2*Math.PI/360);
				bordLine[1][0] = -recT/2*Math.sin(angleCount*2*Math.PI/360);
				bordLine[1][1] =  recT/2*Math.cos(angleCount*2*Math.PI/360);
				bordLine[0][0] += posv[0];
				bordLine[0][1] += posv[1];
				bordLine[1][0] += posv[0];
				bordLine[1][1] += posv[1];
			}
			else{                             // otherwise, they'll just be placed relatively to the mouse coordinates
				bordLine[0][0]=posv[0]       ;
				bordLine[0][1]=posv[1]-recT/2;
				bordLine[1][0]=posv[0]       ;	
				bordLine[1][1]=posv[1]+recT/2;
			}
			if(angleCount<360){              // and there goes angleCount increasing in size, until reaching 360 and looping
				angleCount++;                // for trigonometric functions it has no real effect to make the number loop, but I think its nicer this way
			}
			else{
				angleCount = 1;
			}
		}
		
		//Now from here, its some sort of evil dark black heretic application of what was done above, several times.
		public void drawLinesFromCenter(Graphics g, int cenX, int cenY){
			if(bordr==true){
				g.drawLine(prec[0]       , prec[1]       , posc[0]       , posc[1]       );
				k.drawLine(prec[0]-xShift, prec[1]-yShift, posc[0]-xShift, posc[1]-yShift);
			}
			g.drawLine(linc[0]       , linc[1]       , posc[0]       , posc[1]       );
			k.drawLine(linc[0]-xShift, linc[1]-yShift, posc[0]-xShift, posc[1]-yShift);

			if(symme==true){
				if(bordr==true){
					g.drawLine(2*cenX-prec[0]       , prec[1]       , 2*cenX-posc[0]       , posc[1]       );
					k.drawLine(2*cenX-prec[0]-xShift, prec[1]-yShift, 2*cenX-posc[0]-xShift, posc[1]-yShift);
				}
				g.drawLine(2*cenX-linc[0]       , linc[1]       , 2*cenX-posc[0]       , posc[1]       );
				k.drawLine(2*cenX-linc[0]-xShift, linc[1]-yShift, 2*cenX-posc[0]-xShift, posc[1]-yShift);
			}
			posd[0] = posc[0]-cenX;
			posd[1] = posc[1]-cenY;
			pred[0] = prec[0]-cenX;
			pred[1] = prec[1]-cenY;
			lind[0] = linc[0]-cenX;
			lind[1] = linc[1]-cenY;
		    
			for(int i=0; i<num; ++i){
				nx1 = pred[0]*Math.cos(angle) - pred[1]*Math.sin(angle);
				ny1 = pred[0]*Math.sin(angle) + pred[1]*Math.cos(angle);
				nx2 = posd[0]*Math.cos(angle) - posd[1]*Math.sin(angle);
				ny2 = posd[0]*Math.sin(angle) + posd[1]*Math.cos(angle);
				nx3 = lind[0]*Math.cos(angle) - lind[1]*Math.sin(angle);
				ny3 = lind[0]*Math.sin(angle) + lind[1]*Math.cos(angle);
			   	
				if(bordr==true){
					g.drawLine((int) nx1+cenX       , (int) ny1+cenY       , (int) nx2+cenX       , (int) ny2+cenY       );
					k.drawLine((int) nx1+cenX-xShift, (int) ny1+cenY-yShift, (int) nx2+cenX-xShift, (int) ny2+cenY-yShift);
				}
				g.drawLine((int) nx3+cenX       , (int) ny3+cenY       , (int) nx2+cenX       , (int) ny2+cenY       );
				k.drawLine((int) nx3+cenX-xShift, (int) ny3+cenY-yShift, (int) nx2+cenX-xShift, (int) ny2+cenY-yShift);
		    	
				if(symme==true){
					if(bordr==true){
						g.drawLine((int) -nx1+cenX       , (int) ny1+cenY       , (int) -nx2+cenX       , (int) ny2+cenY       );
						k.drawLine((int) -nx1+cenX-xShift, (int) ny1+cenY-yShift, (int) -nx2+cenX-xShift, (int) ny2+cenY-yShift);
					}
					g.drawLine((int) -nx3+cenX       , (int) ny3+cenY       , (int) -nx2+cenX       , (int) ny2+cenY       );	
					k.drawLine((int) -nx3+cenX-xShift, (int) ny3+cenY-yShift, (int) -nx2+cenX-xShift, (int) ny2+cenY-yShift);
				}
				posd[0] = nx2;
				posd[1] = ny2;
				pred[0] = nx1;
				pred[1] = ny1;
				lind[0] = nx3;
				lind[1] = ny3;
			}
			prec[0] = posc[0];
			prec[1] = posc[1];
		}	
		
		public void drawHorizontalLines(Graphics g, int cenX, int cenY){
			if(bordr==true){
				g.drawLine((int)prevLineH[0][0]       , (int)prevLineH[0][1]       , (int)bordLineH[0][0]       , (int)bordLineH[0][1]       );
				g.drawLine((int)prevLineH[1][0]       , (int)prevLineH[1][1]       , (int)bordLineH[1][0]       , (int)bordLineH[1][1]       );
				k.drawLine((int)prevLineH[0][0]-xShift, (int)prevLineH[0][1]-yShift, (int)bordLineH[0][0]-xShift, (int)bordLineH[0][1]-yShift);
				k.drawLine((int)prevLineH[1][0]-xShift, (int)prevLineH[1][1]-yShift, (int)bordLineH[1][0]-xShift, (int)bordLineH[1][1]-yShift);
			}
			g.drawLine((int)bordLineH[0][0]       , (int)bordLineH[0][1]       , (int)bordLineH[1][0]       , (int)bordLineH[1][1]       );				
			k.drawLine((int)bordLineH[0][0]-xShift, (int)bordLineH[0][1]-yShift, (int)bordLineH[1][0]-xShift, (int)bordLineH[1][1]-yShift);
			
			if(symme==true){
				if(bordr==true){
					g.drawLine((int)(2*cenX-prevLineH[0][0])       , (int)prevLineH[0][1]       , (int)(2*cenX-bordLineH[0][0])       , (int)bordLineH[0][1]       );
					g.drawLine((int)(2*cenX-prevLineH[1][0])       , (int)prevLineH[1][1]       , (int)(2*cenX-bordLineH[1][0])       , (int)bordLineH[1][1]       );
					k.drawLine((int)(2*cenX-prevLineH[0][0])-xShift, (int)prevLineH[0][1]-yShift, (int)(2*cenX-bordLineH[0][0])-xShift, (int)bordLineH[0][1]-yShift);
					k.drawLine((int)(2*cenX-prevLineH[1][0])-xShift, (int)prevLineH[1][1]-yShift, (int)(2*cenX-bordLineH[1][0])-xShift, (int)bordLineH[1][1]-yShift);
				}
				g.drawLine((int)(2*cenX-bordLineH[0][0])       ,(int)bordLineH[0][1]       ,(int)(2*cenX-bordLineH[1][0])       ,(int)bordLineH[1][1]       );
				k.drawLine((int)(2*cenX-bordLineH[0][0])-xShift,(int)bordLineH[0][1]-yShift,(int)(2*cenX-bordLineH[1][0])-xShift,(int)bordLineH[1][1]-yShift);
			}
			posd[0] = bordLineH[0][0]-cenX;
			posd[1] = bordLineH[0][1]-cenY;
			pred[0] = bordLineH[1][0]-cenX;
			pred[1] = bordLineH[1][1]-cenY;
			lind[0] = prevLineH[0][0]-cenX;
			lind[1] = prevLineH[0][1]-cenY;
			pnnv[0] = prevLineH[1][0]-cenX;
			pnnv[1] = prevLineH[1][1]-cenY;

			for(int i=0; i<num; ++i){
				nx1 = pred[0]*Math.cos(angle) - pred[1]*Math.sin(angle);
				ny1 = pred[0]*Math.sin(angle) + pred[1]*Math.cos(angle);
				nx2 = posd[0]*Math.cos(angle) - posd[1]*Math.sin(angle);
				ny2 = posd[0]*Math.sin(angle) + posd[1]*Math.cos(angle);
				nx3 = lind[0]*Math.cos(angle) - lind[1]*Math.sin(angle);
				ny3 = lind[0]*Math.sin(angle) + lind[1]*Math.cos(angle);
				nx4 = pnnv[0]*Math.cos(angle) - pnnv[1]*Math.sin(angle);
				ny4 = pnnv[0]*Math.sin(angle) + pnnv[1]*Math.cos(angle);
			   	
				g.drawLine((int) nx1+cenX       , (int) ny1+cenY       , (int) nx2+cenX       , (int) ny2+cenY       );	    	
				k.drawLine((int) nx1+cenX-xShift, (int) ny1+cenY-yShift, (int) nx2+cenX-xShift, (int) ny2+cenY-yShift);	
		    	
				if(bordr==true){	
					g.drawLine((int)nx3+cenX       , (int)ny3+cenY       , (int)nx2+cenX       , (int)ny2+cenY       );
					g.drawLine((int)nx4+cenX       , (int)ny4+cenY       , (int)nx1+cenX       , (int)ny1+cenY       );
					k.drawLine((int)nx3+cenX-xShift, (int)ny3+cenY-yShift, (int)nx2+cenX-xShift, (int)ny2+cenY-yShift);
					k.drawLine((int)nx4+cenX-xShift, (int)ny4+cenY-yShift, (int)nx1+cenX-xShift, (int)ny1+cenY-yShift);
				}
				if(symme==true){
					if(bordr==true){
						g.drawLine((int)-nx3+cenX       , (int)ny3+cenY       , (int)-nx2+cenX       , (int)ny2+cenY       );
						g.drawLine((int)-nx4+cenX       , (int)ny4+cenY       , (int)-nx1+cenX       , (int)ny1+cenY       );	
						k.drawLine((int)-nx3+cenX-xShift, (int)ny3+cenY-yShift, (int)-nx2+cenX-xShift, (int)ny2+cenY-yShift);
						k.drawLine((int)-nx4+cenX-xShift, (int)ny4+cenY-yShift, (int)-nx1+cenX-xShift, (int)ny1+cenY-yShift);	
					}
					g.drawLine((int)-nx1+cenX       , (int) ny1+cenY       , (int)-nx2+cenX       , (int) ny2+cenY       );
					k.drawLine((int)-nx1+cenX-xShift, (int) ny1+cenY-yShift, (int)-nx2+cenX-xShift, (int) ny2+cenY-yShift);
				}
				posd[0] = nx2;
				posd[1] = ny2;
				pred[0] = nx1;
				pred[1] = ny1;
				lind[0] = nx3;
				lind[1] = ny3;
				pnnv[0] = nx4;
				pnnv[1] = ny4;
			}
			prevLineH[0][0] = bordLineH[0][0];
			prevLineH[0][1] = bordLineH[0][1];
			prevLineH[1][0] = bordLineH[1][0];
			prevLineH[1][1] = bordLineH[1][1];
		    
			if(varia==true){
				if(limit == false){
					recT++;
				}
				if(limit == true){
					recT--;
				}
				if(recT==recS*7/4){
					limit = true;
				}
				if(recT==recS/4){
					limit = false;
				}
			}   
			if(rotar==true){
				bordLineH[0][0] =  recT/2*Math.sin(angleCount*2*Math.PI/360+Math.PI/2);
				bordLineH[0][1] = -recT/2*Math.cos(angleCount*2*Math.PI/360+Math.PI/2);
				bordLineH[1][0] = -recT/2*Math.sin(angleCount*2*Math.PI/360+Math.PI/2);
				bordLineH[1][1] =  recT/2*Math.cos(angleCount*2*Math.PI/360+Math.PI/2);
				bordLineH[0][0] += posv[0];
				bordLineH[0][1] += posv[1];
				bordLineH[1][0] += posv[0];
				bordLineH[1][1] += posv[1];
			}
			else{
				bordLineH[0][0]=posv[0]-recT/2;
				bordLineH[0][1]=posv[1]       ;
				bordLineH[1][0]=posv[0]+recT/2;	
				bordLineH[1][1]=posv[1]       ;
			}
			if(angleCount<360){
				angleCount++;
			}
			else{
				angleCount = 1;
			}
		}
		
		public void drawSimpleLine(Graphics g, int cenX, int cenY){
			g.drawLine(prev[0]       , prev[1]       , posv[0]       , posv[1]       );
			k.drawLine(prev[0]-xShift, prev[1]-yShift, posv[0]-xShift, posv[1]-yShift); 
		    if(symme==true){
		    	g.drawLine(2*cenX-prev[0]       , prev[1]       , 2*cenX-posv[0]       , posv[1]       );
		    	k.drawLine(2*cenX-prev[0]-xShift, prev[1]-yShift, 2*cenX-posv[0]-xShift, posv[1]-yShift);
		    }
		    posd[0] = posv[0]-cenX;
		    posd[1] = posv[1]-cenY;
		    pred[0] = prev[0]-cenX;
		    pred[1] = prev[1]-cenY;
		    
		    for(int i=0; i<num; ++i){
		    	nx1 = pred[0]*Math.cos(angle) - pred[1]*Math.sin(angle);
		    	ny1 = pred[0]*Math.sin(angle) + pred[1]*Math.cos(angle);
		    	nx2 = posd[0]*Math.cos(angle) - posd[1]*Math.sin(angle);
		    	ny2 = posd[0]*Math.sin(angle) + posd[1]*Math.cos(angle);
			   	
		    	g.drawLine((int) nx1+cenX       , (int) ny1+cenY       , (int) nx2+cenX       , (int) ny2+cenY       );
		    	k.drawLine((int) nx1+cenX-xShift, (int) ny1+cenY-yShift, (int) nx2+cenX-xShift, (int) ny2+cenY-yShift);
		    	
		    	if(symme==true){
		    		g.drawLine((int) -nx1+cenX       , (int) ny1+cenY       , (int) -nx2+cenX       , (int) ny2+cenY       );
		    		k.drawLine((int) -nx1+cenX-xShift, (int) ny1+cenY-yShift, (int) -nx2+cenX-xShift, (int) ny2+cenY-yShift);
		    	}
		    	posd[0] = nx2;
		    	posd[1] = ny2;
		    	pred[0] = nx1;
		    	pred[1] = ny1;
		    }
		    prev[0] = posv[0];
		    prev[1] = posv[1];
		}
		
		public void drawSpiralPower(Graphics g, int cenX, int cenY){
			g.drawLine(prev[0]       , prev[1]       , posv[0]       , posv[1]       );
			k.drawLine(prev[0]-xShift, prev[1]-yShift, posv[0]-xShift, posv[1]-yShift); 
		    if(symme==true){
		    	g.drawLine(2*cenX-prev[0]       , prev[1]       , 2*cenX-posv[0]       , posv[1]       );
		    	k.drawLine(2*cenX-prev[0]-xShift, prev[1]-yShift, 2*cenX-posv[0]-xShift, posv[1]-yShift);
		    }
		    posd[0] = posv[0]-cenX;
		    posd[1] = posv[1]-cenY;
		    pred[0] = prev[0]-cenX;
		    pred[1] = prev[1]-cenY;
		    
		    for(int i=listX.size()-1; i>listX.size()-recS; --i){
		    	if(i>0){
		    		g.drawLine(listX.get(i), listY.get(i), posv[0], posv[1]);
		    		k.drawLine(listX.get(i)-xShift, listY.get(i)-yShift, posv[0]-xShift, posv[1]-yShift);
		    		if(symme==true){
				    	g.drawLine(2*cenX-listX.get(i)       , listY.get(i)       , 2*cenX-posv[0]       , posv[1]       );
				    	k.drawLine(2*cenX-listX.get(i)-xShift, listY.get(i)-yShift, 2*cenX-posv[0]-xShift, posv[1]-yShift);
				    }
		    	}
		    }

		    ArrayList<Double> listDX = new ArrayList<Double>();
			ArrayList<Double> listDY = new ArrayList<Double>();
			
		    ArrayList<Double> listZX = new ArrayList<Double>();
			ArrayList<Double> listZY = new ArrayList<Double>();

		    for(int i=0; i<listX.size(); ++i){
		    	listDX.add((double) (listX.get(i)-cenX));
		    	listDY.add((double) (listY.get(i)-cenY));
		    }
		    for(int i=0; i<num; ++i){
				listZY.clear();
				listZX.clear();
		    	for(int k=0; k<listDX.size(); ++k){
			    	listZX.add(listDX.get(k)*Math.cos(angle) - listDY.get(k)*Math.sin(angle));
			    	listZY.add(listDX.get(k)*Math.sin(angle) + listDY.get(k)*Math.cos(angle));
			    }
		    	
		    	nx1 = pred[0]*Math.cos(angle) - pred[1]*Math.sin(angle);
		    	ny1 = pred[0]*Math.sin(angle) + pred[1]*Math.cos(angle);
		    	nx2 = posd[0]*Math.cos(angle) - posd[1]*Math.sin(angle);
		    	ny2 = posd[0]*Math.sin(angle) + posd[1]*Math.cos(angle);
			   	
		    	g.drawLine((int) nx1+cenX       , (int) ny1+cenY       , (int) nx2+cenX       , (int) ny2+cenY       );
		    	k.drawLine((int) nx1+cenX-xShift, (int) ny1+cenY-yShift, (int) nx2+cenX-xShift, (int) ny2+cenY-yShift);
		    	
		    	if(symme==true){
		    		g.drawLine((int) -nx1+cenX       , (int) ny1+cenY       , (int) -nx2+cenX       , (int) ny2+cenY       );
		    		k.drawLine((int) -nx1+cenX-xShift, (int) ny1+cenY-yShift, (int) -nx2+cenX-xShift, (int) ny2+cenY-yShift);
		    	}
		    	
		    	 for(int j=listZX.size()-1; j>listZX.size()-recS; --j){
				    	if(j>0){
				    		int xm = (int)listZX.get(j).doubleValue()+cenX;
				    		int ym = (int)listZY.get(j).doubleValue()+cenY;
				    		ny1 += cenY;
				    		nx1 += cenX;
				    		g.drawLine(xm       , ym       , (int)nx1       , (int)ny1        );
				    		k.drawLine(xm-xShift, ym-yShift, (int)nx1-xShift, (int)ny1-yShift);
				    		if(symme==true){
						    	g.drawLine(2*cenX-xm       , ym       , 2*cenX-(int)nx1      , (int)ny1       );
						    	k.drawLine(2*cenX-xm-xShift, ym-yShift, 2*cenX-(int)nx1-xShift,(int)ny1-yShift);
						    }
				    		ny1 -= cenY;
				    		nx1 -= cenX;
				    	}
				    }
		    	
		    	
		    	posd[0] = nx2;
		    	posd[1] = ny2;
		    	pred[0] = nx1;
		    	pred[1] = ny1;
		    	listDX.clear();
		    	listDY.clear();
		    	for(int m=0; m<listZX.size(); ++m){
		    		listDX.add(listZX.get(m));
		    		listDY.add(listZY.get(m));
		    	}
		    }
		    prev[0] = posv[0];
		    prev[1] = posv[1];
		    listX.add(posv[0]);
		    listY.add(posv[1]);
		    vertexCount++;
		}
		
		
		public void drawCircle(Graphics g, int cenX, int cenY){
			g.drawOval((int)posv[0]-recT/2       ,(int)posv[1]-recT/2       ,recT,recT);
			k.drawOval((int)posv[0]-recT/2-xShift,(int)posv[1]-recT/2-yShift,recT,recT);
			
		    if(symme==true){
				g.drawOval(2*cenX-(int)posv[0]-recT/2       ,(int)posv[1]-recT/2       ,recT,recT);
				k.drawOval(2*cenX-(int)posv[0]-recT/2-xShift,(int)posv[1]-recT/2-yShift,recT,recT);
		    }
		    posd[0] = posv[0]-cenX;
		    posd[1] = posv[1]-cenY;
		    for(int i=0; i<num; ++i){
		    	nx1 = posd[0]*Math.cos(angle) - posd[1]*Math.sin(angle);
		    	ny1 = posd[0]*Math.sin(angle) + posd[1]*Math.cos(angle);
		    	
		    	g.drawOval((int)nx1-recT/2+cenX       ,(int)ny1-recT/2+cenY       ,recT,recT);
		    	k.drawOval((int)nx1-recT/2+cenX-xShift,(int)ny1-recT/2-yShift+cenY,recT,recT);
		    	
		    	if(symme==true){
			    	g.drawOval((int)-nx1-recT/2+cenX       ,(int)ny1-recT/2+cenY       ,recT,recT);
			    	k.drawOval((int)-nx1-recT/2+cenX-xShift,(int)ny1-recT/2-yShift+cenY,recT,recT);
		    	}
		    	posd[0] = nx1;
		    	posd[1] = ny1;
		    }
		    if(varia==true){
			    if(limit == false){
			    	recT++;
			    }
			    if(limit == true){
			    	recT--;
			    }
			    if(recT==recS*7/4){
			    	limit = true;
			    }
			    if(recT==recS/4){
			    	limit = false;
			    }
		    }
		}
		
		public void drawFreeStyleFill(Graphics g, int cenX, int cenY){
			g.drawLine(prev[0]       , prev[1]       , posv[0]       , posv[1]       );
			k.drawLine(prev[0]-xShift, prev[1]-yShift, posv[0]-xShift, posv[1]-yShift); 
		    if(symme==true){
		    	g.drawLine(2*cenX-prev[0]       , prev[1]       , 2*cenX-posv[0]       , posv[1]       );
		    	k.drawLine(2*cenX-prev[0]-xShift, prev[1]-yShift, 2*cenX-posv[0]-xShift, posv[1]-yShift);
		    }
		    posd[0] = posv[0]-cenX;
		    posd[1] = posv[1]-cenY;
		    pred[0] = prev[0]-cenX;
		    pred[1] = prev[1]-cenY;
		    
		    for(int i=0; i<num; ++i){
		    	nx1 = pred[0]*Math.cos(angle) - pred[1]*Math.sin(angle);
		    	ny1 = pred[0]*Math.sin(angle) + pred[1]*Math.cos(angle);
		    	nx2 = posd[0]*Math.cos(angle) - posd[1]*Math.sin(angle);
		    	ny2 = posd[0]*Math.sin(angle) + posd[1]*Math.cos(angle);
			   	
		    	g.drawLine((int) nx1+cenX       , (int) ny1+cenY       , (int) nx2+cenX       , (int) ny2+cenY       );
		    	k.drawLine((int) nx1+cenX-xShift, (int) ny1+cenY-yShift, (int) nx2+cenX-xShift, (int) ny2+cenY-yShift);
		    	
		    	if(symme==true){
		    		g.drawLine((int) -nx1+cenX       , (int) ny1+cenY       , (int) -nx2+cenX       , (int) ny2+cenY       );
		    		k.drawLine((int) -nx1+cenX-xShift, (int) ny1+cenY-yShift, (int) -nx2+cenX-xShift, (int) ny2+cenY-yShift);
		    	}
		    	posd[0] = nx2;
		    	posd[1] = ny2;
		    	pred[0] = nx1;
		    	pred[1] = ny1;
		    }
		    prev[0] = posv[0];
		    prev[1] = posv[1];
		    listX.add(posv[0]);
		    listY.add(posv[1]);
		    vertexCount++;
		}
		
		public void drawFreeStyleFillClones(Graphics g, int coorX[], int coorY[], int cenX, int cenY){
			int  coorYT[] = new int[vertexCount];
			int  coorXT[] = new int[vertexCount];
			int coorYRT[] = new int[vertexCount];
			int coorXRT[] = new int[vertexCount];
			for(int j=0;j<vertexCount;++j){
				coorYT[j] = coorY[j]-yShift;
				coorXT[j] = coorX[j]-xShift;
			}
			g.fillPolygon(coorX,coorY ,vertexCount);
			k.fillPolygon(coorXT,coorYT,vertexCount);
			for(int j=0;j<vertexCount;++j){
				coorXT[j] = 2*cenX-coorX[j];
				coorXRT[j] = 2*cenX-coorX[j]-xShift;
			}
			if(symme==true){
				g.fillPolygon(coorXT,coorY ,vertexCount);
				k.fillPolygon(coorXRT,coorYT,vertexCount);
		    }
			
			double coorYR[] = new double[vertexCount];
			double coorXR[] = new double[vertexCount];
			for(int z=0; z<vertexCount; ++z){
				coorYR[z] = coorY[z]-cenY;
				coorXR[z] = coorX[z]-cenX;
			}	
			double coorXD[] = new double[vertexCount];
			double coorYD[] = new double[vertexCount];
			
		    for(int i=0; i<num; ++i){
		    	for(int f=0;f<vertexCount; ++f){
		    		coorXD[f] = coorXR[f]*Math.cos(angle) - coorYR[f]*Math.sin(angle); 
			    	coorYD[f] = coorXR[f]*Math.sin(angle) + coorYR[f]*Math.cos(angle);
			    	coorXT[f] = (int)coorXD[f]+cenX-orXShf;
			    	coorYT[f] = (int)coorYD[f]+cenY  ;
			    	coorYRT[f]= (int)coorYT[f]-yShift   ;
			    	coorXRT[f]= (int)(cenX-coorXD[f])-xShift;
		    	}
		    	g.fillPolygon(coorXT,coorYT ,vertexCount);
		    	k.fillPolygon(coorXT,coorYRT,vertexCount);
		    	
		    	if(symme==true){
			    	g.fillPolygon(coorXRT,coorYT ,vertexCount);
			    	k.fillPolygon(coorXRT,coorYRT,vertexCount);
		    	}
		    	for(int f=0; f<vertexCount; ++f){
		    		coorXR[f] = coorXD[f];
		    		coorYR[f] = coorYD[f];
		    	}
	    }
		}
		
		public void drawTriangles(Graphics g, int cenX, int cenY){
			g.drawLine((int)trip[0][0]       ,(int)trip[0][1]       ,(int)trip[1][0]       ,(int)trip[1][1]       );
		    g.drawLine((int)trip[1][0]       ,(int)trip[1][1]       ,(int)trip[2][0]       ,(int)trip[2][1]       );
		    g.drawLine((int)trip[2][0]       ,(int)trip[2][1]       ,(int)trip[0][0]       ,(int)trip[0][1]       );
		    k.drawLine((int)trip[0][0]-xShift,(int)trip[0][1]-yShift,(int)trip[1][0]-xShift,(int)trip[1][1]-yShift);
		    k.drawLine((int)trip[1][0]-xShift,(int)trip[1][1]-yShift,(int)trip[2][0]-xShift,(int)trip[2][1]-yShift);
		    k.drawLine((int)trip[2][0]-xShift,(int)trip[2][1]-yShift,(int)trip[0][0]-xShift,(int)trip[0][1]-yShift);
		    if(symme == true){  
			    g.drawLine(2*cenX-(int)trip[0][0]       ,(int)trip[0][1]       ,2*cenX-(int)trip[1][0]       ,(int)trip[1][1]       );
			    g.drawLine(2*cenX-(int)trip[1][0]       ,(int)trip[1][1]       ,2*cenX-(int)trip[2][0]       ,(int)trip[2][1]       );
			    g.drawLine(2*cenX-(int)trip[2][0]       ,(int)trip[2][1]       ,2*cenX-(int)trip[0][0]       ,(int)trip[0][1]       );
			    k.drawLine(2*cenX-(int)trip[0][0]-xShift,(int)trip[0][1]-yShift,2*cenX-(int)trip[1][0]-xShift,(int)trip[1][1]-yShift);
			    k.drawLine(2*cenX-(int)trip[1][0]-xShift,(int)trip[1][1]-yShift,2*cenX-(int)trip[2][0]-xShift,(int)trip[2][1]-yShift);
			    k.drawLine(2*cenX-(int)trip[2][0]-xShift,(int)trip[2][1]-yShift,2*cenX-(int)trip[0][0]-xShift,(int)trip[0][1]-yShift);			    	
		    }
		    trid[0][0] = trip[0][0] - cenX;
		    trid[1][0] = trip[1][0] - cenX;
		    trid[2][0] = trip[2][0] - cenX;
		    trid[0][1] = trip[0][1] - cenY;
		    trid[1][1] = trip[1][1] - cenY;
		    trid[2][1] = trip[2][1] - cenY;

		    for(int i=0; i<num; ++i){
		    	nx1 = trid[0][0]*Math.cos(angle) - trid[0][1]*Math.sin(angle);
		    	ny1 = trid[0][0]*Math.sin(angle) + trid[0][1]*Math.cos(angle);
		    	nx2 = trid[1][0]*Math.cos(angle) - trid[1][1]*Math.sin(angle);
		    	ny2 = trid[1][0]*Math.sin(angle) + trid[1][1]*Math.cos(angle);
		    	nx3 = trid[2][0]*Math.cos(angle) - trid[2][1]*Math.sin(angle);
		    	ny3 = trid[2][0]*Math.sin(angle) + trid[2][1]*Math.cos(angle);
		    	g.drawLine((int)nx1+cenX       ,(int)ny1+cenY       ,(int)nx2+cenX       ,(int)ny2+cenY       );
		    	g.drawLine((int)nx2+cenX       ,(int)ny2+cenY       ,(int)nx3+cenX       ,(int)ny3+cenY       );
		    	g.drawLine((int)nx3+cenX       ,(int)ny3+cenY       ,(int)nx1+cenX       ,(int)ny1+cenY       );
		    	k.drawLine((int)nx1+cenX-xShift,(int)ny1-yShift+cenY,(int)nx2+cenX-xShift,(int)ny2-yShift+cenY);
		    	k.drawLine((int)nx2+cenX-xShift,(int)ny2-yShift+cenY,(int)nx3+cenX-xShift,(int)ny3-yShift+cenY);
		    	k.drawLine((int)nx3+cenX-xShift,(int)ny3-yShift+cenY,(int)nx1+cenX-xShift,(int)ny1-yShift+cenY);
		    	
		    	if(symme==true){
		    		g.drawLine((int)-nx1+cenX       ,(int)ny1+cenY       ,(int)-nx2+cenX,(int)ny2    +cenY          );
		    		g.drawLine((int)-nx2+cenX       ,(int)ny2+cenY       ,(int)-nx3+cenX,(int)ny3    +cenY          );
		    		g.drawLine((int)-nx3+cenX       ,(int)ny3+cenY       ,(int)-nx1+cenX,(int)ny1    +cenY          );
		    		k.drawLine((int)-nx1+cenX-xShift,(int)ny1-yShift+cenY,(int)-nx2+cenX-xShift,(int)ny2-yShift+cenY);
		    		k.drawLine((int)-nx2+cenX-xShift,(int)ny2-yShift+cenY,(int)-nx3+cenX-xShift,(int)ny3-yShift+cenY);
		    		k.drawLine((int)-nx3+cenX-xShift,(int)ny3-yShift+cenY,(int)-nx1+cenX-xShift,(int)ny1-yShift+cenY);
		    	}
		    	trid[0][0] = nx1;
		    	trid[1][0] = nx2;
		    	trid[2][0] = nx3;
		    	trid[0][1] = ny1;
		    	trid[1][1] = ny2;
		    	trid[2][1] = ny3;
		    }
		    if(rotar==true){
		    	trip[0][0] =  recT/2*Math.sin(angleCount*2*Math.PI/360);
		    	trip[0][1] = -recT/2*Math.cos(angleCount*2*Math.PI/360);
		    }
		    else{
		    	trip[0][0] =  recT/2*Math.sin(2*Math.PI/3);
		    	trip[0][1] = -recT/2*Math.cos(2*Math.PI/3);
		    }
		    if(state.equals("TRIANGLEROT")){
		    	trip[0][0] =  lind[0] - posv[0];
			    trip[0][1] =  lind[1] - posv[1];
		    }
		    trip[1][0] =  trip[0][0]*Math.cos(2*Math.PI/3) - trip[0][1]*Math.sin(2*Math.PI/3);
		    trip[1][1] =  trip[0][0]*Math.sin(2*Math.PI/3) + trip[0][1]*Math.cos(2*Math.PI/3);
		    trip[2][0] =  trip[1][0]*Math.cos(2*Math.PI/3) - trip[1][1]*Math.sin(2*Math.PI/3);
		    trip[2][1] =  trip[1][0]*Math.sin(2*Math.PI/3) + trip[1][1]*Math.cos(2*Math.PI/3);
		    trip[0][0] += posv[0];
		    trip[0][1] += posv[1];
		    trip[1][0] += posv[0];
		    trip[1][1] += posv[1];
		    trip[2][0] += posv[0];
		    trip[2][1] += posv[1];
		    if(angleCount<=360){
		    	angleCount++;
		    }
		    else{
		    	angleCount = 1;
		    }
		    if(varia==true){
			    if(limit == false){
			    	recT++;
			    }
			    if(limit == true){
			    	recT--;
			    }
			    if(recT==recS*7/4){
			    	limit = true;
			    }
			    if(recT==recS/4){
			    	limit = false;
			    }
		    }
		}
		
		public void drawSquares(Graphics g, int cenX, int cenY){
			g.drawLine((int)quad[0][0]       ,(int)quad[0][1]       ,(int)quad[1][0]       ,(int)quad[1][1]       );
		    g.drawLine((int)quad[1][0]       ,(int)quad[1][1]       ,(int)quad[2][0]       ,(int)quad[2][1]       );
		    g.drawLine((int)quad[2][0]       ,(int)quad[2][1]       ,(int)quad[3][0]       ,(int)quad[3][1]       );
		    g.drawLine((int)quad[3][0]       ,(int)quad[3][1]       ,(int)quad[0][0]       ,(int)quad[0][1]       );
		    k.drawLine((int)quad[0][0]-xShift,(int)quad[0][1]-yShift,(int)quad[1][0]-xShift,(int)quad[1][1]-yShift);
		    k.drawLine((int)quad[1][0]-xShift,(int)quad[1][1]-yShift,(int)quad[2][0]-xShift,(int)quad[2][1]-yShift);
		    k.drawLine((int)quad[2][0]-xShift,(int)quad[2][1]-yShift,(int)quad[3][0]-xShift,(int)quad[3][1]-yShift);
		    k.drawLine((int)quad[3][0]-xShift,(int)quad[3][1]-yShift,(int)quad[0][0]-xShift,(int)quad[0][1]-yShift);
		    if(symme == true){
			    g.drawLine(2*cenX-(int)quad[0][0]       ,(int)quad[0][1]       ,2*cenX-(int)quad[1][0]       ,(int)quad[1][1]       );
			    g.drawLine(2*cenX-(int)quad[1][0]       ,(int)quad[1][1]       ,2*cenX-(int)quad[2][0]       ,(int)quad[2][1]       );
			    g.drawLine(2*cenX-(int)quad[2][0]       ,(int)quad[2][1]       ,2*cenX-(int)quad[3][0]       ,(int)quad[3][1]       );
			    g.drawLine(2*cenX-(int)quad[3][0]       ,(int)quad[3][1]       ,2*cenX-(int)quad[0][0]       ,(int)quad[0][1]       );
			    k.drawLine(2*cenX-(int)quad[0][0]-xShift,(int)quad[0][1]-yShift,2*cenX-(int)quad[1][0]-xShift,(int)quad[1][1]-yShift);
			    k.drawLine(2*cenX-(int)quad[1][0]-xShift,(int)quad[1][1]-yShift,2*cenX-(int)quad[2][0]-xShift,(int)quad[2][1]-yShift);
			    k.drawLine(2*cenX-(int)quad[2][0]-xShift,(int)quad[2][1]-yShift,2*cenX-(int)quad[3][0]-xShift,(int)quad[3][1]-yShift);	
			    k.drawLine(2*cenX-(int)quad[3][0]-xShift,(int)quad[3][1]-yShift,2*cenX-(int)quad[0][0]-xShift,(int)quad[0][1]-yShift);			    	
		    }
		    quap[0][0] = quad[0][0] - cenX;
		    quap[1][0] = quad[1][0] - cenX;
		    quap[2][0] = quad[2][0] - cenX;
		    quap[3][0] = quad[3][0] - cenX;
		    quap[0][1] = quad[0][1] - cenY;
		    quap[1][1] = quad[1][1] - cenY;
		    quap[2][1] = quad[2][1] - cenY;
		    quap[3][1] = quad[3][1] - cenY;

		    for(int i=0; i<num; ++i){
		    	nx1 = quap[0][0]*Math.cos(angle) - quap[0][1]*Math.sin(angle);
		    	ny1 = quap[0][0]*Math.sin(angle) + quap[0][1]*Math.cos(angle);
		    	nx2 = quap[1][0]*Math.cos(angle) - quap[1][1]*Math.sin(angle);
		    	ny2 = quap[1][0]*Math.sin(angle) + quap[1][1]*Math.cos(angle);
		    	nx3 = quap[2][0]*Math.cos(angle) - quap[2][1]*Math.sin(angle);
		    	ny3 = quap[2][0]*Math.sin(angle) + quap[2][1]*Math.cos(angle);
		    	nx4 = quap[3][0]*Math.cos(angle) - quap[3][1]*Math.sin(angle);
		    	ny4 = quap[3][0]*Math.sin(angle) + quap[3][1]*Math.cos(angle);

		    	g.drawLine((int)nx1+cenX,(int)ny1+cenY,(int)nx2+cenX,(int)ny2+cenY);
		    	g.drawLine((int)nx2+cenX,(int)ny2+cenY,(int)nx3+cenX,(int)ny3+cenY);
		    	g.drawLine((int)nx3+cenX,(int)ny3+cenY,(int)nx4+cenX,(int)ny4+cenY);
		    	g.drawLine((int)nx4+cenX,(int)ny4+cenY,(int)nx1+cenX,(int)ny1+cenY);

		    	k.drawLine((int)nx1+cenX-xShift,(int)ny1-yShift+cenY,(int)nx2+cenX-xShift,(int)ny2-yShift+cenY);
		    	k.drawLine((int)nx2+cenX-xShift,(int)ny2-yShift+cenY,(int)nx3+cenX-xShift,(int)ny3-yShift+cenY);
		    	k.drawLine((int)nx3+cenX-xShift,(int)ny3-yShift+cenY,(int)nx4+cenX-xShift,(int)ny4-yShift+cenY);
		    	k.drawLine((int)nx4+cenX-xShift,(int)ny4-yShift+cenY,(int)nx1+cenX-xShift,(int)ny1-yShift+cenY);
		    	
		    	if(symme==true){
		    		g.drawLine((int)-nx1+cenX,(int)ny1+cenY,(int)-nx2+cenX,(int)ny2+cenY);
		    		g.drawLine((int)-nx2+cenX,(int)ny2+cenY,(int)-nx3+cenX,(int)ny3+cenY);
		    		g.drawLine((int)-nx3+cenX,(int)ny3+cenY,(int)-nx4+cenX,(int)ny4+cenY);
		    		g.drawLine((int)-nx4+cenX,(int)ny4+cenY,(int)-nx1+cenX,(int)ny1+cenY);

		    		k.drawLine((int)-nx1+cenX-xShift,(int)ny1-yShift+cenY,(int)-nx2+cenX-xShift,(int)ny2-yShift+cenY);
		    		k.drawLine((int)-nx2+cenX-xShift,(int)ny2-yShift+cenY,(int)-nx3+cenX-xShift,(int)ny3-yShift+cenY);
		    		k.drawLine((int)-nx3+cenX-xShift,(int)ny3-yShift+cenY,(int)-nx4+cenX-xShift,(int)ny4-yShift+cenY);
		    		k.drawLine((int)-nx4+cenX-xShift,(int)ny4-yShift+cenY,(int)-nx1+cenX-xShift,(int)ny1-yShift+cenY);
		    	}
		    	quap[0][0] = nx1;
		    	quap[1][0] = nx2;
		    	quap[2][0] = nx3;
		    	quap[3][0] = nx4;
		    	quap[0][1] = ny1;
		    	quap[1][1] = ny2;
		    	quap[2][1] = ny3;
		    	quap[3][1] = ny4;
		    }
		    if(rotar==true){
		    	quad[0][0] = -recT/2*Math.cos(angleCount*2*Math.PI/360) + recT/2*Math.sin(angleCount*2*Math.PI/360); 
		    	quad[0][1] = -recT/2*Math.sin(angleCount*2*Math.PI/360) - recT/2*Math.cos(angleCount*2*Math.PI/360); 
		    	
		    	if(state.equals("SQUARTILT")){
			    	quad[0][0] =  recT/2*Math.sin(angleCount*2*Math.PI/360);
			    	quad[0][1] = -recT/2*Math.cos(angleCount*2*Math.PI/360);
		    	}
		    }
		    else{
		    	quad[0][0] =  - recT/2;
		    	quad[0][1] =  - recT/2;
		    	if(state.equals("SQUARTILT")){
			    	quad[0][0] =  recT/2*Math.sin(angleCount*2*Math.PI/4);
			    	quad[0][1] = -recT/2*Math.cos(angleCount*2*Math.PI/4);
		    	}
		    }
		    if(state.equals("SQUAREROT")){
		    	quad[0][0] = lind[0] - posv[0];
		    	quad[0][1] = lind[1] - posv[1];
		    }
		    quad[1][0] =  quad[0][0]*Math.cos(2*Math.PI/4) - quad[0][1]*Math.sin(2*Math.PI/4);
		    quad[1][1] =  quad[0][0]*Math.sin(2*Math.PI/4) + quad[0][1]*Math.cos(2*Math.PI/4);
		    quad[2][0] =  quad[1][0]*Math.cos(2*Math.PI/4) - quad[1][1]*Math.sin(2*Math.PI/4);
		    quad[2][1] =  quad[1][0]*Math.sin(2*Math.PI/4) + quad[1][1]*Math.cos(2*Math.PI/4);
		    quad[3][0] =  quad[2][0]*Math.cos(2*Math.PI/4) - quad[2][1]*Math.sin(2*Math.PI/4);
		    quad[3][1] =  quad[2][0]*Math.sin(2*Math.PI/4) + quad[2][1]*Math.cos(2*Math.PI/4);
		    quad[0][0] += posv[0];
		    quad[0][1] += posv[1];
		    quad[1][0] += posv[0];
		    quad[1][1] += posv[1];
		    quad[2][0] += posv[0];
		    quad[2][1] += posv[1];
		    quad[3][0] += posv[0];
		    quad[3][1] += posv[1];
		    if(angleCount<=360){
		    	angleCount++;
		    }
		    else{
		    	angleCount = 1;
		    }
		    if(varia==true){
			    if(limit == false){
			    	recT++;
			    }
			    if(limit == true){
			    	recT--;
			    }
			    if(recT==recS*7/4){
			    	limit = true;
			    }
			    if(recT==recS/4){
			    	limit = false;
			    }
		    }
		}

		
				
		public void setStatus(String status, Color background){             // resets image
			state = status                                                ;
			currWCol = background                                         ; // new background color
			Graphics g = this.getGraphics()                               ;
			xShift = -(horizontal/2 - this.getWidth()/2)                  ; //xShify and yShift are recalculated with a centered image
			yShift = -(vertical/2 - this.getHeight()/2)                   ;
			orXShf = xShift                                               ;
			orYShf = yShift                                               ;
			centX.set(0,this.getWidth()/2)                                ; // center coordinates are restored to the center of the image and window
			centY.set(0,this.getHeight()/2)                               ;
			mosX = centX.get(0)                                           ; // cache of where the center is in respect to the real image's center
			mosY = centY.get(0)                                           ;
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			image= new BufferedImage(horizontal, vertical, BufferedImage.TYPE_INT_RGB); // the old image is sent to oblivion (well, really to the Undo stack)
			k = image.getGraphics()                                       ;
			((Graphics2D) k).setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			k.setColor(currWCol)                                          ;
			g.setColor(currWCol)                                          ;
			g.fillRect(xShift,yShift,horizontal,vertical);
			k.fillRect(0,0,horizontal,vertical)                           ;
			stackIzq.push(image)                                          ; // See, old image is just at the Undo stack
			repaint()                                                     ;
		}
		
		public void eraser(){
			stackDer.clear()         ; // cleans any possible Redo stack
			setStatus(state,currWCol); // resets image	
		}

		public void updata(Color curCol, int value){
			this.value = value;
			currBCol = curCol ;
		}
		
		
		public void saveIMG(){ // this way, I can only save images to the place the .jar file is located on...
			try{
				ImageIO.write((RenderedImage) stackIzq.peek(), "png", new File(JOptionPane.showInputDialog(this,"Input the desired file name (file will be saved in the executable's folder)") + ".png"));
			}
			catch (IOException exception){}
		}
		
		public void mouseDragged(MouseEvent arg0){

			mouseMoved(arg0);
		}
		
		public void bucketFill(int x, int y){
			try{
				if(visit[x][y]==true){
					return;
				}
				visit[x][y] = true;
				((BufferedImage) image).setRGB(x, y, currBCol.getRGB());
				
				if(x-1>0 && visit[x-1][y]==false && ((BufferedImage) image).getRGB(x-1, y)==origin){
					bucketFill(x-1,y);
				}
				if(x+1<960 && visit[x+1][y]==false && ((BufferedImage) image).getRGB(x+1, y)==origin){
					bucketFill(x+1,y);
				}
				if(y-1>0 && visit[x][y-1]==false && ((BufferedImage) image).getRGB(x, y-1)==origin){
					bucketFill(x,y-1);
				}
				if(y+1<960 && visit[x][y+1]==false && ((BufferedImage) image).getRGB(x, y+1)==origin){
					bucketFill(x,y+1);
				}
			}
			catch(StackOverflowError e){
			}
			return;
		}
				
		public void mouseReleased(MouseEvent arg0){ // when the mouse is released
			press = false;                          // its obviously not being pressed anymore
			move = false;                           // nor the image will be dragged around in case you were doing it
			
			if(state.equals("FREESTYLE")){          // in a very special case, when 'Free-Style Fill' is being used
				Graphics g = this.getGraphics();
				((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.setColor(currBCol);
				int coorX[] = new int[vertexCount]; // two new arrays of size vertexCount are created
				int coorY[] = new int[vertexCount]; // because the fillPolygon method receives only arrays, not ArrayLists 
				for(int i=0; i<vertexCount; ++i){
					coorX[i] = listX.get(i);        // and everything that was in there gets thrown inside the arrays
					coorY[i] = listY.get(i);
				}
				for(int i=0; i<centX.size();++i){
					drawFreeStyleFillClones(g,coorX,coorY,centX.get(i),centY.get(i)); // and a special drawing method is called, that will fill the
					                                                                  // 'polygon' described by the 'Free-Style Fill' tool
				}
			}
			if(!state.equals("CENTER") && !state.equals("DRAG")){ // if the action wasn't 'Change Center' or 'Drag Image Around'
				k = image.getGraphics();
				((Graphics2D) k).setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
				stackIzq.push(image);                             // the current image gets pushed to the Undo stack
				stackDer.clear();                                 // the Redo stack is cleaned
				repaint();
				if(stackIzq.size()>10){                           // and if the Undo stack gets more than 10 elements
					stackIzq.removeElementAt(0);                  // zilch goes boom. you know, because of memory...
				}
			}
			// doing this for the 'Change Center' and 'Drag Image Around' operations would just add 
	        // useless images to the Undo stack, because no real changes were done to the images 
			else{
				if(hold==true && !state.equals("DRAG")){ // if you released the mouse, and hold was true (meaning you were moving the mouse)
					centX.set(0, arg0.getX());           // the current mouse position is set as the new center
					centY.set(0, arg0.getY());
					mosX = centX.get(0);                 // the relative position to the center of the image is the same as the center
					mosY = centY.get(0);
					hold = false;                        // it is no longer moving the center
				}
			}
			repaint();
		}
		
		public void resetPosition(){
			centX.set(0, mosX-xShift+orXShf);
			centY.set(0, mosY-yShift+orYShf);
			xShift = -(horizontal/2 - this.getWidth()/2 );
			yShift = -(vertical/2   - this.getHeight()/2);
		}
		
		public void shifter(){
			int red = currBCol.getRed();
			int gre = currBCol.getGreen();
			int blu = currBCol.getBlue();
			if(redi==true){
				if(red<255){
					red+=10;
				}
				else{
					if(blu>0){
						blu-=10;
					}
					else{
						redi = false;
						grei = true;
					}
				}
			}
			if(grei==true){
				if(gre<255){
					gre+=10;
				}
				else{
					if(red>0){
						red-=10;
					}
					else{
						grei = false;
						blui = true;
					}
				}
			}
			if(blui==true){
				if(blu<255){
					blu+=10;
				}
				else{
					if(gre>0){
						gre-=10;
					}
					else{
						blui = false;
						redi = true;
					}
				}
			}
			if(red<0){red=0;}
			if(red>255){red=255;}

			if(gre<0){gre=0;}
			if(gre>255){gre=255;}

			if(blu<0){blu=0;}
			if(blu>255){blu=255;}
			currBCol = new Color(red,gre,blu);
		}
		public void initShift(){
			int red = tempBCol.getRed();
			int gre = tempBCol.getGreen();
			int blu = tempBCol.getBlue();
			int maye = Math.max(Math.max(red, gre),blu);
			if(maye == red){
				blu = gre = 0;
				redi = true;
				blui = grei = false;
			}
			else{
				if(maye == gre){
					red = blu = 0;
					grei = true;
					blui = redi = false;

				}
				else{
					red = gre = 0;
					blui = true;
					grei = redi = false;
				}
			}
			currBCol = new Color(red,gre,blu);
			
		}
		public void mouseMoved(MouseEvent arg0){
			if(move==true){                       // if the canvas, mother of all that is good in this program, gets moved, everything based on coordinates has to move   
				x = (int) arg0.getPoint().getX(); // the coordinates of the mouse are set to a couple
				y = (int) arg0.getPoint().getY(); // of simple containers. 
				deltaY = y-z;                     // x and y are current mouse coordinates. w and z are previous mouse coordinates. pretty much a delta
				deltaX = x-w;
				yShift +=deltaY;
				mosY   += deltaY;
				centY.set(0, centY.get(0)+deltaY);
				xShift +=deltaX;
				mosX   +=deltaX;
				centX.set(0, centX.get(0)+deltaX);
				repaint();
				w = x;
				z = y;
			}
			else{                                   // if it ain't being moved, the previous coordinates will still be stored, because if you were to depend
				if(color==true){
					shifter();
				}
				w = (int) arg0.getPoint().getX();	// on the coordinates from the last time you were dragging the image around, then there would be a huge
				z = (int) arg0.getPoint().getY();	// undesired leap in the image movement 
				if(turn==true){                     // this is what makes the program skip one of the registered mouse coordinates
					posc[0] = posv[0] = (int) arg0.getX();
					posc[1] = posv[1] = (int) arg0.getY();
					if(press==true){
						lapiZ();                    // the method that will proced to call almost any drawing method
					}	
					turn = true;
				}
				else{
					turn = true;
				}
				if(state.equals("CENTER")){
					repaint();
				}
			}
		}
	
		// these are here because Java would give me a headache if they weren't
		public void mouseClicked(MouseEvent arg0){
		}
		public void mouseEntered(MouseEvent arg0){
		}
		public void mouseExited(MouseEvent arg0){}

		public void undoRedo(String op) { // so, I heard you like undoing and redoing all the... whatever, its a stack game
			if(op.equals("undo") && stackIzq.size()>1){ //there has to be always something on the Undo stack, since whats painted its the top element
				stackDer.push(stackIzq.pop());          
				repaint();
			}
			if(op.equals("redo") && stackDer.size()>0){  // there is no lower limits to the Redo stack, the upper limit is also 10, but not by definition
				stackIzq.push(stackDer.pop());           // but because the only way it can get stuff is taking it from the Undo stack, which has a explicit
				repaint();                               // 10 item limit
			}
		}
		
		static BufferedImage deepCopy(BufferedImage bi){ // Am I worthy of using this code? Oh, gods at Stack Overflow, it is thanks to you that I was able to 
			 ColorModel cm = bi.getColorModel();         // get Undo/Redo working, seriously, why the hell doesn't Java have a clone method for Image/BufferedImage?
			 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
			 WritableRaster raster = bi.copyData(null);
			 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
		}
}   
		// It was nice to use you, Java, but my next project will be in Qt, C++! Obviously it doesn't gives me the "Portability" but its C++, you know..