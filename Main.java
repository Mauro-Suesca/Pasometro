import java.util.Scanner;

public class Main{
    //TODO Opción para consultar papa
    //TODO Opción para consultar pappi al ingresar un semestre (Si no se ingresa el semestre mostrar el pappi del último registrado)
    //TODO Opción para ver qué notas se necesita sacar para obtener una nota final dada por el usuario
    private static Scanner input;
    private static Estudiante perfil_principal;
    public static void main(String[] args){
        input = new Scanner(System.in);
        //TODO Leer el archivo para obtener la información del estudiante
        while(true){
            System.out.println("1. Ingresar o modificar notas");
            System.out.println("2. Consultar notas");
            System.out.println("3. Consultar P.A.P.P.I");
            System.out.println("4. Consultar P.A.P.A");
            System.out.println("\nIngresa el número de la opción que desees tomar ('ENTER' = Salir): ");
            String opcion = input.nextLine().trim();
            if(opcion == "1"){
                opcion_ingresar_modificar_notas();
            }else if(opcion == "2"){

            }else if(opcion == "3"){

            }else if(opcion == "4"){

            }else if(opcion == ""){
                break;
            }else{
                System.out.println("La opción ingresada no es válida, presiona 'ENTER' para volver a intentar");
                input.nextLine();
                continue;
            }
        }
        input.close();
    }
    private static void opcion_ingresar_modificar_notas(){
        //TODO Opción para que se guarde en el "perfil" propio y opción para un perfil volátil o de prueba
        while(true){
            System.out.println("1. Ingresar y guardar notas");
            System.out.println("2. Ingresar notas de prueba (no se guardan) teniendo en cuenta las notas ya registradas");
            System.out.println("3. Ingresar notas de prueba (no se guardan) sin tener en cuenta las notas ya registradas");
            System.out.println("\nIngresa el número de la opción que desees tomar ('ENTER' = Salir): ");
            String opcion = input.nextLine().trim();
            if(opcion == "1"){
                elegir_materias();
            }else if(opcion == "2"){

            }else if(opcion == "3"){

            }else if(opcion == ""){
                break;
            }else{
                System.out.println("La opción ingresada no es válida, presiona 'ENTER' para volver a intentar");
                input.nextLine();
                continue;
            }
        }
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
                if(aux_str == ""){
                    break;
                }else{
                    int opcion = Integer.parseInt(aux_str);
                    if(opcion <= 0 || opcion > materias_arr.length+1){
                        throw new NumberFormatException();
                    }
                    //TODO Continuar, permitir que el usuario ingrese información para la materia correspondiente
                }
            }catch(NumberFormatException e){
                System.out.println("La opción ingresada no es válida, presiona 'ENTER' para volver a intentar");
                input.nextLine();
                continue;
            }
        }
    }
}