import java.util.ArrayList;

public class Grupo_recursivo{
    private double porcentaje_total;
    private ArrayList<Grupo_de_notas> grupos;

    //Constructores
    Grupo_recursivo(){
        porcentaje_total = 0;
        grupos = new ArrayList<>();
    }

    Grupo_recursivo(double porcentaje_total){
        grupos = new ArrayList<>();
        set_porcentaje_total(porcentaje_total);
    }

    public void add_grupo(Grupo_de_notas grupo){
        this.grupos.add(grupo);
    }

    //Getters    
    public ArrayList<Grupo_de_notas> get_grupos(){
        grupos.sort(null);
        return grupos;
    }

    public double get_porcentaje_total(){
        return porcentaje_total;
    }

    //Setters
    public void set_porcentaje_total(double porcentaje_total){
        this.porcentaje_total = porcentaje_total;
    }
}
