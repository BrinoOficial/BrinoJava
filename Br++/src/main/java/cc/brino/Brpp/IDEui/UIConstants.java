package cc.brino.Brpp.IDEui;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;


public class UIConstants {
	public static final Color VERDE = new Color(96, 188, 14);/* 11, 125,73*/ 
	public static final Color CINZA = new Color(25, 25, 25);/*46,46,46*/
	public static final Color CINZAESCURO = new Color(10, 10, 10);/*30,30,30*/
	public static final Border BORDACINZAESCUROARREDONDADA = new LineBorder(CINZAESCURO, 15,
			true);
	public static final Border BORDAVERDEARREDONDADA = new LineBorder(UIConstants.VERDE, 5, true);
	public static final Border BORDAVAZIA = BorderFactory.createEmptyBorder();
	public static final Border BORDATRANSPARENTE= BorderFactory.createEmptyBorder(5,
			5,
			5,
			5);
}
