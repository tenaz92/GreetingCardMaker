package com.qburst.greetingcardapp.models;



public class ImageModel {
	private int cardId;
	private String cardThumb;
	private String originalImage;
	private String edittedImage;
	private String signImage;
	private String text;
	private int frameId;
	private int color;
	private float xText;
	private float yText;
	private String card;
	public String getCard() {
		return card;
	}
	public void setCard(String card) {
		this.card = card;
	}
	public String getOriginalImage() {
		return originalImage;
	}
	public void setOriginalImage(String originalImage) {
		this.originalImage = originalImage;
	}
	public String getEdittedImage() {
		return edittedImage;
	}
	public void setEdittedImage(String edittedImage) {
		this.edittedImage = edittedImage;
	}
	public String getSignImage() {
		return signImage;
	}
	public void setSignImage(String signImage) {
		this.signImage = signImage;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getCardThumb() {
		return cardThumb;
	}
	public void setCardThumb(String cardThumb) {
		this.cardThumb = cardThumb;
	}
	public int getCardId() {
		return cardId;
	}
	public void setCardId(int cardId) {
		this.cardId = cardId;
	}
	public int getFrameId() {
		return frameId;
	}
	public void setFrameId(int frameId) {
		this.frameId = frameId;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public float getxText() {
		return xText;
	}
	public void setxText(float xText) {
		this.xText = xText;
	}
	public float getyText() {
		return yText;
	}
	public void setyText(float yText) {
		this.yText = yText;
	}

	
}
