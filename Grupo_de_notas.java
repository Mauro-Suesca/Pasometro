import java.util.InputMismatchException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;

public class Grupo_de_notas{
    private String nombre;
    private double porcentaje;
    private double nota_total;
    private ArrayList<Double> notas;
    
    //Constructores
    Grupo_de_notas(){
        nombre = "";
        porcentaje = 0.0;
        nota_total = 0.0;
        notas = new ArrayList<>();
    }
    Grupo_de_notas(String nombre, double porcentaje){
        set_grupo(nombre, porcentaje);
        nota_total = 0.0;
        notas = new ArrayList<>();
    }
    Grupo_de_notas(String nombre, double porcentaje, double[] notas){
        set_grupo(nombre, porcentaje, notas);
    }

    //Setters
    public void set_nombre(String nombre) throws InputMismatchException{
        Pattern whitespace = Pattern.compile("\\s+");
        Matcher empty = whitespace.matcher(nombre);
        if(nombre != "" || !empty.matches()){
            this.nombre = nombre;
        }else{
            throw new InputMismatchException("El nombre del grupo no puede estar vacío");
        }
    }
    public void set_porcentaje(double porcentaje) throws InputMismatchException{
        if(porcentaje > 0){
            this.porcentaje = porcentaje;
        }else{
            throw new InputMismatchException("El porcentaje tiene que ser mayor a 0");
        }
    }
    public void set_notas(double[] notas){
        this.notas = new ArrayList<>();
        for(int i=0; i<notas.length; i++){
            add_notas(notas[i], true);
        }
        set_nota_total();
    }
    private void set_nota_total(){
        nota_total = 0.0;
        if(notas.size() > 0){
            for(int i=0; i<notas.size(); i++){
                nota_total += notas.get(i);
            }
            nota_total /= notas.size();
        }
    }
    public void set_grupo(String nombre, double porcentaje){
        set_nombre(nombre);
        set_porcentaje(porcentaje);
    }
    public void set_grupo(String nombre, double porcentaje, double[] notas){
        set_grupo(nombre, porcentaje);
        set_notas(notas);
    }

    //Getters
    public String get_nombre(){
        return nombre;
    }
    public double get_porcentaje(){
        return porcentaje;
    }
    public double[] get_notas(){
        double[] notas_array = new double[notas.size()];
        for(int i=0; i<notas.size(); i++){
            notas_array[i] = notas.get(i);
        }
        return notas_array;
    }
    public double get_nota_total(){
        return nota_total;
    }

    //Other Methods
    public void add_notas(double nota, boolean viene_en_grupo) throws InputMismatchException{
        if(nota >= 0 && nota <= 5.0){
            this.notas.add(nota);
            if(!viene_en_grupo){
                set_nota_total();
            }
        }else{
            throw new InputMismatchException("La nota debe ser un valor entre 0.0 y 5.0");
        }
    }
    public void remove_notas(int ... notas_ind){
        for(int i=notas_ind.length-1; i>=0; i--){
            this.notas.remove(notas_ind[i]);
        }
        set_nota_total();
    }
    public void remove_notas(double ... notas){
        for(int i=0; i<notas.length; i++){
            this.notas.remove(notas[i]);
        }
        set_nota_total();
    }
}