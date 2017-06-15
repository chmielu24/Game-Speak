import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import SerializableClass.MessageToUser;
import SerializableClass.MessageWithTime;

/**
 * Class TextChat is responsible for communication with user Input and Output
 */

public class TextChat {
	
	private JTextField chatIn ;
	private JTextPane chatOut ;
	
	private Style blackStyle;
	private Style redStyle;
	private Style darkGrayStyle;
	private Style blueStyle;
	private Style blackBoldStyle;

	/**
	 * Initialize chatOut and chatIn, add Listener to ApplicationManager
	 * send Message from chatIn and clear chatIn
	 * set the Styles of messages: blackStyle, blackBoldStyle, redStyle, darkGrayStyle, blueStyle
	 */
	public TextChat(JTextPane chatOut, JTextField chatIn){
		this.chatOut = chatOut;
		this.chatIn = chatIn;
		
		chatIn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(ApplicationManager.Instance().currentConnection != null)
					ApplicationManager.Instance().currentConnection.SendTextMessage(chatIn.getText());
				
				chatIn.setText("");
			}
		});
		
		StyledDocument document = chatOut.getStyledDocument ();
		Style defaultStyle =
		      StyleContext.getDefaultStyleContext ().getStyle (StyleContext.DEFAULT_STYLE);
		Style regular = document.addStyle ("regular", defaultStyle);

		blackStyle = document.addStyle ("BlackStyle", regular);
		StyleConstants.setForeground (blackStyle, Color.black);
		
		blackBoldStyle = document.addStyle ("VlackBoldStyle", regular);
		StyleConstants.setForeground (blackBoldStyle, Color.black);
		StyleConstants.setBold (blackBoldStyle, true);	

		redStyle = document.addStyle ("RedStyle", regular);
		StyleConstants.setForeground (redStyle, Color.red);
		StyleConstants.setBold (redStyle, true);	
		
		darkGrayStyle = document.addStyle ("GreenStyle", regular);
		StyleConstants.setForeground (darkGrayStyle, Color.darkGray);
		StyleConstants.setBold (darkGrayStyle, true);	
		
		blueStyle = document.addStyle ("BlueStyle", regular);
		StyleConstants.setForeground (blueStyle, Color.blue);
		StyleConstants.setBold (blueStyle, true);	
		
	
		
	
	}
	
	/**
	 * write functions write information for user in chatOut in black style
	 * @param s message to write
	 */
	public void write(String s)
	{
		try {
			chatOut.getStyledDocument().insertString(chatOut.getSelectionEnd(), s + "\n", blackStyle);
		} catch (BadLocationException e) {
			System.err.println("Write Exception");
			//e.printStackTrace();
		}
		
		//chatOut.setText(chatOut.getText() + s);
		//chatOut.setText(chatOut.getText() + "\n");	
	}
	
	/**
	 * write functions write information for user in chatOut in darkGrayStyle
	 * @param s message to write
	 */
	public void writeGreen(String s)
	{
		try {
			chatOut.getStyledDocument().insertString(chatOut.getSelectionEnd(), s + "\n", darkGrayStyle);
		} catch (BadLocationException e) {
			System.err.println("Write Exception");
			//e.printStackTrace();
		}
		
		//chatOut.setText(chatOut.getText() + s);
		//chatOut.setText(chatOut.getText() + "\n");	
	}
	
	/**
	 * write functions write information for user in chatOut in blueStyle
	 * @param s message to write
	 */
	public void writeLog(String s)
	{

		try {
			chatOut.getStyledDocument().insertString(chatOut.getSelectionEnd(),"Info: " + s + "\n", blueStyle);
		} catch (BadLocationException e) {
			System.err.println("Write Exception");
			//e.printStackTrace();
		}
		
		//chatOut.setText(chatOut.getText() + s);
		//chatOut.setText(chatOut.getText() + "\n");	
	}
	
	/**
	 * write the message (time + sender + message) in chatOut
	 * @param message keep our message
	 */
	public void write(MessageWithTime message)
	{
		try {
			chatOut.getStyledDocument().insertString(chatOut.getSelectionEnd(), message.time + " ", redStyle);
			chatOut.getStyledDocument().insertString(chatOut.getSelectionEnd(), message.sender + ": ", blackBoldStyle);
			chatOut.getStyledDocument().insertString(chatOut.getSelectionEnd(), message.message + "\n", blackStyle);
		} catch (BadLocationException e) {
			System.err.println("Write Exception");
			e.printStackTrace();
		}
		
	
		
		//chatOut.setText(chatOut.getText() + message.message);
		//chatOut.setText(chatOut.getText() + "\n");	
	}
	
	/**
	 * write the private message (Private Message + time + from + message) in chatOut
	 * @param message keep our message
	 */
	public void write(MessageToUser message)
	{
		try {
			chatOut.getStyledDocument().insertString(chatOut.getSelectionEnd(),"Private Message " + message.Time + " ", redStyle);
			chatOut.getStyledDocument().insertString(chatOut.getSelectionEnd(), message.from + ": ", blackBoldStyle);
			chatOut.getStyledDocument().insertString(chatOut.getSelectionEnd(), message.Message + "\n", blackStyle);
		} catch (BadLocationException e) {
			System.err.println("Write Exception");
			e.printStackTrace();
		}
		
	
		
		//chatOut.setText(chatOut.getText() + message.message);
		//chatOut.setText(chatOut.getText() + "\n");	
	}
	

	/**
	 * clear chat
	 */
	public void Clear()
	{
		chatOut.setText("");
		chatIn.setText("");
	}
}
