import java.util.InputMismatchException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Nota implements Comparable<Nota>{
    private String nombre;
    private Double valor;

    Nota(){
        nombre = "";
        valor = null;
    }

    Nota(String nombre){
        this();
        set_nombre(nombre);
    }

    Nota(String nombre, double valor){
        set_nota(nombre, valor);
    }

    Nota(Nota otra){
        this.nombre = otra.nombre;
        this.valor = otra.valor;
    }
    
    /**
     * Comparador hecho para que al usar .sort en una lista de Notas, las notas que no tienen valor queden de últimas
     */
    @Override public int compareTo(Nota otra){
        if(this.valor == null && otra.valor != null){
            return 1;
        }else if((this.valor != null && otra.valor != null) || (this.valor == null && otra.valor == null)){
            return this.nombre.compareTo(otra.nombre);
        }else{
            return -1;
        }
    }

    @Override public boolean equals(Object otro){
        if(otro != null && otro instanceof Nota){
            return this.nombre.equals(((Nota)otro).nombre);
        }else{
            return false;
        }
    }

    //Getters
    public String get_nombre(){
        return nombre;
    }

    public Double get_valor(){
        return valor;
    }

    //Setters
    public void set_nombre(String nombre){
        Pattern whitespace = Pattern.compile("\\s+");
        Matcher empty = whitespace.matcher(nombre);
        if(!nombre.equals("") || !empty.matches()){
            this.nombre = nombre;
        }else{
            throw new InputMismatchException("El nombre de la nota no puede estar vacío");
        }
    }

    public void set_nota(String nombre, double valor){
        set_nombre(nombre);
        set_valor(valor);
    }

    public void set_valor(double valor){
        if(valor >= 0 && valor <= 5.0){
            this.valor = valor;
        }else{
            throw new InputMismatchException("La nota debe ser un valor entre 0.0 y 5.0");
        }
    }
}