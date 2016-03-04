import java.applet.AudioClip;
import java.awt.image.BufferedImage;
import java.io.*; 
import java.net.*; 
import java.nio.file.Files;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.spi.AudioFileReader;

class UDPClient 
{   
	public static DatagramSocket clientSocket;
	public static DatagramPacket sendPacket;
	public static DatagramPacket receivePacket;
	public static DatagramPacket packet;
	public static String sentence;
	public static String modifiedSentence;
	public static BufferedReader inFromUser;
	public static InetAddress IPAddress; 
	public static byte[] sendData;  
	public static byte[] receiveData;
	public static byte[] buffer;
	public static String[] command;
	public static String[] checker;
	public static String sourceFile;
	public static String fileType;
	public static BufferedImage img;
	public static ByteArrayOutputStream baos;
	public static File f;
	public static AudioFileReader afReader;
	public static AudioFormat audFormat;
	public static AudioInputStream ais;
	
	public static void main(String args[]) throws Exception    
	{   
		inFromUser = new BufferedReader(new InputStreamReader(System.in));
		IPAddress = InetAddress.getByName("192.168.1.4"); 
		
		while(true)
		{	           
			receiveData = new byte[1024]; 
			clientSocket = new DatagramSocket();
			sendData = new byte[1000000];
			command = null;
			checker = null;
			baos = new ByteArrayOutputStream();
			
			
			System.out.println("Enter command: ");
			sentence = inFromUser.readLine();
			command = sentence.split(" ");
			
			
			
			if(command.length == 2) // checks for the space within the input string
			{	
//				System.out.println(command[0]);
//				System.out.println(command[1]);	
				sourceFile = command[1];
				
				if(command[0].equals("send_img")) // checks for the "send_pic" command
				{
					System.out.println("Command Accepted!");
					f = new File("src/img/" + sourceFile);
					fileType = Files.probeContentType(f.toPath());
					checker = fileType.split("/");
					
					if(f.exists() && checker[0].equals("image")) // checks file type and existence
					{	
						accepted(); // sends the command which includes the file name
						sendImage(); // sends the image
						getFeedback(); // receives feedback from server
					}
					
					else
						System.out.println("File Not Found!\n");
									
				}
				
				else if(command[0].equals("send_vid")) // checks for the "send_vid" command 
				{
					System.out.println("Command Accepted!");
					f = new File("src/vid/" + sourceFile);
					fileType = Files.probeContentType(f.toPath());
					checker = fileType.split("/");
					System.out.println(fileType);

					if(f.exists() && checker[0].equals("video")) // checks file type and existence
					{	
						accepted(); // sends the command which includes the file name
						sendVideo(); // sends the video
						getFeedback(); // receives feedback from server
					}
					
					else
						System.out.println("File Not Found!\n");
			 							//break;
				}
				
				else if(command[0].equals("send_aud")) // checks for the "send_aud" command
				{
					System.out.println("Command Accepted!");
					f = new File("src/aud/" + sourceFile);
					fileType = Files.probeContentType(f.toPath());
					checker = fileType.split("/");

					if(f.exists() && checker[0].equals("audio")) // checks file type and existence
					{	
						accepted(); // sends the command which includes the file name
						sendAudio(); // sends the audio
						getFeedback(); // receives feedback from server
					}
					
					else
						System.out.println("File Not Found!\n");
			 							
				}
				
				else if(command[0].equals("play_vid")) // checks for the "play_vid" command 
				{
					System.out.println("Command Accepted!");
					System.out.println("Playing vid: " + sourceFile);
					accepted();
				}
				
				else if(command[0].equals("play_aud")) // checks for the "play_aud" command
				{
					System.out.println("Command Accepted!");
					System.out.println("Playing vid: " + sourceFile);
					accepted();
				}
				
				// checks for the "spec_time" and the valid time in seconds 
				else if(command[0].equals("spec_time") && Integer.parseInt(command[1]) > 0) 
				{	
					System.out.println("Command Accepted!");
					System.out.println("Changing slide show interval to: " + sourceFile + " sec");
					accepted();
				}
				
				else // command is not accepted
					System.out.println("Incorrect command or source file!");	
				
			}
			
			//checker for the commands that does not include file names
			else if((command[0].equals("pause_vid") || command[0].equals("stop_vid")|| command[0].equals("prev_img") 
					|| command[0].equals("next_img") || command[0].equals("start_slide") 
					|| command[0].equals("pause_slide") || command[0].equals("stop_slide") 
					|| command[0].equals("pause_aud") || command[0].equals("stop_aud")))
			{
				System.out.println("Command Accepted!");
				accepted();
			}
				
			else
				System.out.println("Incorrect command!\n");
			
			clientSocket.close();
		}
		
	}
	
	public static void accepted() throws IOException{ // sends the comman to the server
		sendData = sentence.getBytes();			
		sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 1235);       
		clientSocket.send(sendPacket);          
		System.out.println("Execution successful!");
	}
	
	public static void sendImage() throws IOException{ // sends the image to the server
		img = ImageIO.read(f);
		
		if(checker[1].equals("png"))
			ImageIO.write(img, "png", baos);
		
		else if(checker[1].equals("jpeg"))
			ImageIO.write(img, "jpg", baos);
		
		baos.flush();
		buffer = baos.toByteArray();
		packet = new DatagramPacket(buffer, buffer.length, IPAddress, 1235);
		clientSocket.send(packet);
	}
	
	public static void sendVideo() throws IOException{ // sends the video to the server
		
	}
	
	public static void sendAudio() throws IOException, UnsupportedAudioFileException{ // sends the audio to the server 
		ais = afReader.getAudioInputStream(f);
		
		packet = new DatagramPacket(buffer, buffer.length, IPAddress, 1235);
		clientSocket.send(packet);
	}
	
	public static void getFeedback() throws IOException{ // gets feedback from the server
		receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);       
		modifiedSentence = new String(receivePacket.getData(),"UTF-8");    
		System.out.println("FROM SERVER: " + modifiedSentence.toString().trim() + "\n");
	}
}