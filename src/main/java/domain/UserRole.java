package domain;

/**
 * Enum representing the different user roles in the system.
 */
public enum UserRole {
	ADMIN("admin"),
	DRIVER("driver"),
	TRAVELER("traveler");
	
	private final String roleName;
	
	UserRole(String roleName) {
		this.roleName = roleName;
	}
	
	public String getRoleName() {
		return roleName;
	}
	
	@Override
	public String toString() {
		return roleName;
	}
	
	/**
	 * Get UserRole from string value
	 * @param role the role string
	 * @return the corresponding UserRole or TRAVELER as default
	 */
	public static UserRole fromString(String role) {
		if (role == null) {
			return TRAVELER;
		}
		
		String roleLower = role.toLowerCase();
		switch (roleLower) {
			case "admin":
				return ADMIN;
			case "driver":
				return DRIVER;
			case "traveler":
			case "passenger":
				return TRAVELER;
			default:
				return TRAVELER;
		}
	}
}
