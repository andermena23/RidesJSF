package domain;

import javax.persistence.*;

/**
 * Abstract base class for all user types in the system.
 * Provides common functionality and JPA mappings for user entities.
 */
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
public abstract class AbstractUser implements User {

	private static final long serialVersionUID = 1L;
	
	@Id
	private String username;
	
	private String password;
	
	@Column(unique = true)
	private String email;
	
	public AbstractUser() {
		super();
	}
	
	public AbstractUser(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
	}
	
	@Override
	public String getUsername() {
		return username;
	}
	
	@Override
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Override
	public String getPassword() {
		return password;
	}
	
	@Override
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String getEmail() {
		return email;
	}
	
	@Override
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public abstract UserRole getRole();
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof User))
			return false;
		User other = (User) obj;
		if (username == null) {
			if (other.getUsername() != null)
				return false;
		} else if (!username.equals(other.getUsername()))
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		return username != null ? username.hashCode() : 0;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " [username=" + username + ", email=" + email + ", role=" + getRole().getRoleName() + "]";
	}
}
