package com.gustavogenovese.snake;

import java.util.LinkedList;
import java.util.List;

public class Vibora {
	private List<Bloque> bloques;

	private Direccion direccion;
	private boolean crecer;

	public Vibora(){
		direccion = Direccion.DERECHA;
		bloques = new LinkedList<Bloque>();
		bloques.add(0, new Bloque(1,1));
		bloques.add(0, new Bloque(2,1));
		bloques.add(0, new Bloque(3,1));
		bloques.add(0, new Bloque(4,1));
		bloques.add(0, new Bloque(5,1));

		crecer = false;
	}

	public void avanzar() {
		switch(direccion){
		case ABAJO:
			bloques.add(0, new Bloque(bloques.get(0).x, bloques.get(0).y + 1));
			break;

		case ARRIBA:
			bloques.add(0, new Bloque(bloques.get(0).x, bloques.get(0).y - 1));
			break;

		case DERECHA:
			bloques.add(0, new Bloque(bloques.get(0).x + 1, bloques.get(0).y));
			break;

		case IZQUIERDA:
			bloques.add(0, new Bloque(bloques.get(0).x -1, bloques.get(0).y));
			break;
		}
		if (!crecer){
			bloques.remove(bloques.size() -1);
		}else{
			crecer = false;
		}
	}

	public void cambiarDireccion(Direccion d) {
		if ((direccion == Direccion.DERECHA && d == Direccion.IZQUIERDA)
				|| (direccion == Direccion.IZQUIERDA && d == Direccion.DERECHA)) {
			return;
		}

		if ((direccion == Direccion.ARRIBA && d == Direccion.ABAJO)
				|| (direccion == Direccion.ABAJO && d == Direccion.ARRIBA)) {
			return;
		}
		this.direccion = d;
	}

	public int tamanio(){
		return bloques.size();
	}

	public Bloque getBloque(int indice){
		return bloques.get(indice);
	}

	public boolean verificarObstaculos(List<Bloque> obstaculos) {
		for (Bloque obstaculo: obstaculos){
			if (bloques.get(0).x == obstaculo.x &&
				 bloques.get(0).y == obstaculo.y){
				return true;
			}
		}
		return false;
	}

	public boolean verificarObjetivo(Bloque objetivo) {
		if (bloques.get(0).x == objetivo.x &&
				 bloques.get(0).y == objetivo.y){
			crecer = true;
			return true;
		}
		return false;
	}
}
