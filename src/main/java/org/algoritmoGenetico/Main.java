package org.algoritmoGenetico;

import org.algoritmoGenetico.utils.PopularTurmas;
import org.algoritmoGenetico.utils.Serialize;
import org.algoritmoGenetico.utils.Verificacoes;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        PopularTurmas.popularTurmas();

        if(!Verificacoes.verificarViabilidade()){
            System.out.println("Quantidades de Aulas maior que a quantidades de Aulas por Sala disponivel!!!");
            System.exit(-1);
        }

        //Define os caracteres existentes
        Algoritimo.setCaracteres("01");
        //taxa de crossover de 60%
        Algoritimo.setTaxaDeCrossover(1);
        //taxa de mutação de 3%
        Algoritimo.setTaxaDeMutacao(0.3);

        //elitismo
        boolean eltismo = true;

        //tamanho da população
        int tamPop = PopularTurmas.qtdSalas*10;

        //numero máximo de gerações
        int numMaxGeracoes = 100000;

        //define o número de genes do indivíduo baseado na solução
        int numGenes = (PopularTurmas.qtdSalas*20*PopularTurmas.bits);

        //cria a primeira população aleatérioa
        Populacao populacao = new Populacao(numGenes, tamPop);

        String inicioPopulacao = populacao.getIndivduo(0).getGenes() + " " + populacao.getIndivduo(0).getAptidao();
        Serialize.printSalas(populacao.getIndivduo(0).getGenes());

        Serialize.main(populacao.getIndivduo(0).getGenes());
        System.out.println(inicioPopulacao);
        System.out.println("\n\n");

        boolean temSolucao = false;
        int geracao = 0;

        //loop até o critério de parada
        while (!temSolucao && geracao < numMaxGeracoes ) {
            geracao++;

            //cria nova populacao
            populacao = Algoritimo.novaGeracao(populacao, eltismo);

            System.out.println("Geração " + geracao + " | Aptidão: " + populacao.getIndivduo(0).getAptidao() + " | Melhor: " + populacao.getIndivduo(0).getGenes());

            //verifica se tem a solucao
            temSolucao = populacao.temSolocao(Algoritimo.getSolucao());
        }

        if (geracao == numMaxGeracoes) {
            System.out.println("Número Maximo de Gerações | " + populacao.getIndivduo(0).getGenes() + " " + populacao.getIndivduo(0).getAptidao());
        }

        Serialize.main(populacao.getIndivduo(0).getGenes());

        System.out.println("\nTem-se: "+ populacao.getIndivduo(0).getGenes().length()+" bits");
        if (temSolucao) {
            System.out.println("Encontrado resultado na geração " + geracao + " | " + populacao.getIndivduo(0).getGenes() + " (Aptidão: " + populacao.getIndivduo(0).getAptidao() + ")");
        }

        Serialize.printSalas(populacao.getIndivduo(0).getGenes());
    }
}