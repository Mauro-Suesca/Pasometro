import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main{
    //TODO Opción para consultar papa
    //TODO Opción para consultar pappi al ingresar un semestre (Si no se ingresa el semestre mostrar el pappi del último registrado)
    //TODO Opción para eliminar una materia (en caso de que sea cancelada)
    //TODO Opción para ver qué notas se necesita sacar para obtener una nota final dada por el usuario (usando el método calcular_notas_necesarias de la clase Materia)

    private static Scanner input;
    private static Estudiante perfil_principal;
    public static void main(String[] args){
        input = new Scanner(System.in);
        //TODO Escribir en el archivo

        try{
            FileReader archivo = new FileReader("./datos.json");
            JSONParser jsonParser = new JSONParser();
            JSONObject estudiante_json = (JSONObject)jsonParser.parse(archivo);
            int creditos_carrera = ((Number)estudiante_json.get("creditos_carrera")).intValue();
            JSONArray materias_json = (JSONArray)estudiante_json.get("materias");
            Materia[] materias_array = new Materia[materias_json.size()];

            for(int i=0; i<materias_json.size(); i++){
                JSONObject materia = (JSONObject)materias_json.get(i);
                String nombre_materia = (String)materia.get("nombre");
                int creditos_materia = ((Number)materia.get("creditos")).intValue();
                int semestre_materia = ((Number)materia.get("semestre")).intValue();
                JSONArray grupos_json = (JSONArray)materia.get("grupos");
                if(grupos_json == null){
                    double nota_final = ((Number)materia.get("nota_final")).doubleValue();
                    materias_array[i] = new Materia(nombre_materia, creditos_materia, semestre_materia, nota_final);
                }else{
                    Grupo_de_notas[] grupos_array = new Grupo_de_notas[grupos_json.size()];
                    for(int j=0; j<grupos_json.size(); j++){
                        JSONObject grupo = (JSONObject)grupos_json.get(j);
                        String nombre_grupo = (String)grupo.get("nombre");
                        int porcentaje_grupo = ((Number)grupo.get("porcentaje")).intValue();
                        JSONArray notas_json = (JSONArray)grupo.get("notas");
                        if(notas_json == null){
                            grupos_array[j] = new Grupo_de_notas(nombre_grupo, porcentaje_grupo);
                        }else{
                            boolean completo_grupo = (boolean)grupo.get("completo");
                            Nota[] notas_array = new Nota[notas_json.size()];
                            for(int k=0; k<notas_json.size(); k++){
                                JSONObject nota = (JSONObject)notas_json.get(k);
                                String nombre_nota = (String)nota.get("nombre");
                                Double valor_nota = (Number)nota.get("valor") != null ? ((Number)nota.get("valor")).doubleValue() : null;
                                if(valor_nota != null){
                                    notas_array[k] = new Nota(nombre_nota, valor_nota);
                                }else{
                                    notas_array[k] = new Nota(nombre_nota);
                                }
                            }
                            grupos_array[j] = new Grupo_de_notas(nombre_grupo, porcentaje_grupo, notas_array);
                            grupos_array[j].set_completo(completo_grupo);
                        }
                    }

                    materias_array[i] = new Materia(nombre_materia, creditos_materia, semestre_materia, grupos_array);
                }
            }

            perfil_principal = new Estudiante(materias_array, creditos_carrera);
        }catch(IOException e){
            //TODO: En caso de que no haya información en el archivo/no exista, un menú para crear el perfil principal
        }catch(ParseException e){
            //Esta excepción ocurre cuando hay un error en el formato del json
        }

        while(true){
            System.out.println("1. Ingresar o modificar notas");
            System.out.println("2. Consultar notas");
            System.out.println("3. Consultar P.A.P.P.I");
            System.out.println("4. Consultar P.A.P.A");
            System.out.println("\nIngresa el número de la opción que desees tomar ('ENTER' = Salir): ");
            String opcion = input.nextLine().trim();
            if(opcion.equals("1")){
                opcion_ingresar_modificar_notas();
            }else if(opcion.equals("2")){

            }else if(opcion.equals("3")){

            }else if(opcion.equals("4")){

            }else if(opcion.equals("")){
                break;
            }else{
                System.out.println("La opción ingresada no es válida, presiona 'ENTER' para volver a intentar");
                input.nextLine();
                continue;
            }
        }
        input.close();
    }

    private static void elegir_materias(){
        while(true){
            Materia[] materias_arr = perfil_principal.get_materias();
            for(int i=0; i<materias_arr.length; i++){
                System.out.println((i+1) + ". " + materias_arr[i].get_nombre());
            }
            System.out.println((materias_arr.length+1) + ". Crear nueva materia");
            System.out.println("\nIngresa el número de la materia cuyas notas quieras acceder ('ENTER' = Salir): ");
            try{
                String aux_str = input.nextLine().trim();
                if(aux_str.equals("")){
                    break;
                }else{
                    int opcion = Integer.parseInt(aux_str);
                    if(opcion <= 0 || opcion > materias_arr.length+1){
                        throw new NumberFormatException();
                    }else if(opcion == materias_arr.length+1){
                        //TODO Que el usuario pueda crear una nueva materia y se guarde en su perfil en el archivo
                    }else{
                        //TODO Que el usuario pueda acceder a la información de una de sus materias y modificar las notas
                    }
                }
            }catch(NumberFormatException e){
                System.out.println("La opción ingresada no es válida, presiona 'ENTER' para volver a intentar");
                input.nextLine();
                continue;
            }
        }
    }

    private static void opcion_ingresar_modificar_notas(){
        //TODO Opción para que se guarde en el "perfil" propio y opción para un perfil volátil o de prueba
        while(true){
            System.out.println("1. Ingresar y guardar notas");
            System.out.println("2. Ingresar notas de prueba (no se guardan) teniendo en cuenta las notas ya registradas");
            System.out.println("3. Ingresar notas de prueba (no se guardan) sin tener en cuenta las notas ya registradas");
            System.out.println("\nIngresa el número de la opción que desees tomar ('ENTER' = Salir): ");
            String opcion = input.nextLine().trim();
            if(opcion.equals("1")){
                elegir_materias();
            }else if(opcion.equals("2")){

            }else if(opcion.equals("3")){

            }else if(opcion.equals("")){
                break;
            }else{
                System.out.println("La opción ingresada no es válida, presiona 'ENTER' para volver a intentar");
                input.nextLine();
                continue;
            }
        }
    }
}