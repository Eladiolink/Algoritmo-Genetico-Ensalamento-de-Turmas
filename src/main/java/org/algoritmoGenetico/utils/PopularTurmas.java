package org.algoritmoGenetico.utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.algoritmoGenetico.Turma;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PopularTurmas {
    public static Map<String, Turma> mapa = new HashMap<>();
    public static int bits;
    public static int qtdSalas = 3;
    public static int quantidadeDisciplina;
    public static void popularTurmas(){

        try {
            String fileName = "disciplinas.csv";
            ClassLoader classLoader = PopularTurmas.class.getClassLoader();
            File file = new File(classLoader.getResource(fileName).toURI());
            CSVReader csvReader = new CSVReader(new FileReader(file.toString()));

            // Ler todas as linhas do CSV
            List<String[]> linhas = csvReader.readAll();

            boolean skip = true;
            quantidadeDisciplina = linhas.size()-1;

            Turma[] turmas= new Turma[PopularTurmas.quantidadeDisciplina];
            int index = 0;
            bits = calculateBitsNeeded(PopularTurmas.quantidadeDisciplina-1);

            for (String[] linha : linhas) {
                if(skip){
                    skip = false;
                    continue;
                }

                turmas[index] = new Turma(linha[0],linha[1],Integer.parseInt(linha[2]),Integer.parseInt(linha[3]),Integer.parseInt(linha[4]));
                index++;
            }
            csvReader.close();

            for(int i = 0; i < PopularTurmas.quantidadeDisciplina; i++){
                String binario = Integer.toBinaryString(i);

                while (binario.length() < bits)  binario = "0" + binario;

                turmas[i].setCodigo(binario);
                mapa.put(binario, turmas[i]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

//        System.out.println("AA: "+mapa.get("1000").getProfessor());
//        System.exit(0);
    }

    public static int calculateBitsNeeded(int n) {
        if (n <= 0) {
            return 1; // Para representar 0, é necessário apenas 1 bit (0)
        }

        int bitsNeeded = 0;
        while (n > 0) {
            bitsNeeded++;
            n >>= 1; // Deslocamento para a direita para verificar o próximo bit
        }

        return bitsNeeded;
    }
}
