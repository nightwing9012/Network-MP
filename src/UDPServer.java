import java.io.*; 
import java.net.*; 
class UDPServer
{    
	public static void main(String args[]) throws Exception       
	{   
		DatagramSocket serverSocket = new DatagramSocket(1235);       
		byte[] receiveData;
		byte[] sendData;
		byte[] data;
		int port;
		String sentence;
		String fileDst;
		DatagramPacket receivePacket;
		FileOutputStream fo;
		String[] command;
		String fileName = "nothing";
		String prevCommand = "nothing";
		String path;
		File file;
		
		while(true)                
		{	
			receiveData = new byte[100000];
			sendData = new byte[1024];
			
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);
			sentence = new String(receivePacket.getData());
			command = sentence.split(" ");
			path = null;
			
			System.out.println(fileName);
			
			if(prevCommand.equals("send_img"))
			{
				data = receivePacket.getData();
				
				//path = path.concat(fileName);
				//file = new File(path);
				//System.out.println(path);
				fo = new FileOutputStream("C:/Users/Goldwin/Desktop/received/" + "image1.png");
				fo.write(data);
				fo.flush();
				fo.close();
				
			}
			
			else if(prevCommand.equals("send_aud"))
			{
				data = receivePacket.getData();

				fo = new FileOutputStream("C:/Users/Goldwin/Desktop/received/" + "audio1.mp3");
				fo.write(data);
				fo.flush();
				fo.close();
				
			}
			
			else 
			{	
				System.out.println("RECEIVED: " + sentence.toString().trim());
				prevCommand = command[0];
				fileName = command[1];
			}
			
			InetAddress IPAddress = receivePacket.getAddress();                   
			port = receivePacket.getPort();                   
	//		String capitalizedSentence = sentence.toUpperCase();
			String capitalizedSentence = "file accepted!";  
			sendData = capitalizedSentence.getBytes();  
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			serverSocket.send(sendPacket);              	
			
			
		}       
	} 
}