import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import SerializableClass.ClientData;
import SerializableClass.ClientsOnChannelData;

/**
 * class InformationsList is responsible for drawing additional informations to user like a other user on channel
 */
public class InformationsList {

	JPanel InformationPanel;
	ClientsOnChannelData Clientsdata;
	
	List<JTextField> data = new LinkedList<>();
	
	/**
	 * initialize Information Panel
	 */
	public InformationsList(JPanel informationPanel)
	{
		InformationPanel = informationPanel;
	}
	
	/**
	 * Draw Information about users on channel on InformationPanel
	 * @param d list of data of Clients on channel
	 */
	public void ShowInfo(ClientsOnChannelData d)
	{
		if(Clientsdata != null)
			Clear();
		
		Clientsdata = d;
		
		JPanel panel = new JPanel(new GridLayout(10,1));
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		JScrollPane scrollPanel = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		InformationPanel.add(scrollPanel, BorderLayout.CENTER);
		
		JTextField Header = new JTextField("Users List:");
		Header.setEditable(false);
		Header.setHorizontalAlignment(JTextField.CENTER);
		data.add(Header);
		
		int counter = 1;
		for (ClientData client : Clientsdata.clientsOnChannel) {
			JTextField text = new JTextField(counter++ + ". " +client.ClientName);
			text.setEditable(false);
			text.setHorizontalAlignment(JTextField.CENTER);
			data.add(text);	
		}	
		
		for (JTextField el : data) {
			panel.add(el);
		}	
		
		InformationPanel.repaint();
		InformationPanel.updateUI();
	}
	
	/**
	 * clear InformationPanel
	 */
	public void Clear()
	{
		Clientsdata = null;
		data.clear();
		InformationPanel.removeAll();
		InformationPanel.repaint();
		InformationPanel.updateUI();
	}
}
