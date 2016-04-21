package br.com.RatosDePC.Brpp.Utils;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import br.com.RatosDePC.SerialMonitor.SerialMonitor;

/**
 *
 * @author JSupport http://javasrilankansupport.blogspot.com/
 */
public class CommPortUtils implements SerialPortEventListener {
	
	static CommPortIdentifier portId;
	static SerialPort serialPort;
	static InputStream inStream;
	static OutputStream outputStream;
	
	public static Enumeration<CommPortIdentifier> getComPorts() {
		@SuppressWarnings("unchecked")
		Enumeration<CommPortIdentifier> enu_ports = CommPortIdentifier
				.getPortIdentifiers();
		return enu_ports;
	}
	
	public static boolean openPort(String com) {
		Enumeration<CommPortIdentifier> portList = getComPorts();
		while (portList.hasMoreElements()) {
			portId = (CommPortIdentifier) portList.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				if (portId.getName().equals(com)) {
//				if (portId.getName().equals("/dev/term/a")) {
					try {
						serialPort = (SerialPort) portId.open("Arduino", 9600);
						serialPort.setSerialPortParams(9600,
								SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
								SerialPort.PARITY_NONE);
						inStream = serialPort.getInputStream();
						serialPort.addEventListener(new CommPortUtils());
						serialPort.notifyOnDataAvailable(true);
						serialPort.notifyOnBreakInterrupt(true);
						serialPort.notifyOnCarrierDetect(true);
						serialPort.notifyOnCTS(true);
						serialPort.notifyOnDataAvailable(true);
						serialPort.notifyOnDSR(true);
						serialPort.notifyOnFramingError(true);
						serialPort.notifyOnOutputEmpty(true);
						serialPort.notifyOnOverrunError(true);
						serialPort.notifyOnParityError(true);
						serialPort.notifyOnRingIndicator(true);
						return true;
					} catch (PortInUseException e) {
						System.out.println("PortInUse");
						e.printStackTrace();
						return false;
					} catch (UnsupportedCommOperationException e) {
						e.printStackTrace();
						return false;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return false;
					} catch (TooManyListenersException e) {
						e.printStackTrace();
						return false;
					}
				}
			}
		}
		return false;
	}
	
	public static void send(String msg) {

		try {
			outputStream = serialPort.getOutputStream();
			outputStream.write(msg.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void serialEvent(SerialPortEvent event) {
		if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			byte[] readBuffer = new byte[20];
			try {
				while (inStream.available() > 0) {
					inStream.read(readBuffer);
				}
				System.out.print(new String(readBuffer));
				SerialMonitor.display(new String(readBuffer));
			} catch (IOException ioe) {
				System.out.println("Exception " + ioe);
				ioe.printStackTrace();
			}
		}
	}	

	public static void closePort() throws NullPointerException {
		serialPort.close();
	}

}