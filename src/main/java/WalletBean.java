import java.io.IOException;
import java.io.Serializable;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import businessLogic.BLFacade;
import domain.Traveler;
import domain.User;

@Named("wallet")
@ViewScoped
public class WalletBean implements Serializable {

	static {
		System.out.println("[DEBUG] ==================== WalletBean CLASS LOADED ====================");
	}

	@Inject
	@Named("businessLogic")
	private BLFacade facadeBL;
	
	@Inject
	@Named("login")
	private LoginBean loginBean;
	
	private static final long serialVersionUID = 1L;
	
	private double currentBalance = 0.0;
	private double depositAmount = 0.0;
	private double withdrawAmount = 0.0;
	private String message;
	private String messageType; // "success" or "error"
	
	public WalletBean() {
		System.out.println("[DEBUG] WalletBean - Constructor called");
	}
	
	@PostConstruct
	public void init() {
		System.out.println("[DEBUG] WalletBean - PostConstruct init called");
		checkAuthentication();
		loadBalance();
	}
	
	public void checkAuthentication() {
		if (!loginBean.isLoggedIn()) {
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("Login.xhtml");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		
		// Check if the user is a traveler
		User user = loginBean.getCurrentUser();
		if (!(user instanceof Traveler)) {
			try {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
					"Access Denied", "Only travelers can access the wallet."));
				context.getExternalContext().redirect("Index.xhtml");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void loadBalance() {
		if (loginBean.isLoggedIn() && loginBean.getCurrentUser() instanceof Traveler) {
			String username = loginBean.getCurrentUser().getUsername();
			currentBalance = facadeBL.getWalletBalance(username);
			System.out.println("[DEBUG] WalletBean - Loaded balance: " + currentBalance + " for user: " + username);
		}
	}
	
	public void deposit() {
		System.out.println("[DEBUG] WalletBean - deposit called with amount: " + depositAmount);
		
		if (depositAmount <= 0) {
			message = "Deposit amount must be greater than 0";
			messageType = "error";
			System.out.println("[DEBUG] WalletBean - Invalid deposit amount");
			return;
		}
		
		String username = loginBean.getCurrentUser().getUsername();
		boolean success = facadeBL.depositMoney(username, depositAmount);
		
		if (success) {
			loadBalance();
			message = "Successfully deposited " + String.format("%.2f", depositAmount) + " euros";
			messageType = "success";
			depositAmount = 0.0;
			System.out.println("[DEBUG] WalletBean - Deposit successful");
		} else {
			message = "Failed to deposit money. Please try again.";
			messageType = "error";
			System.out.println("[DEBUG] WalletBean - Deposit failed");
		}
	}
	
	public void withdraw() {
		System.out.println("[DEBUG] WalletBean - withdraw called with amount: " + withdrawAmount);
		
		if (withdrawAmount <= 0) {
			message = "Withdrawal amount must be greater than 0";
			messageType = "error";
			System.out.println("[DEBUG] WalletBean - Invalid withdrawal amount");
			return;
		}
		
		if (withdrawAmount > currentBalance) {
			message = "Insufficient funds. Current balance: " + String.format("%.2f", currentBalance) + " euros";
			messageType = "error";
			System.out.println("[DEBUG] WalletBean - Insufficient funds");
			return;
		}
		
		String username = loginBean.getCurrentUser().getUsername();
		boolean success = facadeBL.withdrawMoney(username, withdrawAmount);
		
		if (success) {
			loadBalance();
			message = "Successfully withdrew " + String.format("%.2f", withdrawAmount) + " euros";
			messageType = "success";
			withdrawAmount = 0.0;
			System.out.println("[DEBUG] WalletBean - Withdrawal successful");
		} else {
			message = "Failed to withdraw money. Please try again.";
			messageType = "error";
			System.out.println("[DEBUG] WalletBean - Withdrawal failed");
		}
	}
	
	public String goToIndex() {
		return "Index?faces-redirect=true";
	}
	
	// Getters and Setters
	
	public double getCurrentBalance() {
		return currentBalance;
	}
	
	public void setCurrentBalance(double currentBalance) {
		this.currentBalance = currentBalance;
	}
	
	public double getDepositAmount() {
		return depositAmount;
	}
	
	public void setDepositAmount(double depositAmount) {
		this.depositAmount = depositAmount;
	}
	
	public double getWithdrawAmount() {
		return withdrawAmount;
	}
	
	public void setWithdrawAmount(double withdrawAmount) {
		this.withdrawAmount = withdrawAmount;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessageType() {
		return messageType;
	}
	
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
}
