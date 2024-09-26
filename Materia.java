import java.util.ArrayList;
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

    private ArrayList<Nota> calcular_notas_grupo(Nota[] notas_grupo, double valor_necesario){
        ArrayList<Nota> respuesta = new ArrayList<>();
        valor_necesario *= notas_grupo.length;
        for(int i=0; i<notas_grupo.length; i++){
            if(notas_grupo[i].get_valor() == null){
                respuesta.add(notas_grupo[i]);
            }else{
                valor_necesario -= notas_grupo[i].get_valor();
            }
        }
        for(int i=0; i<respuesta.size(); i++){
            respuesta.get(i).set_valor(valor_necesario/respuesta.size());
        }
        return respuesta;
    }

    public Nota[] calcular_notas_necesarias(double nota_deseada) throws InputMismatchException{
        //Método para cálcular las notas que necesita el usuario para llegar a un valor determinado en la definitiva:
        /*
            Sin notas faltantes, posiblemente tirar una excepción indicando eso
            Con una nota faltante, se saca el valor necesario.
            Con dos notas faltantes, se pone una de ellas como la nota máxima (5) y se mira qué nota se necesitaría en la otra para llegar al valor deseado (si es menor a 0, se pone 0), ahora, se pone la primera nota como la mínima (0), y se mira qué nota se necesitaría en la otra para llegar al valor deseado (si es mayor a 5, se pone 5), luego, se promedian las dos notas necesarias y esa es la nota que se tomará. Se invierten los roles de las dos notas para obtener el valor necesario en ambas (pero si las dos tienen el mismo porcentaje, se pone el mismo valor en todas: el valor deseado)
            Con tres o más notas faltantes, se agrupan todas las notas faltantes menos una en un solo grupo (asegurándose que las notas con un mismo porcentaje queden agrupadas juntas siempre), cuyo porcentaje es la suma de los porcentajes de las notas que lo conforman, con eso, se hace el procedimiento de dos notas faltantes. Después, se hace el mismo procedimiento con las notas que conformaban al grupo creado anteriormente, hasta que se tenga el valor necesario en todas las notas. (pero si todas tienen el mismo porcentaje, se pone el mismo valor en todas: el valor deseado)
        */
        Nota[] respuesta;
        ArrayList<Nota> respuesta_ArrayList = new ArrayList<>();
        ArrayList<Integer> ind_grupos_faltantes = new ArrayList<>();
        for(int i=0; i<grupos.length; i++){
            if(!grupos[i].get_completo()){
                ind_grupos_faltantes.add(i);
            }
        }
        if(ind_grupos_faltantes.size() != 0){
            if(ind_grupos_faltantes.size() == 1){
                double valor_necesario = nota_deseada*100;
                if(grupos.length > 1){
                    double valor_actual = 0.0;
                    for(int i=0; i<grupos.length; i++){
                        if(grupos[i].get_completo()){
                            valor_actual += grupos[i].get_nota_total()*grupos[i].get_porcentaje();
                        }
                    }
                    valor_necesario -= valor_actual;
                }else{
                    valor_necesario = nota_deseada;
                }

                Nota[] notas_grupo = grupos[ind_grupos_faltantes.get(0)].get_notas();
                respuesta_ArrayList = calcular_notas_grupo(notas_grupo, valor_necesario);

            }else if(ind_grupos_faltantes.size() == 2){
                //TODO Con dos notas faltantes, se pone una de ellas como la nota máxima (5) y se mira qué nota se necesitaría en la otra para llegar al valor deseado (si es menor a 0, se pone 0), ahora, se pone la primera nota como la mínima (0), y se mira qué nota se necesitaría en la otra para llegar al valor deseado (si es mayor a 5, se pone 5), luego, se promedian las dos notas necesarias y esa es la nota que se tomará. Se invierten los roles de las dos notas para obtener el valor necesario en ambas (pero si las dos tienen el mismo porcentaje, se pone el mismo valor en todas: el valor deseado)
            }else{
                //TODO Con tres o más notas faltantes, se agrupan todas las notas faltantes menos una en un solo grupo (asegurándose que las notas con un mismo porcentaje queden agrupadas juntas siempre), cuyo porcentaje es la suma de los porcentajes de las notas que lo conforman, con eso, se hace el procedimiento de dos notas faltantes. Después, se hace el mismo procedimiento con las notas que conformaban al grupo creado anteriormente, hasta que se tenga el valor necesario en todas las notas. (pero si todas tienen el mismo porcentaje, se pone el mismo valor en todas: el valor deseado)
            }
            respuesta = new Nota[respuesta_ArrayList.size()];
            respuesta_ArrayList.sort(null);
            for(int i=0; i<respuesta_ArrayList.size(); i++){
                respuesta[i] = respuesta_ArrayList.get(i);
            }
            return respuesta;
        }else{
            throw new InputMismatchException("La materia " + nombre + " tiene todas sus notas completas");
        }
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

    //Getters
    public int get_creditos(){
        return creditos;
    }

    public String get_nombre(){
        return nombre;
    }

    public double get_nota_final(){
        return nota_final;
    }

    public Grupo_de_notas[] get_notas(){
        return grupos;
    }

    public int get_semestre(){
        return semestre;
    }

    //Setters
    public void set_creditos(int creditos) throws InputMismatchException{
        if(creditos > 0){
            this.creditos = creditos;
        }else{
            throw new InputMismatchException("El número de créditos tiene que ser un entero positivo");
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

    public void set_nombre(String nombre) throws InputMismatchException{
        Pattern whitespace = Pattern.compile("\\s+");
        Matcher empty = whitespace.matcher(nombre);
        if(!nombre.equals("") || !empty.matches()){
            this.nombre = nombre;
        }else{
            throw new InputMismatchException("El nombre de la materia no puede estar vacío");
        }
    }

    public void set_nota_final(double nota_final) throws InputMismatchException{
        if(nota_final >= 0){
            this.nota_final = nota_final;
        }else{
            throw new InputMismatchException("La nota final no puede ser negativa");
        }
    }
    
    public void set_semestre(int semestre) throws InputMismatchException{
        if(semestre > 0){
            this.semestre = semestre;
        }else{
            throw new InputMismatchException("El semestre en el que se vio la materia tiene que ser un entero positivo");
        }
    }
}