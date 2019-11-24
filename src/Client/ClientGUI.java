package Client;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.ozten.font.JFontChooser;

import Client.Shape;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Vector;

public class ClientGUI extends Thread implements ActionListener, MouseListener, MouseMotionListener, ChangeListener {
	Client client;
	Shape shape;

	JFrame frame;
	JMenuBar menuBar;
	JMenu FileMenue, mnFont, mnSize, mnColor;
	JMenuItem NewItem, OpenItem, SaveItem, SaveAsItem, mntmClear, CloseItem, mntnChooseColor, mntnChooseFont;

	JButton btnFree, btnStraightLine, btnCircle, btnRectangle, btnOval, btnInputText, btnEraser, btnSendMsg, btnRemove;
	
	JSlider slider;
	JPanel drawPanel;
	JTextArea textArea, chatArea, chatScreen;
	JScrollPane scrPaneChatScreen, scrollPane, clScrollPane;
	
	JList<String> clientList;
	
	ImageIcon freeLineIcon, straightLineIcon, circleIcon, rectangleIcon, ovalIcon, textIcon, eraseIcon;
	
	BufferedImage bi;
	Graphics2D graphics, sharedGraphics, backgroundGraphics;
	int startX, startY, endX, endY, brushSize;
	Color color, eColor;
	String drawOption, inputText="";
	Font font;
	
	URL flURL, slURL, cirURL, recURL, ovalURL, textURL, eraURL;

	
	public ClientGUI(Client client) throws HeadlessException, IOException {
		this.client = client;
		
		iconSetting();
		initialize();

		btnRemove.addActionListener(this);
			
		NewItem.addActionListener(this);
		OpenItem.addActionListener(this);
		SaveItem.addActionListener(this);
		SaveAsItem.addActionListener(this);
		mntmClear.addActionListener(this);
		CloseItem.addActionListener(this);

		btnFree.addActionListener(this);
		btnStraightLine.addActionListener(this);
		btnCircle.addActionListener(this);
		btnOval.addActionListener(this);
		btnRectangle.addActionListener(this);
		btnInputText.addActionListener(this);
		btnEraser.addActionListener(this);

		drawPanel.addMouseListener(this);
		drawPanel.addMouseMotionListener(this);
		
		slider.addChangeListener(this);
		
		btnSendMsg.addActionListener(this);
		
		mntnChooseColor.addActionListener(this);
		mntnChooseFont.addActionListener(this);
	}
	
	private void iconSetting() {
		
		flURL = this.getClass().getClassLoader().getResource("freelineIcon.png");
		slURL = this.getClass().getClassLoader().getResource("straightlineIcon.png");
		cirURL = this.getClass().getClassLoader().getResource("circleIcon.png");
		recURL = this.getClass().getClassLoader().getResource("rectangleIcon.png");
		ovalURL = this.getClass().getClassLoader().getResource("circleIcon.png");
		textURL = this.getClass().getClassLoader().getResource("textIcon.png");
		eraURL = this.getClass().getClassLoader().getResource("eraserIcon.png");
		
		freeLineIcon = new ImageIcon(new ImageIcon(flURL).getImage().getScaledInstance(60, 40, Image.SCALE_SMOOTH));
		straightLineIcon = new ImageIcon(new ImageIcon(slURL).getImage().getScaledInstance(60, 40, Image.SCALE_SMOOTH));
		circleIcon = new ImageIcon(new ImageIcon(cirURL).getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH));
		rectangleIcon = new ImageIcon(new ImageIcon(recURL).getImage().getScaledInstance(60, 40, Image.SCALE_SMOOTH));
		ovalIcon = new ImageIcon(new ImageIcon(ovalURL).getImage().getScaledInstance(60, 30, Image.SCALE_SMOOTH));
		textIcon = new ImageIcon(new ImageIcon(textURL).getImage().getScaledInstance(60, 40, Image.SCALE_SMOOTH));
		eraseIcon = new ImageIcon(new ImageIcon(eraURL).getImage().getScaledInstance(60, 40, Image.SCALE_SMOOTH));
		
	}
	
	private void initialize() throws HeadlessException, IOException {
		frame = new JFrame(client.remoteServer.getBoardName() + " - " + client.userType);
		frame.setBounds(100, 100, 821, 699);
		frame.setResizable(false);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				try {
					clientWindowClose();
				} catch (NotBoundException e) {
					e.printStackTrace();
				}
			}
		});
		
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		FileMenue = new JMenu("File");
		menuBar.add(FileMenue);

		NewItem = new JMenuItem("New");
		FileMenue.add(NewItem);

		OpenItem = new JMenuItem("Open");
		FileMenue.add(OpenItem);

		SaveItem = new JMenuItem("Save ");
		FileMenue.add(SaveItem);

		SaveAsItem = new JMenuItem("Save As");
		FileMenue.add(SaveAsItem);

		mntmClear = new JMenuItem("Clear ");
		FileMenue.add(mntmClear);

		CloseItem = new JMenuItem("Close");
		FileMenue.add(CloseItem);

		mnFont = new JMenu("Font");
		menuBar.add(mnFont);
		
		mntnChooseFont= new JMenuItem("Select Font");
		mnFont.add(mntnChooseFont);

		mnSize = new JMenu("Size");
		menuBar.add(mnSize);

		slider = new JSlider();
		slider.setOrientation(SwingConstants.VERTICAL);
		slider.setMajorTickSpacing(10);
		slider.setMinorTickSpacing(2);
		slider.setSnapToTicks(true);
		slider.setPaintTicks(true);
		slider.setBounds(0, 0, 100, 50);
		slider.setToolTipText(Long.toString(slider.getValue()));
		mnSize.add(slider);

		mnColor = new JMenu("Color");
		menuBar.add(mnColor);

		mntnChooseColor = new JMenuItem("Choose Color");
		mnColor.add(mntnChooseColor);

		frame.getContentPane().setLayout(null);

		drawPanel = new JPanel();
		drawPanel.setBackground(Color.WHITE);
		drawPanel.setBounds(10, 10, 595, 573);
		frame.getContentPane().add(drawPanel);

		btnFree = new JButton();
		btnFree.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		btnFree.setBounds(6, 588, 87, 61);
		btnFree.setIcon(freeLineIcon);
		frame.getContentPane().add(btnFree);

		btnStraightLine = new JButton();
		btnStraightLine.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		btnStraightLine.setBounds(91, 588, 87, 61);
		btnStraightLine.setIcon(straightLineIcon);
		frame.getContentPane().add(btnStraightLine);

		btnCircle = new JButton();
		btnCircle.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		btnCircle.setBounds(177, 588, 87, 61);
		btnCircle.setIcon(circleIcon);
		frame.getContentPane().add(btnCircle);

		btnRectangle = new JButton();
		btnRectangle.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		btnRectangle.setBounds(264, 588, 87, 61);
		btnRectangle.setIcon(rectangleIcon);
		frame.getContentPane().add(btnRectangle);

		btnOval = new JButton();
		btnOval.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		btnOval.setBounds(351, 588, 87, 61);
		btnOval.setIcon(ovalIcon);
		frame.getContentPane().add(btnOval);

		btnInputText = new JButton();
		btnInputText.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		btnInputText.setBounds(437, 588, 87, 61);
		btnInputText.setIcon(textIcon);
		frame.getContentPane().add(btnInputText);

		btnEraser = new JButton();
		btnEraser.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		btnEraser.setBounds(523, 588, 87, 61);
		btnEraser.setIcon(eraseIcon);
		frame.getContentPane().add(btnEraser);

		btnSendMsg = new JButton("Send");
		btnSendMsg.setBounds(770, 588, 42, 62);
		frame.getContentPane().add(btnSendMsg);

		textArea = new JTextArea();
		textArea.setFont(new Font("Arial", Font.PLAIN, 10));
		textArea.setLineWrap(true);

		scrPaneChatScreen = new JScrollPane(textArea);
		scrPaneChatScreen.setBounds(619, 590, 150, 58);
		frame.getContentPane().add(scrPaneChatScreen);

		JLabel lblNewLabel = new JLabel("Active Clients");
		lblNewLabel.setBounds(620, 10, 116, 16);
		frame.getContentPane().add(lblNewLabel);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(618, 200, 193, 373);
		frame.getContentPane().add(scrollPane);

		clScrollPane = new JScrollPane();
		clScrollPane.setBounds(618, 30, 193, 142);
		frame.getContentPane().add(clScrollPane);
		
		clientList = new JList<String>();
		clientList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		clScrollPane.setViewportView(clientList);
		

		if(client.userType.equals("Manager")) {
			btnRemove = new JButton("Remove");	
		}
		else {
			btnRemove = new JButton("Sync Whiteboard");
		}
		
		
		btnRemove.setBounds(628, 172, 172, 29);
		frame.getContentPane().add(btnRemove);

		chatArea = new JTextArea();
		chatArea.setFont(new Font("Arial", Font.PLAIN, 11));
		chatArea.setLineWrap(true);
		chatArea.setEditable(false);
		scrollPane.setViewportView(chatArea);

		color = new Color(0, 0, 0);
		eColor = new Color(255, 255, 255);
		
		font = new Font("Lucida Grande", Font.PLAIN, 12);
		
		
		frame.setVisible(true);
		
		
		graphics = (Graphics2D) drawPanel.getGraphics();
		sharedGraphics = (Graphics2D) drawPanel.getGraphics();

		bi = new BufferedImage(drawPanel.getWidth(), drawPanel.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		
		graphics.setBackground(Color.WHITE);
		sharedGraphics.setBackground(Color.white);
		
		backgroundGraphics = bi.createGraphics();
		backgroundGraphics.setColor(Color.WHITE);
		backgroundGraphics.fillRect(0, 0, drawPanel.getWidth(), drawPanel.getHeight());
		backgroundGraphics.setColor(Color.BLACK);
		backgroundGraphics.setBackground(Color.WHITE);
		
		shape = new Shape();
		
		brushSize = 3;
	}

	public void shutDown(String action){
		if(client.userType.equals("User")) {
		    if(action.equals("Quit")){
	            JOptionPane.showMessageDialog(frame, "The Host has ended the session.\nPlease restart the program.");
	        }
	        else if(action.equals("Removed")){
			    JOptionPane.showMessageDialog(frame, "The Host has removed you from the whiteboard.\nPlease restart the program.");
	        }
	        else {
	        	JOptionPane.showMessageDialog(frame, "Server is closed.");
	        }
		}
		if(client.userType.equals("Manager")) {
			JOptionPane.showMessageDialog(frame, "Server is closed.");
		}
	}

	public boolean permission(String userName) throws RemoteException{
		int response = JOptionPane.showOptionDialog(frame, userName + " would like to join the whiteboard.\nMay they join the fun?",
				"Request to join", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		if(response == JOptionPane.YES_OPTION){
			return true;
		}
		else{
			return false;
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		brushSize = slider.getValue();
		graphics.setStroke(new BasicStroke(brushSize));
	}	

	public void updateClientList(Vector<String> clientName) {
		clientList.setListData(clientName);
	}

	public void save(String type, String fileName) throws IOException {
		String filePath;
		File file;

		if(type.equals("save")) {
			filePath = client.userName+".png";
		}
		else {
			filePath = fileName+".png";
			file = new File(filePath);

			if(file.exists()) {
				JOptionPane.showMessageDialog(null, "Same file name exists", "System Message", JOptionPane.OK_OPTION);
				return;
			}
		}
		ImageIO.write(bi, "png", new File(filePath));
	}
	
	public void open(File file) throws IOException, ClassNotFoundException {
		if(file.exists()) {
			bi = ImageIO.read(file);
			if(bi != null) {
				int[][] pixels = getCurrentBoardImage(bi);
				if(pixels != null) {
					client.sendOpenedImage(pixels);
				}
				else {
					JOptionPane.showMessageDialog(null, "Image size does not fit the whiteboard\nPlease choose a different image", "System Message", JOptionPane.OK_OPTION);
				}
			}
			else {
				JOptionPane.showMessageDialog(null, "Cannot read this image file\nPlease choose a different image", "System Message", JOptionPane.OK_OPTION);
			}
		}
		else {
			JOptionPane.showMessageDialog(null, "File does not exist", "System Message", JOptionPane.OK_OPTION);
		}
	}

	public void clientWindowClose() throws NotBoundException {
		int result = JOptionPane.showConfirmDialog(null, "Do you want to close the Window?", "Conrim Message", JOptionPane.OK_CANCEL_OPTION);
		
		if(result == JOptionPane.OK_OPTION) {
			try {
				client.disconnect(client.userName, null);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}
		else {
			frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if(e.getSource() == NewItem) {
				if(client.userType.equals("Manager")) {
					client.newBoard();
				}
				else {
					JOptionPane.showMessageDialog(frame, "Only manager can create a new whiteboard");
				}
			}
			
			
			else if(e.getSource() == SaveItem) {
				save("save", "");
			}
			
			
			else if(e.getSource() == SaveAsItem) {
				String fileName = JOptionPane.showInputDialog("Enter file name");
				save("saveAs", fileName);
			}
			
			
			else if(e.getSource() == OpenItem) {
				if(client.userType.equals("Manager")) {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
						@Override
						public String getDescription() {
							return null;
						}
						
						@Override
						public boolean accept (File f) {
							String fileName = f.getName();
							if(fileName.indexOf(".png") != -1) {
								return true;
							}
							else if(fileName.indexOf(".jpg") != -1) {
								return true;
							}
							else if(fileName.indexOf(".gif") != -1) {
								return true;
							}
							else if(f.isDirectory()) {
								return true;
							}
							return false;
						}
					});
					
					int result = fileChooser.showOpenDialog(null);
					if(result == JFileChooser.APPROVE_OPTION) {
						open(fileChooser.getSelectedFile());
					}
				}
				else {
					JOptionPane.showMessageDialog(frame, "Only manager can load a whiteboard image");
				}
			}
			
			
			else if(e.getSource() == mntmClear) {
				if(client.userType.equals("Manager")) {
					client.newBoard();
				}
				else {
					graphics.clearRect(0, 0, drawPanel.getWidth(), drawPanel.getHeight());
					backgroundGraphics.clearRect(0, 0, drawPanel.getWidth(), drawPanel.getHeight());
				}
			}
			
			
			else if(e.getSource() == CloseItem) {
				clientWindowClose();
			}

			
			else if(e.getSource() == mntnChooseColor) {
				color = JColorChooser.showDialog(null, "Choose color", Color.black);
			}
			
			
			else if(e.getSource() == mntnChooseFont) {
				font = JFontChooser.showDialog(drawPanel, "Font Chooser", "Test text");
			}
			
			
			else if(e.getSource() == btnSendMsg) {
				client.sendMessage(client.userName + " : " + textArea.getText());
				textArea.setText("");
			}

			
			else if(e.getSource() == btnFree){
				drawOption = "freeLine";
			}
			else if(e.getSource() == btnStraightLine){
				drawOption = "straightLine";
			}
			else if(e.getSource() == btnCircle){
				drawOption = "circle";
			}
			else if(e.getSource() == btnRectangle){
				drawOption = "rect";
			}
			else if(e.getSource() == btnOval) {
				drawOption = "oval";
			}
			else if(e.getSource() == btnInputText) {
				drawOption = "text";
				inputText = JOptionPane.showInputDialog("Please input the text!");
			}
			else if(e.getSource() == btnEraser) {
				drawOption = "erase";
			}
			
			
			else if(e.getSource() == btnRemove) {
				if(client.userType.equals("Manager")) {
					if(clientList.getSelectedValue() != null) {
						if(!(clientList.getSelectedValue().equals(client.userName))) {
							client.disconnect(client.userName, clientList.getSelectedValue());
						}
						else {
							JOptionPane.showMessageDialog(frame, "Please choose other members\nYou cannot be removed yourself");
						}
					}
				}
				else {
					graphics.clearRect(0, 0, drawPanel.getWidth(), drawPanel.getHeight());
					backgroundGraphics.clearRect(0, 0, drawPanel.getWidth(), drawPanel.getHeight());
					drawCurrentBoard(client.remoteArt.managerBoardData());
				}
			}
		} catch (IOException | ClassNotFoundException | NotBoundException e1) {
			e1.printStackTrace();
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(drawOption !=null) {
			startX = e.getX();
			startY = e.getY();
			if(drawOption.equals("erase")) {
				graphics.setColor(eColor);
				
				try {
					shape.setShapeInfo(7, startX, startY, brushSize, brushSize, eColor, brushSize);
					client.shareShape(shape);
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(drawOption !=null) {
			try {
				graphics.setColor(color);
	
				endX = e.getX();
				endY = e.getY();
				
				if(drawOption.equals("straightLine")) {
					shape.setShapeInfo(2, startX, startY, endX, endY, color, brushSize);
				}
				
				else if(drawOption.equals("circle")) {
					int radius = Math.abs(endX - startX) > Math.abs(startY - endY) ? Math.abs(endX - startX) : Math.abs(startY - endY);
					shape.setShapeInfo(3, startX, startY, radius, radius, color, brushSize);
				}
				
				else if(drawOption.equals("rect")) {
					shape.setShapeInfo(4, startX, startY, Math.abs(endX - startX), Math.abs(startY - endY), color, brushSize);
				}
				
				else if(drawOption.equals("oval")) {
					shape.setShapeInfo(5, startX, startY, Math.abs(endX - startX), Math.abs(startY - endY), color, brushSize);
				}
				
				else if(drawOption.equals("text")) {
					graphics.setFont(font);
					drawOption=null;
					shape.setShapeInfo(6, inputText, startX, startY , endX, endY, color, font);
				}
				
				client.shareShape(shape);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(drawOption != null && (drawOption.equals("freeLine") || drawOption.equals("erase"))) {
			try {
				endX = e.getX();
				endY = e.getY();
				
				if(drawOption.equals("freeLine")) {
					graphics.setColor(color);
					shape.setShapeInfo(1, startX, startY, endX, endY, color, brushSize);
				}
				else if(drawOption.equals("erase")) {
					graphics.setColor(eColor);
					shape.setShapeInfo(7, startX, startY, brushSize, brushSize, eColor, brushSize);
				}
				client.shareShape(shape);

				startX = endX;
				startY = endY;
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(drawOption !=null) {
			startX = e.getX();
			startY = e.getY();
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mouseMoved(MouseEvent e) {}

	public void drawSharedShape(Shape shape) {
		sharedGraphics.setColor(shape.color);
		backgroundGraphics.setColor(shape.color);
		
		sharedGraphics.setStroke(new BasicStroke(shape.brushSize));
		backgroundGraphics.setStroke(new BasicStroke(shape.brushSize));
		
		// drawLine : 1(FreeLine), 2(StraightLine) 
		if(shape.type == 1 || shape.type == 2) {
			sharedGraphics.drawLine(shape.startX, shape.startY, shape.endX, shape.endY);
			backgroundGraphics.drawLine(shape.startX, shape.startY, shape.endX, shape.endY);
		}
		
		// fillOval : 3(Circle), 5(Oval), 7(Eraser)
		else if(shape.type == 3 || shape.type == 5 || shape.type == 7) {
			sharedGraphics.fillOval(shape.startX, shape.startY, shape.endX, shape.endY);
			backgroundGraphics.fillOval(shape.startX, shape.startY, shape.endX, shape.endY);
		}
		
		// fillRect : 4(Rectangle)
		else if(shape.type == 4) {
			sharedGraphics.fillRect(shape.startX, shape.startY, shape.endX, shape.endY);
			backgroundGraphics.fillRect(shape.startX, shape.startY, shape.endX, shape.endY);
		}
		
		// 6(InputText)
		else {
			sharedGraphics.setFont(shape.font);
			backgroundGraphics.setFont(shape.font);
			
			sharedGraphics.drawString(shape.inputText, shape.startX, shape.startY);
			backgroundGraphics.drawString(shape.inputText, shape.startX, shape.startY);
		}
	}

	public int[][] getCurrentBoardImage(BufferedImage image) {
		byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		int width = image.getWidth(), height = image.getHeight();
		int[][] result = new int[height][width];
		
		int pixelLength = 3;
		
		if(image.getWidth() == drawPanel.getWidth() && image.getHeight() == drawPanel.getHeight()) {
			for (int pixel = 0, row = 0, col = 0; pixel + 2 < pixels.length; pixel += pixelLength) {
	            int argb = 0;
	            argb += -16777216;
	            argb += ((int) pixels[pixel] & 0xff);
	            argb += (((int) pixels[pixel + 1] & 0xff) << 8);
	            argb += (((int) pixels[pixel + 2] & 0xff) << 16);
	            result[row][col] = argb;
	            col++;
	            
	            if (col == width) {
	               col = 0;
	               row++;
	            }
			}
			return result;
		}
		else {
			return null;
		}
   }
	
	public void drawCurrentBoard(int[][] pixels) throws IOException {
		for(int y=0; y<pixels.length; y++) {
			for(int x=0; x<pixels[y].length; x++) {
				bi.setRGB(x,  y, pixels[y][x]);
			}
		}
		graphics.drawImage(bi, 0, 0, null);
		backgroundGraphics.drawImage(bi, 0, 0, null);
	}
}