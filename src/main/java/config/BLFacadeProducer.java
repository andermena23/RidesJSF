package config;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;
import dataAccess.DataAccess;

@ApplicationScoped
public class BLFacadeProducer {
	
	private DataAccess dataAccess;
	
	public BLFacadeProducer() {
		System.out.println("[DEBUG] BLFacadeProducer - Constructor called");
	}
	
	@PostConstruct
	public void init() {
		System.out.println("[DEBUG] BLFacadeProducer - @PostConstruct init() called");
		dataAccess = new DataAccess();
		System.out.println("[DEBUG] BLFacadeProducer - DataAccess created: " + dataAccess.getClass().getName());
	}
	
	@Produces
	@Named("businessLogic")
	@RequestScoped
	public BLFacade produceBLFacade() {
		System.out.println("[DEBUG] BLFacadeProducer - produceBLFacade() called - creating NEW BLFacade for this request");
		BLFacade facade = new BLFacadeImplementation(dataAccess);
		System.out.println("[DEBUG] BLFacadeProducer - BLFacade instance created: " + facade.getClass().getName());
		return facade;
	}
}
