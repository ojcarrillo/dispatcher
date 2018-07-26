package com.example.demo;


import java.util.ArrayList;
import java.util.List;

/**
 * clase para el manejo de la cola de llamadas no atendidas
 * 
 * @author ojcarrillo
 *
 */
public class ColaLlamadas {

	private List<Integer> cola = new ArrayList<>();
	
	public void enColar(Integer t){
		cola.add(t);
	}
	
	public Integer desEncolar(){
		Integer t = cola.get(0);
		cola.remove(0);
		return t;
	}
	
	public Boolean isEmpty(){
		return cola.isEmpty();
	}
}
