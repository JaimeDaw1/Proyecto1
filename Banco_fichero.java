
package banco_fichero;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class Banco_fichero {
    static ArrayList<Cuentas> listado = new ArrayList<>();
    static ArrayList<Cuentas> movimientos = new ArrayList<>();
    static Scanner sc = new Scanner (System.in);
    static double cant;
    static String cadena;
    static String opera=null;

    public static void agregarCuenta(){
        String cantidad="";
        String num;
        boolean bandera=false;
        
        System.out.print("Introduce número de cuenta: ");
        
        do{     
        num=sc.next();
        
            for(int i=0;i<listado.size();i++){
                if(num.equals(listado.get(i).getNumCuenta())){
                    System.out.println("Este número de cuenta ya existe. Introduce otro");
                    bandera=true;
                }else
                    bandera=false;      
            }
        }while(bandera==true);
        do{
            try{
        System.out.print("Introduce saldo: ");
        cantidad=sc.next();
        cant=Double.parseDouble(cantidad);
        }catch(NumberFormatException ex){
                System.out.println("La cantidad introducida no es un número");
        }        
            }while(cant<1);
        
        listado.add(new Cuentas(num,cantidad));
        añadirFichero();
    }
    
    public static void añadirFichero(){
      int dia = LocalDate.now().getDayOfMonth();
      int mes = LocalDate.now().getMonthValue();
      int año = LocalDate.now().getYear();
      
        String FechaAhora= String.valueOf(dia)+"_"+String.valueOf(mes)+"_"+String.valueOf(año);

  
       File directorio = new File("historico.dat");
 
       String rutaAbs = directorio.getAbsolutePath();
       
       int contador=rutaAbs.indexOf("historico.dat");
       
       String nuevaRuta = rutaAbs.substring(0, contador)+ FechaAhora+ "/";
       
       File ruta = new File(nuevaRuta);
       
       if(ruta.mkdir()){
           System.out.println("Directorio creado");
       }else{
           System.out.println("ERROR");
       }
  
        try {
            BufferedWriter bw = new BufferedWriter (new FileWriter(nuevaRuta+"historico.dat"));
            for(Cuentas list: listado){
                bw.write(list.getNumCuenta() + ", ");
                bw.write(list.getSaldo());
                bw.write("\n");
            }
  
    
            bw.close();
        } catch (IOException ex) {
            System.out.println("ERROR :(");        }
    
    }
    
    public static void ingresar() throws IOException{
        opera="suma";
        double ingresar;
        String cuent;
        String ingreso="";
        boolean bander=false;
        
        do{
         System.out.print("Introduce cuenta sobre la que desea operar: ");
         cuent=sc.next();
        for(int i=0;i<listado.size();i++){
            if(cuent.equals(listado.get(i).getNumCuenta())){
                bander=true;
                break;
            }else               
                bander=false;
        }
    }while(bander==false);
        BufferedWriter bw1 = new BufferedWriter (new FileWriter("movimientos.dat"));
        try{
        System.out.print("Introduce cantidad a ingresar: ");
        ingreso=sc.next();
         ingresar=Double.parseDouble(ingreso);

        }catch(NumberFormatException ex1){
            System.out.println("La cantidad introducida no es un número");
        } 
             
        movimientos.add(new Cuentas(cuent,ingreso));

        try {
            
            for(Cuentas operaciones:movimientos){
            bw1.write(operaciones.getNumCuenta()+ ", " + "+");
            bw1.write(operaciones.getSaldo());
            bw1.write("\n");
            }
            bw1.close();
        } catch (IOException ex) {
            System.out.println("ERROR :(");        
        }
            ficheroActual();
    }

    public static void retirar() throws IOException{
        opera="resta";
        Float retirar;
        String cuent;
        String retirada="";
        boolean bander=false;
        
        do{
         System.out.print("Introduce cuenta sobre la que desea operar: ");
         cuent=sc.next();
            for(int i=0;i<listado.size();i++){
                if(cuent.equals(listado.get(i).getNumCuenta())){
                    bander=true;
                    break;
                }else               
                    bander=false;
        }
        }while(bander==false);
        
        BufferedWriter bw1 = new BufferedWriter (new FileWriter("movimientos.dat"));
            try{
                System.out.print("Introduce cantidad a retirar: ");
                retirada=sc.next();
                retirar=Float.parseFloat(retirada);

            }catch(NumberFormatException ex1){
                System.out.println("La cantidad introducida no es un número");
            } 
                   
                movimientos.add(new Cuentas(cuent,retirada));

            try {
            
                for(Cuentas operaciones:movimientos){
                bw1.write(operaciones.getNumCuenta()+ ", " );
                bw1.write(operaciones.getSaldo());
                bw1.write("\n");
                }
                bw1.close();
            } catch (IOException ex) {
                System.out.println("ERROR :(");        
        }
            ficheroActual();     
    }

    public static void ficheroActual() throws IOException{
        String [] separado = null;
        double total;
        
        BufferedReader br = new BufferedReader (new FileReader("movimientos.dat"));
        BufferedWriter bw2 = new BufferedWriter (new FileWriter("historico_actual.dat"));
        try {
            
            while((cadena = br.readLine())!=null){
            separado = cadena.split(", ");
    
            for(int i=0;i<listado.size();i++){
                if(separado[0].equals(listado.get(i).getNumCuenta())&&opera.equals("suma")){
                    total=Float.parseFloat(listado.get(i).getSaldo())+Float.parseFloat(separado[1]);
                    listado.get(i).setSaldo(Double.toString(total));
                }else{
                    total=Float.parseFloat(listado.get(i).getSaldo())-Float.parseFloat(separado[1]);
                    listado.get(i).setSaldo(Double.toString(total));}
                }
                 
                  movimientos.clear();
            }     
            
            for(Cuentas actual:listado){
                bw2.write(actual.getNumCuenta()+ ", ");
                bw2.write(actual.getSaldo());
                bw2.write("\n");
            }
            br.close();
            bw2.close();
            
        }catch (IOException ex) {
            System.out.println("ERROR :(");   
            }

    }
    public static void consultar(){
        for(Cuentas prueba:listado){
            System.out.println("\nNúmero de cuenta: " + prueba.getNumCuenta());
            System.out.println("Saldo: " + prueba.getSaldo());
        }    
    }
    public static int menu(){
        int opcion;
        System.out.println("\nMenú banco:");
        System.out.println("[1] Crear cuenta");
        System.out.println("[2] Ingresar dinero");
        System.out.println("[3] Sacar dinero");
        System.out.println("[4] Consultar histórico actual");
        System.out.println("[5] Fin");
        System.out.print("Elige opción: ");
        return opcion=sc.nextInt();      
    }
    
    public static void main(String[] args) throws IOException {
        int op;
        
        do{
        op=menu();
        
        switch (op){
            case 1: agregarCuenta();
            break;
            case 2: ingresar();
            break;
            case 3: retirar();
            break;
            case 4: consultar();
            break;
            case 5: 
                System.out.println("FIN");     
        }
        }while(op!=5);   
    }   
}

            

    
    

    

