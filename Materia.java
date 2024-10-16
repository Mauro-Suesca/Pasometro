import java.util.ArrayList;
import java.util.Arrays;
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

    private ArrayList<Nota> calcular_notas_grupo(Grupo_de_notas grupo, double valor_necesario){
        ArrayList<Nota> respuesta = new ArrayList<>();
        Nota[] notas_grupo = grupo.get_notas();
        for(int i=0; i<notas_grupo.length; i++){
            notas_grupo[i] = new Nota(notas_grupo[i]);
            notas_grupo[i].set_valor(valor_necesario);
            respuesta.add(notas_grupo[i]);
        }
        return respuesta;
    }

    public Nota[] calcular_notas_necesarias(double nota_deseada) throws InputMismatchException{
        //Método para cálcular las notas que necesita el usuario para llegar a un valor determinado en la definitiva:
        /*
            Se calcula al principio la nota que se obtendría si todas las notas vacías fueran 5, para comprobar si es posible alcanzar la nota deseada. Si no es posible o si ya se alcanzó, tira una excepción indicando eso 
            Sin notas faltantes, tira una excepción indicando eso
            
            Después de eso, se llama a la función calcular_notas_recursivas para que haga el trabajo principal de manera recursiva
        */
        Nota[] respuesta;
        ArrayList<Nota> respuesta_ArrayList = new ArrayList<>();
        ArrayList<Grupo_de_notas> grupos_en_cuenta = new ArrayList<>();
        boolean hay_incompletos = false;
        for(int i=0; i<grupos.length; i++){
            if(grupos[i].get_completo()){
                grupos_en_cuenta.add(grupos[i]);
            }else{
                hay_incompletos = true;
                //Se separan las Notas individuales que ya tengan un valor establecido de las que realmente faltan, en Grupo_de_notas aparte, cada uno con su porcentaje correspondiente según el caso (ej: si un grupo tenía 3 notas conocidas y 2 desconocidas y tenía de porcentaje 25%, hacer un grupo para las conocidas con 15% y uno para las desconocidas con 10%), con este método, la nota que se necesite en un grupo se puede dar directamente a todas las Notas del grupo, porque solo habrán Notas faltantes de un mismo porcentaje en un mismo grupo
                Nota[] notas_grupo = grupos[i].get_notas();
                Grupo_de_notas grupo_completo;
                Grupo_de_notas grupo_incompleto;
                int ind_primer_null = -1;
                double porcentaje_completo = 0.0;
                for(int j=0; j<notas_grupo.length; j++){
                    if(notas_grupo[j].get_valor() == null){
                        ind_primer_null = j;
                        break;
                    }
                }
                if(ind_primer_null > 0){
                    porcentaje_completo = ind_primer_null * grupos[i].get_porcentaje() / notas_grupo.length;    //ind_primer_null es igual al número de notas que ya tienen valor en el grupo
                    grupo_completo = new Grupo_de_notas(notas_grupo[0].get_nombre(), porcentaje_completo, Arrays.copyOfRange(notas_grupo, 0, ind_primer_null));
                    grupos_en_cuenta.add(grupo_completo);
                }
                grupo_incompleto = new Grupo_de_notas(notas_grupo[ind_primer_null].get_nombre(), grupos[i].get_porcentaje() - porcentaje_completo, Arrays.copyOfRange(notas_grupo, ind_primer_null, notas_grupo.length));
                grupo_incompleto.set_completo(false);
                grupos_en_cuenta.add(grupo_incompleto);
            }
        }
        if(hay_incompletos){
            double maxima_nota_posible = 0.0;
            int ind_primer_incompleto = -1;
            boolean alcanzado = false;
            grupos_en_cuenta.sort(null);
            for(int i=0; i<grupos_en_cuenta.size(); i++){
                if(grupos_en_cuenta.get(i).get_completo()){
                    maxima_nota_posible += grupos_en_cuenta.get(i).get_nota_total()*grupos_en_cuenta.get(i).get_porcentaje_double();
                }else{
                    if(ind_primer_incompleto == -1){
                        ind_primer_incompleto = i;
                        alcanzado = (maxima_nota_posible / 100) > nota_deseada;
                    }
                    maxima_nota_posible += 5*grupos_en_cuenta.get(i).get_porcentaje_double();
                }
            }
            if(!alcanzado){
                if(maxima_nota_posible/100 > nota_deseada){
                    double valor_necesario = nota_deseada*100;
                    for(int i=0; i<grupos_en_cuenta.size(); i++){
                        if(grupos_en_cuenta.get(i).get_completo()){
                            valor_necesario -= grupos_en_cuenta.get(i).get_nota_total()*grupos_en_cuenta.get(i).get_porcentaje();
                        }else{
                            valor_necesario /= 100;
                            break;
                        }
                    }
                    //TODO Hacer que esta parte llame a la función recursiva que se va a encargar del trabajo (usando la variable valor_necesario)
                    
                }else if(maxima_nota_posible/100 == nota_deseada){
                    for(int i=ind_primer_incompleto; i<grupos_en_cuenta.size(); i++){
                        respuesta_ArrayList.addAll(calcular_notas_grupo(grupos_en_cuenta.get(i), 5.0));
                    }
                }else{
                    throw new InputMismatchException("Es imposible alcanzar " + nota_deseada + " como definitiva en la materia " + nombre + " con las notas obtenidas hasta el momento");
                }
                respuesta = new Nota[respuesta_ArrayList.size()];
                respuesta_ArrayList.sort(null);
                for(int i=0; i<respuesta_ArrayList.size(); i++){
                    respuesta[i] = respuesta_ArrayList.get(i);
                }
                return respuesta;
            }else{
                throw new InputMismatchException("Ya se alcanzó la definitiva deseada en la materia " + nombre);
            }
        }else{
            throw new InputMismatchException("La materia " + nombre + " tiene todas sus notas completas");
        }
    }

    private ArrayList<Nota> calcular_notas_recursivas(double nota_deseada, Grupo_recursivo primer_grupo, Grupo_recursivo segundo_grupo){
        //TODO Implementar función calcular_notas_recursivas
        /*
            Con una nota faltante, se saca el valor necesario.
            Con dos notas faltantes, se pone una de ellas como la nota máxima (5) y se mira qué nota se necesitaría en la otra para llegar al valor deseado (si es menor a 0, se pone 0), ahora, se pone la primera nota como la mínima (0), y se mira qué nota se necesitaría en la otra para llegar al valor deseado (si es mayor a 5, se pone 5), luego, se promedian las dos notas necesarias y esa es la nota que se tomará. Se invierten los roles de las dos notas para obtener el valor necesario en ambas (pero si las dos tienen el mismo porcentaje, se pone el mismo valor en todas: el valor deseado)
            Con tres o más notas faltantes, se agrupan todas las notas faltantes menos una en un solo grupo (asegurándose que las notas con un mismo porcentaje queden agrupadas juntas siempre, y que se agrupen las notas de mayor porcentaje primero), cuyo porcentaje es la suma de los porcentajes de las notas que lo conforman, con eso, se hace el procedimiento de dos notas faltantes. Después, se hace el mismo procedimiento con las notas que conformaban al grupo creado anteriormente, hasta que se tenga el valor necesario en todas las notas. (pero si todas tienen el mismo porcentaje, se pone el mismo valor en todas: el valor deseado)
         */


        /*
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
        */
        if(grupos_en_cuenta.size() == 1){
            
            Nota[] notas_grupo = grupos[grupos_en_cuenta.get(0)].get_notas();
            respuesta_ArrayList = calcular_notas_grupo(notas_grupo, valor_necesario);

        }else if(grupos_en_cuenta.size() == 2){
            //TODO Con dos notas faltantes, se pone una de ellas como la nota máxima (5) y se mira qué nota se necesitaría en la otra para llegar al valor deseado (si es menor a 0, se pone 0), ahora, se pone la primera nota como la mínima (0), y se mira qué nota se necesitaría en la otra para llegar al valor deseado (si es mayor a 5, se pone 5), luego, se promedian las dos notas necesarias y esa es la nota que se tomará. Se invierten los roles de las dos notas para obtener el valor necesario en ambas (pero si las dos tienen el mismo porcentaje, se pone el mismo valor en todas: el valor deseado)
        }else{
            //TODO Con tres o más notas faltantes, se agrupan todas las notas faltantes menos una en un solo grupo (asegurándose que las notas con un mismo porcentaje queden agrupadas juntas siempre, y que se agrupen las notas de mayor porcentaje primero), cuyo porcentaje es la suma de los porcentajes de las notas que lo conforman, con eso, se hace el procedimiento de dos notas faltantes. Después, se hace el mismo procedimiento con las notas que conformaban al grupo creado anteriormente, hasta que se tenga el valor necesario en todas las notas. (pero si todas tienen el mismo porcentaje, se pone el mismo valor en todas: el valor deseado)
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