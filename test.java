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
        this.onlineFact = new FactoryOn();
	this.offlineFact = new FactoryOff();
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
    //gibt gesamte IEmp-Liste zurueck
    public ObservableList<IEmp> getEmp()
    {
	return emps;
    }
	
    public IDept getDept(int deptNo) {}
    
    public IEmp getEmp(int empNo) {}
    
    public void addDept(String name, String beschreibung,String anschrift, String postleitzahl, String standort) {
    // try/Catch idept =onlineFact.createDept(deptnumber, name, beschreibung, anschrift, postleitzahl, standort)
	try {
	    idept = onlineFact.createDept(deptnumber, name, beschreibung, anschrift, postleitzahl, standort);
            depts.add(idept);
	}
	catch (RemoteException e)
        {
	    idept = offlineFact.createDept(deptnumber, name, beschreibung, anschrift, postleitzahl, standort);
            depts.add(idept);
	}	   
    }
	
    public void addEmp(String name, String beschreibung, String anschrift, String postleitzahl, String standort) {
    // try/Catch idept =onlineFact.createDept(deptnumber, name, beschreibung, anschrift, postleitzahl, standort)
	try {
	    iemp = onlineFact.createEmp(name, beschreibung, anschrift, postleitzahl, standort);
            emps.add(iemp);
	}
	catch (RemoteException e)
        {
	    iemp = offlineFact.createEmp(name, beschreibung, anschrift, postleitzahl, standort);
            emps.add(iemp);
	}
    }
    
    public void linkEmpDept(IEmp iemp, IDept idept){
    //try/Catch onlineFact.linkEmpDept(idept, iemp);
	try {
	    onlineFact.linkEmpDept(idept, iemp);
	}
	catch (RemoteException e)
        {
	    offlineFact.linkEmpDept(idept, iemp);
	}
    }
}
