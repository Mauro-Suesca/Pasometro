import java.util.InputMismatchException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Materia implements Comparable<Materia>{
    private String nombre;
    private int creditos;
    private int semestre;
    private double nota_final;
    private Grupo_de_notas[] grupos;

    //Constructores
    Materia(){
        nombre = "";
        creditos = 0;
        semestre = 0;
        nota_final = 0.0;
        grupos = null;
    }
    Materia(String nombre, int creditos, int semestre){
        set_materia(nombre, creditos, semestre, 0.0);
    }
    Materia(String nombre, int creditos, int semestre, double nota_final){
        set_materia(nombre, creditos, semestre, nota_final);
    }
    Materia(String nombre, int creditos, int semestre, Grupo_de_notas[] grupos){
        set_materia(nombre, creditos, semestre, grupos);
    }

    //Setters
    public void set_nombre(String nombre) throws InputMismatchException{
        Pattern whitespace = Pattern.compile("\\s+");
        Matcher empty = whitespace.matcher(nombre);
        if(nombre != "" || !empty.matches()){
            this.nombre = nombre;
        }else{
            throw new InputMismatchException("El nombre de la materia no puede estar vacío");
        }
    }
    public void set_creditos(int creditos) throws InputMismatchException{
        if(creditos > 0){
            this.creditos = creditos;
        }else{
            throw new InputMismatchException("El número de créditos tiene que ser un entero positivo");
        }
    }
    public void set_semestre(int semestre) throws InputMismatchException{
        if(semestre > 0){
            this.semestre = semestre;
        }else{
            throw new InputMismatchException("El semestre en el que se vio la materia tiene que ser un entero positivo");
        }
    }
    public void set_nota_final(double nota_final) throws InputMismatchException{
        if(nota_final >= 0){
            this.nota_final = nota_final;
        }else{
            throw new InputMismatchException("La nota final no puede ser negativa");
        }
    }
    public void set_grupos(Grupo_de_notas[] grupos) throws InputMismatchException{
        double aux = 0.0;
        for(int i=0; i<grupos.length; i++){
            aux += grupos[i].get_porcentaje();
        }
        if(aux == 100.0){
            this.grupos = grupos;
            aux = 0.0;
            for(int i=0; i<grupos.length; i++){
                aux += grupos[i].get_nota_total()*grupos[i].get_porcentaje();
            }
            nota_final = aux / 100;
        }else{
            throw new InputMismatchException("El porcentaje total de las notas no suma 100%");
        }
    }
    public void set_materia(String nombre, int creditos, int semestre){
        set_nombre(nombre);
        set_creditos(creditos);
        set_semestre(semestre);
    }
    public void set_materia(String nombre, int creditos, int semestre, double nota_final){
        set_materia(nombre, creditos, semestre);
        set_nota_final(nota_final);
    }
    public void set_materia(String nombre, int creditos, int semestre, Grupo_de_notas[] grupos){
        set_materia(nombre, creditos, semestre);
        set_grupos(grupos);
    }

    //Getters
    public String get_nombre(){
        return nombre;
    }
    public int get_creditos(){
        return creditos;
    }
    public int get_semestre(){
        return semestre;
    }
    public double get_nota_final(){
        return nota_final;
    }
    public Grupo_de_notas[] get_notas(){
        return grupos;
    }

    @Override public int compareTo(Materia otra){
        if(this.semestre > otra.semestre){
            return 1;
        }else if(this.semestre == otra.semestre){
            if(this.nombre.compareTo(otra.nombre) > 0){
                return 1;
            }else if(this.nombre.equals(otra.nombre)){
                return 0;
            }else{
                return -1;
            }
        }else{
            return -1;
        }
    }
}