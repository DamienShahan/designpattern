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
    
    //Registry registry;
    //CompanyManagerInterface companyManagerInterface;
    
    //satisches Objekt der CompanyFassade
    private static CompanyFacade companyFassade = null;
    
   	//Online- und Offline-Factory zur Verfuegung stellen, damit bei Serverausfall zur Laufzeit ungeschaltet werden kann
	private AbstractFactory onlineFact;
	private AbstractFactory offlineFact;
    
    //Observable-Lists fÃ¼r alle IEmps- und IDept-Objekte
	private ObservableList<DepartmentInterface> depts = FXCollections.observableArrayList();
	private ObservableList<EmployeeInterface> emps = FXCollections.observableArrayList();

    // Konstruktor
    private CompanyFassade() throws AccessException, RemoteException, NotBoundException
    {
        //TODO Factory's anpassen
        this.onlineFact = new FactoryOnline();
		this.offlineFact = new FactoryOffline();
        // Teste ob eine Verbindung zum Server aufgebaut werden kann
        // Der Code ist fehleranfällig, da entweder eine Verbindung aufgebaut werden kann oder nicht (Fehler).
        try 
        {
            emps.addAll(onlineFact.getAllEmps()); 
            depts.addAll(onlineFact.getAllDepts());
			System.out.println("Online-Modus: Elemente aus DB lesen");
        } 
        // Falls keine Verbindung aufgebaut werden kann, wird folgendes Code ausgeführt
        catch (RemoteException e)
        {
             System.out.println("Exeption offline getAllDepts");
			depts.addAll(offlineFact.getAllDepts());
			emps.addAll(offlineFact.getAllEmps());
			System.out.println("Offline-Modus: Dummy-Elemente einlesen, da kein DB-Zugriff");
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
 //view->controller->fassade-> factory->companyinterface
//------------------------  Methoden für den Controller-Zugriff 
    
    //gibt gesamte IDept-Liste zurueck
	public ObservableList<IDept> getDepts()
	{
		return depts;
	}
	
    public IDept getDept(int deptno) {}
    
    public void addDept(String name, String beschreibung,String anschrift, String postleitzahl, String standort) {
    // try/Catch idept =onlineFact.createDept(deptnumber, name, beschreibung, anschrift, postleitzahl, standort)
        depts.add(idept);
    }
    //same Emp
    
    public void linkEmpDept(IEmp iemp, IDept idept){
    //try/Catch onlineFact.linkEmpDept(idept, iemp);
    }
}
