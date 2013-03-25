package com.gustavogenovese.calculadora;

public class ExpertoCalculadora {

	private int buffer;
	private int operacion;
	private int bufferOperacion;
	
	public void digitoPresionado(int digito){
		buffer = buffer*10 + digito;
	}
	
	public int getBuffer(){
		return buffer;
	}
	
	public void operacionPresionada(int oper){
		bufferOperacion = buffer;
		buffer = 0;
		operacion = oper;
	}
	
	public void realizarOperacion(){
		switch(operacion){
		case 0:
			buffer = bufferOperacion + buffer;
			break;
			
		case 1:
			buffer = bufferOperacion - buffer;
			break;
			
		case 2:
			buffer = bufferOperacion * buffer;
			break;
			
		case 3:
			buffer = bufferOperacion / buffer;
			break;
		}
	}
	
	public void limpiarBuffer(){
		buffer = 0;
	}
}
