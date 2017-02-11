package de.cossijns.LibJavaFxMC;

import com.sun.javafx.tk.Toolkit;

import javafx.beans.NamedArg;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Rectangle2D;
import javafx.scene.text.Font;

/**
 * The {@code FontController} class can manage
 * the size of a {@link Font}.
 * <p>
 * The Font size is dependent from the {@code Rectangle},
 * the text and Font Family.
 * <p>
 * The size is the maximal which can draw on the {@code Rectangle}. The
 * {@code Rectangle} will describe by {@code width} and {@code height}.
 *
 * @author  Marcel Cossijns
 */
public final class FontController{
	
	private final ChangeListener<? super Font> fontListener = new ChangeListener<Font>() {
		@Override
		public void changed(ObservableValue<? extends Font> observable, Font oldValue, Font newValue) {
			if(oldValue.getName()!=newValue.getName()){
				setFontName(newValue.getName());		
				recalculate();
			}
		}		
	};

	private ObjectProperty<Font> font=new SimpleObjectProperty<Font>();

    private StringProperty fontName=new SimpleStringProperty("");
    private StringProperty text=new SimpleStringProperty("");
    
    private DoubleProperty width=new SimpleDoubleProperty(0);
    private DoubleProperty height=new SimpleDoubleProperty(0);

	public FontController(@NamedArg("fontName") ObservableValue<? extends String> fontName,@NamedArg("text") ObservableValue<? extends String> text,@NamedArg("width") ObservableValue<Number> width,@NamedArg("height") ObservableValue<Number> height){
		setFontName(fontName);
		setText(text);
		setWidth(width);
		setHeight(height);
		init();
	}
	public FontController(@NamedArg("fontName") ObservableValue<? extends String> fontName,@NamedArg("text") String text,@NamedArg("width") ObservableValue<Number>  width,@NamedArg("height") ObservableValue<Number> height){
		setFontName(fontName);
		setText(text);
		setWidth(width);
		setHeight(height);
		init();
	}
	public FontController(@NamedArg("fontName") String fontName,@NamedArg("text") ObservableValue<? extends String> text,@NamedArg("width") ObservableValue<Number> width,@NamedArg("height") ObservableValue<Number> height){
		setFontName(fontName);
		setText(text);
		setWidth(width);
		setHeight(height);
		init();
	}
	public FontController(@NamedArg("fontName") String fontName,@NamedArg("text") String text,@NamedArg("width") ObservableValue<Number> width,@NamedArg("height") ObservableValue<Number> height){
		setFontName(fontName);
		setText(text);
		setWidth(width);
		setHeight(height);
		init();
	}
	public FontController(@NamedArg("fontName") ObservableValue<? extends String> fontName,@NamedArg("text") ObservableValue<? extends String> text,@NamedArg("width") Double width,@NamedArg("height") Double height){
		setFontName(fontName);
		setText(text);
		setWidth(width);
		setHeight(height);
		init();
	}
	public FontController(@NamedArg("fontName") ObservableValue<? extends String> fontName,@NamedArg("text") String text,@NamedArg("width") Double width,@NamedArg("height") Double height){
		setFontName(fontName);
		setText(text);
		setWidth(width);
		setHeight(height);
		init();
	}
	public FontController(@NamedArg("fontName") String fontName,@NamedArg("text") ObservableValue<? extends String> text,@NamedArg("width") Double width,@NamedArg("height") Double height){
		setFontName(fontName);
		setText(text);
		setWidth(width);
		setHeight(height);
		init();
	}
	public FontController(@NamedArg("fontName") String fontName,@NamedArg("text") String text,@NamedArg("width") Double width,@NamedArg("height") Double height){
		setFontName(fontName);
		setText(text);
		setWidth(width);
		setHeight(height);
		init();
	}
	
	public void setText(@NamedArg("text") String text){this.text.set(text);}	
	public void setText(@NamedArg("text") ObservableValue<? extends String> text){this.text.bind(text);}
	
	public void setFontName(@NamedArg("text") String fontName){this.fontName.set(fontName);}	
	public void setFontName(@NamedArg("text") ObservableValue<? extends String> fontName){this.fontName.bind(fontName);}
	
	public void setWidth(@NamedArg("width") Double width){this.width.set(width);}
	public void setWidth(@NamedArg("width") ObservableValue<Number> width){this.width.bind(width);}
	
	public void setHeight(@NamedArg("height") Double height){this.height.set(height);}
	public void setHeight(@NamedArg("height") ObservableValue<Number> height){this.height.bind(height);}
	
	public ReadOnlyObjectProperty<Font> getFontProperty(){return font;}
	public Font getFont(){return font.get();}
	
	private void init(){
		fontName.addListener((i)->{recalculate();});
		text.addListener((i)->{recalculate();});
		width.addListener((i)->{recalculate();});
		height.addListener((i)->{recalculate();});
		recalculate();
	}
	private void recalculate(){
		font.removeListener(fontListener);
		font.set(new Font(fontName.getName(),pickOptimalFontSize(fontName.get(),text.get(),width.get(),height.get())));
		font.addListener(fontListener);
	}
    /**
     * Returns a {@link Double} object representing the
     * maximal {@link Font} size, which can illustrate
     * the text in the font in the Rectangle.
     *
     * @return  a Double representation the size of {@link Font}
     */
	public static double pickOptimalFontSize (@NamedArg("fontName") String fontName,@NamedArg("text") String text,@NamedArg("width") double width,@NamedArg("height") double height) {
		double maxFont = 10;
		double minFont =  0;
	    Rectangle2D rect;
		do {
			if(maxFont!=10)minFont=maxFont;
			maxFont*=2;
	        Font font = new Font(fontName, maxFont);
	        rect = getStringBoundsRectangle2D( text, font);
	    } while (rect.getWidth() < width && rect.getHeight() < height);
		do {
			double tempFont= (minFont+maxFont)/2;
	        Font font = new Font(fontName, tempFont);
	        rect = getStringBoundsRectangle2D( text, font);
	        if(rect.getWidth() >= width || rect.getHeight()>=height){
	        	maxFont=tempFont;
	        }else{
	        	minFont=tempFont;
	        }
	    } while (maxFont-minFont>0.1);
	    return minFont;
	}
	private static Rectangle2D getStringBoundsRectangle2D (String text, Font font) {
		double width = Toolkit.getToolkit().getFontLoader().computeStringWidth(text,font);
		double height = Toolkit.getToolkit().getFontLoader().getFontMetrics(font).getLineHeight();
	    return new Rectangle2D(0, 0, width, height);
	}
}