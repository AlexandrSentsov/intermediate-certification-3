import com.github.javafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.library.*;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static ru.library.GenerateUtils.*;
import static ru.library.SearchOperations.getBookByName;
import static ru.library.SearchOperations.getBookByNameAndAuthor;
import static ru.library.SortOperations.*;


public class BookTest {

    @Test
    public void shouldSetCategory() {

        Faker faker = new Faker();
        String bookName = faker.book().title();
        String bookAuthor = faker.book().author();
        String categoryName = faker.book().genre();

        final Book book = new Book(bookName, bookAuthor);
        book.setCategory(new Category(categoryName));

        Assertions.assertNotNull(book.getCategory());
        Assertions.assertEquals(book.getName(), bookName,
                "Ожидаемое и получанное имя книги не совпадают");
        Assertions.assertEquals(book.getAuthor(), bookAuthor,
                "Ожидаемое и получанное имя автора не совпадают");
        Assertions.assertEquals(book.getCategory().getName(), categoryName,
                "Ожидаемая и получанная категории не совпадают");
    }

    @Test
    public void shouldGetDate() {

        Faker faker = new Faker();
        String name = faker.book().title();
        Instant date = faker.date().past(20, TimeUnit.DAYS).toInstant();
        final Newspaper newspaper = new Newspaper(name, date);

        Assertions.assertEquals(name, newspaper.getName(), "Ожидаемое и полученное имя газеты не совпадает");
        Assertions.assertEquals(date, newspaper.getDate(), "Ожидаемая и полученная дата газеты не совпадает");
    }

    @Test
    public void shouldCheckSizeCreatedListBooks() {

        int count = new Random().nextInt(1,10);
        final List<Book> books = generateSomeBooks(count);

        Assertions.assertEquals(books.size(), count, "Ожидаемая и полученная длина списка книг не совпадают");
    }

    @Test
    public void shouldCheckSizeCreatedListNewsPapers() {

        int count = new Random().nextInt(1,10);
        List<Newspaper> newspapers = generateSomeNewsPapers(count);

        Assertions.assertEquals(newspapers.size(), count, "Ожидаемая и полученная длина списка газет не совпадают");
    }

    @Test
    public void shouldCheckGetBookByNameAndAuthor() {

        final int count = new Random().nextInt(1,10);
        final List<Book> books = generateSomeBooks(count);
        final int index = new Random().nextInt(0, count - 1);

        Assertions.assertEquals(getBookByNameAndAuthor(books.get(index).getName(), books.get(index).getAuthor(), books)
                .get().getName(), books.get(index).getName(), "Ожидаемое и полученное имя книги не совпадают");
        Assertions.assertEquals(getBookByNameAndAuthor(books.get(index).getName(), books.get(index).getAuthor(), books)
                .get().getAuthor(), books.get(index).getAuthor(), "Ожидаемое и полученное имя автора книги не совпадают");
    }

    @Test
    public void shouldCheckGetBookByName() {

        final int count = new Random().nextInt(1,10);
        final List<Book> books = generateSomeBooks(count);
        final int index = new Random().nextInt(0, count - 1);

        Assertions.assertEquals(getBookByName(books.get(index).getName(), books)
                .get().getName(), books.get(index).getName(), "Ожидаемое и полученное имя книги не совпадают");
    }

    @Test
    public void shouldCheckGetBooksCategoriesForBooks () {

        final int count = new Random().nextInt(1,10);
        final List<Book> books = generateSomeBooks(count);
        for (int i = 0; i <= count - 1; i++) {
            books.get(i).setCategory(new Category(faker.book().genre()));
        }
        for (int i = 0; i <= count - 1; i++) {
            Assertions.assertTrue(getBooksCategories(books).contains(books.get(i).getCategory()),
                    "Одна из книг не попала в массив категорий, а должна была");
        }
    }

    @Test
    public void shouldCheckGetBooksByCategory() {

        final int count = new Random().nextInt(1,10);
        final int index = new Random().nextInt(0, count - 1);
        final List<Book> books = generateSomeBooks(count);
        for (int i = 0; i <= count - 1; i++) {
            books.get(i).setCategory(new Category(faker.book().genre()));
        }

        Assertions.assertNotNull(getBooksByCategory(books, books.get(index).getCategory()),
                "Ни одной книги выбранной категории не найдено");
    }

    @Test
    public void shouldCheckGetBooksByCategoryNull() {

        final int count = new Random().nextInt(1,10);
        final List<Book> books = generateSomeBooks(count);
        for (int i = 0; i <= count - 1; i++) {
            books.get(i).setCategory(new Category(faker.book().genre()));
        }
        Category fakeCategory = new Category(faker.book().genre());

        Assertions.assertNull(getBooksByCategory(books, fakeCategory),
                "Ни одной книги выбранной категории не найдено");
    }

    @Test
    public void shouldCheckSortNewspapersByDate() {

        int count = new Random().nextInt(1, 10);
        List<Newspaper> newspapers = generateSomeNewsPapers(count);
        sortNewspapersByDate(newspapers);

        for (int i = 1; i <= newspapers.size() - 1; i++) {
            Assertions.assertTrue(newspapers.get(i - 1).getDate().isAfter(newspapers.get(i).getDate()),
                    "Список газет отсортированан по дате не верно!");
        }
    }

}
