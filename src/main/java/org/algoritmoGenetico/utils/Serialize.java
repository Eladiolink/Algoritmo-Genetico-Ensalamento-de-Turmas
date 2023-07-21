package org.algoritmoGenetico.utils;

import org.algoritmoGenetico.Turma;

import java.util.HashMap;
import java.util.Map;

public class Serialize {
    public static void main(String genes) {

        String[] result = splitString(genes);

        // Imprime o array resultante
        for (String part : result) {
            Turma t = PopularTurmas.mapa.get(part);
//            System.out.println(t);
        }

        Map<String, Integer> counter = countValues(result);

        // Imprime os resultados
        for (Map.Entry<String, Integer> entry : counter.entrySet()) {
            System.out.println("A Disciplina '" + PopularTurmas.mapa.get(entry.getKey()).getDisciplina() + "' aparece " + entry.getValue() + " vezes.");
        }
    }

    public static String[] splitString(String input) {
        int chunkSize = PopularTurmas.bits;
        int length = input.length();
        int numOfChunks = (int) Math.ceil((double) length / chunkSize);

        String[] result = new String[numOfChunks];

        for (int i = 0; i < numOfChunks; i++) {
            int startIndex = i * chunkSize;
            int endIndex = Math.min(startIndex + chunkSize, length);
            result[i] = input.substring(startIndex, endIndex);
        }

        return result;
    }

    public static String convertNumToBinare(Integer numero){
        String binario = Integer.toBinaryString(numero);

        while (binario.length() < PopularTurmas.bits)  binario = "0" + binario;

        return binario;
    }
    public static String[]dividirSalas (String input) {
        int length = input.length();
        int partSize = length / PopularTurmas.qtdSalas;
        int remainingChars = length % PopularTurmas.qtdSalas;

        String[] result = new String[PopularTurmas.qtdSalas];

        int startIndex = 0;
        int endIndex = 0;
        for (int i = 0; i < PopularTurmas.qtdSalas; i++) {
            endIndex = startIndex + partSize + (remainingChars > 0 ? 1 : 0);
            result[i] = input.substring(startIndex, endIndex);
            startIndex = endIndex;
            remainingChars--;
        }

        return result;
    }

    public static void printSalas(String individuo){
        String[] salas = Serialize.dividirSalas(individuo);

        String[][] Turmas = new String[PopularTurmas.qtdSalas][20];

        int index = 0;

        for(int i=0;i<PopularTurmas.qtdSalas;i++){
            Turmas[i] = Serialize.splitString(salas[i]);
        }

        for(int i=0;i<PopularTurmas.qtdSalas;i++){
            System.out.println("\nSALA "+(i+1));
            String[][] turma= transform(Turmas[i]);
            printTransposedMatrix(turma,PopularTurmas.mapa);
        }
    }

    public static void printTransposedMatrix(String[][] matrix,Map<String, Turma> turmasMap) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        for (int col = 0; col < cols; col++) {
            for (int row = 0; row < rows; row++) {
                System.out.format("%45s",turmasMap.get(matrix[row][col]).getDisciplina());
            }
            System.out.println();
        }
    }
    private static String[][] transform(String[] sala){
        int count = 0;
        String[][] Sala = new String[5][4];
        for (int i=0; i<5;i++){
            for(int j=0;j<4;j++){
                Sala[i][j] = sala[count];
                  count++;
            }
        }
        return Sala;
    }
    public static Map<String, Integer> countValues(String[] array) {
        Map<String, Integer> counter = new HashMap<>();

        for (String str : array) {
            counter.put(str, counter.getOrDefault(str, 0) + 1);
        }

        return counter;
    }

    public static boolean verificarTodosNulos(String[] vetor) {
        for (String elemento : vetor) {
            if (PopularTurmas.mapa.get(elemento).getDisciplina() != null) {
                return false; // Se um elemento não for null, retorna falso imediatamente
            }
        }
        return true; // Se todos os elementos forem null, retorna true
    }

    public static boolean verificarPorcentagemNulos(String[] vetor, double porcentagem) {
        int totalElementos = vetor.length;
        int elementosNulos = 0;

        for (String elemento : vetor) {
            if (PopularTurmas.mapa.get(elemento).getDisciplina() != null) {
                elementosNulos++;
            }
        }

        double porcentagemNulos = (elementosNulos / (double)totalElementos) * 100.0;

        return porcentagemNulos >= porcentagem;
    }
}
