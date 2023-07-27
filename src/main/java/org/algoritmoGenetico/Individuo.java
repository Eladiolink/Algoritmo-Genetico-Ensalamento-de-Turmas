package org.algoritmoGenetico;

import org.algoritmoGenetico.utils.PopularTurmas;
import org.algoritmoGenetico.utils.Serialize;
import org.algoritmoGenetico.utils.Verificacoes;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Individuo {
    private String genes = "";
    private int aptidao = 0;

    //gera um indivíduo aleatório
    public Individuo(int numGenes) {
        gerarGeneOpt3(numGenes);
    }
    private void  gerarGeneOpt3(int numGenes){
        genes = "";

        Integer[][] salas = new Integer[PopularTurmas.qtdSalas][20];

        for (Integer[] sala: salas) {
            for(int i=0;i<20;i++){
                sala[i] = 0;
            }
        }

        int[] aulas;

        int parcel = PopularTurmas.quantidadeDisciplina/PopularTurmas.qtdSalas;

        int index = 0;
        int countParcels = 0;
        for (int i=0;i<PopularTurmas.quantidadeDisciplina;i++){

            if(countParcels>parcel){
                index++;
                countParcels = 0;
            }
                aulas = escolherAula(salas[index]);
                salas[index][aulas[0]] = i;
                salas[index][aulas[1]] = i;
                countParcels++;

        }

        // criar gene
        int count = 0;
        for (Integer[] sala: salas) {
            for (int i: sala) {
                String binario = Integer.toBinaryString(i);
                while (binario.length() < PopularTurmas.bits)  binario = "0" + binario;
                genes += binario;
            }
        }

        geraAptidao();
    }

    private int[] escolherAula(Integer[] salas){
        Random r = new Random();
        int[] aulas = new int[2];
        boolean loop = true;
        int num1;
        int num2;
        int diferenca;

        while (loop){
            num1 = r.nextInt(20);
            num2 = r.nextInt(20);

            diferenca = num1-num2;

            if(Math.abs(diferenca)>4 && salas[num1] == 0 && salas[num2]==0){
                aulas[0] = num1;
                aulas[1] = num2;
                loop = false;
            }
        }
        return aulas;
    }

    //cria um indivíduo com os genes definidos
    public Individuo(String genes) {
        this.genes = genes;

        Random r = new Random();
        //se for mutar, cria um gene aleatório
        if (r.nextDouble() <= Algoritimo.getTaxaDeMutacao()) {
            String caracteres = Algoritimo.getCaracteres();
            String geneNovo=genes;

            int valPositionMax = ((PopularTurmas.qtdSalas*20*PopularTurmas.bits)/PopularTurmas.bits)/2;
            int posAleatoria = (r.nextInt(valPositionMax)*PopularTurmas.bits);
            int disciplina = r.nextInt(PopularTurmas.quantidadeDisciplina);

            String disciplinaBinario = Serialize.convertNumToBinare(disciplina);

            String resultado = geneNovo.replaceFirst(disciplinaBinario, "");

            geneNovo = genes.substring(0, posAleatoria) + disciplinaBinario + genes.substring(posAleatoria + PopularTurmas.bits);

            this.genes = geneNovo;
        }
        geraAptidao();
    }

    //gera o valor de aptidão, será calculada pelo número de bits do gene iguais ao da solução
    private void geraAptidao() {
        opt1();
//        opt2();
    }

    private void opt1(){
        Random r = new Random();
        Individuo.this.aptidao = 100000;

        String[] result = Serialize.splitString(genes);

        Map<String, Integer> counter = Serialize.countValues(result);

        for (Map.Entry<String, Integer> entry : counter.entrySet()) {
            if(entry.getValue()>2 && !PopularTurmas.mapa.get(entry.getKey()).getDisciplina().equals("SA")) {
                Individuo.this.aptidao -= 550;
            }
            if(entry.getValue()<2 && !PopularTurmas.mapa.get(entry.getKey()).getDisciplina().equals("SA")) {
                Individuo.this.aptidao -= 550;
            }
        }

        String[] salas = Serialize.dividirSalas(genes);
        String[][] Salas = new String[PopularTurmas.qtdSalas][20];

        int index = 0;

        for (String sala: salas) {
            Salas[index] = Serialize.splitString(sala);

            index++;
        }

        // Penalisa se a turma tem o mesmo professor, periodo e interesse na mesma hora
        String[] professorTurma = new String[PopularTurmas.qtdSalas];

        String[] disciplinaTurma = new String[PopularTurmas.qtdSalas];

        Integer[] periodoTurma = new Integer[PopularTurmas.qtdSalas];

        Integer[] interesseTurma = new Integer[PopularTurmas.qtdSalas];

        Map<String, Integer> disciplinas = new HashMap<>();

//         Verifica se nao falta disciplinas
        for(int i =0;i<20;i++){
            for(int j=0;j<PopularTurmas.qtdSalas;j++)
                if(disciplinas.get(Salas[j][i])==null) disciplinas.put(Salas[j][i],1);
        }

        for (Map.Entry<String, Turma> entry : PopularTurmas.mapa.entrySet()) {
            if (disciplinas.get(entry.getKey()) == null) Individuo.this.aptidao -= 1550;
            if (disciplinas.get(entry.getKey()) == null) Individuo.this.aptidao -= 1550;
            if (disciplinas.get(entry.getKey()) == null) Individuo.this.aptidao -= 1550;
        }

        int gosto;

        for(int i=0;i<20;i++){

            for(int j=0;j<PopularTurmas.qtdSalas;j++){
                professorTurma[j] = PopularTurmas.mapa.get(Salas[j][i]).getProfessor();
            }

            for(int j=0;j<PopularTurmas.qtdSalas;j++){
                periodoTurma[j] = PopularTurmas.mapa.get(Salas[j][i]).getPeriodo();
            }

            for(int j=0;j<PopularTurmas.qtdSalas;j++){
                disciplinaTurma[j] = PopularTurmas.mapa.get(Salas[j][i]).getDisciplina();
            }

            for(int j=0;j<PopularTurmas.qtdSalas;j++){
                interesseTurma[j] = PopularTurmas.mapa.get(Salas[j][i]).getQtd_interesse();
            }

            for(int x=0;x<PopularTurmas.qtdSalas;x++){
                for(int y=0;y<PopularTurmas.qtdSalas;y++){
                    if(x==y) continue;

                    if(professorTurma[x].equals(professorTurma[y]) && (!professorTurma[x].equals("SA") && !professorTurma[y].equals("SA"))) Individuo.this.aptidao -= 3000;;
                }
            }

            for(int x=0;x<PopularTurmas.qtdSalas;x++){
                for(int y=0;y<PopularTurmas.qtdSalas;y++){
                    if(x==y) continue;

                    if(periodoTurma[x].equals(periodoTurma[y]))
                        if(periodoTurma[x] != 0 && periodoTurma[y] !=0) Individuo.this.aptidao -= 20000;
                }
            }

            for(int x=0;x<PopularTurmas.qtdSalas;x++){
                if(Verificacoes.verificaDisciplinaNoMesmoDiaGeneric(disciplinaTurma[x],Salas)) aptidao -= 500;
            }
        }
    }

    private void opt2(){
        Random r = new Random();
        Individuo.this.aptidao = 100000;

        String[] result = Serialize.splitString(genes);

        Map<String, Integer> counter = Serialize.countValues(result);

        for (Map.Entry<String, Integer> entry : counter.entrySet()) {
            if(entry.getValue()>2 && !entry.getKey().equals("0000")) Individuo.this.aptidao -= 550;
            if(entry.getValue()<2 && !entry.getKey().equals("0000")) Individuo.this.aptidao -= 550;
        }

        String[] salas = Serialize.dividirSalas(genes);
        String[][] Salas = new String[3][20];
        Salas[0] = Serialize.splitString(salas[0]);
        Salas[1] = Serialize.splitString(salas[1]);
        Salas[2] = Serialize.splitString(salas[2]);

//        for(int i=0;i<20;i++){
//            if(Salas[0][i].equals(Salas[1][i]) && Salas[0].equals("0000") && Salas[1].equals("0000")) aptidao -= 550;
//            if(Salas[1][i].equals(Salas[2][i]) && Salas[1].equals("0000") && Salas[2].equals("0000")) aptidao -= 550;
//        }

        // Penalisa se a turma tem o mesmo professor, periodo e interesse na mesma hora
        String professorTurma1;
        String professorTurma2;
        String professorTurma3;

        String disciplinaTurma1;
        String disciplinaTurma2;
        String disciplinaTurma3;

        Integer periodoTurma1;
        Integer periodoTurma2;
        Integer periodoTurma3;

        Integer interesseTurma1;
        Integer interesseTurma2;
        Integer interesseTurma3;

        Map<String, Integer> disciplinas = new HashMap<>();

//        System.out.println(Serialize.verificarPorcentagemNulos(Salas[0],.6));

//        if(Serialize.verificarPorcentagemNulos(Salas[0],.9)) aptidao -= 1550;
//        if(Serialize.verificarPorcentagemNulos(Salas[1],.9)) aptidao -= 1550;
//        if(Serialize.verificarPorcentagemNulos(Salas[2],.9)) aptidao -= 1550;

//         Verifica se nao falta disciplinas
        for(int i =0;i<20;i++){
            if(disciplinas.get(Salas[0][i])==null) disciplinas.put(Salas[0][i],1);
            if(disciplinas.get(Salas[1][i])==null) disciplinas.put(Salas[1][i],1);
            if(disciplinas.get(Salas[2][i])==null) disciplinas.put(Salas[2][i],1);
        }

        for (Map.Entry<String, Turma> entry : PopularTurmas.mapa.entrySet()) {
            if (disciplinas.get(entry.getKey()) == null) Individuo.this.aptidao -= 1550;
            if (disciplinas.get(entry.getKey()) == null) Individuo.this.aptidao -= 1550;
            if (disciplinas.get(entry.getKey()) == null) Individuo.this.aptidao -= 1550;
        }

        for(int i=0;i<20;i++){
            professorTurma1 = PopularTurmas.mapa.get(Salas[0][i]).getProfessor();
            professorTurma2 = PopularTurmas.mapa.get(Salas[1][i]).getProfessor();
            professorTurma3 = PopularTurmas.mapa.get(Salas[2][i]).getProfessor();

            periodoTurma1 = PopularTurmas.mapa.get(Salas[0][i]).getPeriodo();
            periodoTurma2 = PopularTurmas.mapa.get(Salas[1][i]).getPeriodo();
            periodoTurma3 = PopularTurmas.mapa.get(Salas[2][i]).getPeriodo();

            disciplinaTurma1 = PopularTurmas.mapa.get(Salas[0][i]).getDisciplina();
            disciplinaTurma2 = PopularTurmas.mapa.get(Salas[1][i]).getDisciplina();
            disciplinaTurma3 = PopularTurmas.mapa.get(Salas[2][i]).getDisciplina();

            interesseTurma1 = PopularTurmas.mapa.get(Salas[0][i]).getQtd_interesse();
            interesseTurma2 = PopularTurmas.mapa.get(Salas[1][i]).getQtd_interesse();
            interesseTurma3 = PopularTurmas.mapa.get(Salas[2][i]).getQtd_interesse();

//            if(interesseTurma1>=9) aptidao -= 550;
//            if(interesseTurma2>=9) aptidao -= 550;
//            if(interesseTurma3>=9) aptidao -= 550;

            if(professorTurma1.equals(professorTurma2) && (!professorTurma1.equals("SA") && !professorTurma2.equals("SA"))) Individuo.this.aptidao -= 3000;
            if(professorTurma2.equals(professorTurma3) && (!professorTurma2.equals("SA") && !professorTurma3.equals("SA"))) Individuo.this.aptidao -= 3000;
            if(professorTurma3.equals(professorTurma1) && (!professorTurma3.equals("SA") && !professorTurma1.equals("SA"))) Individuo.this.aptidao -= 3000;

            if(periodoTurma3.equals(periodoTurma2)) {
                if(periodoTurma3 != 0 && periodoTurma2 !=0) {
//                    System.out.println(PopularTurmas.mapa.get(Salas[1][i]).getDisciplina()+" - "+PopularTurmas.mapa.get(Salas[2][i]).getDisciplina());
                    Individuo.this.aptidao -= 20000;
                }
            }
            if(periodoTurma2.equals(periodoTurma1)) {
                if(periodoTurma2 != 0 && periodoTurma1 !=0) Individuo.this.aptidao -= 20000;
            }
            if(periodoTurma1.equals(periodoTurma3)) {
                if(periodoTurma1 != 0 && periodoTurma3 !=0) Individuo.this.aptidao -= 20000;
            }

//            if(Verificacoes.verificaDisciplinaNoMesmoDia(disciplinaTurma1,Salas[0],Salas[1],Salas[2])) aptidao -= 50;
//            if(Verificacoes.verificaDisciplinaNoMesmoDia(disciplinaTurma2,Salas[0],Salas[1],Salas[2])) aptidao -= 50;
//            if(Verificacoes.verificaDisciplinaNoMesmoDia(disciplinaTurma3,Salas[0],Salas[1],Salas[2])) aptidao -= 50;
        }

//        System.out.println("ERRO Grande "+aptidao);
//       System.out.println("Aptidao Final: "+aptidao);
    }
    public int getAptidao() {
        return aptidao;
    }

    public String getGenes() {
        return genes;
    }
}
