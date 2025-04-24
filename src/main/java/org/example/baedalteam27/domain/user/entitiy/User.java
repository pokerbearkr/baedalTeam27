package org.example.baedalteam27.domain.user.entitiy;

import java.time.LocalDateTime;

import org.example.baedalteam27.domain.user.UserRole;
import org.example.baedalteam27.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users") // H2 예약어 방지용
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
	private UserRole role;      // 역할 (USER, OWNER, ADMIN)

	private String provider;     // "kakao", "naver" 등
	private String providerId;   // 소셜 ID

	private Boolean isDeleted = false;

}
