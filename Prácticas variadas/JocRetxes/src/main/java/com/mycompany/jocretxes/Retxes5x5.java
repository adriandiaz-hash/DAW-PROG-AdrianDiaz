/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.jocretxes;

import java.awt.GridLayout;
import javax.swing.*;
import java.awt.event.*;
/**
 *
 * @author avf i dsb
 * 
 * Formulari creat dinàmicament per jugar al joc
 */


public class Retxes5x5 extends JFrame implements ActionListener {

    private final JButton[][] botones;
    private final int filas = 5;
    private final int columnas = 5;
    private int turno = 1; // 1 = jugador 1, 2 = jugador 2

    public Retxes5x5() {
        setTitle("Joc de retxes 5x5");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crear panel principal
        JPanel panelPrincipal = new JPanel(new GridLayout(filas, columnas));

        // Crear botones y agregarlos al panel
        botones = new JButton[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                botones[i][j] = new JButton();
                botones[i][j].addActionListener(this);
                panelPrincipal.add(botones[i][j]);
            }
        }

        // Agregar panel principal al frame
        add(panelPrincipal);

        // Mostrar el frame
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Obtener el botón que se ha pulsado
        JButton botonPulsado = (JButton) e.getSource();

        // Si el botón no está vacío, ignorarlo
        if (!botonPulsado.getText().isEmpty()) {
            return;
        }

        // Marcar el botón con el símbolo del jugador actual
        if (turno == 1) {
            botonPulsado.setText("X");
        } else {
            botonPulsado.setText("O");
        }

        // Comprobar si hay ganador
        if (comprobarGanador()) {
            // Mostrar mensaje de ganador
            JOptionPane.showMessageDialog(this, "El jugador " + turno + " ha ganado!");
            reiniciarJuego();
        }

        // Comprobar empate
        if (estaLleno()) {
            JOptionPane.showMessageDialog(this, "Empate!");
            reiniciarJuego();
        }

        // Cambiar de turno
        turno = turno == 1 ? 2 : 1;
    }

    private boolean comprobarGanador() {
        // Comprobar filas
        for (int i = 0; i < filas; i++) {
            if (botones[i][0].getText().equals(botones[i][1].getText()) &&
                    botones[i][1].getText().equals(botones[i][2].getText()) &&
                    botones[i][2].getText().equals(botones[i][3].getText()) &&
                    botones[i][3].getText().equals(botones[i][4].getText()) &&
                    !botones[i][0].getText().isEmpty()) {
                return true;
            }
        }

        // Comprobar columnas
        for (int j = 0; j < columnas; j++) {
            if (botones[0][j].getText().equals(botones[1][j].getText()) &&
                    botones[1][j].getText().equals(botones[2][j].getText()) &&
                    botones[2][j].getText().equals(botones[3][j].getText()) &&
                    botones[3][j].getText().equals(botones[4][j].getText()) &&
                    !botones[0][j].getText().isEmpty()) {
                return true;
            }
        }

        // Comprobar diagonales
        if (botones[0][0].getText().equals(botones[1][1].getText()) &&
                botones[1][1].getText().equals(botones[2][2].getText()) &&
                botones[2][2].getText().equals(botones[3][3].getText()) &&
                botones[3][3].getText().equals(botones[4][4].getText()) &&
                !botones[0][0].getText().isEmpty()) {
            return true;
        }

       return false;
    }

    private void reiniciarJuego() {
        // Recorrer todos los botones y vaciarlos
    for (int i = 0; i < filas; i++) {
        for (int j = 0; j < columnas; j++) {
            botones[i][j].setText("");
        }
    }

    // Restablecer el turno al jugador 1
    turno = 1;    
}
    
private boolean estaLleno() {
    for (int i = 0; i < filas; i++) {
        for (int j = 0; j < columnas; j++) {
            if (botones[i][j].getText().isEmpty()) {
                return false;
            }
        }
    }
    return true;
}    
}
