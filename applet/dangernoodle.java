import processing.core.*; 
import processing.xml.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class Snakey extends PApplet {

class Location {
	int x;
	int y;
	Location(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public boolean equals(Location l) {
		return ((l.x == x) && (l.y == y));
	}
}

int direction = 39;
ArrayList<Location> snakey;
int snakeySize = 10;
float snakeySpeed = 20;
Location food;
int score;
int deadSnakey = 0;

public void setup() {
	size(640, 480);
	background(255, 240, 255);
	textFont(createFont("SansSerif", 15));
	ellipseMode(RADIUS);
	smooth();
	colorMode(RGB, 255, 255, 255, 100);
	reset();
	frameRate(15);
}

public void reset() {
	score = 0;
	frameRate(1);
	babySnakey();
	frameRate(15);
	addFood();
	direction = 39;
}

public void babySnakey() {
	snakey = new ArrayList<Location>();
	growSnake(5, 5);
	growSnake(4, 5);
	growSnake(3, 5);
	growSnake(2, 5);
	growSnake(1, 5);  
  
}

public void draw() {
	background(255, 240, 255);
	score();
	drawNoms();
	move();
	drawSnakey();
}

public void score() {
	fill(0);
	text(score, width/2, 2*snakeySize);
}

public void move() {
	Location head = snakey.get(0);
	if ((direction == 38) && (head.y >= snakeySize/snakeySpeed + 1) && (!tailNomming(new Location(head.x, head.y - 1)))) {
		hasNoms(new Location(head.x, head.y - 1));
	}
	else if ((direction == 40) && (head.y <= (height / snakeySpeed) - (snakeySpeed / snakeySize)) && (!tailNomming(new Location(head.x, head.y + 1)))) {
		hasNoms(new Location(head.x, head.y + 1));
	}
	else if ((direction == 37) && (head.x >= snakeySize/snakeySpeed + 1) && (!tailNomming(new Location(head.x - 1, head.y)))) {
		hasNoms(new Location(head.x - 1, head.y));
	}
	else if ((direction == 39) && (head.x <= (width / snakeySpeed) - (snakeySpeed / snakeySize)) && (!tailNomming(new Location(head.x + 1, head.y)))) {
		hasNoms(new Location(head.x + 1, head.y));
	}
	else {
		death();
	}
}
public void death() {
	deadSnakey = 1;
	background(255, 240, 255);
	drawSnakey();
	fill(0);
	text ("OUCH! Press any key to restart!", width/2 - 100, height/2);
}


public void keyPressed() {
	if (deadSnakey == 1) {
		deadSnakey = 0;
		reset();
	}
	if (key == CODED) {
		if ((keyCode >= 37) && (keyCode <= 40) && (keyCode+2!=direction) && (keyCode-2!=direction)) {
			direction = keyCode;
		}
	}
}

public void addFood() {
	float foodWidth = ((width/snakeySpeed) - (snakeySpeed/snakeySize));
	float foodHeight = ((height/snakeySpeed) - (snakeySpeed/snakeySize));
	float edge = snakeySpeed/snakeySize;
	food = new Location(round((int)random(edge, foodWidth)), round((int)random(edge, foodHeight))); 
}

public void growSnake(int x, int y) {
	snakey.add(new Location(x, y));
}

public void drawNoms() {
	drawNoms(food);
}

public void drawSnakey() {
	for(int i = snakey.size() - 1; i > 0; i--) {
		drawButt(snakey.get(i));
	}
	float snakeyX = snakey.get(0).x * snakeySpeed;
	float snakeyY = snakey.get(0).y * snakeySpeed;
	stroke(25, 25, 112);
	fill(106, 90, 205);
	bezier(
		snakeyX - (snakeySize / 3), snakeyY - (snakeySize / 1.5f),
		snakeyX - snakeySize, snakeyY - 2.75f*snakeySize,
		snakeyX, snakeyY - 2.75f*snakeySize,
		snakeyX - (snakeySize / 3), snakeyY - (snakeySize / 1.5f)
	);
	bezier(
		snakeyX + (snakeySize / 3), snakeyY - (snakeySize / 1.5f),
		snakeyX + snakeySize, snakeyY - 2.75f*snakeySize,
		snakeyX, snakeyY - 2.74f*snakeySize,
		snakeyX + (snakeySize / 3), snakeyY - (snakeySize / 1.5f)
	);
	stroke(150, 0, 0);
	fill(255, 0, 0);
	ellipse(snakeyX, snakeyY, snakeySize, snakeySize);
	stroke(0);
	fill(0);
	triangle(snakeyX - (snakeySize / 7), snakeyY + (snakeySize / 1.5f), snakeyX, snakeyY - (snakeySize / 5) + (snakeySize / 1.5f), snakeyX + (snakeySize / 7), snakeyY + (snakeySize / 1.5f));
	stroke(255, 255, 0);
	fill(50, 205, 50);
	ellipse(snakeyX - (snakeySize / 2.8f), snakeyY - (snakeySize / 10) + (snakeySize / 5), snakeySize / 5, snakeySize / 2.5f);
	ellipse(snakeyX + (snakeySize / 2.8f), snakeyY - (snakeySize / 10) + (snakeySize / 5), snakeySize / 5, snakeySize / 2.5f);
	
}

public void drawButt(Location value) {
	stroke(255, 0, 0);
	line (value.x*snakeySpeed, value.y*snakeySpeed, value.x*snakeySpeed - 1.5f*snakeySpeed/PI, value.y*snakeySpeed - 1.5f*snakeySpeed/PI);
	line (value.x*snakeySpeed, value.y*snakeySpeed, value.x*snakeySpeed + 1.5f*snakeySpeed/PI, value.y*snakeySpeed - 1.5f*snakeySpeed/PI);
	stroke(255, 165, 0);
	line (value.x*snakeySpeed, value.y*snakeySpeed, value.x*snakeySpeed, value.y*snakeySpeed - 2.1f*snakeySpeed/PI);
	stroke(0, 191,255);
	line (value.x*snakeySpeed, value.y*snakeySpeed, value.x*snakeySpeed - snakeySpeed/PI, value.y*snakeySpeed - 2*snakeySpeed/PI);
	line (value.x*snakeySpeed, value.y*snakeySpeed, value.x*snakeySpeed + snakeySpeed/PI, value.y*snakeySpeed - 2*snakeySpeed/PI);
	stroke(0);
	fill(50, 205, 50);
	ellipse(value.x * snakeySpeed, value.y * snakeySpeed, snakeySize, snakeySize);
}

public void drawNoms(Location value) {
	pushMatrix();
	noStroke();
	translate(value.x*snakeySpeed, value.y*snakeySpeed);
	fill(178, 58, 238);
	for (int i = 0; i < 5; i++) {
		ellipse(0, -snakeySize/2, snakeySize/2, snakeySize/2);
		rotate(radians(72));	 
	}
	fill(255, 215, 0);
	ellipse(0, 0, snakeySize/2.5f, snakeySize/2.5f);
	popMatrix();
}

public void hasNoms(Location newHead) {
	if (!newHead.equals(food)) {
		snakey.remove(snakey.size() - 1);
	} else {
		frameRate(1);
		fill(0);
		text("*nom*", (snakey.get(0).x * snakeySpeed) + snakeySize, (snakey.get(0).y * snakeySpeed) - snakeySize);
		frameRate(15);
		addFood();
		score++;
	}
	snakey.add(0, newHead);
}

public boolean tailNomming(Location goal) {
	for(int i = 1; i < snakey.size(); i++) {
		if (goal.equals(snakey.get(i))) {
			fill(0);
			return true;
		}
	}
	return false;
}

  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#F0F0F0", "Snakey" });
  }
}
