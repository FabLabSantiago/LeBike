/*
Software para el dispositivo smart citizen bike
Basado en el programa para el dispositivo Colloky escrito por fernando@fablabsantiago.org
*/

#include <PinChangeInt.h>
volatile bool interruptCall = true; // flag q cambia cuando se llama el interrupt()

/* CONSTANTES */
int like_pin = 0; // pines de lectura
int dislike_pin = 1;
int valoracion = 0; //
int enviar = 0; //elimuna el problema de doble envio con una apretada

String mensaje = "Error";
String mensaje_positivo = "wiiii :)";
String mensaje_negativo = "buuuu :(";

/* FUNCIONES
****************************************
*/
//void pinChanged(); // interrupt ISR() function


void setup()
{
  Serial.begin( 57600);


  pinMode( like_pin, INPUT_PULLUP);
  pinMode( dislike_pin, INPUT_PULLUP);
//  digitalWrite( button_pin, LOW); // pulldown
//  digitalWrite( transistor_pin, LOW); // pulldown


  // run function when something changes on A0
  attachPinChangeInterrupt( like_pin, pinLike, RISING);
  attachPinChangeInterrupt( dislike_pin, pinDislike, RISING); 

}

void loop()
{
  Bean.sleep(0xFFFFFFFF);
  Bean.setLed(255*valoracion , 255 , 0);  
  Bean.sleep(200);
  Bean.setLed(0, 0, 0);
  if(enviar)
  {
    Serial.println(mensaje);
  }
  mensaje = "Error";
  enviar = 0;


  
  //Serial.println("bleh");
}

// Interrupt service routine (ISR) needs to return
// void and accept no arguments
void pinLike()
{
  // to avoid new interrupt calls
  // during the duration of pinChanged()
  // and the rest of the loop() funcion
  // detachPinChangeInterrupt( button_pin);
  // interruptCall = true;
  mensaje = mensaje_positivo;
	valoracion = 1;
  enviar = 1;

}
void pinDislike(){
  //do something
  valoracion = 0;
  mensaje = mensaje_negativo;
  enviar = 1;

}

