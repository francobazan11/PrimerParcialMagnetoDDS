package com.example.PrimerParcialMagnetoDDS.service;

import com.example.PrimerParcialMagnetoDDS.Entidades.ADN;
import com.example.PrimerParcialMagnetoDDS.Repositories.ADNRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ADNService {

    @Autowired
    private ADNRepository adnRepo;

    //Metodo para formar una matriz

    public char[][] formarMatriz(String[] strings) {
        int filas = strings.length;// Número de filas es el número de strings
        int columnas = strings[0].length();/ Número de columnas es el tamaño de cada string (todos son del mismo tamaño)

        // Crear la matriz de caracteres
        char[][] matriz = new char[filas][columnas];

        // Llenar la matriz con los caracteres de los strings
        for (int i = 0; i < filas; i++) {
            matriz[i] = strings[i].toCharArray();  // Convertir cada string a un array de caracteres y asignarlo a la fila correspondiente
        }

        return matriz;
    }

    //Metodo para validar que los string del array sean del mismo tamaño y los caracteres pertenezcan al grupo permitido
    public boolean validarArray(String[] strings) {
        if (strings == null || strings.length == 0) {
            return false; // Retornar false si el array es nulo o vacío
        }

        int size = strings[0].length(); // Obtener el tamaño del primer string

        if (size < 4 || strings.length < 4) {
            return false;
        }

        // Recorrer el array y hacer las validaciones
        for (String str : strings) {
            // Verificar si todos los strings tienen el mismo tamaño
            if (str.length() != size) {
                return false; // Si algún string tiene longitud diferente, retornar false
            }

            // Verificar si el string contiene solo caracteres válidos (A, T, C, G)
            for (char c : str.toCharArray()) {
                if (c != 'A' && c != 'T' && c != 'C' && c != 'G') {
                    return false; // Si algún carácter no es válido, retornar false
                }
            }
        }

        return true; // Si todo es valido, retorna true
    }

    //Metodo que verifica si el adn es mutante o no

    public boolean isMutant(String[] dna) {
        if (validarArray(dna)) {

            char[][] matriz = formarMatriz(dna);

            int filas = matriz.length;
            int columnas = matriz[0].length;
            int contador = 0;  // Inicializar el contador de secuencias

            // Recorrer toda la matriz
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    char actual = matriz[i][j];

                    // Condición 1: Vertical (hacia abajo)
                    if (i + 3 < filas && actual == matriz[i+1][j] && actual == matriz[i+2][j] && actual == matriz[i+3][j]) {
                        contador++;
                    }

                    // Condición 2: Horizontal (hacia la derecha)
                    if (j + 3 < columnas && actual == matriz[i][j+1] && actual == matriz[i][j+2] && actual == matriz[i][j+3]) {
                        contador++;
                    }

                    // Condición 3: Diagonal descendente (hacia abajo a la derecha)
                    if (i + 3 < filas && j + 3 < columnas && actual == matriz[i+1][j+1] && actual == matriz[i+2][j+2] && actual == matriz[i+3][j+3]) {
                        contador++;
                    }

                    // Condición 4: Diagonal ascendente (hacia abajo a la izquierda)
                    if (i + 3 < filas && j - 3 >= 0 && actual == matriz[i+1][j-1] && actual == matriz[i+2][j-2] && actual == matriz[i+3][j-3]) {
                        contador++;
                    }
                    if (contador > 1) {
                        guardarADN(dna,true);
                        return true;
                    }
                }
            }
            guardarADN(dna,false);
            return false; // Si no se encuentra ninguna coincidencia

        } else {
            System.out.println("Los strings del arreglo no tienen el mismo tamaño o tienen caracteres invalidos");
            return false;
        }

    }

    private void guardarADN(String[] dna, boolean isMutant) {
        String adnString = Arrays.toString(dna);

        Optional<ADN> adnExistente = adnRepo.findByDna(adnString);
        if (adnExistente.isPresent()) {
            System.out.println("El ADN ya ha sido registrado anteriormente");
            return;
        }
        ADN adn = new ADN();
        adn.setDna(adnString);
        adn.setEsMutante(isMutant);
        adnRepo.save(adn); //Guarda el adn en la base de datos
    }

    //Metodo para calcular y devolver las estadisticas

    public Map<String, Object> obtenerEstadisticas() {
        long countMutants = adnRepo.countMutant();
        long countHumans = adnRepo.countHuman();

        double ratio = (countHumans == 0) ? 0: (double) countMutants / countHumans; /*Si la cantidad de humanos es 0, el ratio es 0
        si no, se calcula */

        Map<String, Object> estadisticas = new HashMap<>();
        estadisticas.put("count_mutant_dna", countMutants);
        estadisticas.put("count_human_dna", countHumans);
        estadisticas.put("ratio", ratio);
        return estadisticas;

    }


}
