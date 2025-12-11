import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import businessLogic.BLFacade;
import domain.Admin;
import domain.User;

@Named("queryUsers")
@ViewScoped
public class QueryUsersBean implements Serializable {

	static {
		System.out.println("[DEBUG] ==================== QueryUsersBean CLASS LOADED ====================");
	}

	@Inject
	@Named("businessLogic")
	private BLFacade facadeBL;
	
	@Inject
	@Named("login")
	private LoginBean loginBean;
	
	private static final long serialVersionUID = 1L;
	
	private List<User> users = new ArrayList<>();
	
	public QueryUsersBean() {
		System.out.println("[DEBUG] QueryUsersBean - Constructor called");
	}
	
	@PostConstruct
	public void init() {
		System.out.println("[DEBUG] QueryUsersBean - PostConstruct init called");
		checkAuthentication();
		loadUsers();
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
		
		// Check if the user is an admin
		User user = loginBean.getCurrentUser();
		if (!(user instanceof Admin)) {
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("Index.xhtml");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void loadUsers() {
		System.out.println("[DEBUG] QueryUsersBean - loadUsers called");
		if (facadeBL != null) {
			users = facadeBL.getAllUsers();
			if (users == null) {
				users = new ArrayList<>();
			}
			System.out.println("[DEBUG] QueryUsersBean - Loaded " + users.size() + " users");
		}
	}
	
	public String goToIndex() {
		return "Index?faces-redirect=true";
	}
	
	public String getRoleDisplayName(User user) {
		String roleName = user.getRole().getRoleName();
		return roleName.substring(0, 1).toUpperCase() + roleName.substring(1).toLowerCase();
	}
	
	// Getters and Setters
	
	public List<User> getUsers() {
		return users;
	}
	
	public void setUsers(List<User> users) {
		this.users = users;
	}
}
