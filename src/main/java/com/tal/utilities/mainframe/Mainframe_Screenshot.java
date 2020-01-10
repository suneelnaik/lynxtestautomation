package com.tal.utilities.mainframe;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import com.ibm.eNetwork.ECL.ECLErr;
import com.ibm.eNetwork.ECL.ECLSession;

public class Mainframe_Screenshot {

	// Creates a screenshot of the current mainframe window and returns a filename location
	public String createScreenshot(ECLSession session) throws IOException {
		char[] buffer = new char[1920];
		StringBuilder sb = new StringBuilder();

		try {						
			sb.append("________________________________________________________________________________\n");
			session.GetPS().GetScreen(buffer, 1920, 1, 1920, 1);
			for (int i = 0; i < 1920; i++) {
				if (i % 80 == 0) {
					sb.append("\n");
				}
				sb.append(buffer[i]);
			}
			sb.append("\n--------------------------------------------------------------------------------");			
		} catch (ECLErr e) {
			System.out.println("Unable to capture screenshot");
			sb.append("\n--------------------------------------------------------------------------------");
		}
	
		String Base64Image = this.CreateScreenshotImage(sb);
		return Base64Image;

	}

	/*
	 * takes a string and converts it into an image representing the current
	 * mainframe screen
	 */
	private String CreateScreenshotImage(StringBuilder sb) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		BufferedImage img = new BufferedImage(610, 410, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = img.createGraphics();
		Font font = new Font("IBM3270", Font.PLAIN, 10);
		Map<TextAttribute, Object> attributes = new HashMap<TextAttribute, Object>();
		attributes.put(TextAttribute.TRACKING, 0.1);
		Font font2 = font.deriveFont(attributes);
		g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		g2d.setFont(font2);
		g2d.setBackground(Color.black);
		g2d.setColor(Color.GREEN);
		String[] lines = sb.toString().split("\n");
		for (int lineCount = 0; lineCount < lines.length; lineCount++) { // lines from above
			int xPos = 15;
			int yPos = 15 + lineCount * g2d.getFontMetrics().getHeight();
			String line = lines[lineCount];
			g2d.drawString(line.replace(" ", "  "), xPos, yPos);
		}
		g2d.dispose();
		ImageIO.write(img, "png", os);
		return Base64.getEncoder().encodeToString(os.toByteArray());		
	}
}
