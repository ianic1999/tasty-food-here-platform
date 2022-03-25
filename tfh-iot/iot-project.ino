
#include <string.h>
#include <WiFiEsp.h>

#ifndef HAVE_HWSERIAL1
#include "SoftwareSerial.h"
SoftwareSerial Serial1(6, 7); // RX, TX
#endif

char ssid[] = "StarNet - pavel95";
char pass[] = "485754433AA9EBA6";
int status = WL_IDLE_STATUS;

IPAddress server(192,168,100,7);
IPAddress mask(255,255,255,0);
IPAddress gateway(192,168,100,1);

const int RED_PIN = 11;
const int GREEN_PIN = 10;
const int BLUE_PIN = 9;
const int BUTTON_PIN = 2;
const int DISPLAY_TIME = 1000;
const int CALL_INACTIVITY_TIME = 10000; 

WiFiEspClient client;

void setup() 
{
  Serial.begin(9600);
  Serial1.begin(9600);
  WiFi.init(&Serial1);
  
  if (WiFi.status() == WL_NO_SHIELD) {
    Serial.println("WiFi shield not present");
    // don't continue
    while (true);
  }
  
  while ( status != WL_CONNECTED) {
    Serial.print("Attempting to connect to WPA SSID: ");
    Serial.println(ssid);
    status = WiFi.begin(ssid, pass);
  }
  
  Serial.println("You're connected to the network");
  Serial.println();
  
  pinMode(RED_PIN, OUTPUT);
  pinMode(GREEN_PIN, OUTPUT);
  pinMode(BLUE_PIN, OUTPUT);
  pinMode(BUTTON_PIN, INPUT);
}

unsigned int lastLedOnTime = 0 - DISPLAY_TIME;
bool isLedOn = false;

void loop()
{
    static unsigned int lastButtonClickTimestamp = 0 - CALL_INACTIVITY_TIME;
    unsigned int currentTime = millis();
    
    if((isLedOn && currentTime - lastLedOnTime > DISPLAY_TIME) || !isLedOn) {
      if(isButtonClicked())
      {
        if (currentTime - lastButtonClickTimestamp > CALL_INACTIVITY_TIME) {
          blueLed();
          lastButtonClickTimestamp = currentTime;
          callWaiter();
        } else {
          redLed();
        }
        isLedOn = true;
        lastLedOnTime = millis();
      }
      else
      {
          digitalWrite(BLUE_PIN, LOW);
          digitalWrite(RED_PIN, LOW);
          digitalWrite(GREEN_PIN, LOW);
          isLedOn = false;
      }
    }
}

static void blueLed() {
  digitalWrite(BLUE_PIN, HIGH);
  digitalWrite(RED_PIN, LOW);
  digitalWrite(GREEN_PIN, LOW);
}

static void redLed() {
  digitalWrite(BLUE_PIN, LOW);
  digitalWrite(RED_PIN, HIGH);
  digitalWrite(GREEN_PIN, LOW);
}

static void greenLed() {
  digitalWrite(BLUE_PIN, LOW);
  digitalWrite(RED_PIN, LOW);
  digitalWrite(GREEN_PIN, HIGH);
}

static bool isButtonClicked() {
  return digitalRead(BUTTON_PIN) == HIGH;
}

static void callWaiter() {
  client.stop();
  if(client.connect(server, 8081)) {
    Serial.println("Client connected.."); 
    client.print("POST /api/arduino/call");
    client.println(" HTTP/1.1");
    client.println("Host: 192.168.100.7:8081");
    client.println("Connection: close");
    client.println();
    
    greenLed();
    isLedOn = true;
    lastLedOnTime = millis();
  }
}