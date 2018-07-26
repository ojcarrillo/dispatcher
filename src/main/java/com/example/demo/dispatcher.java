package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

/**
 * la aplicacion esta creada en SpringBoot usando maven
 * 
 * @author ojcarrillo
 *
 */
@Component
public class dispatcher {

	/* objetos encargados de atender las llamadas (operadores, supervisor, director) */
	List<Operador> operadores = new ArrayList<>();
	Operador supervisor = new Operador();
	Operador director = new Operador();

	/* cola para manejo de llamadas que no puedan ser atendidas al estar ocupados los operadores, 
	 * se recomienda implementar una cola de mensajes especilizada como rabbitMQ y definir politicas de 
	 * numero de reintentos y de tiempos de expiracion para los mensajes en la cola 
	 * */
	ColaLlamadas llamadasPendientes = new ColaLlamadas();

	/*
	 *  constructor de la clase que inicializa y lanza la prueba
	 * */
	public dispatcher() {
		System.out.println("inicia ejecucion");
		/* inicializa los operadores */
		initOperadores();
		/* hace la prueba */
		try {
			test(10);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * para el test de ejecucion 
	 */
	public void test(Integer cant) {
		Random t = new Random();
		for (int i = 0; i < cant; i++) {
			/* crea el tiempo de duracion de la llamada de 5 a 10 segundos */
			Integer tiempo = t.nextInt(5) + 5;
			System.out.println("tiempo: ----->" + tiempo);
			/* se envia para atencion la llamada a un operador */
			dispatchCall(tiempo);
		}
		/* saca las llamadas pendientes de atencion */
		desEncolar();
	}
	
	/*
	 * cada operador tiene un estado que permite determinar si puede o no atender la llamada, si se encuentran ocupados
	 * es atendida por el siguiente en la jerarquia y asi sucesivamente, de no poder atenderse por algun empleado
	 * se envia a una cola para su reintento cuando halla alguien disponible
	 * 
	 */
	public void dispatchCall(Integer tiempo) {
		for (Operador op : operadores) {
			/* si un operador esta disponible ateiende la llamada */
			if (op.getDisponible()) {
				op.setDisponible(false);
				System.out.println("operador disponible atiende llamada "+op.toString());
				op.setTiempo(tiempo);
				Thread o = new Thread(op);
				o.start();
				return;
			}
		}
		/* si el supervisor esta disponible atiende la llamada */
		if (supervisor.getDisponible()) {
			supervisor.setDisponible(false);
			System.out.println("supervisor disponible atiende llamada");
			supervisor.setTiempo(tiempo);
			Thread o = new Thread(supervisor);
			o.start();
			return;
		}
		/* si el director esta disponible atiende la llamada */
		if (director.getDisponible()) {
			director.setDisponible(false);
			System.out.println("director disponible atiende llamada");
			director.setTiempo(tiempo);
			Thread o = new Thread(director);
			o.start();
			return;
		}
		/* sino hay nadie disponible manda la llamada a una cola */
		System.out.println("encola llamada sin atender --->"+tiempo);
		llamadasPendientes.enColar(tiempo);
	}

	/*
	 * si llamadas en cola, las envia a atender a un operador sino puede ser
	 * atendida, la encola de nuevo
	 */
	public void desEncolar() {
		while (!llamadasPendientes.isEmpty()) {
			System.out.println("desencola llamada ");
			dispatchCall(llamadasPendientes.desEncolar());
		}
	}

	/*
	 * inicia los operadores
	 */
	private void initOperadores() {
		for (int i = 0; i < 10; i++) {
			Operador op = new Operador();
			System.out.println("operador :: "+op.toString());
			operadores.add(op);
		}
	}
}
