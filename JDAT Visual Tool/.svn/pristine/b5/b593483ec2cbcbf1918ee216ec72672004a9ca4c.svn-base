package cn.edu.buaa.sei.jdat.vt.res;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.cocotingo.snail.AcceleratedImage;
import com.cocotingo.snail.GraphicsX;
import com.cocotingo.snail.StretchableImage;
import com.cocotingo.snail.template.ResourceLoader;
import com.cocotingo.snail.template.TemplateException;
import com.cocotingo.snail.template.TemplateLoader;

public class Resources {
	
	public static class LocalImageLoader implements AcceleratedImage.ImageSourceLoader {
		private final String filename;
		public LocalImageLoader(String filename) {
			this.filename = filename;
		}
		@Override
		public BufferedImage loadImage() {
			try {
				return ImageIO.read(Resources.class.getResourceAsStream(filename));
			} catch (IOException e) {
				return null;
			}
		}
		@Override
		public void unloadImage(BufferedImage image) {
			image.flush();
		}
	}
	
	public final static Color COLOR_TEXT_DEFAULT = new Color(245, 245, 245);
	public final static Color COLOR_TEXT_LIGHT = new Color(200, 185, 175);
	public final static Color COLOR_TEXT_GRAY = new Color(150, 135, 125);
	public final static Color COLOR_TEXT_SHADOW = new Color(57, 51, 44);
	public final static Color COLOR_LINE = new Color(81, 77, 72);
	public final static Color COLOR_TEXT_ORANGE = new Color(92, 58, 19);
	public final static Color COLOR_TEXT_ORANGE_SHADOW = new Color(205, 148, 81);
	public final static Color COLOR_TEXT_BROWN = new Color(213, 211, 208);
	public final static Color COLOR_TEXT_BROWN_SHADOW = new Color(63, 55, 47);
	public final static Color COLOR_TEXT_PINK = new Color(68, 19, 34);
	public final static Color COLOR_TEXT_PINK_SHADOW = new Color(192, 124, 143);
	public final static Color COLOR_LIGHT_BACKGROUND = new Color(70, 65, 55);
	public final static Color COLOR_DEFAULT_BACKGROUND = new Color(32, 30, 26);
	
	public final static Color COLOR_GRAPH_ARROW = new Color(255, 208, 81);
	public final static Color COLOR_GRAPH_ARROW_HIGHLIGHTED = new Color(255, 220, 190);
	public final static Color COLOR_GRAPH_ARROW_SELECTED = Color.white;
	
	public final static Font FONT_LABEL = new Font(Font.DIALOG, Font.BOLD, 12);
	public final static Font FONT_BIG_BOLD = new Font(Font.DIALOG, Font.BOLD, 16);
	public final static Font FONT_BIG_PLAIN = new Font(Font.DIALOG, Font.PLAIN, 16);
	public final static Font FONT_DEFAULT = new Font(Font.DIALOG, Font.PLAIN, 12);
	public final static Font FONT_MONO = new Font(Font.MONOSPACED, Font.PLAIN, 12);
	public final static Font FONT_MONO_BOLD = new Font(Font.MONOSPACED, Font.BOLD, 12);
	public final static Font FONT_SMALL = new Font(Font.DIALOG, Font.PLAIN, 9);
	
	public final static Stroke STROKE_DASHLINE = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 6.0f, new float[] {6.0f}, 0.0f);;
	
	public static AcceleratedImage IMG_BACKGROUND;
	public static StretchableImage IMG_BUTTON_NORMAL, IMG_BUTTON_PRESSED, IMG_BUTTON_HIGHLIGHTED;
	public static StretchableImage IMG_TEXTBOX_NORMAL;
	public static StretchableImage IMG_DEPTH_BACKGROUND;
	public static StretchableImage IMG_SCROLL_INDICATOR;
	public static StretchableImage IMG_TABBUTTON_LEFT_NORMAL, IMG_TABBUTTON_MIDDLE_NORMAL, IMG_TABBUTTON_RIGHT_NORMAL;
	public static StretchableImage IMG_TABBUTTON_LEFT_HIGHLIGHTED, IMG_TABBUTTON_MIDDLE_HIGHLIGHTED, IMG_TABBUTTON_RIGHT_HIGHLIGHTED;
	public static StretchableImage IMG_TABBUTTON_LEFT_PRESSED, IMG_TABBUTTON_MIDDLE_PRESSED, IMG_TABBUTTON_RIGHT_PRESSED;
	public static StretchableImage IMG_PROGRESSBAR_BACKGROUND, IMG_PROGRESSBAR_FOREGROUND;
	public static StretchableImage IMG_ORANGE_PANEL, IMG_BROWN_PANEL, IMG_PINK_PANEL;
	public static AcceleratedImage IMG_CHECKBOX_NORMAL, IMG_CHECKBOX_HIGHLIGHTED, IMG_CHECKBOX_SELECTED;
	public static AcceleratedImage IMG_RADIOBUTTON_NORMAL, IMG_RADIOBUTTON_HIGHLIGHTED, IMG_RADIOBUTTON_SELECTED;
	public static AcceleratedImage IMG_VALUEBUBBLE;
	public static StretchableImage IMG_TIPBUBBLE;
	
	public static AcceleratedImage ICON_ABSTRACT;
	public static AcceleratedImage ICON_CLASS;
	public static AcceleratedImage ICON_CONSTRUCTOR;
	public static AcceleratedImage ICON_DEFAULT;
	public static AcceleratedImage ICON_ENUM;
	public static AcceleratedImage ICON_FIELD;
	public static AcceleratedImage ICON_FINAL;
	public static AcceleratedImage ICON_INTERFACE;
	public static AcceleratedImage ICON_JAR;
	public static AcceleratedImage ICON_METHOD;
	public static AcceleratedImage ICON_PACKAGE;
	public static AcceleratedImage ICON_PRIVATE;
	public static AcceleratedImage ICON_PROTECTED;
	public static AcceleratedImage ICON_QUESTION;
	public static AcceleratedImage ICON_STATIC;
	public static AcceleratedImage ICON_UNKNOWN_TYPE;
	
	public static TemplateLoader TEMPLATE_LOADER;
	
	public static void loadResource(GraphicsConfiguration gc) throws IOException, TemplateException {
		
		TEMPLATE_LOADER = new TemplateLoader(gc);
		TEMPLATE_LOADER.load("ui.xml", new ResourceLoader() {
			@Override
			public InputStream loadResource(String resourceName) {
				return Resources.class.getResourceAsStream(resourceName);
			}
		});
		
		IMG_BACKGROUND = (AcceleratedImage) TEMPLATE_LOADER.getResource("IMG_BACKGROUND");
		IMG_BUTTON_NORMAL = (StretchableImage) TEMPLATE_LOADER.getResource("IMG_BUTTON_NORMAL");
		IMG_BUTTON_PRESSED = (StretchableImage) TEMPLATE_LOADER.getResource("IMG_BUTTON_PRESSED");
		IMG_BUTTON_HIGHLIGHTED = (StretchableImage) TEMPLATE_LOADER.getResource("IMG_BUTTON_HIGHLIGHTED");
		IMG_TEXTBOX_NORMAL = (StretchableImage) TEMPLATE_LOADER.getResource("IMG_TEXTBOX_NORMAL");
		IMG_DEPTH_BACKGROUND = (StretchableImage) TEMPLATE_LOADER.getResource("IMG_DEPTH_BACKGROUND");
		IMG_SCROLL_INDICATOR = (StretchableImage) TEMPLATE_LOADER.getResource("IMG_SCROLL_INDICATOR");
		IMG_TABBUTTON_LEFT_NORMAL = (StretchableImage) TEMPLATE_LOADER.getResource("IMG_TABBUTTON_LEFT_NORMAL");
		IMG_TABBUTTON_MIDDLE_NORMAL = (StretchableImage) TEMPLATE_LOADER.getResource("IMG_TABBUTTON_MIDDLE_NORMAL");
		IMG_TABBUTTON_RIGHT_NORMAL = (StretchableImage) TEMPLATE_LOADER.getResource("IMG_TABBUTTON_RIGHT_NORMAL");
		IMG_TABBUTTON_LEFT_HIGHLIGHTED = (StretchableImage) TEMPLATE_LOADER.getResource("IMG_TABBUTTON_LEFT_HIGHLIGHTED");
		IMG_TABBUTTON_MIDDLE_HIGHLIGHTED = (StretchableImage) TEMPLATE_LOADER.getResource("IMG_TABBUTTON_MIDDLE_HIGHLIGHTED");
		IMG_TABBUTTON_RIGHT_HIGHLIGHTED = (StretchableImage) TEMPLATE_LOADER.getResource("IMG_TABBUTTON_RIGHT_HIGHLIGHTED");
		IMG_TABBUTTON_LEFT_PRESSED = (StretchableImage) TEMPLATE_LOADER.getResource("IMG_TABBUTTON_LEFT_PRESSED");
		IMG_TABBUTTON_MIDDLE_PRESSED = (StretchableImage) TEMPLATE_LOADER.getResource("IMG_TABBUTTON_MIDDLE_PRESSED");
		IMG_TABBUTTON_RIGHT_PRESSED = (StretchableImage) TEMPLATE_LOADER.getResource("IMG_TABBUTTON_RIGHT_PRESSED");
		IMG_PROGRESSBAR_BACKGROUND = (StretchableImage) TEMPLATE_LOADER.getResource("IMG_PROGRESSBAR_BACKGROUND");
		IMG_PROGRESSBAR_FOREGROUND = (StretchableImage) TEMPLATE_LOADER.getResource("IMG_PROGRESSBAR_FOREGROUND");
		IMG_ORANGE_PANEL = (StretchableImage) TEMPLATE_LOADER.getResource("IMG_ORANGE_PANEL");
		IMG_BROWN_PANEL = (StretchableImage) TEMPLATE_LOADER.getResource("IMG_BROWN_PANEL");
		IMG_PINK_PANEL = (StretchableImage) TEMPLATE_LOADER.getResource("IMG_PINK_PANEL");
		IMG_CHECKBOX_NORMAL = (AcceleratedImage) TEMPLATE_LOADER.getResource("IMG_CHECKBOX_NORMAL");
		IMG_CHECKBOX_HIGHLIGHTED = (AcceleratedImage) TEMPLATE_LOADER.getResource("IMG_CHECKBOX_HIGHLIGHTED");
		IMG_CHECKBOX_SELECTED = (AcceleratedImage) TEMPLATE_LOADER.getResource("IMG_CHECKBOX_SELECTED");
		IMG_RADIOBUTTON_NORMAL = (AcceleratedImage) TEMPLATE_LOADER.getResource("IMG_RADIOBUTTON_NORMAL");
		IMG_RADIOBUTTON_HIGHLIGHTED = (AcceleratedImage) TEMPLATE_LOADER.getResource("IMG_RADIOBUTTON_HIGHLIGHTED");
		IMG_RADIOBUTTON_SELECTED = (AcceleratedImage) TEMPLATE_LOADER.getResource("IMG_RADIOBUTTON_SELECTED");
		IMG_VALUEBUBBLE = (AcceleratedImage) TEMPLATE_LOADER.getResource("IMG_VALUEBUBBLE");
		IMG_TIPBUBBLE = (StretchableImage) TEMPLATE_LOADER.getResource("IMG_TIPBUBBLE");
		
		ICON_ABSTRACT = (AcceleratedImage) TEMPLATE_LOADER.getResource("ICON_ABSTRACT");
		ICON_CLASS = (AcceleratedImage) TEMPLATE_LOADER.getResource("ICON_CLASS");
		ICON_CONSTRUCTOR = (AcceleratedImage) TEMPLATE_LOADER.getResource("ICON_CONSTRUCTOR");
		ICON_DEFAULT = (AcceleratedImage) TEMPLATE_LOADER.getResource("ICON_DEFAULT");
		ICON_ENUM = (AcceleratedImage) TEMPLATE_LOADER.getResource("ICON_ENUM");
		ICON_FIELD = (AcceleratedImage) TEMPLATE_LOADER.getResource("ICON_FIELD");
		ICON_FINAL = (AcceleratedImage) TEMPLATE_LOADER.getResource("ICON_FINAL");
		ICON_INTERFACE = (AcceleratedImage) TEMPLATE_LOADER.getResource("ICON_INTERFACE");
		ICON_JAR = (AcceleratedImage) TEMPLATE_LOADER.getResource("ICON_JAR");
		ICON_METHOD = (AcceleratedImage) TEMPLATE_LOADER.getResource("ICON_METHOD");
		ICON_PACKAGE = (AcceleratedImage) TEMPLATE_LOADER.getResource("ICON_PACKAGE");
		ICON_PRIVATE = (AcceleratedImage) TEMPLATE_LOADER.getResource("ICON_PRIVATE");
		ICON_PROTECTED = (AcceleratedImage) TEMPLATE_LOADER.getResource("ICON_PROTECTED");
		ICON_QUESTION = (AcceleratedImage) TEMPLATE_LOADER.getResource("ICON_QUESTION");
		ICON_STATIC = (AcceleratedImage) TEMPLATE_LOADER.getResource("ICON_STATIC");
		ICON_UNKNOWN_TYPE = (AcceleratedImage) TEMPLATE_LOADER.getResource("ICON_UNKNOWN_TYPE");
	}
	
	public static void fillBackground(GraphicsX g, int width, int height) {
		final Shape clipArea = g.getClip();
		g.clipRect(0, 0, width, height);
		for (int x = 0; x < width; x += IMG_BACKGROUND.getWidth())
			for (int y = 0; y < height; y += IMG_BACKGROUND.getHeight()) {
				g.drawImage(IMG_BACKGROUND, x, y);
			}
		g.setClip(clipArea);
	}
	
}
