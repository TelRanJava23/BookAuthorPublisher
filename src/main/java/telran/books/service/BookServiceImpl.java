package telran.books.service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import telran.books.dao.IRepository;
import telran.books.domain.Author;
import telran.books.domain.Book;
import telran.books.domain.Publisher;
import telran.books.dto.AuthorDto;
import telran.books.dto.BookDto;

@Service
public class BookServiceImpl implements BookService {
	
	@Autowired
	IRepository repository;

	@Override
	@Transactional
	public boolean addBook(BookDto bookDto) {
		if(repository.findBook(bookDto.getIsbn()) != null) {
			return false;
		}
		Publisher publisher = repository.findPublisher(bookDto.getPublisher());
		if (publisher == null) {
			publisher = new Publisher(bookDto.getPublisher());
			repository.addPublisher(publisher);
		}
		Set<AuthorDto> authorDtos = bookDto.getAuthors();
		Set<Author> authors = new HashSet<>();
		for (AuthorDto authorDto : authorDtos) {
			Author author = repository.findAuthor(authorDto.getName());
			if (author == null) {
				author = new Author(authorDto.getName(), authorDto.getBirthDate());
				repository.addAuthor(author);
			}
			authors.add(author);
		}
		return repository.addBook(new Book(bookDto.getIsbn(),
				bookDto.getTitle(), authors, publisher));
		
	}

	@Override
	@Transactional
	public BookDto removeBook(long isbn) {
		Book book = repository.removeBook(isbn);
		if (book == null) {
			return null;
		}
		return bookToBookDto(book);
	}

	@Override
	public BookDto getBookByIsbn(long isbn) {
		Book book = repository.findBook(isbn);
		BookDto bookDto = bookToBookDto(book);
		return bookDto;
	}

	private BookDto bookToBookDto(Book book) {
		Set<AuthorDto> authors = book.getAuthors().stream()
				.map(a -> new AuthorDto(a.getName(), a.getBirthDate()))
				.collect(Collectors.toSet());
		BookDto bookDto = new BookDto(book.getIsbn(), book.getTitle(),
				authors, book.getPublisher().getPublisherName());
		return bookDto;
	}

	@Override
	public Iterable<BookDto> getBooksByAuthor(String authorName) {
		Author author = repository.findAuthor(authorName);
		if (author == null) {
			return null;
		}
		Set<Book> books = author.getBooks();
		return books.stream()
				.map(this::bookToBookDto)
				.collect(Collectors.toSet());
	}

	@Override
	public Iterable<BookDto> getBooksByPublisher(String publisherName) {
		Publisher publisher = repository.findPublisher(publisherName);
		if (publisher == null) {
			return null;
		}
		Set<Book> books = publisher.getBooks();
		return books.stream()
				.map(this::bookToBookDto)
				.collect(Collectors.toSet());
	}

	@Override
	public Iterable<AuthorDto> getBookAuthors(long isbn) {
		Book book = repository.findBook(isbn);
		if (book == null) {
			return null;
		}
		Set<Author> authors = book.getAuthors();
		
		return authors.stream()
				.map(a -> new AuthorDto(a.getName(), a.getBirthDate()))
				.collect(Collectors.toSet());
	}

	@Override
	public Iterable<String> getPublishersByAuthor(String authorName) {
		// TODO Auto-generated method stub
		return null;
	}

}
