import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class whatsTheDay extends PApplet {

int[][] schoolYear = {
  {9, 9, 9, 9, 0, 4, 1, 2, 3, 9, 9, 4, 1, 2, 3, 4, 9, 9, 1, 2, 3, 4, 1, 9, 9, 2, 3, 4, 1, 2, 9, 9}, // January
  {9, 3, 4, 1, 2, 3, 9, 9, 4, 1, 2, 3, 9, 9, 9, 9, 4, 1, 2, 3, 9, 9, 4, 1, 2, 3, 4, 9, 9, 1},       // February
  {9, 2, 3, 4, 1, 9, 9, 2, 3, 4, 1, 2, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 0, 3, 4, 1}, // March
  {9, 2, 9, 9, 3, 4, 1, 2, 3, 9, 9, 4, 1, 2, 3, 4, 9, 9, 1, 2, 3, 4, 1, 9, 9, 2, 3, 4, 1, 2, 9},    // April
  {9, 9, 3, 4, 1, 2, 3, 9, 9, 4, 1, 2, 3, 4, 9, 9, 1, 2, 3, 4, 1, 9, 9, 9, 2, 3, 4, 1, 9, 9, 2, 3}, // May
  {9, 4, 1, 2, 9, 9, 9, 3, 4, 1, 2, 3, 9, 9, 4, 1, 2, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9},    // June
  {9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9}, // July
  {9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9}, // August
  {9, 9, 9, 9, 9, 9, 9, 9, 9, 0, 1, 2, 9, 9, 3, 4, 1, 2, 3, 9, 9, 4, 1, 2, 3, 5, 9, 9, 4, 1, 2},    // September
  {9, 3, 4, 9, 9, 1, 2, 3, 4, 1, 9, 9, 9, 2, 3, 4, 1, 9, 9, 2, 3, 4, 1, 2, 9, 9, 3, 4, 1, 2, 3, 9}, // October
  {9, 9, 4, 1, 2, 3, 4, 9, 9, 1, 2, 3, 9, 9, 9, 9, 4, 1, 2, 3, 4, 9, 9, 1, 2, 3, 4, 1, 9, 9, 2},    // November
  {9, 3, 4, 1, 2, 9, 9, 3, 4, 1, 2, 3, 9, 9, 4, 1, 2, 3, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9}  // December
}; //same array that is in the gecko firmware that is filled with the day calendar for 2015-2016 school year
int dayNum; //int to store day number
int s; // second
int m; // minute
int h; // hour
int mon; // month
int d; // day
int rectX;
int rectY;
int rectWidth;
int rectHeight;
int monthX; //variables to store values for rectangle being drawn for the user to input the month
int monthY;
int monthWidth;
int monthHeight;
int dayX; //variables to store values for rectangle being drawn for the user to input the day
int dayY;
int dayWidth;
int dayHeight;
int dayInput;
int monthInput;
int interval; //these ints will be used to debounce buttons to allow the user to set month and day values more easily
int now;
int prevMillis;
boolean cButtonPressed; //will record whether the change date button has been pressed or not
boolean triPressed; // records whether a triangular button has been pressed or not
boolean monthSwitch; //records that user is has inputted the month and is ready to input the day
boolean dayTriPressed; //records if the user has pressed the incriment or decriment buttons of the day text box
boolean isFeb; //true if the month is february
boolean month31;
String hol; // holiday
String p1;
String p2;
String p3;
String p4;
String p1Time; //these strings will hold what time each class takes place at
String p2Time;
String p3Time;
String p4Time;
String textValue = "";


public void setup() { //code runs once

noStroke();
rect(0, 0, 400, 700);
rectX = 280;
rectY = 80;
rectWidth = 100;
rectHeight = 30;
monthX = 280;
monthY = 115;
monthWidth = 50;
monthHeight = 20;
monthInput = month();
dayInput = day();
dayX = 280;
dayY = 150;
dayWidth = 50;
dayHeight = 20;
hol = "Holiday!";
p1Time = "  (8:15 AM - 9:15 AM)";
p2Time = "  (9:35 AM - 10:50 AM)";
p3Time = "  (11:15 AM - 12:30 PM)";
p4Time = "  (1:25 PM - 2:40 PM)";
}
public boolean overRect(int x, int y, int width, int height) { // code snippet to form button from processing reference button example
  if (mouseX >= x && mouseX <= x + width && //returns true if mouse is hovering over rectangle specified
      mouseY >= y && mouseY <= y + height) {
    return true;
  }else{
    return false;
  }
}

public boolean buttonPressed(int bx, int by, int bWidth, int bHeight) { //pass button cordinates into function and returns true if the button is pressed or not
  if (mouseX >= bx && mouseX <= bx + width && //returns true if mouse is hovering over button specified
      mouseY >= by && mouseY <= by + height) {
      if (mousePressed) {
        return true;
      }
  }
  return false;
}

public boolean overTri(int x1, int y1, int x2, int y2, int x3, int y3, int px, int py) { //code snippet from GitHub to detect collision of triangle and point
  int a0 = abs((x2-x1)*(y3-y1)-(x3-x1)*(y2-y1)); //https://github.com/jeffThompson/CollisionDetectionFunctionsForProcessing/blob/master/pointTriangle/pointTriangle.pde
  int a1 = abs((x1-px)*(y2-py)-(x2-px)*(y1-py));
  int a2 = abs((x2-px)*(y3-py)-(x3-px)*(y2-py));
  int a3 = abs((x3-px)*(y1-py)-(x1-px)*(y3-py));

  return (abs(a1+a2+a3 - a0) <= 1/256);
}

//boolean triPressed()

public void textBox(int textX, int textY, int textWidth, int textHeight, String defaultText) {
  fill(255);
  stroke(5);
  rect(textX, textY, textWidth, textHeight); //create rectangle to house text box where user will input month and day to get schedule
  fill(150, 150, 150);
  textSize(20);
  text(defaultText, textX + 5, textY + 15);
}

public void button(int bx, int by, int bWidth, int bHeight, String bLabel, int textSize, int textX, int textY) {
  fill(255);
  stroke(5);
  rect(bx, by, bWidth, bHeight);
  fill(0);
  textSize(textSize);
  text(bLabel, textX, textY);
  if (overRect(bx, by, bWidth, bHeight)) {
    fill(209,209,209);
    rect(bx, by, bWidth, bHeight);
    fill(0);
    textSize(textSize);
    text(bLabel, textX, textY);
  }
}

public void changeDate() { // function will be used to create a button allowing the user to type in a date letting them see their Schedule on that day
  //stroke(5);
  //fill(255);
  //rect(rectX, rectY, rectWidth, rectHeight);
  //fill(0);
  //textSize(15);
  //text("Change Date", rectX + 3, rectY + 20);
  button(rectX, rectY, rectWidth, rectHeight, "Change Date", 15, rectX + 3, rectY + 20);
  //button(rectX, rectY, rectWidth, rectHeight, "Change Date", 15);
  if (overRect(rectX, rectY, rectWidth, rectHeight)) {
    fill(209, 209, 209);
    rect(rectX, rectY, rectWidth, rectHeight);
    fill(0);
    textSize(15);
    text("Change Date", rectX + 3, rectY + 20);
  if (mousePressed) {
    cButtonPressed = true;
    }
  }
  if (cButtonPressed) { // have seperate if statement to ensure code continues to run even after the mouse is released
    textBox(monthX, monthY, monthWidth, monthHeight, "mm");
    button(rectX, rectY - 40, rectWidth, rectHeight, "Today", 20, rectX + 20, rectY - 17);
    fill(255);
    stroke(5);
    triangle(monthX + 65, monthY, monthX + 55, monthY + 10, monthX + 75, monthY + 10); //draw month incriment button
    triangle(monthX + 65, monthY + 25, monthX + 55, monthY + 15, monthX + 75, monthY + 15); //draw month decriment button
    //draw today button which will take user to the current date

    if (overTri(monthX + 65, monthY + 25, monthX + 55, monthY + 15, monthX + 75, monthY + 15, mouseX, mouseY)) { //fill the incriment/decriment triangles grey when the mouse hovers over them
      fill(209,209,209);
      triangle(monthX + 65, monthY + 25, monthX + 55, monthY + 15, monthX + 75, monthY + 15);
      if (mousePressed) {
        monthInput--;
        triPressed = true;
      }
    }
    if (overTri(monthX + 65, monthY, monthX + 55, monthY + 10, monthX + 75, monthY + 10, mouseX, mouseY)) {
      fill(209,209,209);
      triangle(monthX + 65, monthY, monthX + 55, monthY + 10, monthX + 75, monthY + 10);
      if (mousePressed) {
        monthInput++;
        triPressed = true;
      }
    }
  }
  if (monthInput > 12) {
    monthInput = 1;
  }else if (monthInput < 1) {
    monthInput = 12;
  }
  if (triPressed) {
    fill(255);
    noStroke();
    rect(monthX + 5, monthY + 3, monthWidth - 10, monthHeight - 3);
    textSize(20);
    fill(0);
    text(monthInput, monthX + 10, monthY + 17);
  }
  if (keyPressed && triPressed) {
    if (key == ENTER) {
      monthSwitch = true;
    }
  }
  if (monthSwitch) {
    textBox(dayX, dayY, dayWidth, dayHeight, "dd");
    stroke(5);
    fill(255); //make triangles white
    triangle(dayX + 65, dayY, dayX + 55, dayY + 10, dayX + 75, dayY + 10); //draw day incriment button
    triangle(dayX + 65, dayY + 25, dayX + 55, dayY + 15, dayX + 75, dayY + 15); //draw day decriment button
    if (overTri(dayX + 65, dayY, dayX + 55, dayY + 10, dayX + 75, dayY + 10, mouseX, mouseY)) { //check if mouse is over incriment button
      fill(209,209,209);
      triangle(dayX + 65, dayY, dayX + 55, dayY + 10, dayX + 75, dayY + 10); //draw grey triangle at the same cordinates to make it look like the triangle is highlighted when the mouse hovers over
      if (mousePressed) {
        dayInput++;
        dayTriPressed = true;
      }
    }
    if (overTri(dayX + 65, dayY + 25, dayX + 55, dayY + 15, dayX + 75, dayY + 15, mouseX, mouseY)) { //do the same for decriment button
      fill(209,209,209);
      triangle(dayX + 65, dayY + 25, dayX + 55, dayY + 15, dayX + 75, dayY + 15);
      if (mousePressed) {
        dayInput--;
        dayTriPressed = true;
      }
    }
  }
  if (dayTriPressed) {
    fill(255);
    noStroke();
    rect(dayX + 5, dayY + 3, dayWidth - 10, dayHeight - 3);
    textSize(20);
    fill(0);
    text(dayInput, dayX + 10, dayY + 17);
    if (keyPressed && dayTriPressed) {
      if (key == ENTER) {
        dayNum = schoolYear[monthInput - 1][dayInput];
      }
    }
  }
}


public void update() { //function responsible for updating p1-p4 strings with the correct class depending on the day number
  if (dayNum == 1) {
    p1 = "Comm. Tech" + p1Time;
    p2 = "Gym" + p2Time;
    p3 = "English" + p3Time;
    p4 = "Instrumental" + p4Time;
  }
  if (dayNum == 2) {
    p1 = "Science" + p1Time;
    p2 = "Software" + p2Time;
    p3 = "French" + p3Time;
    p4 = "Math" + p4Time;
  }
  if (dayNum == 3) {
    p1 = "Instrumental" + p1Time;
    p2 = "Gym" + p2Time;
    p3 = "English" + p3Time;
    p4 = "Comm. Tech" + p4Time;
  }
  if (dayNum == 4) {
    p1 = "Math" + p1Time;
    p2 = "Software" + p2Time;
    p3 = "French" + p3Time;
    p4 = "Science" + p4Time;
  }
  if (dayNum == 9) {
    p1 = "H";
    p2 = "H";
    p3 = "H";
    p4 = "H";
  }
  //update isFeb and month31 booleans here to make sure the month is being counted correctly and the array does not go out of bounds
  //and that each month has the correct amount of days in it
  if (monthInput == 1 || monthInput == 3 || monthInput == 5 || monthInput == 7 ||
      monthInput == 8 || monthInput == 10 || monthInput == 12) {
        month31 = true;
      }
  if (monthInput == 2) {
    isFeb = true;
  }
  if (month31) {
    if (dayInput > 31) {
      monthInput = 1;
    }
    if (dayInput < 1) {
      dayInput = 31;
    }
  }
  if (isFeb) {
    if (dayInput > 28) {
      dayInput = 1;
    }
    if (dayInput < 1) {
      dayInput = 28;
    }
  }
}
public void draw() { //loop
  s = second();  //update time variables to determine the day number and what period it is because draw function is a loop
  m = minute();
  h = hour();
  mon = month();
  d = day();
  background(255); //redraw background to prevent ghosting
  changeDate(); //call changeDate to check if someone pressed the button and change the day of the corresponding schedule
  update(); // call update to keep Schedule up to date
  textSize(32); //add text styling
  fill(0);
  smooth();
  if (!cButtonPressed) {
    dayNum = schoolYear[mon-1][d]; // gets daynumber from array using current month and day and updates it in the loop
    //as long as the change date button has not been pressed
  }
  if (dayNum == 9) { // tell the user it is holiday if dayNum = 9 because 9 represents holidays in the array
    text(hol, 10, 50);
  }else{
    text("Day " + dayNum, 10, 50);
  }
  text("Schedule:", 10, 120);
  //draw scheduele in the remaining canvas space
  textSize(17);
  text("Period 1: " + p1, 10, 200);
  text("Period 2: " + p2, 10, 290);
  text("Period 3: " + p3, 10, 390);
  text("Period 4: " + p4, 10, 490);
}
  public void settings() { 
size(400, 700); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "whatsTheDay" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
