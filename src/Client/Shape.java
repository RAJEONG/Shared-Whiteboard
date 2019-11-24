package Client;

import java.awt.Color;
import java.awt.Font;
import java.io.Serializable;

public class Shape implements Serializable {
	int type;
	int startX, startY, endX, endY;
	String inputText;
	Color color;
	Font font;
	int brushSize;
	
	/* ShapeType List
	 * Free Line : 1
	 * Straight Line : 2
	 * Circle : 3
	 * Rectangle : 4
	 * Oval : 5
	 * Input Text : 6
	 * Eraser : 7
	 */
	
	public void setShapeInfo(int type, int startX, int startY, int endX, int endY, Color color, int brushSize) {
		this.type = type;
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.color = color;
		this.brushSize = brushSize;
	}
	
	public void setShapeInfo(int type, String inputText, int startX, int startY, int endX, int endY, Color color, Font font) {
		this.type = type;
		this.inputText = inputText;
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.color = color;
		this.font = font;
	}
	
	public int getShapeType() {
		return type;
	}
	
	public int getShapeStartX() {
		return startX;
	}
	
	public int getShapeStartY() {
		return startY;
	}
	
	public int getShapeEndX() {
		return endX;
	}
	
	public int getShapeEndY() {
		return endY;
	}
	public Color getColor() {
		return color;
	}
}
