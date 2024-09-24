import java.util.ArrayList;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.InputMismatchException;

public class Estudiante{
    //TODO Que los métodos que modifican algo llamen a un método que guarde en el archivo
    private ArrayList<Materia> materias;
    private int creditos_carrera;
    private double porcentaje_carrera;
    private String papa;

    //Constructores
    Estudiante(){
        materias = new ArrayList<>();
        creditos_carrera = 0;
        porcentaje_carrera = 0.0;
        papa = "0.0";
    }
    Estudiante(Materia[] materias){
        set_materias(materias);
    }
    Estudiante(int creditos_carrera){
        set_creditos_carrera(creditos_carrera);
    }
    Estudiante(Materia[] materias, int creditos_carrera){
        set_estudiante(materias, creditos_carrera);
    }

    //Setters
    public void set_materias(Materia[] materias){
        this.materias = new ArrayList<>();
        for(int i=0; i<materias.length; i++){
            this.materias.add(materias[i]);
        }
        this.materias.sort(null);
        set_porcentaje_carrera();
        set_papa();
    }
    public void set_creditos_carrera(int creditos_carrera)throws InputMismatchException{
        if(creditos_carrera > 0){
            this.creditos_carrera = creditos_carrera;
            set_porcentaje_carrera();
        }else{
            throw new InputMismatchException("El número de créditos tiene que ser un entero positivo");
        }
    }
    private void set_porcentaje_carrera(){
        porcentaje_carrera = 0.0;
        if(creditos_carrera != 0 && materias.size() != 0){
            for(int i=0; i<materias.size(); i++){
                porcentaje_carrera += materias.get(i).get_creditos();
            }
            porcentaje_carrera *= (100/creditos_carrera);
        }
    }
    private void set_papa(){
        DecimalFormat formatter = new DecimalFormat("#.#");
        formatter.setRoundingMode(RoundingMode.HALF_UP);
        int creditos_cursados = 0;
        double papa_num = 0.0;
        for(int i=0; i<materias.size(); i++){
            creditos_cursados += materias.get(i).get_creditos();
            papa += materias.get(i).get_creditos()*materias.get(i).get_nota_final();
        }
        papa_num /= creditos_cursados;
        papa = formatter.format(papa_num);
    }
    public void set_estudiante(Materia[] materias, int creditos_carrera){
        set_materias(materias);
        set_creditos_carrera(creditos_carrera);
    }

    //Getters
    public Materia[] get_materias(){
        Materia[] materias_array = new Materia[materias.size()];
        materias.sort(null);
        for(int i=0; i<materias.size(); i++){
            materias_array[i] = materias.get(i);
        }
        return materias_array;
    }
    public int get_creditos_carrera(){
        return creditos_carrera;
    }
    public double get_porcentaje_carrera(){
        return porcentaje_carrera;
    }
    public String get_papa(){
        return papa;
    }

    //Other methods
    public boolean add_materia(Materia nueva){
        boolean add = true;
        for(int i=0; i<materias.size(); i++){
            if(materias.get(i).get_nombre() == nueva.get_nombre()){
                add = false;
                break;
            }
        }
        if(add){
            materias.add(nueva);
        }
        return add;
    }
    public String calcular_pappi(int semes){
        ArrayList<Materia> materias_actuales = new ArrayList<>();
        boolean existen_materias = false;
        if(semes > 0){
            materias.sort(null);
            for(int i=0; i<materias.size(); i++){
                if(materias.get(i).get_semestre() == semes){
                    materias_actuales.add(materias.get(i));
                    existen_materias = true;
                }else if(materias.get(i).get_semestre() > semes){
                    break;
                }
            }
        }
        if(existen_materias){
            DecimalFormat formatter = new DecimalFormat("#.#");
            formatter.setRoundingMode(RoundingMode.HALF_UP);
            double pappi_num = 0.0;
            int creditos_cursados = 0;
            for(int i=0; i<materias_actuales.size(); i++){
                pappi_num += materias_actuales.get(i).get_creditos()*materias_actuales.get(i).get_nota_final();
                creditos_cursados += materias_actuales.get(i).get_creditos();
            }
            pappi_num /= creditos_cursados;
            return formatter.format(pappi_num);
        }else{
            return "0.0, no hay materias registradas para el semestre ingresado";
        }
    }
    public boolean modificar_materia(Materia nueva){
        boolean success = false;
        for(int i=0; i<materias.size(); i++){
            if(materias.get(i).get_nombre() == nueva.get_nombre()){
                materias.set(i, nueva);
                success = true;
                break;
            }
        }
        return success;
    }
    @Override public String toString(){
        return String.format("Ha completado el %.3f\\% de la carrera, y tiene un PAPA de %s después de haber cursado %d materias", porcentaje_carrera, papa, materias.size());
    }
}