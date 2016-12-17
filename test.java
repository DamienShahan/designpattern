package Client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;

import java.util.*;

import AbstractFactory;

import share.CompanyManagerInterface;
import share.DepartmentInterface;
import share.EmployeeInterface;
/**
*  @author Damien
**/

public class CompanyFassade {
    
    Registry registry;
    CompanyManagerInterface companyManagerInterface;
    private static AbstractFactory af = null;
    // Konstruktor
    private CompanyFassade(){
        // Teste ob eine Verbindung zum Server aufgebaut werden kann
        // Der Code ist fehleranfällig, da entweder eine Verbindung aufgebaut werden kann oder nicht (Fehler).
        try {
            // Versuche Verbindung zu Server aufzubauen
            // Registry vom Server abrufen
            this.registry = LocateRegistry.getRegistry("localhost", 1099);
            
            // Prüfen ob CompanyManager im Registry eingetragen ist
            this.companyManagerInterface = (CompanyManagerInterface) this.registry.lookup("CompanyManager");
            
            // Führe online Methode aus Version 1
            //online();
            
            // Führe online Methode aus Version 2
            af = FactoryMaker.getFactory("online");
            
            // Inhalt
            this.companyManagerInterface.getEmployee(empNo);
            this.companyManagerInterface.addEmployee(empNo, empFirstName, empLastName, empBirthday, empBirthplace:String, empDescription, empSalary);
            this.companyManagerInterface.getAllEmployees();
            this.companyManagerInterface.getDepartment(deptNo);
            this.companyManagerInterface.addDepartment(deptNo, deptName, deptDescription, deptLocation, deptStreet, deptPostalcode);
            this.companyManagerInterface.getAllDepartments();
            this.companyManagerInterface.linkEmployeesDepartments(employeeInterface, departmentInterface);
        } 
        // Falls keine Verbindung aufgebaut werden kann, wird folgendes Code ausgeführt
        catch (NotBoundException e) {
            //e.printStackTrace();
            
            // Führe online Methode aus Version 1
            //offline();
            
            // Führe online Methode aus Version 2
            af = FactoryMaker.getFactory("offline");
        }
    }
    
    // Singleton Pattern   
    public static CompanyFassade getInstance() throws RemoteException {
        if (companyFassade == null) {
            companyFassade instance = new CompanyFassade();
            return companyFassade;
        }
        else {
            return companyFassade;
        }
    }
    // AbstractFactory-Objekte Version 1
    //public void online() {
    //    af = AbstractFactory.getFactory(AbstractFactory.FactoryOn);
    //}
    //public void offline() {
    //    af = AbstractFactory.getFactory(AbstractFactory.FactoryOff);
    //    Employee emp = af.createEmp();
    //    Department dept = af.createDept();       
    //} 
    
    
    // AbstractFactory-Objekte Version 2
    class FactoryMaker{
        static AbstractFactory getFactory(String choice){
            if(choice.equals("online")){
                af = new FactoryOn();
            }
            else if(choice.equals("offline")){
                af = new FactoryOff();
            } 
            return af;
        }
    }
    
    //  Von Mandy
    //- zwei AbstracteFactory-Objekte implementiert, einer für online einer für 
    //  offline ( nur das Gerüst, noch keine Instanziierung) 
    //- innerhalb des Konstruktors erfolgt die Instanziierung der AbstractFactory- 
    //  Objekte und es wird überprüft, ob eine Verbindung zum Server 
    //  möglich ist (Abfrage aller Emps und Depts über die online-Factory), ist keine 
    //  Verbindung möglich wird die Offline-Factory verwendet. 
    //- Zudem wird hier die Kommunikation mit den Controllern implementiert (z.B. 
    //  addEmp(), getEmp(), getAllEmps(), linkEmpDep()) 
}
