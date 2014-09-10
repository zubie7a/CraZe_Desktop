package pack;


import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent   ;
import javax.swing.event.ChangeListener       ;
import javax.swing.border.TitledBorder        ;
import javax.swing.ListSelectionModel         ;
import javax.swing.SpinnerNumberModel         ;
import java.awt.event.ActionListener          ;
import javax.swing.border.LineBorder          ;
import javax.swing.event.ChangeEvent          ;
import javax.swing.SwingConstants             ;
import java.awt.event.ActionEvent             ;
import java.text.DecimalFormat                ;
import javax.swing.JScrollPane                ;
import javax.swing.JOptionPane                ;
import javax.swing.JTextField                 ;
import javax.swing.JCheckBox                  ;
import javax.swing.JSpinner                   ;
import javax.swing.JSlider                    ;
import javax.swing.JButton                    ;
import java.awt.GridLayout                    ;
import javax.swing.JPanel                     ;
import javax.swing.JLabel                     ;
import javax.swing.JList                      ;
import java.awt.Color                         ;

public class Panopt extends JPanel implements ListSelectionListener, ActionListener, ChangeListener{
	//There are three kinds of listeners implemented:
	//       -ListSelectionListener: It makes the program react when certain element at the JList is selected, but it doesn't really tell the specific
	//                               object that was selected, but the index it occupies
	//       -ActionListener:        It makes the program react when stuff like a JButton, JCheckBox, so elements with immediate actions are selected
	//       -ChangeListener:        It makes the program react when stuff like a JSpinner or JSlider are changed, as those contain modifiable values
	
	private static final long serialVersionUID = 1L; //This thing is just everywhere

	private JSpinner.NumberEditor dec; // This is used to allow the JSpinner angle to contain double values
	private JScrollPane  listScroller; // JScrollPane, is a container for the JList list, adding a scroller to traverse it vertically
	private TitledBorder dialogRedBor; // Red border for the JSlider dialogRedSli in the 'New Image' dialog window. Title contains the slider value
	private TitledBorder dialogGreBor; // Green border for the JSlider dialogGreSli in the 'New Image' dialog window. Title contains the slider value
	private TitledBorder dialogBluBor; // Blue border for the JSlider dialogBluSli in the 'New Image' dialog window. Title contains the slider value
	private TitledBorder redBor      ; // Red border for the JSlider redSli in the Main Window. Title contains the slider value
	private TitledBorder greBor      ; // Green border for the JSlider greSli in the Main Window. Title contains the slider value
	private TitledBorder bluBor      ; // Blue border for the JSlider bluSli in the Main Window. Title contains the slider value
	private JTextField backrCol      ; // Contains no text, but will be filled with the resulting color of the JSliders at the 'New Image' dialog
	private JTextField brushCol      ; // Contains no text, but will be filled with the resulting color of the JSliders at the Main Window 
	private JTextField widthVal      ; // Its at the 'New Image' dialog, and receives the value of the New Image's desired width 
	private JTextField heightVal     ; // Its at the 'New Image' dialog, and receives the value of the New Image's desired height 
	private JSlider dialogRedSli     ; // Slider for the Red Hue value at the 'New Image' dialog, its values ranges from 0 to 255
	private JSlider dialogGreSli     ; // Slider for the Green Hue value at the 'New Image' dialog, its values ranges from 0 to 255
	private JSlider dialogBluSli     ; // Slider for the Blue Hue value at the 'New Image' dialog, its values ranges from 0 to 255
	private JSlider redSli           ; // Slider for the Red Hue value at the Main Window, its values ranges from 0 to 255
	private JSlider greSli           ; // Slider for the Green Hue value at the Main Window, its values ranges from 0 to 255
	private JSlider bluSli           ; // Slider for the Blue Hue value at the Main Window, its values ranges from 0 to 255
	private JPanel stylePan          ; // The main container of the options, like various sub JPanels detailed below, and JCheckboxes
	                                   // sym, var, rot, bor, con.
	private JPanel altAnPan          ; //       sub JPanel containing the JCheckBox ena, and the JSpinner angle
	private JPanel spinnPan          ; //       sub JPanel containing the JLabel rots and the JSpinner spin
	private JPanel bSizePan          ; //       sub JPanel containing the JLabel size and the JSpinner recS
	private JPanel craZePan          ; // The main container of the sub JPanel containing the RGB JSliders and the JPanel containing the buttons
	private JPanel colorPan          ; //       sub JPanel containing the RGB JSliders of the Main Window
	private JPanel buttnPan          ; //       sub JPanel containing the various file manipulation JButtons, like savePNG, newIMG, and the sub JPanel undRePan
	private JPanel undRePan          ; //              sub sub JPanel containing the JButtons undoZ and redoZ
	private JPanel dialgPan          ; // The main container of the 'New Image' dialog components, such as the RGB JSliders, and the resolution values
	private JPanel dimenPan          ; //       sub JPanel containing the JTextField widthVal and heightVal
	private JButton savePNG          ; // Saves images to the place the .jar file is located
	private JButton newImg           ; // Creates new images. Erases the previous one, asks for new resolution, and new background color
	private JButton undoZ            ; // Undoes stuff
	private JButton redoZ            ; // Redoes stuff
	                                   // [And all the others]
	private String data[]            ; // Contains the different options for the JList list, stored as String objects
	private Color curBCol            ; // Current Brush color
	private Color curWCol            ; // Current Background color
	private JCheckBox sym            ; // Enables/Disables symmetry
	private JCheckBox var            ; // Enables/Disables variable size
	private JCheckBox col            ; // Enables/Disables changing colors
	private JCheckBox rot            ; // Enables/Disables rotating brushes
	private JCheckBox bor            ; // Enables/Disables connected borders
	private JCheckBox con            ; // Enables/Disables connected centers
	private JCheckBox ang            ; // Enables/Disables the alteration of angles
	private JSpinner recS            ; // Allows the user to modify brush size value
	private JSpinner spin            ; // Allows the user to modify the spin number
	public JSpinner angle            ; // Allows the user to modify rotation angle
	private Craze craze              ; // The (God)Father
	private JLabel rots              ; // It says "Spins: " and its located at the Main Window
	private JLabel size              ; // It says "Brush Size: " and its located at the Main Window
	private JLabel width             ; // It says "Width: " and its located at the 'New Image' dialog
	private JLabel height            ; // It says "Height: " and its located at the 'New Image' dialog
	private JList list               ; // Contains the brushes and other image options

	public Panopt(Craze craze){
		        
		this.setLayout(new GridLayout(3,1)); // The left panel is separated into three main sub-spaces
		                                     // The first subspace contains the JScrollPane listScroller, with the brushes and image editing options
		                                     // The second subspace contains all the parameters (JCheckBoxes) and values (JSpinners)
		                                     // The third subspace contains the RGB JSliders, and the buttons for file operations
		this.setBackground(Color.GRAY);
		this.setVisible(true)         ;
		this.craze=craze              ;

		recS = new JSpinner()       ;
		recS.setValue(7)            ;
		recS.addChangeListener(this);

		angle = new JSpinner(new SpinnerNumberModel(360.0, 0.1, 360.0, 0.1)); //Starting value: 360, range: 0.1-360, step:0.1
		angle.addChangeListener(this);
		angle.setEnabled(false); 
		
		dec = (JSpinner.NumberEditor)angle.getEditor(); // This allows the JSpinner angle to contain doubles 
        DecimalFormat format = dec.getFormat()        ;    
        format.setMinimumFractionDigits(1)            ;   
		
		spin = new JSpinner()       ;
		spin.setValue(1)            ; // By default, only one spin on screen, so what you draw isn't rotated (when you draw, it rotates stuff n-1 times)
		                              // Because what you are drawing is counted as the first spin, otherwise it would go all the way around and waste a loop.
		spin.addChangeListener(this);
		
		/* JPanel Constructors */
		// These constructors work as follows:
		// object = new JPanel()                -> instantiates JPanel
		// object.setLayout(new GridLayout(y,x) -> sets a GridLayout, with y vertical spaces and x horizontal spaces
		// object.setBackground(Color...)       -> Colors can be used from defaults (Color.GRAY); or manual colors (new Color(int R, int G, int B));
		stylePan = new JPanel()                    ;
		stylePan.setLayout(new GridLayout(9,1))    ;
		stylePan.setBackground(Color.GRAY.darker());
		
		undRePan = new JPanel()                    ;
		undRePan.setLayout(new GridLayout(1,2))    ;
		undRePan.setBackground(Color.GRAY.darker());
		
		craZePan = new JPanel()                    ;
		craZePan.setLayout(new GridLayout(2,1))    ;
		craZePan.setBackground(Color.GRAY.darker());
		
		altAnPan = new JPanel()                    ;
		altAnPan.setLayout(new GridLayout(1,2))    ;
		altAnPan.setBackground(Color.GRAY.darker());
		
		buttnPan = new JPanel()                    ;
		buttnPan.setLayout(new GridLayout(3,1))    ;
		buttnPan.setBackground(Color.GRAY.darker());

		colorPan = new JPanel()                    ;
		colorPan.setLayout(new GridLayout(4,1))    ;
		colorPan.setBackground(Color.GRAY.darker());
		
		spinnPan = new JPanel()                    ;
		spinnPan.setLayout(new GridLayout(1,2))    ;
		spinnPan.setBackground(Color.GRAY.darker());
		
		bSizePan = new JPanel()                    ;
		bSizePan.setLayout(new GridLayout(1,2))    ;
		bSizePan.setBackground(Color.GRAY.darker());

		dialgPan = new JPanel()                    ;
		dialgPan.setLayout(new GridLayout(5,1))    ;
		dialgPan.setBorder(new TitledBorder("Select Background Color:"));

		dimenPan = new JPanel()                ;
		dimenPan.setLayout(new GridLayout(2,2));

		/* JTextField Constructors */
		// These constructors work as follows:
		// object = new JTextField()      -> instantiates JTextField
		// object.setEditable(false)      -> the JTextFields aren't going to be used to write stuff
		// object.setBackground(Color...) -> Colors can be used from defaults (Color.GRAY); or manual colors (new Color(int R, int G, int B));
		
		backrCol = new JTextField()        ;
		backrCol.setEditable(false)        ;
		backrCol.setBackground(Color.BLACK);
		
		brushCol = new JTextField()        ;
		brushCol.setEditable(false)        ;
		brushCol.setBackground(Color.WHITE);
		
		widthVal  = new JTextField();
		
		heightVal = new JTextField();

		/* JButton Constructors */
		// These constructors work as follows:
		// object = new JButton("String X")    -> instantiates JButton, and passes a string as parameter (or even a icon) to display
		// object.setActionCommand("String Z") -> the program gets "String Z" as a signal that the user interacted with certain object
		// object.addActionListener(this)      -> it connects this class and its listener to the object
		
		savePNG = new JButton("Save Image");
		savePNG.setActionCommand("SAVE")   ;
		savePNG.addActionListener(this)    ;
        			
		newImg = new JButton("New Image")  ;
		newImg.setActionCommand("ERASE")   ;
		newImg.addActionListener(this)     ;
		
		undoZ = new JButton("Un,Do")       ;
		undoZ.setActionCommand("UNDO")     ;
		undoZ.addActionListener(this)      ; 
		
		redoZ = new JButton("Re,Do")       ;
		redoZ.setActionCommand("REDO")     ;
		redoZ.addActionListener(this)      ; 

		/* JChechBox Constructors */
		// These constructors work as follows:
		// object = new JCheckBox("String X")  -> instantiates JCheckBox, and passes a string as parameter to display as label
		// object.setBackground(Color...)      -> Colors can be used from defaults (Color.GRAY); or manual colors (new Color(int R, int G, int B));
		// object.setForeground(Color...)      -> Colors can be used from defaults (Color.GRAY); or manual colors (new Color(int R, int G, int B));
		// object.setActionCommand("String Z") -> the program gets "String Z" as a signal that the user interacted with certain object
		// object.addActionListener(this)      -> it connects this class and its listener to the object

		ang = new JCheckBox("Alter Angle")    ;
		ang.setBackground(Color.GRAY.darker());
		ang.setForeground(Color.WHITE)        ;
		ang.setActionCommand("ANGLE")         ;
		ang.addActionListener(this)           ;

		sym = new JCheckBox("Symmetry")       ;
		sym.setBackground(Color.GRAY.darker());
		sym.setForeground(Color.WHITE)        ;
		sym.setActionCommand("SYMMETRY")      ;
		sym.addActionListener(this)           ;
		sym.setSelected(true)                 ;

		var = new JCheckBox("Variable Size")  ;
		var.setBackground(Color.GRAY.darker());
		var.setForeground(Color.WHITE)        ;
		var.setActionCommand("VARSIZE")       ;
		var.addActionListener(this)           ;
		
		con = new JCheckBox("Connected Center");
		con.setBackground(Color.GRAY.darker()) ;
		con.setForeground(Color.WHITE)         ;
		con.setActionCommand("CONNECT")        ;
		con.addActionListener(this)            ;
       
		rot = new JCheckBox("Rotating Brush")  ;
		rot.setBackground(Color.GRAY.darker()) ;
		rot.setForeground(Color.WHITE)         ;
		rot.setActionCommand("ROTATE")         ;
		rot.addActionListener(this)            ;
		
		bor = new JCheckBox("Draw Borders")    ;
		bor.setBackground(Color.GRAY.darker()) ;
		bor.setForeground(Color.WHITE)         ;
		bor.setActionCommand("BORDERS")        ;
		bor.addActionListener(this)            ;
		
		col = new JCheckBox("Shifting Colors") ;
		col.setBackground(Color.GRAY.darker()) ;
		col.setForeground(Color.WHITE)         ;
		col.setActionCommand("COLORS")         ;
		col.addActionListener(this)            ;

		
		/* JLabel Constructors */
		// These constructors work as follows:
		// object = new JLabel("String Y") -> instantiates JLabel, and passes a string as parameter of its text
		// object.setForeground(Color...)  -> Colors can be used from defaults (Color.WHITE); or manual colors (new Color(int R, int G, int B));

		rots = new JLabel("  Spins: ")  ;
		rots.setForeground(Color.WHITE) ;
       	
		width = new JLabel("Width: ")   ;
		width.setForeground(Color.BLACK);
		
		height = new JLabel("Height: ") ;
		height.setForeground(Color.BLACK);

		size = new JLabel("  Brush Size: ");
		size.setForeground(Color.WHITE)    ;
       	
		/* TitledBorder Constructors */
		// These constructors work as follows:
		// object = new TitledBorder(new LineBorder(Color..., int x), String z) -> instantiates TitledBorder, with a LineBorder, A certain color, int x is the thickness value, String z is the default text
		// object.setTitleColor(Color...)                                       -> Colors can be used from defaults (Color.WHITE); or manual colors (new Color(int R, int G, int B));
		redBor = new TitledBorder(new LineBorder(Color.RED,  2),"255"); 
		greBor = new TitledBorder(new LineBorder(Color.GREEN,2),"255"); 
		bluBor = new TitledBorder(new LineBorder(Color.BLUE, 2),"255"); 
		redBor.setTitleColor(Color.WHITE)                             ;
		greBor.setTitleColor(Color.WHITE)                             ;
		bluBor.setTitleColor(Color.WHITE)                             ;

		dialogRedBor = new TitledBorder(new LineBorder(Color.RED,  2),"0"); 
		dialogGreBor = new TitledBorder(new LineBorder(Color.GREEN,2),"0");  
		dialogBluBor = new TitledBorder(new LineBorder(Color.BLUE, 2),"0"); 
		dialogRedBor.setTitleColor(Color.BLACK)                           ;
		dialogGreBor.setTitleColor(Color.BLACK)                           ;
		dialogBluBor.setTitleColor(Color.BLACK)                           ;	

		/* JSlider Constructors */
		// These constructors work as follows:
		// object = new JSlider(direction, int x, int y, int z) -> instantiates JSlider, direction is a predetermined value, x-y is the range, and z the default value
		// object.addChangeListener(this)                       -> it connects this class and its listener to the object
		// object.setBorder(border)                             -> it sets a previously defined border to the object
		// object.setBackground(Color...)                       -> Colors can be used from defaults (Color.GRAY); or manual colors (new Color(int R, int G, int B));

		redSli = new JSlider(SwingConstants.HORIZONTAL,0,255,255)     ;
		greSli = new JSlider(SwingConstants.HORIZONTAL,0,255,255)     ;
		bluSli = new JSlider(SwingConstants.HORIZONTAL,0,255,255)     ;
		redSli.addChangeListener(this)                                ;
		greSli.addChangeListener(this)                                ;
		bluSli.addChangeListener(this)                                ;
		redSli.setBorder(redBor)                                      ;
		bluSli.setBorder(bluBor)                                      ;
		greSli.setBorder(greBor)                                      ;
		redSli.setBackground(Color.gray.darker())                     ;
		greSli.setBackground(Color.gray.darker())                     ;
		bluSli.setBackground(Color.gray.darker())                     ;
		
		dialogRedSli = new JSlider(SwingConstants.HORIZONTAL,0,255,0)     ;
		dialogGreSli = new JSlider(SwingConstants.HORIZONTAL,0,255,0)     ;
		dialogBluSli = new JSlider(SwingConstants.HORIZONTAL,0,255,0)     ;
		dialogRedSli.addChangeListener(this)                              ;
		dialogGreSli.addChangeListener(this)                              ;
		dialogBluSli.addChangeListener(this)                              ;
		dialogRedSli.setBorder(dialogRedBor)                              ;
		dialogGreSli.setBorder(dialogGreBor)                              ;
		dialogBluSli.setBorder(dialogBluBor)                              ;

		/*Process to build the list*/
		
		data = new String[18]; //an Object array is declared. In this case, the object to use is String
		/* Brushes */
		data[ 0] = "Spiral Power"                  ;
		data[ 1] = "Line Drawer"                   ;
		data[ 2] = "Lines From Start"              ;
		data[ 3] = "Vertical Line"                 ;
		data[ 4] = "Horizontal Line"               ;
		data[ 5] = "Great Cross"                   ;
		data[ 6] = "Triangle"                      ;
		data[ 7] = "Triangle from Start"           ;
		data[ 8] = "Square"                        ;
		data[ 9] = "Square from Start"             ;
		data[10] = "Diamond"                       ;
		data[11] = "Circle"                        ;
		data[12] = "Free-Style Fill"               ;
		data[13] = "Bucket Fill"                   ;
		/* Tools */
		data[14] = "Change Center"                 ;
		data[15] = "Reset Center"                  ;
		data[16] = "Drag Image Around"             ;
		data[17] = "Reset Image Position"          ;
		
		list = new JList(data)                                             ; // a JList is instantiated using the String array as parameter
		list.setForeground(Color.WHITE)                                    ; 
		list.addListSelectionListener(this)                                ; // it connects this class and its listener to the object
		list.setLayoutOrientation(JList.VERTICAL)                          ;	 	
		list.setBackground(Color.GRAY.darker().darker())                   ;
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION); // a single element can be selected at once
		
		listScroller = new JScrollPane(list)                 ; // a JScrollPane is instantiated using the JList listSCroller as parameter       
	
		TitledBorder b = new TitledBorder("CraZe")           ;
		listScroller.setBackground(Color.GRAY.darker())      ;
		listScroller.setForeground(Color.WHITE)              ;
		b.setTitleColor(Color.WHITE)                         ;
		listScroller.setBorder(b)                            ;

		/* Adding stuff to their panels */
		
		/* New Image's dialog */
		dimenPan.add(width)    ;
		dimenPan.add(widthVal) ;
		dimenPan.add(height)   ;
		dimenPan.add(heightVal);

		dialgPan.add(dialogRedSli);
		dialgPan.add(dialogGreSli);
		dialgPan.add(dialogBluSli);
		dialgPan.add(backrCol)    ;
		dialgPan.add(dimenPan)    ;

		/*Main Window*/
		
		this.add(listScroller);
		
		altAnPan.add(ang)     ;
		altAnPan.add(angle)   ;
		stylePan.add(altAnPan);
		spinnPan.add(rots)    ;
		spinnPan.add(spin)    ;
		stylePan.add(spinnPan);
		bSizePan.add(size)    ; 
		bSizePan.add(recS)    ;
		stylePan.add(bSizePan);
		stylePan.add(sym)     ;
		stylePan.add(var)     ;
		stylePan.add(rot)     ;
		stylePan.add(bor)     ; 
		stylePan.add(con)     ;
		stylePan.add(col)     ;
		
		this.add(stylePan);
		
		colorPan.add(redSli)  ;
		colorPan.add(greSli)  ;
		colorPan.add(bluSli)  ;
		colorPan.add(brushCol);
		
		undRePan.add(undoZ)   ;
		undRePan.add(redoZ)   ;
		buttnPan.add(undRePan);
		buttnPan.add(savePNG) ;
		buttnPan.add(newImg)  ;
		        
		craZePan.add(colorPan);
		craZePan.add(buttnPan);
        
		this.add(craZePan);
		
		curWCol = Color.black; //The default background color is black.
	}
	
	public void valueChanged(ListSelectionEvent e) {
		// This method fires up once a element in the list is selected
		
	    if (e.getValueIsAdjusting() == false){
	    	craze.pangra.repaint();
	    	if(list.getSelectedIndex() == 0){
	        	craze.pangra.state = "SPIRAL"     ; // This does nothing but clearing the screen. I am still planing how to implement a certain particular brush
	        } 
	        if(list.getSelectedIndex() == 1){
	        	craze.pangra.state = "LINE"       ; // Line Drawer 
	        }
	        if(list.getSelectedIndex() == 2){
	        	craze.pangra.state = "LINCEN"     ; // Lines from Start
	        } 
	        if(list.getSelectedIndex() == 3){ 
	        	craze.pangra.state = "VERTICAL"   ; // Vertical Line
	        }
	        if(list.getSelectedIndex() == 4){
	        	craze.pangra.state = "HORIZONTAL" ; // Horizontal Line
	        }
	        if(list.getSelectedIndex() == 5){
	        	craze.pangra.state = "CROSS"      ; // Great Cross
	        }
	        if(list.getSelectedIndex() == 6){
	        	craze.pangra.state = "TRIANGLE"   ; // Triangle
	        }
	        if(list.getSelectedIndex() == 7){
	        	craze.pangra.state = "TRIANGLEROT"; // Triangle from Start
	        }
	        if(list.getSelectedIndex() == 8){
	        	craze.pangra.state = "SQUARE"     ; // Square
	        }
	        if(list.getSelectedIndex() == 9){
	        	craze.pangra.state = "SQUAREROT"  ; // Square from Start
	        }
	        if(list.getSelectedIndex() == 10){
	        	craze.pangra.state = "SQUARTILT"  ; // Diamond
	        }
	        if(list.getSelectedIndex() == 11){
	        	craze.pangra.state = "CIRCLE"     ; // Circle
	        }
	        if(list.getSelectedIndex() == 12){
	        	craze.pangra.state = "FREESTYLE"  ; // Free-Style Fill
	        }
	        if(list.getSelectedIndex() == 13){ 
	        	craze.pangra.state = "BUCKET"     ; // Bucket Fill
	        }
	        if(list.getSelectedIndex() == 14){
	        	craze.pangra.state = "CENTER"     ; // Change Center         
	        	craze.pangra.repaint()            ;
	        }
	        if(list.getSelectedIndex() == 15){
	        	craze.pangra.centX.clear()        ; // Reset Center
	        	craze.pangra.centY.clear()        ;
	        	craze.pangra.centX.add(480)       ;
	        	craze.pangra.centY.add(360)       ;
	        	craze.pangra.mosX = 480           ;
	        	craze.pangra.mosY = 360           ;
	        }
	        if(list.getSelectedIndex() == 16){
	        	craze.pangra.state = "DRAG"       ; // Drag Image Around
	        }
	        if(list.getSelectedIndex() == 17){
	        	craze.pangra.resetPosition()      ; // Reset Image Position
	        }
	    }
	}
	
	public void actionPerformed(ActionEvent arg0) {	
		if(arg0.getActionCommand()=="SAVE"){
			craze.pangra.saveIMG();
		}
		if(arg0.getActionCommand()=="ERASE"){
	    	JOptionPane.showMessageDialog(this, dialgPan)                      ; // the New Image's dialog is displayed, using dialgPan as a object to display
	    	craze.pangra.currWCol = curWCol                                    ; // the new background color is set              
	    	if(widthVal.getText().length()<1 || heightVal.getText().length()<1){ // if the text fields were left empty, it will set a default value of 960x960
		    	craze.pangra.horizontal = 960                                  ;
		    	craze.pangra.vertical   = 960                                  ;
	    	}
	    	else{
		    	craze.pangra.horizontal = Integer.parseInt(widthVal.getText()) ;
		    	craze.pangra.vertical   = Integer.parseInt(heightVal.getText());
	    	}
			craze.pangra.eraser(); //Old image is now gone
		} 
		
		if(arg0.getActionCommand()=="SYMMETRY"){
			craze.pangra.symme = sym.isSelected();
		}
		
		if(arg0.getActionCommand()=="ROTATE"){
			craze.pangra.rotar = rot.isSelected();
		}
		
		if(arg0.getActionCommand()=="VARSIZE"){
			craze.pangra.varia = var.isSelected();
		}
		
		if(arg0.getActionCommand()=="BORDERS"){
			craze.pangra.bordr = bor.isSelected();
		}
		if(arg0.getActionCommand()=="COLORS"){
			craze.pangra.color = col.isSelected();
			if(col.isSelected()==true){
				craze.pangra.tempBCol = craze.pangra.currBCol;
			}
			if(col.isSelected()==false){
				craze.pangra.currBCol = craze.pangra.tempBCol;
			}
		}
		if(arg0.getActionCommand()=="CONNECT"){
			craze.pangra.conne = con.isSelected();
		}
		if(arg0.getActionCommand()=="ANGLE"){
			angle.setEnabled(ang.isSelected());
		}
		if(arg0.getActionCommand()=="UNDO"){
			craze.pangra.undoRedo("undo");
		}	
		if(arg0.getActionCommand()=="REDO"){
			craze.pangra.undoRedo("redo");
		}	
	}

	public void stateChanged(ChangeEvent arg0) {
	    curBCol = new Color(redSli.getValue(),greSli.getValue(),bluSli.getValue())                  ; // new Color(int R, int G, int B) is used to create a new color	
	    curWCol = new Color(dialogRedSli.getValue(),dialogGreSli.getValue(),dialogBluSli.getValue());
	     
	    /*Spinner Reactions*/
	    if(arg0.getSource()==recS){                        // the spinner for the Brush Size value was altered
	    	if((Integer) recS.getValue()<1){
	    		recS.setValue(1);
	    	}
	    	craze.pangra.recS = (Integer) recS.getValue();
	    }
	    if(arg0.getSource()==spin){                        // the spinner for the Spins value was altered
	    	if((Integer) spin.getValue()<1){
	    		spin.setValue(1);
	    	}
	    	craze.pangra.value = (Integer)spin.getValue();
	    	angle.setValue(360/craze.pangra.value)       ;// the default rotation angle is 360/(number of spins)
	    	craze.pangra.alt = false                     ;
	    }
	    
	    
	    if(arg0.getSource()==angle){
	    	craze.pangra.alt = true;
	    }
	    
	    /*Slider Reactions*/
	    //Always that a Slider is modified, the border's title is changed to reflect the value, and the resulting color is updated
	    
	    if(arg0.getSource()==redSli){
	    	redBor = new TitledBorder(new LineBorder(Color.RED,2),Integer.toString(redSli.getValue()));
	    	redBor.setTitleColor(Color.WHITE)                     ;
	    	redSli.setBorder(redBor)                              ;
	    	brushCol.setBackground(curBCol)                       ;
			craze.pangra.updata(curBCol,(Integer) spin.getValue());
		}
	    if(arg0.getSource()==bluSli){
	    	bluBor = new TitledBorder(new LineBorder(Color.BLUE,2),Integer.toString(bluSli.getValue()));
	    	bluBor.setTitleColor(Color.WHITE)                     ;
	    	bluSli.setBorder(bluBor)                              ;
	    	brushCol.setBackground(curBCol)                       ;
	    	craze.pangra.updata(curBCol,(Integer) spin.getValue());
	    }
	    if(arg0.getSource()==greSli){
	    	greBor = new TitledBorder(new LineBorder(Color.GREEN,2),Integer.toString(greSli.getValue())); 
	    	greBor.setTitleColor(Color.WHITE)                     ;
	    	greSli.setBorder(greBor)                              ;
	    	brushCol.setBackground(curBCol)                       ;
	    	craze.pangra.updata(curBCol,(Integer) spin.getValue());
		}
	    if(arg0.getSource()==dialogRedSli){
	    	dialogRedBor = new TitledBorder(new LineBorder(Color.RED,2),Integer.toString(dialogRedSli.getValue()));
	    	dialogRedBor.setTitleColor(Color.BLACK);
	    	dialogRedSli.setBorder(dialogRedBor)   ;
	    	backrCol.setBackground(curWCol)        ;	
	    }	
	    if(arg0.getSource()==dialogBluSli){
	    	dialogBluBor = new TitledBorder(new LineBorder(Color.BLUE,2),Integer.toString(dialogBluSli.getValue()));
	    	dialogBluBor.setTitleColor(Color.BLACK);
	    	dialogBluSli.setBorder(dialogBluBor)   ;
	    	backrCol.setBackground(curWCol)        ;
	    }
	    if(arg0.getSource()==dialogGreSli){
	    	dialogGreBor = new TitledBorder(new LineBorder(Color.GREEN,2),Integer.toString(dialogGreSli.getValue())); 
	    	dialogGreBor.setTitleColor(Color.BLACK);
	    	dialogGreSli.setBorder(dialogGreBor)   ;
	    	backrCol.setBackground(curWCol)        ;
	    }
	}
}             
