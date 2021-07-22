#include <SoftwareSerial.h> 
int LM1 = 7;
int LM2 = 6;
SoftwareSerial btm(2,3); // rx tx 
int index = 0; 
char data[20]; 
char c; 
boolean flag = false;
void setup() { 
 pinMode(LM1, OUTPUT);
 pinMode(LM2, OUTPUT);
 //digitalWrite(RELAY,HIGH); 
 //digitalWrite(LIGHT,LOW); 
 btm.begin(9600);
 Serial.begin(9600); 
} 
void loop() { 
   if(btm.available() > 0){ 
     while(btm.available() > 0){ 
          c = btm.read(); 
          Serial.println(c);
          delay(10); //Delay required 
          data[index] = c; 
          index++; 
     } 
     data[index] = '\0'; 
     flag = true;   
   }  
   if(flag){ 
     processCommand(); 
     flag = false; 
     index = 0; 
     data[0] = '\0'; 
   } 
} 
void processCommand(){ 
 char command = data[0]; 
 char inst = data[1]; 
 switch(command){ 
   case 'm': 
         if(inst == 'o'){ 
           digitalWrite(LM1,HIGH);
           digitalWrite(LM2, LOW); 
           btm.println("Recieved"); 
           Serial.println(data);
         } 
         break; 
  //  case 'L': 
  //        if(inst == 'Y'){ 
  //          digitalWrite(LIGHT,HIGH); 
  //          btm.println("Light: ON"); 
  //        } 
  //        else if(inst == 'N'){ 
  //          digitalWrite(LIGHT,LOW); 
  //          btm.println("Light: OFF"); 
  //        } 
  //  break; 
 } 
         
         
 } 
