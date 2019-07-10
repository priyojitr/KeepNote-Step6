package com.stackroute.keepnote.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.stackroute.keepnote.exception.ReminderNotCreatedException;
import com.stackroute.keepnote.exception.ReminderNotFoundException;
import com.stackroute.keepnote.model.Reminder;
import com.stackroute.keepnote.repository.ReminderRepository;

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
public class ReminderServiceImpl implements ReminderService {

	/*
	 * Autowiring should be implemented for the ReminderRepository. (Use
	 * Constructor-based autowiring) Please note that we should not create any
	 * object using the new keyword.
	 */

	private final ReminderRepository reminderRepository;

	public ReminderServiceImpl(ReminderRepository reminderRepository) {
		this.reminderRepository = reminderRepository;
	}

	/*
	 * This method should be used to save a new reminder.Call the corresponding
	 * method of Respository interface.
	 */
	public Reminder createReminder(Reminder reminder) throws ReminderNotCreatedException {

		Reminder reminderCreated = this.reminderRepository.insert(reminder);
		if (null != reminderCreated) {
			return reminderCreated;
		} else {
			throw new ReminderNotCreatedException("reminder not created exception");
		}
	}

	/*
	 * This method should be used to delete an existing reminder.Call the
	 * corresponding method of Respository interface.
	 */
	public boolean deleteReminder(String reminderId) throws ReminderNotFoundException {
		try {
			Reminder reminder = Optional.ofNullable(this.reminderRepository.findById(reminderId)).get()
					.orElseThrow(() -> new ReminderNotFoundException("reminder not found exception"));
			if (null != reminder) {
				this.reminderRepository.delete(reminder);
				return Boolean.TRUE;
			}
		} catch (Exception e) {
			throw new ReminderNotFoundException(e.getMessage());
		}
		return Boolean.FALSE;
	}

	/*
	 * This method should be used to update a existing reminder.Call the
	 * corresponding method of Respository interface.
	 */
	public Reminder updateReminder(Reminder reminder, String reminderId) throws ReminderNotFoundException {
		try {
			Optional.ofNullable(this.reminderRepository.findById(reminderId)).get()
					.orElseThrow(() -> new ReminderNotFoundException("reminder not found exception"));
			this.reminderRepository.save(reminder);
			return this.reminderRepository.findById(reminderId).get();
		} catch (Exception e) {
			throw new ReminderNotFoundException(e.getMessage());
		}
	}

	/*
	 * This method should be used to get a reminder by reminderId.Call the
	 * corresponding method of Respository interface.
	 */
	public Reminder getReminderById(String reminderId) throws ReminderNotFoundException {
		try {
			Optional<Reminder> reminder = Optional.ofNullable(this.reminderRepository.findById(reminderId)).get();
			if (reminder.isPresent()) {
				return reminder.get();
			} else {
				throw new ReminderNotFoundException("reminder not found exception");
			}
		} catch (Exception e) {
			throw new ReminderNotFoundException(e.getMessage());
		}
	}

	/*
	 * This method should be used to get all reminders. Call the corresponding
	 * method of Respository interface.
	 */

	public List<Reminder> getAllReminders() {

		return this.reminderRepository.findAll();
	}

}
