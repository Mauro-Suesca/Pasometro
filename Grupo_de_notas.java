import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Cada instancia de la clase es una serie de notas que tienen el mismo peso dentro de un grupo que es un porcentaje determinado de la definitiva (por ejemplo, un grupo de notas podría ser "Tareas", donde todas las tareas pesan lo mismo pero el grupo "Tareas" tiene un porcentaje definido que ocupa)
 */
public class Grupo_de_notas{
    private String nombre;
    private double porcentaje;
    private double nota_total;
    private ArrayList<Nota> notas;
    private boolean completo;
    
    //Constructores
    Grupo_de_notas(){
        nombre = "";
        porcentaje = 0.0;
        nota_total = 0.0;
        notas = new ArrayList<>();
        completo = false;
    }

    Grupo_de_notas(String nombre, double porcentaje){
        set_grupo(nombre, porcentaje);
        nota_total = 0.0;
        notas = new ArrayList<>();
        completo = false;
    }

    Grupo_de_notas(String nombre, double porcentaje, int num_notas){
        set_grupo(nombre, porcentaje);
        nota_total = 0.0;
        notas = new ArrayList<>(num_notas);
        completo = false;

        for(int i=0; i<num_notas; i++){
            add_nota_vacia(false);
        }
    }

    Grupo_de_notas(String nombre, double porcentaje, double[] notas){
        set_grupo(nombre, porcentaje, notas);
    }

    Grupo_de_notas(String nombre, double porcentaje, Nota[] notas){
        set_grupo(nombre, porcentaje, notas);
    }

    public void add_nota_vacia(boolean calcular_nota_total){
        add_nota_vacia(this.nombre + " " + String.valueOf(this.notas.size()+1), calcular_nota_total);
    }

    public void add_nota_vacia(String nombre, boolean calcular_nota_total){
        add_notas(new Nota(nombre), calcular_nota_total);
        completo = false;
    }

    public void add_notas(double valor, boolean calcular_nota_total){
        Nota nota = new Nota(this.nombre + " " + String.valueOf(this.notas.size()+1), valor);
        add_notas(nota, calcular_nota_total);
    }

    public void add_notas(Nota nota, boolean calcular_nota_total) throws InputMismatchException{
        if(this.notas.indexOf(nota) == -1){
            this.notas.add(nota);
            if(calcular_nota_total){
                set_nota_total();
            }
        }else{
            throw new InputMismatchException("No pueden haber dos notas con un mismo nombre en una misma Materia");
        }
    }

    //Getters
    public boolean get_completo(){
        if(!completo){
            notas.sort(null);
            completo = notas.get(notas.size()-1).get_valor() != null;
        }
        return completo;
    }

    public String get_nombre(){
        return nombre;
    }
    
    public double get_nota_total(){
        return nota_total;
    }

    public Nota[] get_notas(){
        Nota[] notas_array = new Nota[notas.size()];
        notas.sort(null);
        for(int i=0; i<notas.size(); i++){
            notas_array[i] = notas.get(i);
        }
        return notas_array;
    }

    public double get_porcentaje(){
        return porcentaje;
    }

    public void remove_notas(int ... notas_ind){
        for(int i=notas_ind.length-1; i>=0; i--){
            this.notas.remove(notas_ind[i]);
        }
        set_nota_total();
    }

    public void remove_notas(String ... notas){
        for(int i=0; i<notas.length; i++){
            for(int j=0; j<this.notas.size(); j++){
                if(this.notas.get(j).get_nombre().equals(notas[i])){
                    this.notas.remove(j);
                    break;
                }
            }
        }
        set_nota_total();
    }

    //Setters
    public void set_grupo(String nombre, double porcentaje){
        set_nombre(nombre);
        set_porcentaje(porcentaje);
        completo = true;
    }

    public void set_grupo(String nombre, double porcentaje, double[] notas){
        set_grupo(nombre, porcentaje);
        set_notas(notas);
    }

    public void set_grupo(String nombre, double porcentaje, Nota[] notas){
        set_grupo(nombre, porcentaje);
        set_notas(notas);
    }

    public void set_nombre(String nombre) throws InputMismatchException{
        Pattern whitespace = Pattern.compile("\\s+");
        Matcher empty = whitespace.matcher(nombre);
        if(!nombre.equals("") || !empty.matches()){
            this.nombre = nombre;
        }else{
            throw new InputMismatchException("El nombre del grupo no puede estar vacío");
        }
    }

    private void set_nota_total(){
        nota_total = 0.0;
        if(notas.size() > 0){
            this.notas.sort(null);
            for(int i=0; i<notas.size(); i++){
                if(notas.get(i).get_valor() != null){
                    nota_total += notas.get(i).get_valor();
                }else{
                    break;
                }
            }
            nota_total /= notas.size();
        }
    }

    public void set_notas(int index, double nota){
        notas.get(index).set_valor(nota);
        set_nota_total();
    }

    public void set_notas(double[] notas){
        this.notas = new ArrayList<>();
        for(int i=0; i<notas.length; i++){
            add_notas(notas[i], false);
        }
        set_nota_total();
    }

    public void set_notas(Nota[] notas){
        this.notas = new ArrayList<>();
        for(int i=0; i<notas.length; i++){
            add_notas(notas[i], false);
        }
        set_nota_total();
    }

    public void set_porcentaje(double porcentaje) throws InputMismatchException{
        if(porcentaje > 0){
            this.porcentaje = porcentaje;
        }else{
            throw new InputMismatchException("El porcentaje tiene que ser mayor a 0");
        }
    }
}