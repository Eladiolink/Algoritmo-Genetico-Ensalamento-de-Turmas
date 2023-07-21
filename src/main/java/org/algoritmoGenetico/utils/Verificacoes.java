package org.algoritmoGenetico.utils;

public class Verificacoes {

    public static boolean verificarPeriodo(Integer periodo1,Integer periodo2){
        if(periodo1.equals(0) || periodo2.equals(0)) return false;

        if(periodo1.equals(periodo2)) return true;

        return false;
    }

    public static boolean verificaDisciplinaNoMesmoDia(String disciplina, String[] diaSala1, String[] diaSala2, String[] diaSala3){
        if(disciplina.equals("SA")) return false;
        int soma = 0;
        for (String turma: diaSala1) {
            if (disciplina.equals(PopularTurmas.mapa.get(turma).getDisciplina())) {
                soma += 1;
                break;
            }
        }
        for (String turma: diaSala2){
            if (disciplina.equals(PopularTurmas.mapa.get(turma).getDisciplina())) {
                soma += 1;
                break;
            }
        }
        for (String turma: diaSala3){
            if (disciplina.equals(PopularTurmas.mapa.get(turma).getDisciplina())) {
                soma += 1;
                break;
            }
        }

        if(soma>=2) return true;

        return false;
    }

    public static boolean verificaDisciplinaNoMesmoDiaGeneric(String disciplina, String[][] Salas){
        if(disciplina.equals("SA")) return false;
        int soma = 0;

        int dados =0;
        for (String[] sala: Salas) {
            for (String salaCode: sala) {
//                System.out.println(salaCode+" - "+PopularTurmas.mapa.get(salaCode));
                dados = Integer.parseInt(salaCode, 2);

                if(dados>=PopularTurmas.quantidadeDisciplina){
                    System.out.println("ERRO: "+dados);
//                    System.exit(0);
                }

                if (disciplina.equals(PopularTurmas.mapa.get(salaCode).getDisciplina())) {
                    soma += 1;
                    break;
                }
            }
        }

        if(soma>=2) return true;

        return false;
    }

    public static boolean verificarGene(String gene){

        int step = PopularTurmas.bits;
        int length = gene.length();

        for (int i = 0; i < length; i += step) {
            // Verifica se ainda há 5 caracteres a partir da posição atual
            if (i + step <= length) {
                String subString = gene.substring(i, i + step);

                long numeroLongo = Long.parseLong(subString, 2);

                if (numeroLongo >= PopularTurmas.quantidadeDisciplina) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean verificarViabilidade(){
        int aulasTotal = PopularTurmas.qtdSalas*20;
        if(aulasTotal< (PopularTurmas.quantidadeDisciplina*2))
            return false;
        return true;
    }
}
