#include <SparkFun_ADXL345.h>
#include <math.h>

//communication setup for I2C 
ADXL345 adxl = ADXL345(); 

/* PORT SPECIFICATION */
#define trigPin_1  2
#define trigPin_2  7  
#define echoPin_1  4
#define echoPin_2  8

/* VARIABLE DECLERATION */
long duration, dist_cm, bottomSensor=0, frontSensor=0 ;
double roll = 0.00, pitch = 0.00, yaw =0.00 ;

void setup() {
  
  Serial.begin(9600);

  /*Methods*/
  void readUltraSonicSensors(int trigPin, int echoPin); //To read Ultrasonic sensors
  void sendAndroidValues(); //to send values to android via bluetooth
  void readADXL(); //read accelerometer

  /*SETUP FOR ULTRASONIC SENSORS */
  pinMode(trigPin_1, OUTPUT);
  pinMode(echoPin_1, INPUT);
  
  pinMode(trigPin_2, OUTPUT);
  pinMode(echoPin_2, INPUT);
  
  /* SETUP FOR ADXL */
  adxl.powerOn();
  adxl.setRangeSetting(16);

  //TO MAKE ARDUINO WAIT TILL USER GETS READY
  while(Serial.read()!='C'){}

  //GET INITILAL VALUE FOR CALIBRATION
  
  readUltraSonicSensors(trigPin_1,echoPin_1);
    bottomSensor=dist_cm;
    Serial.print("Cali-");
    Serial.print(bottomSensor);
    Serial.print("~");
}
void loop() {
  readUltraSonicSensors(trigPin_1,echoPin_1);
  bottomSensor=dist_cm;

  /* Average of 5 values to improve accuracy*/
  
  frontSensor=0;
  for(int i=0;i<5;i++)
  {
  readUltraSonicSensors(trigPin_2,echoPin_2);
  frontSensor=frontSensor+dist_cm;
  }
  frontSensor=frontSensor/5;
  
  readADXL();
  sendAndroidValues();
}

void readADXL(){
  
  int x, y, z;
  adxl.readAccel(&x, &y, &z);
  double x_Buff = float(x);
  double y_Buff = float(y);
  double z_Buff = float(z);
  roll = atan2(y_Buff , z_Buff) * 57.3;
  // pitch = atan2((- x_Buff) , sqrt(y_Buff * y_Buff + z_Buff * z_Buff)) * 57.3;
  // pitch = atan2((y_Buff) , sqrt(x_Buff * x_Buff + z_Buff * z_Buff)) * 57.3;
  //  yaw = atan2((sqrt(y_Buff*y_Buff + x_Buff*y_Buff)),(z_Buff))*57.3;
  
}

void readUltraSonicSensors(int trigPin, int echoPin)
{

  // The sensor is triggered by a HIGH pulse of 10 or more microseconds.
  // Give a short LOW pulse beforehand to ensure a clean HIGH pulse:
  delay(60);
  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);

  // Read the signal from the sensor: a HIGH pulse whose
  // duration is the time (in microseconds) from the sending
  // of the ping to the reception of its echo off of an object.
  duration = pulseIn(echoPin, HIGH,20000UL);
  // convert the time into a distance
  if(duration != 0){
  dist_cm = (duration/2)/28.92;
  }
}

/* SEND VALUES TO ANDROID DEVICE */

void sendAndroidValues()
{
  //puts # before the values so our app knows what to do with the data
  Serial.print('#');
  Serial.print(bottomSensor);
  Serial.print("+");
  Serial.print(frontSensor);
  Serial.print("@");
  Serial.print(roll);
  Serial.print('~');
  Serial.println();
}

