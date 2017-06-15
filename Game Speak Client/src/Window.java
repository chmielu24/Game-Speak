import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultCaret;


/**
 * class Window is responsible for application look window
 */
public class Window extends JFrame {
	private static final long serialVersionUID = 7242762776818397557L;
	
	private JPanel contentPane;
	private JTextField chatIn;
	private JTextPane chatOut;
	private JPanel channelPanel;
	private JPanel informationPanel;
	/**
	 * Create the frame.
	 */
	public Window() {
		setTitle("Game Speak");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 785, 542);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JToolBar toolBar = new JToolBar();
		contentPane.add(toolBar, BorderLayout.NORTH);
		
		JMenuBar menuBar = new JMenuBar();
		toolBar.add(menuBar);
		
		JMenu mnMenu = new JMenu("Menu");
		mnMenu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		menuBar.add(mnMenu);
		
		
		JPanel panel_0 = new JPanel();
		mnMenu.add(panel_0);
		panel_0.setLayout(new BorderLayout(0, 0));
		
		JButton btnSetName = new JButton("Set Name");
		panel_0.add(btnSetName);
		btnSetName.setToolTipText("");
		btnSetName.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String newName = GetNewName();
				ApplicationManager.Instance().SetName(newName);
			}
		});
		
		
		JPanel panel_1 = new JPanel();
		mnMenu.add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		JButton btnConnect = new JButton("Connect");
		panel_1.add(btnConnect);
		btnConnect.setToolTipText("");
		btnConnect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				ConnectionInfo c = getServerAddress();
				if(c != null)
				ApplicationManager.Instance().Connect(c);
			}
		});
		
		JPanel panel_2 = new JPanel();
		mnMenu.add(panel_2);
			panel_2.setLayout(new BorderLayout(0, 0));
		
			JButton btnDisconnect = new JButton("Disconnect");
			panel_2.add(btnDisconnect);
			btnDisconnect.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					ApplicationManager.Instance().Disconnect();

				}
			});
			
		JPanel panel_c = new JPanel();
		mnMenu.add(panel_c);
		panel_c.setLayout(new BorderLayout(0, 0));
		
		JButton btnClearChat = new JButton("Clear Chat");
		panel_c.add(btnClearChat);
		btnClearChat.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ApplicationManager.Instance().textChat.Clear();
				
			}
		});
		
		JPanel panel_3 = new JPanel();
		mnMenu.add(panel_3);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		JButton btnExit = new JButton("Exit");
		panel_3.add(btnExit);
		btnExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ApplicationManager.Instance().Exit();
				
			}
		});
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel chatPanel = new JPanel();
		panel.add(chatPanel, BorderLayout.SOUTH);
		chatPanel.setLayout(new BorderLayout(5, 2));
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new CompoundBorder(null, new LineBorder(new Color(0, 0, 0), 1, true)));
		chatPanel.add(panel_4, BorderLayout.NORTH);
		panel_4.setLayout(new BorderLayout(5, 0));
		
		chatOut = new JTextPane();
		chatOut.setEditable(false);
		//chatOut.setRows(10);
		chatOut.setPreferredSize(new Dimension(300,200));
		panel_4.add(new JScrollPane(chatOut), BorderLayout.CENTER);
		chatOut.setAutoscrolls(true);
		DefaultCaret caret = (DefaultCaret)chatOut.getCaret();
	    caret.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new CompoundBorder(null, new LineBorder(new Color(0, 0, 0), 1, true)));
		chatPanel.add(panel_5, BorderLayout.CENTER);
		panel_5.setLayout(new BorderLayout(0, 0));
		
		chatIn = new JTextField();
		chatIn.setHorizontalAlignment(SwingConstants.LEFT);
		panel_5.add(chatIn, BorderLayout.NORTH);
		chatIn.setColumns(10);
		//chatPanel.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{chatIn}));
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setContinuousLayout(true);
		panel.add(splitPane, BorderLayout.CENTER);
		splitPane.setResizeWeight(0.5);

		
		channelPanel = new JPanel();
		splitPane.setLeftComponent(channelPanel);
		channelPanel.setLayout(new BorderLayout(0, 0));
		
		informationPanel = new JPanel();
		splitPane.setRightComponent(informationPanel);
		informationPanel.setLayout(new BorderLayout(0, 0));
		
		setMinimumSize(new Dimension(400,400));
		setVisible(true);
	}
	
	/**
	 * 
	 * @return chatOut panel
	 */
	public JTextPane getchatOut(){
		return chatOut;
	}

	/**
	 * 
	 * @return chatIn field
	 */
	public JTextField getChatIn() {
		return chatIn;
	}
	
	/**
	 * 
	 * @return channel panel
	 */
	public JPanel getChannelsPanel()
	{
		return channelPanel;
	}
	
	/**
	 * 
	 * @return information panel
	 */
	public JPanel getInformationPanel()
	{
		return informationPanel;
	}
	
	/**
	 * Ask user for server address and port.
	 * If user leave Server Address empty insert 127.0.0.1
	 * @return new ConnectionInfo or null if user cancel action
	 */
	private ConnectionInfo getServerAddress() {
		ConnectionInfo Cni = new ConnectionInfo();
		
		JTextField serverAdress = new JTextField(32);
		JTextField port = new JTextField(6);
		
		port.setText("9001");
		
		JPanel myPanel = new JPanel(new GridLayout(4,1));
	      myPanel.add(new JLabel("Server Adress:"));  
	      myPanel.add(serverAdress);
	      myPanel.add(new JLabel("Port:"));
	      myPanel.add(port);
	     

	      int result = JOptionPane.showConfirmDialog(this, myPanel, 
	               "Please Enter Server Adress and Port Values", JOptionPane.OK_CANCEL_OPTION);
	      
	      Cni.ServerAdress = serverAdress.getText();
	      if(Cni.ServerAdress.equals(""))
	    	  Cni.ServerAdress = null;
	      
	      if(result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION)
	    	  return null;
	      
	      try
	      {
	    	  Cni.port = Integer.parseInt(port.getText());
	      }
	      catch(NumberFormatException e)
	      {
	    	  JOptionPane.showMessageDialog(contentPane,
					    "Bad server adress or port.",
					    "Inane error",
					    JOptionPane.ERROR_MESSAGE);	
	    	  
	    	  result = JOptionPane.CANCEL_OPTION;
	      }
	      
	      
	      if (result == JOptionPane.OK_OPTION)
	    	  return Cni;      
	      else 
	    	  return null;	
    }
	
	/**
	 * Ask user for new nickname
	 * @return new string with nickname
	 */
	private String GetNewName() {
	        return JOptionPane.showInputDialog(
	           this,
	            "Enter your new nick name",
	            "New nick name",
	            JOptionPane.QUESTION_MESSAGE);
	}
	
	
}
