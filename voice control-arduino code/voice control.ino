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
  if(data[5] == 'f'){ 
    digitalWrite(LM1, HIGH);
    digitalWrite(LM2, LOW); 
    btm.println("Recieved"); 
    Serial.println(data);
  }
  else if(data[5] == 'b'){ 
    digitalWrite(LM1, LOW);
    digitalWrite(LM2, HIGH); 
    btm.println("Recieved"); 
    Serial.println(data);
  }    
  else if(data[5] == 'r'){ 
    digitalWrite(LM1, HIGH);
    digitalWrite(LM2, LOW); 
    btm.println("Recieved"); 
    Serial.println(data);
  }    
  else if(data[5] == 'l'){ 
    digitalWrite(LM1, LOW);
    digitalWrite(LM2, HIGH); 
    btm.println("Recieved"); 
    Serial.println(data);
  }
  else if(data[0] == 's'){ 
    digitalWrite(LM1, LOW);
    digitalWrite(LM2, HIGH); 
    btm.println("Recieved"); 
    Serial.println(data);
  }      
 } 
