package config;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;
import dataAccess.HibernateDataAccess;

@ApplicationScoped
public class BLFacadeProducer {
	
	public BLFacadeProducer() {
		System.out.println("[DEBUG] BLFacadeProducer - Constructor called");
	}
	
	@PostConstruct
	public void initializeDatabase() {
		System.out.println("[DEBUG] BLFacadeProducer - @PostConstruct initializeDatabase() called");
		try {
			HibernateDataAccess dataAccess = new HibernateDataAccess();
			BLFacade facade = new BLFacadeImplementation(dataAccess);
			facade.initializeBD();
			System.out.println("[DEBUG] BLFacadeProducer - Database initialized successfully");
		} catch (Exception e) {
			System.err.println("[ERROR] BLFacadeProducer - Error initializing database: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Produces
	@Named("businessLogic")
	@ApplicationScoped
	public BLFacade produceBLFacade() {
		System.out.println("[DEBUG] BLFacadeProducer - produceBLFacade() called - creating NEW BLFacade for this request");
		HibernateDataAccess dataAccess = new HibernateDataAccess();
		BLFacade facade = new BLFacadeImplementation(dataAccess);
		System.out.println("[DEBUG] BLFacadeProducer - BLFacade instance created: " + facade.getClass().getName());
		return facade;
	}
}
