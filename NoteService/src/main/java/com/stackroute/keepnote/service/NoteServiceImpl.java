package com.stackroute.keepnote.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stackroute.keepnote.exception.NoteNotFoundExeption;
import com.stackroute.keepnote.model.Note;
import com.stackroute.keepnote.model.NoteUser;
import com.stackroute.keepnote.repository.NoteRepository;

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
public class NoteServiceImpl implements NoteService {

	/*
	 * Autowiring should be implemented for the NoteRepository and MongoOperation.
	 * (Use Constructor-based autowiring) Please note that we should not create any
	 * object using the new keyword.
	 */

	private final NoteRepository noteRepository;

	@Autowired
	public NoteServiceImpl(NoteRepository noteRepository) {
		this.noteRepository = noteRepository;
	}

	/*
	 * This method should be used to save a new note.
	 */
	public boolean createNote(Note note) {

		NoteUser noteUser = new NoteUser();
		noteUser.setUserId(note.getNoteCreatedBy());
		noteUser.setNotes(Arrays.asList(note));
		if (null != this.noteRepository.insert(noteUser)) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	/* This method should be used to delete an existing note. */

	public boolean deleteNote(String userId, int noteId) {
		NoteUser noteUser = this.noteRepository.findById(userId).get();
		// filtering notes where id do not match with supplied note id
		List<Note> filteredNotes = noteUser.getNotes().stream().filter(currNote -> currNote.getNoteId() != noteId)
				.collect(Collectors.toList());
		noteUser.setNotes(filteredNotes);
		if (null != this.noteRepository.save(noteUser)) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}

	}

	/* This method should be used to delete all notes with specific userId. */

	public boolean deleteAllNotes(String userId) throws NoteNotFoundExeption {
		boolean flag = Boolean.FALSE;
		try {
			Optional<NoteUser> notes = Optional.ofNullable(this.noteRepository.findById(userId)).get();
			if (notes.isPresent()) {
				this.noteRepository.delete(notes.get());
				flag = Boolean.TRUE;
			} else {
				throw new NoteNotFoundExeption("note not found exception");
			}
		} catch (Exception e) {
			throw new NoteNotFoundExeption(e.getMessage());
		}
		return flag;
	}

	/*
	 * This method should be used to update a existing note.
	 */
	public Note updateNote(Note note, int id, String userId) throws NoteNotFoundExeption {
		try {
			Optional<NoteUser> noteUser = Optional.ofNullable(this.noteRepository.findById(userId)).get();
			if (noteUser.isPresent()) {
				// get all notes of the user, except specified not id
				List<Note> updatedNoteList = noteUser.get().getNotes().stream()
						.filter(currNote -> currNote.getNoteId() != id).collect(Collectors.toList());
				updatedNoteList.add(note);
				noteUser.get().setNotes(updatedNoteList);
				this.noteRepository.save(noteUser.get());
				return note;
			} else {
				throw new NoteNotFoundExeption("note not found exception");
			}
		} catch (Exception e) {
			throw new NoteNotFoundExeption(e.getMessage());
		}
	}

	/*
	 * This method should be used to get a note by noteId created by specific user
	 */
	public Note getNoteByNoteId(String userId, int noteId) throws NoteNotFoundExeption {
		try {
			Optional<NoteUser> noteUser = Optional.ofNullable(this.noteRepository.findById(userId)).get();
			if (noteUser.isPresent()) {
				Optional<Note> noteOptional = noteUser.get().getNotes().stream()
						.filter(currNote -> currNote.getNoteId() == noteId).findFirst();
				if (noteOptional.isPresent()) {
					return noteOptional.get();
				} else {
					throw new NoteNotFoundExeption("note not found exception");
				}
			} else {
				throw new NoteNotFoundExeption("note not found exception -- userid");
			}
		} catch (Exception e) {
			throw new NoteNotFoundExeption(e.getMessage());
		}
	}

	/*
	 * This method should be used to get all notes with specific userId.
	 */
	public List<Note> getAllNoteByUserId(String userId) {
		Optional<NoteUser> noteUser = Optional.ofNullable(this.noteRepository.findById(userId)).get();
		return noteUser.get().getNotes();
	}

}
