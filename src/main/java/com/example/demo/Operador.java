package com.example.demo;

/**
 * clase para la entidad operador quien atiende la llamada si se encuentra disponible
 * 
 * @author ojcarrillo
 *
 */
public class Operador implements Runnable{

	/* duracion de la llamada */
	private Integer tiempo;
	/* estado para determinar si puede atender una llamada */
	private Boolean disponible = true;

	public Operador(Integer tiempo) {
		this.tiempo = tiempo;
	}
	
	public Operador(){
		
	}
	
	public void setTiempo(Integer tiempo){
		this.tiempo = tiempo;
	}

	public void setDisponible(Boolean disponible){
		this.disponible = disponible;
	}
	
	public Boolean getDisponible(){
		return this.disponible;
	}
	
	/* simulacion de la atencion de la llamada durante el tiempo de duracion dado */
	public void dispathCall() {
		try {
			System.out.println("inicia la atencion "+this.toString());
			Thread.sleep(tiempo * 1000);
			System.out.println("fin de la llamada "+this.toString());
			disponible = true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		disponible = false;
		dispathCall();
	}
}
