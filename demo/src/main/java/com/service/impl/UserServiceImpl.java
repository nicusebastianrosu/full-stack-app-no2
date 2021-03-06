package com.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.model.User;
import com.repository.UserRepository;
import com.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

//	public static List<User> usersList = new ArrayList<>();
//
//	private static Long COUNTER = 1L;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

//	static {
//		User user = new User(COUNTER++, "Georgina", "Ortega", 24, "Brazil");
//		usersList.add(user);
//		user = new User(COUNTER++, "Rosa", "Sparks", 34, "Mexic");
//		usersList.add(user);
//		user = new User(COUNTER++, "Orla", "Mccoy", 19, "USA");
//		usersList.add(user);
//		user = new User(COUNTER++, "Jerry", "Hanna", 42, "CANADA");
//		usersList.add(user);
//		user = new User(COUNTER++, "Nicu", "Rosu", 54, "ROMANIA");
//		usersList.add(user);
//	}

//	@Override
//	public List<User> findAll() {
//
//		return usersList.stream().sorted(Comparator.comparing(User::getId)).collect(Collectors.toList());
//	}

	@Override
	public Page<User> findAll(Pageable pageable) {

		return userRepository.findAll(pageable);
	}

//	@Override
//	public Optional<User> findById(Long id) {
//
//		return usersList.stream().filter(user -> user.getId() == id).findFirst();
//
//		// Optional<User> userOpt = usersList.stream().filter(user -> user.getId() ==
//		// id).findFirst();
////		if (userOpt.isEmpty()) {
////			return userOpt;
////		}
////		return Optional.empty();
//		// return userOpt;
//	}

	@Override
	public Optional<User> findById(Long id) {

		return userRepository.findById(id);

	}

//	@Override
//	public void add(User user) {
//		user.setId(COUNTER++);
//		usersList.add(user);
//	}

	@Override
	public void add(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);
	}

//	@Override
//	public Optional<User> update(User user) {
//		Optional<User> userOpt = usersList.stream().filter(u -> u.getId() == user.getId()).findFirst();
//		if (userOpt.isPresent()) {
//			User existingUser = userOpt.get();
//			if (user.getFirstName() != null) {
//				existingUser.setFirstName(user.getFirstName());
//			}
//			if (user.getLastName() != null) {
//				existingUser.setLastName(user.getLastName());
//			}
//
//			if (user.getAge() != null) {
//				existingUser.setAge(user.getAge());
//			}
//
//			if (user.getCountry() != null) {
//				existingUser.setCountry(user.getCountry());
//			}
//			usersList = usersList.stream().filter(u -> u.getId() != existingUser.getId()).collect(Collectors.toList());
//			usersList.add(existingUser);
//			return Optional.of(existingUser);
//		}
//
//		return Optional.empty();
//
//	}

	@Override
	public Optional<User> update(User user) {
		Optional<User> userOpt = userRepository.findById(user.getId());
		if (userOpt.isPresent()) {
			User existingUser = userOpt.get();

			if (user.getUsername() != null) {
				existingUser.setUsername(user.getUsername());
			}

			if (user.getPassword() != null) {
				existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
			}

			if (user.getFirstName() != null) {
				existingUser.setFirstName(user.getFirstName());
			}
			if (user.getLastName() != null) {
				existingUser.setLastName(user.getLastName());
			}

			if (user.getAge() != null) {
				existingUser.setAge(user.getAge());
			}

			if (user.getCountry() != null) {
				existingUser.setCountry(user.getCountry());
			}
			userRepository.save(existingUser);

			return Optional.of(existingUser);
		}

		return Optional.empty();

	}

//	@Override
//	public Optional<User> delete(Long id) {
//		Optional<User> userOpt = usersList.stream().filter(user -> user.getId() == id).findFirst();
//		if (userOpt.isPresent()) {
//			usersList = usersList.stream().filter(user -> userOpt.get().getId() != user.getId())
//					.collect(Collectors.toList());
//			return userOpt;
//		}
//
//		return Optional.empty();
//	}

	@Override
	public Optional<User> delete(Long id) {
		Optional<User> userOpt = userRepository.findById(id);
		if (userOpt.isPresent()) {
			userRepository.delete(userOpt.get());
			return userOpt;
		}

		return Optional.empty();
	}

	@Override
	public List<User> findByCriteria(String criteria, String searchItem) {

		switch (criteria) {
		case "username":
			return this.userRepository.findByUsername(searchItem);
		case "firstName":
			return this.userRepository.findByFirstName(searchItem);
		case "lastName":
			return this.userRepository.findByLastName(searchItem);
		case "age":
			try {
				Integer age = Integer.valueOf(searchItem);
				return this.userRepository.findByAge(age);
			} catch (NumberFormatException e) {
				System.out.println("Could not convert age to number...");
			}
			return new ArrayList<>();
		case "country":
			return this.userRepository.findByCountry(searchItem);
		}
		return new ArrayList<>();
	}

}
