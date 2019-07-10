package com.stackroute.keepnote.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.stackroute.keepnote.exceptions.UserAlreadyExistsException;
import com.stackroute.keepnote.exceptions.UserNotFoundException;
import com.stackroute.keepnote.model.User;
import com.stackroute.keepnote.repository.UserRepository;

/*
* Service classes are used here to implement additional business logic/validation 
* This class has to be annotated with @Service annotation.
* @Service - It is a specialization of the component annotation. It doesn't currently 
* provide any additional behavior over the @Component annotation, but it's a good idea 
* to use @Service over @Component in service-layer classes because it specifies intent 
* better. Additionally, tool support and additional behavior might rely on it in the 
* future.
* */

@Service
public class UserServiceImpl implements UserService {

	/*
	 * Autowiring should be implemented for the UserRepository. (Use
	 * Constructor-based autowiring) Please note that we should not create any
	 * object using the new keyword.
	 */

	private final UserRepository userRepository;

	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/*
	 * This method should be used to save a new user.Call the corresponding method
	 * of Respository interface.
	 */

	public User registerUser(User user) throws UserAlreadyExistsException {
		User userCreated = this.userRepository.insert(user);
		if (null == userCreated) {
			throw new UserAlreadyExistsException("user already exists exception");
		}
		return userCreated;
	}

	/*
	 * This method should be used to update a existing user.Call the corresponding
	 * method of Respository interface.
	 */

	public User updateUser(String userId, User user) throws UserNotFoundException {
		try {
			Optional.ofNullable(this.userRepository.findById(userId)).get()
					.orElseThrow(() -> new UserNotFoundException("user not found exception"));
			this.userRepository.save(user);
			return this.userRepository.findById(userId).get();
		} catch (Exception e) {
			throw new UserNotFoundException(e.getMessage());
		}
	}

	/*
	 * This method should be used to delete an existing user. Call the corresponding
	 * method of Respository interface.
	 */

	public boolean deleteUser(String userId) throws UserNotFoundException {
		try {
			User user = Optional.ofNullable(this.userRepository.findById(userId)).get()
					.orElseThrow(() -> new UserNotFoundException("user not found exception"));
			if (null != user) {
				this.userRepository.delete(user);
				return Boolean.TRUE;
			}
		} catch (Exception e) {
			throw new UserNotFoundException(e.getMessage());
		}
		return Boolean.FALSE;
	}

	/*
	 * This method should be used to get a user by userId.Call the corresponding
	 * method of Respository interface.
	 */

	public User getUserById(String userId) throws UserNotFoundException {
		try {
			Optional<User> user = Optional.ofNullable(this.userRepository.findById(userId)).get();
			if (user.isPresent()) {
				return user.get();
			} else {
				throw new UserNotFoundException("user not found exception");
			}
		} catch (Exception e) {
			throw new UserNotFoundException(e.getMessage());
		}
	}

}
