import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main{

    //TODO Opción para eliminar una materia (en caso de que sea cancelada)
    //TODO Opción para ver qué notas se necesita sacar para obtener una nota final dada por el usuario (usando el método calcular_notas_necesarias de la clase Materia)
    /*
        Python code
        def rgb_color(r, g, b):     #Al darle como parámetros un código de color rgb, cambia el texto al color correspondiente
        return f'{ESC}[38;2;{r};{g};{b}m'
    */

    private static Scanner input;
    private static Estudiante perfil_principal;
    private static String archivo = "./datos.json";
    private static final String ESC = "\u001B";
    public static void main(String[] args){
        input = new Scanner(System.in);
        if(!leer_archivo()){
            while(true){
                try{
                    clear_screen();
                    System.out.println("Parece que es la primera vez que entras a este programa (si crees que es un error, comunícate con el desarrollador)");
                    System.out.println("Por favor ingresa la cantidad de créditos que hay en tu carrera: ");
                    int creditos = input.nextInt();
                    perfil_principal = new Estudiante(creditos);
                    actualizar_archivo();
                }catch(InputMismatchException e){
                    System.out.println("La cantidad de créditos tiene que ser un número entero positivo, presiona 'ENTER' para volver a intentar");
                    input.nextLine();
                    continue;
                }
            }
        }
        
        while(true){
            clear_screen();
            boolean hay_materias = perfil_principal.get_materias().length > 0;
            System.out.println("1. Ingresar o modificar notas");
            if(hay_materias){
                System.out.println("2. Consultar notas");
                System.out.println("3. Consultar P.A.P.P.I");
                System.out.println("4. Consultar P.A.P.A");
            }
            System.out.println("\nIngresa el número de la opción que desees tomar ('ENTER' = Salir): ");
            String opcion = input.nextLine().trim();
            if(opcion.equals("")){
                actualizar_archivo();
                break;
            }else if(opcion.equals("1")){
                opcion_ingresar_modificar_notas();
            }else if(opcion.equals("2") && hay_materias){

            }else if(opcion.equals("3") && hay_materias){
                opcion_obtener_promedios(true);
            }else if(opcion.equals("4") && hay_materias){
                opcion_obtener_promedios(false);
            }else{
                System.out.println("La opción ingresada no es válida, presiona 'ENTER' para volver a intentar");
                input.nextLine();
                continue;
            }
        }
        input.close();
    }

    /**
     * Este método toma los datos del perfil principal y los guarda en el archivo para poder usarlos luego
     * @return true o false dependiendo de si se pudo actualizar los datos en el archivo correctamente
     */
    @SuppressWarnings("unchecked")
    public static boolean actualizar_archivo(){
        JSONObject estudiante_json = new JSONObject();
        estudiante_json.put("creditos_carrera", perfil_principal.get_creditos_carrera());
        JSONArray materias_json = new JSONArray();
        Materia[] materias_array = perfil_principal.get_materias();

        for(int i=0; i<materias_array.length; i++){
            JSONObject materia = new JSONObject();
            materia.put("nombre", materias_array[i].get_nombre());
            materia.put("creditos", materias_array[i].get_creditos());
            materia.put("semestre", materias_array[i].get_semestre());
            Grupo_de_notas[] grupos_array = materias_array[i].get_grupos();

            if(grupos_array.length > 0){
                JSONArray grupos_json = new JSONArray();
                for(int j=0; j<grupos_array.length; j++){
                    JSONObject grupo = new JSONObject();
                    grupo.put("nombre", grupos_array[i].get_nombre());
                    grupo.put("porcentaje", grupos_array[i].get_porcentaje());
                    Nota[] notas_array = grupos_array[i].get_notas();
                    
                    if(notas_array.length > 0){
                        grupo.put("completo", grupos_array[i].get_completo());
                        JSONArray notas_json = new JSONArray();
                        for(int k=0; k<notas_array.length; k++){
                            JSONObject nota = new JSONObject();
                            nota.put("nombre", notas_array[i].get_nombre());
                            nota.put("valor", notas_array[i].get_valor());

                            notas_json.add(nota);
                        }
                        
                        grupo.put("notas", notas_json);
                    }
                    
                    grupos_json.add(grupo);
                }
                materia.put("grupos", grupos_json);
            }else{
                materia.put("nota_final", materias_array[i].get_nota_final());
            }

            materias_json.add(materia);
        }

        estudiante_json.put("materias", materias_json);

        try{
            FileWriter writer = new FileWriter(archivo);
            writer.write(estudiante_json.toJSONString());
            writer.flush();
            writer.close();

            return true;
        }catch(IOException e){
            return false;
        }
    }
    
    private static void clear_screen(){
        System.out.print(ESC + "[3J" + ESC + "[2J" + ESC + "[H" + ESC + "[0m");
        System.out.flush();
    }

    private static void elegir_materias(){
        while(true){
            Materia[] materias_arr = perfil_principal.get_materias();
            clear_screen();
            for(int i=0; i<materias_arr.length; i++){
                System.out.println((i+1) + ". " + materias_arr[i].get_nombre());
            }
            System.out.println((materias_arr.length+1) + ". Crear nueva materia");
            System.out.println("\nIngresa el número de la materia cuyas notas quieras acceder ('ENTER' = Guardar y salir): ");
            try{
                String aux_str = input.nextLine().trim();
                if(aux_str.equals("")){
                    actualizar_archivo();
                    break;
                }else{
                    int opcion = Integer.parseInt(aux_str);
                    if(opcion <= 0 || opcion > materias_arr.length+1){
                        throw new NumberFormatException();
                    }else if(opcion == materias_arr.length+1){
                        //TODO Que el usuario pueda crear una nueva materia y se guarde en su perfil en el archivo
                        while(true){
                            clear_screen();
                            Materia nueva;
                            System.out.println("Ingresa el nombre de la materia: ");
                            String nombre = input.nextLine().trim(), aux;
                            int creditos, semestre;
                            while(true){
                                try{
                                    System.out.println("Ingresa el número de créditos de la materia: ");
                                    aux = input.nextLine().trim();
                                    creditos = Integer.parseInt(aux);
                                    //TODO Semestre y opción de ingresar nota final
                                }catch(NumberFormatException e){
                                    System.out.println("El valor ingresado no es un número entero, presiona 'ENTER' para volver a intentar");
                                    input.nextLine();
                                    continue;
                                }
                                try{

                                }catch(InputMismatchException e){
                                    //TODO Crear materia
                                }
                                break;
                            }

                            if(perfil_principal.add_materia(nueva)){
                                break;
                            }else{

                            }
                        }
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
            clear_screen();
            boolean hay_materias = perfil_principal.get_materias().length > 0;
            System.out.println("1. Ingresar y guardar notas");
            if(hay_materias){
                System.out.println("2. Ingresar notas de prueba (no se guardan) teniendo en cuenta las notas ya registradas");
                System.out.println("3. Ingresar notas de prueba (no se guardan) sin tener en cuenta las notas ya registradas");
            }
            System.out.println("\nIngresa el número de la opción que desees tomar ('ENTER' = Salir): ");
            String opcion = input.nextLine().trim();
            if(opcion.equals("")){
                break;
            }else if(opcion.equals("1")){
                elegir_materias();
            }else if(opcion.equals("2") && hay_materias){

            }else if(opcion.equals("3") && hay_materias){

            }else{
                System.out.println("La opción ingresada no es válida, presiona 'ENTER' para volver a intentar");
                input.nextLine();
                continue;
            }
        }
    }

    private static void opcion_obtener_promedios(boolean pappi){
        if(pappi){
            Materia[] materias = perfil_principal.get_materias();
            int semestre_actual = materias[materias.length-1].get_semestre();
            while(true){
                clear_screen();
                System.out.println("Ingresa el número del semestre cuyo P.A.P.P.I. quieres consultar ('ENTER' = semestre actual): ");
                String opcion_str = input.nextLine().trim();
                int opcion;
                if(!opcion_str.equals("")){
                    try{
                        opcion = Integer.parseInt(opcion_str);
                        if(opcion <= 0 || opcion > semestre_actual) throw new NumberFormatException();
                    }catch(NumberFormatException e){
                        if(semestre_actual > 1){
                            System.out.println("El valor ingresado debe ser un número entre 1 y " + semestre_actual + ", presiona 'ENTER' para volver a intentar");
                        }else{
                            System.out.println("El valor ingresado no corresponde a un semestre cursado, presiona 'ENTER' para volver a intentar");
                        }
                        input.nextLine();
                        continue;
                    }
                }else{
                    opcion = semestre_actual;
                }
                clear_screen();
                System.out.println("Tu P.A.P.P.I. en el semestre " + (opcion == semestre_actual ? "actual es: " : opcion + " fue: ") + perfil_principal.calcular_pappi(opcion));
                input.nextLine();
                break;
            }
        }else{
            clear_screen();
            System.out.println("Tu P.A.P.A hasta este momento es de: " + perfil_principal.get_papa());
            input.nextLine();
        }
    }

    private static boolean leer_archivo(){
        try{
            FileReader reader = new FileReader(archivo);
            JSONParser jsonParser = new JSONParser();
            JSONObject estudiante_json = (JSONObject)jsonParser.parse(reader);
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

            return true;
        }catch(IOException | ParseException e){
            return false;
        }
    }
}