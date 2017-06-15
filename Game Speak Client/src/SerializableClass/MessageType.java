package SerializableClass;

/**
 * stores information about type of message
 */
public enum MessageType 
{
	message,
	clientData,
	serverData,
	disconnect,
	log,
	changeChannel,
	channelInfo,
	UserJoinChannel,
	UserDisconnectChannel,
	sendMessageToUser
}