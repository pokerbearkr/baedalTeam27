package org.example.baedalteam27.domain.user.entitiy;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.baedalteam27.domain.user.UserRole;
import org.example.baedalteam27.global.entity.BaseEntity;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 30)
	private String email;

	@Column(nullable = false, length = 60)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserRole role;

	private String provider;

	private String providerId;

	private Boolean isDeleted = false;

	@Builder
	public User(String email, String password, UserRole role, String provider, String providerId) {
		this.email = email;
		this.password = password;
		this.role = role;
		this.provider = provider;
		this.providerId = providerId;
		this.isDeleted = false;
	}

	public void changePassword(String encodedPassword) {
		this.password = encodedPassword;
	}

	public void withdraw() {
		this.isDeleted = true;
	}
}
