package br.com.RatosDePC.Brpp.Utils;

/*
Copyright (c) 2016 StarFruitBrasil

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

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
					// if (portId.getName().equals("/dev/term/a")) {
					try {
						serialPort = (SerialPort) portId.open("Arduino", 9600);
						serialPort.setSerialPortParams(9600,
								SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
								SerialPort.PARITY_NONE);
						inStream = serialPort.getInputStream();
						serialPort.addEventListener(new CommPortUtils());
						serialPort.notifyOnDataAvailable(true);
						// serialPort.notifyOnBreakInterrupt(true);
						// serialPort.notifyOnCarrierDetect(true);
						// serialPort.notifyOnCTS(true);
						// serialPort.notifyOnDataAvailable(true);
						// serialPort.notifyOnDSR(true);
						// serialPort.notifyOnFramingError(true);
						// serialPort.notifyOnOutputEmpty(true);
						// serialPort.notifyOnOverrunError(true);
						// serialPort.notifyOnParityError(true);
						// serialPort.notifyOnRingIndicator(true);
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
//		byte[] readBuffer = new byte[2000];
		try {
			while (inStream.available() > 0) {
//				inStream.read(readBuffer);
				SerialMonitor.display(String.valueOf((char) inStream.read()));
			}
//			SerialMonitor.display(new String(readBuffer));
		} catch (IOException ioe) {
			System.out.println("Exception " + ioe);
			ioe.printStackTrace();
		} 
	}

	public static void closePort() throws NullPointerException {
		serialPort.close();
	}

}