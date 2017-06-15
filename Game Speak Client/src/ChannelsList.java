import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.sun.org.apache.bcel.internal.generic.NEW;

import SerializableClass.ChannelData;
import SerializableClass.ServerData;
import sun.util.resources.cldr.zh.CalendarData_zh_Hans_CN;
/**
 * channelsList is responsible for drawing channels data from server and switching channel event when user click on channel
 */
public class ChannelsList {
	/**
	 * channelsPanel is responsible for panel with channel
	 */
	private JPanel channelsPanel;
	
	/**
	 * data is responsible for serialize data of channel on server 
	 */
	private ServerData data;
	
	/**
	 * channelsButtons is responsible for list of buttons
	 */
	private ArrayList<JButton> channelsButtons = new ArrayList<>();
	
	/**
	 * CurrentID is responsible for current ID channel
	 */
	private int CurrentID = -1;
	
	/**
	 * Initialize ChannelList
	 */
	
	public ChannelsList(JPanel channelsPanel) {
		this.channelsPanel = channelsPanel;

	}
	
	/**
	 * Draw Channels on channelsPanel and add MouseListener for button with channel
	 * @param d Data from server
	 */
	public void DrawChannels(ServerData d)
	{
		if(this.data != null)
			ClearChannels();
		
		this.data = d;
		
		JPanel panel = new JPanel(new GridLayout(10,1));
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		JScrollPane scrollPanel = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		channelsPanel.add(scrollPanel, BorderLayout.CENTER);

		for (ChannelData el : d.channels) {
			JButton button = new JButton(el.ChannelName);
								
			channelsButtons.add(button);
			
			button.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					
					if(ApplicationManager.Instance().currentConnection != null)
					{
						
						ApplicationManager.Instance().currentConnection.changeChannel(el);
					}
				}
			});
					}	
		
		for (JButton el : channelsButtons) {
			panel.add(el);
		}	
		
		channelsPanel.updateUI();
	}
	
	/**
	 * Clear Channels
	 */
		public void ClearChannels()
	{
		data = null;
		channelsButtons.clear();
		channelsPanel.removeAll();
		CurrentID = -1;
		
		channelsPanel.updateUI();
	}
	
		/**
		 * Set current channel
		 * @param ID - id of channel
		 */
	public void SerCurrentChannel(int ID)
	{
		if(CurrentID != -1)
		{
			channelsButtons.get(CurrentID).setEnabled(true);
		}
		
		CurrentID = ID;
		channelsButtons.get(CurrentID).setEnabled(false);

	}
	
}
