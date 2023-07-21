package org.algoritmoGenetico;

public class Turma {
    private String codigo;
    private String professor;
    private String disciplina;
    private String horario;
    private Integer periodo;
    private Integer qtd_aula;
    private Integer qtd_interesse;

    public  Turma(String professor, String disciplina,Integer periodo, Integer qtd_aula, Integer qtd_interesse){
        this.professor = professor;
        this.disciplina = disciplina;
        this.periodo = periodo;
        this.qtd_aula = qtd_aula;
        this.qtd_interesse = qtd_interesse;
    }

    public String toString(){
        if(this.professor == null) return "Sem aula";
        return "Disciplina : "+this.disciplina+"| Professor: "+this.professor+"| Periodo: "+this.periodo+"| Qtd_aula: "+this.qtd_aula+"| qtd_interesse:"+this.qtd_interesse;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public String getProfessor() {
        return professor;
    }

    public Integer getPeriodo() {
        return periodo;
    }

    public Integer getQtd_interesse() {
        return qtd_interesse;
    }
}
