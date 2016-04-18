package userInterface;

import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.FlowLayout;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;

import game.Main;
import game.Ship;
import game.Square;
import GameState.GameState;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;

public class GameSetupUIState extends UI {
	
	public JPanel panel;
	public SquareLabel [][] boardLabel;
	//Ship setting
	public SquareLabel [] highlighting;
	public boolean shipPlacingEnabled;
	public String shipPlacingDirection;
	public int shipNumber;

	public GameSetupUIState(Main main) {
		super(main);
		stateString = GameState.GAME_SETUP_STATE;
		shipPlacingEnabled = false;
		shipPlacingDirection = "down"; //SHIPDIRECTION
		shipNumber = 0;
		initialize();
	}
	
	private void initialize() {
		
		ImageIcon bgIcon = createImageIcon("bg.png",1024, 768);
		Image img = bgIcon.getImage();
		
		panel = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g.create();
				g2d.drawImage(img, 0, 0, 1024, 768, this);
				
			}
		};
		panel.setLayout(new BorderLayout(0, 0));
		panel.setPreferredSize(new Dimension(1024,768));
		
		//Top panel
		JPanel top = new JPanel();
		top.setPreferredSize(new Dimension(1024, 150));
		top.setLayout(new BorderLayout(0, 0));
		top.setOpaque(false);
		panel.add(top, BorderLayout.NORTH);
		
		JPanel leftTop = new JPanel();
		JPanel rightTop = new JPanel();
		leftTop.setPreferredSize(new Dimension(150,100));
		leftTop.setOpaque(false);
		rightTop.setPreferredSize(new Dimension(150,100));
		rightTop.setOpaque(false);
		
		//Top logo
		JButton logo = new JButton("");
		logo.setIcon ( new ImageIcon ( "logo.png" ) );
		top.add(leftTop,BorderLayout.WEST);
		top.add(logo, BorderLayout.CENTER);
		top.add(rightTop,BorderLayout.EAST);
		
		/*LEFT BORDER*/
		JPanel west = new JPanel();
		west.setPreferredSize(new Dimension(150,568));
		panel.add(west, BorderLayout.WEST);
		west.setLayout(new BorderLayout(0, 0));
		west.setOpaque(false);
		
		
		/*CENTER*/
		JPanel center = new JPanel();
		center.setPreferredSize(new Dimension(724,568));
		panel.add(center, BorderLayout.CENTER);
		center.setLayout(new BorderLayout(0, 0));
		center.setOpaque(false);
		
		/*PLAYER1 TABLE*/
		JPanel leftCol = new JPanel();
		leftCol.setPreferredSize(new Dimension(300,568));
		leftCol.setOpaque(false);
		center.add(leftCol, BorderLayout.WEST);
		
		JPanel player1 = new JPanel();
		player1.setBackground(Color.PINK); // bg of battle table1
		player1.setPreferredSize(new Dimension(300,300));
		
		JPanel topP1 = new JPanel(); //Panel for label "Place your ships!"
		topP1.setPreferredSize(new Dimension(300, 100));
		JLabel lblPlaceYourShip = new JLabel("PLACE YOUR SHIPS!");
		lblPlaceYourShip.setForeground(Color.WHITE);
		lblPlaceYourShip.setVerticalAlignment(SwingConstants.BOTTOM);
		lblPlaceYourShip.setFont(new Font("Avenir", Font.BOLD, 20));
		lblPlaceYourShip.setHorizontalAlignment(SwingConstants.LEFT);
		topP1.add(lblPlaceYourShip);
		topP1.setOpaque(false);
		
		
		JPanel bottomP1 = new JPanel(); //gap bottom of battle table1
		bottomP1.setPreferredSize(new Dimension(300,100));
		bottomP1.setOpaque(false);
		
		leftCol.setLayout(new BorderLayout(0,0));
		leftCol.add(topP1,BorderLayout.NORTH);
		topP1.setLayout(new GridLayout(1, 0, 0, 0));
		leftCol.add(player1, BorderLayout.CENTER);
		leftCol.add(bottomP1 , BorderLayout.SOUTH);
		GridLayout tableLayout = new GridLayout(8,8);
		player1.setLayout(tableLayout);
		//Create JLabel for each square
		
		boardLabel = new SquareLabel [8][8];
		for(int y=0; y<8; y++) {
			for(int x=0; x<8; x++) {
				SquareLabel squareLabel = new SquareLabel("", this.main);
				squareLabel.setName(y + "," + x);
				squareLabel.setIndex();
				squareLabel.setSquare();
				squareLabel.setHorizontalAlignment(SwingConstants.CENTER);
				squareLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				
				squareLabel.addMouseListener(new MouseAdapter() {
					//Mouse clicked
					@Override
					public void mouseClicked(MouseEvent e) {
						if(isShipPlacingEnabled()) {
							if(e.getButton() == MouseEvent.BUTTON3) { //If it is a right button click
								if(shipPlacingDirection.equals("down")) {
									shipPlacingDirection = "right";
									//Re-invoke mouse exited and entered on e
									mouseExited(e);
									mouseEntered(e);
								} else {
									shipPlacingDirection = "down";
									mouseExited(e);
									mouseEntered(e);
								}
							} else { //If it is a left click
								if(highlighting == null) return; //If highlighting not exist, do nothing
								//Check if any of the label in highlighting is occupied
								if(main.client.boardGame.checkOccupation(highlighting)) { //If one of them already occupied, do nothing
									return;
								}
								//Create a ship on those squares
								Ship ship = new Ship(shipNumber);
								//Set ship on board game
								boolean success = main.client.boardGame.setShip(ship, shipNumber, highlighting);
								
								if(success) { //Success -> set ship graphically
									int i = 1;
									for(SquareLabel label: highlighting) {
										//TODO set ship icon on the board game
										//label.setText(shipNumber + "");
										//label.setIcon(new ImageIcon("ship1.png"));
										if(shipPlacingDirection.equals("right")){
											label.setIcon(new ImageIcon("horizontal/ship"+(shipNumber+1)+""+(i++)+".png"));
											
										}
										//label.setIcon(new ImageIcon("ship"+(shipNumber+1)+".png")); //PLACESHIP
									}
									//Re-invoke mouse exited on e
									mouseExited(e);
									setShipPlacingEnabled(false);
								}
							}
						}
						//Else do nothing
					}
					
					//Mouse entered a JLabel
					@Override
					public void mouseEntered(MouseEvent e) {
						SquareLabel squareLabel = (SquareLabel) e.getSource();
						if(isShipPlacingEnabled()) { //If placing mode is enabled
							//Search for eligible label to hightlight
							//Check if any of the label in highlighting is occupied
							highlighting = searchForHighlightableLabel(squareLabel);
							if(highlighting == null) return; //If highlighting not exist, do nothing
							for(SquareLabel label : highlighting) {
								label.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
							}
							/*
							if(main.client.boardGame.checkOccupation(highlighting)) { //If one of them already occupied, do nothing
								return;
							}
							*/
							//Else do highlighting
							for(SquareLabel label : highlighting) {
								label.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
								
							}
						}
					}
					
					//Mouse left a JLabel
					@Override
					public void mouseExited(MouseEvent e) {
						if(isShipPlacingEnabled()) { //If placing mode is enabled
							//Remove highlight from highlighted labels
							if(highlighting == null) return; //If highlighting not exist, do nothing
							for(SquareLabel label : highlighting) {
								label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
							}
						}
					}
				});
				boardLabel[y][x] = squareLabel;
				player1.add(squareLabel);
			}
		}
		
		/*CENTER GAP*/
		JPanel centerCol = new JPanel();
		centerCol.setPreferredSize(new Dimension(124,300));
		center.add(centerCol, BorderLayout.CENTER);
		centerCol.setOpaque(false);
		
		
		/*PLAYER2 TABLE*/
		JPanel rightCol = new JPanel();
		rightCol.setPreferredSize(new Dimension(300,568));
		rightCol.setOpaque(false);
		center.add(rightCol, BorderLayout.EAST);
	
		
		JPanel statusPanel = new JPanel();
		statusPanel.setPreferredSize(new Dimension(300,50));
		statusPanel.setLayout( new BorderLayout(0,0));
		statusPanel.setOpaque(false);
		JPanel topP2 = new JPanel();
		topP2.setOpaque(false);
	
		
		JPanel bottomP2 = new JPanel();
		bottomP2.setPreferredSize(new Dimension(300,100));	
		bottomP2.setOpaque(false);
		
		JPanel player2 = new JPanel();
		player2.setPreferredSize(new Dimension(300,300));
		player2.setOpaque(false);
		
		rightCol.setLayout(new BorderLayout(0,0));
		rightCol.add(topP2, BorderLayout.NORTH);
		topP2.setLayout(new BorderLayout(0,0));
		
		
		JLabel status = new JLabel("STATUS:");
		status.setHorizontalAlignment(SwingConstants.RIGHT);
		status.setFont(new Font("Avenir", Font.PLAIN, 12));
		
		JPanel rightTopP2 = new JPanel();
		rightTopP2.setBorder(new LineBorder(null, 1, true));
		rightTopP2.setBackground(SystemColor.control);
		rightTopP2.setPreferredSize(new Dimension(300, 40));
		topP2.add(rightTopP2,BorderLayout.EAST);
		
		JPanel gap2 = new JPanel();
		gap2.setPreferredSize(new Dimension(220,10));
		gap2.setOpaque(false);
		topP2.add(gap2, BorderLayout.SOUTH);
		
		rightTopP2.setLayout(new GridLayout(1, 5, 0, 0));
		JLabel p1 = new JLabel ("YOU");
		p1.setHorizontalAlignment(SwingConstants.CENTER);
		p1.setFont(new Font("Avenir", Font.PLAIN, 10));
		JButton b1 = new JButton("READY");
		b1.setFont(new Font("Lucida Grande", Font.PLAIN, 9));
		JLabel p2 = new JLabel ("ENEMY");
		p2.setHorizontalAlignment(SwingConstants.CENTER);
		p2.setFont(new Font("Avenir", Font.PLAIN, 10));
		JButton b2 = new JButton("READY");
		b2.setFont(new Font("Lucida Grande", Font.PLAIN, 9));
		rightTopP2.add(status);
		rightTopP2.add(p1);
		rightTopP2.add(b1);
		rightTopP2.add(p2);
		rightTopP2.add(b2);
		
		rightCol.add(player2,BorderLayout.CENTER);
		rightCol.add(bottomP2, BorderLayout.SOUTH);
		
		player2.setLayout(new BorderLayout(0, 0));
		JPanel northPlayer2 = new JPanel();
		northPlayer2.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		northPlayer2.setPreferredSize(new Dimension(300,130));
		player2.add(northPlayer2, BorderLayout.NORTH);
		northPlayer2.setLayout(new BorderLayout(0, 0));
		northPlayer2.setOpaque(false);
		
		/*PLAYER PANEL*/
	
		

		JPanel namePanel = new JPanel();
		namePanel.setPreferredSize(new Dimension(190, 30));
		namePanel.setLayout(new BorderLayout(0, 0));
		JLabel name = new JLabel("PLAYER1");
		name.setFont(new Font("Avenir", Font.PLAIN, 13));
		name.setHorizontalAlignment(SwingConstants.CENTER);
		namePanel.add(name);
		namePanel.setBackground(Color.GRAY);
		northPlayer2.add(namePanel, BorderLayout.NORTH);
		
		
		JPanel playerPanel = new JPanel();
		playerPanel.setPreferredSize(new Dimension(300, 100)); 
		northPlayer2.add(playerPanel, BorderLayout.SOUTH);
		playerPanel.setLayout(new BorderLayout(0, 0));
		
		
		JButton profile = new JButton("");
		profile.setIcon(new ImageIcon("avatarr.png"));
		profile.setBackground(Color.GRAY);
		profile.setPreferredSize(new Dimension(80, 100));
		playerPanel.add(profile, BorderLayout.WEST);
		
		
		JPanel gapCol=new JPanel();
		gapCol.setPreferredSize(new Dimension(10,100));
		playerPanel.add(gapCol, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension(190,100));
		playerPanel.add(buttonPanel, BorderLayout.EAST);
		buttonPanel.setLayout(new BorderLayout(0, 0));
		
		
		JPanel keyButton = new JPanel();
		keyButton.setPreferredSize(new Dimension(190,69));
		buttonPanel.add(keyButton,BorderLayout.CENTER);
		keyButton.setLayout(new BorderLayout(0,0));
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setBackground(new Color(255, 0, 0));
		cancelButton.setFont(new Font("Avenir", Font.PLAIN, 13));
		cancelButton.setPreferredSize(new Dimension(95, 60));
		keyButton.add(cancelButton, BorderLayout.WEST);
		
		JButton readyButton = new JButton("Ready");
		readyButton.setBackground(new Color(153, 204, 0));
		readyButton.setFont(new Font("Avenir", Font.PLAIN, 13));
		readyButton.setPreferredSize(new Dimension(95, 60));
		readyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Check if all ship has been set
				System.out.println(main.client.boardGame.isAllShipSet());
				if(main.client.boardGame.isAllShipSet()) {
					main.client.startGame();
					
				}
			}
		});
		keyButton.add(readyButton, BorderLayout.EAST);
		
		JPanel gapName = new JPanel();
		gapName.setPreferredSize(new Dimension(190, 10));
		keyButton.add(gapName, BorderLayout.NORTH);
		
	
		
		JButton randomButton = new JButton("Random Place");
		randomButton.setFont(new Font("Avenir", Font.PLAIN, 13));
		randomButton.setPreferredSize(new Dimension(190, 30));
		buttonPanel.add(randomButton, BorderLayout.SOUTH);
		
		/*SHIP PANEL*/
		
//		logo.setIcon ( new ImageIcon ( "logo.png" ) );
		
		JPanel shipPanel = new JPanel();
		shipPanel.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));

		shipPanel.setPreferredSize(new Dimension(300,150));
		player2.add(shipPanel, BorderLayout.SOUTH);
		shipPanel.setLayout(new GridLayout(4, 0, 0, 0));
		JButton ship1 = new JButton("ship1");
		ship1.setName("ship1");
		ship1.setIcon(new ImageIcon("ship1.png"));
		ship1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton shipLabel = (JButton) e.getSource();
				//Set ship number
				shipNumber = Integer.parseInt(shipLabel.getName().substring(shipLabel.getName().length()-1)) - 1;
				//Clear ship occupation
				Ship ship = main.client.boardGame.getShip(shipNumber);
				if(ship != null) { //If there are already ship1 set, clear the occupation
					main.client.boardGame.clearOccupation(ship);
				}
				//Enable ship placing mode
				setShipPlacingEnabled(true);
			}
		});
		JButton ship2 = new JButton("ship2");
		ship2.setName("ship2");
		ship2.setIcon(new ImageIcon("ship2.png"));
		ship2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton shipLabel = (JButton) e.getSource();
				//Set ship number
				shipNumber = Integer.parseInt(shipLabel.getName().substring(shipLabel.getName().length()-1)) - 1;
				//Clear ship occupation
				Ship ship = main.client.boardGame.getShip(shipNumber);
				if(ship != null) { //If there are already ship2 set, clear the occupation
					main.client.boardGame.clearOccupation(ship);
				}
				//Enable ship placing mode
				setShipPlacingEnabled(true);
			}
		});
		JButton ship3 = new JButton("ship3");
		ship3.setName("ship3");
		ship3.setIcon(new ImageIcon("ship3.png"));
		ship3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton shipLabel = (JButton) e.getSource();
				//Set ship number
				shipNumber = Integer.parseInt(shipLabel.getName().substring(shipLabel.getName().length()-1)) - 1;
				//Clear ship occupation
				Ship ship = main.client.boardGame.getShip(shipNumber);
				if(ship != null) { //If there are already ship3 set, clear the occupation
					main.client.boardGame.clearOccupation(ship);
				}
				//Enable ship placing mode
				setShipPlacingEnabled(true);
			}
		});
		JButton ship4 = new JButton("ship4");
		ship4.setName("ship4");
		ship4.setIcon(new ImageIcon("ship4.png"));
		ship4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton shipLabel = (JButton) e.getSource();
				//Set ship number
				shipNumber = Integer.parseInt(shipLabel.getName().substring(shipLabel.getName().length()-1)) - 1;
				//Clear ship occupation
				Ship ship = main.client.boardGame.getShip(shipNumber);
				if(ship != null) { //If there are already ship4 set, clear the occupation
					main.client.boardGame.clearOccupation(ship);
				}
				//Enable ship placing mode
				setShipPlacingEnabled(true);
			}
		});

		shipPanel.add(ship1);
		shipPanel.add(ship2);
		shipPanel.add(ship3);
		shipPanel.add(ship4);
		
		JPanel toolTip = new JPanel();
		player2.add(toolTip, BorderLayout.CENTER);
		toolTip.setLayout(new BorderLayout(0, 0));
		toolTip.setOpaque(false);
		
		JLabel lblPressReady = new JLabel("Press Ready !!");
		lblPressReady.setFont(new Font("Avenir", Font.BOLD, 15));
		lblPressReady.setHorizontalAlignment(SwingConstants.CENTER);
		toolTip.add(lblPressReady);
		
		/*RIGHT BORDER*/
		JPanel east = new JPanel();
		east.setPreferredSize(new Dimension(150,300));
		panel.add(east, BorderLayout.EAST);
		east.setLayout(new BorderLayout(0, 0));
		east.setOpaque(false);
		
		
		JPanel bottom = new JPanel();
		bottom.setPreferredSize(new Dimension(1024,50));
		bottom.setOpaque(false);
		panel.add(bottom, BorderLayout.SOUTH);
	}
	
	public SquareLabel[] searchForHighlightableLabel(SquareLabel startingLabel) {
		int y = startingLabel.getYIndex();
		int x = startingLabel.getXIndex();
		//Find down/right label that don't cause IndexOutOfBoundError
		SquareLabel [] highlightable = new SquareLabel[4];
		if(boardLabel[y][x].getSquare().isOccupied()) return null; //If the square is already occupied, return null
		highlightable[0] = boardLabel[y][x]; //Add the first label into the array
		int index = 1, failedAttempt = 0;
		return checkNext(y, x, index, failedAttempt, shipPlacingDirection, highlightable);
		/*
		int failedAttempt = 0;
		if(shipPlacingDirection.equals("down")) { //If the ship placing direction is down
			if((y + 1 <= 7) && !boardLabel[y+1][x].getSquare().isOccupied()) { //If the next down square exists and is not occupied by a ship
				highlightable[1] = boardLabel[y+1][x];
			//Else trying to add the above next square
			} else if((y - ++failedAttempt >= 0) && !boardLabel[y-failedAttempt][x].getSquare().isOccupied()){ //If the next above square exists and is not occupied by a ship
				highlightable[1] = boardLabel[y-failedAttempt][x];
			} else return null; //Else return null
			if((y + 2 <= 7) && !boardLabel[y+2][x].getSquare().isOccupied()) { //If the next down square exists and is not occupied by a ship
				highlightable[2] = boardLabel[y+2][x];
			//Else trying to add the above next square
			} else if((y - ++failedAttempt >= 0) && !boardLabel[y-failedAttempt][x].getSquare().isOccupied() && (failedAttempt == 0)){ //If the next above square exists and is not occupied by a ship (and must be consecutive)
				highlightable[2] = boardLabel[y-failedAttempt][x];
			} else return null; //Else return null
			if((y + 3 <= 7) && !boardLabel[y+3][x].getSquare().isOccupied()) { //If the next down square exists and is not occupied by a ship
				highlightable[3] = boardLabel[y+3][x];
			//Else trying to add the above next square
			} else if((y - ++failedAttempt >= 0) && !boardLabel[y-failedAttempt][x].getSquare().isOccupied() && (failedAttempt == 0)){ //If the next above square exists and is not occupied by a ship (and must be consecutive(failed = 0))
				highlightable[3] = boardLabel[y-failedAttempt][x];
			} else return null; //Else return null
		
		} else { //If the ship placing direction is right
			if((x + 1 <= 7) && !boardLabel[y][x+1].getSquare().isOccupied()) { //If the next down square exists and is not occupied by a ship
				highlightable[1] = boardLabel[y][x+1];
			//Else trying to add the above next square
			} else if((x - ++failedAttempt >= 0) && !boardLabel[y][x-failedAttempt].getSquare().isOccupied()){ //If the next above square exists and is not occupied by a ship
				highlightable[1] = boardLabel[y][x-failedAttempt];
			} else return null; //Else return null
			if((x + 2 <= 7) && !boardLabel[y][x+2].getSquare().isOccupied()) { //If the next down square exists and is not occupied by a ship
				highlightable[2] = boardLabel[y][x+2];
			//Else trying to add the above next square
			} else if((x - ++failedAttempt >= 0) && !boardLabel[y][x-failedAttempt].getSquare().isOccupied()){ //If the next above square exists and is not occupied by a ship
				highlightable[2] = boardLabel[y][x-failedAttempt];
			} else return null; //Else return null
			if((x + 3 <= 7) && !boardLabel[y][x+3].getSquare().isOccupied()) { //If the next down square exists and is not occupied by a ship
				highlightable[3] = boardLabel[y][x+3];
			//Else trying to add the above next square
			} else if((x - ++failedAttempt >= 0) && !boardLabel[y][x-failedAttempt].getSquare().isOccupied()){ //If the next above square exists and is not occupied by a ship
				highlightable[3] = boardLabel[y][x-failedAttempt];
			} else return null; //Else return null
		}
		*/
	}
	
	public SquareLabel[] checkNext(int y, int x, int index, int failedAttempt, String direction, SquareLabel[] highlightable) {
		//Check to continue;
		if(index <= 3) {
			if(direction.equals("down")) { //If down direction
				//Check if the next square exists
				if(y + index <= 7) { //If exists
					//Check occupancy
					if(!boardLabel[y+index][x].getSquare().isOccupied()) { //If not occupied
						//Add the label to highlightable
						highlightable[index] = boardLabel[y+index++][x];
						highlightable = checkNext(y, x, index, failedAttempt, direction, highlightable);
					} else { //If occupied
						//Check previous square occupancy
						highlightable = checkPrevious(y, x, index, ++failedAttempt, direction, highlightable);
					}
				} else { //If not exist
					highlightable = checkPrevious(y, x, index, ++failedAttempt, direction, highlightable);
				}
			} else { //If right direction
				//Check if the next square exists
				if(x + index <= 7) { //If exists
					//Check occupancy
					if(!boardLabel[y][x+index].getSquare().isOccupied()) { //If not occupied
						//Add the label to highlightable
						highlightable[index] = boardLabel[y][x+index++];
						highlightable = checkNext(y, x, index, failedAttempt, direction, highlightable);
					} else { //If occupied
						//Check upper square occupancy
						highlightable = checkPrevious(y, x, index, ++failedAttempt, direction, highlightable);
					}
				} else { //If not exist, check for previous square
					highlightable = checkPrevious(y, x, index, ++failedAttempt, direction, highlightable);
				}
			}
		}
		//Done
		return highlightable;
	}
	
	public SquareLabel[] checkPrevious(int y, int x, int index, int failedAttempt, String direction, SquareLabel[] highlightable) {
		//Check to continue;
		if(index <= 3) {
			if(direction.equals("down")) { //If down direction
				//Check if the next square exists
				if(y - failedAttempt >= 0) { //If exists
					//Check occupancy
					if(!boardLabel[y-failedAttempt][x].getSquare().isOccupied()) { //If not occupied
						//Add the label to highlightable
						highlightable[index++] = boardLabel[y-failedAttempt++][x];
						highlightable = checkPrevious(y, x, index, failedAttempt, direction, highlightable);
					} else { //If occupied, return null
						return null;
					}
				} else { //If not exsit, return null
					return null;
				}
			} else { //If right direction
				//Check if the next square exists
				if(x - failedAttempt >= 0) { //If exists
					//Check occupancy
					if(!boardLabel[y][x-failedAttempt].getSquare().isOccupied()) { //If not occupied
						//Add the label to highlightable
						highlightable[index++] = boardLabel[y][x-failedAttempt++];
						highlightable = checkPrevious(y, x, index, failedAttempt, direction, highlightable);
					} else { //If occupied, return null
						return null;
					}
				} else { //If not exsit, return null
					return null;
				}
			}
		}
		//Done
		return highlightable;
	}
	
	public void setShipPlacingEnabled(boolean setting) {
		shipPlacingEnabled = setting;
	}
	
	public boolean isShipPlacingEnabled() {
		return shipPlacingEnabled;
	}
	
	public static ImageIcon createImageIcon(String path, int width, int height) {
		Image img = null;
		try {
			img = ImageIO.read(new File(path));
		} catch (IOException e) {
		}
		Image resizedImage = img.getScaledInstance(width, height, 0);
		return new ImageIcon(resizedImage);
	}

	@Override
	public void entered() {
		System.out.println(Thread.currentThread().getName() + ": entered " + stateString);
		main.replaceCurrentPanel(panel);
		JOptionPane.showMessageDialog(main, "Welcome, Alice!");
	}

	@Override
	public void leaving() {
		System.out.println(Thread.currentThread().getName() + ": leaving " + stateString);
		// TODO Auto-generated method stub
		
	}


	@Override
	public void obscuring() {
		System.out.println(Thread.currentThread().getName() + ": " + stateString + " being stacked");
		main.setEnabled(false);
	}


	@Override
	public void revealed() {
		System.out.println(Thread.currentThread().getName() + ": " + stateString + " resumed");
		main.setEnabled(true);
	}
	
}