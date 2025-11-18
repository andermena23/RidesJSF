package config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;
import dataAccess.DataAccess;

@ApplicationScoped
public class BLFacadeProducer {
	
	static {
		System.out.println("[DEBUG] ==================== BLFacadeProducer CLASS LOADED ====================");
	}
	
	private BLFacade facadeInstance;
	
	public BLFacadeProducer() {
		System.out.println("[DEBUG] BLFacadeProducer - Constructor called");
	}
	
	@Produces
	@Named("businessLogic")
	@ApplicationScoped
	public BLFacade produceBLFacade() {
		System.out.println("[DEBUG] BLFacadeProducer - produceBLFacade() called");
		if (facadeInstance == null) {
			System.out.println("[DEBUG] BLFacadeProducer - Creating new BLFacade instance with DataAccess");
			DataAccess dataAccess = new DataAccess();
			System.out.println("[DEBUG] BLFacadeProducer - DataAccess created, checking database path...");
			System.out.println("[DEBUG] BLFacadeProducer - DataAccess instance: " + dataAccess.getClass().getName());
			facadeInstance = new BLFacadeImplementation(dataAccess);
			System.out.println("[DEBUG] BLFacadeProducer - BLFacade instance created: " + facadeInstance.getClass().getName());
		}
		return facadeInstance;
	}
}
