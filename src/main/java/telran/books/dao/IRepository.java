package telran.books.dao;

import java.util.List;

import telran.books.domain.Author;
import telran.books.domain.Book;
import telran.books.domain.Publisher;

public interface IRepository {
	
	boolean addBook(Book book);
	
	boolean addAuthor(Author author);
	
	boolean addPublisher(Publisher publisher);
	
	Book removeBook(long isbn);
	
	Book findBook(long isbn);
	
	Author findAuthor(String authorName);
	
	Publisher findPublisher(String publisherName);
	
	List<Publisher> getPublishersByAuthor(String authorName);

}


