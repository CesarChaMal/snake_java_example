//package com.tata.rigo.general;
package com.games;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

public class Snake extends JFrame implements Runnable {


	public static int W = 600;
	public static int H = 400;

	// Tablero
	public static int CV = 15; // Casillas verticales
	public static int CH = 28; // Casillas horizontales
	public static int CT = 20; // Tamaño de las casillas
	public static int CX = 20; // Posición X del comienzo del tablero en la
								// pantalla
	public static int CY = 60; // Posición Y del comienzo del tablero en la
								// pantalla

	// Juego

	public static int juego[][] = new int[CH][CV];
	public static int MAX_LONG_SNAKE = 40;
	public static int snake[][] = new int[MAX_LONG_SNAKE + 1][2];
	public static int snakeLongAct;
	public static int snakeMov = 1;

	// Comprobaciones

	public static boolean gameOver = false;
	public static int puntuacion = 0;

	// Tecla pulsada
	public static int tecla = snakeMov;
	// Retardo

	public static int retardo =150;
	public static String nivel="Medium";

	public void pintarTablero(Graphics g) {

		for (int x = 0; x < CH; x++) {
			for (int y = 0; y < CV; y++) {

				switch (juego[x][y]) {

				case 0: // La casilla está vacía
					g.setColor(Color.white);
					g.fillRect(x * CT + CX, y * CT + CY, CT, CT);
					break;
				case 1: // La casilla es un borde
					g.setColor(Color.black);
					g.fillRect(x * CT + CX, y * CT + CY, CT, CT);
					break;
				}
			}
		}
	}

	public void pintarSerpiente(Graphics g) {

		for (int i = 0; i < snakeLongAct; i++) {

			int x = snake[i][0];
			int y = snake[i][1];

			if (i == 0)
				g.setColor(Color.DARK_GRAY);
			else
				g.setColor(Color.green);

			g.fillRect(x * CT + CX, y * CT + CY, CT, CT);
		}
	}

	public void paint(Graphics g) {

		g.setColor(new Color(242,168,13));
		g.fillRect(0, 0, W, H);
		pintarTablero(g);
		pintarSerpiente(g);
		g.setColor(Color.black);
		g.drawString("Rigo's Snake Game", 240, 40);


		g.drawString("LEVEL: " + nivel, 20, 35);
		g.drawString("SCORE:" + puntuacion, 20, 50);
		g.drawString("Press U to increase and L to decrease the level!!",400,390);

		if (gameOver) {
			g.drawString("GAME OVER!!! Press R to restart",20,380);
		}
	}

	public static void main(String[] args) {

		Snake snk = new Snake();
		crearVentana(snk);

		inicializarJuego();
		inicializarSerpiente();
		ponerManzana();

	}

	public static void crearVentana(Snake snk) {
		snk.setVisible(true);
		snk.setTitle("Rigo's Snake Game");

		snk.setBounds(0, 0, W, H);
	}

	public static void inicializarJuego() {

		// Inicializamos el tablero de juego

		for (int x = 0; x < CH; x++) {
			for (int y = 0; y < CV; y++) {
				juego[x][y] = 0;
			}
		}

		// Fijamos los bordes
		for (int x = 1; x < CH; x++) {
			juego[x][0] = 1;
			juego[x][CV - 1] = 1;
		}
		for (int y = 0; y < CV; y++) {
			juego[0][y] = 1;
			juego[CH - 1][y] = 1;
		}

	}

	public static void inicializarSerpiente() {

		// Situamos la serpiente en su posición inicial

		int mitadVertical = CV / 2;

		snake[0][0] = 4;
		snake[0][1] = mitadVertical;

		snake[1][0] = 3;
		snake[1][1] = mitadVertical;

		snake[2][0] = 2;
		snake[2][1] = mitadVertical;

		snakeLongAct = 3; // Fijamos la longitud inicial

	}

	public Snake() {

		Thread hilo = new Thread(this);
		hilo.start();

		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				tecla = e.getKeyCode();

				// Tenemos que comprobar siempre que no vaya en dirección
				// contraria
				switch (e.getKeyCode()) {
				case 39: { // Derecha
					if (snakeMov != 3)
						snakeMov = 1;
					break;
				}
				case 40: { // Abajo
					if (snakeMov != 4)
						snakeMov = 2;
					break;
				}
				case 37: { // Izquierda
					if (snakeMov != 1)
						snakeMov = 3;
					break;
				}
				case 38: { // Arriba
					if (snakeMov != 2)
						snakeMov = 4;
					break;
				}
				case 'r':
				case 'R': {
					puntuacion = 0;
					snakeMov = 1;
					gameOver=false;
					inicializarJuego();
					inicializarSerpiente();
					ponerManzana();
					break;

				}
				case 'u':
				case 'U':
				{
					puntuacion = 0;
					snakeMov = 1;
					gameOver=false;
					if(nivel.equals("Medium"))
					{
						retardo=retardo-100;
						nivel="Dificult";
					}
					else if(nivel.equals("Easy"))
					{
						retardo=retardo-100;
						nivel="Medium";
					}


//					inicializarJuego();
//					inicializarSerpiente();
//					ponerManzana();
					break;

				}

				case 'l':
				case 'L':
				{
					puntuacion = 0;
					snakeMov = 1;
					gameOver=false;

					if(nivel.equals("Medium"))
					{
						retardo=retardo+100;
						nivel="Easy";
					}
					else if(nivel.equals("Dificult"))
					{
						retardo=retardo+100;
						nivel="Medium";
					}


//					inicializarJuego();
//					inicializarSerpiente();
//					ponerManzana();
					break;
				}

				}

			}
		});
	}

	public void run() {

		while (true) {

			try {
				Thread.sleep(retardo);

			} catch (Exception e) {

			}

			// Comprobar movimiento
			comprobaciones();

			if (!gameOver) {
				movimientoSerpiente();
			}
			repaint();
			// Repintar
		}
	}

	public void comprobaciones() {

		int sx = snake[0][0];
		int sy = snake[0][1];

		switch (snakeMov) {

		case 1:
			sx = snake[0][0] + 1;
			break;
		case 2:
			sy = snake[0][1] + 1;
			break;
		case 3:
			sx = snake[0][0] - 1;
			break;
		case 4:
			sy = snake[0][1] - 1;
			break;
		}

		for (int x = 0; x < CH; x++) {
			for (int y = 0; y < CV; y++) {
				if ((x == sx) && (y == sy)) {
					if ((juego[x][y] == 1)) { // Choca contra la pared
						gameOver = true;
					}
					if (juego[x][y] == 1000) { // Comemos manzana

						juego[x][y] = 0; // Quitamos la manzana

						if (snakeLongAct < MAX_LONG_SNAKE) { // Si es posible
																// crecer,
																// crecemos
							snakeLongAct++;

						}
						puntuacion += 10; // Aumentamos los puntos
						ponerManzana(); // Colocamos una nueva manzana
					}
				}
			}
		}
		for (int i = 0; i < snakeLongAct; i++) {

			if ((sx == snake[i][0]) && (sy == snake[i][1]))
				gameOver = true;
		}
	}

	public void movimientoSerpiente() {

		for (int i = snakeLongAct; i > 0; i--) {
			snake[i][0] = snake[i - 1][0];
			snake[i][1] = snake[i - 1][1];
		}
		switch (snakeMov) {

		case 1:
			snake[0][0] = snake[0][0] + 1;
			break;
		case 2:
			snake[0][1] = snake[0][1] + 1;
			break;
		case 3:
			snake[0][0] = snake[0][0] - 1;
			break;
		case 4:
			snake[0][1] = snake[0][1] - 1;
			break;
		}
	}

	public static void ponerManzana() {

		boolean manzanaPuesta = false;

		while (!manzanaPuesta) {

			// Generamos una posición aleatoria en el tablero

			int xManzana = (int) (Math.random() * CH);
			int yManzana = (int) (Math.random() * CV);

			// Comprobamos que no haya nada en la posición

			if (juego[xManzana][yManzana] == 0) { 


				boolean estaSerpiente = false;

				for (int i = 0; i < snakeLongAct; i++) {

					if ((snake[i][0] == xManzana) && (snake[i][1] == yManzana))
						estaSerpiente = true;
				}
				if (!estaSerpiente) { // Podemos poner la manzana
					juego[xManzana][yManzana] = 1000;
					manzanaPuesta = true;
				}

			}
		}

	}

} 