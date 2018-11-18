package telran.books.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import telran.books.domain.Author;
import telran.books.domain.Book;
import telran.books.domain.Publisher;

@Repository
public class RepositoryImpl implements IRepository {
	
	@PersistenceContext
	EntityManager em;

	@Override
	public boolean addBook(Book book) {
		if(em.find(Book.class, book.getIsbn()) != null) {
			return false;
		}
		em.persist(book);
		return true;
	}

	@Override
	public boolean addAuthor(Author author) {
		if(em.find(Author.class, author.getName()) != null) {
			return false;
		}
		em.persist(author);
		return true;
	}

	@Override
	public boolean addPublisher(Publisher publisher) {
		if(em.find(Publisher.class, publisher.getPublisherName()) != null) {
			return false;
		}
		em.persist(publisher);
		return true;
	}

	@Override
	public Book removeBook(long isbn) {
		Book book = em.find(Book.class, isbn);
		if(book != null) {
			em.remove(book);
		}
		return book;
	}

	@Override
	public Book findBook(long isbn) {
		return em.find(Book.class, isbn);
	}

	@Override
	public Author findAuthor(String authorName) {
		return em.find(Author.class, authorName);
	}

	@Override
	public Publisher findPublisher(String publisherName) {
		return em.find(Publisher.class, publisherName);
	}

	@Override
	public List<Publisher> getPublishersByAuthor(String authorName) {
		TypedQuery<Publisher> query = 
				em.createQuery("select p from Book b join b.authors a "
						+ "join b.publisher p where a.name = ?1",Publisher.class);
		query.setParameter(1, authorName);
		return query.getResultList();
	}

}







